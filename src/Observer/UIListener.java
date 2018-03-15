package Observer;

import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;

public interface UIListener {

	public InfoTable triggerService(String thingName, String serviceName, 
			ValueCollection parameters);

}