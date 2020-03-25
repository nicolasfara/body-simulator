package it.unibo.pcd;

import it.unibo.pcd.presenter.SimulatorPresenter;

public class BodySimulatorCli {
    public static void main(String[] args) {
        SimulatorPresenter presenter = new SimulatorPresenter(500);

        presenter.execute(10000);
    }
}
