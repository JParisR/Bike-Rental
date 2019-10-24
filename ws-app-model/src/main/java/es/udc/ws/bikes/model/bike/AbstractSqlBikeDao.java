package es.udc.ws.bikes.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

/**
 * A partial implementation of
 * <code>SQLBikeDAO</code> that leaves
 * <code>create(Connection, Bike)</code> as abstract.
 */
public abstract class AbstractSqlBikeDao implements SqlBikeDao {

    protected AbstractSqlBikeDao() {
    }

    @Override
    public Bike find(Connection connection, Long bikeId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT bikeId "
                + " description, price, creationDate FROM Bike WHERE bikeId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, bikeId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(bikeId,
                        Bike.class.getName());
            }

            /* Get results. */
            i = 1;
            String description = resultSet.getString(i++);
            float price = resultSet.getFloat(i++);
            Calendar creationDate = Calendar.getInstance();
            creationDate.setTime(resultSet.getTimestamp(i++));
            int units = resultSet.getInt(i++);
            
            /* Return movie. */
            return new Bike(bikeId, description, creationDate,
            		price, units);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Bike> findByKeywords(Connection connection, String keywords) {

        /* Create "queryString". */
        String[] words = keywords != null ? keywords.split(" ") : null;
        String queryString = "SELECT bikeId"
                + " description, price, creationDate FROM Bike";
        if (words != null && words.length > 0) {
            queryString += " WHERE";
            for (int i = 0; i < words.length; i++) {
                if (i > 0) {
                    queryString += " AND";
                }
                queryString += " LOWER(title) LIKE LOWER(?)";
            }
        }
        queryString += " ORDER BY bikeId";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            if (words != null) {
                /* Fill "preparedStatement". */
                for (int i = 0; i < words.length; i++) {
                    preparedStatement.setString(i + 1, "%" + words[i] + "%");
                }
            }

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read movies. */
            List<Bike> bikes = new ArrayList<Bike>();

            while (resultSet.next()) {

                int i = 1;
                Long bikeId = new Long(resultSet.getLong(i++));
                String description = resultSet.getString(i++);
                float price = resultSet.getFloat(i++);
                Calendar creationDate = Calendar.getInstance();
                creationDate.setTime(resultSet.getTimestamp(i++));
                int units = resultSet.getInt(i++);

                bikes.add(new Bike(bikeId, description, creationDate,
                		price, units));

            }

            /* Return movies. */
            return bikes;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void update(Connection connection, Bike bike)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Bike"
                + " SET description = ?, "
                + "price = ? WHERE bikeId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, bike.getDescription());
            preparedStatement.setFloat(i++, bike.getPrice());
            preparedStatement.setLong(i++, bike.getBikeId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(bike.getBikeId(),
                        Bike.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long bikeId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Bike WHERE" + " bikeId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, bikeId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(bikeId,
                        Bike.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
