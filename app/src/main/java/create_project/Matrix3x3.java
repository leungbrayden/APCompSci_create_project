package create_project;

public class Matrix3x3 {
    public double[][] m;

    public Matrix3x3() {
        m = new double[3][3];
    }

    public Matrix3x3(double m00, double m01, double m02,
                     double m10, double m11, double m12,
                     double m20, double m21, double m22) {
        m = new double[][] {
            {m00, m01, m02},
            {m10, m11, m12},
            {m20, m21, m22}
        };
    }

    public static Matrix3x3 identity() {
        return new Matrix3x3(
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
        );
    }

    public static Matrix3x3 zero() {
        return new Matrix3x3(
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
        );
    }

    public Matrix3x3 transpose() {
        return new Matrix3x3(
            m[0][0], m[1][0], m[2][0],
            m[0][1], m[1][1], m[2][1],
            m[0][2], m[1][2], m[2][2]
        );
    }

    public Matrix3x3 inverse() {
        double a = m[0][0], b = m[0][1], c = m[0][2];
        double d = m[1][0], e = m[1][1], f = m[1][2];
        double g = m[2][0], h = m[2][1], i = m[2][2];

        double det = a*(e*i - f*h) - b*(d*i - f*g) + c*(d*h - e*g);
        if (Math.abs(det) < 1e-8) return Matrix3x3.zero();
        double invDet = 1.0 / det;

        return new Matrix3x3(
            (e*i - f*h) * invDet,
            (c*h - b*i) * invDet,
            (b*f - c*e) * invDet,

            (f*g - d*i) * invDet,
            (a*i - c*g) * invDet,
            (c*d - a*f) * invDet,

            (d*h - e*g) * invDet,
            (b*g - a*h) * invDet,
            (a*e - b*d) * invDet
        );
    }

    public Matrix3x3 multiply(Matrix3x3 other) {
        Matrix3x3 result = new Matrix3x3();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                for (int k = 0; k < 3; k++) {
                    result.m[row][col] += this.m[row][k] * other.m[k][col];
                }
            }
        }
        return result;
    }

    public Vector3D multiply(Vector3D v) {
        return new Vector3D(
            m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z,
            m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z,
            m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z
        );
    }

    @Override
    public String toString() {
        return String.format("[%.3f %.3f %.3f]\n[%.3f %.3f %.3f]\n[%.3f %.3f %.3f]",
            m[0][0], m[0][1], m[0][2],
            m[1][0], m[1][1], m[1][2],
            m[2][0], m[2][1], m[2][2]
        );
    }
} 
