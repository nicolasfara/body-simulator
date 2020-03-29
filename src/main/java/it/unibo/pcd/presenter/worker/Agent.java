package it.unibo.pcd.presenter.worker;

public abstract class Agent extends Thread {
    private final String name;

    public Agent(String name) {
        this.name = name;
    }

    public void log(final String message) {
        System.out.println("[" + name + "] " + message);
    }
}
