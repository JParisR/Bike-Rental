package es.udc.ws.bikes.model.bikesservice;

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
import es.udc.ws.bikes.model.bikesservice.exceptions.InvalidStartDateException;
import es.udc.ws.bikes.model.bikesservice.exceptions.InvalidNumberOfBikesException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class BikeServiceImpl implements BikeService{


	private final DataSource dataSource;
	private SqlBikeDao bikeDao = null;
	private SqlBookDao bookDao = null;

	public BikeServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(BIKE_DATA_SOURCE);
		bikeDao = SqlBikeDaoFactory.getDao();
		bookDao = SqlBookDaoFactory.getDao();
	}

	private void validateBike(Bike bike) throws InputValidationException {

		PropertyValidator.validateMandatoryString("description", bike.getDescription());
		PropertyValidator.validateDouble("price", bike.getPrice(), 0, MAX_PRICE);

	}

	@Override
	public Bike addBike(Bike bike) throws InputValidationException {

		validateBike(bike);
		bike.setCreationDate(Calendar.getInstance());

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
				Bike bikeAux = bikeDao.find(connection, bike.getBikeId()); 
				if (bikeAux.getStartDate().before(bike.getStartDate()) ) {
					throw new InvalidStartDateException(bikeAux.getBikeId(), bikeAux.getStartDate()); 
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
    
	public Bike findBike(Long bikeId) throws InstanceNotFoundException {

		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.find(connection, bikeId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	
	}
	
	@Override
	public List<Bike> findMovies(String keywords) {

		try (Connection connection = dataSource.getConnection()) {
			return bikeDao.findByKeywords(connection, keywords);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public Book bookBike(Long bookId, Long bikeId, String email, String creditCard, Calendar initDate, Calendar endDate, int numberBikes)
			throws InstanceNotFoundException, InputValidationException, InvalidNumberOfBikesException {

		PropertyValidator.validateCreditCard(creditCard);
		
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike bike = bikeDao.find(connection, bikeId);
				if (bike.getUnits() < numberBikes) {
					throw new InvalidNumberOfBikesException(bike.getUnits(),numberBikes);
					
				}
				Book book = bookDao.create(connection, new Book(bookId, bikeId, email, creditCard, initDate,
							endDate, numberBikes, Calendar.getInstance()));

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

			Book book = bookDao.find(connection, bookId);
				return book;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	
}
