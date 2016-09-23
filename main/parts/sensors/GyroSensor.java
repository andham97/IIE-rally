package main.parts.sensors;

import lejos.hardware.BrickFinder;
import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.GyroscopeAdapter;
import lejos.robotics.SampleProvider;

public class GyroSensor implements Sensor {
    
    private final EV3GyroSensor sensor;
    private final SampleProvider sampleProvider;
    private final float[] samples;
    private long lastReadTime;
    private double angle;
    private double drift;
    private double offset;
    private GyroscopeAdapter adapter;

    /**
     * Creates a GyroSensor object using EV3GyroSensor at port
     * <code>portName</code>.
     * @param portName 
     */
    public GyroSensor(String portName) {
        Port gyroPort = BrickFinder.getDefault().getPort(portName);
        sensor = new EV3GyroSensor(gyroPort);
        sampleProvider = sensor.getRateMode();
        samples = new float[sampleProvider.sampleSize()];
        sensor.reset();
        angle = 0;
        drift = 0;
        offset = 0;
        
        // For testing purposes
        adapter = new GyroscopeAdapter(sampleProvider, 100);
        adapter.reset();
        System.out.print("Calibrating GyroscopeAdapter... ");
        adapter.recalibrateOffset();
        System.out.println("Done");
    }

    /**
     * Reads a value from the sensor and calculates angle. This should be called
     * in MainSensorController's loop to get accurate calculations.
     */
    public void update() {
        sampleProvider.fetchSample(samples, 0);
        
        long timeNow = System.currentTimeMillis();
        double dt = (double) (timeNow - lastReadTime) / 1000.0;
        lastReadTime = timeNow;
        
        angle += ((double) samples[0] - offset) * dt - drift * dt;
    }
    
    /**
     * Recalibrates the readings from gyro by calculating drift.
     * The gyro must be completely still while calibrating.
     */
    public void recalibrate() {
        float[] smp = new float[sampleProvider.sampleSize()];
        
        long startTime = System.currentTimeMillis();
        sampleProvider.fetchSample(smp, 0);
        float startValue = smp[0];
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) { }
        
        sampleProvider.fetchSample(smp, 0);
        double dt = (double) (System.currentTimeMillis() - startTime) / 1000.0;
        drift = (double) (smp[0] - startValue) / dt;
    }

    /**
     * Returns the current calculated absolute angle.
     * @return 
     */
    public double getAngle() {
        return angle;
    }
    
    /**
     * Returns the current angle, calculated by a GyroscopeAdapter.
     * @return 
     */
    public int getAdapterAngle() {
        return adapter.getAngle();
    }
    
    /**
     * Returns the GyroscopeAdapter object. (For testing purposes)
     * @return 
     */
    public GyroscopeAdapter getAdapter() {
        return adapter;
    }
    
    /**
     * Resets the EV3GyroSensor and sets angle to 0.
     */
    public void reset() {
        sensor.reset();
        angle = 0;
        adapter.reset();
    }

    /**
     * TODO: Must be implemented into this class
     */
	@Override
	public float getValue() {
		return (float) angle;
	}
}