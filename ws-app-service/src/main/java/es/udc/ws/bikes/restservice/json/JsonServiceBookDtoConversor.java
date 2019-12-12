package es.udc.ws.bikes.restservice.json;

import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.*;

public class JsonServiceBookDtoConversor {
	
	public static JsonNode toJsonObject(ServiceBookDto book) {		

		ObjectNode bookNode = JsonNodeFactory.instance.objectNode();

        if (book.getBookId() != null) {
        	bookNode.put("bookId", book.getBookId());
        }
        bookNode.put("bikeId", book.getBikeId()).
        	put("numberBikes", book.getNumberBikes()).
        	set("initDate", getInitDate(book.getInitDate()));
        bookNode.set("endDate", getEndDate(book.getEndDate()));
        
        return bookNode;
    }


    private static ObjectNode getInitDate(Calendar initDate) {

		ObjectNode releaseDateObject = JsonNodeFactory.instance.objectNode();

        int day = initDate.get(Calendar.DAY_OF_MONTH);
        int month = initDate.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = initDate.get(Calendar.YEAR);

        releaseDateObject.put("day", day).
			put("month", month).
			put("year", year);

        return releaseDateObject;

    }
    
    private static ObjectNode getEndDate(Calendar endDate) {

		ObjectNode releaseDateObject = JsonNodeFactory.instance.objectNode();

        int day = endDate.get(Calendar.DAY_OF_MONTH);
        int month = endDate.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = endDate.get(Calendar.YEAR);

        releaseDateObject.put("day", day).
			put("month", month).
			put("year", year);

        return releaseDateObject;

    }

}
