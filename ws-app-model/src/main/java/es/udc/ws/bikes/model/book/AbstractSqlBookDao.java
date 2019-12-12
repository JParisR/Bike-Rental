package es.udc.ws.bikes.model.book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlBookDao implements SqlBookDao {

    protected AbstractSqlBookDao() {
    }

    @Override
    public Book findByBikeId(Connection connection, Long bikeId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT bookId, email, creditCard, initDate,"
                + " endDate, numberBikes, bookDate FROM Book WHERE bikeId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, bikeId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(bikeId,
                		Book.class.getName());
            }

            /* Get results. */
            i = 1;
            Long bookId = resultSet.getLong(i++);
            String email = resultSet.getString(i++);
            String creditCard = resultSet.getString(i++);
            Calendar initDate = Calendar.getInstance();
            initDate.setTime(resultSet.getTimestamp(i++));
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(resultSet.getTimestamp(i++));
            int numberBikes = resultSet.getInt(i++);
            Calendar bookDate = Calendar.getInstance();
            bookDate.setTime(resultSet.getTimestamp(i++));

            /* Return book. */
            return new Book(bookId, bikeId, email, creditCard,
                    initDate, endDate, numberBikes, bookDate);

            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Book findByBookId(Connection connection, Long bookId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT bikeId, email, creditCard, initDate,"
                + " endDate, numberBikes, bookDate, bookRate FROM Book WHERE bookId = ?";

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
            Long bikeId = resultSet.getLong(i++);
            String email = resultSet.getString(i++);
            String creditCard = resultSet.getString(i++);
            Calendar initDate = Calendar.getInstance();
            initDate.setTime(resultSet.getTimestamp(i++));
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(resultSet.getTimestamp(i++));
            int numberBikes = resultSet.getInt(i++);
            Calendar bookDate = Calendar.getInstance();
            bookDate.setTime(resultSet.getTimestamp(i++));
            int bookRate = resultSet.getInt(i++);

            /* Return book. */
            return new Book(bookId, bikeId, email, creditCard,
                    initDate, endDate, numberBikes, bookDate, bookRate);

            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public List<Book> findByUser(Connection connection, String email) {

    	/* Create "queryString". */
        String queryString = "SELECT bookId, bikeId, email, creditCard," 
        		+ " endDate, numberBikes, bookDate WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {
        	
        	/* Fill "preparedStatement". */
        	int i = 1;
        	preparedStatement.setString(i++, email);
        	
            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Read books. */
            List<Book> books = new ArrayList<Book>();

            while (resultSet.next()) {

                i = 1;
                //Long bookId = new Long(resultSet.getLong(i++));
                Long bikeId = new Long(resultSet.getLong(i++));
                String creditCard = resultSet.getString(i++);
                Calendar initDate = Calendar.getInstance();
                initDate.setTime(resultSet.getTimestamp(i++));
                Calendar endDate = Calendar.getInstance();
                endDate.setTime(resultSet.getTimestamp(i++));
                int numberBikes = resultSet.getInt(i++);
                Calendar bookDate = Calendar.getInstance();
                bookDate.setTime(resultSet.getTimestamp(i++));
                
                books.add(new Book(bikeId, email, creditCard, initDate,
                        endDate, numberBikes, bookDate));

            }

            /* Return books. */
            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public void update(Connection connection, Book book)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Book"
                + " SET bikeId = ?, email = ?, creditCard = ?,"
                + " initDate = ?, endDate = ?, numberBikes = ?,"
                + " bookRate = ? WHERE bookId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, book.getBikeId());
            preparedStatement.setString(i++, book.getEmail());
            preparedStatement.setString(i++, book.getCreditCard());
            Timestamp initDate = book.getInitDate() != null ? new Timestamp(
            		book.getInitDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, initDate);
            Timestamp endDate = book.getEndDate() != null ? new Timestamp(
            		book.getEndDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, endDate);
            preparedStatement.setInt(i++, book.getNumberBikes());
            preparedStatement.setInt(i++, book.getBookRate());
            preparedStatement.setLong(i++, book.getBookId());

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
        String queryString = "DELETE FROM Book WHERE bookId = ?";

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
