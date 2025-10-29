package cinema.manager;

import cinema.model.Movie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieManager {
    private List<Movie> movies;

    public MovieManager() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public boolean removeMovie(String movieId) {
        return movies.removeIf(m -> m.getId().equals(movieId));
    }

    public Optional<Movie> findMovieById(String id) {
        return movies.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public List<Movie> getShowingMovies() {
        List<Movie> showingMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.isShowing()) {
                showingMovies.add(movie);
            }
        }
        return showingMovies;
    }

    public List<Movie> searchByTitle(String title) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }

    public List<Movie> searchByGenre(String genre) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getGenre().toLowerCase().contains(genre.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }

    public void displayAllMovies() {
        if (movies.isEmpty()) {
            System.out.println("Không có phim nào trong hệ thống.");
            return;
        }
        System.out.println("\n=== DANH SÁCH PHIM ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}
