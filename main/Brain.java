package main;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.motor.Motor;
import main.parts.EngineController;
import main.parts.MainSensorController;

public class Brain {
	private Keys keys;
	private MainSensorController sensorController;
	private EngineController engine;
	private boolean isRunning = false;
	private boolean insideTunnel = false;
	private int engineSpeed = 5;
	private float turnDegrees = 25.0f;
	private int sleepTime = 2000;
	private float lastColor = 0;
        
        /**
         * At what sound level should the bot react?
         */
        private static final float REACT_SOUND_LEVEL = 0.55f;
        
        /**
         * The color value of the start/stop line.
         */
        private static final int REACT_COLOR_VALUE = 7;
	
        
	/**
         * Constructor for this class, initializes a bunch of important stuff
         */
	public Brain(){
		sensorController = new MainSensorController("S4", "S2", "S3");
		engine = new EngineController(Motor.A, Motor.B);
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
    public void run(){
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
            
            try {
                if (sensorController.soundLevel() >= REACT_SOUND_LEVEL) {
                    soundReaction();
                }
                
                this.colorCheck();

                if (sensorController.rightTouch()) {
                    hitRightReaction();
                }

                Thread.sleep(50);
            } catch (InterruptedException ex) { }
        }
    }
	
	/**
         * Reaction for when the right touch sensor has been hit.
         * @throws InterruptedException 
         */
	public void hitRightReaction() throws InterruptedException{
		this.stop();
		engine.leftTurn(turnDegrees);
		Thread.sleep(sleepTime);
	}
	
	/**
         * Reaction for when the sound sensor has heard a sound.
         * @throws InterruptedException 
         */
	public void soundReaction() throws InterruptedException{
		engine.stop();
		Thread.sleep(sleepTime);
	}
	
	/**
         * Reaction for when the color sensor discovers the black tape.
         */
	public void blackTapeReaction(){
		if(!insideTunnel){
			insideTunnel = true;
		}else if(isRunning){
			this.engine.backward(20, false);
			this.engine.leftTurn(180);
		}
	}
	
	public void colorCheck(){
		if (sensorController.colorLevel() != lastColor && sensorController.colorLevel() == REACT_COLOR_VALUE) {
            lastColor = sensorController.colorLevel();
            blackTapeReaction();
        }
        else if(sensorController.colorLevel() != lastColor){
        	lastColor = sensorController.colorLevel();
        }
	}
}
