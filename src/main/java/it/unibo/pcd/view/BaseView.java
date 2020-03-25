package it.unibo.pcd.view;

import it.unibo.pcd.presenter.BasePresenter;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
