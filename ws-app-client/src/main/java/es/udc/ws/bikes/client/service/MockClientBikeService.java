package es.udc.ws.bikes.client.service;

import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;
import java.util.ArrayList;
import java.util.List;

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
    	
    	/*List<AdminClientBikeDto> bikes = new ArrayList<>();

        bikes.add(new AdminClientBikeDto());
        bikes.add(new AdminClientBikeDto());*/
    	
    	AdminClientBikeDto bikeDto = new AdminClientBikeDto();
        return bikeDto;
        
    }

}
