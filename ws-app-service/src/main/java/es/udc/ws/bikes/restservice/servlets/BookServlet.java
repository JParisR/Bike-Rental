package es.udc.ws.bikes.restservice.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.*;
import java.text.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.bikes.model.book.Book;
import es.udc.ws.bikes.serviceutil.BookToBookDtoConversor;
import es.udc.ws.bikes.dto.ServiceBookDto;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.bikeservice.exceptions.*;
import es.udc.ws.bikes.restservice.json.JsonServiceExceptionConversor;
import es.udc.ws.bikes.restservice.json.JsonServiceBookDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class BookServlet extends HttpServlet{

	 @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String path = ServletUtils.normalizePath(req.getPathInfo());
			if (path != null && path.length() > 0) {
				ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
						JsonServiceExceptionConversor.toInputValidationException(
								new InputValidationException("Invalid Request: " + "invalid path " + path)),
						null);
				return;
			}
	        String bikeIdParameter = req.getParameter("bikeId");
	        if (bikeIdParameter == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(
	                            new InputValidationException("Invalid Request: " + "parameter 'bikeId' is mandatory")),
	                    null);
	            return;
	        }
	        Long bikeId;
	        try {
	            bikeId = Long.valueOf(bikeIdParameter);
	        } catch (NumberFormatException ex) {
	            ServletUtils
	                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                                    "Invalid Request: " + "parameter 'bikeId' is invalid '" + bikeIdParameter + "'")),
	                            null);

	            return;
	        }
	        String email = req.getParameter("email");
	        if (email == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(
	                            new InputValidationException("Invalid Request: " + "parameter 'email' is mandatory")),
	                    null);
	            return;
	        }
	        String creditCard = req.getParameter("creditCard");
	        if (creditCard == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                            "Invalid Request: " + "parameter 'creditCard' is mandatory")),
	                    null);

	            return;
	        }
	        
	        Calendar initDate;
	        String initDateString = req.getParameter("initDate");	        
	        if (initDateString == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                            "Invalid Request: " + "parameter 'initDate' is mandatory")),
	                    null);

	            return;
	        } else {
	        	initDate = null;
	        	Date date1=null;
				try {
					date1 = new SimpleDateFormat("dd/MM/yyyy").parse(initDateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	initDate = Calendar.getInstance();
	        	initDate.setTime(date1);
	        }
	        
	        Calendar endDate;
	        String endDateString = req.getParameter("endDate");	        
	        if (endDateString == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                            "Invalid Request: " + "parameter 'endDate' is mandatory")),
	                    null);

	            return;
	        } else {
	        	endDate = null;
	        	Date date2=null;
				try {
					date2 = new SimpleDateFormat("dd/MM/yyyy").parse(endDateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	endDate = Calendar.getInstance();
	        	endDate.setTime(date2);
	        }
	        
	        int numberBikes;
	        String numberBikesString = req.getParameter("numberBikes");
	        if (numberBikesString == null) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                            "Invalid Request: " + "parameter 'numberBikes' is mandatory")),
	                    null);

	            return;
	        } else {
	        	numberBikes = Integer.parseInt(numberBikesString);
	        }
	        
	        int bookRate;
	        String bookRateString = req.getParameter("numberBikes");
	        if (bookRateString == null) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                            "Invalid Request: " + "parameter 'numberBikes' is mandatory")),
	                    null);

	            return;
	        } else {
	        	bookRate = Integer.parseInt(bookRateString);
	        }
	        
	        Calendar bookDate;
	        String bookDateString = req.getParameter("bookDate");	        
	        if (bookDateString == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(new InputValidationException(
	                            "Invalid Request: " + "parameter 'bookDate' is mandatory")),
	                    null);

	            return;
	        } else {
	        	bookDate = null;
	        	Date date3=null;
				try {
					date3 = new SimpleDateFormat("dd/MM/yyyy").parse(bookDateString);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	bookDate = Calendar.getInstance();
	        	bookDate.setTime(date3);
	        }
	        
	        Book book = null;
	        Book bookBike = null;
	        try {
	        	
	        	//Long bikeId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes, Calendar bookDate
	            bookBike = new Book(bikeId, email, creditCard, initDate, endDate, numberBikes, bookRate, bookDate);
	        	book = BikeServiceFactory.getService().bookBike(bookBike);
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	            		JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	            return;
	        } catch (InputValidationException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(ex), null);
	            return;
	        } catch (InvalidStartDateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidNumberOfBikesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidDaysOfBookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        ServiceBookDto bookDto = BookToBookDtoConversor.toBookDto(book);

	        String bookURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + book.getBookId().toString();

	        Map<String, String> headers = new HashMap<>(1);
	        headers.put("Location", bookURL);

	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
	                JsonServiceBookDtoConversor.toJsonObject(bookDto), headers);
	    }

	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        String path = ServletUtils.normalizePath(req.getPathInfo());
	        if (path == null || path.length() == 0) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(
	                            new InputValidationException("Invalid Request: " + "invalid book id")),
	                    null);
	            return;
	        }
	        String bookIdAsString = path.substring(1);
	        Long bookId;
	        try {
	            bookId = Long.valueOf(bookIdAsString);
	        } catch (NumberFormatException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	            		JsonServiceExceptionConversor.toInputValidationException(
	                            new InputValidationException("Invalid Request: " + "invalid book id '" + bookIdAsString)),
	                    null);
	            return;
	        }
	        Book book;
	        try {
	            book = BikeServiceFactory.getService().findBook(bookId);
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	            		JsonServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	            return;
	        } 

	        ServiceBookDto bookDto = BookToBookDtoConversor.toBookDto(book);

	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
	                JsonServiceBookDtoConversor.toJsonObject(bookDto), null);

	    }
	
}
