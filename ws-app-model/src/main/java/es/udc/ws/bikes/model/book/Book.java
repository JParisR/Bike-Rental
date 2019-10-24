package es.udc.ws.bikes.model.book;

import java.util.Calendar;

public class Book {

	private Long bookId;
	private String email;
	private String creditCard;
	private Calendar initDate;
	private Calendar endDate;
	private int numberBikes;
	private Calendar bookDate;
	
	public Book(String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes) {
	
		this.email = email;
		this.creditCard = creditCard;
		this.initDate = initDate;
		this.endDate = endDate;
		this.numberBikes = numberBikes;
	}
	
	public Book(Long bookId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes) {
		this(email, creditCard, initDate, endDate, numberBikes);
		this.bookId = bookId;
	}
	
	public Book(Long bookId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes, Calendar bookDate) {
		this(email, creditCard, initDate, endDate, numberBikes);
		this.bookDate = bookDate;
		if (bookDate != null) {
			this.bookDate.set(Calendar.MILLISECOND, 0);
		}
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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