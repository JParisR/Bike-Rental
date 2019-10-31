package es.udc.ws.bikes.model.bikesservice;

import static es.udc.ws.bikes.model.util.ModelConstants.MAX_PRICE;
import static es.udc.ws.bikes.model.util.ModelConstants.MAX_RUNTIME;
import static es.udc.ws.bikes.model.util.ModelConstants.MOVIE_DATA_SOURCE;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;

import javax.sql.DataSource;

import es.udc.ws.bikes.model.bike.Bike;
import es.udc.ws.bikes.model.bike.SqlBikeDao;
import es.udc.ws.bikes.model.bike.SqlBikeDaoFactory;
import es.udc.ws.bikes.model.book.SqlBookDao;
import es.udc.ws.bikes.model.book.SqlBookDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class BikeServiceImpl implements BikeService{


	private final DataSource dataSource;
	private SqlBikeDao bikeDao = null;
	private SqlBookDao bookDao = null;

	public BikeServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(MOVIE_DATA_SOURCE);
		bikeDao = SqlBikeDaoFactory.getDao();
		bookDao = SqlBookDaoFactory.getDao();
	}

	private void validateBike(Bike bike) throws InputValidationException {

		PropertyValidator.validateMandatoryString("description", bike.getDescription());
		PropertyValidator.validateLong("runtime", bike.getBikeId(), 0, MAX_RUNTIME);
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
	public void updateBike(Bike bike) throws InputValidationException, InstanceNotFoundException {

		validateBike(bike);

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Bike bikeAux = bikeDao.find(connection, bike.getBikeId()); 
				if (bikeAux.getStartDate().before(bike.getStartDate()) ) {
				//	throw 
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
}
