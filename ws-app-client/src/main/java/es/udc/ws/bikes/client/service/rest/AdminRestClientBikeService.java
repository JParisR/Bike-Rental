package es.udc.ws.bikes.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.bikes.client.service.AdminClientBikeService;
import es.udc.ws.bikes.client.service.dto.AdminClientBikeDto;
import es.udc.ws.bikes.client.service.exceptions.ClientBookExpirationException;
import es.udc.ws.bikes.client.service.rest.json.JsonClientExceptionConversor;
import es.udc.ws.bikes.client.service.rest.json.JsonAdminClientBikeDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class AdminRestClientBikeService implements AdminClientBikeService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "AdminRestClientBikeService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addBike(AdminClientBikeDto bike) throws InputValidationException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "bikes").
                    bodyStream(toInputStream(bike), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonAdminClientBikeDtoConversor.toClientBikeDto(response.getEntity().getContent()).getBikeId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateBike(AdminClientBikeDto bike) throws InputValidationException,
            InstanceNotFoundException {

        try {

            HttpResponse response = Request.Put(getEndpointAddress() + "bikes/" + bike.getBikeId()).
                    bodyStream(toInputStream(bike), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    
    
    public List<AdminClientBikeDto> findBikesById(Long bikeId) {
    	
    	try {

            HttpResponse response = Request.Get(getEndpointAddress() + "bikes?bikeId=")
                            .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonAdminClientBikeDtoConversor.toClientBikeDtos(response.getEntity()
                    .getContent());

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

    private InputStream toInputStream(AdminClientBikeDto bike) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream, JsonAdminClientBikeDtoConversor.toJsonObject(bike));

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
                    throw JsonClientExceptionConversor.fromInputValidationException(
                            response.getEntity().getContent());

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

}
