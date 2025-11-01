package cinema.dao.impl;

import cinema.dao.CustomerDAO;
import cinema.database.DatabaseConnection;
import cinema.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDAOImpl implements CustomerDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (id, name, phone, email, loyalty_points) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getEmail());
            stmt.setInt(5, customer.getLoyaltyPoints());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET name=?, phone=?, email=?, loyalty_points=? WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getPhone());
            stmt.setString(3, customer.getEmail());
            stmt.setInt(4, customer.getLoyaltyPoints());
            stmt.setString(5, customer.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Customer> findById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extractCustomer(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> findByPhone(String phone) throws SQLException {
        String sql = "SELECT * FROM customers WHERE phone=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, phone);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extractCustomer(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(extractCustomer(rs));
            }
        }
        return customers;
    }

    @Override
    public List<Customer> findByName(String name) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers WHERE name LIKE ? ORDER BY name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(extractCustomer(rs));
            }
        }
        return customers;
    }

    @Override
    public List<Customer> findTopCustomers(int limit) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY loyalty_points DESC LIMIT ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(extractCustomer(rs));
            }
        }
        return customers;
    }

    private Customer extractCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("phone"),
            rs.getString("email")
        );
        customer.setLoyaltyPoints(rs.getInt("loyalty_points"));
        return customer;
    }
}
