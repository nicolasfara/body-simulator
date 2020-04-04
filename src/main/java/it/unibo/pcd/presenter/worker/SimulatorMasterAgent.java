package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.World;
import it.unibo.pcd.presenter.worker.util.ResettableCountDownLatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimulatorMasterAgent extends Agent {
    private final List<Body> bodies;
    private final int nWorker;

    private final List<Long> stepTimes = new ArrayList<>();

    private final List<Semaphore> nextStep;
    private final List<SimulatorWorkerAgent> workersPools;
    private final ResettableCountDownLatch stepDone;
    private final ResettableCountDownLatch pauseResume;
    private final CyclicBarrier barrier;
    private transient SimulatorContract.View mView;
    final Object playPause;

    private boolean isRunning = true;

    public SimulatorMasterAgent(final List<Body> bodies, final int nWorker, final Object playPause) {
        super("Master");
        this.bodies = bodies;
        this.nWorker = nWorker;
        this.playPause = playPause;

        workersPools = new ArrayList<>(nWorker);
        nextStep = new ArrayList<>(nWorker);
        stepDone = new ResettableCountDownLatch(nWorker);
        pauseResume = new ResettableCountDownLatch(1);
        barrier = new CyclicBarrier(nWorker);
    }

    @Override
    public void run() {
        super.run();
        initWorkers();
        doSimulation();
    }

    public void setView(final SimulatorContract.View mView) {
        this.mView = mView;
    }

    public void pauseSim() {
        log("Pause");
        isRunning = false;
    }

    public void resumeSim() {
        log("Resume");
        isRunning = true;
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

    private void doSimulation() {
        long iter = 0;
        final World world = World.getInstance();
        long tStart;
        long tEnd;

        workersPools.forEach(SimulatorWorkerAgent::start); //Start all worker threads

        while (iter < world.getIterationsNumber()) {
            try {
                synchronized (playPause) {
                    while (!isRunning) {
                        playPause.wait();
                    }
                }

                tStart = System.currentTimeMillis();
                stepDone.reset(); // Reset latch count for next step
                nextStep.forEach(Semaphore::release); // Unlock all waiting worker threads
                stepDone.await(); // Waiting all threads

                /* update virtual time */
                world.setVirtualTime(world.getVirtualTime() + world.getDt());
                iter++;

                tEnd = System.currentTimeMillis();
                log("Elapsed: " + (tEnd-tStart));

                if (mView != null) {
                    mView.updateView(bodies, world.getVirtualTime(), iter);
                    log("Update View");
                } else {
                    System.out.println("Iteration number: " + iter);
                }

                stepTimes.add((tEnd-tStart));
            } catch (InterruptedException ex) {
                log("Failing to await stepDone");
            }
        }

        workersPools.forEach(SimulatorWorkerAgent::stopWorker); //Terminate all worker threads
        nextStep.forEach(Semaphore::release); // This prevent deadlock (thanks JPF :))
        //try {
        //    for (final SimulatorWorkerAgent worker : workersPools) {
        //        worker.join();
        //    }
        //} catch (InterruptedException ex) {
        //    log("Error on joining workers");
        //}

        //final double avgTime = (double) stepTimes.stream().reduce(0L, Long::sum) / stepTimes.size();
        //log("Average timer per step: " + avgTime + " ms");
    }
}
