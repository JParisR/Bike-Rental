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
                + " (description, startDate, price, units"
                + " creationDate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, bike.getDescription());
            Timestamp startDate = bike.getCreationDate() != null ? new Timestamp(
                    bike.getCreationDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, startDate);
            preparedStatement.setFloat(i++, bike.getPrice());
            preparedStatement.setInt(i++, bike.getUnits());
            Timestamp creationDate = bike.getCreationDate() != null ? new Timestamp(
                    bike.getCreationDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, creationDate);

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
            return new Bike(bikeId, bike.getDescription(), bike.getStartDate(), 
            		bike.getPrice(), bike.getUnits(), bike.getCreationDate());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
