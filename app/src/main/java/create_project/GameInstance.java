package create_project;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class GameInstance {
    private static GameInstance instance = null;

    private PGraphics graphics;

    private GameInstance() {
    }

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }

    public void setGraphics(PGraphics graphics) {
        this.graphics = graphics;
    }

    private void draw() {
        
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }
}
