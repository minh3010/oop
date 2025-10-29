package cinema.model;

public class Room {
    private String id;
    private String name;
    private int totalSeats;
    private int rows;
    private int seatsPerRow;
    private String roomType; // Standard, VIP, IMAX

    public Room(String id, String name, int rows, int seatsPerRow, String roomType) {
        this.id = id;
        this.name = name;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.totalSeats = rows * seatsPerRow;
        this.roomType = roomType;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        this.totalSeats = rows * seatsPerRow;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
        this.totalSeats = rows * seatsPerRow;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Phòng: %s | Loại: %s | Số ghế: %d (%d hàng x %d ghế)",
                id, name, roomType, totalSeats, rows, seatsPerRow);
    }
}
