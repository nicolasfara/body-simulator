package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;

import java.util.List;

public class SimulatorMasterAgent extends Agent {
    private final List<Body> bodies;
    private final int nWorker;

    public SimulatorMasterAgent(final String name, final List<Body> bodies, final int nWorker) {
        super(name);
        this.bodies = bodies;
        this.nWorker = nWorker;
    }

    @Override
    public void run() {
        super.run();
        for (int i = 0; i < nWorker; i++) {
            int start = (bodies.size() - 1) * i / nWorker;
            int end = (bodies.size() - 1) * (i+1) / nWorker;
            new SimulatorWorkerAgent("Worker" + i, start, end, bodies).start();
        }
    }
}
