package es.udc.ws.bikes.model.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBookDao implements SqlBookDao {

    protected AbstractSqlBookDao() {
    }

    @Override
    public Book find(Connection connection, Long bookId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT movieId, userId, expirationDate,"
                + " creditCardNumber, price, movieUrl, saleDate FROM Sale WHERE saleId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, bookId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(bookId,
                		Book.class.getName());
            }

            /* Get results. */
            i = 1;
            String email = resultSet.getString(i++);
            Calendar initDate = Calendar.getInstance();
            initDate.setTime(resultSet.getTimestamp(i++));
            String creditCard = resultSet.getString(i++);
            int numberBikes = resultSet.getInt(i++);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(resultSet.getTimestamp(i++));
            Calendar bookDate = Calendar.getInstance();
            bookDate.setTime(resultSet.getTimestamp(i++));

            /* Return book. */
            return new Book(bookId, email, creditCard,
                    initDate, endDate, numberBikes, bookDate);

            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public void update(Connection connection, Book book)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Sale"
                + " SET movieId = ?, userId = ?, expirationDate = ?, "
                + " creditCardNumber = ?, price = ? WHERE saleId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, book.getEmail());
            Timestamp dateInit = book.getInitDate() != null ? new Timestamp(
            		book.getInitDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, dateInit);
            Timestamp dateEnd = book.getEndDate() != null ? new Timestamp(
            		book.getEndDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, dateEnd);
            Timestamp dateBook = book.getBookDate() != null ? new Timestamp(
                    book.getBookDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, dateBook);
            preparedStatement.setString(i++, book.getCreditCard());
            preparedStatement.setInt(i++, book.getNumberBikes());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(book.getEmail(),
                		Book.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void remove(Connection connection, Long bookId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Sale WHERE" + " saleId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, bookId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(bookId,
                		Book.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
	
}
