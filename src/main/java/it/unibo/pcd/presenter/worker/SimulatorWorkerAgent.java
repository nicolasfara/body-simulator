package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.World;
import it.unibo.pcd.presenter.worker.util.ResettableCountDownLatch;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimulatorWorkerAgent extends Agent {

    private final int start;
    private final int end;
    private final List<Body> bodies;
    private final Semaphore nextStep;
    private final ResettableCountDownLatch stepDone;
    private final CyclicBarrier barrier;

    public SimulatorWorkerAgent(Semaphore nextStep, ResettableCountDownLatch stepDone, CyclicBarrier barrier,
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

        World world = World.getInstance();

        while (true) {
            try {
                /* Waiting master to compute next step */
                nextStep.acquire();

                /* compute bodies new pos */
                for (int i = start; i < end; i++) {
                    bodies.get(i).updatePos(world.getDt());
                }

                barrier.await();

                /* manage collision */
                for (int i = start; i < end; i++) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        if (bodies.get(i).collideWith(bodies.get(j))) {
                            Body.solveCollision(bodies.get(i), bodies.get(j));
                        }
                    }
                }

                barrier.await();

                /* check boundaries */
                for (int i = start; i < end; i++) {
                    bodies.get(i).checkAndSolveBoundaryCollision(world.getBounds());
                }

                stepDone.down(); // Finish the step... synchronize with other threads and master

            } catch (InterruptedException ex) {
                log("Interrupted. Terminating");
            } catch (BrokenBarrierException ex) {
                log("Barrier broken\n" + ex);
            }
        }

    }
}
