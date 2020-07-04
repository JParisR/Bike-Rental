package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class BookNotFinishedException extends Exception {

	public BookNotFinishedException() {
		super("Book not finished yet. You should wait until the date"
				+ " of end to rate a book.");
	}
	
}
