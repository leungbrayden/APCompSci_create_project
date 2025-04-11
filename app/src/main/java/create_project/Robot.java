package create_project;

import java.util.List;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends Box {
  boolean isRedAlliance;
  int teamNumber;
  private final static float MAX_SPEED = 186.f;
  private final static float MAX_ACCEL = 200.f;
  private static final double kP = 5.0;

//   private boolean holdingCoral = true;
  private Coral coral = null;

  private MotionProfile mechanism = new MotionProfile(0, 50, 100, 25);

  private int lastCollisionTime = 0;

  private IntakeZone intakeZone = new IntakeZone(new PVector(0.f, 0.f, 0.f), 20., 10., 20.);
  private PVector intakeZonePosition = new PVector(0.f, 0.f, 15.f);

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

  public void checkIntake(List<Coral> corals) {
    var intakedCoral = intakeZone.checkCollision(corals);
    if (intakedCoral == null || coral != null) {
      return;
    }
    this.coral = intakedCoral;

  }

  public void startElevator() {
    mechanism.reset(mechanism.getPosition(), 50);
    mechanism.start();
  }

  public void returnElevator() {
    mechanism.reset(mechanism.getPosition(), 0);
    mechanism.start();
  }

  public void move(Vector2D direction) {
    if (lastCollisionTime + 100 > Main.getTime()) {
        this.setAcceleration(new Vector2D());
      return;
    }
    if (direction.magnitude() < 0.1) {
      this.setAcceleration(this.getVelocity().copy().scale(-1.));
      return;
    }
    Vector2D appliedForce = direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP);
    if (appliedForce.magnitude() > MAX_ACCEL) {
      appliedForce = appliedForce.copy().scale(MAX_ACCEL/appliedForce.magnitude());
    }
    this.setAcceleration(appliedForce);
    // this.setAcceleration(direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP));
  }

  public void ejectCoral() {

    coral.setVisible(false);
  }
  
  @Override
  public void applyImpulse(Vector2D impulse, Vector2D point, double otherInvMass) {
    super.applyImpulse(impulse, point, otherInvMass);
    if (impulse.magnitude() > 100) {
        // System.out.println(impulse.magnitude());
        this.lastCollisionTime = Main.getTime();
    }
  }

  @Override
  public void handleCollision(GameObject other, Vector2D collisionNormal, double minOverlap, Vector2D collisionPoint) {
    super.handleCollision(other, collisionNormal, minOverlap, collisionPoint);
  }

  @Override
  public void update() {
    super.update();
    PVector intakeZoneOffset = new PVector(
        (float)(intakeZonePosition.x*Math.cos(this.getRotation()) - intakeZonePosition.z*Math.sin(this.getRotation())), 
        0.f, 
        (float)(intakeZonePosition.x*Math.sin(this.getRotation()) + intakeZonePosition.z*Math.cos(this.getRotation())));
    intakeZone.setPosition(
        PVector.add(
            new PVector((float) this.getPosition().getX(), 5.f, (float) this.getPosition().getY()),
            intakeZoneOffset));
    intakeZone.setRotation(0, this.getRotation(), 0);
  }

  @Override
  public void draw(PGraphics pg) {
        pg.pushMatrix();
        pg.translate((float) this.getPosition().getX(), (float) 0.5, (float) this.getPosition().getY());
        pg.rotateY((float) -getRotation());
        pg.fill(200);
        pg.shininess(5.0f);

        pg.box((float) this.getWidth(), .5f, (float) this.getDepth());
        pg.shininess(1.0f);

        // draw 4 sides of bumpers
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

        if (Constants.DEBUG) {
            Logger.recordVector(this.getVelocity(), new PVector((float) this.getPosition().getX(), 10.f, (float) this.getPosition().getY()), 0xFF00FF00);
        }

        if (coral != null) {
            pg.pushMatrix();
            coral.setVisible(true);
            pg.translate(0.f, (float) mechanism.getPosition(), 0.f);
            coral.draw(pg);
            pg.popMatrix();
        }

        pg.fill(0xFF000000);
        pg.translate(0, (float)mechanism.getPosition()/2.f, 0);
        pg.box(10, (float) mechanism.getPosition(), 10);
        mechanism.update();
        
        pg.popMatrix();

        if (Constants.DEBUG) {
            intakeZone.draw(pg);
        }
  }
}