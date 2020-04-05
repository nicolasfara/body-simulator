package it.unibo.pcd.presenter;

import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.*;
import it.unibo.pcd.presenter.worker.SimulatorMasterAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SimulatorPresenter implements SimulatorContract.Presenter {
    private transient SimulatorContract.View mView;

    private transient List<Body> bodies;
    /* boundary of the field */
    private transient Boundary bounds;

    /* Flag start/stop master worker */
    public Flag stopFlag;
    private int nIter;
    private int nWorker;
    SimulatorMasterAgent master;

    public SimulatorPresenter(final int bodiesCount, final int iter) {
        bounds = new Boundary(-1.0, -1.0, 1.0, 1.0);
        bodies = new ArrayList<>();
        this.nIter = iter;
        this.mView = null;
        bodies.addAll(BodyFactory.getBodiesAtRandomPosition(bounds, bodiesCount));
    }

    public SimulatorPresenter(final SimulatorContract.View mView, final int bodiesCount, final int iter) {
        this(bodiesCount, iter);
        this.mView = mView;
    }

    public void started() {
        this.stopFlag = new Flag();
        this.nWorker = Runtime.getRuntime().availableProcessors() + 1;
        master = new SimulatorMasterAgent("Master", bodies, stopFlag, nIter, mView, bounds, nWorker);
        master.start();
    }

    public void stopped() {
        //this.stopFlag.set();
        this.master.stopped();
    }

    public void step() {
        this.master.stepByStepCommand();
    }
}
