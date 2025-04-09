
package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class Main extends PApplet{

    public void settings(){
        size(1080, 720, P2D);
    }

    List<Character> keysHeld = new ArrayList<Character>();


    public void setup(){
        // size(1080, 800, P2D);
        background(0);
        frameRate(Constants.FRAMERATE);
        GameInstance.getInstance().setGraphics(createGraphics(1080,720, P3D));
    }

    public void draw(){
        handleMovement();
        GameInstance.getInstance().draw();
        image(GameInstance.getInstance().getGraphics(), 0, 0);
        GameInstance.getInstance().drawHUD(this);
    }

    public void mousePressed(){

  }

    public void handleMovement() {
        Vector3D movement = new Vector3D(0, 0, 0);
        if (keysHeld.contains('w')) {
            movement = movement.add(new Vector3D(0, 0, 1));
        }
        if (keysHeld.contains('s')) {
            movement = movement.add(new Vector3D(0, 0, -1));
        }
        if (keysHeld.contains('a')) {
            movement = movement.add(new Vector3D(-1, 0, 0));
        }
        if (keysHeld.contains('d')) {
            movement = movement.add(new Vector3D(1, 0, 0));
        }
        GameInstance.getInstance().moveRobot(movement.normalize());
    }

    public void keyPressed() {
        if (!keysHeld.contains(key)) {
            keysHeld.add(key);
        }

        // if (key == 'i') {
        //     GameInstance.getInstance().createCoral();
        // }

        if (key == 'f') {
            GameInstance.getInstance().startElevator();
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