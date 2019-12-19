package es.udc.ws.bikes.client.service;

import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.util.List;

public interface AdminClientBikeService {

    public Long addBike(AdminClientBikeDto bike)
            throws InputValidationException;

    public void updateBike(AdminClientBikeDto bike)
            throws InputValidationException, InstanceNotFoundException;
    
    public List<AdminClientBikeDto> findBikesById(Long bikeId);

}
