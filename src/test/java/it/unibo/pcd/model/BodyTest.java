package it.unibo.pcd.model;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Test;

import static org.junit.Assert.*;

public class BodyTest extends TestJPF {

    private final Body body = new Body(new Position(2.3, 4.5), new Velocity(0.1, 4.0), 0.01);

    @Test
    public void getRadius() {
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(body::getRadius).start();
            new Thread(body::getRadius).start();
            new Thread(body::getRadius).start();
            new Thread(body::getRadius).start();
        }
    }

    @Test
    public void getPos() {
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(body::getPos).start();
            new Thread(body::getPos).start();
            new Thread(body::getPos).start();
        }
    }

    @Test
    public void getVel() {
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(body::getVel).start();
            new Thread(body::getVel).start();
            new Thread(body::getVel).start();
        }
    }

    @Test
    public void updatePos() {
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(() -> body.updatePos(0.54)).start();
            new Thread(() -> body.updatePos(0.53)).start();
            new Thread(() -> body.updatePos(0.57)).start();
            new Thread(() -> body.updatePos(0.84)).start();
        }
    }

    @Test
    public void changeVel() {
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(() -> body.changeVel(0.54, 9.5)).start();
            new Thread(() -> body.changeVel(1.54, 9.5)).start();
            new Thread(() -> body.changeVel(2.54, 9.5)).start();
        }
    }

    @Test
    public void getDistance() {
        final Body b1 = new Body(new Position(6.3, 6.5), new Velocity(3.1, 4.0), 0.01);
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(() -> body.getDistance(b1));
            new Thread(() -> body.getDistance(b1));
            new Thread(() -> body.getDistance(b1));
        }
    }

    @Test
    public void checkAndSolveBoundaryCollisionTest() {
        final Body b1 = new Body(new Position(2.3, 4.5), new Velocity(0.1, 4.0), 0.01);
        final Body b2 = new Body(new Position(2.3, 4.5), new Velocity(0.1, 4.0), 0.01);
        final Body b3 = new Body(new Position(2.3, 4.5), new Velocity(0.1, 4.0), 0.01);
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            new Thread(() -> Body.solveCollision(b1, b2)).start();
            new Thread(() -> Body.solveCollision(b2, b3)).start();
        }
    }
}