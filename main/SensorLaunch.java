package main;

import main.parts.sensors.ColorSensor;
import main.parts.sensors.SoundSensor;

public class SensorLaunch {
	public static void main(String[] args) throws InterruptedException{
		SoundSensor s = new SoundSensor("S1");
		ColorSensor c = new ColorSensor("S2");
		System.out.println("start");
		for(int i = 0; i < 1000; i++){
			s.update();
			c.update();
		}
		System.out.println(s.getValue());
		System.out.println(c.getValue());
		System.out.println("");
		System.out.println("Ny verdi");
		System.out.println("");
		Thread.sleep(2000);
		for(int i = 0; i < 1000; i++){
			s.update();
			c.update();
		}
		System.out.println(s.getValue());
		System.out.println(c.getValue());
	}
}
