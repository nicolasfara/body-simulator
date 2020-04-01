package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.presenter.Flag;
import it.unibo.pcd.presenter.ResettableLatch;

import java.util.List;
import java.util.concurrent.Semaphore;

public class SimulatorWorkerAgent extends Agent {

    private final int start;
    private final int end;
    private final List<Body> bodies;
    private Flag stopFlag;
    private ResettableLatch stepDone;


    public SimulatorWorkerAgent(final String name, final int start, final int end, ResettableLatch stepDone, final List<Body> bodies, final Flag stopFlag) {
        super(name);
        this.start = start;
        this.end = end;
        this.bodies = bodies;
        this.stopFlag = stopFlag;
        this.stepDone = stepDone;
    }

    @Override
    public void run() {
        super.run();
        super.log("Working from " + start + " to " + end);
        while (!stopFlag.isSet()) {

                for (int i = start; i < end; i++) {
                    for (int j = i + 1; j < bodies.size(); j++) {
                        if (bodies.get(i).collideWith(bodies.get(j))) {
                            Body.solveCollision(bodies.get(i), bodies.get(j));
                        }
                    }
                }
                stepDone.down();

        }
    }
}
