package cinema.dao;

import cinema.model.Room;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RoomDAO {
    void save(Room room) throws SQLException;
    void update(Room room) throws SQLException;
    void delete(String id) throws SQLException;
    Optional<Room> findById(String id) throws SQLException;
    List<Room> findAll() throws SQLException;
    List<Room> findByType(String type) throws SQLException;
}
