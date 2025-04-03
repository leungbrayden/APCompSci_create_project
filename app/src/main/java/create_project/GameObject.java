package create_project;

import processing.core.PVector;

import java.util.List;

import processing.core.PGraphics;

public abstract class GameObject {
    public PVector position;
    public PVector velocity;
    public PVector acceleration;
    public double weight;

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

    public void checkCollision(GameObject object) {
        // Separating Axis Theorem
        // Check for collision between this object and another object
    
        // Get the vertices of both objects
        List<Vertex> vertices1 = this.getVertices();
        List<Vertex> vertices2 = object.getVertices();
    
        // Get the positions of both objects
        PVector position1 = this.getPosition();
        PVector position2 = object.getPosition();
    
        // Iterate through each edge of the first object
        for (int i = 0; i < vertices1.size(); i++) {
            Vertex vertex1 = vertices1.get(i);
            Vertex vertex2 = vertices1.get((i + 1) % vertices1.size());
    
            // Calculate the edge vector
            PVector edge = new PVector(
                (float) (vertex2.getX() - vertex1.getX()),
                (float) (vertex2.getY() - vertex1.getY())
            );
    
            // Calculate the normal vector
            PVector normal = new PVector(-edge.y, edge.x);
            normal.normalize();
    
            // Project the vertices of both objects onto the normal vector
            float min1 = Float.MAX_VALUE;
            float max1 = Float.MIN_VALUE;
            float min2 = Float.MAX_VALUE;
            float max2 = Float.MIN_VALUE;
    
            for (Vertex v : vertices1) {
                // Adjust vertex by the object's position
                float projection = PVector.dot(normal, new PVector(
                    (float) (v.getX() + position1.x),
                    (float) (v.getY() + position1.z)
                ));
                min1 = Math.min(min1, projection);
                max1 = Math.max(max1, projection);
            }
    
            for (Vertex v : vertices2) {
                // Adjust vertex by the object's position
                float projection = PVector.dot(normal, new PVector(
                    (float) (v.getX() + position2.x),
                    (float) (v.getY() + position2.z)
                ));
                min2 = Math.min(min2, projection);
                max2 = Math.max(max2, projection);
            }
    
            // Check for overlap
            if (max1 < min2 || max2 < min1) {
                // No collision
                return;
            }
        }
    
        // If we reach this point, there is a collision
        System.out.println("Collision detected between objects!");

        // Handle collision response here (e.g., resolve positions, apply forces, etc.)
        
    }

    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
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