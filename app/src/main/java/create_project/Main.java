
package create_project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;

public class Main extends PApplet{
    public static int time = 0;
    GUI title = new GUI();
    GUI select = new GUI();
    GUI settings = new GUI();
    GUI tutorial = new GUI();
    // PShape barge;
    public Scene scene = Scene.TITLE;
    private double rotationController = 0;
    public static int currentTime;

    public void settings(){
        size(1080, 720, P2D);
    }

    List<Character> keysHeld = new ArrayList<Character>();

    public void setup(){
        // size(1080, 800, P2D);
        background(0);
        frameRate(Constants.FRAMERATE);
        // startTimer();
        GameInstance.getInstance().setGraphics(createGraphics(1080,720, P3D));
        // barge = loadShape("BARGE.obj");
        
        title.addButton(new Button(1080/2, 720/2, 500, 150, "Start", () -> {scene = Scene.SELECT;}));
        title.addButton(new Button(1080/2, 720/2 + 150, 300, 80, "Settings", () -> {scene = Scene.SETTINGS;}));
        title.addButton(new Button(1080/2, 720/2 + 280, 200, 60, "Tutorial", () -> {scene = Scene.TUTORIAL;}));
        title.noBackground();

        String[] increment = {"2438","6328","2056","2910","359"};
    
        for (int i = 0; i < increment.length; i++){
        select.addButton(new Button(1080/6 + i*180, 720/2, 120, 40, increment[i], () -> {scene = Scene.GAME; Main.startTimer();}));
        }

        
    }


    public void draw() {
        Main.time = millis();
        switch (scene) {
        case TITLE:
          background(Constants.backgroundColor);
          title.drawScreen(this);
          return;
        case SELECT:
          select.drawScreen(this);
          return;
        case GAME:
          drawGame();
          return;
        case SETTINGS:
          settings.drawScreen(this);
          return;
        case TUTORIAL:
          tutorial.drawScreen(this);
          return;
        }
      }


    public void drawGame(){
        handleMovement();
            GameInstance.getInstance().draw();
            image(GameInstance.getInstance().getGraphics(), 0, 0);
            GameInstance.getInstance().drawHUD(this);
        text(Math.round(frameRate), 10, 10);
    }

    public static int getTime() {
        return time;
    }

    public static void startTimer(){
        Main.currentTime = Main.getTime();
    }

    public static int getGameTime(){
       
        return getTime() - Main.currentTime;
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
        if (Constants.FOLLOW_ROBOT) {
            movement = movement.rotate(GameInstance.getInstance().getRobot().getRotation()).scale(-1.);
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
        switch (key) {
            case 'u':
                GameInstance.getInstance().createCoral();
                break;
            case 'i':
                GameInstance.getInstance().getRobot().raiseL4();
                break;
            case 'o':
                GameInstance.getInstance().getRobot().raiseL3();
                break;
            case 'p':
                GameInstance.getInstance().getRobot().raiseL2();
                break;
            case ' ':
                GameInstance.getInstance().ejectCoral();
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

    public void mouseClicked() {
        switch (scene) {
        case TITLE:
          title.checkClick(this);
          return;
        case SELECT:
          select.checkClick(this);
          return;
        case GAME:
          return;
        case SETTINGS:
          settings.checkClick(this);
        case TUTORIAL:
          tutorial.checkClick(this);
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