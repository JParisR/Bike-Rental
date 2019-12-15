package es.udc.ws.bikes.service;

import es.udc.ws.bikes.service.dto.ClientBikeDto;
import java.util.ArrayList;
import java.util.List;

public class MockClientBikeService implements ClientBikeService {

    @Override
    public Long addBike(ClientBikeDto bike) {
        // TODO Auto-generated method stub
        return (long) 0;
    }

    @Override
    public void updateBike(ClientBikeDto bike) {

    }

    @Override
    public void removeBike(Long bikeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<ClientBikeDto> findBikes(String keywords) {

        List<ClientBikeDto> bikes = new ArrayList<>();

        bikes.add(new ClientBikeDto());
        bikes.add(new ClientBikeDto());

        return bikes;

    }
    
    @Override
    public List<ClientBikeDto> findBikesById(Long bikeId) {
    	
    	List<ClientBikeDto> bikes = new ArrayList<>();

        bikes.add(new ClientBikeDto());
        bikes.add(new ClientBikeDto());

        return bikes;
        
    }

    @Override
    public Long bookBike(Long bikeId, String email, String creditCardNumber) {
        // TODO Auto-generated method stub
        return null;
    }

}
