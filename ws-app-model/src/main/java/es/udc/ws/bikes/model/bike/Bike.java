package es.udc.ws.bikes.model.bike;

import java.util.Calendar;

public class Bike {
	
	private Long bikeId;
	private String description;
	private Calendar startDate;
	private float price;
	private int units;
	private Calendar creationDate;
	private int numberOfRates;
	private int avgRate;

	public Bike(String description, Calendar startDate, float price, int units) {
		this.description = description;
		this.startDate = startDate;
		this.price = price;
		this.units = units;
	}
	
	public Bike(Long bikeId, String description, Calendar startDate, float price, int units) {
		this(description, startDate, price, units);
		this.bikeId = bikeId;
	}
	
	public Bike(Long bikeId, String description, Calendar startDate, float price, int units, Calendar creationDate) {
		this(bikeId, description, startDate, price, units);
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}	
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

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public int getNumberOfRates() {
		return numberOfRates;
	}

	public void setNumberofRates(int numberOfRates) {
		this.numberOfRates = numberOfRates;
	}
	
	public int getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(int avgRate) {
		this.avgRate = avgRate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + units;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bike other = (Bike) obj;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (units != other.units)
			return false;
		return true;
	}
}
	
	
