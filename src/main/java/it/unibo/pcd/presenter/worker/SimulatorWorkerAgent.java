package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.presenter.Flag;
import it.unibo.pcd.presenter.ResettableLatch;

import java.security.cert.CertificateNotYetValidException;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
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
    private CyclicBarrier cyclicBarrier;
    private int to;


    public SimulatorWorkerAgent(final String name, final int start, final int end, Semaphore nextStep,
                                ResettableLatch stepDone, final List<Body> bodies, final Flag stopFlag,
                                final Boundary bounds, final CyclicBarrier cyclicBarrier) {
        super(name,stopFlag);
        this.start = start;
        this.end = end;
        this.bodies = bodies;
        this.stopFlag = stopFlag;
        this.nextStep = nextStep;
        this.stepDone = stepDone;
        this.bounds = bounds;
        this.cyclicBarrier = cyclicBarrier;
        this.to = start + end - 1;
    }

    @Override
    public void run() {
        super.log("Working from " + start + " to " + to);
        while (!stopFlag.isSet()) {
            try {
                nextStep.acquire();
                /* compute bodies new pos */
                computePosition(start,to);

                /* Every worker have to block here */
                cyclicBarrier.await();
                for (int i = start; i <to; i++) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        if (bodies.get(i).collideWith(bodies.get(j))) {
                            Body.solveCollision(bodies.get(i), bodies.get(j));
                        }
                    }
                }
                /* check boundaries */
                checkBoundaries(start,to);
                stepDone.down();

            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        super.log("Job completed");
    }
    private void computePosition(final int start_, final int to) {
        final double dt = 0.1;
        for (int j = start_; j <to; j++) {
            bodies.get(j).updatePos(dt);
        }
    }
    private void checkBoundaries(final int start_, final int to) {
        for (int j = start_; j <to; j++) {
            bodies.get(j).checkAndSolveBoundaryCollision(bounds);
        }
    }
}
