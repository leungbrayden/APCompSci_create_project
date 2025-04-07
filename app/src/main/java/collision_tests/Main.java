package collision_tests;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet{
    private List<GameObject> gameObjects = new ArrayList<>();
    List<Character> keysHeld = new ArrayList<Character>();
    Robot robot;

    public void settings(){
        size(1080, 800, P2D);
    }

    public void setup(){
        // background();
        frameRate(240);
        // gameObjects.add(new GameObject(new PVector(0f, 0f), 1, 50, 50));
        robot = new Robot(1234);
        gameObjects.add(robot);
        gameObjects.add(new GameObject(new PVector(500,500), 100., 50, 50));
        // gameObjects.add(new GameObject(new PVector(500, 540), 1., 50, 50));
        rectMode(CENTER);
        strokeWeight(2);
        Logger.init(this);
        
    }

    public void draw(){
        background(50);
        handleMovement();
        for (GameObject obj : gameObjects) {
            obj.draw(this);
            obj.update();
            for (GameObject other : gameObjects) {
                if (obj != other) {
                    obj.checkCollision(other);
                }
            }
        }
    }

    public void handleMovement() {
        PVector movement = new PVector(0, 0);
        // System.out.println("Keys held: " + keysHeld);
        if(keysHeld.contains('q')) {
            robot.rotate(-PI/60);
        }
        if(keysHeld.contains('e')) {
            robot.rotate(PI/60);
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
        // System.out.println("Movement: " + movement);
        if (movement.equals(new PVector(0, 0))) {
            robot.stop();
            return;
        }
        robot.move(movement.normalize());
    }

    public void keyPressed() {
        if (key == 'l') {
            robot.applyImpulse(new PVector(10, 10), new PVector(-5, -5).normalize());
        }
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