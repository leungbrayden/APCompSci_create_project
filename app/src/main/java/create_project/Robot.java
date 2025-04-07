package create_project;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends GameObject {
  boolean isRedAlliance;
  int teamNumber;
  final static double width = 30;
  final static double length = 30;
  private final static float MAX_SPEED = 186.f / 60;
  private final static float MAX_ACCEL = 314.96f / 60;

  public Robot(boolean isRedAlliance, int teamNumber) {
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
    this.position = new PVector(0.f, 0.f, 297.5f);
    this.velocity = new PVector(0, 0, 0);
    this.setVertices(
        new Vertex(width * 0.5, length * 0.5),
        new Vertex(-width * 0.5, length * 0.5),
        new Vertex(-width * 0.5, -length * 0.5),
        new Vertex(width * 0.5, -length * 0.5));
    this.weight = 115;
  }

  public void draw(PGraphics pg) {
    pg.pushMatrix();
    pg.fill(128,0,0);
    pg.translate(position.x, position.y, position.z);
    pg.box((float) width, 5, (float) length);
    pg.popMatrix();
  }

  public void move(PVector direction) {

    // System.out.println("move");

    if (this.velocity.mag() < MAX_SPEED) {
      this.acceleration = direction.normalize().mult(MAX_ACCEL);
    } else {
        this.acceleration = new PVector(0, 0, 0);
        this.velocity = this.velocity.normalize().mult(MAX_SPEED);
        // this.velocity.rotate(direction.heading());
    }
    System.out.println("acceleration: " + this.acceleration);
    System.out.println("velocity: " + this.velocity);
    System.out.println("position: " + this.position);

    // this.velocity = direction.normalize().mult(speed);

    // this.velocity.normalize();
    // this.velocity.mult(speed);
    // this.update();
    // this.position.add(direction.mult(speed));
  }

  public void stop() {
    // System.out.println("stop");
    this.acceleration = new PVector(0, 0, 0);
    this.velocity = new PVector(0, 0, 0);
  }
  
  //public PVector getPosition() {
  //    return position;
  //}
}
