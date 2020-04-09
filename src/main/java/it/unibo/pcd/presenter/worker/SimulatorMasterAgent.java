package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.World;
import it.unibo.pcd.presenter.worker.util.Chrono;
import it.unibo.pcd.presenter.worker.util.ResettableCountDownLatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimulatorMasterAgent extends Agent {
    private final List<Body> bodies;
    private final int nWorker;

    private final List<Semaphore> nextStep;
    private final List<SimulatorWorkerAgent> workersPools;
    private final ResettableCountDownLatch stepDone;
    private final CyclicBarrier barrier;
    private transient SimulatorContract.View mView;

    private AtomicBoolean isRunning = new AtomicBoolean();
    private boolean isStepByStep;
    private boolean nextStepFlag = true;

    private final Lock simulationLock =  new ReentrantLock();
    private final Condition pauseCond = simulationLock.newCondition();

    private final World world = World.getInstance();

    private final Chrono chrono = new Chrono();
    private double lastIterationTime = 0;

    public SimulatorMasterAgent(final List<Body> bodies, final int nWorker) {
        super("Master");
        this.bodies = bodies;
        this.nWorker = nWorker;

        workersPools = new ArrayList<>(nWorker);
        nextStep = new ArrayList<>(nWorker);
        stepDone = new ResettableCountDownLatch(nWorker);
        barrier = new CyclicBarrier(nWorker);
    }

    @Override
    public void run() {
        super.run();
        initWorkers();
        startWorkers();
        chrono.start();

        while (world.getCurrentIteration() < world.getIterationsNumber()) {
            if (mView != null) {
                try {
                    canGoOn();
                } catch (InterruptedException ex) {
                    log("interrupted");
                }

                doSimulationStep();
                mView.updateBodies(bodies);
                mView.updateIter(world.getCurrentIteration());
                mView.updateVt(world.getVirtualTime());

            } else {
                doSimulationStep();
            }
        }
        workersPools.forEach(SimulatorWorkerAgent::stopWorker); //Terminate all worker threads
        nextStep.forEach(Semaphore::release); // This prevent deadlock (thanks JPF :))
        chrono.stop();
        final long dt2 = chrono.getTime();
        final double timePerStep = ((double) dt2) / world.getIterationsNumber();
        lastIterationTime = timePerStep;
        super.log("Done " + world.getIterationsNumber() + " iter with " + bodies.size() + " bodies using "
                + nWorker + " workers in: " + dt2 + "ms");
        super.log("- " + timePerStep + " ms per step");
    }

    public void setView(final SimulatorContract.View mView) {
        this.mView = mView;
    }

    public void pauseSim() {
        isRunning.set(false);
    }

    public void resumeSim() {
        simulationLock.lock();
        isStepByStep = false;
        isRunning.set(true);

        pauseCond.signal();
        simulationLock.unlock();
    }

    public void stepSim() {
        simulationLock.lock();
        isStepByStep = true;
        isRunning.set(true);
        nextStepFlag = false;

        pauseCond.signal();
        simulationLock.unlock();
    }

    public double getLastIterationTime() {
        return lastIterationTime;
    }

    private void initWorkers() {
        final int chunkSize = (bodies.size() + nWorker - 1) / nWorker;
        for (int i = 0; i < nWorker; i++) {
            nextStep.add(new Semaphore(0));
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, bodies.size());
            workersPools.add(new SimulatorWorkerAgent(nextStep.get(i), stepDone, barrier, start, end, bodies));
        }
    }

    private void startWorkers() {
        workersPools.forEach(SimulatorWorkerAgent::start);
    }

    private void doSimulationStep() {
        try {
            stepDone.reset(); // Reset latch count for next step
            nextStep.forEach(Semaphore::release); // Unlock all waiting worker threads
            stepDone.await(); // Waiting all workers

            /* update virtual time */
            world.setVirtualTime(world.getVirtualTime() + world.getDt());
            world.incrementIteration();

            nextStepFlag = true; // Setup next step mode
        } catch (InterruptedException ex) {
            log("Failing to await stepDone");
        }
    }

    private void canGoOn() throws InterruptedException {
        simulationLock.lock();
        /* Manage both play/pause and step-by-step mode
        *
        * (isStepByStep & netStepFlag) manage the case where step-by-step mode is on and we compute next step:
        * isStepByStep = true -- nextStepFlag = false (set by stepSim()) the AND is = false and we can go on to
        * compute the next simulation step.
        *
        * The intent of this check is to manage only one condition variable to play/pause the simulator ether for
        * play/pause mode or step-by-step mode, for optimisation.
        */
        while (!isRunning.get() || isStepByStep & nextStepFlag) {
            pauseCond.await();
        }
        simulationLock.unlock();
    }
}
