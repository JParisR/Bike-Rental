package es.udc.ws.bikes.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.bikes.dto.ServiceBikeDto;
import es.udc.ws.bikes.model.bike.Bike;

public class BikeToBikeDtoConversor {
	public static List<ServiceBikeDto> toBikeDtos(List<Bike> bikes) {
		List<ServiceBikeDto> bikeDtos = new ArrayList<>(bikes.size());
		for (int i = 0; i < bikes.size(); i++) {
			Bike bike = bikes.get(i);
			bikeDtos.add(toFindBikeDto(bike));
		}
		return bikeDtos;
	}

	public static ServiceBikeDto toBikeDto(Bike bike) {
		return new ServiceBikeDto(bike.getBikeId(), bike.getName(), bike.getDescription(), 
				bike.getPrice(), bike.getUnits(), bike.getStartDate());
	}
	
	public static ServiceBikeDto toFindBikeDto(Bike bike) {
		return new ServiceBikeDto(bike.getBikeId(),  bike.getName(), bike.getDescription(), 
				bike.getPrice(), bike.getUnits(), bike.getStartDate(), bike.getNumberOfRates(),
				bike.getAvgRate());
	}

	public static Bike toBike(ServiceBikeDto bike) {
		return new Bike(bike.getName(), bike.getDescription(), bike.getStartDate(), bike.getPrice(),
				bike.getUnits());
	}

}
