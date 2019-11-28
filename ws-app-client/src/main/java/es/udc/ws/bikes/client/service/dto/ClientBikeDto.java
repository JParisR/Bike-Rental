package es.udc.ws.bikes.client.service.dto;

import java.util.Calendar;

public class ClientBikeDto {

	private Long bikeId;
	private String description;
	private Calendar startDate;
	private float price;
	private int units;
	private int numberOfRates;
	private double avgRate;

    public ClientBikeDto() {
    }    
    
    public ClientBikeDto(Long bikeId, String description, Calendar startDate, float price,
    		int units, int numberOfRates, double avgRate) {

        this.bikeId = bikeId;
        this.description = description;
        this.price = price;
        this.units = units;
        this.numberOfRates = numberOfRates;
        this.avgRate = avgRate;

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
		return "ClientBikeDto [bikeId=" + bikeId + ", description=" + description + ", startDate=" + startDate
				+ ", price=" + price + ", units=" + units + ", numberOfRates=" + numberOfRates + ", avgRate=" + avgRate
				+ "]";
	}

}