package it.unibo.pcd.view.fx;

import it.unibo.pcd.Body;
import it.unibo.pcd.Position;
import it.unibo.pcd.view.SimulationViewer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class SimulationViewerFX extends Group implements SimulationViewer {
    private List<Body> bodies;

    public SimulationViewerFX(final int w, final int h) {
    }

    @Override
    public void display(double vt, long iter) {

    }

    @Override
    public void setBodies(List<Body> bodies) {
        this.bodies = bodies;
    }
}
