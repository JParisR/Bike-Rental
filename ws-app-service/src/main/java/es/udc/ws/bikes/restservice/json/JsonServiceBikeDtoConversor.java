package es.udc.ws.bikes.restservice.json;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.dto.ServiceBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonServiceBikeDtoConversor {
	
	public final static String CONVERSION_PATTERN = "dd-MM-yyyy";
	public final static SimpleDateFormat sdf = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
	
	private static String getStartDate(Calendar startDate) {
		String date = sdf.format(startDate.getTime());
		return date;
	}
	
	public static ObjectNode toObjectNode(ServiceBikeDto bike) {
		ObjectNode bikeObject = JsonNodeFactory.instance.objectNode();
		
		if (bike.getBikeId() != null) {
			bikeObject.put("bikeId", bike.getBikeId());
		}
		bikeObject.put("name", bike.getName()).
			put("description", bike.getDescription()).
			put("price", bike.getPrice()).
			put("units", bike.getUnits()).
			put("startDate", getStartDate(bike.getStartDate())).
			put("numberOfRates", bike.getNumberOfRates()).
			put("avgRate", bike.getAvgRate());
		
		return bikeObject;
	}
	
	public static ArrayNode toArrayNode(List<ServiceBikeDto> bikes) {

		ArrayNode bikesNode = JsonNodeFactory.instance.arrayNode();
		for (int i = 0; i < bikes.size(); i++) {
			ServiceBikeDto bikeDto = bikes.get(i);
			ObjectNode bikeObject = toObjectNode(bikeDto);
			bikesNode.add(bikeObject);
		}

		return bikesNode;
	}
	
	public static ServiceBikeDto toServiceBikeDto(InputStream jsonBike) throws ParsingException {
		try {
			ObjectMapper objectMapper = ObjectMapperFactory.instance();
			JsonNode rootNode = objectMapper.readTree(jsonBike);
			
			if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
				throw new ParsingException("Unrecognized JSON (object expected)");
			} else {
				ObjectNode bikeObject = (ObjectNode) rootNode;
				
				String name = bikeObject.get("name").textValue().trim();
				String description = bikeObject.get("description").textValue().trim();
				
				JsonNode startDateObject = bikeObject.get("startDate");
				Calendar startDate = Calendar.getInstance();
				startDate.clear();
				startDate.set(startDateObject.get("year").intValue(),
						startDateObject.get("month").intValue()-1,
						startDateObject.get("day").intValue());
				
				int units =  bikeObject.get("units").intValue();
				float price = bikeObject.get("price").floatValue();

				return new ServiceBikeDto(name, description, price, units, startDate);
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}
	
}
