package es.udc.ws.bikes.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.ServiceBikeDto;

public class JsonServiceBikeDtoConversor {
	
	public static ObjectNode toObjectNode(ServiceBikeDto bike) {
		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();
		
		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}
		bikeObject.put("description", bike.getDescription()).
			put("price", bike.getPrice()).
			put("units", bike.getUnits());
		
		return bikeObject;
	}
	
}
