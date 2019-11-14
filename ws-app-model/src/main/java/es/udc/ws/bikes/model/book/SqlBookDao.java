package es.udc.ws.bikes.model.book;

import java.sql.Connection;
import java.util.List;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlBookDao {
	
	public Book create(Connection connection, Book book);

    public Book find(Connection connection, Long bikeId)
            throws InstanceNotFoundException;
    
    public List<Book> findByUser(Connection connection, String email);

    public void update(Connection connection, Book book)
            throws InstanceNotFoundException;

    public void remove(Connection connection, Long bookId)
            throws InstanceNotFoundException;

}
