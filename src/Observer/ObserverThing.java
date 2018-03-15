package Observer;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;

public class ObserverThing extends VirtualThing{
	public static final String LOG_SERVICE = "logMessage";
	public static final String LOG_SERVICE_MESSAGE = "message";
	
	private ArrayList<ObserverListener> obsListeners = new ArrayList<ObserverListener>();
	
	public ObserverThing(String name, String description, ConnectedThingClient client)
    		throws Exception{
		super(name, description, client);
    	this.initializeFromAnnotations();
	}
	
	@ThingworxServiceDefinition(name = LOG_SERVICE, description = "displays message in Log of Observer")
    /**
     * Displays the given message in the applications Log Field
     * @param message
     * @throws Exception
     */
    public void logMessage(
    		@ThingworxServiceParameter(name = LOG_SERVICE_MESSAGE,
			description = "message contains info displayd in the log",
			baseType = "STRING") String message)
    		throws Exception{
		updateListenersLog(message);
	}
	
	/**
	 * updates ObserverListeners with the given message
	 * @param update
	 */
	public void updateListenersLog(String update) {
		for(ObserverListener obsLis: obsListeners){
			obsLis.updateLog(update);
		}
	}
	
	/**
	 * adds an ObserverListener to the Listeners
	 * @param listener
	 */
	public void addObserverListener(ObserverListener listener) {
		obsListeners.add(listener);
	}
	
}
