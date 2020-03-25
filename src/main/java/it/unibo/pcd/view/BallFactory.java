package it.unibo.pcd.view;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;

import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.*;

public class BallFactory implements EntityFactory {

    private static final Random random = new Random();

    public Entity newBall() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.setFixtureDef(new FixtureDef().density(0.3f).restitution(1.0f));
        physics.setOnPhysicsInitialized(() -> physics.setLinearVelocity(5.0f, 5.0f));

        return entityBuilder()
                .bbox(new HitBox(BoundingShape.circle(5)))
                .with(physics)
                .with(new CollidableComponent(true))
                .at(new Point2D(random(0, 620), random(0, 620)))
                .build();
    }
}
