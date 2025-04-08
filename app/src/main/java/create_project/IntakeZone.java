package create_project;

import java.util.function.Supplier;

import processing.core.PVector;

public class IntakeZone {
    private Supplier<PVector> position;
    private double width, height, depth;
    private double rotation;
    
    private boolean active = true;

    public IntakeZone(Supplier<PVector> position, double width, double height, double depth) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    public void update() {

    }




}
