package main.parts.sensors;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTLightSensor;
import lejos.robotics.SampleProvider;

public class ColorSensorNXT implements Sensor {
	private NXTLightSensor colorSensor;
    public boolean running = true;
    private SampleProvider colorProvider;
    private float[] colorSample;

    public ColorSensorNXT(String portName) {
        Port port = LocalEV3.get().getPort(portName);
        colorSensor = new NXTLightSensor(port);
        colorProvider = colorSensor.getAmbientMode();
        colorSample = new float[colorProvider.sampleSize()];
    }

    public void update() {
    	colorProvider.fetchSample(colorSample, 0);
    }

    public float getValue() {
    	return colorSample[0];
    }
}
