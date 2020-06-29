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
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidDaysOfBookException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidNumberOfBikesException;
import es.udc.ws.bikes.model.bikeservice.exceptions.InvalidStartDateException;;

public class BikeServiceTest {
	private final long NON_EXISTENT_BIKE_ID = -1;
	private final long NON_EXISTENT_BOOK_ID = -1;
	private final String USER_EMAIL = "ws-user@udc.es";

	private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
	private final String INVALID_CREDIT_CARD= "";
	
	private final int NUMBER_OF_BIKES = 2;
	private final int INVALID_NUMBER_OF_BIKES = 999;
	private final int BOOK_RATE = 5;
	private static BikeService bikeService = null;

	private static SqlBookDao bookDao = null;
	
	@BeforeClass
	public static void init() {
		
		DataSource datasource = new SimpleDataSource();		
		
		DataSourceLocator.addDataSource(BIKE_DATA_SOURCE, datasource);
		
		bikeService = BikeServiceFactory.getService();

		bookDao = SqlBookDaoFactory.getDao();
	}
	
	private Bike getValidBike(Long bikeId, String description){
		Calendar startDate = Calendar.getInstance();
		startDate.set(Calendar.MILLISECOND, 0);
		Calendar creationDate = Calendar.getInstance();
		creationDate.set(Calendar.MILLISECOND, 0);
		return new Bike(bikeId, description, startDate, 19.95F, 4, creationDate);
	}
	
