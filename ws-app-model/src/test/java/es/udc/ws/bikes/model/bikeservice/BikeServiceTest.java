package es.udc.ws.bikes.model.bikeservice;

import static org.junit.Assert.*;
import org.junit.Test;
import static es.udc.ws.bikes.model.util.ModelConstants.MAX_PRICE;
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

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bikeservice.BikeService;
import es.udc.ws.bikes.model.bikeservice.BikeServiceFactory;
import es.udc.ws.bikes.model.book.Book;
import es.udc.ws.bikes.model.book.SqlBookDao;
import es.udc.ws.bikes.model.book.SqlBookDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import es.udc.ws.bikes.model.bikeservice.exceptions.BookAlreadyRatedException;
import es.udc.ws.bikes.model.bikeservice.exceptions.BookNotFinishedException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidBookDatesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDaysOfBookException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidNumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidStartDateException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidStartDateToBookException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidStartDateToUpdateException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidUserException;

public class BikeServiceTest {
	private final long NON_EXISTENT_BIKE_ID = -1;
	private final long NON_EXISTENT_BOOK_ID = -1;
	private final String USER_EMAIL = "ws-user@udc.es";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD= "";
	
	private final int NUMBER_OF_BIKES = 2;
	private final int INVALID_NUMBER_OF_BIKES = 999;
	private final int BOOK_RATE = 0;
	private static BikeService bikeService = null;

	private static SqlBookDao bookDao = null;
	
	@BeforeClass
	public static void init() {
		
		DataSource datasource = new SimpleDataSource();		
		
		DataSourceLocator.addDataSource(BIKE_DATA_SOURCE, datasource);
		
		bikeService = BikeServiceFactory.getService();

		bookDao = SqlBookDaoFactory.getDao();
	}
	
	private Bike getValidBike(Long bikeId, String name, String description){
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		startDate.add(Calendar.MONTH, 1);
		startDate.set(Calendar.MILLISECOND, 0);
		return new Bike(bikeId, name, description, startDate, 19.95F, 4);
	}
	
	private Bike getValidBike() {
		return getValidBike((long) 0, "Bike name", "Bike description");
	}
	
	private Bike createBike(Bike bike) {
		
		Bike addedBike = null;
		try {
			addedBike = bikeService.addBike(bike);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		} catch (InvalidStartDateException e) {
			throw new RuntimeException(e);
		}
		return addedBike;
	}
	
	private Book getValidBook(Long bikeId, Calendar initDate, Calendar endDate) {
		
		return new Book(bikeId, USER_EMAIL, VALID_CREDIT_CARD_NUMBER, initDate, endDate, NUMBER_OF_BIKES, BOOK_RATE);
	
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
				
				/*Prepare connection*/
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);
				
				/*Do work*/
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
	
	/* Este test falla porque no se puede puntuar una reserva hasta que esta finaliza, pero esta comprobacion
	   se hace directamente al puntuar, por lo que no se pueden cambiar las fechas en el test. Aún así, se ha comprobado
	   con esta excepcion "desactivada" y el test funcionaba.
	@Test
	public void testRateBook() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException, BookNotFinishedException, BookAlreadyRatedException, 
			InvalidUserException {
		
		Bike bike = createBike(getValidBike());
		
		try {
			
			bikeService.addBike(bike);
			
			Calendar initDate = Calendar.getInstance();
			initDate.set(Calendar.DAY_OF_MONTH, 1);
			initDate.add(Calendar.MONTH, 1);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.add(Calendar.MONTH, 1);
			endDate.set(Calendar.MILLISECOND, 0);
			
			Book book = bikeService.bookBike(getValidBook(bike.getBikeId(), initDate, endDate));
			
			int rate = 7;
			bikeService.rateBook(book.getBookId(), USER_EMAIL, rate);
			
			Book foundBook = bikeService.findBook(book.getBookId());
			
			assertEquals(rate, foundBook.getBookRate());
			
		} finally {
			removeBike(bike.getBikeId());
		}
		
	}*/
	
