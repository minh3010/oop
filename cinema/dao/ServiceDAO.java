package cinema.dao;

import cinema.model.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ServiceDAO {
    void save(Service service) throws SQLException;
    void update(Service service) throws SQLException;
    void delete(String id) throws SQLException;
    Optional<Service> findById(String id) throws SQLException;
    List<Service> findAll() throws SQLException;
    List<Service> findByType(String type) throws SQLException;
    List<Service> findAvailable() throws SQLException;
}
