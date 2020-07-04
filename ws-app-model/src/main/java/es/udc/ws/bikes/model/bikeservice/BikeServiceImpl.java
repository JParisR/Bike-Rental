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

	private void validateBike(Bike bike, boolean update) throws InputValidationException {
		
		if (bike.getUnits() < 1) {
			throw new InputValidationException("The units can not be \"" + bike.getUnits() +
					"\". Must be greater than 0.");
		}
		if (bike.getDescription() == "") {
			throw new InputValidationException("The description can not be empty.");
		}
		if (bike.getName() == "") {
			throw new InputValidationException("The name can not be empty.");
		}
		if (bike.getPrice() <= 0) {
			throw new InputValidationException("The price can not be \"" + bike.getUnits() +
					"\". Must be greater than 0.");
		}
		
		if (update && bike.getBikeId() == null) {
			throw new InputValidationException("The bikeId can't be null.");
		}
		else if (update) {
			PropertyValidator.validateLong("bikeId", bike.getBikeId(), 0, MAX_BIKEID);
		}
		PropertyValidator.validateMandatoryString("description", bike.getDescription());
		PropertyValidator.validateDouble("price", bike.getPrice(), 0, MAX_PRICE);
	}
	
	private void validateRate(Long bookId, int rating) throws InputValidationException {
		
		if (rating < 0 || rating > 10) {
			throw new InputValidationException("The rate can not be \"" + rating +
					"\". Must be between 0 and 10.");
		}
		
		if (bookId == null) {
			throw new InputValidationException("The bookId can't be null.");
		}
		else {
			PropertyValidator.validateLong("bookId", bookId, 0, MAX_BIKEID);
		}
		
	}

	@Override
	public Bike addBike(Bike bike) throws InputValidationException, InvalidStartDateException {

		validateBike(bike, false);
		
		Calendar creationDate = Calendar.getInstance();

		try (Connection connection = dataSource.getConnection()) {

			try {

				bike.setCreationDate(creationDate);
				if (bike.getStartDate().before(creationDate)) {
					throw new InvalidStartDateException();
				}
				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike createdBike = bikeDao.create(connection, bike);

				/* Commit. */
				connection.commit();

				return createdBike;

			} catch (InvalidStartDateException e) {
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
	public void updateBike(Bike bike) throws InputValidationException, InstanceNotFoundException, 
			InvalidStartDateToUpdateException {

		validateBike(bike, true);
		
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Book bookAux = bookDao.findByBikeId(connection, bike.getBikeId());
				if (bookAux.getInitDate().after(bike.getStartDate()) ) {
					throw new InvalidStartDateToUpdateException(bookAux.getBikeId(), bookAux.getInitDate()); 
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
	
	@Override
	public void rateBook(Long bookId, String email, int rate) throws InputValidationException, 
			InstanceNotFoundException, BookNotFinishedException, BookAlreadyRatedException,
			InvalidUserException {

		validateRate(bookId, rate);
		Calendar actualDate = Calendar.getInstance();
		
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Book bookAux = bookDao.findByBookId(connection, bookId);
				Bike bikeAux = bikeDao.find(connection, bookAux.getBikeId());
				
				// Las reservas se introducen con rate a valor -1 a la BD, por tanto,
				// si la puntuación es distinta de -1 es que se ha puntuado ya.
				if (bookAux.getBookRate() != -1) {
					throw new BookAlreadyRatedException();
				}
				if (bookAux.getEndDate().after(actualDate)) {
					throw new BookNotFinishedException();
				}
				if (!bookAux.getEmail().equals(email)) {
					throw new InvalidUserException(email);
				}
				
    			bookAux.setBookRate(rate);
    			bikeAux.rate(rate);
				bookDao.update(connection, bookAux);
				bikeDao.update(connection, bikeAux);
				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				throw e;
			} catch (BookAlreadyRatedException e) {
				throw e;
			} catch (BookNotFinishedException e) {
				throw e;
			} catch (InvalidUserException e) {
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
	public Book bookBike(Book book) throws InstanceNotFoundException, InputValidationException, 
			InvalidBookDatesException, InvalidNumberOfBikesException, InvalidDaysOfBookException,
			InvalidStartDateToBookException {

		PropertyValidator.validateCreditCard(book.getCreditCard());
		
		try {
			if((book.getEndDate().getTimeInMillis() - book.getInitDate().getTimeInMillis()) / (24 * 60 * 60 * 1000) > MAX_BOOK_DAYS) {
				throw new InvalidDaysOfBookException(book.getInitDate(), book.getEndDate());
			}
			if (book.getInitDate().after(book.getEndDate())) {
				throw new InvalidBookDatesException();
			}
		} catch (InvalidDaysOfBookException e) {
			throw e;
		} catch (InvalidBookDatesException e) {
			throw e;
		}
		
		Calendar bookDate = Calendar.getInstance();
				
		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike bike = bikeDao.find(connection, book.getBikeId());
				if (bike.getUnits() < book.getNumberBikes()) {
					throw new InvalidNumberOfBikesException(bike.getBikeId(), bike.getUnits(),book.getNumberBikes());
				}
				else if(bike.getStartDate().after(book.getInitDate())){
					throw new InvalidStartDateToBookException();
				}
			
				Book createdBook = bookDao.create(connection, new Book(bike.getBikeId(), book.getEmail(), book.getCreditCard(), 
								book.getInitDate(), book.getEndDate(), book.getNumberBikes(), bookDate));
				
				
				/* Commit. */
				connection.commit();

				return createdBook;

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (InvalidNumberOfBikesException e) {
				throw e;
			} catch (InvalidStartDateToBookException e) {
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
