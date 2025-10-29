package cinema.model;

public class Service {
    private String id;
    private String name;
    private String type; // DRINK, POPCORN, COMBO
    private double price;
    private int quantity;
    private String size; // S, M, L

    public Service(String id, String name, String type, double price, int quantity, String size) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public boolean reduceQuantity(int amount) {
        if (quantity >= amount) {
            quantity -= amount;
            return true;
        }
        return false;
    }

    public void addQuantity(int amount) {
        quantity += amount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | %s (%s) | Size: %s | Giá: %.0f VNĐ | Còn lại: %d",
                id, name, type, size, price, quantity);
    }
}
