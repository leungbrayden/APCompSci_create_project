package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class TestPhysics extends PApplet {
    List<GameObject3D> gameObjects = new ArrayList<GameObject3D>();
    public void settings() {
        size(1080, 720, P3D);
    }

    public void setup() {
        frameRate(Constants.FRAMERATE);
        gameObjects.add(GameObject3D.createBox(new Vector3D(0,400,0), 80, 50, 50, 1));
        gameObjects.add(GameObject3D.createBox(new Vector3D(500,400,0), 50, 50, 50, 1));
    }

    public void draw() {
        background(0);
        lights();
        for (GameObject3D obj : gameObjects) {
            obj.update();
            obj.draw(this);
            for (GameObject3D other : gameObjects) {
                if (obj != other) {
                    obj.resolveCollision(other);
                }
            }
        }


    }

    public void keyPressed() {
        if (key == 'a') {
            gameObjects.get(0).setVelocity(new Vector3D(-100, 0, 0));

        } else if (key == 'd') {
            gameObjects.get(0).setVelocity(new Vector3D(100, 0, 0));
        } else if (key == 'w') {
            gameObjects.get(0).setVelocity(new Vector3D(0, -100, 0));
        } else if (key == 's') {
            gameObjects.get(0).setVelocity(new Vector3D(0, 100, 0));
        } else if (key == ' ') {
            gameObjects.get(0).setVelocity(new Vector3D(0, 0, 0));
        }
    }
    
    public static void main(String[] args){
		String[] processingArgs = {"MySketch"};
		TestPhysics app = new TestPhysics();
		PApplet.runSketch(processingArgs, app);
	}
}
