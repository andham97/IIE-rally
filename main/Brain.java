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
    private int engineSpeed = 200;
    private boolean sideOfStroke = false;
    private boolean lastRight = false;
    private boolean isRunning = false;

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

        while (isRunning) {
            engine.forward(5, true);
            this.checkColor();
            Thread.sleep(2);
        }
    }
    
    public void checkColor() throws InterruptedException{
    	float leftSide = sensorController.getValue(SensorSide.LEFT);
    	float rightSide = sensorController.getValue(SensorSide.RIGHT) < 0.5 ? 7f : 0f;
    	System.out.println(leftSide);
    	System.out.println(rightSide);
    	System.out.println("");
    	if(rightSide != 7 && leftSide != 7){
    		if(sideOfStroke){
    			engine.stopLeftTurn(engineSpeed);
        		engine.leftTurn(engineSpeed);
    		}
    		else {
    			engine.stopLeftTurn(engineSpeed);
        		engine.rightTurn(engineSpeed);
    		}
    		
    	}
    	else if(leftSide == 7 && rightSide != 7){
    		if(lastRight)
    			sideOfStroke = true;
    		engine.stopRightTurn(engineSpeed);
    		engine.leftTurn(engineSpeed);
    		lastRight = false;
    	}
    	else {
    		lastRight = true;
    		sideOfStroke = false;
    		engine.stopLeftTurn(engineSpeed);
    		engine.stopRightTurn(engineSpeed);
    	}
    }
}
