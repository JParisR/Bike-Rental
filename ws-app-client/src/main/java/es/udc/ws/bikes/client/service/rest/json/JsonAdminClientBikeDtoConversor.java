package es.udc.ws.bikes.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonAdminClientBikeDtoConversor {
	
	public final static String CONVERSION_PATTERN = "dd-MM-yyyy";
	public final static SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
	
	public static JsonNode toJsonObject(AdminClientBikeDto bike) throws IOException {

		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();

		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}
		
		bikeObject.put("name", bike.getName()).put("description", bike.getDescription()).
			put("price", bike.getPrice()).put("units", bike.getUnits());
		bikeObject.set("startDate", getNodeFromDate(bike.getStartDate()));

		return bikeObject;
	}

	public static AdminClientBikeDto toClientBikeDto(InputStream jsonBike) throws ParsingException {
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

	public static List<AdminClientBikeDto> toClientBikeDtos(InputStream jsonBikes) throws ParsingException {
		try {

			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBikes);
			if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
				throw new ParsingException("Unrecognized JSON (array expected)");
			} else {
				ArrayNode bikesArray = (ArrayNode) rootNode;
				List<AdminClientBikeDto> bikeDtos = new ArrayList<>(bikesArray.size());
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

	private static AdminClientBikeDto toClientBikeDto(JsonNode bikeNode) throws ParsingException,
			ParseException {
		if (bikeNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode bikeObject = (ObjectNode) bikeNode;

			JsonNode bikeIdNode = bikeObject.get("bikeId");
			Long bikeId = (bikeIdNode != null) ? bikeIdNode.longValue() : null;

			String name = bikeObject.get("name").textValue().trim();
			String description = bikeObject.get("description").textValue().trim();
			JsonNode startDateNode = bikeObject.get("startDate");
			
			
			Calendar startDate = Calendar.getInstance();
			try {
				startDate.setTime(sdf.parse(startDateNode.asText()));
			} catch (ParseException e) {
				throw e;
			}
						
			float price = bikeObject.get("price").floatValue();
			int units = bikeObject.get("units").intValue();

			return new AdminClientBikeDto(bikeId, name, description, startDate, price, units);
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
