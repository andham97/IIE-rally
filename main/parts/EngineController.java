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
    public void setSpeed(float speed, boolean leftWheel, boolean rightWheel) {
        if(leftWheel)
        	motorFrontLeft.setSpeed(speed);
        if(rightWheel)
        	motorFrontRight.setSpeed(speed);
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
        motorFrontLeft.forward();
        motorFrontRight.forward();
    }

    /**
     * Stops both motors. Avoid using this as it might cause an unintentional
     * turn of the vehicle because of motor sync issues.
     */
    public void stop() {
        motorFrontLeft.stop(true);
        motorFrontRight.stop(true);
    }

    /**
     * Turns to the left by a number of degrees.
     *
     * @param degrees
     * @throws InterruptedException 
     */
    public void leftTurn(float e, float stabilizer, float turnM) throws InterruptedException {
    	setSpeed(stabilizer*turnM, true, false);
    	setSpeed((e*turnM), false, true);
    	forward();
    }

    /**
     * Turns to the right by a number of degrees.
     *
     * @param degrees
     * @throws InterruptedException 
     */
    public void rightTurn(float e, float stabilizer, float turnM) throws InterruptedException {
    	setSpeed(stabilizer*turnM, false, true);
    	setSpeed((e*turnM), true, false);
    	forward();
    }
    
    public void stopLeftTurn(float regularSpeed)throws InterruptedException{
    	setSpeed(regularSpeed, true, false);
    	forward();
    }
    public void stopRightTurn(float regularSpeed)throws InterruptedException{
    	setSpeed(regularSpeed, false, true);
    	forward();
    }
}