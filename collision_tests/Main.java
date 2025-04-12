package collision_tests;

import java.util.ArrayList;
import java.util.List;

import collision_tests.util.Constants;
import collision_tests.util.Logger;
import collision_tests.util.Vector2D;
import processing.core.PApplet;

public class Main extends PApplet{
    List<Character> keysHeld = new ArrayList<Character>();
    GameObject square;
    Robot robot;
    List<GameObject> objects = new ArrayList<GameObject>();

    public void settings(){
        size(1080, 800, P2D);
    }

    public void setup(){
        frameRate(Constants.FRAMERATE);
        square = new GameObject(new Vector2D[] {
            new Vector2D(25,25),
            new Vector2D(-25, 25),
            new Vector2D(-25, -25),
            new Vector2D(25, -25)
        });
        robot = new Robot();
        objects.add(square);
        objects.add(robot);

        strokeWeight(2);

        square.setPosition(new Vector2D(500,500));
    }

    public void draw(){
        handleMovement();
        background(50);
        for (GameObject obj : objects) {
            obj.update();
            obj.draw(this);
            for (GameObject other : objects) {
                if (obj != other) {
                    obj.checkCollision(other);
                }
            }
        }
        Logger.draw(this);
    }

    public void handleMovement() {
        Vector2D movement = new Vector2D(0, 0);

        if (keysHeld.contains('q')) {
            robot.setAngularVelocity(-1);
        }
        if (keysHeld.contains('e')) {
            robot.setAngularVelocity(1);
        }
        if (keysHeld.contains('w')) {
            movement.add(0, -1);
        }
        if (keysHeld.contains('s')) {
            movement.add(0, 1);
        }
        if (keysHeld.contains('a')) {
            movement.add(-1, 0);
        }
        if (keysHeld.contains('d')) {
            movement.add(1, 0);
        }
        robot.move(movement.normal());
    }

    public void keyPressed() {
        if (!keysHeld.contains(key)) {
            keysHeld.add(key);
        }
    }

    public void keyReleased() {
        keysHeld.remove(Character.valueOf(key));
    }

    public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		Main app = new Main();
		PApplet.runSketch(processingArgs, app);
	}
}