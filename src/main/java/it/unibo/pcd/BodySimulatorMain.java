package it.unibo.pcd;

import it.unibo.pcd.presenter.SimulatorPresenter;
import it.unibo.pcd.view.SimulationViewer;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 *
 * @author aricci
 */
public class BodySimulatorMain {
    public static void main(String[] args) {
        SimulationViewer viewer = new SimulationViewer(620, 620);
        SimulatorPresenter presenter = new SimulatorPresenter(viewer, 500);
        viewer.setPresenter(presenter);

        presenter.execute(10000);
    }
}
