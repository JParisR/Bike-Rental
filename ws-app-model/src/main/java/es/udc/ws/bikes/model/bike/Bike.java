package es.udc.ws.bikes.model.bike;

import java.util.Calendar;

public class Bike {
	
	private Long bikeId;
	private String name;
	private String description;
	private Calendar startDate;
	private float price;
	private int units;
	private Calendar creationDate;
	private int numberOfRates;
	private double avgRate;

	public Bike(String name, String description, Calendar startDate, float price, 
			int units, Calendar creationDate) {
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.price = price;
		this.units = units;
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
	}
	
	public Bike(Long bikeId, String name, String description, Calendar startDate, 
			float price, int units, Calendar creationDate) {
		this(name, description, startDate, price, units, creationDate);
		this.bikeId = bikeId;
	}
	
	public Bike(Long bikeId, String name, String description, Calendar startDate, 
			float price, int units, Calendar creationDate,
			double avgRate, int numberOfRates) {
		this(bikeId, name, description, startDate, price, units, creationDate);
		this.avgRate = avgRate;
		this.numberOfRates = numberOfRates;
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

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
	}

	public double getNumberOfRates() {
		return numberOfRates;
	}

	public void setNumberofRates(int numberOfRates) {
		this.numberOfRates = numberOfRates;
	}
	
	public double getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(double avgRate) {
		this.avgRate = avgRate;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
	
	public double getAvgRating () {
		return avgRate/numberOfRates;
	}
}
	
	
