package it.unibo.pcd;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import it.unibo.pcd.view.BallFactory;
import it.unibo.pcd.view.fx.SimulationViewerFX;


public class BodySimulatorFX extends GameApplication {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(620);
        settings.setHeight(620);
        settings.setTitle("Body Collision");
        settings.setVersion("0.1");
    }

    @Override
    protected void initGame() {
        super.initGame();

        BallFactory ballFactory = new BallFactory();

        FXGL.getGameWorld().addEntity(ballFactory.newBall());

        SimulationViewerFX simulationViewerFX = new SimulationViewerFX(620, 620);
        Simulator simulator = new Simulator(simulationViewerFX);
    }


}
