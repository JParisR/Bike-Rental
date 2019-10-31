package es.udc.ws.bikes.model.bikesservice;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface BikeService {
	
    public Bike addBike(Bike bike) throws InputValidationException;

    public void updateBike(Bike bike) throws InputValidationException,
    		InstanceNotFoundException;
    
   // public void removeBike(Long bikeId) throws InstanceNotFoundException;

}
