package es.udc.ws.bikes.dto;

import java.util.Calendar;

public class ServiceBookDto {
	
	private Long bookId;
	private Long bikeId;
	private Calendar initDate;
	private Calendar endDate;
	private int numberBikes;
	
	public ServiceBookDto() {
		
	}
	
	public ServiceBookDto(Long bookId, Long bikeId, Calendar initDate, 
			Calendar endDate, int numberBikes) {
		this.bookId = bookId;
		this.bikeId = bikeId;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberBikes = numberBikes;
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

}
