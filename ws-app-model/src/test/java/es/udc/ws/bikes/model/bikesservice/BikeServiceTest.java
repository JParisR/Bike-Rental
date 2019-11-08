package es.udc.ws.bikes.model.bikesservice;

import static org.junit.Assert.*;
import org.junit.Test;
import static es.udc.ws.bikes.model.util.ModelConstants.BASE_URL;
import static es.udc.ws.bikes.model.util.ModelConstants.MAX_PRICE;
import static es.udc.ws.movies.model.util.ModelConstants.MAX_PRICE;
import static es.udc.ws.movies.model.util.ModelConstants.MAX_RUNTIME;
import static es.udc.ws.movies.model.util.ModelConstants.MOVIE_DATA_SOURCE;
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
import es.udc.ws.movies.model.movie.Movie;
import es.udc.ws.movies.model.sale.Sale;
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
	
	private Bike getValidBike(Long bikeId){
		return new Bike(bikeId , "Bike description", Calendar.getInstance() , 19.95F,  1, Calendar.getInstance());
	}
	
	private Bike getValidBike() {
		return getValidBike((long) 0);
	}
	
	private Bike createBike(Bike bike) {
		
		Bike addedBike = null;
		try {
			addedBike = bikeService.addBike(bike);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedBike;
	}
	
	private void removeBike(Long bikeId) {
	
		try {
			bikeService.removeBike(bikeId);
		} catch (InstanceNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void removeBook(Long bookId) {
		
		DataSource dataSource = DataSourceLocator.getDataSource(BIKE_DATA_SOURCE);
		
		try (Connection connection = dataSource.getConnection()) {
			
			try {
				
				/*Prepara la conexión*/
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				
				/*Elimina la reserva*/
				bookDao.remove(connection, bookId);
				
				/*Commit*/
				connection.commit();
				
			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void updateBook(Book book) {

		DataSource dataSource = DataSourceLocator.getDataSource(BIKE_DATA_SOURCE);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepara la conexión */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Actualiza la reserva*/
				bookDao.update(connection, book);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw new RuntimeException(e);
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	@Test
	public void testAddBikeAndFindBike() throws InputValidationException, InstanceNotFoundException{
		Bike bike = getValidBike();
		Bike addedBike = null;
		
		try {
			addedBike = bikeService.addBike(bike);
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());

			assertEquals(addedBike, foundBike);
		
		} finally {
			// Borramos la bici de prueba
			if (addedBike!=null) {
				removeBike(addedBike.getBikeId());
			}
		}
		
		//fail("Not yet implemented");
	}
	
	@Test
	public void testAddInvalidBike() {
		Bike bike = getValidBike();
		Bike addedBike = null;
		boolean exceptionCatched = false;

		try {
			// Check bikeId not null
			bike.setBikeId(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike description not null
			exceptionCatched = false;
			bike = getValidBike();
			bike.setDescription(null);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check movie description not empty
			exceptionCatched = false;
			bike = getValidBike();
			bike.setDescription("");
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check movie price >= 0
			exceptionCatched = false;
			movie = getValidMovie();
			movie.setPrice((short) -1);
			try {
				addedMovie = movieService.addMovie(movie);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check movie price <= MAX_PRICE
			exceptionCatched = false;
			movie = getValidMovie();
			movie.setRuntime((short) (MAX_PRICE + 1));
			try {
				addedMovie = movieService.addMovie(movie);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			if (!exceptionCatched) {
				// Clear Database
				removeMovie(addedMovie.getMovieId());
			}
		}
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
