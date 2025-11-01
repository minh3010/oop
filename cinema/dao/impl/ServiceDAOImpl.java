package cinema.dao.impl;

import cinema.dao.ServiceDAO;
import cinema.database.DatabaseConnection;
import cinema.model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServiceDAOImpl implements ServiceDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Service service) throws SQLException {
        String sql = "INSERT INTO services (id, name, type, price, quantity, size) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, service.getId());
            stmt.setString(2, service.getName());
            stmt.setString(3, service.getType());
            stmt.setDouble(4, service.getPrice());
            stmt.setInt(5, service.getQuantity());
            stmt.setString(6, service.getSize());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Service service) throws SQLException {
        String sql = "UPDATE services SET name=?, type=?, price=?, quantity=?, size=? WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, service.getName());
            stmt.setString(2, service.getType());
            stmt.setDouble(3, service.getPrice());
            stmt.setInt(4, service.getQuantity());
            stmt.setString(5, service.getSize());
            stmt.setString(6, service.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM services WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Service> findById(String id) throws SQLException {
        String sql = "SELECT * FROM services WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extractService(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Service> findAll() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                services.add(extractService(rs));
            }
        }
        return services;
    }

    @Override
    public List<Service> findByType(String type) throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE type=? ORDER BY name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                services.add(extractService(rs));
            }
        }
        return services;
    }

    @Override
    public List<Service> findAvailable() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE quantity > 0 ORDER BY name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                services.add(extractService(rs));
            }
        }
        return services;
    }

    private Service extractService(ResultSet rs) throws SQLException {
        return new Service(
            rs.getString("id"),
            rs.getString("name"),
            rs.getString("type"),
            rs.getDouble("price"),
            rs.getInt("quantity"),
            rs.getString("size")
        );
    }
}
