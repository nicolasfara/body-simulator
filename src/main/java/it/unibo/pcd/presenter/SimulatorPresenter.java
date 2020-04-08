package it.unibo.pcd.presenter;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.*;
import it.unibo.pcd.presenter.worker.SimulatorMasterAgent;

import java.util.ArrayList;
import java.util.List;

public class SimulatorPresenter implements SimulatorContract.Presenter {
    private transient SimulatorContract.View mView;
    private final SimulatorMasterAgent masterAgent;
    private final World world;

    public SimulatorPresenter(final int bodiesCount, final int nWorkers) {
        world = World.getInstance();
        world.setBounds(new Boundary(-1.0,-1.0,1.0,1.0));

        final List<Body> bodies = new ArrayList<>(BodyFactory.getBodiesAtRandomPosition(world.getBounds(), bodiesCount));

        masterAgent = new SimulatorMasterAgent(bodies, nWorkers);
    }

    public SimulatorPresenter(final SimulatorContract.View mView, final int bodiesCount, final int nWorkers) {
        this(bodiesCount, nWorkers);
        this.mView = mView;
    }

    @Override
    public void stopSimulation() {
        masterAgent.pauseSim();
    }

    @Override
    public void startSimulation() {
        masterAgent.resumeSim();
    }

    @Override
    public void step() {
        masterAgent.stepSim();
    }

    @Override
    public void execute(final long nIterations) {
        /* init virtual time */
        world.setVirtualTime(0);
        /* set iterations number */
        world.setIterationsNumber(nIterations);

        if (mView != null) {
            masterAgent.setView(mView);
        }

        masterAgent.start();
        try {
            masterAgent.join();
        } catch (InterruptedException ex) {

        }
    }

    @Override
    public double getLastExecutionTime() {
        return masterAgent.getLastIterationTime();
    }
}
