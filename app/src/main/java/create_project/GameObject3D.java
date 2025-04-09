package create_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import processing.core.PApplet;
import processing.core.PGraphics;


public class GameObject3D {
    // physics properties
    private Vector3D position, velocity, acceleration;
    private Quaternion orientation;
    private Vector3D angularVelocity, angularAcceleration;
    private double mass, restitution, friction, invMass;
    private Matrix3x3 inertiaTensor, invInertiaTensor;
    private boolean isStatic, isVisible, isCollidable, isGravityEnabled;
    private List<Vector3D> localVertices;
    private List<int[]> edges;
    private List<int[]> faces;

    // visual properties
    private int color;

    public GameObject3D(Vector3D position, Vector3D velocity, Vector3D acceleration,
                        Quaternion orientation, Vector3D angularVelocity, Vector3D angularAcceleration,
                        double mass, boolean isStatic, boolean isVisible, List<Vector3D> vertices, int color) {

        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.orientation = orientation;
        this.angularVelocity = angularVelocity;
        this.angularAcceleration = angularAcceleration;
        this.mass = mass;
        this.isStatic = isStatic;
        this.isVisible = isVisible;
        this.localVertices = vertices;
        this.edges = computeEdges(vertices);
        this.faces = computeBoxFaces();
        this.color = color;

        this.isCollidable = true;
        this.restitution = 0.4;
        this.friction = 0.6;

        if (mass == 0 || isStatic) {
            invMass = 0;
            inertiaTensor = Matrix3x3.identity();
            invInertiaTensor = Matrix3x3.zero();
        } else {
            invMass = 1.0 / mass;
            inertiaTensor = computeBoxInertiaTensor(mass, vertices);
            invInertiaTensor = inertiaTensor.inverse();
        }
    }

    public GameObject3D(List<Vector3D> vertices, Vector3D position, double mass, int color) {
        this(position, new Vector3D(), new Vector3D(),
             Quaternion.identity(), new Vector3D(), new Vector3D(),
             mass, false, true, vertices, color);
    }

    public static List<Vector3D> createBoxVertices(double width, double height, double depth) {
        return Arrays.asList(
            new Vector3D( width/2,  height/2,  depth/2),
            new Vector3D(-width/2,  height/2,  depth/2),
            new Vector3D(-width/2, -height/2,  depth/2),
            new Vector3D( width/2, -height/2,  depth/2),
            new Vector3D( width/2,  height/2, -depth/2),
            new Vector3D(-width/2,  height/2, -depth/2),
            new Vector3D(-width/2, -height/2, -depth/2),
            new Vector3D( width/2, -height/2, -depth/2)
        );
    }

    public static GameObject3D createBox(Vector3D position, double width, double height, double depth, double mass, int color) {
        List<Vector3D> vertices = Arrays.asList(
            new Vector3D( width/2,  height/2,  depth/2),
            new Vector3D(-width/2,  height/2,  depth/2),
            new Vector3D(-width/2, -height/2,  depth/2),
            new Vector3D( width/2, -height/2,  depth/2),
            new Vector3D( width/2,  height/2, -depth/2),
            new Vector3D(-width/2,  height/2, -depth/2),
            new Vector3D(-width/2, -height/2, -depth/2),
            new Vector3D( width/2, -height/2, -depth/2)
        );
        return new GameObject3D(position, new Vector3D(), new Vector3D(),
                                Quaternion.identity(), new Vector3D(), new Vector3D(),
                                mass, mass == 0, true, vertices, color);
    }

    public static GameObject3D createBox(Vector3D position, double width, double height, double depth, double mass) {
        return createBox(position, width, height, depth, mass, 0xFFFFFFFF);
    }

    private List<int[]> computeBoxFaces() {
        return Arrays.asList(
            new int[]{0, 1, 2}, new int[]{0, 2, 3}, // front
            new int[]{4, 5, 6}, new int[]{4, 6, 7}, // back
            new int[]{0, 4, 5}, new int[]{0, 5, 1}, // top
            new int[]{3, 2, 6}, new int[]{3, 6, 7}, // bottom
            new int[]{1, 5, 6}, new int[]{1, 6, 2}, // left
            new int[]{0, 3, 7}, new int[]{0, 7, 4}  // right
        );
    }

