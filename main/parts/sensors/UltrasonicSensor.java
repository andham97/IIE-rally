package main.parts.sensors;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3UltrasonicSensor;

public class UltrasonicSensor implements Sensor {
	/**
	 * Ultrasonic sensor object
	 */
	private EV3UltrasonicSensor uSensor;
	
	/**
	 * Array for storing sensor values
	 */
	private float[] sampleArray;

	/**
	 * Init new ultrasonic sensor
	 * @param usp string value of the port
	 */
	public UltrasonicSensor(String usp){
		Brick brick = BrickFinder.getDefault();
		Port usPort = brick.getPort(usp);
		uSensor = new EV3UltrasonicSensor(usPort);
		uSensor.enable();
		sampleArray = new float[uSensor.getDistanceMode().sampleSize()];
		uSensor.setCurrentMode(0);
	}

	/**
	 * Return the value of the sensor
	 * @return
	 */
	@Override
	public float getValue() {
		return sampleArray[0];
	}

	/**
	 * Update the sensor values
	 */
	@Override
	public void update() {
		if(uSensor.isEnabled()){
			uSensor.fetchSample(sampleArray, 0);
			sampleArray[0] *= 100;
		}
		else
			sampleArray[0] = Float.POSITIVE_INFINITY;
	}
}