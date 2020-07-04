package es.udc.ws.bikes.serviceutil;

import es.udc.ws.bikes.dto.ServiceBookDto;
import es.udc.ws.bikes.model.book.*;

public class BookToBookDtoConversor {
	
	public static ServiceBookDto toBookDto(Book book) {
        return new ServiceBookDto(book.getBookId(), book.getBikeId(), book.getEmail(),
        		book.getCreditCard(), book.getInitDate(), book.getEndDate(), book.getNumberBikes()); 
    }
	
}
