package it.unibo.pcd;

import it.unibo.pcd.presenter.SimulatorPresenter;

public final class BodySimulatorCli {
    private BodySimulatorCli() { }

    public static void main(final String[] args) {
        final SimulatorPresenter presenter = new SimulatorPresenter(2,100);
        presenter.started();
    }
}
