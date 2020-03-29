package it.unibo.pcd.model;

public class Boundary {
    private final double x0;
    private final double y0;
    private final double x1;
    private final double y1;

    public Boundary(final double x0, final double y0, final double x1, final double y1){
        this.x0=x0;
        this.y0=y0;
        this.x1=x1;
        this.y1=y1;
    }

    public double getX0(){
        return x0;
    }

    public double getX1(){
        return x1;
    }

    public double getY0(){
        return y0;
    }

    public double getY1(){
        return y1;
    }
}
