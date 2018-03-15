package TemperaturSensor.SensorObserver;

import com.thingworx.communications.client.ClientConfigurator;

import Observer.ObserverThing;
import TemperaturSensor.SensorThing.TempSensorGatewayClient;
import TemperaturSensor.SensorThing.TempSensorThing;

public class TempSensorObserverApp {

	static String hikoServer = "ws://localhost:80/Thingworx/WS";
	static String hikoAppKey = "6ddf707e-08c3-49bb-8c8f-91e28b4f1e5d";
	
	static String studioServer = "wss://dqnyqm72.studio-trial.thingworx.io:8443/Thingworx/WS";
	static String studioAppKey = "dfb8743d-4526-4774-bcb4-2232a1513144";
	
	public static void main(String[] args) throws Exception {
		
		TempSensorGatewayClient client = new TempSensorGatewayClient(makeConfigurator());
		
		client.connect();
		
		ObserverThing observer =  new ObserverThing("TempSensorObserver", "Ein Observer für den Temperatursensor", client);
		TempSensorObserverUI obsListener = new TempSensorObserverUI();
		observer.addObserverListener(obsListener);
		obsListener.setVisible(true);
		
		client.bindThing(observer);
		client.run();
		
	}
	
	public static ClientConfigurator makeConfigurator() {
		ClientConfigurator config = new ClientConfigurator();
		config.setUri(studioServer);
		config.setAppKey(studioAppKey);
		config.ignoreSSLErrors(true);
		return config;
	}
}
