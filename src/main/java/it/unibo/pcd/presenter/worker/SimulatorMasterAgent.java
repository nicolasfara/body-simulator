package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.presenter.Chrono;
import it.unibo.pcd.presenter.Flag;
import it.unibo.pcd.presenter.ResettableLatch;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimulatorMasterAgent extends Agent {
    private List<Body> bodies;
    private Flag stopFlag;
    private ResettableLatch stepDone;
    private int iter;
    private transient SimulatorContract.View mView;
    private int nWorker;
    private transient Boundary bounds;
    private Semaphore[] nextSteps;
    private SimulatorWorkerAgent[] workers;
    private CyclicBarrier cyclicBarrier;


    public SimulatorMasterAgent(final String name, final List<Body> bodies, final Flag stopFlag,
                                final int nIter, final SimulatorContract.View mView,  final Boundary bounds) {
        super(name, stopFlag);
        this.bodies = bodies;
        this.stopFlag = stopFlag;
        this.iter = nIter;
        this.mView = mView;
        this.bounds = bounds;
    }

    @Override
    public void run() {
        super.run();
        super.log("Init workers...");
        initWorkers();
        super.log("Starting simulation...");

        if (mView != null) {
            doSimulationWithGUI();
        } else {
            doSimulationWithChrono();
        }
    }

    private void initWorkers() {

       // this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        this.nWorker = 1;
        log("creating workers " + nWorker);
        nextSteps = new Semaphore[nWorker];
        stepDone = new ResettableLatch(nWorker);
        workers = new SimulatorWorkerAgent[nWorker];
        this.cyclicBarrier = new CyclicBarrier(nWorker);
        int nBodyPerWorker = bodies.size() / nWorker;
        int nRem = bodies.size() % nWorker;
        int from = 0;
        for (int i = 0; i < nWorker; i++) {
            nextSteps[i] = new Semaphore(0);
            int num = nBodyPerWorker;
            if (nRem > 0) {
                num++;
                nRem--;
            }
            workers[i] = new SimulatorWorkerAgent("Worker" + i, from, num, nextSteps[i],
                            stepDone, bodies, stopFlag, bounds,cyclicBarrier);
            workers[i].start();
            from = from + num;
        }
    }

    private void doSimulationWithChrono() {

        /* init virtual time */
        double vt = 0;
        final double dt = 0.1;
        long nIterations = 0;
        Chrono chrono = new Chrono();
        chrono.start();
        super.log("Started.");

        /* simulation loop */
        while (nIterations < iter) {
            stepDone.reset();
            /* notify workers to make a new step */
            for (Semaphore s : nextSteps) {
                s.release();
            }
            try {
                /* wait for all workers to complete their job */
                stepDone.await();

                /* update virtual time */
                vt = vt + dt;
                nIterations++;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.stopFlag.reset();
        chrono.stop();
        long dt2 = chrono.getTime();
        double timePerStep = ((double) dt2) / iter;
        super.log("Done " + iter + " iter with " + bodies.size() + " bodies using " + nWorker + " workers in: " + dt2 + "ms");
        super.log("- " + timePerStep + " ms per step");
        System.exit(0);


    }

    private void doSimulationWithGUI() {
        /* init virtual time */
        double vt = 0;
        final double dt = 0.1;
        /* simulation loop */
        while (!stopFlag.isSet()) {
            stepDone.reset();
            /* notify workers to make a new step */
            for (Semaphore s : nextSteps) {
                s.release();
            }
            try {
                /* wait for all workers to complete their job */
                stepDone.await();
                /* update virtual time */
                vt = vt + dt;

                mView.updateView(bodies, vt, iter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.log("completed.");
    }
}

