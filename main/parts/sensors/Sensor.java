package main.parts.sensors;

public interface Sensor {
	/**
	 * Return the float value of the sensor
	 * @return
	 */
	public float getValue();
	
	/**
	 * Update the sensor values
	 */
	public void update();
}
