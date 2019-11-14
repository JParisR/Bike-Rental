package es.udc.ws.bikes.model.bikeservice.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class InvalidDaysOfBookException extends Exception{
	
    private Calendar initDate;
    private Calendar endDate;

    public InvalidDaysOfBookException(Calendar initDate, Calendar endDate) {
        super("The difference between=\"" + initDate + 
              "\" and = \"" + 
              endDate + "\")can´t be more than 15.");
        this.initDate = initDate;
        this.endDate = endDate;
    }

    public Calendar getInitDate() {
        return initDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setInitDate(Calendar initDate) {
        this.initDate = initDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }
}
