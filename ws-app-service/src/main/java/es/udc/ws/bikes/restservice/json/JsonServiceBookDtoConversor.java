package es.udc.ws.bikes.restservice.json;

import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.*;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonServiceBookDtoConversor {
	
	public static JsonNode toJsonObject(ServiceBookDto book) {		

		ObjectNode bookNode = JsonNodeFactory.instance.objectNode();

        if (book.getBookId() != null) {
        	bookNode.put("bookId", book.getBookId());
        }
        bookNode.put("bikeId", book.getBikeId()).
        	put("email", book.getEmail()).
        	put("creditCard", book.getCreditCard()).
        	put("numberBikes", book.getNumberBikes()).
        	set("initDate", getNodeFromDate(book.getInitDate()));
        bookNode.set("endDate", getNodeFromDate(book.getEndDate()));
        
        return bookNode;
    }

	public static ServiceBookDto toServiceBookDto(InputStream jsonBook) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBook);
			
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode bookObject = (ObjectNode) rootNode;
				
				Long bookId = bookObject.get("bookId").longValue();
				String email = bookObject.get("email").textValue().trim();
				int rating =  bookObject.get("rating").intValue();

				return new ServiceBookDto(bookId, email, rating);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

    private static ObjectNode getNodeFromDate(Calendar date) {

		ObjectNode releaseDateObject = JsonNodeFactory.instance.objectNode();

        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = date.get(Calendar.YEAR);

        releaseDateObject.put("day", day).
			put("month", month).
			put("year", year);

        return releaseDateObject;

    }

}
