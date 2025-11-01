package cinema.dao.impl;

import cinema.dao.ShowTimeDAO;
import cinema.dao.MovieDAO;
import cinema.dao.RoomDAO;
import cinema.database.DatabaseConnection;
import cinema.model.ShowTime;
import cinema.model.Movie;
import cinema.model.Room;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ShowTimeDAOImpl implements ShowTimeDAO {
    private MovieDAO movieDAO;
    private RoomDAO roomDAO;

    public ShowTimeDAOImpl() {
        this.movieDAO = new MovieDAOImpl();
        this.roomDAO = new RoomDAOImpl();
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(ShowTime showTime) throws SQLException {
        String sql = "INSERT INTO showtimes (id, movie_id, room_id, start_time, ticket_price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, showTime.getId());
            stmt.setString(2, showTime.getMovie().getId());
            stmt.setString(3, showTime.getRoom().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(showTime.getStartTime()));
            stmt.setDouble(5, showTime.getTicketPrice());
            stmt.executeUpdate();

            // Save booked seats
            for (String seat : showTime.getBookedSeats()) {
                bookSeat(showTime.getId(), seat);
            }
        }
    }

    @Override
    public void update(ShowTime showTime) throws SQLException {
        String sql = "UPDATE showtimes SET movie_id=?, room_id=?, start_time=?, ticket_price=? WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, showTime.getMovie().getId());
            stmt.setString(2, showTime.getRoom().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(showTime.getStartTime()));
            stmt.setDouble(4, showTime.getTicketPrice());
            stmt.setString(5, showTime.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM showtimes WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<ShowTime> findById(String id) throws SQLException {
        String sql = "SELECT * FROM showtimes WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extractShowTime(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<ShowTime> findAll() throws SQLException {
        List<ShowTime> showTimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes ORDER BY start_time";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showTimes.add(extractShowTime(rs));
            }
        }
        return showTimes;
    }

    @Override
    public List<ShowTime> findByMovieId(String movieId) throws SQLException {
        List<ShowTime> showTimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes WHERE movie_id=? ORDER BY start_time";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, movieId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                showTimes.add(extractShowTime(rs));
            }
        }
        return showTimes;
    }

    @Override
    public List<ShowTime> findByRoomId(String roomId) throws SQLException {
        List<ShowTime> showTimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes WHERE room_id=? ORDER BY start_time";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                showTimes.add(extractShowTime(rs));
            }
        }
        return showTimes;
    }

    @Override
    public List<ShowTime> findUpcoming() throws SQLException {
        List<ShowTime> showTimes = new ArrayList<>();
        String sql = "SELECT * FROM showtimes WHERE start_time > NOW() ORDER BY start_time";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showTimes.add(extractShowTime(rs));
            }
        }
        return showTimes;
    }

    @Override
    public Set<String> getBookedSeats(String showTimeId) throws SQLException {
        Set<String> bookedSeats = new HashSet<>();
        String sql = "SELECT seat_number FROM showtime_booked_seats WHERE showtime_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, showTimeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookedSeats.add(rs.getString("seat_number"));
            }
        }
        return bookedSeats;
    }

    @Override
    public void bookSeat(String showTimeId, String seatNumber) throws SQLException {
        String sql = "INSERT INTO showtime_booked_seats (showtime_id, seat_number) VALUES (?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, showTimeId);
            stmt.setString(2, seatNumber);
            stmt.executeUpdate();
        }
    }

    private ShowTime extractShowTime(ResultSet rs) throws SQLException {
        String movieId = rs.getString("movie_id");
        String roomId = rs.getString("room_id");
        
        Optional<Movie> movieOpt = movieDAO.findById(movieId);
        Optional<Room> roomOpt = roomDAO.findById(roomId);
        
        if (!movieOpt.isPresent() || !roomOpt.isPresent()) {
            throw new SQLException("Movie or Room not found for ShowTime");
        }
        
        ShowTime showTime = new ShowTime(
            rs.getString("id"),
            movieOpt.get(),
            roomOpt.get(),
            rs.getTimestamp("start_time").toLocalDateTime(),
            rs.getDouble("ticket_price")
        );
        
        // Load booked seats
        Set<String> bookedSeats = getBookedSeats(showTime.getId());
        for (String seat : bookedSeats) {
            showTime.bookSeat(seat);
        }
        
        return showTime;
    }
}
