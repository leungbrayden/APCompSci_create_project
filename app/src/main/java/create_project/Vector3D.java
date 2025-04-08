package create_project;

public class Vector3D {
    public double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D() {
        this(0, 0, 0);
    }

    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    public Vector3D sub(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    public Vector3D scale(double s) {
        return new Vector3D(x * s, y * s, z * s);
    }

    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector3D cross(Vector3D v) {
        return new Vector3D(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x
        );
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Vector3D normalize() {
        double len = length();
        return len == 0 ? new Vector3D(0, 0, 0) : scale(1.0 / len);
    }

    public Vector3D negate() {
        return new Vector3D(-x, -y, -z);
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    @Override
    public String toString() {
        return String.format("Vector3D(%.3f, %.3f, %.3f)", x, y, z);
    }

    public Vector3D clone() {
        return new Vector3D(x, y, z);
    }

    public static Vector3D lerp(Vector3D a, Vector3D b, double t) {
        return a.add(b.sub(a).scale(t));
    }
}
