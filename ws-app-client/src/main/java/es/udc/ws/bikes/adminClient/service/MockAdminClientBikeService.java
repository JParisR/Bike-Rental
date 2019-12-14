package es.udc.ws.bikes.adminClient.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.bikes.adminClient.service.dto.*;

public class MockAdminClientBikeService implements AdminClientBikeService{

    @Override
    public Long addBike(AdminClientBikeDto Bike) {
        // TODO Auto-generated method stub
        return (long) 0;
    }

    @Override
    public void updateBike(AdminClientBikeDto Bike) {

    }

    @Override
    public List<AdminClientBikeDto> findBikes(String bikeId) {

        List<AdminClientBikeDto> Bikes = new ArrayList<>();
        Calendar startDate = Calendar.getInstance(); //MIRAR ESTO
        
        Bikes.add(new AdminClientBikeDto(1L, "Bike1 description", startDate, 10F, 1, 0,0));

        return Bikes;

    }

}
