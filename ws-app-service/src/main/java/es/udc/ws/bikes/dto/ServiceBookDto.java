package es.udc.ws.bikes.dto;

import java.util.Calendar;

public class ServiceBookDto {
	
	private Long bookId;
	private Long bikeId;
	private String email;
	private String creditCard;
	private Calendar initDate;
	private Calendar endDate;
	private int numberBikes;
	private int rating;
	
	
	public ServiceBookDto(Long bookId, Long bikeId, String email, String creditCard,
			Calendar initDate, Calendar endDate, int numberBikes) {
		this.bookId = bookId;
		this.bikeId = bikeId;
		this.email = email;
		this.creditCard = creditCard;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberBikes = numberBikes;
	}
	
	public ServiceBookDto(Long bookId, String email, int rating) {
		this.bookId = bookId;
		this.email = email;
		this.rating = rating;
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
	
	public Calendar getInitDate() {
		return initDate;
	}
	
	public void setInitDate(Calendar initDate) {
		this.initDate = initDate;
	}
	
	public Calendar getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
	
	public int getNumberBikes() {
		return numberBikes;
	}
	
	public void setNumberBikes(int numberBikes) {
		this.numberBikes = numberBikes;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
}
