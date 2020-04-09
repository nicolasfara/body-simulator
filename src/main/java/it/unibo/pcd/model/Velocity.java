package it.unibo.pcd.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Velocity {
    public double x;
    public double y;
    private final Lock velocityLock =  new ReentrantLock();

    public Velocity(final double x, final double y){
        this.x = x;
        this.y = y;
    }

    public synchronized void change(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public synchronized double getX() {
        return x;
    }

    public synchronized double getY() {
        return y;
    }

    public synchronized void setX(final double x) {
        this.x = x;
    }

    public synchronized void setY(final double y) {
        this.y = y;
    }

    public synchronized double getModule() {
        return x*x + y*y;
    }
}
