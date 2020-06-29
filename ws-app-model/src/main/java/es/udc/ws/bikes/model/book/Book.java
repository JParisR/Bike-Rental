package es.udc.ws.bikes.model.book;

import java.util.Calendar;

public class Book {

	private Long bookId;
	private Long bikeId;
	private String email;
	private String creditCard;
	private Calendar initDate;
	private Calendar endDate;
	private int numberBikes;
	private Calendar bookDate;
	private int bookRate;
	
	public Book(String email, String creditCard, Calendar initDate, 
			Calendar endDate, int numberBikes) {
	
		this.email = email;
		this.creditCard = creditCard;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberBikes = numberBikes;
		
	}
	
	public Book(Long bikeId, String email, String creditCard, 
			Calendar initDate, Calendar endDate, int numberBikes) {
		this(email, creditCard, initDate, endDate, numberBikes);
		this.bikeId = bikeId;
	}
	
	public Book(Long bikeId, String email, 
			String creditCard, Calendar initDate, Calendar endDate, 
			int numberBikes, Calendar bookDate) {
		this(bikeId, email, creditCard, initDate, endDate, numberBikes);
		this.bookDate = bookDate;
		if (bookDate != null) {
			this.bookDate.set(Calendar.MILLISECOND, 0);
		}
	}
	
	public Book(Long bikeId, String email, 
			String creditCard, Calendar initDate, Calendar endDate, 
			int numberBikes, Calendar bookDate, int bookRate) {
		this(bikeId, email, creditCard, initDate, endDate, numberBikes, bookDate);
		this.bookRate = bookRate;
	}
	
	public Book(Long bookId, Long bikeId, String email, 
			String creditCard, Calendar initDate, Calendar endDate, 
			int numberBikes, Calendar bookDate, int bookRate) {
		this(bikeId, email, creditCard, initDate, endDate, numberBikes, bookDate, bookRate);
		this.bookId = bookId;
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

	public Calendar getBookDate() {
		return bookDate;
	}

	public void setBookDate(Calendar bookDate) {
		this.bookDate = bookDate;
	}
	
	public int getBookRate() {
		return bookRate;
	}
	
	public void setBookRate(int bookRate) {
		this.bookRate = bookRate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bikeId == null) ? 0 : bikeId.hashCode());
		result = prime * result + ((bookDate == null) ? 0 : bookDate.hashCode());
		result = prime * result + ((bookId == null) ? 0 : bookId.hashCode());
		result = prime * result + ((creditCard == null) ? 0 : creditCard.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((initDate == null) ? 0 : initDate.hashCode());
		result = prime * result + numberBikes;
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
		Book other = (Book) obj;
		if (bikeId == null) {
			if (other.bikeId != null)
				return false;
		} else if (!bikeId.equals(other.bikeId))
			return false;
		if (bookDate == null) {
			if (other.bookDate != null)
				return false;
		} else if (!bookDate.equals(other.bookDate))
			return false;
		if (bookId == null) {
			if (other.bookId != null)
				return false;
		} else if (!bookId.equals(other.bookId))
			return false;
		if (creditCard == null) {
			if (other.creditCard != null)
				return false;
		} else if (!creditCard.equals(other.creditCard))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (initDate == null) {
			if (other.initDate != null)
				return false;
		} else if (!initDate.equals(other.initDate))
			return false;
		if (numberBikes != other.numberBikes)
			return false;
		return true;
	}
	
}