package create_project;

import create_project.util.Vector2D;
import processing.core.PVector;

public class Robot extends Box {
  boolean isRedAlliance;
  int teamNumber;
  private final static float MAX_SPEED = 186.f;
  private final static float MAX_ACCEL = 314.96f;
  private static final double kP = 5.0;

  public Robot(boolean isRedAlliance, int teamNumber) {
    super(new PVector(0.f, 0.f, 297.5f), 115, 30., 5., 30., 0xFFFF0000);
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
  }

  public void move(Vector2D direction) {
    this.setAcceleration(direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP));
  }
}
