package es.udc.ws.bikes.model.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdb3CcSqlBookDao extends AbstractSqlBookDao{


    @Override
    public Book create(Connection connection, Book book) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Sale"
                + " (movieId, userId, expirationDate, creditCardNumber,"
                + " price, movieUrl, saleDate) VALUES (?, ?, ?, ?, ?, ?, ?)";


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, book.getEmail());
            Timestamp dateInit = new Timestamp(book.getInitDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, dateInit);
            Timestamp dateEnd = new Timestamp(book.getEndDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, dateEnd);
            preparedStatement.setString(i++, book.getCreditCard());
            preparedStatement.setInt(i++, book.getNumberBikes());
            Timestamp DateBook = new Timestamp(book.getBookDate().getTime()
                    .getTime());
            preparedStatement.setTimestamp(i++, DateBook);
            
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
            return new Book(bookId, book.getEmail(), book.getCreditCard(),
            		book.getInitDate(), book.getEndDate(),
            		book.getNumberBikes(), book.getBookDate());
            
            

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
