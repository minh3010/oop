package cinema.manager;

import cinema.model.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomManager {
    private List<Room> rooms;

    public RoomManager() {
        this.rooms = new ArrayList<>();
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public boolean removeRoom(String roomId) {
        return rooms.removeIf(r -> r.getId().equals(roomId));
    }

    public Optional<Room> findRoomById(String id) {
        return rooms.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }

    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }

    public List<Room> getRoomsByType(String type) {
        List<Room> result = new ArrayList<>();
        for (Room room : rooms) {
            if (room.getRoomType().equalsIgnoreCase(type)) {
                result.add(room);
            }
        }
        return result;
    }

    public void displayAllRooms() {
        if (rooms.isEmpty()) {
            System.out.println("Không có phòng chiếu nào trong hệ thống.");
            return;
        }
        System.out.println("\n=== DANH SÁCH PHÒNG CHIẾU ===");
        for (Room room : rooms) {
            System.out.println(room);
        }
    }
}
