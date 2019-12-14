package es.udc.ws.bikes.client.service.rest.json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
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
				ObjectNode bikeObject = (ObjectNode) rootNode;

				JsonNode BookIdNode = bikeObject.get("BookId");
				Long BookId = (BookIdNode != null) ? BookIdNode.longValue() : null;

				Long bikeId = bikeObject.get("bikeId").longValue();
				int numberBikes = bikeObject.get("numberBikes").intValue();
				JsonNode initDateNode = bikeObject.get("initDate");
				Calendar initDate = getInitDate(initDateNode);
				JsonNode endDateNode = bikeObject.get("endDate");
				Calendar endDate = getInitDate(endDateNode);
				int bookRate = bikeObject.get("bookRate").intValue();

				return new ClientBookDto(BookId, bikeId, initDate, endDate, numberBikes, bookRate);


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
