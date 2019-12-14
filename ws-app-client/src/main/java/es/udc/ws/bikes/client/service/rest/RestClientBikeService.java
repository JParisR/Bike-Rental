package es.udc.ws.bikes.client.service.rest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import es.udc.ws.bikes.client.service.dto.*;
import es.udc.ws.bikes.client.service.ClientBikeService;
import es.udc.ws.bikes.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonClientBikeDtoConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonClientBookDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.bikes.client.service.exceptions.ClientInvalidStartDateException;
import es.udc.ws.util.json.exceptions.ParsingException;

public class RestClientBikeService implements ClientBikeService{

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeService.endpointAddress";
    private String endpointAddress;

    @Override
    public List<ClientBikeDto> findBikes(String keywords) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "Bikes?keywords="
                            + URLEncoder.encode(keywords, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonClientBikeDtoConversor.toClientBikeDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public List<ClientBookDto> findBooks(String email) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "Books?email="
                            + URLEncoder.encode(email, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonClientBookDtoConversor.toClientBookDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long bookBike(Long BikeId, String userId, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "Books").
                    bodyForm(
                            Form.form().
                            add("BikeId", Long.toString(BikeId)).
                            add("userId", userId).
                            add("creditCardNumber", creditCardNumber).
                            build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonClientBookDtoConversor.toClientBookDto(
                    response.getEntity().getContent()).getBookId();

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long rateBook(Long BookId, String userId)
            throws InstanceNotFoundException, InputValidationException {
    	
    	
    	return null;
    }
    
    
    
    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private void validateStatusCode(int successCode, HttpResponse response)
            throws InstanceNotFoundException, ClientInvalidStartDateException,
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
                    throw JsonClientExceptionConversor.fromInputValidationException(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                    throw JsonClientExceptionConversor.fromInvalidStartDateException(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
