package main.parts.sensors;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.NXTSoundSensor;
import lejos.robotics.SampleProvider;

public class SoundSensor implements Sensor {
	private NXTSoundSensor sound;
	public boolean running = true;
	private float[] samples;
	private SampleProvider sampler;
	
	public SoundSensor(String tp){
		Brick brick = BrickFinder.getDefault();
		Port gyroPort = brick.getPort(tp);
		sound = new NXTSoundSensor(gyroPort);
		sound.setCurrentMode(1);
		sampler = sound.getDBMode();
		samples = new float[sampler.sampleSize()];
	}
	
	public void update(){
		sampler.fetchSample(samples, 0);
	}
	
	private float fetchValue(int index){
		return samples[index];
	}

	@Override
	public float getValue() {
		return this.fetchValue(0);
	}
}
