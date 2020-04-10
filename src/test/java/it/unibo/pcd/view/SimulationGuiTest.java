package it.unibo.pcd.view;

import gov.nasa.jpf.util.test.TestJPF;
import it.unibo.pcd.presenter.SimulatorPresenter;
import org.junit.Test;

public class SimulationGuiTest  extends TestJPF {
    final SimulatorPresenter presenter = new SimulatorPresenter( 4, 2);
    final SimulationGui gui = new SimulationGui(presenter);

    @Test
    public void testStartStop(){
        presenter.execute(2);
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            gui.addStartListener();
            gui.addStopListener();
            gui.start();
        }
    }
    @Test
    public void testStep(){
        presenter.execute(4);
        if (verifyNoPropertyViolation("listener=gov.nasa.jpf.listener.PreciseRaceDetector")) {
            gui.addStepListener();
            gui.addStartListener();
            gui.addStopListener();
            gui.addStepListener();
            gui.start();

        }
    }


}
