package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;

public class Elevator extends MotionProfile {
    private List<Stage> stages = new ArrayList<>();
    private double totalTravelDistance = 0;
    public Elevator(double maxVel, double maxAccel, Stage... stage) {
        super(0, 100, maxVel, maxAccel);
        for (int i = 0; i < stage.length; i++) {
            if (i == stage.length - 1) {
                stage[i].setCarriage(true);
            }
            stages.add(stage[i]);
        }
        System.out.println(getTotalTravelDistance(this.stages));
        this.setMaxPos(getTotalTravelDistance(this.stages));
    }

    public void draw(PGraphics pg) {
        this.stages.forEach(s -> {
            pg.pushMatrix();
            pg.translate(0.f, (float) getPosition(s), 0.f);
            drawBoxFrame(pg, s.getWidth(), s.getHeight(), s.isCarriage());
            pg.popMatrix();
        });
    }

    public double getTotalTravelDistance(List<Stage> stages) {
        if (totalTravelDistance == 0) {
            double travelHeight = 0;
            for (int i = 1; i < stages.size(); i++) {
                travelHeight += stages.get(i).height - 2;
            }
            this.totalTravelDistance = travelHeight;
        }
        return totalTravelDistance;
    }

    public double getTotalTravelDistance() {
        return totalTravelDistance;
    }

    public double getPosition(Stage stage) {
        if (stage == null) {
            return 0;
        }
        int index = this.stages.indexOf(stage);
        if (index == -1) {
            return 0;
        }
        double travelHeight = 0;
        for (int i = 0; i < index+1; i++) {
            if (i == index) {
                travelHeight += stage.height / 2.;
            } else {
                travelHeight += (stages.get(i).height - 2) * (getPosition()/ getTotalTravelDistance());
            }
        }
        return travelHeight;
    }

    public Stage getLastStage() {
        // return stages.getLast();
        return stages.get(stages.size() - 1);
    }

    public static class Stage {
        private double height, width;
        private boolean isCarriage = false;
        

        public Stage(double height, double width) {
            this.height = height;
            this.width = width;
        }

        public void setCarriage(boolean isCarriage) {
            this.isCarriage = isCarriage;
        }

        public boolean isCarriage() {
            return isCarriage;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }
    }

    public void drawBoxFrame(PGraphics pg, double width, double height, boolean elevatorCarriage) {
        pg.pushMatrix();
        pg.translate((float)((width-1)/2.), 0, 0);
        pg.box(1, (float) height, 2);
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate((float)(-(width-1)/2.), 0, 0);
        pg.box(1, (float) height, 2);
        pg.popMatrix();
        if (elevatorCarriage) {
            pg.pushMatrix();
            pg.translate(0, (float) (height/2.), 0);
            pg.box((float) width, 1, 2);
            pg.popMatrix();
            pg.pushMatrix();
            pg.translate(0, (float) (-(height/2.)), 0);
            pg.box((float) width, 1, 2);
            pg.popMatrix();
            return;
        }
        pg.pushMatrix();
        pg.translate(0, (float) (height/2.), 2);
        pg.box((float) width, 1, 1);
        pg.popMatrix();
        pg.pushMatrix();
        pg.translate(0, (float) (-(height/2.)), 0);
        pg.box((float) width, 1, 1);
        pg.popMatrix();
        return;
    }
}
