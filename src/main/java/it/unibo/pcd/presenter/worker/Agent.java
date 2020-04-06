package it.unibo.pcd.presenter.worker;

public abstract class Agent extends Thread {

    public Agent(final String name) {
        super(name);
    }

    public void log(final String message) {
        synchronized (System.out) {
            System.out.println("[" + getName() + "] " + message);
        }
    }
}
