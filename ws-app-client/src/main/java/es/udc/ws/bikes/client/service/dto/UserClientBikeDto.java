package es.udc.ws.bikes.client.service.dto;

import java.util.Calendar;

public class UserClientBikeDto {

	private Long bikeId;
	private String name;
	private String description;
	private Calendar startDate;
	private int numberOfRates;
	private double avgRate;

	public UserClientBikeDto(Long bikeId, String name, String description, Calendar startDate, 
			int numberOfRates, double avgRate) {
		this.bikeId = bikeId;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.numberOfRates = numberOfRates;
		this.avgRate = avgRate;
	}

	public Long getBikeId() {
        return bikeId;
    }
    
    public void setBikeId(Long bikeId) {
        this.bikeId = bikeId;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public int getNumberOfRates() {
		return numberOfRates;
	}

	public void setNumberOfRates(int numberOfRates) {
		this.numberOfRates = numberOfRates;
	}

	public double getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(double avgRate) {
		this.avgRate = avgRate;
	}

	@Override
	public String toString() {
		return "UserClientBikeDto [bikeId=" + bikeId + ", numberOfRates=" + numberOfRates + 
				", avgRate=" + avgRate + "]";
	}

}
