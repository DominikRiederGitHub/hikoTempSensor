package TemperaturSensor;

import com.thingworx.communications.client.ClientConfigurator;

import TemperaturSensor.SensorThing.TempSensorGatewayClient;
import TemperaturSensor.SensorThing.TempSensorThing;

public class TempSensorApplication {
	
	static String hikoServer = "ws://192:80/Thingworx/WS";
	static String hikoAppKey = "6ddf707e-08c3-49bb-8c8f-91e28b4f1e5d";
	
	static String studioServer = "wss://dqnyqm72.studio-trial.thingworx.io:8443/Thingworx/WS";
	static String studioAppKey = "dfb8743d-4526-4774-bcb4-2232a1513144";
	
	static String nürnbergServer = "ws://10.1.1.50:80/Thingworx/WS";
	static String nürnbergAppKey = "88826d4b-2023-4081-b6cd-1b513bd1577c";
	
	public static void main(String[] args) throws Exception {
		
		TempSensorGatewayClient client = new TempSensorGatewayClient(makeConfigurator());
		
		client.connect();
		
		TempSensorThing sensor =  new TempSensorThing("hikoTempSensor", "ein Temperatur Sensor", client);
		
		client.bindThing(sensor);
		
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
