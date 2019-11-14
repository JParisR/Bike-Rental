package es.udc.ws.bikes.model.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlBookDao extends AbstractSqlBookDao{


    @Override
    public Book create(Connection connection, Book book) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Book"
                + " (bikeId, email, creditCard,"
                + " initDate, endDate, numberBikes, bookDate)" 
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, book.getBikeId());
            preparedStatement.setString(i++, book.getEmail());
            preparedStatement.setString(i++, book.getCreditCard());
            Timestamp initDate = new Timestamp(book.getInitDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, initDate);
            Timestamp endDate = new Timestamp(book.getEndDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, endDate);
            preparedStatement.setInt(i++, book.getNumberBikes());
            Timestamp bookDate = new Timestamp(book.getBookDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, bookDate);
            
            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long bookId = resultSet.getLong(1);

            /* Return sale. */
            return new Book(bookId, book.getEmail(), 
            		book.getCreditCard(), book.getInitDate(), book.getEndDate(),
            		book.getNumberBikes(), book.getBookDate());
            
            

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
