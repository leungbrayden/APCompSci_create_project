package create_project;

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
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

    public Vector2D add(Vector2D other) {
        this.x += other.getX();
        this.y += other.getY();
        return this;
    }

    public Vector2D add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public static Vector2D add(Vector2D a, Vector2D b) {
        return new Vector2D(a.getX() + b.getX(), a.getY() + b.getY());
    }

    public Vector2D sub(Vector2D other) {
        this.x -= other.getX();
        this.y -= other.getY();
        return this;
    }

    public Vector2D sub(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public static Vector2D sub(Vector2D a, Vector2D b) {
        return new Vector2D(a.getX() - b.getX(), a.getY() - b.getY());
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2D normal() {
        double mag = magnitude();
        if (mag == 0) {
            return new Vector2D(0, 0);
        }
        return new Vector2D(x / mag, y / mag);
    }

    public Vector2D scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public double dot(Vector2D other) {
        return this.x * other.getX() + this.y * other.getY();
    }

    public double cross(Vector2D other) {
        return this.x * other.getY() - this.y * other.getX();
    }

    public static double cross(Vector2D a, Vector2D b) {
        return a.getX() * b.getY() - a.getY() * b.getX();
    }

    public Vector2D rotate(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double newX = this.x * cos - this.y * sin;
        double newY = this.x * sin + this.y * cos;
        return new Vector2D(newX, newY);
    }

    public Vector2D copy() {
        return new Vector2D(this.x, this.y);
    }

    public Vector2D perpendicular() {
        return new Vector2D(-this.y, this.x);
    }

    public static Vector2D mult(Vector2D vector, double scalar) {
        return new Vector2D(vector.getX() * scalar, vector.getY() * scalar);
    }
}
