
package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PShape;

public class Main extends PApplet{
    private boolean gameStart = true;
    public static int time = 0;
    private long timerStart;
    // PShape barge;

    private double rotationController = 0;
    public void settings(){
        size(1080, 720, P2D);
    }

    List<Character> keysHeld = new ArrayList<Character>();

    public void setup(){
        // size(1080, 800, P2D);
        background(0);
        frameRate(Constants.FRAMERATE);
        startTimer();
        GameInstance.getInstance().setGraphics(createGraphics(1080,720, P3D));
        // barge = loadShape("BARGE.obj");
    }

    public void draw(){
        Main.time = millis();
        handleMovement();
        if (gameStart){
            GameInstance.getInstance().draw();
            image(GameInstance.getInstance().getGraphics(), 0, 0);
            GameInstance.getInstance().drawHUD(this);
        }

    }

    public static int getTime() {
        return time;
    }

    public void startTimer(){
        timerStart = System.currentTimeMillis();
    }

    public void mousePressed(){

  }

    public void handleMovement() {
        Vector2D movement = new Vector2D(0, 0);
        boolean rotationChanged = false;

        if (keysHeld.contains('q')) {
            rotationChanged = true;
            if (rotationController < 1) {
                rotationController += 0.01;
            }
        }
        if (keysHeld.contains('e')) {
            rotationChanged = true;
            if (rotationController > -1) {
                rotationController -= 0.01;
            }
        }
        if (keysHeld.contains('w')) {
            movement.add(0, 1);
        }
        if (keysHeld.contains('s')) {
            movement.add(0, -1);
        }
        if (keysHeld.contains('a')) {
            movement.add(-1, 0);
        }
        if (keysHeld.contains('d')) {
            movement.add(1, 0);
        }
        GameInstance.getInstance().moveRobot(movement.normal());
        if (!rotationChanged) {
            if (Math.abs(rotationController) < 0.1) {
                rotationController = 0;
            } else {
                if (rotationController > 0) {
                    rotationController -= 0.03;
                } else {
                    rotationController += 0.03;
                }
            }
        }
        GameInstance.getInstance().rotateRobot((float) rotationController * 3);
    }

    public void keyPressed() {
        if (!keysHeld.contains(key)) {
            keysHeld.add(key);
        }

        if (key == 'i') {
            GameInstance.getInstance().createCoral();
        }
        switch (key) {
            case 'f':
                GameInstance.getInstance().startElevator();
                break;
            case 'g':
                GameInstance.getInstance().returnElevator();
                break;
            case '1':
                GameInstance.getInstance().coralScored(1);
                break;
            case '2':
                GameInstance.getInstance().coralScored(2);
                break;
            case '3':
                GameInstance.getInstance().coralScored(3);
                break;
            case '4':
                GameInstance.getInstance().coralScored(4);
                break;
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