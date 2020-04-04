package it.unibo.pcd;

import it.unibo.pcd.presenter.SimulatorPresenter;

public final class BodySimulatorCli {
    private BodySimulatorCli() { }

    public static void main(final String[] args) throws InterruptedException {
        final SimulatorPresenter presenter = new SimulatorPresenter(500);
        presenter.execute(100);
    }
}

