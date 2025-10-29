package cinema.model;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private int duration; // phút
    private String director;
    private String description;
    private boolean isShowing;
    private List<String> cast;

    public Movie(String id, String title, String genre, int duration, String director, String description) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.director = director;
        this.description = description;
        this.isShowing = true;
        this.cast = new ArrayList<>();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    public List<String> getCast() {
        return cast;
    }

    public void addCast(String actor) {
        this.cast.add(actor);
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Phim: %s | Thể loại: %s | Thời lượng: %d phút | Đạo diễn: %s | Đang chiếu: %s",
                id, title, genre, duration, director, isShowing ? "Có" : "Không");
    }
}
