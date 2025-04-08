package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class GameInstance{
    private static GameInstance instance = null;
    private List<GameObject> gameObjects;

    private Robot robot;

    private PGraphics graphics;

    private GameInstance() {
        gameObjects = new ArrayList<>();
        Box floor = new Box(new PVector(0.f,-1.1f,345.4375f + 20), Integer.MAX_VALUE, 317, 2, 690.875, 127);
        floor.notCollidable();
        floor.setStatic(true);
        
        gameObjects.add(floor);
        robot = new Robot(true, 1234);
        gameObjects.add(robot);
        gameObjects.add(new Coral(new PVector(0.f, 2.25f, 100.f + 20)));

        // gameObjects.add(new Box(new PVector(0.f,-1.f,345.4375f + 20), 20, 4.5, 12, 4.5, 0));

        float fieldWidth = 317;
        float fieldDepth = 690f;
        float wallThickness = 2;
        float wallHeight = 50;
        float reefAngle;

        gameObjects.add(new Box(new PVector(0,0.f,fieldDepth), Integer.MAX_VALUE, fieldWidth, 40, 10,255).isStatic()); 
        gameObjects.add(new Box(new PVector(0,0.f, 20), Integer.MAX_VALUE, fieldWidth, 20, 20,255).isStatic()); 
        gameObjects.add(new Box(new PVector(-fieldWidth/2,0.f,(fieldDepth/2)+5), Integer.MAX_VALUE, 10, 40, fieldDepth-25, 255).isStatic());
        gameObjects.add(new Box(new PVector(fieldWidth/2,0.f, (fieldDepth/2)+5), Integer.MAX_VALUE, 10, 40, fieldDepth-25, 255).isStatic());

        // sample rotation constructor: 
        for (int i = 0; i > 6; i++){
        reefAngle = i * Math.PI/3;
        gameObjects.add(new Box(new PVector(0.f,0.f,0.f), 20, 4.5, 12, 4.5, 0, (float) Math.PI/3));
    }
    }

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }
    
    public void moveRobot(Vector2D direction) {
        robot.move(direction);
    }
    
    public void rotateRobot(float angle) {
        robot.setAngularVelocity(angle);
    }

    public void reset() {
        instance = new GameInstance();
    }

    public void setGraphics(PGraphics graphics) {
        Logger.init(graphics);
        this.graphics = graphics;
        graphics.fill(255);
    }

    public void draw() {
        for (GameObject gameObject : gameObjects) {
            for (GameObject otherGameObject : gameObjects) {
                if (gameObject != otherGameObject) {
                    gameObject.checkCollision(otherGameObject);
                }
            }
        }
        graphics.beginDraw();
        graphics.background(50);
        graphics.camera(0.0f,72.0f,-24.0f, (float) robot.getPosition().getX(), 0.f, (float) robot.getPosition().getY(),
          0.0f, -1.0f, 0.0f);
        graphics.perspective();
        graphics.noStroke();
        graphics.pushMatrix();
        graphics.translate(0f, 0f, (float) robot.getPosition().getY());
        graphics.popMatrix();

        robot.draw(graphics);
        for (GameObject gameObject : gameObjects) {
            gameObject.update();
            gameObject.draw(graphics);
        }
        graphics.lights();
        graphics.endDraw();
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }

    public void drawHUD(PApplet app) {

    }
}
