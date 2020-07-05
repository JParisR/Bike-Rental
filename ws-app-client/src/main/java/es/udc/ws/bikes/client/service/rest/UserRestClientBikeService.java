package es.udc.ws.bikes.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import es.udc.ws.bikes.client.service.UserClientBikeService;
import es.udc.ws.bikes.client.service.dto.UserClientBikeDto;
import es.udc.ws.bikes.client.service.dto.UserClientBookDto;
import es.udc.ws.bikes.client.service.exceptions.ClientBookExpirationException;
import es.udc.ws.bikes.client.service.rest.json.JsonUserClientBikeDtoConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonUserClientBookDtoConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class UserRestClientBikeService implements UserClientBikeService {

	private final static String ENDPOINT_ADDRESS_PARAMETER = "UserRestClientBikeService.endpointAddress";
    private String endpointAddress;
	
	@Override
    public List<UserClientBikeDto> findBikes(String keywords) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "bikes?keywords="
                            + URLEncoder.encode(keywords, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonUserClientBikeDtoConversor.toClientBikeDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
	
	@Override
	public List<UserClientBikeDto> findBikes(String keywords, Calendar startDate) {

        try {
        	
        	String strStartDate = calendarToString(startDate);

            HttpResponse response = Request.Get(getEndpointAddress() + "bikes?keywords="
                            + URLEncoder.encode(keywords, "UTF-8") + "&startDate=" 
                            + URLEncoder.encode(strStartDate, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonUserClientBikeDtoConversor.toClientBikeDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long rentBike(UserClientBookDto book)
            throws InstanceNotFoundException, InputValidationException {

    	String initDate;
    	String endDate;
    	Calendar iDate;
    	Calendar eDate;
    	
        try {
        	
        	iDate = book.getStartDate();
        	eDate = book.getEndDate();
        	initDate = calendarToString(iDate);
        	endDate = calendarToString(eDate);
        	
            HttpResponse response = Request.Post(getEndpointAddress() + "books/").
            		bodyForm(
                            Form.form().
                            add("bikeId", Long.toString(book.getBikeId())).
                            add("email", book.getEmail()).
                            add("creditCard", book.getCreditCard()).
                            add("initDate", initDate).
                            add("endDate", endDate).
                            add("numberBikes", Integer.toString(book.getUnits())).
                            build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonUserClientBookDtoConversor.toClientBookDto(
                    response.getEntity().getContent()).getBookId();

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    public void rateBook(Long bookId, String email, int rating) throws InstanceNotFoundException,
    		InputValidationException {
    		
    	try {
    		UserClientBookDto book = new UserClientBookDto(bookId, email, rating);
    	
    		HttpResponse response = Request.Put(getEndpointAddress() + "books/" 
    				+ Long.toString(bookId)).bodyStream(toInputStream(book), ContentType.create("application/json"))
    				.execute().returnResponse();
    		
    		validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
    		
    		return;
    
    	} catch (InputValidationException | InstanceNotFoundException e) {
    		throw e;
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    
    }
    
    public List<UserClientBookDto> findBooks(String email) {
		
    	try {
    		HttpResponse response = Request.Get(getEndpointAddress() + "books?email=" + 
    				URLEncoder.encode(email, "UTF-8")).
    				execute().returnResponse();
		
		
			validateStatusCode(HttpStatus.SC_OK, response);
			
			return JsonUserClientBookDtoConversor.toClientBookDtos(
                    response.getEntity().getContent());
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    	
    }
    
    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }
    
    private String calendarToString(Calendar date) {
    	return Integer.toString(date.get(Calendar.DAY_OF_MONTH)) + "-" +
				Integer.toString(date.get(Calendar.MONTH) + 1) + "-" +
				Integer.toString(date.get(Calendar.YEAR));
    }

    private InputStream toInputStream(UserClientBookDto book) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).
            	writeValue(outputStream, JsonUserClientBookDtoConversor.toRateJsonObject(book));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, HttpResponse response)
            throws InstanceNotFoundException, ClientBookExpirationException,
            InputValidationException, ParsingException {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonClientExceptionConversor.fromInstanceNotFoundException(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                	if (exceptionType(response.getEntity().getContent()).equalsIgnoreCase("inputValidationException")) {
                			throw JsonClientExceptionConversor.fromInputValidationException(
                					response.getEntity().getContent());
                	} else {
                		throw JsonClientExceptionConversor.fromException(response.getEntity().getContent());
                	}
                case HttpStatus.SC_GONE:
                    throw JsonClientExceptionConversor.fromBookExpirationException(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    
    private String exceptionType(InputStream Exception)
	            throws ParsingException {
	        try {

	        	ObjectMapper objectMapper = ObjectMapperFactory.instance();
				JsonNode rootNode = objectMapper.readTree(Exception);
				if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
					throw new ParsingException("Unrecognized JSON (object expected)");
				} else {
					String type = rootNode.get(0).asText();
			    	return type;
				}
	        } catch (Exception e) {
	            throw new ParsingException(e);
	        }
    }
	
}