	private Bike getValidBike() {
		return getValidBike((long) 0, "Bike description");
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
	
	private Book getValidBook(Long bikeId, Calendar initDate, Calendar endDate, Calendar bookDate) {
		
		return new Book(bikeId, USER_EMAIL, VALID_CREDIT_CARD_NUMBER, initDate, endDate, NUMBER_OF_BIKES, bookDate, BOOK_RATE);
	
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
	
	@Test
	public void testAddBikeAndFindBike() throws InputValidationException, InstanceNotFoundException{
		Bike bike = getValidBike();
		Bike addedBike = null;
		
		try {
			addedBike = bikeService.addBike(bike);
			Bike foundBike = bikeService.findBike(addedBike.getBikeId());
			
			assertEquals(addedBike, foundBike);
		
		} finally {
			// Clear Database
			if (addedBike!=null) {
				removeBike(addedBike.getBikeId());
			}
		}
		
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
	public void testUpdateBike() throws InputValidationException, InstanceNotFoundException, InvalidStartDateException, InvalidNumberOfBikesException, 
				InvalidDaysOfBookException {
		Bike bike = createBike(getValidBike());
		try {
			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.MONTH, Calendar.NOVEMBER);
			float newPrice = 24.95f;
			Bike bikeToUpdate = new Bike(bike.getBikeId(), "Bike description", newDate, newPrice, 1, bike.getCreationDate());
			
			Calendar initDate = Calendar.getInstance();
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.set(Calendar.MILLISECOND, 0);
			
			bikeService.bookBike(getValidBook(bike.getBikeId(), initDate, endDate, bookDate));
			
			bikeService.updateBike(bikeToUpdate);
			
			Bike updatedBike = bikeService.findBike(bike.getBikeId());
			
			bikeToUpdate.setCreationDate(bike.getCreationDate());
			assertEquals(bikeToUpdate.getBikeId(), updatedBike.getBikeId());
			assertEquals(bikeToUpdate.getClass(), updatedBike.getClass());
			assertEquals(bikeToUpdate.getCreationDate(), updatedBike.getCreationDate());
			assertEquals(bikeToUpdate.getDescription(), updatedBike.getDescription());
			assertEquals(bikeToUpdate.getPrice(), updatedBike.getPrice(), 0.001d);
			assertEquals(bikeToUpdate.getUnits(), updatedBike.getUnits());
			assertEquals(bikeToUpdate.getAvgRating(), updatedBike.getAvgRating(), 0.001d);
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
		Bike bike1 = createBike(getValidBike((long) 0, "Bici verde 1"));
		bikes.add(bike1);
		Bike bike2 = createBike(getValidBike((long) 1, "Bici verde 2"));
		bikes.add(bike2);
		Bike bike3 = createBike(getValidBike((long) 2, "Bici verde 3"));
		bikes.add(bike3);
		
		try {
			List<Bike> foundBikes = bikeService.findBikesByKeywords("verDe");
			assertEquals(bikes, foundBikes);
			
			foundBikes = bikeService.findBikesByKeywords("Bici verde 2");
			assertEquals(1, foundBikes.size());
			assertEquals(bikes.get(1), foundBikes.get(0));
			
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
	public void testBookBikeAndFindBook() 
			throws InstanceNotFoundException, InputValidationException, InvalidStartDateException, InvalidNumberOfBikesException, InvalidDaysOfBookException{
		
		Bike bike = createBike(getValidBike());
		Book book = null;
		
		try {
			
			// Book bike
			Calendar initDate = Calendar.getInstance();
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.set(Calendar.MILLISECOND, 0);
			
			book = bikeService.bookBike(getValidBook(bike.getBikeId(), initDate, endDate, bookDate));
			
			// Find book
			Book foundBook = bikeService.findBook(book.getBookId());
			
			//Check book
			assertEquals(book, foundBook);
			assertEquals(VALID_CREDIT_CARD_NUMBER, foundBook.getCreditCard());
			assertEquals(USER_EMAIL, foundBook.getEmail());
			assertEquals(book.getBikeId(), foundBook.getBikeId());
			assertEquals(initDate, foundBook.getInitDate());
			assertEquals(endDate, foundBook.getEndDate());
			assertTrue(Calendar.getInstance().after(foundBook.getBookDate()));
			assertEquals(book.getNumberBikes(), foundBook.getNumberBikes());
			assertEquals(book.getBookRate(), foundBook.getBookRate());
			
		} finally {
			//Clear database: remove book (if created) and bike
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
		
	}
	
	@Test
	public void testRateBook() throws InputValidationException, InvalidStartDateException, 
			InstanceNotFoundException, InvalidNumberOfBikesException, InvalidDaysOfBookException {
		
		Bike bike = createBike(getValidBike());
		
		try {
			
			bikeService.addBike(bike);
			
			Calendar initDate = Calendar.getInstance();
			initDate.add(Calendar.DAY_OF_MONTH, 0);
			initDate.set(Calendar.MILLISECOND, 0);
			
			Calendar bookDate = Calendar.getInstance();
			bookDate.add(Calendar.DAY_OF_MONTH, 0);
			bookDate.set(Calendar.MILLISECOND, 0);
			
			Calendar endDate = Calendar.getInstance();
			endDate.add(Calendar.DAY_OF_MONTH, 5);
			endDate.set(Calendar.MILLISECOND, 0);
			
			Book book = bikeService.bookBike(getValidBook(bike.getBikeId(), initDate, endDate, bookDate));
			
			int rate = 7;
			bikeService.rateBook(book.getBookId(), rate);
			
			Book foundBook = bikeService.findBook(book.getBookId());
			
			assertEquals(rate, foundBook.getBookRate());
			
		} finally {
			removeBike(bike.getBikeId());
		}
		
	}

	@Test(expected = InputValidationException.class)
	public void testUpdateInvalidBike() throws InputValidationException, InstanceNotFoundException, InvalidStartDateException{
		
		Bike bike = createBike(getValidBike());
		
		// Check bikeId not null
		bike = bikeService.findBike(bike.getBikeId());
		bike.setBikeId(null);
		bikeService.updateBike(bike);
		
	}
	
	@Test(expected = InputValidationException.class)
	public void testUpdateNonExistentBike() throws InputValidationException, InstanceNotFoundException, InvalidStartDateException{
		
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
	public void testBookBikeWithInvalidCreditCard() throws InputValidationException, InvalidStartDateException, InstanceNotFoundException, 
			InputValidationException, InvalidNumberOfBikesException, InvalidDaysOfBookException {
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
			
			bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, INVALID_CREDIT_CARD, initDate, endDate, NUMBER_OF_BIKES, bookDate));
			
		} finally {
			//Clear database
			removeBike(bike.getBikeId());
		}
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testBookNonExistentBike() throws InputValidationException, InstanceNotFoundException, 
			InvalidStartDateException, InvalidNumberOfBikesException, InvalidDaysOfBookException{

		Calendar initDate = Calendar.getInstance();
		initDate.add(Calendar.DAY_OF_MONTH, 0);
		initDate.set(Calendar.MILLISECOND, 0);
		
		Calendar bookDate = Calendar.getInstance();
		bookDate.add(Calendar.DAY_OF_MONTH, 0);
		bookDate.set(Calendar.MILLISECOND, 0);
		
		Calendar endDate = Calendar.getInstance();
		endDate.add(Calendar.DAY_OF_MONTH, 5);
		endDate.set(Calendar.MILLISECOND, 0);
		
		bikeService.bookBike(new Book(NON_EXISTENT_BIKE_ID, USER_EMAIL, VALID_CREDIT_CARD_NUMBER, initDate, endDate, NUMBER_OF_BIKES, bookDate));
		
	}
	
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentBook() throws InstanceNotFoundException{
		
		bikeService.findBook(NON_EXISTENT_BOOK_ID);
		
	}
	
	@Test(expected = InvalidDaysOfBookException.class)
	public void testBookInvalidDays() throws InputValidationException, InstanceNotFoundException, InvalidDaysOfBookException, InvalidStartDateException,
			InvalidNumberOfBikesException {
		
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
			
			book = bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, initDate, endDate, NUMBER_OF_BIKES, bookDate));
			
		} finally {
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
	}
	
	@Test(expected = InvalidNumberOfBikesException.class)
	public void testBookInvalidNumberOfBikes() throws InputValidationException, InstanceNotFoundException, InvalidStartDateException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException{
		
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
			
			book = bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, initDate, endDate, INVALID_NUMBER_OF_BIKES, bookDate));
		
		} finally {
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
		
	}
	
	@Test(expected = InvalidStartDateException.class)
	public void testBookInvalidStartDate() throws InputValidationException, InstanceNotFoundException, InvalidStartDateException, 
			InvalidNumberOfBikesException, InvalidDaysOfBookException{
		
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
			
			book = bikeService.bookBike(new Book(bike.getBikeId(), USER_EMAIL, VALID_CREDIT_CARD_NUMBER, initDate, endDate, NUMBER_OF_BIKES, bookDate));
		
		} finally {
			if (book != null) {
				removeBook(book.getBookId());
			}
			removeBike(bike.getBikeId());
		}
		
	}
}
