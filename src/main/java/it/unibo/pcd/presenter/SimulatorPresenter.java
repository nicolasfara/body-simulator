package it.unibo.pcd.presenter;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.*;
import it.unibo.pcd.presenter.worker.SimulatorMasterAgent;

import java.util.ArrayList;
import java.util.List;

public class SimulatorPresenter implements SimulatorContract.Presenter {
    private transient SimulatorContract.View mView;

    final SimulatorMasterAgent masterAgent;
    private final World world;
    private final Object playPause = new Object();

    public SimulatorPresenter(final int bodiesCount) {
        world = World.getInstance();
        world.setBounds(new Boundary(-1.0,-1.0,1.0,1.0));

        List<Body> bodies = new ArrayList<>(BodyFactory.getBodiesAtRandomPosition(world.getBounds(), bodiesCount));

        masterAgent = new SimulatorMasterAgent(bodies, Runtime.getRuntime().availableProcessors() + 1, playPause);
    }

    public SimulatorPresenter(final SimulatorContract.View mView, final int bodiesCount) {
        this(bodiesCount);
        this.mView = mView;
    }

    @Override
    public void pauseSimulation() {
        masterAgent.pauseSim();
    }

    @Override
    public void resumeSimulation() {
        masterAgent.resumeSim();
        synchronized (playPause) {
            playPause.notifyAll();
        }
    }

    @Override
    public void execute(final long nIterations) {
        /* init virtual time */
        world.setVirtualTime(0);
        /* set iterations number */
        world.setIterationsNumber(nIterations);
        //final SimulatorMasterAgent masterAgent = new SimulatorMasterAgent(bodies, 1);
        if (mView != null) {
            masterAgent.setView(mView);
        }

        masterAgent.start();
    }
}
