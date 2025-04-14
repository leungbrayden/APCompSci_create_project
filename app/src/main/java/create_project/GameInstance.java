package create_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class GameInstance{
    private static GameInstance instance = null;
    private List<GameObject> gameObjects;

    private List<Coral> corals;

    private Robot robot;
    private Reef blueReef;
    private Barge blueBarge;

    private PGraphics graphics;

    private PShape reef;
    private PShape barge;

    private int[] levelPointAuto = {3,4,6,7,6,4};
    private int[] levelPointTele = {2,3,4,5,6,4};
    private int sumCoralPoints = 0;
    private boolean tele = false;
    private int timeLeft = 0;

    private int timerStart;
    private int timerEnd = 150000;

    private final float fieldWidth = 317;
    private final float fieldDepth = 690f;
    private final float wallThickness = 2;
    private final float wallHeight = 50;
    private float reefAngle;

    private Random random = new Random();


    private GameInstance() {
        gameObjects = new ArrayList<>();
        corals = new ArrayList<>();
        Box floor = new Box(new PVector(0.f,-1.1f,fieldDepth/2.f), 10, 317, 2, 690.875, 200);
        floor.notCollidable();
        floor.setStatic(true);
        
        gameObjects.add(floor);
        robot = new Robot(true, 1234);
        gameObjects.add(robot.withElasticity(0.4));
        var coral = new Coral(new PVector());
        gameObjects.add(coral);
        corals.add(coral);
        coral.setCollidable(false);
        coral.setStatic(true);
        robot.setCoral(coral);
        

        blueReef = new Reef(false);
        gameObjects.add(blueReef.withElasticity(0.1));

        // gameObjects.add(new Box(new PVector(0.f,-1.f,345.4375f + 20), 20, 4.5, 12, 4.5, 0));

        gameObjects.add(
            new Box(new PVector(0,0.f,fieldDepth), 0, fieldWidth, 40, 10,0x55FFFFFF).isStatic().withElasticity(0.8).withVisibility(false));
        gameObjects.add(new Box(new PVector(0,0.f, 0), 0, fieldWidth, 40, 20,0x55FFFFFF).isStatic().withElasticity(0.8).withVisibility(false)); 
        gameObjects.add(new Box(new PVector(-fieldWidth/2,0.f,(fieldDepth/2)), 0, 10, 40, fieldDepth, 0x55FFFFFF).isStatic().withElasticity(0.8).withVisibility(false));
        gameObjects.add(new Box(new PVector(fieldWidth/2,0.f, (fieldDepth/2)), 0, 10, 40, fieldDepth, 0x55FFFFFF).isStatic().withElasticity(0.8).withVisibility(false));

        // sample rotation constructor: 
        for (int i = 0; i > 6; i++){
        reefAngle = (float) (i * Math.PI/3);
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

    public void startElevator() {
        robot.startElevator();
    }
    public void returnElevator() {
        robot.returnElevator();
    }

    public void ejectCoral() {
        robot.ejectCoral();
    }
    
    public void rotateRobot(float angle) {
        robot.setAngularVelocity(angle);
    }

    public void reset() {
        instance = new GameInstance();
    }

    public Robot getRobot() {
        return robot;
    }
    /**
     *  sets graphics object for the game instance and initializes objects that require it
     *  @param graphics the graphics object to set
     */
    public void setGraphics(PGraphics graphics) {
        this.graphics = graphics;


        Logger.init(graphics);

        // String dataDir = System.getenv("DATA_PATH");

        String dataDir = null;

        if (dataDir == null) {
            dataDir = "/Users/braydenleung/Desktop/comp sci stuff/create_project/app/src/main/java/create_project/data/";
            // dataDir = "C:\\Users\\zhish\\Documents\\APCompSci_create_project\\app\\src\\main\\java\\create_project\\data\\";
            // dataDir = "C:\\Users\\leung\\Desktop\\APCompSci_create_project\\app\\src\\main\\java\\create_project\\data\\";
            System.out.println("DATA_PATH not set, ");
            System.out.println("    macos/linux: export DATA_PATH=/path/to/data");
            System.out.println("    windows: set DATA_PATH=C:\\path\\to\\data");
        }

        try {
            reef = graphics.loadShape(dataDir+"REEF.obj");
            // barge = graphics.loadShape(dataDir+"BARGE.obj");
            //troubleshoot
            System.out.println("objects loaded");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // reef.scale((float)(39.37 / 1.196));
        reef.scale((float)(39.37));
        // barge.scale((float)(39.37));

        blueReef.loadShape(reef);
    }

    public void draw() {
        graphics.beginDraw();

        for (GameObject gameObject : gameObjects) {
            for (GameObject otherGameObject : gameObjects) {
                if (gameObject != otherGameObject) {
                    gameObject.checkCollision(otherGameObject);
                }
            }
        }
        graphics.background(50);
        if (Constants.FOLLOW_ROBOT) {
            var cameraPos = Vector2D.add(new Vector2D(0,100).rotate(robot.getRotation()),robot.getPosition());
        graphics.camera((float) cameraPos.getX(),72.0f,(float) cameraPos.getY(), (float) robot.getPosition().getX(), 40.f, (float) robot.getPosition().getY(),
          0.0f, -1.0f, 0.0f);
        }
        else {
            graphics.camera(0.f,72.0f,-64.f, (float) robot.getPosition().getX(), 40.f, (float) robot.getPosition().getY(),
          0.0f, -1.0f, 0.0f);
        }

        // graphics.camera(0.0f,200.0f,-64.0f, (float) robot.getPosition().getX(), 40.f, (float) robot.getPosition().getY(),
        //   0.0f, -1.0f, 0.0f);
        // graphics.camera((float) robot.getPosition().getX()+5, 1000.f, (float) robot.getPosition().getY(), (float) robot.getPosition().getX()+5, 40.f, (float) robot.getPosition().getY(),
        //   0.0f, -1.0f, 0.0f);
        graphics.perspective();
        graphics.noStroke();


        for (GameObject gameObject : gameObjects) {
            // if (gameObject == robot.getCoral()) {
            //     continue;
            // }
            gameObject.update();
            if (!gameObject.isVisible()) {
                continue;
            }
            gameObject.draw(graphics);
        }
        robot.checkIntake(corals);
        blueReef.checkIntakeZones(corals);
        drawStaticElements();
        graphics.endDraw();
    }

    private void drawStaticElements() {
        drawBox(0f, 0f, 297.75f + 1, fieldWidth, 1f, 2f, 0xFF000000);
        drawBox(0f, 0f, fieldDepth - (297.75f + 1), fieldWidth, 1f, 2f, 0xFF000000);
        drawBox(12f + ((fieldWidth - 24) / 4f), 0f, 322.75f + 1, (fieldWidth-24)/2, 1f, 2f, 0xFF0000FF);
        drawBox(12f + ((fieldWidth - 24) / 4f), 0f, fieldDepth - (322.75f + 1), (fieldWidth-24)/2, 1f, 2f, 0xFF0000FF);
    }

    private void drawBox(float x, float y, float z, float width, float height, float depth, int color) {
        graphics.pushMatrix();
        graphics.pushStyle();
        graphics.translate(x, y, z);
        graphics.fill(color);
        graphics.box(width, height, depth);
        graphics.popStyle();
        graphics.popMatrix();
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }
    //the cases are 1, 2, 3, 4, with input as integer. 
    public void coralScored(int input){
        int coralPoints = 0;
        if (timeLeft <= 135000){
                coralPoints = levelPointTele[input - 1];
        } else {
            coralPoints = levelPointAuto[input - 1];
        }
        theFunctionThatCountsForYou(coralPoints);
    }

    private int theFunctionThatCountsForYou(int coralPoints){
        sumCoralPoints += coralPoints;
        return sumCoralPoints;
    }

//starts in drawHud
    public void drawHUD(PApplet app) {

        app.rectMode(PConstants.CORNER);

        app.fill(0,0,255);
        app.rect(100,0,890,100);

        app.fill(255,0,0);
        app.rect(100, 0, 450, 100);

        app.fill(255);
        app.rect(480,0, 140, 100);

        app.fill(255); 
        app.stroke(0); 

        app.textMode(PConstants.CENTER);

        app.textSize(50);
        app.text("Alliance 1", 240, 45); 

        app.textSize(50);
        app.text("Alliance 2", 840, 45); 

        int score = theFunctionThatCountsForYou(0);
        int digitCount = (int) Math.log10(score + 1) + 1;
        float widthChange = (digitCount - 1) * 15;
       
        app.textSize(50);
        app.text(theFunctionThatCountsForYou(0),1080/2+30 - (130 + widthChange), 45); 
        
        app.fill(0);
        app.textSize(25);
        app.text(timerCount(), (1080/2)+10, 55);
        //test using keys pressed
        
    }

    public String timerCount(){
        int currentTime = Main.getGameTime();
        int elapsedTime = currentTime - timerStart;
        timeLeft = timerEnd - elapsedTime;
        int seconds = (timeLeft/1000) % 60;
        int minutes = (timeLeft/(1000*60))%60;
        return String.format("%02d:%02d", minutes, seconds);
        }

    public void createCoral() {
        Coral coral = new Coral(new PVector(-fieldWidth/2.f + 20.f, 50.f, 20.f));
        coral.setVelocity(new Vector2D(random.nextDouble(50),0).rotate(Math.PI/4. +random.nextDouble(Math.PI/6.)));
        gameObjects.add(coral);
        corals.add(coral);

        coral = new Coral(new PVector(fieldWidth/2.f - 20.f, 50.f, 20.f));
        coral.setVelocity(new Vector2D(random.nextDouble(50),0).rotate(Math.PI - (Math.PI/4. + random.nextDouble(Math.PI/6.))));
        gameObjects.add(coral);
        corals.add(coral);
    }
}
