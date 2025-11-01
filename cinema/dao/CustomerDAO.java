package cinema.dao;

import cinema.model.Customer;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO {
    void save(Customer customer) throws SQLException;
    void update(Customer customer) throws SQLException;
    void delete(String id) throws SQLException;
    Optional<Customer> findById(String id) throws SQLException;
    Optional<Customer> findByPhone(String phone) throws SQLException;
    List<Customer> findAll() throws SQLException;
    List<Customer> findByName(String name) throws SQLException;
    List<Customer> findTopCustomers(int limit) throws SQLException;
}
