package cinema.dao;

import cinema.model.Ticket;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    String save(Ticket ticket) throws SQLException;
    void update(Ticket ticket) throws SQLException;
    void delete(String id) throws SQLException;
    Optional<Ticket> findById(String id) throws SQLException;
    List<Ticket> findAll() throws SQLException;
    List<Ticket> findByCustomerId(String customerId) throws SQLException;
    List<Ticket> findByShowTimeId(String showTimeId) throws SQLException;
    int getNextTicketNumber() throws SQLException;
}
