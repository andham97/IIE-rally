package main.parts;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import main.parts.sensors.ColorSensorEV3;
import main.parts.sensors.ColorSensorNXT;
import main.parts.sensors.Sensor;

public class MainSensorController implements Runnable {
	
	/**
	 * Enum with the different models of sensors
	 * @author andham97
	 */
	public enum SensorType {
		NXT,
		EV3
	}
	
	/**
	 * Enum with the different sides the sensors can be configured at, will 
	 * @author andham97
	 */
	public enum SensorSide {
		LEFT,
		RIGHT
	}
	
	/**
	 * HashMap containing the different sensors mapped to the sensorside enum
	 */
	private HashMap<SensorSide, Sensor> sensors = new HashMap<SensorSide, Sensor>();
    
    /**
     * Boolean for looping in run method
     */
    private boolean running;
    
    /**
     * Thread object to hold the current thread running in this class
     */
    private Thread thread;
    
    /**
     * Init the sensors and prepare the object for running
     * @param leftColorSensorPort
     * @param leftColorSensorType enum SensorType in this class
     * @param rightColorSensorPort
     * @param rightColorSensorType enum SensorType in this class
     */
    public MainSensorController(String leftColorSensorPort,
    		SensorType leftColorSensorType,
    		String rightColorSensorPort,
    		SensorType rightColorSensorType) {
    	sensors.put(SensorSide.LEFT,
    			leftColorSensorType == SensorType.EV3 ?
    					new ColorSensorEV3(leftColorSensorPort) :
    						new ColorSensorNXT(leftColorSensorPort));
    	sensors.put(SensorSide.RIGHT,
    			rightColorSensorType == SensorType.EV3 ?
    					new ColorSensorEV3(rightColorSensorPort) :
    						new ColorSensorNXT(rightColorSensorPort));
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
    		Iterator<Entry<SensorSide, Sensor>> i = this.sensors.entrySet().iterator();
        	while(i.hasNext()){
    			Entry<SensorSide, Sensor> pair = i.next();
    			pair.getValue().update();
    			i.remove();
    		}
    	}
    }
    
    /**
     * Get the color value of the specified sensor at the side
     * @param side enum SensorSide in this class
     * @return
     */
    public float getValue(SensorSide side){
    	return this.sensors.get(side).getValue();
    }
}