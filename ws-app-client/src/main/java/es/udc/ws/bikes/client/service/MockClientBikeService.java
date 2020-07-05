package es.udc.ws.bikes.client.service;

import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;

public class MockClientBikeService implements AdminClientBikeService {

    @Override
    public Long addBike(AdminClientBikeDto bike) {
        // TODO Auto-generated method stub
        return (long) 0;
    }

    @Override
    public void updateBike(AdminClientBikeDto bike) {

    }
    
    @Override
    public AdminClientBikeDto findBikesById(Long bikeId) {
    	
    	AdminClientBikeDto bikeDto = new AdminClientBikeDto();
        return bikeDto;
        
    }

}
