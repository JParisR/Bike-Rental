package es.udc.ws.bikes.service.rest.json;

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

import es.udc.ws.bikes.service.dto.ClientBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBikeDtoConversor {
	public static JsonNode toJsonObject(ClientBikeDto bike) throws IOException {

		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();

		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}
		
		bikeObject.put("description", bike.getDescription()).put("price", bike.getPrice()).
			put("units", bike.getUnits()).set("startDate", getNodeFromDate(bike.getStartDate()));

		return bikeObject;
	}

	public static ClientBikeDto toClientBikeDto(InputStream jsonBike) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBike);
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				return toClientBikeDto(rootNode);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static List<ClientBikeDto> toClientBikeDtos(InputStream jsonBikes) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikes);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode bikesArray = (ArrayNode) rootNode;
				List<ClientBikeDto> bikeDtos = new ArrayList<>(bikesArray.size());
				for (JsonNode bikeNode : bikesArray) {
					bikeDtos.add(toClientBikeDto(bikeNode));
				}

				return bikeDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientBikeDto toClientBikeDto(JsonNode bikeNode) throws ParsingException {
		if (bikeNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode bikeObject = (ObjectNode) bikeNode;

			JsonNode bikeIdNode = bikeObject.get("bikeId");
			Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue() : null;

			String description = bikeObject.get("description").textValue().trim();
			JsonNode startDateNode = bikeObject.get("startDate");
			Calendar startDate = getDate(startDateNode);
			float price = bikeObject.get("price").floatValue();
			int units = bikeObject.get("units").intValue();

			return new ClientBikeDto(bikeId, description, startDate, price, units);
		}
	}
	
	private static Calendar getDate(JsonNode dateNode) {

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
        int month = date.get(Calendar.MONTH) - Calendar.JANUARY + 1;
        int year = date.get(Calendar.YEAR);

        releaseDateObject.put("day", day).
			put("month", month).
			put("year", year);

        return releaseDateObject;

    }

}
