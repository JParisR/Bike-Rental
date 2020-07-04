package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidStartDateToBookException extends Exception {

	public InvalidStartDateToBookException() {
		super("The startDate of the bike can not be previous to the startDate of the bike.");
	}
	
}
