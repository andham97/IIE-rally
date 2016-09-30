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
    private BaseRegulatedMotor motorFrontLeft;
    private BaseRegulatedMotor motorFrontRight;

    
    public EngineController(BaseRegulatedMotor frontLeft,
            BaseRegulatedMotor frontRight) {
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
        motorFrontLeft.rotate(degrees, true);
        motorFrontRight.rotate(degrees, dontWait);
    }

    /**
     * Drives forward until stop() is called.
     */
    public void forward() {
        motorFrontLeft.backward();
        motorFrontRight.backward();
    }

    /**
     * Stops both motors. Avoid using this as it might cause an unintentional
     * turn of the vehicle because of motor sync issues.
     */
    public void stop() {
        motorFrontLeft.stop(true);
        motorFrontRight.stop();
    }

    /**
     * Turns to the left by a number of degrees.
     *
     * @param degrees
     * @throws InterruptedException 
     */
    public void leftTurn(int turnSpeed, int regularSpeed) throws InterruptedException {
        /*int turnDeg = (int)degrees; //Math.round(TURN_CIRCUMFERENCE * degrees / WHEEL_CIRCUMFERENCE) * DIR;
        motorFrontLeft.rotate(turnDeg, true);
        motorFrontRight.rotate(-turnDeg);*/
    	motorFrontRight.setSpeed(turnSpeed);
    	Thread.sleep(200);
    	motorFrontRight.setSpeed(regularSpeed);
    }

    /**
     * Turns to the right by a number of degrees.
     *
     * @param degrees
     * @throws InterruptedException 
     */
    public void rightTurn(int turnSpeed, int regularSpeed) throws InterruptedException {
        /*int turnDeg = (int)degrees;//Math.round(TURN_CIRCUMFERENCE * degrees / WHEEL_CIRCUMFERENCE) * DIR;
        motorFrontLeft.rotate(-turnDeg, true);
        motorFrontRight.rotate(turnDeg);*/
    	motorFrontLeft.setSpeed(turnSpeed);
    	Thread.sleep(200);
    	motorFrontLeft.setSpeed(regularSpeed);
    }
}
