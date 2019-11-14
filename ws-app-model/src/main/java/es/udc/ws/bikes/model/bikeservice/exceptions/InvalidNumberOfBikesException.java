package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidNumberOfBikesException extends Exception{

	private int numberBikes;
  	private int numberBikesBook;

    public InvalidNumberOfBikesException(int numberBikes, int numberBikesBook) {
        super("Bike with id=\"" + numberBikes + 
              "\" can´t be updated to = \"" + 
              numberBikesBook + "\")");
        this.numberBikes = numberBikes;
        this.numberBikesBook = numberBikesBook;
    }

    public int getNumberBikes() {
        return numberBikes;
    }

    public int getNumberBikesBook() {
        return numberBikesBook;
    }

    public void setNumberBikes(int numberBikes) {
        this.numberBikes = numberBikes;
    }

    public void setNumberBikesBook(int numberBikesBook) {
        this.numberBikesBook = numberBikesBook;
    }
}
