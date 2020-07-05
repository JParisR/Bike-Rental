package es.udc.ws.bikes.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.client.service.dto.UserClientBookDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonUserClientBookDtoConversor {
	
	public static JsonNode toJsonObject(UserClientBookDto book) throws IOException {

		ObjectNode bookObject = JsonNodeFactory.instance.objectNode();

		if (book.getBookId() != null) {
			bookObject.put("bookId", book.getBookId());
		}
		
		bookObject.put("email", book.getEmail()).put("bikeId", book.getBikeId()).
		put("creditCard", book.getCreditCard()).put("numberBikes", book.getUnits());
		bookObject.set("initDate", getNodeFromDate(book.getStartDate()));
		bookObject.set("endDate", getNodeFromDate(book.getEndDate()));
		
		return bookObject;
	}
	
	public static JsonNode toRateJsonObject(UserClientBookDto book) throws IOException {

		ObjectNode bookObject = JsonNodeFactory.instance.objectNode();

		if (book.getBookId() != null) {
			bookObject.put("bookId", book.getBookId());
		}
		
		bookObject.put("email", book.getEmail()).put("bikeId", book.getBikeId()).
			put("rating", book.getRating());
		
		return bookObject;
	}
	
	public static UserClientBookDto toClientBookDto(InputStream jsonBook) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBook);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode bikeObject = (ObjectNode) rootNode;

				JsonNode bookIdNode = bikeObject.get("bookId");
				Long bookId = (bookIdNode != null) ? bookIdNode.longValue() : null;

				Long bikeId = bikeObject.get("bikeId").longValue();
				String email = bikeObject.get("email").textValue();
				String creditCard = bikeObject.get("creditCard").textValue();
				JsonNode startDateNode = bikeObject.get("initDate");
				Calendar startDate = getDateFromNode(startDateNode);
				JsonNode endDateNode = bikeObject.get("endDate");
				Calendar endDate = getDateFromNode(endDateNode);
				int units = bikeObject.get("numberBikes").intValue();
				
				return new UserClientBookDto(bookId, bikeId, email, creditCard, startDate, endDate, units);

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static List<UserClientBookDto> toClientBookDtos(InputStream jsonBook) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBook);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode booksArray = (ArrayNode) rootNode;
				List<UserClientBookDto> bookDtos = new ArrayList<>(booksArray.size());
				for (JsonNode bookNode : booksArray) {
					bookDtos.add(toClientFindBookDto(bookNode));
				}

				return bookDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static UserClientBookDto toClientFindBookDto(JsonNode jsonBook) throws ParsingException {
		try {
			if (jsonBook.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode bikeObject = (ObjectNode) jsonBook;

				JsonNode bookIdNode = bikeObject.get("bookId");
				Long bookId = (bookIdNode != null) ? bookIdNode.longValue() : null;

				String email = bikeObject.get("email").textValue();
				int days = bikeObject.get("days").intValue();
				int rating = bikeObject.get("rating").intValue();
				
				return new UserClientBookDto(bookId, email, days, rating);

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static Calendar getDateFromNode(JsonNode dateNode) {

		if (dateNode == null) {
			return null;
		}
		int day = dateNode.get("day").intValue();
		int month = dateNode.get("month").intValue();
		int year = dateNode.get("year").intValue();
		Calendar releaseDate = Calendar.getInstance();

		releaseDate.set(Calendar.DAY_OF_MONTH, day);
		releaseDate.set(Calendar.MONTH, month - 1);
		releaseDate.set(Calendar.YEAR, year);

		return releaseDate;

	}

	private static ObjectNode getNodeFromDate(Calendar date) {

		ObjectNode releaseDateObject = JsonNodeFactory.instance.objectNode();

        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);

        releaseDateObject.put("day", day).
			put("month", month).
			put("year", year);

        return releaseDateObject;

    }
	
}
