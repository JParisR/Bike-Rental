package es.udc.ws.bikes.client.service.dto;

import java.util.Calendar;

public class AdminClientBookDto {

    private Long bookId;
    private Long bikeId;
    private String email;
    private String creditCard;
    private Calendar startDate;
    private Calendar endDate;
    private int units;
    
    public AdminClientBookDto() {
    }
    
    public AdminClientBookDto(Long bookId, Long bikeId, String email, String creditCard, Calendar startDate, Calendar endDate, int units) {
		
    	this.bookId = bookId;
		this.bikeId = bikeId;
		this.email = email;
		this.creditCard = creditCard;
		this.startDate = startDate;
		this.endDate = endDate;
		this.units = units;
	
    }
    
    public AdminClientBookDto(Long bikeId, String email, String creditCard, Calendar startDate, Calendar endDate, int units) {

		this.bikeId = bikeId;
		this.email = email;
		this.creditCard = creditCard;
		this.startDate = startDate;
		this.endDate = endDate;
		this.units = units;
	
    }
    
	public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public Long getBikeId() {
		return bikeId;
	}

	public void setBikeId(Long bikeId) {
		this.bikeId = bikeId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "ClientBookDto [bookId=" + bookId + ", bikeId=" + bikeId + ", email=" + email + ", creditCard="
				+ creditCard + ", startDate=" + startDate.toString() + ", endDate=" + endDate.toString() + ", units=" + units + "]";
	}

}
