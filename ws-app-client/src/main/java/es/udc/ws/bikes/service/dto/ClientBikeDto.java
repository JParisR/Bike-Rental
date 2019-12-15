package es.udc.ws.bikes.service.dto;

import java.util.Calendar;

public class ClientBikeDto {

    private Long bikeId;
    private String description;
    private Calendar startDate;
    private float price;
    private int units;

    public ClientBikeDto() {
    }    
    
    public ClientBikeDto(Long bikeId, String description, Calendar startDate, float price, int units) {

        this.bikeId = bikeId;
        this.description = description;
        this.startDate = startDate;
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
        return "bikeDto [bikeId=" + bikeId + ", description=" + description
                + ", startDate=" + startDate.toString() + "price=" + price + ", units=" + units + "]";
    }



}
