package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.presenter.Chrono;
import it.unibo.pcd.presenter.Flag;
import it.unibo.pcd.presenter.ResettableLatch;

import java.util.ArrayList;
import java.util.List;

public class SimulatorMasterAgent extends Agent {
    private List<Body> bodies;
    private Flag stopFlag;
    private ResettableLatch stepDone;
    private int iter;
    private transient SimulatorContract.View mView;
    private int nWorker;
    private transient Boundary bounds;

    public SimulatorMasterAgent(final String name, final List<Body> bodies, final Flag stopFlag,
                                final int nIter, final SimulatorContract.View mView, final Boundary bounds) {
        super(name);
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

        this.nWorker = Runtime.getRuntime().availableProcessors() ;
        stepDone = new ResettableLatch(nWorker);
        for (int i = 0; i < nWorker; i++) {
            int start = (bodies.size() - 1) * i / nWorker;
            int end = (bodies.size() - 1) * (i + 1) / nWorker;
            new SimulatorWorkerAgent("Worker" + i, start, end, stepDone, bodies, stopFlag).start();
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
        while (nIterations < iter && !stopFlag.isSet()) {
            stepDone.reset();
            try {
                /* compute bodies new pos */
                computePosition(bodies,dt);
                /* wait for all workers to complete their job */
                stepDone.await();

                /* check boundaries */
                checkBoundaries(bodies);

                /* update virtual time */
                vt = vt + dt;
                iter++;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            chrono.stop();
            long dt2 = chrono.getTime();
            double timePerStep = ((double) dt2) / iter;
            //super.log("Done " + iter + " iter with " + bodies.size() + " bodies using " + nWorker + " workers in: " + dt2 + "ms");
            //super.log("- " + timePerStep + " ms per step");
            System.exit(0);
        }
    }

    private void doSimulationWithGUI() {
        /* init virtual time */
        double vt = 0;
        final double dt = 0.1;
        long nIterations = 0;
        /* simulation loop */
        while (nIterations < iter && !stopFlag.isSet()) {
            stepDone.reset();
            try {

                /* compute bodies new pos */
                computePosition(bodies,dt);
                /* wait for all workers to complete their job */
                stepDone.await();
                /* check boundaries */
                checkBoundaries(bodies);
                /* update virtual time */
                vt = vt + dt;
                iter++;
                mView.updateView(bodies, vt, iter);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.log("completed.");
    }

    private void computePosition(final List<Body> upBodies,final double dt){
        for (final Body b : upBodies) {
            b.updatePos(dt);
        }
    }
    private void checkBoundaries(List<Body> cBodies){
        for (final Body b : cBodies) {
            b.checkAndSolveBoundaryCollision(bounds);
        }
    }
}

