package es.udc.ws.bikes.adminClient.service;

import java.util.List;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.bikes.adminClient.service.dto.*;

public interface AdminClientBikeService {

    public Long addBike(AdminClientBikeDto Bike)
            throws InputValidationException;

    public void updateBike(AdminClientBikeDto Bike)
            throws InputValidationException, InstanceNotFoundException;

    public List<AdminClientBikeDto> findBikes(String bikeId);

}
