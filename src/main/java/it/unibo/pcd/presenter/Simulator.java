package it.unibo.pcd.presenter;

import it.unibo.pcd.view.SimulationViewer;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.model.Position;
import it.unibo.pcd.model.Velocity;

import java.util.*;

public class Simulator {
    private SimulationViewer viewer;

    /* bodies in the field */
    ArrayList<Body> bodies;

    /* boundary of the field */
    private Boundary bounds;

    public Simulator(SimulationViewer viewer){
        this.viewer = viewer;

        /* initializing boundary and bodies */

        bounds = new Boundary(-1.0,-1.0,1.0,1.0);

        /* test with 3 big bodies */
        /*
        bodies = new ArrayList<Body>();
        bodies.add(new Body(new Position(-0.5,0), new Velocity(0.005,0), 0.05));
        bodies.add(new Body(new Position(0,0.05), new Velocity(0,0), 0.05));
        bodies.add(new Body(new Position(0.07,-0.1), new Velocity(0,0), 0.05));
        */

        /* test with 1000 small bodies */

        Random rand = new Random(System.currentTimeMillis());
        bodies = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            double x = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double y = bounds.getX0() + rand.nextDouble()*(bounds.getX1() - bounds.getX0());
            double dx = -1 + rand.nextDouble()*2;
            double speed = rand.nextDouble()*0.05;
            Body b = new Body(new Position(x, y), new Velocity(dx*speed,Math.sqrt(1 - dx*dx)*speed), 0.01);
            bodies.add(b);
        }

    }

    public void execute() {

        /* init virtual time */

        double vt = 0;
        double dt = 0.1;

        long iter = 0;
        long nIterations = 10000;

        /* simulation loop */

        while (iter < nIterations){

            /* compute bodies new pos */

            for (Body b: bodies) {
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

            for (Body b: bodies) {
                b.checkAndSolveBoundaryCollision(bounds);
            }

            /* update virtual time */

            vt = vt + dt;
            iter++;

            /* display current stage */

            viewer.display(bodies, vt, iter);

        }
    }

}
