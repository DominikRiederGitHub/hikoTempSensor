package Gateway;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.ConnectionException;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.StringPrimitive;



public abstract class SimpleTWxClient extends ConnectedThingClient {
	public static final String CONNCECTION_SUCCSESSFULL = "The client is now connected.";
	public static final String CONNCECTION_TIMEOUT = "The client could not connect to the server within timeout time.";
	public static final String CONNECTION_FAILED = "Some error occurred during connection process.";
	
	public static final String SERVICE_FAILED = "Some error occured while invoking Service";
	public static final String SERVICE_TIMEOUT = "A timeout Error occurred while invoking Service";
	public static final String SERVICE_CONNECTION_FAILED = "A connection Error occured while invoking Service";
	
	public static final String RUN_NOT_CONNECTED = "Could not run the process because client is not connected.";
	
	public static final String SCAN_FAILED = "Some error occurred while processing scan request of a thing.";
	
	public static final int DEFAULT_CONNECTION_TIMEOUT = 30000;
	
	public static final Logger LOG = LoggerFactory.getLogger(SimpleTWxClient.class);
	
	protected ArrayList<VirtualThing> registeredThings;
	
	public SimpleTWxClient(ClientConfigurator config) throws Exception {
		super(config);
		registeredThings = new ArrayList<VirtualThing>();
	}

	/**
	 * Connects to the Server with the credentials provided by the ClientConfigurator of this class.
	 * Throws exception if Connection fails to be established
	 * @param timeout The time waited for a Connection to establish
	 */
	public void connect(int timeout) {
		try {
			this.start();
			if(this.waitForConnection(timeout)) {
				LOG.info(CONNCECTION_SUCCSESSFULL);
			}else {
				LOG.info(CONNCECTION_TIMEOUT);
			}
		} catch (Exception e) {
			LOG.warn(CONNECTION_FAILED, e);
		}
		
	}
	
	/**
	 * Connects to the Server with the credentials provided by the ClientConfigurator of this class.
	 * Uses the default timeout DEFAULT_CONNECTION_TIMEOUT = 30000 (30 seconds)
	 * Throws exception if Connection fails to be established
	 */
	public void connect() {
		connect(DEFAULT_CONNECTION_TIMEOUT);
	}
	
	
	/**
	 * runs the specified cycling process if the client is connected.
	 * Calls runProcessingCycle if client is connected.
	 */
	public void run() {
		if(this.isConnected()) {
			runProcessingCycle();
		}else {
			LOG.warn(RUN_NOT_CONNECTED);
		}
	}
	
	
	/**
	 * This method defines the cyclic behavior of this gateway.
	 * It is responsible for sending/receiving data to/from the server.
	 */
	public abstract void runProcessingCycle();
	
	/**
	 * Iterates over all binded VirtualThings and calls their processScanRequest(
	 */
	public void processAllScanRequests() {
		try {
			for(VirtualThing vt : this.getThings().values()) {
				vt.processScanRequest();
			}
		}catch(Exception e){
			LOG.warn(SCAN_FAILED, e);
		}
	}
	
}
