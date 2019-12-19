package es.udc.ws.bikes.client.service;

import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.client.service.dto.UserClientBikeDto;
import es.udc.ws.bikes.client.service.dto.UserClientBookDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface UserClientBikeService {
 
    public List<UserClientBikeDto> findBikes(String keywords, Calendar startDate);

    public Long rentBike(Long bikeId, String email, String creditCard) throws InstanceNotFoundException, InputValidationException;
	
    public void rateBook(Long bookId, String email, int rating);
    
    public List<UserClientBookDto> findBooks(String email);
}
