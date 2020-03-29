package it.unibo.pcd.model;

public class Velocity {
    public double x;
    public double y;

    public Velocity(final double x, final double y){
        this.x = x;
        this.y = y;
    }

    public void change(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(final double x) {
        this.x = x;
    }

    public void setY(final double y) {
        this.y = y;
    }

    public double getModule() {
        return x*x + y*y;
    }
}
