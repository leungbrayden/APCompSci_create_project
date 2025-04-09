package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class GameInstance{
    private static GameInstance instance = null;
    private List<GameObject3D> gameObjects;

    private Robot robot;

    private PGraphics graphics;

    private PShape reef;

    private GameInstance() {
        gameObjects = new ArrayList<>();
        gameObjects.add(GameObject3D.createBox(new Vector3D(0, -50, 345.4375f + 20), 317, 100, 690.875, 0).setStatic(true).setGravityEnabled(false));

        robot = new Robot(true, 1234);
        gameObjects.add(robot.setGravityEnabled(true));

        gameObjects.add(GameObject3D.createBox(new Vector3D(0, 0f, 345.4375f + 20), 10, 10, 10, 10).setGravityEnabled(true));
        // robot = new Robot(true, 1234);
        // gameObjects.add(robot);
        // gameObjects.add(new Coral(new PVector(0.f, 2.25f, 100.f + 20)));

        // // gameObjects.add(new Box(new PVector(0.f,-1.f,345.4375f + 20), 20, 4.5, 12, 4.5, 0));

        // float fieldWidth = 317;
        // float fieldDepth = 690f;
        // float wallThickness = 2;
        // float wallHeight = 50;
        // float reefAngle;

        // gameObjects.add(new Box(new PVector(0,0.f,fieldDepth), 0, fieldWidth, 40, 10,0x55FFFFFF).isStatic()); 
        // gameObjects.add(new Box(new PVector(0,0.f, 0), 0, fieldWidth, 40, 20,0x55FFFFFF).isStatic()); 
        // gameObjects.add(new Box(new PVector(-fieldWidth/2,0.f,(fieldDepth/2)+5), 0, 10, 40, fieldDepth-25, 0x55FFFFFF).isStatic());
        // gameObjects.add(new Box(new PVector(fieldWidth/2,0.f, (fieldDepth/2)+5), 0, 10, 40, fieldDepth-25, 0x55FFFFFF).isStatic());

        // // sample rotation constructor: 
        // for (int i = 0; i > 6; i++){
        // reefAngle = (float) (i * Math.PI/3);
        // gameObjects.add(new Box(new PVector(0.f,0.f,0.f), 20, 4.5, 12, 4.5, 0, (float) Math.PI/3));
        // }
    }

    public void draw() {
        graphics.beginDraw();
        graphics.background(10);
        graphics.camera(0.0f,72.0f,-64.0f, (float) robot.getPosition().x, 0.f, (float) robot.getPosition().z,
          0.0f, -1.0f, 0.0f);
        this.graphics.lights();
        for (GameObject3D obj : gameObjects) {
            obj.update();
            obj.draw(this.graphics);
            for (GameObject3D other : gameObjects) {
                if (obj != other) {
                    obj.resolveCollision(other);
                }
            }
        }
        graphics.endDraw();
        // for (GameObject gameObject : gameObjects) {
        //     for (GameObject otherGameObject : gameObjects) {
        //         if (gameObject != otherGameObject) {
        //             gameObject.checkCollision(otherGameObject);
        //         }
        //     }
        // }
        // graphics.beginDraw();
        // graphics.background(50);
        // 
        // graphics.perspective();
        // graphics.noStroke();


        // for (GameObject gameObject : gameObjects) {
        //     gameObject.update();
        //     gameObject.draw(graphics);
        // }
        // if (reef != null) {
        //     graphics.pushMatrix();
        //     graphics.translate(0, 0, 100);
        //     graphics.shape(reef);
        //     graphics.popMatrix();
        // }
        // graphics.endDraw();
    }

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }
    
    public void moveRobot(Vector3D direction) {
        robot.move(direction);
    }

    public void startElevator() {
        robot.startElevator();
    }
    
    // public void rotateRobot(float angle) {
    //     robot.setAngularVelocity(angle);
    // }

    public void reset() {
        instance = new GameInstance();
    }
    /**
     *  sets graphics object for the game instance and initializes objects that require it
     *  @param graphics the graphics object to set
     */
    public void setGraphics(PGraphics graphics) {
        System.out.println("Setting graphics");
        this.graphics = graphics;

        Logger.init(graphics);

        String dataDir = System.getenv("DATA_PATH");

        if (dataDir == null) {
            System.out.println("DATA_PATH not set, ");
            System.out.println("    macos/linux: export DATA_PATH=/path/to/data");
            System.out.println("    windows: set DATA_PATH=C:\\path\\to\\data");
            System.exit(1);
        }

        try {
            reef = graphics.loadShape(dataDir+"REEF.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        reef.scale((float)(39.37 / 1.196));
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }

    public void drawHUD(PApplet app) {

    }

    // public void createCoral() {
    //     Coral coral = new Coral(new PVector(0.f, 70.f, 50.f));
    //     gameObjects.add(coral);
    // }
}
