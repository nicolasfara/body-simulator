package it.unibo.pcd.presenter.worker;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Agent extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Agent.class.getName());

    public Agent(final String name) {
        super(name);
    }

    public void log(final String message) {
        synchronized (LOGGER) {
            LOGGER.log(Level.INFO,  "[" + getName() + "] " + message + "\n");
        }
    }
}
