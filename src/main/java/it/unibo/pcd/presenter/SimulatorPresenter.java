package it.unibo.pcd.presenter;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.*;
import it.unibo.pcd.presenter.worker.SimulatorMasterAgent;

import java.util.ArrayList;
import java.util.List;

public class SimulatorPresenter implements SimulatorContract.Presenter {
    private transient SimulatorContract.View mView;

    private transient List<Body> bodies;
    /* boundary of the field */
    private transient Boundary bounds;

    public SimulatorPresenter(final int bodiesCount) {
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        bodies = new ArrayList<>();

        bodies.addAll(BodyFactory.getBodiesAtRandomPosition(bounds, bodiesCount));
    }

    public SimulatorPresenter(final SimulatorContract.View mView, final int bodiesCount) {
        this(bodiesCount);
        this.mView = mView;
    }

    @Override
    public void execute(final long nIterations) {

        /* init virtual time */
        double vt = 0;
        final double dt = 0.1;

        long iter = 0;

        /* simulation loop */
        while (iter < nIterations) {

            /* compute bodies new pos */
            for (final Body b : bodies) {
                b.updatePos(dt);
            }

            /* check collisions */
            /*for (int i = 0; i < bodies.size() - 1; i++) {
                for (int j = i + 1; j < bodies.size(); j++) {
                    if (bodies.get(i).collideWith(bodies.get(j))) {
                        Body.solveCollision(bodies.get(i), bodies.get(j));
                    }
                }
            }*/
            new SimulatorMasterAgent("Master", bodies).start();

            /* check boundaries */
            for (final Body b : bodies) {
                b.checkAndSolveBoundaryCollision(bounds);
            }

            /* update virtual time */
            vt = vt + dt;
            iter++;

            if (mView != null) {
                mView.updateView(bodies, vt, iter);
            } else {
                System.out.println("Iteration number: " + iter);
            }
        }
    }
}
