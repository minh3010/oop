package cinema.dao.impl;

import cinema.dao.RoomDAO;
import cinema.database.DatabaseConnection;
import cinema.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAOImpl implements RoomDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (id, name, total_seats, rows, seats_per_row, room_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, room.getId());
            stmt.setString(2, room.getName());
            stmt.setInt(3, room.getTotalSeats());
            stmt.setInt(4, room.getRows());
            stmt.setInt(5, room.getSeatsPerRow());
            stmt.setString(6, room.getRoomType());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Room room) throws SQLException {
        String sql = "UPDATE rooms SET name=?, total_seats=?, rows=?, seats_per_row=?, room_type=? WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setInt(2, room.getTotalSeats());
            stmt.setInt(3, room.getRows());
            stmt.setInt(4, room.getSeatsPerRow());
            stmt.setString(5, room.getRoomType());
            stmt.setString(6, room.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Room> findById(String id) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extractRoom(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Room> findAll() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY name";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(extractRoom(rs));
            }
        }
        return rooms;
    }

    @Override
    public List<Room> findByType(String type) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_type=? ORDER BY name";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(extractRoom(rs));
            }
        }
        return rooms;
    }

    private Room extractRoom(ResultSet rs) throws SQLException {
        return new Room(
            rs.getString("id"),
            rs.getString("name"),
            rs.getInt("rows"),
            rs.getInt("seats_per_row"),
            rs.getString("room_type")
        );
    }
}
