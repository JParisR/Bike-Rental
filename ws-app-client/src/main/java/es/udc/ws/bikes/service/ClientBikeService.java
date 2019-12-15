package es.udc.ws.bikes.service;

import es.udc.ws.bikes.service.dto.ClientBikeDto;
import es.udc.ws.bikes.service.exceptions.ClientBookExpirationException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;

public interface ClientBikeService {

    public Long addBike(ClientBikeDto bike)
            throws InputValidationException;

    public void updateBike(ClientBikeDto bike)
            throws InputValidationException, InstanceNotFoundException;

    public void removeBike(Long bikeId) throws InstanceNotFoundException;
    
    public List<ClientBikeDto> findBikesById(Long bikeId);

    public List<ClientBikeDto> findBikes(String keywords);

    public Long bookBike(Long bikeId, String email, String creditCard)
            throws InstanceNotFoundException, InputValidationException;

}
