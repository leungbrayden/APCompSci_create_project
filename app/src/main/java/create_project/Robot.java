package create_project;

import java.util.List;

import javax.lang.model.type.NullType;

import processing.core.PGraphics;
import processing.core.PVector;

public class Robot extends Box {
  boolean isRedAlliance;
  int teamNumber;
  private final static float MAX_SPEED = 186.f;
  private final static float MAX_ACCEL = 200.f;
  private static final double kP = 5.0;
  private static final float ARM_LENGTH = 20.f;

//   private boolean holdingCoral = true;
  private Coral coral = null;
  private double ELEVATOR_HEIGHT = 90;
  // private MotionProfile elevator = new MotionProfile(0, ELEVATOR_HEIGHT*(2./3.), 100, 25);
  // private MotionProfile arm = new MotionProfile(Math.PI*(3./2.), Math.PI*2, Math.PI/4., 100);
  private Elevator elevator = new Elevator(
    100, 
    100, 
    new Elevator.Stage(28, 27),
    new Elevator.Stage(30, 25.5),
    new Elevator.Stage(32, 23),
    new Elevator.Stage(10, 20.5)
    );

  private int lastCollisionTime = 0;

  private IntakeZone intakeZone = new IntakeZone(new PVector(0.f, 0.f, 0.f), 20., 10., 20.);
  private PVector intakeZonePosition = new PVector(0.f, 0.f, 15.f);

  public Robot(boolean isRedAlliance, int teamNumber) {
    super(new PVector(0.f, 0.f, 297.5f), 115, 34., 5., 34., 0xFFFF0000);
    this.isRedAlliance = isRedAlliance;
    this.teamNumber = teamNumber;
    System.out.println("inertia" + this.getInertia());
    this.setInertia(2000.0);
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
    // intakedCoral.setVisible(false);
    intakedCoral.setCollidable(false);
    intakedCoral.setPosition(new Vector2D());
    intakedCoral.setRotation(0);
    intakedCoral.setStatic(true);
    
    this.coral = intakedCoral;
  }

  public void startElevator() {
    elevator.stop(); // 54
    // elevator.reset(elevator.getPosition(), 54);
    elevator.reset(elevator.getPosition(), 36);
    // elevator.start();
    // arm.reset(arm.getPosition(), Math.PI*(3./4.));
    // arm.start();
  }

  public void returnElevator() {
    elevator.stop();
    elevator.reset(elevator.getPosition(), elevator.getMinPos());
    // arm.reset(arm.getPosition(), arm.getMinPos());
    // arm.start();
  }

  public Coral getCoral() {
    return coral;
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
    if (coral == null) {
      return;
    }
    coral.setCollidable(true);
    coral.setStatic(false);
    coral.setVelocity(new Vector2D(0,-100).rotate(this.getRotation()));
    this.coral = new Coral(new PVector()) {};
    this.coral = null;
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
    if (this.getVelocity().magnitude() < 0.2) {
      this.setVelocity(new Vector2D());
    }
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

        // if (Constants.DEBUG) {
        //     Logger.recordVector(this.getVelocity(), new PVector((float) this.getPosition().getX(), 10.f, (float) this.getPosition().getY()), 0xFF00FF00);
        // }

        pg.fill(0xFF000000);

        pg.translate(0.f, 2, -2.5f);
        pg.rotateX((float) -Math.PI* (8.f/180.f));

        elevator.draw(pg);
        elevator.update();

        if (coral != null) {
          // coral.setVisible(true);
          double offset = elevator.getPosition(elevator.getLastStage()) * Math.cos((82.*Math.PI)/180.);
          coral.setPosition(this.getPosition().copy().add(new Vector2D(0, -offset).rotate(this.getRotation())));
          coral.setY((float) (2. + elevator.getPosition(elevator.getLastStage()) * Math.sin((82.*Math.PI)/180.)));
          coral.setRotation((float)(Math.PI/2.f + this.getRotation()));
          // pg.translate(0.f, (float) (3 + elevator.getPosition()), -5.f);
          // pg.rotateY((float)(Math.PI/2.f));
          // pg.rotateX((float) -Math.PI* (8.f/180.f));
          // pg.printMatrix();
      }



        // pg.translate(-2, 2 +40/2, 0);
        // pg.box(2, 40, 2);
        // pg.popMatrix();
        
        // pg.translate(0, 42.f, 0);
        // pg.translate(0.f, (float)((ARM_LENGTH/2.)*Math.sin(arm.getPosition())), (float)((ARM_LENGTH/2)*Math.cos(arm.getPosition())));
        // pg.rotateX((float) -arm.getPosition());
        // pg.box(2, 2, ARM_LENGTH);
        // elevator.update();
        // arm.update();
        
        pg.popMatrix();

        if (Constants.DEBUG) {
            intakeZone.draw(pg);
        }
    }

    public void drawBoxFrame(PGraphics pg, double width, double height, boolean elevatorCarriage) {
        pg.pushMatrix();
        pg.translate((float)((width-1)/2.), 0, 0);
        pg.box(1, (float) height, 2);
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate((float)(-(width-1)/2.), 0, 0);
        pg.box(1, (float) height, 2);
        pg.popMatrix();
        if (elevatorCarriage) {
            pg.pushMatrix();
            pg.translate(0, (float) (height/2.), 0);
            pg.box((float) width, 1, 2);
            pg.popMatrix();
            pg.pushMatrix();
            pg.translate(0, (float) (-(height/2.)), 0);
            pg.box((float) width, 1, 2);
            pg.popMatrix();
            return;
        }
        pg.pushMatrix();
        pg.translate(0, (float) (height/2.), 2);
        pg.box((float) width, 1, 1);
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate(0, (float) (-(height/2.)), 0);
        pg.box((float) width, 1, 1);
        pg.popMatrix();
        return;
    }
}