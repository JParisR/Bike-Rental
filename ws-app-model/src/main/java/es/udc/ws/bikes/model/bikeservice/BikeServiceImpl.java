package es.udc.ws.bikes.model.bikeservice;

import static es.udc.ws.bikes.model.util.ModelConstants.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bike.SqlBikeDao;
import es.udc.ws.bikes.model.bike.SqlBikeDaoFactory;
import es.udc.ws.bikes.model.book.SqlBookDao;
import es.udc.ws.bikes.model.book.SqlBookDaoFactory;
import es.udc.ws.bikes.model.book.Book;
import es.udc.ws.bikes.model.bikeservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class BikeServiceImpl implements BikeService{


	private DataSource dataSource = null;
	private SqlBikeDao bikeDao = null;
	private SqlBookDao bookDao = null;

	public BikeServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(BIKE_DATA_SOURCE);
		bikeDao = SqlBikeDaoFactory.getDao();
		bookDao = SqlBookDaoFactory.getDao();
	}

	private void validateBike(Bike bike) throws InputValidationException {
		
		if (bike.getBikeId() == null) {
			throw new InputValidationException("The bikeId can't be null");
		}
		else {
			PropertyValidator.validateLong("bikeId", bike.getBikeId(), 0, MAX_BIKEID);
			PropertyValidator.validateMandatoryString("description", bike.getDescription());
			PropertyValidator.validateDouble("price", bike.getPrice(), 0, MAX_PRICE);
		}
	}

	@Override
	public Bike addBike(Bike bike) throws InputValidationException {

		validateBike(bike);

		try (Connection connection = dataSource.getConnection()) {

			try {
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike createdBike = bikeDao.create(connection, bike);

				/* Commit. */
				connection.commit();

				return createdBike;

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
	
	@Override
	public void updateBike(Bike bike) throws InputValidationException, InstanceNotFoundException, InvalidStartDateException {

		validateBike(bike);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Book bookAux = bookDao.findByBikeId(connection, bike.getBikeId());
				if (bookAux.getInitDate().before(bike.getStartDate()) ) {
					throw new InvalidStartDateException(bookAux.getBikeId(), bookAux.getInitDate()); 
				}
				
				bikeDao.update(connection, bike);
				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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
	
	@Override
	public Bike findBike(Long bikeId) throws InstanceNotFoundException {

		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.find(connection, bikeId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	
	}
	
	@Override
    public void removeBike(Long bikeId) throws InstanceNotFoundException{
		
    	try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				bikeDao.remove(connection, bikeId);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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
    
	
	@Override
	public List<Bike> findBikesByKeywords(String keywords) {

		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.findByKeywords(connection, keywords);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	public List<Bike> findBikesByKeywords(String keywords, Calendar startDate) {
		
		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.findByKeywords(connection, keywords, startDate);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}

    public void rateBook(Long bookId, int rate) throws InstanceNotFoundException,
    		InvalidStartDateException {
    	
    	Calendar actualDate = Calendar.getInstance();
    	
    	try (Connection connection = dataSource.getConnection()) {
    		
    		try {
    			
    			/* Prepare connection */
    			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    			connection.setAutoCommit(false);
    			
    			/* Do work */
    			Book book = bookDao.findByBookId(connection, bookId);
    			Bike bike = bikeDao.find(connection, book.getBikeId());
    			
    			if (bike.getStartDate().after(actualDate)) {
    				throw new InvalidStartDateException(bike.getBikeId(), bike.getStartDate());
    			}
    			
    			book.setBookRate(rate);
    			bookDao.update(connection, book);
    		
    		} catch (SQLException e) {
    			connection.commit();
    			throw e;
    		} catch (InstanceNotFoundException e) {
    			connection.rollback();
    			throw e;
    		}
    		
		} catch (SQLException e) {
    		throw new RuntimeException(e);
    	}
    	
    }
	
	@Override
	public Book bookBike(Long bikeId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes, Calendar bookDate)
			throws InstanceNotFoundException, InputValidationException, InvalidNumberOfBikesException, InvalidDaysOfBookException, InvalidStartDateException {

		PropertyValidator.validateCreditCard(creditCard);
		if((endDate.getTimeInMillis() - initDate.getTimeInMillis()) / (24 * 60 * 60 * 1000) > MAX_BOOK_DAYS) {
			throw new InvalidDaysOfBookException(initDate, endDate);
		}
		
				
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike bike = bikeDao.find(connection, bikeId);
				if (bike.getUnits() < numberBikes) {
					throw new InvalidNumberOfBikesException(bike.getBikeId(), bike.getUnits(),numberBikes);
				}
				else if(bike.getStartDate().after(initDate)){
					throw new InvalidStartDateException(bike.getBikeId(), initDate);
				}
			
				Book book = bookDao.create(connection, new Book(bike.getBikeId(), email, creditCard, initDate,
							endDate, numberBikes, bookDate));
				
				
				/* Commit. */
				connection.commit();

				return book;

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
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
	
	@Override
	public Book findBook(Long bookId) throws InstanceNotFoundException {

		try (Connection connection = dataSource.getConnection()) {

			Book book = bookDao.findByBookId(connection, bookId);
				return book;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public List<Book> findBookByUser(String email) throws InstanceNotFoundException{

		try (Connection connection = dataSource.getConnection()) {
			return bookDao.findByUser(connection, email);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
