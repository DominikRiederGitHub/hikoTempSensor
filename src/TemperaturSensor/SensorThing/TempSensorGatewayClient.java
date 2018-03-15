package TemperaturSensor.SensorThing;

import com.thingworx.communications.client.ClientConfigurator;

import Gateway.SimpleTWxClient;

public class TempSensorGatewayClient extends SimpleTWxClient {
	public static final String CYCLE_FAILED = "Some Error occurred during cyclic scan request.";

	public TempSensorGatewayClient(ClientConfigurator config) throws Exception {
		super(config);
	}

	@Override
	public void runProcessingCycle() {
		try {
			while(!isShutdown()) {
				Thread.sleep(3000);
				processAllScanRequests();
			}
		} catch (InterruptedException e) {
			LOG.warn(CYCLE_FAILED, e);
		}
		
	}
	
	

}
