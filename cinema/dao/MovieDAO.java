package cinema.dao;

import cinema.model.Movie;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MovieDAO {
    void save(Movie movie) throws SQLException;
    void update(Movie movie) throws SQLException;
    void delete(String id) throws SQLException;
    Optional<Movie> findById(String id) throws SQLException;
    List<Movie> findAll() throws SQLException;
    List<Movie> findByTitle(String title) throws SQLException;
    List<Movie> findByGenre(String genre) throws SQLException;
    List<Movie> findShowingMovies() throws SQLException;
}
