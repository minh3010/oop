package cinema.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class ShowTime {
    private String id;
    private Movie movie;
    private Room room;
    private LocalDateTime startTime;
    private double ticketPrice;
    private Set<String> bookedSeats;

    public ShowTime(String id, Movie movie, Room room, LocalDateTime startTime, double ticketPrice) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.ticketPrice = ticketPrice;
        this.bookedSeats = new HashSet<>();
    }

    public boolean bookSeat(String seatNumber) {
        if (bookedSeats.contains(seatNumber)) {
            return false;
        }
        bookedSeats.add(seatNumber);
        return true;
    }

    public boolean isSeatAvailable(String seatNumber) {
        return !bookedSeats.contains(seatNumber);
    }

    public int getAvailableSeats() {
        return room.getTotalSeats() - bookedSeats.size();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Set<String> getBookedSeats() {
        return bookedSeats;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("ID: %s | Phim: %s | Phòng: %s | Giờ chiếu: %s | Giá vé: %.0f VNĐ | Ghế trống: %d/%d",
                id, movie.getTitle(), room.getName(), startTime.format(formatter), 
                ticketPrice, getAvailableSeats(), room.getTotalSeats());
    }
}
