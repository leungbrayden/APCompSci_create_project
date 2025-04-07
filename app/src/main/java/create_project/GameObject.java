package create_project;

import processing.core.PVector;

import java.util.List;

import processing.core.PGraphics;

public abstract class GameObject {
    public PVector position = new PVector();
    public PVector velocity = new PVector();
    public PVector acceleration = new PVector();
    public double weight;
    private boolean isCollidable = true;

    private List<Vertex> vertices = new java.util.ArrayList<>();
    /**
     * creates collision vertices, which are used to determine the collision
     * between two objects
     * @param vertex assuming the vertices are in a clockwise order and convex
     */
    public void setVertices(Vertex... vertex) {
        for (Vertex v : vertex) {
            vertices.add(v);
        }
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setCollidable(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    public void checkCollision(GameObject object) {
        if (!this.isCollidable || !object.isCollidable) {
            return; // No collision check if either object is not collidable
        }
        // Separating Axis Theorem
        List<Vertex> vertices1 = this.getVertices();
        List<Vertex> vertices2 = object.getVertices();
    
        PVector position1 = this.getPosition();
        PVector position2 = object.getPosition();
    
        PVector collisionNormal = null; // To store the collision normal
        float minOverlap = Float.MAX_VALUE; // To store the minimum overlap
    
        // Iterate through each edge of both objects
        for (int i = 0; i < vertices1.size() + vertices2.size(); i++) {
            // Get the edge from the appropriate object
            Vertex vertex1, vertex2;
            if (i < vertices1.size()) {
                vertex1 = vertices1.get(i);
                vertex2 = vertices1.get((i + 1) % vertices1.size());
            } else {
                int j = i - vertices1.size();
                vertex1 = vertices2.get(j);
                vertex2 = vertices2.get((j + 1) % vertices2.size());
            }
    
            // Calculate the edge vector and normal
            PVector edge = new PVector(
                (float) (vertex2.getX() - vertex1.getX()),
                (float) (vertex2.getY() - vertex1.getY())
            );
            PVector normal = new PVector(-edge.y, edge.x).normalize();
            // normal.normalize();
    
            // Project both objects onto the normal
            float min1 = Float.MAX_VALUE, max1 = Float.MIN_VALUE;
            float min2 = Float.MAX_VALUE, max2 = Float.MIN_VALUE;
    
            for (Vertex v : vertices1) {
                float projection = PVector.dot(normal, new PVector(
                    (float) (v.getX() + position1.x),
                    (float) (v.getY() + position1.y)
                ));
                min1 = Math.min(min1, projection);
                max1 = Math.max(max1, projection);
            }
    
            for (Vertex v : vertices2) {
                float projection = PVector.dot(normal, new PVector(
                    (float) (v.getX() + position2.x),
                    (float) (v.getY() + position2.y)
                ));
                min2 = Math.min(min2, projection);
                max2 = Math.max(max2, projection);
            }
    
            // Check for overlap
            if (max1 < min2 || max2 < min1) {
                // No collision
                return;
            }
    
            // Calculate the overlap
            float overlap = Math.min(max1, max2) - Math.max(min1, min2);
            if (overlap < minOverlap) {
                minOverlap = overlap;
                collisionNormal = normal.copy();
            }
        }
    
        // If we reach here, there is a collision
        // System.out.println("Collision detected!");
        if (this.getClass() == Robot.class) {
            // System.out.println("Collision normal: " + collisionNormal);
            // System.out.println("object1: " + this.getClass());
            // System.out.println("object2: " + object.getClass());
            // System.out.println("object1 position: " + this.getPosition());
            // System.out.println("object2 position: " + object.getPosition());
        }
        // Resolve collision
        resolveCollision(object, new PVector(collisionNormal.x, 0, collisionNormal.y), minOverlap);
    }
    
    private void resolveCollision(GameObject object, PVector collisionNormal, float overlap) {
        // Separate the objects to resolve overlap
        float totalWeight = (float) (this.getWeight() + object.getWeight());
        PVector correction = collisionNormal.copy().mult(overlap / totalWeight);
    
        this.position.add(correction.copy().mult((float) object.getWeight()));
        object.position.sub(correction.copy().mult((float) this.getWeight()));
    
        // Calculate relative velocity
        PVector relativeVelocity = object.getVelocity().copy().sub(this.getVelocity());
        float velocityAlongNormal = PVector.dot(relativeVelocity, collisionNormal);
    
        // Do not resolve if objects are separating
        if (velocityAlongNormal > 0) return;
    
        // Calculate restitution (bounciness) and friction
        float restitution = 0.5f; // Adjust as needed (0 = inelastic, 1 = elastic)
        float friction = 0.2f; // Adjust as needed
    
        // Impulse scalar
        float impulseScalar = -(1 + restitution) * velocityAlongNormal;
        impulseScalar /= (1 / this.getWeight() + 1 / object.getWeight());
    
        // Apply impulse
        PVector impulse = collisionNormal.copy().mult(impulseScalar);
        this.velocity.sub(impulse.copy().mult(1 / (float) this.getWeight()));
        object.velocity.add(impulse.copy().mult(1 / (float) object.getWeight()));
    
        // Apply friction
        PVector tangent = relativeVelocity.copy().sub(collisionNormal.copy().mult(velocityAlongNormal));
        tangent.normalize();
    
        float frictionImpulseScalar = -PVector.dot(relativeVelocity, tangent);
        frictionImpulseScalar /= (1 / this.getWeight() + 1 / object.getWeight());
    
        // Clamp friction to Coulomb's law
        float frictionImpulseMax = impulseScalar * friction;
        frictionImpulseScalar = Math.max(-frictionImpulseMax, Math.min(frictionImpulseScalar, frictionImpulseMax));
    
        PVector frictionImpulse = tangent.copy().mult(frictionImpulseScalar);
        this.velocity = this.velocity.sub(frictionImpulse.copy().mult(1 / (float) this.getWeight()));
        object.velocity = object.velocity.add(frictionImpulse.copy().mult(1 / (float) object.getWeight()));
    }

    public void update() {
        this.velocity.add(acceleration);
        this.position.add(this.velocity);
    }

    public abstract void draw(PGraphics pg);

    public PVector getPosition() {
        return position;
    }

    public void setPosition(PVector position) {
        this.position = position;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(PVector velocity) {
        this.velocity = velocity;
    }

    public PVector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(PVector acceleration) {
        this.acceleration = acceleration;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
    }
}