package create_project;

public class Mechanism {
    private double maxVel, maxAccel;
    private double startPos, endPos, direction;
    private double accelTime, cruiseTime, decelTime;
    private double totalTime;
    private double accelDist, cruiseDist, decelDist;

    public Mechanism(double startPos, double endPos, double maxVel, double maxAccel) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.maxVel = maxVel * Constants.deltaTime;
        this.maxAccel = maxAccel * Constants.deltaTime;

        double distance = endPos - startPos;
        direction = Math.signum(distance);
        distance = Math.abs(distance);

        double accelTimeRaw = maxVel / maxAccel;
        double accelDistRaw = 0.5 * maxAccel * accelTimeRaw * accelTimeRaw;

        if (2 * accelDistRaw > distance) {
            // Triangle profile (max velocity not reached)
            accelTime = Math.sqrt(distance / maxAccel);
            decelTime = accelTime;
            cruiseTime = 0;
            maxVel = maxAccel * accelTime;
        } else {
            // Trapezoidal profile
            accelTime = accelTimeRaw;
            decelTime = accelTimeRaw;
            cruiseDist = distance - 2 * accelDistRaw;
            cruiseTime = cruiseDist / maxVel;
        }

        accelDist = 0.5 * maxAccel * accelTime * accelTime;
        decelDist = accelDist;
        totalTime = accelTime + cruiseTime + decelTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getPosition(double t) {
        t = Math.min(t, totalTime);

        if (t < accelTime) {
            // Acceleration phase
            return startPos + direction * 0.5 * maxAccel * t * t;
        } else if (t < accelTime + cruiseTime) {
            // Cruise phase
            double dt = t - accelTime;
            return startPos + direction * (accelDist + maxVel * dt);
        } else {
            // Deceleration phase
            double dt = t - (accelTime + cruiseTime);
            return startPos + direction * (accelDist + cruiseDist + maxVel * dt - 0.5 * maxAccel * dt * dt);
        }
    }

    public double getVelocity(double t) {
        t = Math.min(t, totalTime);

        if (t < accelTime) {
            return direction * maxAccel * t;
        } else if (t < accelTime + cruiseTime) {
            return direction * maxVel;
        } else {
            double dt = t - (accelTime + cruiseTime);
            return direction * (maxVel - maxAccel * dt);
        }
    }

    public double getAcceleration(double t) {
        t = Math.min(t, totalTime);

        if (t < accelTime) {
            return direction * maxAccel;
        } else if (t < accelTime + cruiseTime) {
            return 0;
        } else {
            return -direction * maxAccel;
        }
    }
}
