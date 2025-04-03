package create_project;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends GameObject {
  boolean isRedAlliance;
  int teamNumber;
  final static double width = 30;
  final static double length = 30;
  private final static float speed = 133.8f / 60;

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
  }

  public void draw(PGraphics pg) {
    pg.pushMatrix();
    pg.fill(128,0,0);
    pg.translate(position.x, position.y, position.z);
    pg.box((float) width, 5, (float) length);
    pg.popMatrix();
  }

  public void move(PVector direction) {
    this.position.add(direction.mult(speed));
  }
  
  //public PVector getPosition() {
  //    return position;
  //}
}
