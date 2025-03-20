package create_project;

import java.util.ArrayList;
import java.util.List;

import create_project.GameObjects.Box;
import create_project.GameObjects.GameObject;
import create_project.GameObjects.Robot;
import processing.core.PGraphics;
import processing.core.PVector;

public class GameInstance{
    private static GameInstance instance = null;
    private List<GameObject> gameObjects;

    private Robot robot;

    private PGraphics graphics;

    private GameInstance() {
        gameObjects = new ArrayList<>();
        gameObjects.add(new Box(new PVector(0.f,-1.f,345.4375f + 20), Integer.MAX_VALUE, 317, 2, 690.875));
        robot = new Robot(true, 1234);
    }

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }
    
    public void moveRobot(PVector direction) {
        robot.move(direction);
    }

    public void reset() {
        instance = new GameInstance();
    }

    public void setGraphics(PGraphics graphics) {
        this.graphics = graphics;
        graphics.fill(255);
    }

    public void draw() {
        graphics.beginDraw();
        graphics.background(0);
        graphics.camera(0.0f,72.0f,-24.0f, robot.getPosition().x, robot.getPosition().y, robot.getPosition().z,
          0.0f, -1.0f, 0.0f);
        graphics.noStroke();
        robot.draw(graphics);
        for (GameObject gameObject : gameObjects) {
            gameObject.draw(graphics);
        }
        graphics.lights();
        graphics.endDraw();
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }
}
