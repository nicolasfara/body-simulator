package it.unibo.pcd.presenter.worker;

import it.unibo.pcd.presenter.Flag;

public abstract class Agent extends Thread {
    protected Flag stopFlag;
    public Agent(String name, Flag stopFlag) {
        super(name);
        this.stopFlag = stopFlag;
    }

    public void log(final String message) {
        synchronized (System.out) {
            System.out.println("[" + getName() + "] " + message);
        }
    }
}
