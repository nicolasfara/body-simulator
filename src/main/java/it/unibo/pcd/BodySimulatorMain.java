package it.unibo.pcd;

import it.unibo.pcd.presenter.SimulatorPresenter;
import it.unibo.pcd.view.SimulationViewer;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 *
 * @author aricci
 */
public final class BodySimulatorMain {
    private BodySimulatorMain() { }

    public static void main(final String[] args) {
        final SimulationViewer viewer = new SimulationViewer(620, 620);
        final SimulatorPresenter presenter = new SimulatorPresenter(viewer, 500);
        viewer.setPresenter(presenter);

        presenter.execute(1000);
    }
}
