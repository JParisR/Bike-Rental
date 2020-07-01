package es.udc.ws.bikes.dto;

import java.util.Calendar;

public class ServiceBikeDto {
	
	private Long bikeId;
	private String description;
	private Calendar startDate;
	private float price;
	private int units;
	private Calendar creationDate;
	private int numberOfRates;
	private double avgRate;
	
	
	public ServiceBikeDto(Long bikeId, String description, float price, int units, Calendar startDate) {
		this.bikeId = bikeId;
		this.description = description;
		this.price = price;
		this.units = units;
		this.startDate = startDate;
	}

	public Long getBikeId() {
		return bikeId;
	}
	
	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
	public int getUnits() {
		return units;
	}
	
	public void setUnits(int units) {
		this.units = units;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
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
		return "BikeDto [bikeId=" + bikeId + ", units=" + units
				+ ", price=" + price
				+ ", description=" + description +"]";
	}
}

