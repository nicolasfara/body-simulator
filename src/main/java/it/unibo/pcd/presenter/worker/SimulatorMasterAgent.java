package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;

import java.util.List;

public class SimulatorMasterAgent extends Agent {
    private final List<Body> bodies;

    public SimulatorMasterAgent(final String name, final List<Body> bodies) {
        super(name);
        this.bodies = bodies;
    }

    @Override
    public void run() {
        super.run();
        final int nWorker = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < nWorker; i++) {
            int start = bodies.size() * i / nWorker;
            int end = bodies.size() * (i+1) / nWorker;
            new SimulatorWorkerAgent("Worker" + i, start, end, bodies).start();;
        }
    }
}
