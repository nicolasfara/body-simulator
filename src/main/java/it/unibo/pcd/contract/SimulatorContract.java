package it.unibo.pcd.contract;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.presenter.BasePresenter;
import it.unibo.pcd.view.BaseView;

import java.util.List;

public interface SimulatorContract {

    interface View extends BaseView<Presenter> {
        void updateBodies(final List<Body> bodies);
        void updateVt(double vt);
        void updateIter(long iter);
    }

    interface Presenter extends BasePresenter {
        void stopSimulation();
        void startSimulation();
        void step();
        void execute(final long nIterations);
    }
}
