package it.unibo.pcd.contract;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.presenter.BasePresenter;
import it.unibo.pcd.view.BaseView;

import java.util.List;

public interface SimulatorContract {

    interface View extends BaseView<Presenter> {
        void updateView(final List<Body> bodies, final double vt, final long iter);
    }

    interface Presenter extends BasePresenter {
        void started();
        void stopped();
        void step();
    }
}
