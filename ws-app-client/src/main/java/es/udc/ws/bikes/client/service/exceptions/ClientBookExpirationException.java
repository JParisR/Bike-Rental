package es.udc.ws.bikes.client.service.exceptions;

import java.util.Calendar;

@SuppressWarnings("serial")
public class ClientBookExpirationException extends Exception {

    private Long bookId;
    private Calendar expirationDate;

    public ClientBookExpirationException(Long bookId, Calendar expirationDate) {
        super("Book with id=\"" + bookId
                + "\" has expired (expirationDate = \""
                + expirationDate + "\")");
        this.bookId = bookId;
        this.expirationDate = expirationDate;
    }

    public Long getBookId() {
        return bookId;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
