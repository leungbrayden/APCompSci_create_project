
package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class Main extends PApplet{

    public void settings(){
        size(1080, 800, P2D);
    }

    List<Character> keysHeld = new ArrayList<Character>();

    public void setup(){
        // size(1080, 800, P2D);
        background(0);
        frameRate(60);
        GameInstance.getInstance().setGraphics(createGraphics(1080,800, P3D));
    }

    public void draw(){
        handleMovement();
        GameInstance.getInstance().draw();
        image(GameInstance.getInstance().getGraphics(), 0, 0);
    }

    public void mousePressed(){

  }

    public void handleMovement() {
        PVector movement = new PVector(0, 0, 0);
        if (keysHeld.contains('w')) {
            movement.add(0, 0, 1);
        }
        if (keysHeld.contains('s')) {
            movement.add(0, 0, -1);
        }
        if (keysHeld.contains('a')) {
            movement.add(-1, 0, 0);
        }
        if (keysHeld.contains('d')) {
            movement.add(1, 0, 0);
        }
        GameInstance.getInstance().moveRobot(movement.normalize());
    }

    public void keyPressed() {
        if (!keysHeld.contains(key)) {
            keysHeld.add(key);
        }
    }

    public void keyReleased() {
        keysHeld.remove(Character.valueOf(key));
    }
	
	public static void main(){
		String[] processingArgs = {"MySketch"};
		Main app = new Main();
		PApplet.runSketch(processingArgs, app);
	}
}