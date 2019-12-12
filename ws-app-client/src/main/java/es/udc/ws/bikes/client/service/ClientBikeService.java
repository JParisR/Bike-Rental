package es.udc.ws.bikes.client.service;

import java.util.List;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.bikes.client.service.dto.ClientBikeDto;

public interface ClientBikeService {

    public Long addBike(ClientBikeDto Bike)
            throws InputValidationException;

    public void updateBike(ClientBikeDto Bike)
            throws InputValidationException, InstanceNotFoundException;

    public List<ClientBikeDto> findBikes(String keywords);

    public Long bookBike(Long BikeId, String userId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException;

}
