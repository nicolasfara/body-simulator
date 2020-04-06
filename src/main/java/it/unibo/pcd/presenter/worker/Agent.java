package it.unibo.pcd.presenter.worker;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Agent extends Thread {
    private final Logger logger = Logger.getLogger(Agent.class.getName());

    public Agent(final String name) {
        super(name);
    }

    public void log(final String message) {
        synchronized (logger) {
            logger.log(Level.INFO,  "[" + getName() + "] " + message + "\n");
        }
    }
}
