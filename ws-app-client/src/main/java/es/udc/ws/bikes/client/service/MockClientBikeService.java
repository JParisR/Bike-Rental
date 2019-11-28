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
    public void removeBike(Long BikeId) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<ClientBikeDto> findBikes(String keywords) {

        List<ClientBikeDto> Bikes = new ArrayList<>();

        Bikes.add(new ClientBikeDto(1L, "Bike1 description",
                (short) 2, 10F, 1, 0,0));
      //  Bikes.add(new ClientBikeDto(2L, "Bike2",
        //        (short) 2, (short) 0, "Bike2 description", 10F));
        
        //(Long bikeId, String description, Calendar startDate, float price,
        	//	int units, int numberOfRates, double avgRate)

        return Bikes;

    }

    @Override
    public Long buyBike(Long BikeId, String userId, String creditCardNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getBikeUrl(Long saleId) {
        // TODO Auto-generated method stub
        return null;
    }
}
