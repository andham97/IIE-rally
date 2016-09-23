package main.parts;

import lejos.hardware.motor.BaseRegulatedMotor;


public class EngineController {

    /**
     * Wheel diameter multiplied by pi.
     */
    public static final float WHEEL_CIRCUMFERENCE = 4.96f * (float) Math.PI;

    /**
     * Distance between wheels multiplied by pi.
     */
    public static final float TURN_CIRCUMFERENCE = 11.175f * (float) Math.PI;
    
    /**
     * 1 or -1 based on motor turn direction.
     */
    public static final int DIR = 1;

    private BaseRegulatedMotor motorBackLeft;
    private BaseRegulatedMotor motorBackRight;
    private BaseRegulatedMotor motorFrontLeft;
    private BaseRegulatedMotor motorFrontRight;

    
    public EngineController(BaseRegulatedMotor backLeft,
            BaseRegulatedMotor backRight,
            BaseRegulatedMotor frontLeft,
            BaseRegulatedMotor frontRight) {
        motorBackLeft = backLeft;
        motorBackRight = backRight;
        motorFrontLeft = frontLeft;
        motorFrontRight = frontRight;
    }

    /**
     * Sets the speed in centimeters per second.
     *
     * @param speed
     */
    public void setSpeed(float speed) {
        float convertedSpeed = speed / WHEEL_CIRCUMFERENCE * 360f;
        motorBackLeft.setSpeed(convertedSpeed);
        motorBackRight.setSpeed(convertedSpeed);
        motorFrontLeft.setSpeed(convertedSpeed);
        motorFrontRight.setSpeed(convertedSpeed);
    }

    /**
     * Drives forward by a number of centimetres. Waits until the move is done.
     *
     * @param dst
     */
    public void forward(float dst) {
        forward(dst, false);
    }
    
    /**
     * Drives forward by a number of centimetres. 
     *
     * @param dst
     * @param dontWait if true, don't wait until the move is done
     */
    public void forward(float dst, boolean dontWait) {
        int degrees = Math.round(dst * 360f / WHEEL_CIRCUMFERENCE) * DIR;
        motorBackLeft.rotate(degrees, true);
        motorBackRight.rotate(degrees, true);
        motorFrontLeft.rotate(degrees, true);
        motorFrontRight.rotate(degrees, dontWait);
    }

    /**
     * Drives backwards by a number of centimetres. Waits until the move is
     * done.
     *
     * @param dst
     */
    public void backward(float dst) {
        backward(dst, false);
    }
    
    /**
     * Drives backwards by a number of centimetres. 
     * 
     * @param dst
     * @param dontWait if true, don't wait until the move is done
     */
    public void backward(float dst, boolean dontWait) {
        int degrees = -Math.round(dst * 360f / WHEEL_CIRCUMFERENCE) * DIR;
        motorBackLeft.rotate(degrees, true);
        motorBackRight.rotate(degrees, true);
        motorFrontLeft.rotate(degrees, true);
        motorFrontRight.rotate(degrees, dontWait);
    }

    /**
     * Drives forward until stop() is called.
     */
    public void forward() {
        motorBackLeft.forward();
        motorBackRight.forward();
        motorFrontLeft.forward();
        motorFrontRight.forward();
    }

    /**
     * Stops both motors. Avoid using this as it might cause an unintentional
     * turn of the vehicle because of motor sync issues.
     */
    public void stop() {
        motorBackLeft.stop(true);
        motorBackRight.stop(true);
        motorFrontLeft.stop(true);
        motorFrontRight.stop();
    }

    /**
     * Turns to the left by a number of degrees.
     *
     * @param degrees
     */
    public void leftTurn(float degrees) {
        int turnDeg = Math.round(TURN_CIRCUMFERENCE * degrees / WHEEL_CIRCUMFERENCE) * DIR;
        motorBackLeft.rotate(-turnDeg, true);
        motorBackRight.rotate(turnDeg, true);
        motorFrontLeft.rotate(-turnDeg, true);
        motorFrontRight.rotate(turnDeg);
    }

    /**
     * Turns to the right by a number of degrees.
     *
     * @param degrees
     */
    public void rightTurn(float degrees) {
        int turnDeg = Math.round(TURN_CIRCUMFERENCE * degrees / WHEEL_CIRCUMFERENCE) * DIR;
        motorBackLeft.rotate(turnDeg, true);
        motorBackRight.rotate(-turnDeg, true);
        motorFrontLeft.rotate(turnDeg, true);
        motorFrontRight.rotate(-turnDeg);
    }
}
