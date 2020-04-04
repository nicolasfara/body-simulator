package it.unibo.pcd.presenter.worker;

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

public class SimulatorMasterAgentTest extends TestJPF {
    static class Ptr {
        public int value;
    }

    private SimulatorMasterAgent masterAgent;

    @Before
    public void init() {
        final World world = World.getInstance();
        world.setVirtualTime(0);
        world.setIterationsNumber(30);
        world.setBounds(new Boundary(-1.0,-1.0,1.0,1.0));
        final List<Body> bodies = BodyFactory.getBodiesAtRandomPosition(world.getBounds(), 500);
        masterAgent = new SimulatorMasterAgent(bodies, 8);
    }

    @Test
    public void masterAgentRaceConditionCheck() throws InterruptedException {
        //if (verifyPropertyViolation(new TypeRef("gov.nasa.jpf.listener.PreciseRaceDetector"),"body-simulator.jpf")) {
        //    masterAgent.start();
        //}
        //WARN: this approach does not work neither
        Verify.setProperties("listener+=,gov.nasa.jpf.listener.PreciseRaceDetector");
        //Verify.setProperties("listener+=,MyListener");
        //WARN: this approach works correctly so the setting file is looked for
        //if (verifyPropertyViolation(new TypeRef("gov.nasa.jpf.listener.PreciseRaceDetector"),"body-simulator.jpf")) {
        if (verifyNoPropertyViolation("listener+=,gov.nasa.jpf.listener.PreciseRaceDetector", "body-simulator.jpf")) {
            Ptr x = new Ptr();
            x.value = 10;
            Runnable r = () -> {
                System.out.println(10 / x.value);
                x.value = 0;
                x.value++;
                System.out.println(x.value);
            };
            Thread t1 = new Thread(r);
            Thread t2 = new Thread(r);

            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }
}