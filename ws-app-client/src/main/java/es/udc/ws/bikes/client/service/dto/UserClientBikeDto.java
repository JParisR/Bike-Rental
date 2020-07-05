package es.udc.ws.bikes.client.service.dto;

public class UserClientBikeDto {

	private Long bikeId;
	private int numberOfRates;
	private double avgRate;

    public UserClientBikeDto(Long bikeId, int numberOfRates, double avgRate) {
        this.bikeId = bikeId;
        this.numberOfRates = numberOfRates;
        this.avgRate = avgRate;
    }

	public Long getBikeId() {
        return bikeId;
    }
    
    public void setBikeId(Long bikeId) {
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

	@Override
	public String toString() {
		return "UserClientBikeDto [bikeId=" + bikeId + ", numberOfRates=" + numberOfRates + 
				", avgRate=" + avgRate + "]";
	}

}
