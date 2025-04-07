package create_project;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends Box {
  boolean isRedAlliance;
  int teamNumber;
  private final static float MAX_SPEED = 186.f;
  private final static float MAX_ACCEL = 314.96f;
  private static final double kP = 5.0;

  private boolean holdingCoral = true;

  public Robot(boolean isRedAlliance, int teamNumber) {
    super(new PVector(0.f, 0.f, 297.5f), 115, 30., 5., 30., 0xFFFF0000);
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
  }

  public Robot(boolean isRedAlliance, int teamNumber, double width, double depth) {
    super(new PVector(0.f, 0.f, 297.5f), 115, width+6, 5., depth+6, 0xFFFF0000);
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
  }

  public void move(Vector2D direction) {
    this.setAcceleration(direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP));
  }

  @Override
  public void draw(PGraphics pg) {
        pg.pushMatrix();
        pg.translate((float) this.getPosition().getX(), (float) 0.5, (float) this.getPosition().getY());
        pg.rotateY((float) getRotation());
        pg.fill(200);
        pg.shininess(5.0f);

        pg.box((float) this.getWidth(), .5f, (float) this.getDepth());
        pg.shininess(1.0f);

        pg.fill(this.isRedAlliance ? 0xFFFF0000 : 0xFF0000FF);
        pg.pushMatrix();
        pg.translate((float)((this.getWidth()-3.f)/2.f), 0.f, 0.f);
        pg.box(3.f, 5.f, (float) this.getDepth());
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate((float)(-(this.getWidth()-3.f)/2.f), 0.f, 0.f);
        pg.box(3.f, 5.f, (float) this.getDepth());
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate(0.f, 0.f, (float) ((this.getDepth()-3.f)/2.f));
        pg.box((float) this.getWidth(), 5.f, 3.f);
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate(0.f, 0.f, (float) (-(this.getDepth()-3.f)/2.f));
        pg.box((float) this.getWidth(), 5.f, 3.f);
        pg.popMatrix();
        pg.popMatrix();
  }
}
