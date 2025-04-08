package create_project;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends Box {
  boolean isRedAlliance;
  int teamNumber;
  private final static float MAX_SPEED = 186.f;
  private final static float MAX_ACCEL = 200.f;
  private static final double kP = 5.0;

  private boolean holdingCoral = true;
  private Coral coral = new Coral(new PVector(0.f, 2.25f, 0.f));

  private MotionProfile mechanism = new MotionProfile(0, 100, 100, 1000);


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

  public void startElevator() {
    mechanism.reset();
    mechanism.start();
  }

  public void move(Vector2D direction) {
    Vector2D appliedForce = direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP);
    if (appliedForce.magnitude() > MAX_ACCEL) {
      appliedForce = appliedForce.copy().scale(MAX_ACCEL/appliedForce.magnitude());
    }
    this.setAcceleration(appliedForce);
    // this.setAcceleration(direction.copy().scale((MAX_SPEED-this.getVelocity().magnitude())*kP));
  }

  public void ejectCoral() {
    holdingCoral = false;
    coral.setVisible(false);
  }
  
  @Override
  public void applyImpulse(Vector2D impulse, Vector2D point, double otherInvMass) {
    super.applyImpulse(impulse, point, otherInvMass);
    System.out.println("Impulse: " + impulse.magnitude());
    System.out.println("Impulse point: " + point.getX() + ", " + point.getY());
    // if ()
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

        if (holdingCoral) {
            coral.setVisible(true);
            pg.translate(0.f, 5.f, 0.f);
            // coral.draw(pg);
        }

        if (Constants.DEBUG) {
            Logger.recordVector(this.getVelocity(), new PVector((float) this.getPosition().getX(), 10.f, (float) this.getPosition().getY()), 0xFF00FF00);
        }

        pg.fill(0xFF000000);
        pg.box(10, (float) mechanism.getPosition(), 10);
        mechanism.update();
        
        pg.popMatrix();
  }
}
