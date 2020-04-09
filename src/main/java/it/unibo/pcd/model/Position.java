package it.unibo.pcd.model;

public class Position {
    private double x;
    private double y;

    public Position(final double x, final double y){
        this.x = x;
        this.y = y;
    }

    public synchronized void change(final double x, final double y){
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
}
