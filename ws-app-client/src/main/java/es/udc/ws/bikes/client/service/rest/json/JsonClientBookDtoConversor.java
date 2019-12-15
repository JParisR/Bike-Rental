package es.udc.ws.bikes.client.service.rest.json;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.client.service.dto.ClientBookDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBookDtoConversor {

	public static ClientBookDto toClientBookDto(InputStream jsonBook) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBook);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				return toClientBookDto(rootNode);
//				ObjectNode bikeObject = (ObjectNode) rootNode;
//
//				JsonNode BookIdNode = bikeObject.get("BookId");
//				Long BookId = (BookIdNode != null) ? BookIdNode.longValue() : null;
//
//				Long bikeId = bikeObject.get("bikeId").longValue();
//				int numberBikes = bikeObject.get("numberBikes").intValue();
//				JsonNode initDateNode = bikeObject.get("initDate");
//				Calendar initDate = getInitDate(initDateNode);
//				JsonNode endDateNode = bikeObject.get("endDate");
//				Calendar endDate = getInitDate(endDateNode);
//				int bookRate = bikeObject.get("bookRate").intValue();
//
//				return new ClientBookDto(BookId, bikeId, initDate, endDate, numberBikes, bookRate);
//				

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	public static List<ClientBookDto> toClientBookDtos(InputStream jsonBooks) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBooks);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode booksArray = (ArrayNode) rootNode;
				List<ClientBookDto> bookDtos = new ArrayList<>(booksArray.size());
				for (JsonNode bookNode : booksArray) {
					bookDtos.add(toClientBookDto(bookNode));
				}

				return bookDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
	private static ClientBookDto toClientBookDto(JsonNode bookNode) throws ParsingException {
		if (bookNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode bookObject = (ObjectNode) bookNode;

			JsonNode bookIdNode = bookObject.get("bookId");
			Long bookId = (bookIdNode != null) ? bookIdNode.longValue() : null;

			Long bikeId = bookObject.get("bikeId").longValue();
			String initDateString = bookObject.get("initDate").textValue().trim();
			String endDateString = bookObject.get("endDate").textValue().trim();
			
			Date initDateAux = null;
			Date endDateAux = null;
			
			try{
				initDateAux = new SimpleDateFormat("dd/MM/yyyy").parse(initDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			try{
				endDateAux = new SimpleDateFormat("dd/MM/yyyy").parse(endDateString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			Calendar initDate = Calendar.getInstance();
			initDate.setTime(initDateAux);
			Calendar endDate = Calendar.getInstance();
			endDate.setTime(endDateAux);
			
			int numberBikes = bookObject.get("numberBikes").intValue();
			int bookRate = bookObject.get("bookRate").intValue();

			return new ClientBookDto(bookId, bikeId, initDate, endDate, numberBikes, bookRate);
		}
	}
	
	
 //COMPROBAR ESTO
	private static Calendar getInitDate(JsonNode initDateNode) {

		if (initDateNode == null) {
			return null;
		}
		int day = initDateNode.get("day").intValue();
		int month = initDateNode.get("month").intValue();
		int year = initDateNode.get("year").intValue();
		Calendar releaseDate = Calendar.getInstance();

		releaseDate.set(Calendar.DAY_OF_MONTH, day);
		releaseDate.set(Calendar.MONTH, Calendar.JANUARY + month - 1);
		releaseDate.set(Calendar.YEAR, year);

		return releaseDate;

	}

}
