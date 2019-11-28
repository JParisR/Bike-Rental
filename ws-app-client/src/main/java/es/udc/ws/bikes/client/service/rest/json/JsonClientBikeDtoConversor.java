package es.udc.ws.bikes.client.service.rest.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;

import es.udc.ws.bikes.client.service.dto.ClientBikeDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class JsonClientBikeDtoConversor {
	public static JsonNode toJsonObject(ClientBikeDto Bike) throws IOException {

		ObjectNode BikeObject = JsonNodeFactory.instance.objectNode();

		if (Bike.getBikeId() != null) {
			BikeObject.put("BikeId", Bike.getBikeId());
		}
		BikeObject.put("runtime", Bike.getRuntimeHours() * 60 + Bike.getRuntimeMinutes()).
			put("price", Bike.getPrice()).
			put("title", Bike.getTitle()).
			put("description", Bike.getDescription());

		return BikeObject;
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
				ArrayNode BikesArray = (ArrayNode) rootNode;
				List<ClientBikeDto> BikeDtos = new ArrayList<>(BikesArray.size());
				for (JsonNode BikeNode : BikesArray) {
					BikeDtos.add(toClientBikeDto(BikeNode));
				}

				return BikeDtos;
			}
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientBikeDto toClientBikeDto(JsonNode BikeNode) throws ParsingException {
		if (BikeNode.getNodeType() != JsonNodeType.OBJECT) {
			throw new ParsingException("Unrecognized JSON (object expected)");
		} else {
			ObjectNode BikeObject = (ObjectNode) BikeNode;

			JsonNode BikeIdNode = BikeObject.get("BikeId");
			Long BikeId = (BikeIdNode != null) ? BikeIdNode.longValue() : null;

			String title = BikeObject.get("title").textValue().trim();
			String description = BikeObject.get("description").textValue().trim();
			short runtime = BikeObject.get("runtime").shortValue();
			float price = BikeObject.get("price").floatValue();

			return new ClientBikeDto(BikeId, title, (short) (runtime / 60), (short) (runtime % 60), description,
					price);
		}
	}

}
