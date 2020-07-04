package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class BookAlreadyRatedException extends Exception {

	public BookAlreadyRatedException() {
		super("Book already rated. You can not rate a book several times.");
	}
	
}
