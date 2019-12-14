package es.udc.ws.bikes.client.service;

import java.util.List;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.bikes.client.service.dto.*;

public interface ClientBikeService {

    public List<ClientBikeDto> findBikes(String keywords);
    
    public List<ClientBookDto> findBooks(String userId);

    public Long bookBike(Long BikeId, String userId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException;

    public Long rateBook(Long BookId, String userId)
            throws InstanceNotFoundException, InputValidationException;
}
