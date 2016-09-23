package main.parts.sensors;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3TouchSensor;

public class TouchSensor implements Sensor {
	/**
	 * Touch sensor object
	 */
	private EV3TouchSensor touch;
	
	/**
	 * Sample array
	 */
	private float[] samples;
	
	/**
	 * Init new touch sensor
	 * @param tp port for the sensor
	 */
	public TouchSensor(String tp){
		Brick brick = BrickFinder.getDefault();
		Port gyroPort = brick.getPort(tp);
		touch = new EV3TouchSensor(gyroPort);
		samples = new float[touch.sampleSize()];
	}
	
	/**
	 * Update the sensor value
	 */
	public void update(){
		touch.fetchSample(samples, 0);
	}
	
	/**
	 * Internal: fetch the value at the array index
	 * @param index of the value
	 * @return
	 */
	private float fetchValue(int index){
		return samples[index];
	}
	
	/**
	 * Return the value of the sensor
	 * @return
	 */
	@Override
	public float getValue() {
		return this.fetchValue(0);
	}
}
