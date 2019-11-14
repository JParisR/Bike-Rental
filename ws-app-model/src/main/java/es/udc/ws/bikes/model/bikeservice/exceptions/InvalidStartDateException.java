package es.udc.ws.bikes.model.bikeservice.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidStartDateException extends Exception{

    private Long bikeId;
    private Calendar startDate;

    public InvalidStartDateException(Long bikeId, Calendar startDate) {
        super("Bike with id=\"" + bikeId + 
              "\" can´t be updated to = \"" + 
              startDate + "\")");
        this.bikeId = bikeId;
        this.startDate = startDate;
    }

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
    }
}
