package it.unibo.pcd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BodyFactory {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    private BodyFactory() { }

    public static Body getNewSingleBody(final Position pos, final Velocity velocity) {
        return new Body(pos, velocity, 0.01);
    }

    public static List<Body> getBodiesAtRandomPosition(final Boundary bounds, final int number) {
        final List<Body> list = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            list.add(new Body(getRandomPosition(bounds), getRandomVelocity(), 0.01));
        }
        return list;
    }

    private static Position getRandomPosition(final Boundary bounds) {
        final double x = bounds.getX0() + RANDOM.nextDouble()*(bounds.getX1() - bounds.getX0());
        final double y = bounds.getX0() + RANDOM.nextDouble()*(bounds.getX1() - bounds.getX0());
        return new Position(x, y);
    }

    private static Velocity getRandomVelocity() {
        final double dx = -1 + RANDOM.nextDouble()*2;
        final double speed = RANDOM.nextDouble()*0.05;
        return new Velocity(dx*speed,Math.sqrt(1 - dx*dx)*speed);
    }
}