    private Matrix3x3 computeBoxInertiaTensor(double mass, List<Vector3D> verts) {
        double width = verts.get(0).x * 2;
        double height = verts.get(0).y * 2;
        double depth = verts.get(0).z * 2;
        double x2 = width * width;
        double y2 = height * height;
        double z2 = depth * depth;
        double factor = mass / 12.0;

        return new Matrix3x3(
            factor * (y2 + z2), 0, 0,
            0, factor * (x2 + z2), 0,
            0, 0, factor * (x2 + y2)
        );
    }

    private List<int[]> computeEdges(List<Vector3D> verts) {
        Set<String> edgeSet = new HashSet<>();
        List<int[]> edgeList = new ArrayList<>();
        int n = verts.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Vector3D vi = verts.get(i);
                Vector3D vj = verts.get(j);
                Vector3D diff = vj.sub(vi);
                if (diff.length() < 1e-6) continue;
                String key = i + "," + j;
                String revKey = j + "," + i;
                if (!edgeSet.contains(key) && !edgeSet.contains(revKey)) {
                    edgeSet.add(key);
                    edgeList.add(new int[]{i, j});
                }
            }
        }
        return edgeList;
    }

    public CollisionInfo checkCollision(GameObject3D other) {
        List<Vector3D> vertsA = getWorldVertices();
        List<Vector3D> vertsB = other.getWorldVertices();

        List<Vector3D> axes = new ArrayList<>();
        axes.addAll(getSeparatingAxes(vertsA, edges));
        axes.addAll(getSeparatingAxes(vertsB, other.edges));
        axes.addAll(getFaceNormals(vertsA, faces));
        axes.addAll(getFaceNormals(vertsB, other.faces));

        for (int[] edgeA : edges) {
            Vector3D edgeVecA = vertsA.get(edgeA[1]).sub(vertsA.get(edgeA[0])).normalize();
            for (int[] edgeB : other.edges) {
                Vector3D edgeVecB = vertsB.get(edgeB[1]).sub(vertsB.get(edgeB[0])).normalize();
                Vector3D crossAxis = edgeVecA.cross(edgeVecB);
                if (crossAxis.length() > 1e-6) {
                    axes.add(crossAxis.normalize());
                }
            }
        }

        double minOverlap = Double.MAX_VALUE;
        Vector3D minAxis = null;

        for (Vector3D axis : axes) {
            double[] projA = projectVertices(vertsA, axis);
            double[] projB = projectVertices(vertsB, axis);

            double overlap = Math.min(projA[1], projB[1]) - Math.max(projA[0], projB[0]);
            if (overlap < 0) {
                return new CollisionInfo(); // No collision
            }

            if (overlap < minOverlap) {
                minOverlap = overlap;
                minAxis = axis;

                Vector3D centerDelta = other.position.sub(this.position);
                if (centerDelta.dot(minAxis) < 0) {
                    minAxis = minAxis.scale(-1);
                }
            }
        }

        return new CollisionInfo(true, minAxis, minOverlap);
    }

    private List<Vector3D> getSeparatingAxes(List<Vector3D> verts, List<int[]> edgeList) {
        List<Vector3D> axes = new ArrayList<>();
        for (int[] edge : edgeList) {
            Vector3D v1 = verts.get(edge[0]);
            Vector3D v2 = verts.get(edge[1]);
            Vector3D edgeVec = v2.sub(v1).normalize();
            axes.add(edgeVec);
        }
        return axes;
    }

    private List<Vector3D> getFaceNormals(List<Vector3D> verts, List<int[]> faces) {
        List<Vector3D> normals = new ArrayList<>();
        for (int[] face : faces) {
            Vector3D a = verts.get(face[0]);
            Vector3D b = verts.get(face[1]);
            Vector3D c = verts.get(face[2]);
            Vector3D ab = b.sub(a);
            Vector3D ac = c.sub(a);
            Vector3D normal = ab.cross(ac).normalize();
            if (normal.length() > 1e-6) normals.add(normal);
        }
        return normals;
    }

    private boolean overlapOnAxis(List<Vector3D> vertsA, List<Vector3D> vertsB, Vector3D axis) {
        double[] a = projectVertices(vertsA, axis);
        double[] b = projectVertices(vertsB, axis);
        return !(a[1] < b[0] || b[1] < a[0]);
    }

    private double[] projectVertices(List<Vector3D> verts, Vector3D axis) {
        double min = axis.dot(verts.get(0));
        double max = min;
        for (Vector3D v : verts) {
            double projection = axis.dot(v);
            if (projection < min) min = projection;
            if (projection > max) max = projection;
        }
        return new double[]{min, max};
    }

    public List<Vector3D> getWorldVertices() {
        List<Vector3D> worldVerts = new ArrayList<>();
        Matrix3x3 rot = orientation.toMatrix();
        for (Vector3D local : localVertices) {
            Vector3D rotated = rot.multiply(local);
            worldVerts.add(position.add(rotated));
        }
        return worldVerts;
    }

    public Vector3D getContactPoint(GameObject3D other) {
        List<Vector3D> vertsA = this.getWorldVertices();
        List<Vector3D> vertsB = other.getWorldVertices();

        double minDist = Double.MAX_VALUE;
        Vector3D closestA = null;

        for (Vector3D va : vertsA) {
            for (Vector3D vb : vertsB) {
                double dist = va.sub(vb).lengthSquared();
                if (dist < minDist) {
                    minDist = dist;
                    closestA = va;
                }
            }
        }

        return closestA != null ? closestA : this.position;
    }

    public List<Vector3D> getContactPoints(GameObject3D other, Vector3D collisionNormal) {
        List<Vector3D> contacts = new ArrayList<>();
        double contactThreshold = 50.;

        List<Vector3D> vertsA = this.getWorldVertices();
        List<Vector3D> vertsB = other.getWorldVertices();

        for (Vector3D va : vertsA) {
            for (Vector3D vb : vertsB) {
                Vector3D delta = va.sub(vb);
                double separation = Math.abs(delta.dot(collisionNormal));
                if (separation < contactThreshold) {
                    contacts.add(va.add(vb).scale(0.5));
                }
            }
        }

        if (contacts.isEmpty()) {
            contacts.add(this.getContactPoint(other));
        }

        return contacts;
    }


    public Vector3D getPosition() { return position; }
    public void setPosition(Vector3D pos) { this.position = pos; }
    public Vector3D getVelocity() { return velocity; }
    public void setVelocity(Vector3D vel) { this.velocity = vel; }
    public Vector3D getAcceleration() { return acceleration; }
    public void setAcceleration(Vector3D acc) { this.acceleration = acc; }
    public Quaternion getOrientation() { return orientation; }
    public void setOrientation(Quaternion q) { this.orientation = q; }
    public Vector3D getAngularVelocity() { return angularVelocity; }
    public Vector3D getAngularAcceleration() { return angularAcceleration; }
    public boolean isStatic() { return isStatic; }
    public boolean isVisible() { return isVisible; }
    public boolean isCollidable() { return isCollidable; }
    public List<Vector3D> getLocalVertices() { return localVertices; }
    // chain methods
    public GameObject3D setCollidable(boolean collidable) { this.isCollidable = collidable; return this;}
    public GameObject3D setStatic(boolean isStatic) { this.isStatic = isStatic; return this; }
    public GameObject3D setVisible(boolean isVisible) { this.isVisible = isVisible; return this; }
    public GameObject3D setGravityEnabled(boolean isGravityEnabled) { this.isGravityEnabled = isGravityEnabled; return this; }

    public Matrix3x3 getInertiaTensorWorld() {
        Matrix3x3 rot = orientation.toMatrix();
        return rot.multiply(invInertiaTensor).multiply(rot.transpose());
    }

    public void update() {
        if (isStatic) return;

        if (isGravityEnabled) {
            if (position.y > 0) 
                velocity = velocity.add(new Vector3D(0, -100, 0).scale(Constants.deltaTime));
        }
        velocity = velocity.add(acceleration.scale(Constants.deltaTime));
        position = position.add(velocity.scale(Constants.deltaTime));

        angularVelocity = angularVelocity.add(angularAcceleration.scale(Constants.deltaTime));
        Quaternion deltaOrientation = new Quaternion(0, angularVelocity.x, angularVelocity.y, angularVelocity.z)
                                           .multiply(orientation).scale(0.5 * Constants.deltaTime);
        orientation = orientation.add(deltaOrientation).normalize();

        velocity = velocity.scale(0.99);
        angularVelocity = angularVelocity.scale(0.95);
        if (angularVelocity.length() < 0.05) {
            angularVelocity = new Vector3D();
        }
    }

    public void resolveCollision(GameObject3D other) {
        CollisionInfo collisionInfo = this.checkCollision(other);
        if (!collisionInfo.isColliding || !this.isCollidable || !other.isCollidable) return;

        List<Vector3D> contactPoints = this.getContactPoints(other, collisionInfo.normal);
        Matrix3x3 invInertiaA = this.getInertiaTensorWorld();
        Matrix3x3 invInertiaB = other.getInertiaTensorWorld();
        double e = Math.min(this.restitution, other.restitution);
        double mu = Math.sqrt(this.friction * other.friction);

        for (Vector3D contactPoint : contactPoints) {
            Vector3D ra = contactPoint.sub(this.position);
            Vector3D rb = contactPoint.sub(other.position);

            Vector3D velA = this.velocity.add(this.angularVelocity.cross(ra));
            Vector3D velB = other.velocity.add(other.angularVelocity.cross(rb));
            Vector3D rv = velB.sub(velA);

            double velAlongNormal = rv.dot(collisionInfo.normal);
            if (velAlongNormal > 0) continue;

            Vector3D raCrossN = ra.cross(collisionInfo.normal);
            Vector3D rbCrossN = rb.cross(collisionInfo.normal);

            double invMassSum = this.invMass + other.invMass
                + collisionInfo.normal.dot(invInertiaA.multiply(raCrossN).cross(ra))
                + collisionInfo.normal.dot(invInertiaB.multiply(rbCrossN).cross(rb));

            double j = -(1 + e) * velAlongNormal / invMassSum / contactPoints.size();
            Vector3D impulse = collisionInfo.normal.scale(j);

            applyImpulse(impulse.scale(-1), ra);
            other.applyImpulse(impulse, rb);

            // Friction
            velA = this.velocity.add(this.angularVelocity.cross(ra));
            velB = other.velocity.add(other.angularVelocity.cross(rb));
            rv = velB.sub(velA);

            Vector3D tangent = rv.sub(collisionInfo.normal.scale(rv.dot(collisionInfo.normal))).normalize();
            double jt = -rv.dot(tangent) / invMassSum / contactPoints.size();

            Vector3D frictionImpulse = Math.abs(jt) < j * mu ? tangent.scale(jt) : tangent.scale(-j * mu);
            applyImpulse(frictionImpulse.scale(-1), ra);
            other.applyImpulse(frictionImpulse, rb);
        }

        // Positional correction
        final double percent = 1.2;
        final double slop = 0;
        double correctionMagnitude = Math.max(collisionInfo.penetration - slop, 0.0) / (this.invMass + other.invMass) * percent;
        Vector3D correction = collisionInfo.normal.scale(correctionMagnitude);
        if (!this.isStatic) this.position = this.position.sub(correction.scale(this.invMass));
        if (!other.isStatic) other.position = other.position.add(correction.scale(other.invMass));
    }

    public void applyImpulse(Vector3D impulse, Vector3D contactVector) {
        // System.out.println("Applying impulse: " + impulse + " at contact vector: " + contactVector);
        if (invMass == 0) return;
        this.velocity = this.velocity.add(impulse.scale(invMass));
        Vector3D torque = contactVector.cross(impulse);
        this.angularVelocity = this.angularVelocity.add(this.getInertiaTensorWorld().multiply(torque));
        // After updating angularVelocity
        double maxAngularSpeed = 0.1; // radians per second
        angularVelocity.x = Math.max(-maxAngularSpeed, Math.min(maxAngularSpeed, angularVelocity.x));
        angularVelocity.y = Math.max(-maxAngularSpeed, Math.min(maxAngularSpeed, angularVelocity.y));
        angularVelocity.z = Math.max(-maxAngularSpeed, Math.min(maxAngularSpeed, angularVelocity.z));
    }

    public void draw(PGraphics p) {
        System.out.println("rotation: " + this.orientation);
        System.out.println("angular velocity: " + this.angularVelocity);
        System.out.println("angular acceleration: " + this.angularAcceleration);
        if (!isVisible) return;

        p.pushMatrix();
        p.fill(color);
        p.translate((float)this.getPosition().x, (float)this.getPosition().y, (float)this.getPosition().z);
        double[] ypr = this.orientation.toYawPitchRoll();
        p.rotateZ((float) ypr[0]); // Yaw
        p.rotateY((float) ypr[1]); // Pitch
        p.rotateX((float) ypr[2]); // Roll
        p.box((float)localVertices.get(0).x * 2, (float)localVertices.get(0).y * 2, (float)localVertices.get(0).z * 2);
        p.popMatrix();
    }

    class CollisionInfo {
        boolean isColliding;
        Vector3D normal;
        double penetration;

        CollisionInfo() {
            this.isColliding = false;
            this.normal = new Vector3D();
            this.penetration = 0;
        }
        CollisionInfo(boolean isColliding, Vector3D normal, double penetration) {
            this.isColliding = isColliding;
            this.normal = normal;
            this.penetration = penetration;
        }
    }
}
