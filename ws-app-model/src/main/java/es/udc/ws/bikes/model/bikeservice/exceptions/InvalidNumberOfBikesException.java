package es.udc.ws.bikes.model.bikeservice.exceptions;

@SuppressWarnings("serial")
public class InvalidNumberOfBikesException extends Exception{
	
	private Long bikeId;
	private int numberBikes;
  	private int numberBikesBook;

    public InvalidNumberOfBikesException(Long bikeId, int numberBikes, int numberBikesBook) {
        super("Bike with id=" + bikeId + 
        		"with " + numberBikes + "units of bike" + 
              "\" can´t be updated to = \"" + 
              numberBikesBook + "units of bike\")");
        this.bikeId = bikeId;
        this.numberBikes = numberBikes;
        this.numberBikesBook = numberBikesBook;
    }
    
    public Long getBikeId() {
        return bikeId;
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
    
    public void setBikeId(Long bikeId) {
    	this.bikeId = bikeId;
    }
}
