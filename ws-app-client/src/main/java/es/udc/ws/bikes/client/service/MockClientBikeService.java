package es.udc.ws.bikes.client.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.client.service.dto.ClientBikeDto;

public class MockClientBikeService implements ClientBikeService{

    @Override
    public Long addBike(ClientBikeDto Bike) {
        // TODO Auto-generated method stub
        return (long) 0;
    }

    @Override
    public void updateBike(ClientBikeDto Bike) {

    }

    @Override
    public List<ClientBikeDto> findBikes(String keywords) {

        List<ClientBikeDto> Bikes = new ArrayList<>();
        Calendar startDate = Calendar.getInstance(); //MIRAR ESTO
        
        Bikes.add(new ClientBikeDto(1L, "Bike1 description", startDate, 10F, 1, 0,0));

        return Bikes;

    }

    @Override
    public Long bookBike(Long BikeId, String userId, String creditCardNumber) {
        // TODO Auto-generated method stub
        return null;
    }
}
