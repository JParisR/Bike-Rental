package es.udc.ws.bikes.dto;

public class ServiceBikeDto {
	
	private Long bikeId;
	private String description;
	private float price;
	private int units;
	
	public ServiceBikeDto() {
		
	}
	
	public ServiceBikeDto(Long bikeId, String description, float price, int units) {
		this.bikeId = bikeId;
		this.description = description;
		this.price = price;
		this.units = units;
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
	
	@Override
	public String toString() {
		return "BikeDto [bikeId=" + bikeId + ", units=" + units
				+ ", price=" + price
				+ ", description=" + description +"]";
	}
}

