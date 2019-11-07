package es.udc.ws.bikes.model.bikesservice.exceptions;


@SuppressWarnings("serial")
public class InvalidNumberOfBikesException extends Exception{

  //  private Long bikeId;
  //  private Calendar startDate;

    public InvalidNumberOfBikesException(int numberBikes, int numberBikesBook) {
        super("Bike with id=\"" + numberBikes + 
              "\" can´t be updated to = \"" + 
              numberBikesBook + "\")");
       // this.bikeId = bikeId;
       // this.startDate = startDate;
    }
/*
    public Long getBikeId() {
        return bikeId;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }*/
}
