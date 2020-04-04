package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.presenter.Flag;
import it.unibo.pcd.presenter.ResettableLatch;

import java.util.List;
import java.util.concurrent.Semaphore;

public class SimulatorWorkerAgent extends Agent {

    private final int start;
    private final int end;
    private final List<Body> bodies;
    private Flag stopFlag;
    /* for coordination with the master */
    private Semaphore nextStep;
    private ResettableLatch stepDone;
    private transient Boundary bounds;


    public SimulatorWorkerAgent(final String name, final int start, final int end, Semaphore nextStep,
                                ResettableLatch stepDone, final List<Body> bodies, final Flag stopFlag,
                                final Boundary bounds) {
        super(name,stopFlag);
        this.start = start;
        this.end = end;
        this.bodies = bodies;
        this.stopFlag = stopFlag;
        this.nextStep = nextStep;
        this.stepDone = stepDone;
        this.bounds = bounds;
    }

    @Override
    public void run() {

        super.log("Working from " + start + " to " + end);
        while (!stopFlag.isSet()) {
            try {
                nextStep.acquire();
                /* compute bodies new pos */
                computePosition(bodies);

                for (int i = start; i <end; i++) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        if (bodies.get(i).collideWith(bodies.get(j))) {
                            Body.solveCollision(bodies.get(i), bodies.get(j));
                        }
                    }
                }
                /* check boundaries */
                checkBoundaries(bodies);
                stepDone.down();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        super.log("Job completed");
    }
    private void computePosition(final List<Body> upBodies) {
        final double dt = 0.1;
        for (final Body b : upBodies) {
            b.updatePos(dt);
        }
    }

    private void checkBoundaries(List<Body> cBodies) {
        for (final Body b : cBodies) {
            b.checkAndSolveBoundaryCollision(bounds);
        }
    }


}
