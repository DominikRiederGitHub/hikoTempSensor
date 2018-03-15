package TemperaturSensor.SensorThing;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.types.primitives.BooleanPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;

import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

@ThingworxPropertyDefinitions(properties = {
		@ThingworxPropertyDefinition(name = "Innentemperatur", description = "Die Innentemperatur gemessen vom Sensor",
				baseType = "NUMBER",
				aspects = { "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0",
						"isPersistent:FALSE", "isReadyOnly:TRUE", "isLogged:TRUE", "pushType:ALWAYS",
						"isFolded:FALSE", "defaultValue:0"}),
		
		@ThingworxPropertyDefinition(name = "Auﬂentemperatur", description = "Die Auﬂentemperatur gemessen vom Sensor",
				baseType = "NUMBER",
				aspects = { "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0",
						"isPersistent:FALSE", "isReadyOnly:TRUE", "isLogged:TRUE", "pushType:ALWAYS",
						"isFolded:FALSE", "defaultValue:0"}),
		
		@ThingworxPropertyDefinition(name = "lampeZustand", description = "Beschreibt ob Lampe an oder aus ist",
				baseType = "BOOLEAN",
				aspects = { "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0",
						"isPersistent:FALSE", "isReadyOnly:TRUE", "isLogged:TRUE", "pushType:ALWAYS",
						"isFolded:FALSE", "defaultValue:0"	
				}),
		
		@ThingworxPropertyDefinition(name = "alarmZustand", description = "Alarm Status", 
				baseType = "BOOLEAN",
				aspects = { "dataChangeType:ALWAYS", "dataChangeThreshold:0", "cacheTime:0",
						"isPersistent:FALSE", "isReadyOnly:TRUE", "isLogged:TRUE", "pushType:ALWAYS",
						"isFolded:FALSE", "defaultValue:0"
				})
})

public class TempSensorThing extends VirtualThing{
	
	public static final String INNENTEMP = "Innentemperatur";
	public static final String AUSSENTEMP = "Auﬂentemperatur";
	public static final String LIGHT_STATE = "lampeZustand";
	public static final String ALARM_STATE = "alarmZustand";
	
	public static final String SERVICE_CHANGE_INNER = "changeInnerTemp";
	public static final String SERVICE_CHANGE_INNER_VALUE = "innerValue";
	
	public static final String SERVICE_CHANGE_OUTER = "changeOuterTemp";
	public static final String SERVICE_CHANGE_OUTER_VALUE = "outerValue";
	
	public static final String SERVICE_SWITCH_LIGHT = "switchLight";
	
	public static final String SERVICE_TRIGGER_ALARM = "triggerAlarm";
	public static final String SERVICE_TRIGGER_VALUE = "trigger";
	
	public static final String SENSOR_IP = "192.168.150.102";
	
	
	Thread alarm;
	TempSensorAlarmRunnable alarmRunnable;
	
	ModbusClient modbusClient;
	
	Float innerTemp = 0.0f;
	Float outerTemp = 0.0f;
	Boolean alarmState = false;
	
	public TempSensorThing(String name, String description, ConnectedThingClient client)
			throws Exception{
		
		super(name, description, client);
		this.initializeFromAnnotations();
		modbusClient = new ModbusClient("192.168.150.102",502);
		
		try {
			modbusClient.Connect();
			updateValues();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		alarmRunnable = new TempSensorAlarmRunnable(this);
		alarm = new Thread(alarmRunnable);
	}
	
	@Override
	public void processScanRequest() {
		
		try {
			innerTemp = modbusClient.ConvertRegistersToFloat(modbusClient.ReadHoldingRegisters(12298, 2));

			outerTemp = modbusClient.ConvertRegistersToFloat(modbusClient.ReadHoldingRegisters(12300, 2));
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		updateValues();
	}
	
	public void updateValues() {
		
		try {			
			this.setPropertyValue(INNENTEMP, new NumberPrimitive((double)innerTemp));
			this.setPropertyValue(AUSSENTEMP, new NumberPrimitive((double)outerTemp));
			this.setPropertyValue(LIGHT_STATE, new BooleanPrimitive(getLight()));
			this.setPropertyValue(ALARM_STATE, new BooleanPrimitive(alarmState));
			
			this.updateSubscribedProperties(10000);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	

	@ThingworxServiceDefinition(name = SERVICE_CHANGE_INNER, description = "Changes the inner temperature")
	public void changeInnerTemp(
			@ThingworxServiceParameter(name = SERVICE_CHANGE_INNER_VALUE,
			description = "changes inner temp to this value",
			baseType = "NUMBER") Float setInner)
		throws Exception{
		
		//innerTemp = setInner;
		updateValues();
	}
	

	@ThingworxServiceDefinition(name = SERVICE_CHANGE_OUTER, description = "Changes the outer temperature")
	public void changeOuterTemp(
			@ThingworxServiceParameter(name = SERVICE_CHANGE_OUTER_VALUE,
			description = "changes outer temp to this value",
			baseType = "NUMBER") Float setOuter)
		throws Exception{
		
		//outerTemp = setOuter;
		updateValues();
	}
	
	
	//==================================  LICHT METHODEN  ====================================================
	
	public void setLight(Boolean state) throws UnknownHostException, SocketException, ModbusException, IOException {
		modbusClient.WriteSingleCoil(12608, state);
	}
	
	public Boolean getLight() throws UnknownHostException, SocketException, ModbusException, IOException {
		return modbusClient.ReadCoils(12608, 1)[0];
	}
	
	@ThingworxServiceDefinition(name = SERVICE_TRIGGER_ALARM, description = "triggers the alarm")
	@ThingworxServiceResult(name = "result", baseType = "BOOLEAN")
	public void triggerAlarm(
			@ThingworxServiceParameter(name = SERVICE_TRIGGER_VALUE,
			description = "if true, trigger alarm",
			baseType = "BOOLEAN") Boolean trigger)
		
		throws Exception{
		
		if(trigger) {
			if(!alarm.isAlive()) {
				alarm = new Thread(alarmRunnable);
				alarm.start();
				alarmState = true;
			}
		}else {
			alarmRunnable.cancel();
			alarmState = false;
			updateValues();
		}

		/*
		if(trigger && !alarm.isAlive()) {
			alarm = new Thread(alarmRunnable);
			alarm.start();
			alarmState = true;
		}
		
		if(!trigger && alarm.isAlive()){
				alarm.stop();
				setLight(false);
				alarmState = false;
				updateValues();
		}
		*/
	}
	
	@ThingworxServiceDefinition(name = SERVICE_SWITCH_LIGHT, description = "Switches Light" )
	@ThingworxServiceResult(name = "result", baseType = "BOOLEAN")
	public Boolean switchLight() throws Exception{
		setLight(!getLight());
		updateValues();
		return getLight();
	}


}
