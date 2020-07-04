package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidBookDatesException extends Exception {

	public InvalidBookDatesException() {
		super("The endDate can not be previous to the startDate.");
	}
	
}
