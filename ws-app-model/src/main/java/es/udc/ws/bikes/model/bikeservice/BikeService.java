package es.udc.ws.bikes.model.bikeservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikeservice.exceptions.*;
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
    
    public List<Bike> findBikesByKeywords(String keywords, Calendar startDate);

    public void rateBook(Long bookId, int rate) throws InstanceNotFoundException,
    		InvalidStartDateException;
    
    public Book bookBike(Long bikeId, String email, String creditCard, Calendar initDate, Calendar endDate, 
    		int numberBikes, Calendar bookDate) throws InvalidStartDateException, InstanceNotFoundException, 
    		InputValidationException, InvalidNumberOfBikesException, InvalidDaysOfBookException;

    public Book findBook(Long bookId) throws InstanceNotFoundException;
    
    public List<Book> findBookByUser(String email) throws InstanceNotFoundException;
}
