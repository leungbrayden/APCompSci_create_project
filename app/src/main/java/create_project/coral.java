package create_project;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class Coral extends GameObject{

    private final static float width = 11.875f;
    private final static float radius = 2.25f;
    private float objectY = 0.f;
    private float velocityY = 0.f;
    private float accelerationY = -100.0f;


    public Coral(PVector position) {
        super(new Vector2D[] {
            new Vector2D(width/2., radius),
            new Vector2D(width/2., -radius),
            new Vector2D(-width/2., -radius),
            new Vector2D(-width/2., radius),
        }, new Vector2D(position.x, position.z), 1);
        this.objectY = position.y;
    }

    @Override
    public void update() {
        if (objectY > 20.f) {
            this.notCollidable();
        } else {
            this.setCollidable(true);
        }
        if (getStatic()) {
            return;
        }
        if (objectY > 2.25f) {
            super.update(false);
            objectY += getVelocityY() * Constants.deltaTime;
            setVelocityY((float) (getVelocityY() + getAccelerationY() * Constants.deltaTime));
        } else {
            objectY = 2.25f;
            super.update();
        }
        
    }

    @Override
    public void draw(PGraphics pg) {
        if(!isVisible()) {
            return;
        }
        // draw cylinder
        pg.pushMatrix();
        pg.translate((float) getPosition().getX(), objectY, (float)getPosition().getY());
        pg.rotateY((float) -getRotation());
        pg.fill(0xFFFFFFFF);
        pg.beginShape(18);
        for (int i = 0; i < 20; i++) {
            pg.vertex(width/2.f, (float) Math.sin((Math.PI*2) / (i / 20.f)) * radius, (float) Math.cos((Math.PI*2) / (i / 20.f)) * radius);
            pg.vertex(-width/2.f, (float) Math.sin((Math.PI*2) / (i / 20.f)) * radius, (float) Math.cos((Math.PI*2) / (i / 20.f)) * radius);
        }
        pg.endShape(2);
        // pg.fill(0x5500FF00);
        // pg.box(width, radius*2, radius*2);
        pg.popMatrix();
    }

    public static void draw(PGraphics pg, PVector position, double rotationX, double rotationY, double rotationZ) {
        pg.fill(0xFFFFFFFF);
        pg.translate(position.x, position.y, position.z);
        pg.rotateY((float)rotationY);
        pg.rotateX((float)rotationX);
        pg.rotateZ((float)rotationZ);
        pg.beginShape(18);
        for (int i = 0; i < 20; i++) {
            pg.vertex(width/2.f, (float) Math.sin((Math.PI*2) / (i / 20.f)) * radius, (float) Math.cos((Math.PI*2) / (i / 20.f)) * radius);
            pg.vertex(-width/2.f, (float) Math.sin((Math.PI*2) / (i / 20.f)) * radius, (float) Math.cos((Math.PI*2) / (i / 20.f)) * radius);
        }
        pg.endShape(2);
    }

    public void setY(float y) {
        this.objectY = y;
    }

    public float getY() {
        return objectY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getVelocityY() {
        return velocityY;
    }
    public void setAccelerationY(float accelerationY) {
        this.accelerationY = accelerationY;
    }
    public float getAccelerationY() {
        return accelerationY;
    }
}
