package it.unibo.pcd;

import it.unibo.pcd.model.Body;
import it.unibo.pcd.presenter.BasePresenter;
import it.unibo.pcd.view.BaseView;

import java.util.List;

public interface Contract {

    interface View extends BaseView<Presenter> {
        void updateView(final List<Body> bodies, final double vt, final long iter);
    }

    interface Presenter extends BasePresenter {
        // TODO
        void execute(final long nIterations);
    }
}