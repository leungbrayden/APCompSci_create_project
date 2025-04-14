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
    private List<IntakeZone> intakeZones = new ArrayList<IntakeZone>();
    private final float L4OFFSET = ((65.5f/2.f) - 1.125f);
    private final PVector L4POSITION = new PVector((float) this.getPosition().getX(), 72.f + 2.f, (float) this.getPosition().getY());

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
            intakeZones.add(new IntakeZone(PVector.add(L4POSITION, (i % 2 == 0 ? positiveOffset : negativeOffset).toPVector()), 10, 10, 10));
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
        intakeZones.forEach(zone -> {
            Coral coral = zone.checkCollision(corals);
            if (coral != null) {
                zone.setActive(false);
                GameInstance.getInstance().coralScored(4);
                coral.setVisible(false);
                coral.setCollidable(false);
                corals.remove(coral);
            }
        }
        );
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

        intakeZones.forEach(zone -> {
            zone.draw(pg);
            // if (zone.isActive()) {
                
            // }
        });
    }
}
 