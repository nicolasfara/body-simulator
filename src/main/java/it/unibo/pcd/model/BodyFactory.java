package it.unibo.pcd.model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BodyFactory {
    private static final Random random = new Random(System.currentTimeMillis());

    public static Body getNewSingleBody(Position pos, Velocity velocity) {
        return new Body(pos, velocity, 0.01);
    }

    public static List<Body> getBodiesAtRandomPosition(final Boundary bounds, final int number) {
        return Stream.generate(() -> new Body(getRandomPosition(bounds), getRandomVelocity(), 0.01))
                .limit(number)
                .collect(Collectors.toList());
    }

    private static Position getRandomPosition(final Boundary bounds) {
        double x = bounds.getX0() + random.nextDouble()*(bounds.getX1() - bounds.getX0());
        double y = bounds.getX0() + random.nextDouble()*(bounds.getX1() - bounds.getX0());
        return new Position(x, y);
    }

    private static Velocity getRandomVelocity() {
        double dx = -1 + random.nextDouble()*2;
        double speed = random.nextDouble()*0.05;
        return new Velocity(dx*speed,Math.sqrt(1 - dx*dx)*speed);
    }
}
