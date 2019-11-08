package es.udc.ws.bikes.model.bikesservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikesservice.exceptions.*;
import es.udc.ws.bikes.model.book.Book;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface BikeService {
	
    public Bike addBike(Bike bike) throws InputValidationException;

    public void updateBike(Bike bike) throws InputValidationException,
    		InstanceNotFoundException, InvalidStartDateException;
    
    public Bike findBike(Long bikeId) throws InstanceNotFoundException;
    
    public void removeBike(Long bikeId) throws InstanceNotFoundException;
	
    public List<Bike> findBikesByKeywords(String keywords);
    
  //  public List<Bike> findBikesAvailable(Calendar StartDate);

    public Book bookBike(Long bookId, Long bikeId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes)
            throws InvalidStartDateException, InstanceNotFoundException, InputValidationException, InvalidNumberOfBikesException, InvalidDaysOfBookException;

    public Book findBook(Long bookId) throws InstanceNotFoundException;
    
    public List<Book> findBookByUser(String email) throws InstanceNotFoundException;
}
