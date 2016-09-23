package main.parts.sensors;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class ColorSensorEV3 implements Sensor {
    private EV3ColorSensor colorSensor;
    public boolean running = true;
    private SampleProvider colorProvider;
    private float[] colorSample;

    public ColorSensorEV3(String portName) {
        Port port = LocalEV3.get().getPort(portName);
        colorSensor = new EV3ColorSensor(port);
        colorProvider = colorSensor.getColorIDMode();
        colorSample = new float[colorProvider.sampleSize()];
    }

    public void update() {
    	colorProvider.fetchSample(colorSample, 0);
    }

    public float getValue() {
        return colorSample[0];
    }
}