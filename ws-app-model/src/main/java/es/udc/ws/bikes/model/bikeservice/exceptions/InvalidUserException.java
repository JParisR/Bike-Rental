package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidUserException extends Exception{

	private String email;
	
	public InvalidUserException(String email) {
		super("The user=\"" + email + "\" is invalid.");
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
