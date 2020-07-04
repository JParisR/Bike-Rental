package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidStartDateException extends Exception{

	public InvalidStartDateException() {
		super("The startDate can not be previous to the actual date.");
	}
	
}
