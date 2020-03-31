package it.unibo.pcd.model;

public class World {
    private double virtualTime = 0;
    private long iterationsNumber = -1;
    private Boundary bounds;
    private final double dt = 0.1;
    private static volatile World instance;

    private World() { }

    /**
     * Return the instance of the world. (optimized code)
     * @return the world instance.
     */
    public static World getInstance() {
        if (instance == null) {
            synchronized (World.class) {
                if (instance == null) {
                    instance = new World();
                }
            }
        }
        return instance;
    }

    public synchronized void setVirtualTime(final double virtualTime) {
        this.virtualTime = virtualTime;
    }

    public synchronized double getVirtualTime() {
        return virtualTime;
    }

    public synchronized long getIterationsNumber() {
        return iterationsNumber;
    }

    public synchronized void setIterationsNumber(final long iterationsNumber) {
        if (this.iterationsNumber == -1) {
            this.iterationsNumber = iterationsNumber;
        } else {
            throw new IllegalStateException("The iterations number was already set");
        }
    }

    public synchronized double getDt() {
        return dt;
    }

    public synchronized void setBounds(final Boundary bounds) {
        if (this.bounds == null) {
            this.bounds = bounds;
        } else {
            throw new IllegalStateException("Bounds are already defined");
        }
    }

    public synchronized Boundary getBounds() {
        return bounds;
    }
}
