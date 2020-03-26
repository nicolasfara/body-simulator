package it.unibo.pcd.presenter;

import it.unibo.pcd.contract.Contract;
import it.unibo.pcd.model.*;

import java.util.ArrayList;
import java.util.List;

public class SimulatorPresenter implements Contract.Presenter {
    private Contract.View mView = null;

    private List<Body> bodies;
    /* boundary of the field */
    private Boundary bounds;

    public SimulatorPresenter(final int bodiesCount) {
        bounds = new Boundary(-1.0,-1.0,1.0,1.0);
        bodies = new ArrayList<>();

        bodies.addAll(BodyFactory.getBodiesAtRandomPosition(bounds, bodiesCount));
    }

    public SimulatorPresenter(final Contract.View mView, final int bodiesCount) {
        this(bodiesCount);
        this.mView = mView;
    }

    @Override
    public void execute(final long nIterations) {

        /* init virtual time */

        double vt = 0;
        double dt = 0.1;

        long iter = 0;

        /* simulation loop */

        while (iter < nIterations) {

            /* compute bodies new pos */

            for (Body b : bodies) {
                b.updatePos(dt);
            }

            /* check collisions */

            for (int i = 0; i < bodies.size() - 1; i++) {
                Body b1 = bodies.get(i);
                for (int j = i + 1; j < bodies.size(); j++) {
                    Body b2 = bodies.get(j);
                    if (b1.collideWith(b2)) {
                        Body.solveCollision(b1, b2);
                    }
                }
            }

            /* check boundaries */

            for (Body b : bodies) {
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
