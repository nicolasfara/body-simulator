package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.model.World;
import it.unibo.pcd.presenter.worker.util.ResettableCountDownLatch;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimulatorWorkerAgent extends Agent {
    private boolean isRunning = true;
    private final int start;
    private final int end;
    private final List<Body> bodies;
    private final Semaphore nextStep;
    private final ResettableCountDownLatch stepDone;
    private final CyclicBarrier barrier;
    private final World world = World.getInstance();
    private final Boundary bounds = world.getBounds(); //Since the boundary is not modified... save the instance for performance purpose

    public SimulatorWorkerAgent(final Semaphore nextStep, final ResettableCountDownLatch stepDone, final CyclicBarrier barrier,
                                final int start, final int end, final List<Body> bodies) {
        super("Worker");
        this.start = start;
        this.end = end;
        this.bodies = bodies;
        this.nextStep = nextStep;
        this.stepDone = stepDone;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        super.run();
        super.log("From " + start + " to " + end);


        while (isRunning) {
            try {
                /* Waiting master to compute next step */
                nextStep.acquire();
                if (!isRunning) {
                    continue; // If the las iteration is occurred, exit from while (prevent deadlock on semaphore)
                }

                /* compute bodies new pos */
                updatePositions();

                barrier.await();

                /* manage collision */
                manageCollisions();

                /* check boundaries */
                checkBoundaries();

              stepDone.down(); // Finish the step... synchronize with master

            } catch (InterruptedException ex) {
                log("Interrupted. Terminating");
            } catch (BrokenBarrierException ex) {
                log("Barrier broken\n" + ex);
            }
        }
    }

    public void stopWorker() {
        isRunning = false;
    }

    private void updatePositions() {
        for (int i = start; i < end; i++) {
            bodies.get(i).updatePos(world.getDt());
        }
    }

    private void manageCollisions() {
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                if (bodies.get(i).collideWith(bodies.get(j))) {
                    Body.solveCollision(bodies.get(i), bodies.get(j));
                }
            }
        }
    }

    private void checkBoundaries() {
        for (int i = start; i < end; i++) {
            bodies.get(i).checkAndSolveBoundaryCollision(bounds);
        }
    }
}
