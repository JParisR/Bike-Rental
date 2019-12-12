package es.udc.ws.bikes.restservice.json;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDaysOfBookException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidNumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidStartDateException;
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
		
    	dataObject.put("bookId", (ex.getBookId()!=null) ? ex.getBookId() : null);
    	if (ex.getInitDate() != null) {
    		SimpleDateFormat dateFormatter1 = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
    		dataObject.put("initDate", dateFormatter1.format(ex.getInitDate().getTime()));
    	} else {
    		dataObject.set("initDate", null);
    	} 
    	
    	if (ex.getEndDate() != null) {
    		SimpleDateFormat dateFormatter2 = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
    		dataObject.put("endDate", dateFormatter2.format(ex.getEndDate().getTime()));
    	} else {
    		dataObject.set("endDate", null);
    	}
    	
    	exceptionObject.set("invalidDaysOfBookException", dataObject);
    	
    	return exceptionObject;
		
	}
	
	//NO SE SI ESTÁ BIEN
	public static JsonNode toInvalidNumberOfBikesException(InvalidNumberOfBikesException ex) {
		
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
		
    	dataObject.put("bikeId", (ex.getBikeId()!=null) ? ex.getBikeId() : null);
		if (ex.getNumberBikes() != 0) {
			dataObject.put("numberBikes", ex.getNumberBikes());
		} else {
			dataObject.set("numberBikes", null);
		}
		
		if (ex.getNumberBikesBook() != 0) {
			dataObject.put("numberBikesBook", ex.getNumberBikesBook());
		} else {
			dataObject.set("numberBikesBook", null);
		}
		
		exceptionObject.set("invalidNumberOfBikesException", dataObject);
		
		return exceptionObject;
	}
	
	public static JsonNode toInvalidStartDateException(InvalidStartDateException ex) {
		
		ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();
    	ObjectNode dataObject = JsonNodeFactory.instance.objectNode();
    	
    	dataObject.put("bikeId", (ex.getBikeId()!=null) ? ex.getBikeId() : null);
    	if (ex.getStartDate() != null) {
    		SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
    		dataObject.put("startDate", dateFormatter.format(ex.getStartDate().getTime()));
    	} else {
    		dataObject.set("startDate", null);
    	}
		
    	exceptionObject.set("invalidStartDateException", dataObject);
    	
    	return exceptionObject;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
