package es.udc.ws.bikes.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.bikes.dto.ServiceBookDto;
import es.udc.ws.bikes.model.book.*;

public class BookToBookDtoConversor {
	
	public static List<ServiceBookDto> toBookDtos(List<Book> books) {
		List<ServiceBookDto> bookDtos = new ArrayList<>(books.size());
		for (int i = 0; i < books.size(); i++) {
			Book book = books.get(i);
			bookDtos.add(toFindBookDto(book));
		}
		return bookDtos;
	}
	
	public static ServiceBookDto toBookDto(Book book) {
        return new ServiceBookDto(book.getBookId(), book.getBikeId(), book.getEmail(),
        		book.getCreditCard(), book.getInitDate(), book.getEndDate(), book.getNumberBikes()); 
    }
	
	public static ServiceBookDto toFindBookDto(Book book) {
		return new ServiceBookDto(book.getBookId(), book.getBikeId(), book.getEmail(), book.getInitDate(),
				book.getEndDate(), book.getBookRate());
	}
	
}
