package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class GameInstance{
    private static GameInstance instance = null;
    private List<GameObject> gameObjects;

    private List<Coral> corals;

    private Robot robot;
    private Reef blueReef;

    private PGraphics graphics;

    private PShape reef;

    private int[] levelPointAuto = {3,4,6,7,6,4};
    private int[] levelPointTele = {2,3,4,5,6,4};
    private int sumCoralPoints = 0;

    private final float fieldWidth = 317;
    private final float fieldDepth = 690f;
    private final float wallThickness = 2;
    private final float wallHeight = 50;
    private float reefAngle;


    private GameInstance() {
        gameObjects = new ArrayList<>();
        corals = new ArrayList<>();
        Box floor = new Box(new PVector(0.f,-1.1f,fieldDepth/2.f), 10, 317, 2, 690.875, 200);
        floor.notCollidable();
        floor.setStatic(true);
        
        gameObjects.add(floor);
        robot = new Robot(true, 1234);
        gameObjects.add(robot.withElasticity(0.4));
        var coral = new Coral(new PVector(0.f, 2.25f, 100.f + 20));
        gameObjects.add(coral.withElasticity(0.1));
        corals.add(coral);        
        

        blueReef = new Reef(false);
        gameObjects.add(blueReef.withElasticity(0.1));

        // gameObjects.add(new Box(new PVector(0.f,-1.f,345.4375f + 20), 20, 4.5, 12, 4.5, 0));

        gameObjects.add(new Box(new PVector(0,0.f,fieldDepth), 0, fieldWidth, 40, 10,0x55FFFFFF).isStatic().withElasticity(0.8)); 
        gameObjects.add(new Box(new PVector(0,0.f, 0), 0, fieldWidth, 40, 20,0x55FFFFFF).isStatic().withElasticity(0.8)); 
        gameObjects.add(new Box(new PVector(-fieldWidth/2,0.f,(fieldDepth/2)), 0, 10, 40, fieldDepth, 0x55FFFFFF).isStatic().withElasticity(0.8));
        gameObjects.add(new Box(new PVector(fieldWidth/2,0.f, (fieldDepth/2)), 0, 10, 40, fieldDepth, 0x55FFFFFF).isStatic());

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
    
    public void rotateRobot(float angle) {
        robot.setAngularVelocity(angle);
    }

    public void reset() {
        instance = new GameInstance();
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
            dataDir = "C:\\Users\\zhish\\Documents\\APCompSci_create_project\\app\\src\\main\\java\\create_project\\data\\";
            System.out.println("DATA_PATH not set, ");
            System.out.println("    macos/linux: export DATA_PATH=/path/to/data");
            System.out.println("    windows: set DATA_PATH=C:\\path\\to\\data");
        }

        try {
            reef = graphics.loadShape(dataDir+"REEF.obj");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // reef.scale((float)(39.37 / 1.196));
        reef.scale((float)(39.37));

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
        graphics.camera(0.0f,72.0f,-64.0f, (float) robot.getPosition().getX(), 0.f, (float) robot.getPosition().getY(),
          0.0f, -1.0f, 0.0f);
        graphics.perspective();
        graphics.noStroke();


        for (GameObject gameObject : gameObjects) {
            if (gameObject == robot.getCoral()) {
                continue;
            }
            gameObject.update();
            if (!gameObject.isVisible()) {
                continue;
            }
            gameObject.draw(graphics);
        }
        robot.checkIntake(corals);
        // if (reef != null) {
        //     graphics.pushMatrix();
        //     graphics.translate(0, 0, 100);
        //     graphics.shape(reef);
        //     graphics.popMatrix();
        // }
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
        switch (input) {
            case 1:
                coralPoints = levelPointTele[0];
                break;
            case 2:
                coralPoints = levelPointTele[1];
                break;
            case 3:
                coralPoints = levelPointTele[2];
                break;
            case 4:
                coralPoints = levelPointTele[3];
                break;
        }
        theFunctionThatCountsForYou(coralPoints);
    }

    private int theFunctionThatCountsForYou(int coralPoints){
        sumCoralPoints += coralPoints;
        return sumCoralPoints;
    }

//starts in drawHud
    public void drawHUD(PApplet app) {
        app.fill(0,0,255);
        app.rect(100,0,900,100);

        app.fill(255,0,0);
        app.rect(100, 0, 450, 100);

        app.fill(255); 
        app.stroke(255); 
        app.text("testtext", 1080/2, 720/2); 
        app.text(theFunctionThatCountsForYou(0),1080/3, 30); 

        //test using keys pressed
        
    }
    
    public void timerCount(String[] args){
        long timerStart = System.currentTimeMillis();

        for(int i = 1; i <= 10; i++){
            long currentTime = System.currentTimeMillis();
            


            
        }
            // app.text(elapsedTime, 500, 60);
    }


    public void createCoral() {
        Coral coral = new Coral(new PVector(0.f, 70.f, 50.f));
        gameObjects.add(coral);
        corals.add(coral);
    }
}
