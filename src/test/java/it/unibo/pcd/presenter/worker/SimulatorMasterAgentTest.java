package it.unibo.pcd.presenter.worker;

import gov.nasa.jpf.annotation.JPFConfig;
import gov.nasa.jpf.vm.Verify;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.model.BodyFactory;
import it.unibo.pcd.model.Boundary;
import it.unibo.pcd.model.World;

import gov.nasa.jpf.util.TypeRef;
import gov.nasa.jpf.util.test.TestJPF;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

@JPFConfig({"listener+=,gov.nasa.jpf.listener.PreciseRaceDetector"})
public class SimulatorMasterAgentTest extends TestJPF {
    static class Ptr {
        public int value;
    }

    private SimulatorMasterAgent masterAgent;

    @Before
    public void init() {

    }

    @Test
    public void masterAgentRaceConditionCheck() throws InterruptedException {
        final World world = World.getInstance();
        world.setVirtualTime(0);
        world.setIterationsNumber(30);
        world.setBounds(new Boundary(-1.0,-1.0,1.0,1.0));
        final List<Body> bodies = BodyFactory.getBodiesAtRandomPosition(world.getBounds(), 500);
        masterAgent = new SimulatorMasterAgent(bodies, 8);
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            masterAgent.start();
        }
    }
}