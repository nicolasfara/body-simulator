package it.unibo.pcd;

import it.unibo.pcd.presenter.Simulator;
import it.unibo.pcd.view.SimulationViewer;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 *
 * @author aricci
 */
public class BodySimulatorMain {
    public static void main(String[] args) {
        SimulationViewer viewer = new SimulationViewer(620,620);

        Simulator sim = new Simulator(viewer);
        sim.execute();
    }
}
