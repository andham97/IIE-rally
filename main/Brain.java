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
	private int engineSpeed = 100;
	private float turnDegrees = 50.0f;
	private float lastRColor = 0;
	private float lastLColor = 0;
	private boolean isRunning = false;
        
        /**
         * The color value of the track line.
         */
        private final int REACT_COLOR_VALUE = 7;
	
        
	/**
         * Constructor for this class, initializes a bunch of important stuff
         */
	public Brain(){
		sensorController = new MainSensorController("S1", SensorType.NXT, "S4", SensorType.EV3);
                // EngineController(Bak venstre, bak høyre, forran venstre, forran høyre)
		engine = new EngineController(Motor.D, Motor.A);
		engine.setSpeed(engineSpeed);
		keys = BrickFinder.getLocal().getKeys();
		this.start();
		sensorController.start();
	}
	
	/**
         * Sets the running boolean to true
         */
	public void start(){
		isRunning = true;
		Motor.D.forward();
	}
	
	public void stop(){
        Motor.D.stop();
        isRunning = false;
        System.exit(0);
	}
	
	/**
         * Checks if a button on the Lejos device has been pushed, intended to stop the running loop
         */
	public void checkInterruptButton(){
		if(keys.getButtons() != 0){
			this.stop();
		}
	}
	
	/**
         * The main running loop for the brain
         */
    public void run() throws Exception{
        // Start a thread to check if any terminate buttons are pressed
    	new Thread("InterruptButtonMoitor") {
            @Override
            public void run() {
            	while(isRunning){
            		checkInterruptButton();
            	}
            }
        }.start();
        
        while(isRunning){
            engine.forward(5, true);
            
            this.colorCheck(SensorSide.RIGHT, lastRColor);
            this.colorCheck(SensorSide.LEFT, lastLColor);

            Thread.sleep(50);
        }
    }
	
	/**
         * Reaction for when the color sensor discovers the black tape.
         */
	public void blackTapeRightReaction(){
		engine.leftTurn(turnDegrees);
	}
	public void blackTapeLeftReaction(){
		engine.rightTurn(turnDegrees);
	}
	/**
	 * Checks if the recorded color is black or not
	 */
	public void colorCheck(SensorSide side, float lastColor){
		float currentColor = sensorController.getValue(side);
		if(currentColor < 0.5)
			currentColor = 7;
		if (currentColor != lastColor && currentColor == REACT_COLOR_VALUE) {
            lastColor = currentColor;
            if(side == SensorSide.RIGHT)
            	blackTapeRightReaction();
            else
            	blackTapeLeftReaction();
        }
        else if(currentColor != lastColor){
        	lastColor = currentColor;
        }
	}
}
