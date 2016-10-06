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
	private float lastRColor = 0;
	private float lastLColor = 0;
	private boolean isRunning = false;
	private boolean inRTurn = false;
	private boolean inLTurn = false;
	private boolean rightLast = false;
	private boolean forceForward = false;
        
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
		engine.setSpeed(engineSpeed, true, true);
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
			System.exit(0);
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

            Thread.sleep(20);
        }
    }
	
	/**
         * Reaction for when the color sensor discovers the black tape.
	 * @throws InterruptedException 
         */
	public void blackTapeRightReaction() throws InterruptedException{
		inRTurn = true;
		if(!inLTurn && rightLast){
			engine.rightTurn();
		}else{
			blackTapeBothReaction(true);
		}
		rightLast = true;
	}
	public void blackTapeLeftReaction() throws InterruptedException{
		inLTurn = true;
		if(!inRTurn && !rightLast){
			engine.leftTurn();
		}else{
			blackTapeBothReaction(false);
		}
		rightLast = false;
	}
	
	public void noTapeRightReaction() throws InterruptedException{
		if(inRTurn)
			engine.stopRightTurn(engineSpeed);
		inRTurn = false;
	}
	public void noTapeLeftReaction() throws InterruptedException{
		if(inLTurn)
			engine.stopLeftTurn(engineSpeed);
		inLTurn = false;
	}
	
	public void blackTapeBothReaction(boolean rightTurn) throws InterruptedException{
		forceForward = true;
		if(rightTurn)
			engine.rightTurn();
		else
			engine.leftTurn();
		engine.forward(5, true);
		Thread.sleep(100);
		engine.forward(5, true);
		engine.stopLeftTurn(engineSpeed);
		engine.stopRightTurn(engineSpeed);
		Thread.sleep(200);
		forceForward = false;
	}
	
	/**
	 * Checks if the recorded color is black or not
	 * @throws InterruptedException 
	 */
	public void colorCheck(SensorSide side, float lastColor) throws InterruptedException{
		if(!forceForward){
			float currentColor = sensorController.getValue(side);
			if(currentColor < 0.5 && side == SensorSide.RIGHT)
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
	        	if(side == SensorSide.RIGHT)
	        		noTapeRightReaction();
	        	else
	        		noTapeLeftReaction();
	        }
		}
	}
}