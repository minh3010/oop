package cinema.manager;

import cinema.model.ShowTime;
import cinema.model.Movie;
import cinema.model.Room;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowTimeManager {
    private List<ShowTime> showTimes;

    public ShowTimeManager() {
        this.showTimes = new ArrayList<>();
    }

    public void addShowTime(ShowTime showTime) {
        showTimes.add(showTime);
    }

    public boolean removeShowTime(String showTimeId) {
        return showTimes.removeIf(st -> st.getId().equals(showTimeId));
    }

    public Optional<ShowTime> findShowTimeById(String id) {
        return showTimes.stream()
                .filter(st -> st.getId().equals(id))
                .findFirst();
    }

    public List<ShowTime> getAllShowTimes() {
        return new ArrayList<>(showTimes);
    }

    public List<ShowTime> getShowTimesByMovie(Movie movie) {
        List<ShowTime> result = new ArrayList<>();
        for (ShowTime st : showTimes) {
            if (st.getMovie().getId().equals(movie.getId())) {
                result.add(st);
            }
        }
        return result;
    }

    public List<ShowTime> getShowTimesByRoom(Room room) {
        List<ShowTime> result = new ArrayList<>();
        for (ShowTime st : showTimes) {
            if (st.getRoom().getId().equals(room.getId())) {
                result.add(st);
            }
        }
        return result;
    }

    public List<ShowTime> getUpcomingShowTimes() {
        List<ShowTime> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (ShowTime st : showTimes) {
            if (st.getStartTime().isAfter(now)) {
                result.add(st);
            }
        }
        return result;
    }

    public void displayAllShowTimes() {
        if (showTimes.isEmpty()) {
            System.out.println("Không có suất chiếu nào trong hệ thống.");
            return;
        }
        System.out.println("\n=== DANH SÁCH SUẤT CHIẾU ===");
        for (ShowTime st : showTimes) {
            System.out.println(st);
        }
    }

    public void displayUpcomingShowTimes() {
        List<ShowTime> upcoming = getUpcomingShowTimes();
        if (upcoming.isEmpty()) {
            System.out.println("Không có suất chiếu sắp tới.");
            return;
        }
        System.out.println("\n=== SUẤT CHIẾU SẮP TỚI ===");
        for (ShowTime st : upcoming) {
            System.out.println(st);
        }
    }
}
