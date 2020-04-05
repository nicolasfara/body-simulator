package it.unibo.pcd.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Body {
    private final Position pos;
    private final Velocity vel;
    private final double radius;
    private ReentrantLock pLock = new ReentrantLock();
    private ReentrantLock vLock = new ReentrantLock();


    public Body(final Position pos, final Velocity vel, final double radius){
        this.pos = pos;
        this.vel = vel;
        this.radius = radius;
    }

    public double getRadius() {

        return radius;
    }

    public Position getPos(){
       pLock.lock();
       try {
           return pos;
       }finally {
           pLock.unlock();
       }
    }

    public Velocity getVel(){
        vLock.lock();
        try {
            return vel;
        }finally {
            vLock.unlock();
        }
    }

    /**
     * Update the position, according to current velocity
     *
     * @param dt time elapsed.
     */
    public void updatePos(final double dt) {
        //synchronized (pos) {
        pLock.lock();
        try{
            final double newPosX = pos.getX() + vel.getX() * dt;
            final double newPosY = pos.getY() + vel.getY() * dt;
            pos.change(newPosX, newPosY);
        }finally {
            pLock.unlock();
        }
    }

    /**
     * Change the velocity.
     *
     * @param vx Velocity on x coordinate.
     * @param vy Velocity on y coordinate.
     */
    public void changeVel(final double vx, final double vy) {
       // synchronized (vel){
        vLock.lock();
        try{
            vel.change(vx, vy);
        }finally {
            vLock.unlock();
        }
    }

    /**
     * Computes the distance from the specified body
     *
     * @param b The body for calculate the distance to.
     * @return The distance from the argument body.
     */
    public double getDistance(final Body b) {
        final double dx = pos.getX() - b.getPos().getX();
        final double dy = pos.getY() - b.getPos().getY();
        return Math.sqrt(dx*dx + dy*dy);
    }

    /**
     * Check if there is collision with the specified body
     * @param b The body to check if collide with the current body.
     * @return true if the current body collide with the argument body.
     */
    public boolean collideWith(final Body b) {
        final double distance = getDistance(b);
        return distance < radius + b.getRadius();
    }

    /**
     * Check if there collisions with the boundary and update the
     * position and velocity accordingly.
     *
     * @param bounds Bounds of the world.
     */
    public void checkAndSolveBoundaryCollision(final Boundary bounds) {
        if (pos.getX() > bounds.getX1()){
            pos.change(bounds.getX1(), pos.getY());
            vel.change(-vel.getX(), vel.getY());
        } else if (pos.getX() < bounds.getX0()){
            pos.change(bounds.getX0(), pos.getY());
            vel.change(-vel.getX(), vel.getY());
        } else if (pos.getY() > bounds.getY1()){
            pos.change(pos.getX(), bounds.getY1());
            vel.change(vel.getX(), -vel.getY());
        } else if (pos.getY() < bounds.getY0()){
            pos.change(pos.getX(), bounds.getY0());
            vel.change(vel.getX(), -vel.getY());
        }
    }

    public static void solveCollision(final Body b1, final Body b2) {
        final Position xB1 = b1.getPos();
        final Position xB2 = b2.getPos();
        final Velocity vB1 = b1.getVel();
        final Velocity vB2 = b2.getVel();
        final Velocity updateVelB1 = updateVelocity(xB1, xB2, vB1, vB2);
        final Velocity updateVelB2 = updateVelocity(xB2, xB1, vB2, vB1);

        b1.changeVel(updateVelB1.x, updateVelB1.y);
        b2.changeVel(updateVelB2.x, updateVelB2.y);
    }

    private static Velocity updateVelocity(final Position x1, final Position x2, final Velocity v1, final Velocity v2) {
        final double xDx = x1.getX() - x2.getX();
        final double xDy = x1.getY() - x2.getY();
        final double vDx = v1.getX() - v2.getX();
        final double vDy = v1.getY() - v2.getY();
        final double fact = (xDx * vDx + xDy * vDy) / (xDx * xDx + xDy * xDy);
        final double v1x = v1.getX() - xDx * fact;
        final double v1y = v1.getY() - xDy * fact;

        return new Velocity(v1x, v1y);
    }
}
