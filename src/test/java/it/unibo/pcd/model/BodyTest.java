package it.unibo.pcd.model;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BodyTest extends TestJPF {

    private final Body body = new Body(new Position(2.3, 4.5), new Velocity(0.1, 4.0), 0.01);

    @Before
    public void init() {

    }

    @Test
    public void getRadius() {
    }

    @Test
    public void getPos() {

    }

    @Test
    public void getVel() {
    }

    @Test
    public void updatePos() {
    }

    @Test
    public void changeVel() {
    }

    @Test
    public void getDistance() {
    }
}