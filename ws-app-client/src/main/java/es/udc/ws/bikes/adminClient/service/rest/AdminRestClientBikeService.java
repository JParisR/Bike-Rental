package es.udc.ws.bikes.adminClient.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
//import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.ws.bikes.adminClient.service.*;
import es.udc.ws.bikes.adminClient.service.dto.*;
import es.udc.ws.bikes.adminClient.service.rest.json.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.bikes.client.service.exceptions.ClientInvalidStartDateException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

public class AdminRestClientBikeService implements AdminClientBikeService{

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientBikeService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addBike(AdminClientBikeDto Bike) throws InputValidationException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "Bikes").
                    bodyStream(toInputStream(Bike), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonAdminClientBikeDtoConversor.toAdminClientBikeDto(response.getEntity().getContent()).getBikeId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateBike(AdminClientBikeDto Bike) throws InputValidationException,
            InstanceNotFoundException {

        try {

            HttpResponse response = Request.Put(getEndpointAddress() + "Bikes/" + Bike.getBikeId()).
                    bodyStream(toInputStream(Bike), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<AdminClientBikeDto> findBikes(String bikeId) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "Bikes?bikeId="
                            + URLEncoder.encode(bikeId, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonAdminClientBikeDtoConversor.toAdminClientBikeDtos(response.getEntity()
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

    private InputStream toInputStream(AdminClientBikeDto Bike) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream, JsonAdminClientBikeDtoConversor.toJsonObject(Bike));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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
                    throw JsonAdminClientExceptionConversor.fromInstanceNotFoundException(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonAdminClientExceptionConversor.fromInputValidationException(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                    throw JsonAdminClientExceptionConversor.fromInvalidStartDateException(
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
