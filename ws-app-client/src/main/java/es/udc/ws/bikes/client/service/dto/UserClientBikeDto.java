package es.udc.ws.bikes.client.service.dto;

import java.util.Calendar;

public class UserClientBikeDto {

	private Long bikeId;
	private String name;
    private String description;
    private Calendar startDate;
    private float price;
    private int units;
	private int numberOfRates;
	private double avgRate;

    public UserClientBikeDto() {
    }    
    
    public UserClientBikeDto(String name, String description, Calendar startDate, float price, 
    						 int units, int numberOfRates, double avgRate) {

        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.price = price;
        this.units = units;
        this.numberOfRates = numberOfRates;
        this.avgRate = avgRate;

    }
    
    public UserClientBikeDto(Long bikeId, String name, String description, Calendar startDate, float price, 
    		int units, int numberOfRates, double avgRate) {

    	this(name, description, startDate, price, units, numberOfRates, avgRate);
        this.bikeId = bikeId;

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
    
    @Override
	public String toString() {
		return "UserClientBikeDto [bikeId=" + bikeId + ", name=" + name + ", description=" + description
				+ ", startDate=" + startDate + ", price=" + price + ", units=" + units + ", numberOfRates="
				+ numberOfRates + ", avgRate=" + avgRate + "]";
	}

}
