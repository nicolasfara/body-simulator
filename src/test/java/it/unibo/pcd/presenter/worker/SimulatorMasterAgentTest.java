package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.BodyFactory;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.model.World;

import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SimulatorMasterAgentTest extends TestJPF {
    private SimulatorMasterAgent masterAgent;

    @Before
    public void init() {

    }

    @Test
    public void masterAgentRaceConditionCheck() {
        final World world = World.getInstance();
        world.setVirtualTime(0);
        world.setIterationsNumber(1);
        world.setBounds(new Boundary(-1.0,-1.0,1.0,1.0));
        final List<Body> bodies = BodyFactory.getBodiesAtRandomPosition(world.getBounds(), 4);
        masterAgent = new SimulatorMasterAgent(bodies, 2);
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            masterAgent.start();
            new Thread(() -> masterAgent.pauseSim());
        }
    }
}