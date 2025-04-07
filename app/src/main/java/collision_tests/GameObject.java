package collision_tests;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;

public class GameObject {
    private PVector position;
    private PVector velocity;
    private PVector acceleration;
    private float rotation;
    private float angularVelocity;
    private double weight;
    private boolean collidable;
    private List<Vertex> vertices;
    private double width;
    private double height;
    private double inertia;
    private double friction = 0.5f;

    public float restitution = 0.4f;

    public GameObject() {
        this.position = new PVector();
        this.velocity = new PVector();
        this.acceleration = new PVector();
        this.weight = 0;
        this.collidable = true;
        this.vertices = new ArrayList<Vertex>();
        this.angularVelocity = 0;
        this.rotation = 0;
    }

    public GameObject(PVector position, double weight, double width, double height) {
        this.width = width;
        this.height = height;
        this.position = position;
        this.velocity = new PVector();
        this.acceleration = new PVector();
        this.weight = weight;
        this.collidable = true;
        this.vertices = new ArrayList<Vertex>();
        setVertices(
            new Vertex(width * 0.5, height * 0.5),
            new Vertex(-width * 0.5, height * 0.5),
            new Vertex(-width * 0.5, -height * 0.5),
            new Vertex(width * 0.5, -height * 0.5)
        );
        this.angularVelocity = 0;
        this.rotation = 0;
        this.inertia = (weight * (width * width + height * height)) / 12.f;
    }

    public GameObject setVertices(Vertex... vertices) {
        for (Vertex vertex : vertices) {
            this.vertices.add(vertex);
        }
        return this;
    }

    public void update() {
        Logger.displayVector(this.velocity, this.position, 0x0000FF);
        // Update the position based on velocity and acceleration
        this.velocity.add(this.acceleration);
        this.rotation += this.angularVelocity;
        this.angularVelocity *= 0.99f; // apply angular friction
        // // apply friction
        this.velocity.mult(0.99f);

        this.position.add(this.velocity);
    }

    public void checkCollision(GameObject other) {
        // separating axis theorem
        if (!this.collidable || !other.collidable) {
            return; // No collision check if either object is not collidable
        }
        List<Vertex> vertices1 = this.getVertices();
        List<Vertex> vertices2 = other.getVertices();

        // vertices1.forEach(v -> v.rotate(this.rotation));
        // vertices2.forEach(v -> v.rotate(other.rotation));

        PVector position1 = this.getPosition();
        PVector position2 = other.getPosition();
        PVector collisionNormal = null; // To store the collision normal
        float minOverlap = Float.MAX_VALUE; // To store the minimum overlap
        // Iterate through each edge of both objects
        List<PVector> axes = new ArrayList<>();

        // Get axes from this object's edges
        for (int i = 0; i < vertices1.size(); i++) {
            Vertex v1 = vertices1.get(i);
            Vertex v2 = vertices1.get((i + 1) % vertices1.size());
            PVector edge = new PVector((float) (v2.getX() - v1.getX()), (float) (v2.getY() - v1.getY()));
            PVector axis = new PVector(-edge.y, edge.x).normalize(null);
            axes.add(axis);
            Logger.displayVector(PVector.mult(axis, 50f), PVector.add(position1, v1.toPVector()), 0xFFFF0000);
        }

        // Get axes from the other object's edges
        for (int i = 0; i < vertices2.size(); i++) {
            Vertex v1 = vertices2.get(i);
            Vertex v2 = vertices2.get((i + 1) % vertices2.size());
            PVector edge = new PVector((float) (v2.getX() - v1.getX()), (float) (v2.getY() - v1.getY()));
            PVector axis = new PVector(-edge.y, edge.x).normalize(null);
            axes.add(axis);
            Logger.displayVector(PVector.mult(axis, 5f), PVector.add(position2, v1.toPVector()), 0xFFFF0000);
        }

        // Check for overlap on all axes
        for (PVector axis : axes) {
            float min1 = Float.MAX_VALUE, max1 = -Float.MAX_VALUE;
            float min2 = Float.MAX_VALUE, max2 = -Float.MAX_VALUE;

            // Project vertices of this object onto the axis
            for (Vertex vertex : vertices1) {
                PVector worldVertex = PVector.add(position1, vertex.toPVector());
                float projection = worldVertex.dot(axis);
                min1 = Math.min(min1, projection);
                max1 = Math.max(max1, projection);
            }

            // Project vertices of the other object onto the axis
            for (Vertex vertex : vertices2) {
            PVector worldVertex = PVector.add(position2, vertex.toPVector());
            float projection = worldVertex.dot(axis);
            min2 = Math.min(min2, projection);
            max2 = Math.max(max2, projection);
            }

            // Check for overlap
            float overlap = Math.min(max1, max2) - Math.max(min1, min2);
            if (overlap <= 0) {
            return; // No collision
            }

            // Keep track of the smallest overlap
            if (overlap < minOverlap) {
            minOverlap = overlap;
            collisionNormal = axis;
            }
        }

        // If we reach here, there is a collision
        if (collisionNormal != null) {
            // System.out.println("Collision detected!");
            // System.out.println("Collision normal: " + collisionNormal);
            // // Resolve collision (e.g., adjust positions or velocities)
            // PVector resolution = PVector.mult(collisionNormal, minOverlap);
            // this.position.add(resolution);
            handleCollision(other, collisionNormal, minOverlap, findContactPoint(vertices1, position1, vertices2, position2, collisionNormal));

        }
    }

