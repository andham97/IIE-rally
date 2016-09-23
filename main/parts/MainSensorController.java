package main.parts;

import main.parts.sensors.ColorSensor;
import main.parts.sensors.SoundSensor;
import main.parts.sensors.TouchSensor;

public class MainSensorController implements Runnable {
	/**
	 * Right touch sensor object
	 */
	private TouchSensor rightTouch;
	
	/**
	 * Sound sensor object
	 */
    private SoundSensor sound;
    
    /**
     * Color sensor object
     */
    private ColorSensor color;
    
    /**
     * Boolean for looping in run method
     */
    private boolean running;
    
    /**
     * Thread object to hold the current thread running in this class
     */
    private Thread thread;
    
    /**
     * Main constructor taking in the different ports
     * @param lt Left touch port string
     * @param rt Right touch port string
     * @param ss Sound sensor port string
     * @param lit Light/Color sensor port string
     */
    public MainSensorController(String rt, String ss, String lit) {
    	rightTouch = new TouchSensor(rt);
    	sound = new SoundSensor(ss);
    	color = new ColorSensor(lit);
    	this.running = false;
    }
    
    /**
     * Start the thread
     */
    public void start(){
    	this.running = true;
    	this.thread = new Thread(this);
    	this.thread.start();
    }
    
    /**
     * Stop the thread from running and necesary cleanup
     */
    public void stop(){
    	this.running = false;
    	try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Main run loop
     * Initiated by the Thread.start function in this.start()
     */
    public void run(){
    	while(this.running){
    		rightTouch.update();
    		sound.update();
    		color.update();
    	}
    }
    
    /**
     * Return if the right touch sensor is pressed
     * @return
     */
    public boolean rightTouch(){
    	return this.rightTouch.getValue() == 1;
    }
    
    /**
     * Return the sound level of the sound sensor
     * @return
     */
    public float soundLevel(){
    	return this.sound.getValue();
    }
    
    /**
     * Return the color level
     * @return
     */
    public float colorLevel(){
    	return this.color.getValue();
    }
}