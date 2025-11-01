package cinema.dao.impl;

import cinema.dao.MovieDAO;
import cinema.database.DatabaseConnection;
import cinema.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAOImpl implements MovieDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (id, title, genre, duration, director, description, is_showing) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, movie.getId());
            stmt.setString(2, movie.getTitle());
            stmt.setString(3, movie.getGenre());
            stmt.setInt(4, movie.getDuration());
            stmt.setString(5, movie.getDirector());
            stmt.setString(6, movie.getDescription());
            stmt.setBoolean(7, movie.isShowing());
            stmt.executeUpdate();

            // Save cast members
            saveCast(movie);
        }
    }

    private void saveCast(Movie movie) throws SQLException {
        String sql = "INSERT INTO movie_cast (movie_id, actor_name) VALUES (?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (String actor : movie.getCast()) {
                stmt.setString(1, movie.getId());
                stmt.setString(2, actor);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public void update(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET title=?, genre=?, duration=?, director=?, description=?, is_showing=? WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, movie.getTitle());
            stmt.setString(2, movie.getGenre());
            stmt.setInt(3, movie.getDuration());
            stmt.setString(4, movie.getDirector());
            stmt.setString(5, movie.getDescription());
            stmt.setBoolean(6, movie.isShowing());
            stmt.setString(7, movie.getId());
            stmt.executeUpdate();

            // Update cast
            deleteCast(movie.getId());
            saveCast(movie);
        }
    }

    private void deleteCast(String movieId) throws SQLException {
        String sql = "DELETE FROM movie_cast WHERE movie_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, movieId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Movie> findById(String id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Movie movie = extractMovie(rs);
                loadCast(movie);
                return Optional.of(movie);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY title";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = extractMovie(rs);
                loadCast(movie);
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public List<Movie> findByTitle(String title) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE title LIKE ? ORDER BY title";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + title + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movie movie = extractMovie(rs);
                loadCast(movie);
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public List<Movie> findByGenre(String genre) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE genre LIKE ? ORDER BY title";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + genre + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movie movie = extractMovie(rs);
                loadCast(movie);
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public List<Movie> findShowingMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE is_showing=TRUE ORDER BY title";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = extractMovie(rs);
                loadCast(movie);
                movies.add(movie);
            }
        }
        return movies;
    }

    private Movie extractMovie(ResultSet rs) throws SQLException {
        return new Movie(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("genre"),
            rs.getInt("duration"),
            rs.getString("director"),
            rs.getString("description")
        );
    }

    private void loadCast(Movie movie) throws SQLException {
        String sql = "SELECT actor_name FROM movie_cast WHERE movie_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, movie.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                movie.addCast(rs.getString("actor_name"));
            }
        }
    }
}
