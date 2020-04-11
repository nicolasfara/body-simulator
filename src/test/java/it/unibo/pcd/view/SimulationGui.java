package it.unibo.pcd.view;

import gov.nasa.jpf.vm.Verify;
import it.unibo.pcd.contract.SimulatorContract;
import it.unibo.pcd.model.Body;
import it.unibo.pcd.presenter.SimulatorPresenter;
import junit.framework.TestCase;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SimulationGui extends Thread  {

    private final SimulatorContract.Presenter mPresenter;
    private final List<Runnable> startListeners;
    private final List<Runnable> stopListeners;
    private final List<Runnable> stepListeners;
    public SimulationGui(final SimulatorContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        this.stopListeners = new ArrayList<>();
        this.startListeners = new ArrayList<>();
        this.stepListeners = new ArrayList<>();
    }

    @Override
    public void run() {
        super.run();
        Verify.beginAtomic();
        for(Runnable l : stopListeners){
            l.run();
        }
        for(Runnable l : startListeners){
            l.run();
        }
        for(Runnable l : stepListeners){
            l.run();
        }
        Verify.endAtomic();
    }

    public void addStartListener(){
        this.startListeners.add(mPresenter::startSimulation);
    }
    public void addStopListener(){
        this.stopListeners.add(mPresenter::stopSimulation);
    }
    public void addStepListener(){
        this.stepListeners.add(mPresenter::stopSimulation);
    }
}
