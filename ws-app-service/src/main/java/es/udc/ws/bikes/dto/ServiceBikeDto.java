package es.udc.ws.bikes.dto;

import java.util.Calendar;

public class ServiceBikeDto {
	
	private Long bikeId;
	private String name;
	private String description;
	private Calendar startDate;
	private float price;
	private int units;
	private int numberOfRates;
	private double avgRate;
	
	public ServiceBikeDto(String name, String description, float price, int units, Calendar startDate) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.units = units;
		this.startDate = startDate;
	}
	
	public ServiceBikeDto(Long bikeId, String name, String description, float price, int units, Calendar startDate) {
		this(name, description, price, units, startDate);
		this.bikeId = bikeId;
	}
	
	public ServiceBikeDto(Long bikeId, String name, String description, float price, int units, Calendar startDate,
			int numberOfRates, double avgRate) {
		this(bikeId, name, description, price, units, startDate);
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
		return "BikeDto [bikeId=" + bikeId + ", units=" + units + "name=" + name
				+ ", price=" + price + "description=" + description +"]";
	}
}

