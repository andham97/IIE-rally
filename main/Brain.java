package main;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.motor.Motor;
import main.parts.EngineController;
import main.parts.MainSensorController;
import main.parts.MainSensorController.SensorSide;
import main.parts.MainSensorController.SensorType;

public class Brain {

    private Keys keys;
    private MainSensorController sensorController;
    private EngineController engine;
    private float engineSpeed = 350f;
    private float engineMinM = 0.6f;
    private float engineSpeedM = engineMinM;
    private float engineMaxM = 1.4f;
    private float stabilizer = 0.0f;
    private float turnM = 0.6f;
    private int stabTimer = 0;
    private int stabTimerLimit = 400;
    private int speedIncCounter = 0;
    private boolean emergencyLeft = false;
    private boolean isRunning = false;
    private boolean prevTurnRight = true;

    /**
     * Constructor for this class, initializes a bunch of important stuff
     */
    public Brain() {
        sensorController = new MainSensorController("S1", SensorType.NXT, "S4", SensorType.EV3);
        // EngineController(Bak venstre, bak høyre, forran venstre, forran høyre)
        engine = new EngineController(Motor.D, Motor.A);
        engine.setSpeed(engineSpeed, true, true);
        keys = BrickFinder.getLocal().getKeys();
        this.start();
        sensorController.start();
    }

    /**
     * Sets the running boolean to true
     */
    public void start() {
        isRunning = true;
        Motor.D.forward();
    }

    public void stop() {
        Motor.D.stop();
        isRunning = false;
        System.exit(0);
    }

    /**
     * Checks if a button on the Lejos device has been pushed, intended to stop
     * the running loop
     */
    public void checkInterruptButton() {
        if (keys.getButtons() != 0) {
            System.exit(0);
        }
    }

    /**
     * The main running loop for the brain
     */
    public void run() throws Exception {
        // Start a thread to check if any terminate buttons are pressed
        new Thread("InterruptButtonMoitor") {
            @Override
            public void run() {
                while (isRunning) {
                    checkInterruptButton();
                }
            }
        }.start();
        engine.forward();
        while (isRunning) {
            this.checkColor();
        }
    }
    
    public void stabTimerIncrease(boolean rightTurn){
    	stabTimer += 1;
    	if(stabTimer >= stabTimerLimit){
    		stabTimer = 0;
    		stabilizer = 0.0f;
    	}else if(prevTurnRight != rightTurn){
    		stabTimer = 0;
    		if(stabilizer < engineSpeed){
    			stabilizer += 100.0f;
    		}
    	}
    	prevTurnRight = rightTurn;
    }
    
    public void checkColor() throws InterruptedException{
    	float leftSide = sensorController.getValue(SensorSide.LEFT);
    	float rightSide = sensorController.getValue(SensorSide.RIGHT) < 0.5 ? 7f : 0f;
    	//Both sides are ON the tape OR ONLY right side
    	if((rightSide == 7 && leftSide == 7) || (leftSide != 7 && rightSide == 7)){
    		emergencyLeft = false;
    		engine.stopLeftTurn(engineSpeed*engineSpeedM);//*engineSpeedM);
    		engine.stopRightTurn(engineSpeed*engineSpeedM);//*engineSpeedM);
    		speedIncCounter ++;
    		if(speedIncCounter == 500){
    			if(engineSpeedM < engineMaxM)
    				engineSpeedM += 0.4f;
    			speedIncCounter = 0;
    		}
    	}
    	//Both sides are NOT on tape
    	else if(rightSide != 7 && leftSide != 7){
    		if(emergencyLeft){
    			engine.leftTurn(engineSpeed, stabilizer, turnM);
    			stabTimerIncrease(false);
    			engineSpeedM = engineMinM;
    			speedIncCounter = 0;
    		}else{
	    		engine.rightTurn(engineSpeed, stabilizer, turnM);
	    		stabTimerIncrease(true);
	    		engineSpeedM = engineMinM;
	    		speedIncCounter = 0;
    		}
    	}
    	//Left side is ON tape & Right side is NOT
    	else if(leftSide == 7 && rightSide != 7){
    		engine.leftTurn(engineSpeed, stabilizer, turnM);
    		stabTimerIncrease(false);
    		emergencyLeft = true;
    		engineSpeedM = engineMinM;
    		speedIncCounter = 0;
    	}
    }
}
