package cinema.dao;

import cinema.model.ShowTime;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ShowTimeDAO {
    void save(ShowTime showTime) throws SQLException;
    void update(ShowTime showTime) throws SQLException;
    void delete(String id) throws SQLException;
    Optional<ShowTime> findById(String id) throws SQLException;
    List<ShowTime> findAll() throws SQLException;
    List<ShowTime> findByMovieId(String movieId) throws SQLException;
    List<ShowTime> findByRoomId(String roomId) throws SQLException;
    List<ShowTime> findUpcoming() throws SQLException;
    Set<String> getBookedSeats(String showTimeId) throws SQLException;
    void bookSeat(String showTimeId, String seatNumber) throws SQLException;
}
