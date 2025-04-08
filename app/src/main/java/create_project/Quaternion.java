package create_project;

public class Quaternion {
    public double w, x, y, z;

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(double yaw, double pitch, double roll) {
        double cy = Math.cos(yaw * 0.5);
        double sy = Math.sin(yaw * 0.5);
        double cp = Math.cos(pitch * 0.5);
        double sp = Math.sin(pitch * 0.5);
        double cr = Math.cos(roll * 0.5);
        double sr = Math.sin(roll * 0.5);

        this.w = cr * cp * cy + sr * sp * sy;
        this.x = sr * cp * cy - cr * sp * sy;
        this.y = cr * sp * cy + sr * cp * sy;
        this.z = cr * cp * sy - sr * sp * cy;
    }

    public double[] toYawPitchRoll() {
        double yaw, pitch, roll;

        // roll (x-axis rotation)
        double sinr_cosp = 2 * (w * x + y * z);
        double cosr_cosp = 1 - 2 * (x * x + y * y);
        roll = Math.atan2(sinr_cosp, cosr_cosp);

        // pitch (y-axis rotation)
        double sinp = 2 * (w * y - z * x);
        if (Math.abs(sinp) >= 1)
            pitch = Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        else
            pitch = Math.asin(sinp);

        // yaw (z-axis rotation)
        double siny_cosp = 2 * (w * z + x * y);
        double cosy_cosp = 1 - 2 * (y * y + z * z);
        yaw = Math.atan2(siny_cosp, cosy_cosp);

        return new double[]{yaw, pitch, roll};
    }

    public double getYaw() {
        return toYawPitchRoll()[0];
    }

    public double getPitch() {
        return toYawPitchRoll()[1];
    }

    public double getRoll() {
        return toYawPitchRoll()[2];
    }

    public static Quaternion identity() {
        return new Quaternion(1, 0, 0, 0);
    }

    public Quaternion add(Quaternion q) {
        return new Quaternion(w + q.w, x + q.x, y + q.y, z + q.z);
    }

    public Quaternion scale(double s) {
        return new Quaternion(w * s, x * s, y * s, z * s);
    }

    public Quaternion multiply(Quaternion q) {
        return new Quaternion(
            w * q.w - x * q.x - y * q.y - z * q.z,
            w * q.x + x * q.w + y * q.z - z * q.y,
            w * q.y - x * q.z + y * q.w + z * q.x,
            w * q.z + x * q.y - y * q.x + z * q.w
        );
    }

    public Quaternion normalize() {
        double len = Math.sqrt(w * w + x * x + y * y + z * z);
        if (len < 1e-8) return identity();
        return new Quaternion(w / len, x / len, y / len, z / len);
    }

    public Matrix3x3 toMatrix() {
        double xx = x * x, yy = y * y, zz = z * z;
        double xy = x * y, xz = x * z, yz = y * z;
        double wx = w * x, wy = w * y, wz = w * z;

        return new Matrix3x3(
            1 - 2 * (yy + zz), 2 * (xy - wz),     2 * (xz + wy),
            2 * (xy + wz),     1 - 2 * (xx + zz), 2 * (yz - wx),
            2 * (xz - wy),     2 * (yz + wx),     1 - 2 * (xx + yy)
        );
    }

    public static Quaternion fromAxisAngle(Vector3D axis, double angle) {
        Vector3D norm = axis.normalize();
        double halfAngle = angle * 0.5;
        double sinHalf = Math.sin(halfAngle);
        return new Quaternion(
            Math.cos(halfAngle),
            norm.x * sinHalf,
            norm.y * sinHalf,
            norm.z * sinHalf
        );
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f, %.3f)", w, x, y, z);
    }
} 