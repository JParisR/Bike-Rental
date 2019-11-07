package es.udc.ws.bikes.model.bikesservice;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikesservice.exceptions.InvalidNumberOfBikesException;
import es.udc.ws.bikes.model.bikesservice.exceptions.InvalidStartDateException;
import es.udc.ws.bikes.model.book.Book;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface BikeService {
	
    public Bike addBike(Bike bike) throws InputValidationException;

    public void updateBike(Bike bike) throws InputValidationException,
    		InstanceNotFoundException, InvalidStartDateException;
    
    public void removeBike(Long bikeId) throws InstanceNotFoundException;

	public Bike findBike(Long bikeId) throws InstanceNotFoundException;
	
    public List<Bike> findMovies(String keywords);

    public Book bookBike(Long bookId, Long bikeId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes)
            throws InstanceNotFoundException, InputValidationException, InvalidNumberOfBikesException;

    public Book findBook(Long bookId) throws InstanceNotFoundException;
}
