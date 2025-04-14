package create_project;

public class MotionProfile {
    private double maxVel, maxAccel, velocity;
    private double startPos, endPos, direction;
    private double minPos, maxPos;
    private double accelTime, cruiseTime, decelTime;
    private double totalTime;
    private double accelDist, cruiseDist, decelDist;

    private double time = 0;
    private boolean isMoving = false;

    public MotionProfile(double minPos, double maxPos, double maxVel, double maxAccel) {
        this.maxVel = maxVel;
        this.maxAccel = maxAccel;
        this.minPos = minPos;
        this.maxPos = maxPos;

        reset(minPos, maxPos);
        stop();
    }

    public void reset(double startPos, double endPos) {
        this.startPos = startPos;
        this.endPos = endPos;
        if (startPos == endPos) {
            isMoving = false;
            return;
        }
        velocity = maxVel;
        double distance = endPos - startPos;
        direction = Math.signum(distance);
        distance = Math.abs(distance);

        double accelTimeRaw = velocity / maxAccel;
        double accelDistRaw = 0.5 * maxAccel * accelTimeRaw * accelTimeRaw;

        if (2 * accelDistRaw > distance) {
            // Triangle profile (max velocity not reached)
            accelTime = Math.sqrt(distance / maxAccel);
            decelTime = accelTime;
            cruiseTime = 0;
            this.velocity = maxAccel * accelTime;
        } else {
            // Trapezoidal profile
            accelTime = accelTimeRaw;
            decelTime = accelTimeRaw;
            cruiseDist = distance - 2 * accelDistRaw;
            cruiseTime = cruiseDist / velocity;
        }

        accelDist = 0.5 * maxAccel * accelTime * accelTime;
        decelDist = accelDist;
        totalTime = accelTime + cruiseTime + decelTime;

        this.start();
    }
    

    public double getTotalTime() {
        return totalTime;
    }
    
    public double getMinPos() {
        return minPos;
    }
    public double getMaxPos() {
        return maxPos;
    }

    public void setMinPos(double minPos) {
        this.minPos = minPos;
    }

    public void setMaxPos(double maxPos) {
        this.maxPos = maxPos;
    }

    public double getTime() {
        return time;
    }

    public double getPosition() {
        if (time > totalTime) {
            return endPos;
        }

        if (time < accelTime) {
            // Acceleration phase
            return startPos + (direction * 0.5 * maxAccel * time * time);
        } else if (time > totalTime - accelTime){
            // Deceleration phase
            double dt = time - (accelTime + cruiseTime);
            return startPos + (direction * (accelDist + cruiseDist + velocity * dt - (0.5 * maxAccel * dt * dt)));
        }  else {
            // Cruise phase
            double dt = time - accelTime;
            return startPos + (direction * (accelDist + velocity * dt));
        }
    }

    public void reset() {
        isMoving = false;
        time = 0;
    }

    public void update() {
        if (isMoving) {
            time += Constants.deltaTime;
        }
    }

    public void start() {
        time = 0;
        isMoving = true;
    }

    public void stop() {
        isMoving = false;
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
