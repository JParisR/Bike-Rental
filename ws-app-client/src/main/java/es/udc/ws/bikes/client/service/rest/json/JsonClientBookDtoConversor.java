package es.udc.ws.bikes.client.service.rest.json;

import java.io.InputStream;
import java.util.Calendar;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
				ObjectNode movieObject = (ObjectNode) rootNode;

				JsonNode BookIdNode = movieObject.get("BookId");
				Long BookId = (BookIdNode != null) ? BookIdNode.longValue() : null;

				Long movieId = movieObject.get("movieId").longValue();
				String movieUrl = movieObject.get("movieUrl").textValue().trim();
				JsonNode expirationDateNode = movieObject.get("expirationDate");
				Calendar expirationDate = getExpirationDate(expirationDateNode);

				return new ClientBookDto(BookId, movieId, expirationDate, movieUrl);

			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static Calendar getExpirationDate(JsonNode expirationDateNode) {

		if (expirationDateNode == null) {
			return null;
		}
		int day = expirationDateNode.get("day").intValue();
		int month = expirationDateNode.get("month").intValue();
		int year = expirationDateNode.get("year").intValue();
		Calendar releaseDate = Calendar.getInstance();

		releaseDate.set(Calendar.DAY_OF_MONTH, day);
		releaseDate.set(Calendar.MONTH, Calendar.JANUARY + month - 1);
		releaseDate.set(Calendar.YEAR, year);

		return releaseDate;

	}

}
