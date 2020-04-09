package it.unibo.pcd.presenter.worker;

import gov.nasa.jpf.util.test.TestJPF;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.BodyFactory;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.model.World;
import it.unibo.pcd.presenter.worker.util.ResettableCountDownLatch;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import static org.junit.Assert.*;

public class SimulatorWorkerAgentTest extends TestJPF {
    private final World world = World.getInstance();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void workerAgentTest() throws InterruptedException {
        world.setBounds(new Boundary(1.0, -1.0, 1.0, -1.0));
        final Semaphore semWork1 = new Semaphore(0);
        final Semaphore semWork2 = new Semaphore(0);
        final ResettableCountDownLatch stepDone = new ResettableCountDownLatch(2);
        final CyclicBarrier barrier = new CyclicBarrier(2);
        final List<Body> bodies = BodyFactory.getBodiesAtRandomPosition(world.getBounds(), 4);
        final SimulatorWorkerAgent worker1 = new SimulatorWorkerAgent(semWork1, stepDone, barrier, 0, 2, bodies);
        final SimulatorWorkerAgent worker2 = new SimulatorWorkerAgent(semWork2, stepDone, barrier, 2, 4, bodies);

        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            worker1.start();
            worker2.start();

            semWork1.release();
            semWork2.release();

            stepDone.await();

            worker1.stopWorker();
            worker2.stopWorker();

            semWork1.release();
            semWork2.release();
        }
    }
}