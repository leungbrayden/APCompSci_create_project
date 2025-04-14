package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PGraphics;
import processing.core.PShape;
import processing.core.PVector;

public class Reef extends GameObject{
    PShape reefMesh;

    private final static float depth = 65.5f;
    private final static float half_width = 18.52f;
    private List<List<IntakeZone>> intakeZones = new ArrayList<>();
    {
        for (int i = 0; i < 4; i++) {
            intakeZones.add(new ArrayList<>());
        }
    }
    private final float L4OFFSET = ((65.5f/2.f) - 1.125f);
    private final float L32OFFSET = ((65.5f/2.f) - 1.625f);
    private final PVector L4POSITION = new PVector((float) this.getPosition().getX(), 72.f + 2.f, (float) this.getPosition().getY());
    private final PVector L3POSITION = new PVector((float) this.getPosition().getX(), 47.625f + 2.f, (float) this.getPosition().getY());
    private final PVector L2POSITION = new PVector((float) this.getPosition().getX(), 31.625f + 2.f, (float) this.getPosition().getY());

    public Reef(boolean isRedAlliance) {
        super(new Vector2D[] {}, new Vector2D(0, 144 + depth/2.), 0);
        this.setStatic(true);
        Vector2D[] points = new Vector2D[6];
        // set vertices of collision polygon
        for (int i = 0; i < 6; i++) {
            Vector2D point = new Vector2D(half_width,depth/2.);
            points[i] = point.rotate((float) (i * Math.PI / 3.));
        }

        this.setVertices(points);
        Vector2D positiveOffset = new Vector2D(6.5f, L4OFFSET);
        Vector2D negativeOffset = new Vector2D(-6.5f, L4OFFSET);
        for (int i = 0; i < 12; i++) {
            intakeZones.get(3).add(new IntakeZone(PVector.add(L4POSITION, (i % 2 == 0 ? positiveOffset : negativeOffset).toPVector()), 10, 10, 10));
            if (i % 2 == 1) {
                positiveOffset = positiveOffset.rotate((float) (Math.PI / 3.));
                negativeOffset = negativeOffset.rotate((float) (Math.PI / 3.));
            }
        }

        positiveOffset = new Vector2D(6.5f, L32OFFSET);
        negativeOffset = new Vector2D(-6.5f, L32OFFSET);
        for (int i = 0; i < 12; i++) {
            intakeZones.get(2).add(new IntakeZone(PVector.add(L3POSITION, (i % 2 == 0 ? positiveOffset : negativeOffset).toPVector()), 10, 10, 10));
            intakeZones.get(2).getLast().setRotation(0, -(Math.PI/2.f)-((Math.PI/3.f)*(i/2)), 0);
            if (i % 2 == 1) {
                positiveOffset = positiveOffset.rotate((float) (Math.PI / 3.));
                negativeOffset = negativeOffset.rotate((float) (Math.PI / 3.));
            }
        }

        positiveOffset = new Vector2D(6.5f, L32OFFSET);
        negativeOffset = new Vector2D(-6.5f, L32OFFSET);
        for (int i = 0; i < 12; i++) {
            intakeZones.get(1).add(new IntakeZone(PVector.add(L2POSITION, (i % 2 == 0 ? positiveOffset : negativeOffset).toPVector()), 10, 10, 10));
            intakeZones.get(1).getLast().setRotation(0, -(Math.PI/2.f)-((Math.PI/3.f)*(i/2)), 0);
            if (i % 2 == 1) {
                positiveOffset = positiveOffset.rotate((float) (Math.PI / 3.));
                negativeOffset = negativeOffset.rotate((float) (Math.PI / 3.));
            }
        }
        
        // set position based on alliance
        // setPosition(new Vector2D());
    }

    public void loadShape(PShape shape) {
        this.reefMesh = shape;
    }

    public void checkIntakeZones(List<Coral> corals) {
        intakeZones.forEach(intakeZone->{intakeZone.forEach(zone -> {
            Coral coral = zone.checkCollision(corals);
            if (coral != null) {
                zone.setActive(false);
                GameInstance.getInstance().coralScored(intakeZones.indexOf(intakeZone)+1);
                coral.setVisible(false);
                coral.setCollidable(false);
                corals.remove(coral);
            }
        }
        );});
    }

    @Override
    public void draw(PGraphics pg) {
        if (!this.isVisible()) {
            return;
        }

        // draw stuff
        pg.pushMatrix();
        pg.translate((float) getPosition().getX(), 0.f, (float) getPosition().getY());
        pg.rotateY((float) Math.PI / 6.f);
        pg.shape(reefMesh);
        pg.popMatrix();

        intakeZones.forEach(intakeZone->{intakeZone.forEach(zone -> {
            if (zone.getPosition().y > 60) {
                zone.draw(pg, Math.PI/2);
            } else {
                zone.draw(pg, (Math.PI*35.f/180.f));
            }
            // if (zone.isActive()) {
                
            // }
        });});
    }
}
 