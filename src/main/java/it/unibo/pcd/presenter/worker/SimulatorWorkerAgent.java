package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;

import java.util.List;

public class SimulatorWorkerAgent extends Agent {

    private final int start;
    private final int end;
    private final List<Body> bodies;

    public SimulatorWorkerAgent(final String name, final int start, final int end, final List<Body> bodies) {
        super(name);
        this.start = start;
        this.end = end;
        this.bodies = bodies;
    }

    @Override
    public void run() {
        super.run();
        super.log("From " + start + " to " + end);
        for (int i = start; i < end; i++) {
            for (int j = i + 1; j < bodies.size(); j++) {
                if (bodies.get(i).collideWith(bodies.get(j))) {
                    Body.solveCollision(bodies.get(i), bodies.get(j));
                }
            }
        }
    }
}
