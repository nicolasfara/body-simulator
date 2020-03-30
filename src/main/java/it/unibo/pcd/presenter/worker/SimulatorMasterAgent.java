package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;

import java.util.ArrayList;
import java.util.List;

public class SimulatorMasterAgent extends Agent {
    private List<Body> bodies;
    private final int nWorker;
    private final List<SimulatorWorkerAgent> workersPools;

    public SimulatorMasterAgent(final String name, final List<Body> bodies, final int nWorker) {
        super(name);
        this.bodies = bodies;
        this.nWorker = nWorker;
        workersPools = new ArrayList<>(nWorker);
    }

    @Override
    public void run() {
        super.run();

        initWorkers();
    }

    private void initWorkers() {
        for (int i = 0; i < nWorker; i++) {
            int start = (bodies.size() - 1) * i / nWorker;
            int end = (bodies.size() - 1) * (i+1) / nWorker;
            workersPools.add(new SimulatorWorkerAgent("Worker" + i, start, end, bodies));
        }
    }

    private void doSimulation() {
        /*
        * while (iter < nIter)
        *   latch.countDown()
        *   latch.reset*/
    }
}
