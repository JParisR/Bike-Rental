package es.udc.ws.bikes.model.bikesservice;

import static org.junit.Assert.*;
import org.junit.Test;
import static es.udc.ws.bikes.model.util.ModelConstants.BASE_URL;
import static es.udc.ws.bikes.model.util.ModelConstants.MAX_PRICE;
import static es.udc.ws.bikes.model.util.ModelConstants.MAX_BOOK_DAYS;
import static es.udc.ws.bikes.model.util.ModelConstants.BIKE_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikesservice.BikeService;
import es.udc.ws.bikes.model.bikesservice.BikeServiceFactory;
import es.udc.ws.bikes.model.book.Book;
import es.udc.ws.bikes.model.book.SqlBookDao;
import es.udc.ws.bikes.model.book.SqlBookDaoFactory;
import es.udc.ws.movies.model.movieservice.MovieServiceFactory;
import es.udc.ws.movies.model.sale.SqlSaleDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import es.udc.ws.bikes.model.bikesservice.exceptions.InvalidNumberOfBikesException;
import es.udc.ws.bikes.model.bikesservice.exceptions.InvalidStartDateException;;

public class BikeServiceTest {
	private final long NON_EXISTENT_BIKEID = -1;
	private final long NON_EXISTENT_SALE_ID = -1;
	private final String USER_ID = "ws-user";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD_NUMBER = "";

	private static BikeService bikeService = null;

	private static SqlBookDao bookDao = null;
	@BeforeClass
	public static void init() {
		DataSource datasource = new SimpleDataSource();
		
		bikeService = BikeServiceFactory.getService();

		bookDao = SqlBookDaoFactory.getDao();
	}
	
	@Test
	public void testAddBike() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateBike() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveBike() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindBike() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindMovies() {
		fail("Not yet implemented");
	}

	@Test
	public void testBookBike() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindBook() {
		fail("Not yet implemented");
	}

}
