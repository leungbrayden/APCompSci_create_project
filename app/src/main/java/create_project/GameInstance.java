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
        float far = graphics.map(graphics.mouseX, 0, graphics.width, 120, 400);
        if (showPerspective == true) {
            graphics.perspective(PApplet.PI / 3.0f, (float) graphics.width / (float) graphics.height, 10, far);
        } else {
            graphics.ortho(-graphics.width / 2.0f, graphics.width / 2.0f, -graphics.height / 2.0f, graphics.height / 2.0f, 10, far);
        }
        graphics.translate(graphics.width / 2, graphics.height / 2, 0);
        graphics.rotateX(-PApplet.PI / 6);
        graphics.rotateY(PApplet.PI / 3);
        graphics.box(180);
        graphics.endDraw();
    }

    public PGraphics getGraphics() {
        draw();
        return graphics;
    }
}
