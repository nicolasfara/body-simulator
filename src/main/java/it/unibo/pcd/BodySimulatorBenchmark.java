package it.unibo.pcd;

import it.unibo.pcd.presenter.SimulatorPresenter;

public class BodySimulatorBenchmark {
    private BodySimulatorBenchmark() { }

    public static void main(final String[] args) {
        final SimulatorPresenter simulator = new SimulatorPresenter(2000, 3);

        simulator.execute(100);
    }

}
