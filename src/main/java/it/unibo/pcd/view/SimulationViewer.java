package it.unibo.pcd.view;

import it.unibo.pcd.Body;

import java.util.List;

public interface SimulationViewer {
    /**
     * .
     * @param vt .
     * @param iter .
     */
    void display(double vt, long iter);

    void setBodies(List<Body> bodies);
}