    private PVector findContactPoint(
        List<Vertex> verts1, PVector pos1,
        List<Vertex> verts2, PVector pos2,
        PVector normal
    ) {
        float maxDepth = -Float.MAX_VALUE;
        PVector deepest = null;

        for (Vertex v : verts1) {
            PVector worldV = PVector.add(pos1, v.toPVector());
            float depth = normal.dot(PVector.sub(worldV, pos2));
            if (depth > maxDepth) {
                maxDepth = depth;
                deepest = worldV.copy();
            }
        }

        for (Vertex v : verts2) {
            PVector worldV = PVector.add(pos2, v.toPVector());
            float depth = normal.dot(PVector.sub(worldV, pos1));
            if (depth > maxDepth) {
                maxDepth = depth;
                deepest = worldV.copy();
            }
        }

        return deepest;
    }

    public double cross(PVector a, PVector b) {
        return a.x * b.y - a.y * b.x;
    }


    private void handleCollision(GameObject other, PVector collisionNormal, float minOverlap, PVector contactPoint) {
        // Relative vector from center of mass to contact
        PVector ra = PVector.sub(contactPoint,this.position);
        PVector rb = PVector.sub(contactPoint,other.position);

        // Relative velocity
        PVector rv = PVector.sub(PVector.add(other.velocity,PVector.mult(new PVector(-rb.y, rb.x),
        (other.angularVelocity))),
                          (PVector.add(this.velocity,PVector.mult(new PVector(-ra.y, ra.x),(this.angularVelocity)))));

        double velAlongNormal = rv.dot(collisionNormal);
        System.out.println("Velocity along normal: " + velAlongNormal);
        if (velAlongNormal > 0) return; // Bodies are separating

        // Restitution (elasticity)
        double e = Math.min(this.restitution, other.restitution);
        
        // Calculate impulse scalar
        double raCrossN = cross(ra,collisionNormal);
        double rbCrossN = cross(rb,collisionNormal);
        double invMassSum =  (1. / this.weight) + (1. / other.weight) + 
                            (raCrossN * raCrossN) *  (1. / this.inertia)+ 
                            (rbCrossN * rbCrossN) * (1. / other.inertia);

        double j = -(1 + e) * velAlongNormal / invMassSum;

        // Impulse
        PVector impulse = PVector.mult(collisionNormal,(float)j);
        this.applyImpulse(PVector.mult(impulse,-1.f), ra);
        this.applyImpulse(impulse, rb);

        // --- Friction ---
        rv = PVector.sub(PVector.add(other.velocity,PVector.mult(new PVector(-rb.y, rb.x),
        (other.angularVelocity))),
                          (PVector.add(this.velocity,PVector.mult(new PVector(-ra.y, ra.x),(this.angularVelocity)))));

        PVector tangent = PVector.sub(rv,PVector.mult(collisionNormal,(rv.dot(collisionNormal)))).normalize(null);
        double jt = -rv.dot(tangent) / invMassSum;

        // Coulomb's law
        double mu = Math.sqrt(this.friction * other.friction);
        PVector frictionImpulse;
        if (Math.abs(jt) < j * mu) {
            frictionImpulse = PVector.mult(tangent,(float)jt);
        } else {
            frictionImpulse = PVector.mult(tangent,(float) (-j * mu));
        }

        this.applyImpulse(PVector.mult(frictionImpulse,-1.f), ra);
        other.applyImpulse(frictionImpulse, rb);

        // Positional correction to avoid sinking
        final double percent = 0.2; // 20% of penetration
        final double slop = 0.01;
        System.out.println("Min overlap: " + minOverlap);
        PVector correction = PVector.mult(collisionNormal,(float) (Math.max(minOverlap - slop, 0.0) / ((1./this.weight) + (1./other.weight)) * percent));
        System.out.println("Correction: " + correction);
        this.setPosition(PVector.sub(this.position,PVector.mult(correction,(float)(1./this.weight))));
        other.setPosition(PVector.add(other.position,PVector.mult(correction,(float)(1./other.weight))));

        Logger.displayPoint(contactPoint, 0xCCFF0000);
        Logger.displayVector(PVector.mult(collisionNormal,100.f), contactPoint, 0xCC00FF00);
        // this.collidable = false;
        // other.collidable = false;
        System.out.println("Collision resolved!");
    }

    public void draw(PApplet pg) {
        pg.fill(127);
        pg.stroke(0);
        pg.pushMatrix();
        pg.translate(position.x, position.y);
        pg.rotate(this.rotation);        
        pg.rect(0,0,(float) width, (float) height);
        pg.popMatrix();
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public PVector getPosition() {
        return position;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    public PVector getAcceleration() {
        return acceleration;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
    public float getRotation() {
        return rotation;
    }

    public void addRotation(float rotation) {
        this.rotation += rotation;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public List<Vertex> getVertices() {
        List<Vertex> transformedVertices = new ArrayList<>();
        for (Vertex vertex : vertices) {
            Vertex transformedVertex = new Vertex(vertex.x, vertex.y);
            transformedVertices.add(transformedVertex.rotate(this.rotation));
        }

        return transformedVertices;
    }

    public class Vertex {
        double x;
        double y;

        public Vertex(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public void setX(double x) {
            this.x = x;
        }

        public void setY(double y) {
            this.y = y;
        }

        public PVector toPVector() {
            return new PVector((float) x, (float) y);
        }

        public Vertex rotate(float angle) {
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double newX = x * cos - y * sin;
            double newY = x * sin + y * cos;
            return new Vertex(newX, newY);
        }
    }

    public void applyImpulse(PVector impulse, PVector contactVector) {
        velocity.add(PVector.mult(impulse,(1.f / (float) weight)));
        this.angularVelocity += (float) (1.f/this.inertia) * cross(contactVector,impulse);
    }
    
}