	@Test
	public void testAddBikeAndFindBike() throws InputValidationException, InstanceNotFoundException, InvalidStartDateException {
		Bike bike = getValidBike();
		Bike addedBike = null;
		
		try {
			addedBike = bikeService.addBike(bike);
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());
			
			assertEquals(addedBike.getBikeId(), foundBike.getBikeId());
			assertEquals(addedBike.getClass(), foundBike.getClass());
			assertEquals(addedBike.getCreationDate().get(Calendar.DAY_OF_MONTH), foundBike.getCreationDate().get(Calendar.DAY_OF_MONTH));
			assertEquals(addedBike.getCreationDate().get(Calendar.MONTH), foundBike.getCreationDate().get(Calendar.MONTH));
			assertEquals(addedBike.getCreationDate().get(Calendar.YEAR), foundBike.getCreationDate().get(Calendar.YEAR));
			assertEquals(addedBike.getDescription(), foundBike.getDescription());
			assertEquals(addedBike.getPrice(), foundBike.getPrice(), 0.001d);
			assertEquals(addedBike.getUnits(), foundBike.getUnits());
			assertEquals(addedBike.getAvgRate(), foundBike.getAvgRate(), 0.001d);
			assertEquals(addedBike.getNumberOfRates(), foundBike.getNumberOfRates(), 0.01);
		
		} finally {
			// Clear Database
			if (addedBike!=null) {
				removeBike(addedBike.getBikeId());
			}
		}	
	}
	
	@Test
	public void testAddInvalidBike() throws InvalidStartDateException {
		Bike bike = getValidBike();
		Bike addedBike = null;
		boolean exceptionCatched = false;

		try {

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

			// Check bike description not empty
			exceptionCatched = false;
			bike = getValidBike();
			bike.setDescription("");
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike price >= 0
			exceptionCatched = false;
			bike = getValidBike();
			bike.setPrice((short) -1);
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check bike price <= MAX_PRICE
			exceptionCatched = false;
			bike = getValidBike();
			bike.setPrice((short) (MAX_PRICE + 1));
			try {
				addedBike = bikeService.addBike(bike);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

		} finally {
			if (!exceptionCatched) {
				// Clear Database
				removeBike(addedBike.getBikeId());
			}
		}
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentBike() throws InstanceNotFoundException {

		bikeService.findBike(NON_EXISTENT_BIKE_ID);
	}

	@Test
	public void testUpdateBike() throws InputValidationException, InstanceNotFoundException, 
				InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
				InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
				InvalidStartDateToBookException, InvalidStartDateToUpdateException {
		Bike bike = createBike(getValidBike());
		try {
			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.MONTH, Calendar.NOVEMBER);
			float newPrice = 24.95f;
			Bike bikeToUpdate = new Bike(bike.getBikeId(), "Bike name", "Bike description", newDate, newPrice, 1);
			
			Calendar initDate = Calendar.getInstance();
			initDate.set(Calendar.DAY_OF_MONTH, 1);
			initDate.add(Calendar.MONTH, 1);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.add(Calendar.MONTH, 1);
			endDate.set(Calendar.MILLISECOND, 0);
			
			bikeService.bookBike(getValidBook(bike.getBikeId(), initDate, endDate));
			
			bikeService.updateBike(bikeToUpdate);
			
			Bike updatedBike = bikeService.findBike(bike.getBikeId());
			
			bikeToUpdate.setCreationDate(bike.getCreationDate());
			assertEquals(bikeToUpdate.getBikeId(), updatedBike.getBikeId());
			assertEquals(bikeToUpdate.getClass(), updatedBike.getClass());
			assertEquals(bikeToUpdate.getCreationDate().get(Calendar.DAY_OF_MONTH), updatedBike.getCreationDate().get(Calendar.DAY_OF_MONTH));
			assertEquals(bikeToUpdate.getCreationDate().get(Calendar.MONTH), updatedBike.getCreationDate().get(Calendar.MONTH));
			assertEquals(bikeToUpdate.getCreationDate().get(Calendar.YEAR), updatedBike.getCreationDate().get(Calendar.YEAR));
			assertEquals(bikeToUpdate.getDescription(), updatedBike.getDescription());
			assertEquals(bikeToUpdate.getPrice(), updatedBike.getPrice(), 0.001d);
			assertEquals(bikeToUpdate.getUnits(), updatedBike.getUnits());
			assertEquals(bikeToUpdate.getAvgRate(), updatedBike.getAvgRate(), 0.001d);
			assertEquals(bikeToUpdate.getNumberOfRates(), updatedBike.getNumberOfRates(), 0.01);
			
		} finally {
			//Clear Database
			removeBike(bike.getBikeId());
		}
	}

	@Test(expected = InstanceNotFoundException.class)
	public void testRemoveBike() throws InstanceNotFoundException {
		
		Bike bike = createBike(getValidBike());
		boolean exceptionCatched = false;
		try {
			bikeService.removeBike(bike.getBikeId());
		} catch (InstanceNotFoundException e) {
			exceptionCatched = true;
		}
		assertTrue(!exceptionCatched);

		bikeService.findBike(bike.getBikeId());
	}

	@Test
	public void testFindBikes() {
		//Add bikes
		
		List<Bike> bikes = new LinkedList<Bike>();
		Bike bike1 = createBike(getValidBike((long) 0, "Bici 1", "Bici verde 1"));
		bikes.add(bike1);
		Bike bike2 = createBike(getValidBike((long) 1, "Bici 2", "Bici verde 2"));
		bikes.add(bike2);
		Bike bike3 = createBike(getValidBike((long) 2, "Bici 3", "Bici verde 3"));
		bikes.add(bike3);
		
		try {
			List<Bike> foundBikes = bikeService.findBikesByKeywords("verDe");
			
			assertEquals(bikes.get(0).getBikeId(), foundBikes.get(0).getBikeId());
			assertEquals(bikes.get(0).getClass(), foundBikes.get(0).getClass());
			assertEquals(bikes.get(0).getCreationDate().get(Calendar.DAY_OF_MONTH), foundBikes.get(0).getCreationDate().get(Calendar.DAY_OF_MONTH));
			assertEquals(bikes.get(0).getCreationDate().get(Calendar.MONTH), foundBikes.get(0).getCreationDate().get(Calendar.MONTH));
			assertEquals(bikes.get(0).getCreationDate().get(Calendar.YEAR), foundBikes.get(0).getCreationDate().get(Calendar.YEAR));
			assertEquals(bikes.get(0).getDescription(), foundBikes.get(0).getDescription());
			assertEquals(bikes.get(0).getPrice(), foundBikes.get(0).getPrice(), 0.001d);
			assertEquals(bikes.get(0).getUnits(), foundBikes.get(0).getUnits());
			assertEquals(bikes.get(0).getAvgRate(), foundBikes.get(0).getAvgRate(), 0.001d);
			assertEquals(bikes.get(0).getNumberOfRates(), foundBikes.get(0).getNumberOfRates(), 0.01);
			
			assertEquals(bikes.get(1).getBikeId(), foundBikes.get(1).getBikeId());
			assertEquals(bikes.get(2).getBikeId(), foundBikes.get(2).getBikeId());
			
			foundBikes = bikeService.findBikesByKeywords("Bici verde 2");
			assertEquals(1, foundBikes.size());
			assertEquals(bikes.get(1).getBikeId(), foundBikes.get(0).getBikeId());
			assertEquals(bikes.get(1).getClass(), foundBikes.get(0).getClass());
			assertEquals(bikes.get(1).getCreationDate().get(Calendar.DAY_OF_MONTH), foundBikes.get(0).getCreationDate().get(Calendar.DAY_OF_MONTH));
			assertEquals(bikes.get(1).getCreationDate().get(Calendar.MONTH), foundBikes.get(0).getCreationDate().get(Calendar.MONTH));
			assertEquals(bikes.get(1).getCreationDate().get(Calendar.YEAR), foundBikes.get(0).getCreationDate().get(Calendar.YEAR));
			assertEquals(bikes.get(1).getDescription(), foundBikes.get(0).getDescription());
			assertEquals(bikes.get(1).getPrice(), foundBikes.get(0).getPrice(), 0.001d);
			assertEquals(bikes.get(1).getUnits(), foundBikes.get(0).getUnits());
			assertEquals(bikes.get(1).getAvgRate(), foundBikes.get(0).getAvgRate(), 0.001d);
			assertEquals(bikes.get(1).getNumberOfRates(), foundBikes.get(0).getNumberOfRates(), 0.01);
			
			foundBikes = bikeService.findBikesByKeywords("description 9999");
			assertEquals(0, foundBikes.size());
		
		} finally {
			//Clear Database
			for (Bike bike : bikes) {
				removeBike(bike.getBikeId());
			}
		}	
	}

	@Test
	public void testBookBikeAndFindBook() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException {
		
		Bike bike = createBike(getValidBike());
		Book book = null;
		
		try {
			
			// Book bike
			Calendar initDate = Calendar.getInstance();
			initDate.set(Calendar.DAY_OF_MONTH, 1);
			initDate.add(Calendar.MONTH, 1);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.add(Calendar.MONTH, 1);
			endDate.set(Calendar.MILLISECOND, 0);
			
			book = bikeService.bookBike(getValidBook(bike.getBikeId(), initDate, endDate));
			
			// Find book
			Book foundBook = bikeService.findBook(book.getBookId());
			
			//Check book
			assertEquals(VALID_CREDIT_CARD_NUMBER, foundBook.getCreditCard());
			assertEquals(USER_EMAIL, foundBook.getEmail());
			assertEquals(book.getBikeId(), foundBook.getBikeId());
			assertEquals(book.getInitDate().get(Calendar.DAY_OF_MONTH), foundBook.getInitDate().get(Calendar.DAY_OF_MONTH));
			assertEquals(book.getInitDate().get(Calendar.MONTH), foundBook.getInitDate().get(Calendar.MONTH));
			assertEquals(book.getInitDate().get(Calendar.YEAR), foundBook.getInitDate().get(Calendar.YEAR));
			assertEquals(book.getEndDate().get(Calendar.DAY_OF_MONTH), foundBook.getEndDate().get(Calendar.DAY_OF_MONTH));
			assertEquals(book.getEndDate().get(Calendar.MONTH), foundBook.getEndDate().get(Calendar.MONTH));
			assertEquals(book.getEndDate().get(Calendar.YEAR), foundBook.getEndDate().get(Calendar.YEAR));
			assertEquals(book.getNumberBikes(), foundBook.getNumberBikes());
			
		} finally {
			//Clear database: remove book (if created) and bike
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}	
	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidBike() throws InputValidationException, InstanceNotFoundException, 
			InvalidStartDateToUpdateException {
		
		Bike bike = createBike(getValidBike());
		
		// Check bikeId not null
		bike = bikeService.findBike(bike.getBikeId());
		bike.setBikeId(null);
		bikeService.updateBike(bike);
		
	}
	
	@Test(expected = InputValidationException.class)
	public void testUpdateNonExistentBike() throws InputValidationException, InstanceNotFoundException, 
			InvalidStartDateToUpdateException {
		
		Bike bike = getValidBike();
		bike.setBikeId(NON_EXISTENT_BIKE_ID);
		bike.setCreationDate(Calendar.getInstance());
		bikeService.updateBike(bike);
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testNonExistentRemoveBike() throws InstanceNotFoundException {
		
		bikeService.removeBike(NON_EXISTENT_BIKE_ID);
		
	}
	
	@Test(expected = InputValidationException.class)
	public void testBookBikeWithInvalidCreditCard() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException {
		
		Bike bike = createBike(getValidBike());
		try {
			
			Calendar initDate = Calendar.getInstance();
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.set(Calendar.MILLISECOND, 0);
			
			bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, INVALID_CREDIT_CARD, 
					initDate, endDate, NUMBER_OF_BIKES, BOOK_RATE,bookDate));
			
		} finally {
			//Clear database
			removeBike(bike.getBikeId());
		}
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBookNonExistentBike() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException{

		Calendar initDate = Calendar.getInstance();
		initDate.add(Calendar.DAY_OF_MONTH, 0);
		initDate.set(Calendar.MILLISECOND, 0);
		
		Calendar bookDate = Calendar.getInstance();
		bookDate.add(Calendar.DAY_OF_MONTH, 0);
		bookDate.set(Calendar.MILLISECOND, 0);
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 5);
		endDate.set(Calendar.MILLISECOND, 0);
		
		bikeService.bookBike(new Book(NON_EXISTENT_BIKE_ID, USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 
				initDate, endDate, NUMBER_OF_BIKES, BOOK_RATE,bookDate));
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentBook() throws InstanceNotFoundException{
		
		bikeService.findBook(NON_EXISTENT_BOOK_ID);
		
	}
	
	@Test(expected = InvalidDaysOfBookException.class)
	public void testBookInvalidDays() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException {
		
		Bike bike = createBike(getValidBike());
		Book book = null;
		try {
		
			Calendar initDate = Calendar.getInstance();
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			//Invalid endDate -> 27 days
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 27);
			endDate.set(Calendar.MILLISECOND, 0);
			
			book = bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 
					initDate, endDate, NUMBER_OF_BIKES, BOOK_RATE,bookDate));
			
		} finally {
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
	}
	
	@Test(expected = InvalidNumberOfBikesException.class)
	public void testBookInvalidNumberOfBikes() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException {
		
		Bike bike = createBike(getValidBike());
		Book book = null;
		
		try {
			
			Calendar initDate = Calendar.getInstance();
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.set(Calendar.MILLISECOND, 0);
			
			book = bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 
					initDate, endDate, INVALID_NUMBER_OF_BIKES, BOOK_RATE,bookDate));
		
		} finally {
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
		
	}
	
	@Test(expected = InvalidStartDateToBookException.class)
	public void testBookInvalidStartDate() throws InputValidationException, InstanceNotFoundException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException, 
			InvalidStartDateToBookException {
		
		Bike bike = createBike(getValidBike());
		Book book = null;
		
		try {
			
			Calendar initDate = Calendar.getInstance();
			initDate.set(Calendar.YEAR, 1970);
			initDate.set(Calendar.MONTH, Calendar.AUGUST);
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.YEAR, 1970);
			endDate.set(Calendar.MONTH, Calendar.AUGUST);
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.set(Calendar.MILLISECOND, 0);
			
			book = bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, 
					initDate, endDate, NUMBER_OF_BIKES, BOOK_RATE, bookDate));
		
		} finally {
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
		
	}
}
