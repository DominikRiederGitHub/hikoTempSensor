package TemperaturSensor.SensorThing;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import de.re.easymodbus.exceptions.ModbusException;

public class TempSensorAlarmRunnable implements Runnable{

	Boolean canceled;
	TempSensorThing sensor;
	public TempSensorAlarmRunnable(TempSensorThing sensor) {
		this.sensor = sensor;
	}
	
	@Override
	public synchronized void run() {
		canceled = false;
		while(!canceled) {
			try {
				Thread.sleep(100);
				sensor.switchLight();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		try {
			sensor.setLight(false);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ModbusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cancel() {
		canceled = true;
	}
	
	public boolean getCanceled() {
		return canceled;
	}

}
