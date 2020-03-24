package it.unibo.pcd;

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
