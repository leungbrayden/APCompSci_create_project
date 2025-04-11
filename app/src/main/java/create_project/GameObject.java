package create_project;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class GameObject {
    private Vector2D position, velocity, acceleration;
    private double rotation, angularVelocity, angularAcceleration;
    private double mass, inertia, restitution, friction, invMass, invInertia;
    private boolean isStatic, isVisible, isCollidable, collidable;
    private Vector2D[] vertices;

    public GameObject(Vector2D position, Vector2D velocity, Vector2D acceleration, double rotation,
                      double angularVelocity, double angularAcceleration, double mass, 
                      boolean isStatic, boolean isVisible, Vector2D[] vertices, double restitution) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.rotation = rotation;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
        this.mass = mass;
        this.isStatic = isStatic;
        this.isVisible = isVisible;
        this.vertices = vertices;

        this.isCollidable = true;
        this.restitution = restitution;
        this.friction = .001;
        this.collidable = true; // Default to collidable
        if (mass == 0) {
            this.invMass = 0;
            this.invInertia = 0;
            this.inertia = 0;
            return;
        }
        
        this.inertia = (mass * 
            ((vertices[0].getX() * vertices[0].getX()) + (vertices[0].getY() * vertices[0].getY()))) / 12.;
        this.invMass = 1.0 / mass;
        this.invInertia = 1.0 / inertia;
    }

    public GameObject(Vector2D[] vertices) {
        this(new Vector2D(), new Vector2D(), new Vector2D(), 0, 0, 0, 1, false, true, vertices, 0.5);
    }

    public GameObject(Vector2D[] vertices, Vector2D position, double mass) {
        this(position, new Vector2D(), new Vector2D(), 0, 0, 0, mass, false, true, vertices, 0.5);
    }

    public static GameObject createRect(Vector2D position, double width, double height) {
        Vector2D[] vertices = new Vector2D[] {
            new Vector2D(width / 2, height / 2),
            new Vector2D(-width / 2, height / 2),
            new Vector2D(-width / 2, -height / 2),
            new Vector2D(width / 2, -height / 2)
        };
        return new GameObject(vertices, position, width * height);
    }

    public void checkCollision(GameObject other) {
        // System.out.println("Checking collision between " + this + " and " + other);
        // separating axis theorem
        if (!this.isCollidable || !other.isCollidable) {
            return; // No collision check if either object is not collidable
        }

        if (this.isStatic && other.isStatic) {
            return; // No collision check if both objects are static
        }

        if (this.collidable == false || other.collidable == false) {
            return; // No collision check if either object is not collidable
        }
    

        // Get the vertices of both objects
        Vector2D[] vertices1 = this.getVertices().clone();
        Vector2D[] vertices2 = other.getVertices().clone();

        for (int i = 0; i < vertices1.length; i++) {
            vertices1[i] = vertices1[i].copy().rotate(this.getRotation());
        }
        for (int i = 0; i < vertices2.length; i++) {
            vertices2[i] = vertices2[i].copy().rotate(other.getRotation());
        }

        Vector2D position1 = this.getPosition();
        Vector2D position2 = other.getPosition();
        Vector2D collisionNormal = null; // To store the collision normal
        double minOverlap = Double.MAX_VALUE; // To store the minimum overlap
        // Iterate through each edge of both objects
        List<Vector2D> axes = new ArrayList<>();

        // Get axes from this object's edges
        for (int i = 0; i < vertices1.length; i++) {
            Vector2D v1 = vertices1[i];
            Vector2D v2 = vertices1[(i + 1) % vertices1.length];
            Vector2D edge = new Vector2D((v2.getX() - v1.getX()),(v2.getY() - v1.getY()));
            Vector2D axis = edge.perpendicular().normal();
            axes.add(axis);
            // Logger.recordVector(Vector2D.mult(axis, 50f), Vector2D.add(position1, v1), 0xFFFF0000);
        }

        // Get axes from the other object's edges
        for (int i = 0; i < vertices2.length; i++) {
            Vector2D v1 = vertices2[i];
            Vector2D v2 = vertices2[(i + 1) % vertices2.length];
            Vector2D edge = new Vector2D((v2.getX() - v1.getX()),(v2.getY() - v1.getY()));
            Vector2D axis = edge.perpendicular().normal();
            axes.add(axis);
        }

        // Check for overlap on all axes
        for (Vector2D axis : axes) {
            double min1 = Double.MAX_VALUE, max1 = -Double.MAX_VALUE;
            double min2 = Double.MAX_VALUE, max2 = -Double.MAX_VALUE;

            // Project vertices of this object onto the axis
            for (Vector2D vertex : vertices1) {
                Vector2D worldVertex = Vector2D.add(position1, vertex);
                double projection = worldVertex.dot(axis);
                min1 = Math.min(min1, projection);
                max1 = Math.max(max1, projection);
            }

            // Project vertices of the other object onto the axis
            for (Vector2D vertex : vertices2) {
            Vector2D worldVertex = Vector2D.add(position2, vertex);
            double projection = worldVertex.dot(axis);
            min2 = Math.min(min2, projection);
            max2 = Math.max(max2, projection);
            }

            // Check for overlap
            double overlap = Math.min(max1, max2) - Math.max(min1, min2);
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
            // Vector2D resolution = Vector2D.mult(collisionNormal, minOverlap);
            // this.position.add(resolution);
            handleCollision(other, collisionNormal, minOverlap, findContactPoint(vertices1, position1, vertices2, position2, collisionNormal));

        }
    }

    private Vector2D findContactPoint(
        Vector2D[] verts1, Vector2D pos1,
        Vector2D[] verts2, Vector2D pos2,
        Vector2D normal
    ) {
        double maxDepth = -Double.MAX_VALUE;
        Vector2D deepest = null;
        boolean isInverted = false;

        for (Vector2D v : verts1) {
            Vector2D worldV = Vector2D.add(pos1, v);
            double depth = normal.dot(Vector2D.sub(worldV, pos2));
            if (Math.abs(depth) > maxDepth) {
                maxDepth = Math.abs(depth);
                if (depth < 0) {
                    isInverted = true;
                } else {
                    isInverted = false;
                }
                deepest = worldV.copy();
            }
        }

        for (Vector2D v : verts2) {
            Vector2D worldV = Vector2D.add(pos2, v);
            double depth = normal.dot(Vector2D.sub(worldV, pos1));
            if (Math.abs(depth) > maxDepth) {
                maxDepth = Math.abs(depth);
                if (depth < 0) {
                    isInverted = true;
                } else {
                    isInverted = false;
                }
                deepest = worldV.copy();
            }
        }

        if (isInverted) {
            normal.scale(-1.);
        }

        return deepest;
    }


    public void handleCollision(GameObject other, Vector2D collisionNormal, double minOverlap, Vector2D contactPoint) {
        // Relative vector from center of mass to contact
        Vector2D ra = Vector2D.sub(contactPoint,this.position);
        Vector2D rb = Vector2D.sub(contactPoint,other.position);

        // Relative velocity
        Vector2D rv = Vector2D.sub(Vector2D.add(other.velocity,Vector2D.mult(rb.perpendicular(), (other.angularVelocity))),
                          (Vector2D.add(this.velocity,Vector2D.mult(ra.perpendicular(),(this.angularVelocity)))));

        double velAlongNormal = rv.dot(collisionNormal);
        if (velAlongNormal > 0) return; // Bodies are separating

        // Restitution (elasticity)
        double e = Math.min(this.restitution, other.restitution);
        
        // Calculate impulse scalar
        double raCrossN = Vector2D.cross(ra,collisionNormal);
        double rbCrossN = Vector2D.cross(rb,collisionNormal);
        double invMassSum =  this.invMass + other.invMass + 
                            (raCrossN * raCrossN) *  this.invInertia+ 
                            (rbCrossN * rbCrossN) * other.invInertia;

        double j = -(1 + e) * velAlongNormal / invMassSum;

        // Impulse
        Vector2D impulse = Vector2D.mult(collisionNormal,(double)j);
        this.applyImpulse(Vector2D.mult(impulse,-1.), ra, other.invMass);
        other.applyImpulse(impulse, rb, this.invMass);

        // --- Friction ---
        rv = Vector2D.sub(Vector2D.add(other.velocity,Vector2D.mult(rb.perpendicular(), (other.angularVelocity))),
            (Vector2D.add(this.velocity,Vector2D.mult(ra.perpendicular(),(this.angularVelocity)))));

        Vector2D tangent = Vector2D.sub(rv,Vector2D.mult(collisionNormal,(rv.dot(collisionNormal)))).normal();
        double jt = -rv.dot(tangent) / invMassSum;


        double mu = Math.sqrt(this.friction * other.friction);
        Vector2D frictionImpulse;
        if (Math.abs(jt) < j * mu) {
            frictionImpulse = Vector2D.mult(tangent,jt);
        } else {
            frictionImpulse = Vector2D.mult(tangent, (-j * mu));
        }

        this.applyImpulse(Vector2D.mult(frictionImpulse,-1.), ra, other.invMass);
        other.applyImpulse(frictionImpulse, rb, this.invMass);

        // Positional correction to avoid sinking
        final double percent = 0.2; // 20% of penetration
        final double slop = 0.0;
        Vector2D correction = Vector2D.mult(collisionNormal, 
            (Math.max(minOverlap - slop, 0.0) / (this.invMass + other.invMass) * percent));
        this.setPosition(Vector2D.sub(this.position,Vector2D.mult(correction,this.invMass)));
        other.setPosition(Vector2D.add(other.position,Vector2D.mult(correction,other.invMass)));

        // Logger.recordPoint(contactPoint, 0xCCFF0000);
        Logger.recordVector(Vector2D.mult(collisionNormal,100.f), 
            new PVector((float) contactPoint.getX(), 0.f, (float) contactPoint.getY()), 0xFF00FFFF);
        // this.collidable = false;
        // other.collidable = false;
    }

    public void update() {
        collidable = true;
        if (!isStatic) {
            // postion updates
            position.add(Vector2D.mult(velocity, Constants.deltaTime));
            velocity.add(Vector2D.mult(acceleration, Constants.deltaTime));
            this.setRotation(rotation + (angularVelocity * Constants.deltaTime));
            this.angularVelocity += angularAcceleration * Constants.deltaTime;

            // friction
            velocity.scale(0.997);
            this.angularVelocity *= 0.98;
        }
    }

    public void draw(PGraphics app) {
        System.out.println("Drawing object at: " + position);
        // app.fill(127);
        // app.stroke(0);
        // app.strokeWeight(2);
        // app.beginShape();
        // for (Vector2D vertex : vertices) {

        //     Vector2D newVertex = vertex.copy().rotate(this.getRotation());
        //     Vector2D point = new Vector2D((position.getX() + newVertex.getX()),(position.getY() + newVertex.getY()));
        //     app.vertex((float) point.getX(), (float) point.getY());
        // }
        // app.endShape(2);
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }
    public Vector2D getPosition() {
        return position;
    }
    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }
    public Vector2D getVelocity() {
        return velocity;
    }
    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }
    public void addAcceleration(Vector2D acceleration) {
        this.acceleration.add(acceleration);
    }
    public Vector2D getAcceleration() {
        return acceleration;
    }
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    public double getRotation() {
        return rotation;
    }
    public void setAngularVelocity(double angularVelocity) {
        this.angularVelocity = angularVelocity;
    }
    public double getAngularVelocity() {
        return angularVelocity;
    }
    public void setAngularAcceleration(double angularAcceleration) {
        this.angularAcceleration = angularAcceleration;
    }
    public double getAngularAcceleration() {
        return angularAcceleration;
    }
    public Vector2D[] getVertices() {
        return vertices;
    }

    public void applyImpulse(Vector2D impulse, Vector2D point, double otherInvMass) {
        if (this.invMass == 0) return; // No impulse if static
        // Before applying angular velocity
        double angularImpulse = Vector2D.cross(point,impulse);
        if (otherInvMass == 0) angularImpulse *= 0.5; // soften static collision
        this.angularVelocity += invInertia * angularImpulse;

        // Clamp
        this.angularVelocity = Math.max(-10, Math.min(angularVelocity, 10));

        this.velocity.add(Vector2D.mult(impulse, this.getInvMass()));
    }

    public void notCollidable() {
        this.isCollidable = false;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public void setInertia(double inertia) {
        if (inertia == 0) {
            return;
        }
        this.inertia = inertia;
        this.invInertia = 1. / inertia; 
    }

    public double getInertia() {
        return this.inertia;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public boolean getStatic() {
        return isStatic;
    }

    public double getInvMass() {
        return invMass;
    }
    
    public void setVertices(Vector2D[] vertices) {
        this.vertices = vertices;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    // method chaining
    public GameObject withElasticity(double elasticity) {
        this.restitution = elasticity;
        return this;
    }
    // public GameObject withInertia(double inertia) {
    //     this.setInertia(inertia);
    //     return this;
    // }
}
