package es.udc.ws.bikes.model.bike;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlBikeDao extends AbstractSqlBikeDao {

    @Override
    public Bike create(Connection connection, Bike bike) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Bike"
                + " (description, price, creationDate, units)"
                + " VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, bike.getDescription());
            preparedStatement.setFloat(i++, bike.getPrice());
            Timestamp date = bike.getCreationDate() != null ? new Timestamp(
                    bike.getCreationDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setInt(i++, bike.getUnits());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long bikeId = resultSet.getLong(1);

            /* Return movie. */
            return new Bike(bikeId, bike.getDescription(), bike.getCreationDate(), 
            		bike.getPrice(), bike.getUnits());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
