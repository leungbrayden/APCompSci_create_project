package create_project;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends GameObject3D{
  boolean isRedAlliance;
  int teamNumber;
  private final static float MAX_SPEED = 186.f;
  private final static float MAX_ACCEL = 200.f;
  private static final double kP = 5.0;

  private boolean holdingCoral = true;
  private Coral coral = new Coral(new PVector(0.f, 2.25f, 0.f));
  
  private double width, depth = 30;

  private MotionProfile mechanism = new MotionProfile(0, 100, 100, 1000);


  public Robot(boolean isRedAlliance, int teamNumber) {
    super(GameObject3D.createBoxVertices(30,5,30), new Vector3D(0, 0, 297.5), 115, 0xFFFF0000);
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
  }

  public Robot(boolean isRedAlliance, int teamNumber, double width, double depth) {
    super(GameObject3D.createBoxVertices(width+6,5,depth+6), new Vector3D(0, 0, 297.5), 115, 0xFFFF0000);
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
  }

  public void startElevator() {
    mechanism.reset();
    mechanism.start();
  }

  public void move(Vector3D direction) {
    Vector3D appliedForce = direction.clone().scale((MAX_SPEED-this.getVelocity().magnitude())*kP);
    if (appliedForce.magnitude() > MAX_ACCEL) {
      appliedForce = appliedForce.clone().scale(MAX_ACCEL/appliedForce.magnitude());
    }
    this.setAcceleration(appliedForce);
    // this.setAcceleration(direction.clone().scale((MAX_SPEED-this.getVelocity().magnitude())*kP));
  }

  public void ejectCoral() {
    holdingCoral = false;
    coral.setVisible(false);
  }

  private double getWidth() { return this.width; }
  private double getDepth() { return this.depth; }

  @Override
  public void draw(PGraphics pg) {
    super.draw(pg);
    pg.pushMatrix();
    pg.translate((float) this.getPosition().x, (float) this.getPosition().y, (float) this.getPosition().z);
    double[] ypr = this.getOrientation().toYawPitchRoll();
    
    pg.fill(0xFFFF0000);
    pg.shininess(5.0f);
    pg.pushMatrix();
    pg.rotateZ((float) ypr[0]); // Yaw
    pg.rotateY((float) ypr[1]); // Pitch
    pg.rotateX((float) ypr[2]); // Roll
    pg.box((float) this.getWidth(), .5f, (float) this.getDepth());
    pg.popMatrix();

    pg.shininess(1.0f);

    // draw 4 sides of bumpers
    pg.fill(this.isRedAlliance ? 0xFFFF0000 : 0xFF0000FF);

    pg.pushMatrix();
    pg.translate((float)((this.getWidth()-3.f)/2.f), 0.f, 0.f);
    pg.rotateZ((float) ypr[0]); // Yaw
    pg.rotateY((float) ypr[1]); // Pitch
    pg.rotateX((float) ypr[2]); // Roll
    pg.box(3.f, 5.f, (float) this.getDepth());
    pg.popMatrix();

    pg.pushMatrix();
    pg.translate((float)(-(this.getWidth()-3.f)/2.f), 0.f, 0.f);
    pg.rotateZ((float) ypr[0]); // Yaw
    pg.rotateY((float) ypr[1]); // Pitch
    pg.rotateX((float) ypr[2]); // Roll
    pg.box(3.f, 5.f, (float) this.getDepth());
    pg.popMatrix();

    pg.pushMatrix();
    pg.translate(0.f, 0.f, (float) ((this.getDepth()-3.f)/2.f));
    pg.rotateZ((float) ypr[0]); // Yaw
    pg.rotateY((float) ypr[1]); // Pitch
    pg.rotateX((float) ypr[2]); // Roll
    pg.box((float) this.getWidth(), 5.f, 3.f);
    pg.popMatrix();

    pg.pushMatrix();
    pg.translate(0.f, 0.f, (float) (-(this.getDepth()-3.f)/2.f));
    pg.rotateZ((float) ypr[0]); // Yaw
    pg.rotateY((float) ypr[1]); // Pitch
    pg.rotateX((float) ypr[2]); // Roll
    pg.box((float) this.getWidth(), 5.f, 3.f);
    pg.popMatrix();

    if (holdingCoral) {
        coral.setVisible(true);
        pg.translate(0.f, 5.f, 0.f);
        // coral.draw(pg);
    }

    // if (Constants.DEBUG) {
    //     Logger.recordVector(this.getVelocity(), new PVector((float) this.getPosition().getX(), 10.f, (float) this.getPosition().getY()), 0xFF00FF00);
    // }

    pg.fill(0xFF000000);
    pg.box(10, (float) mechanism.getPosition(), 10);
    mechanism.update();
    
    pg.popMatrix();
  }
}
