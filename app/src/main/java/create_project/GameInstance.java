package create_project;

import processing.core.PApplet;
import processing.core.PGraphics;

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
        graphics.beginDraw();
        graphics.lights();
        graphics.background(0);
        graphics.endDraw();
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }
}
