package es.udc.ws.bikes.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDaysOfBookException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonServiceExceptionConversor {

	public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";
	
	public static JsonNode toInputValidationException(InputValidationException ex) {
		
    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
    	dataObject.put("message", ex.getMessage());
    	
    	exceptionObject.set("inputValidationException", dataObject);

        return exceptionObject;
    }
	
	public static JsonNode toInstanceNotFoundException(InstanceNotFoundException ex) {

    	ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
    	dataObject.put("instanceId", (ex.getInstanceId()!=null) ? ex.getInstanceId().toString() : null);
    	dataObject.put("instanceType", ex.getInstanceType());
    	
    	exceptionObject.set("instanceNotFoundException", dataObject);

        return exceptionObject;
    }
	
	public static JsonNode toInvalidDaysOfBookException(InvalidDaysOfBookException ex) {
		
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
    	dataObject.put("bookId", (ex.get)))
		
	}
	
}
