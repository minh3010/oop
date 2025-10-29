package cinema.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String id;
    private String name;
    private String phone;
    private String email;
    private int loyaltyPoints;
    private List<Ticket> purchaseHistory;

    public Customer(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.loyaltyPoints = 0;
        this.purchaseHistory = new ArrayList<>();
    }

    public void addPurchase(Ticket ticket) {
        purchaseHistory.add(ticket);
        loyaltyPoints += (int)(ticket.getTotalPrice() / 10000); // 1 điểm cho mỗi 10,000 VNĐ
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public List<Ticket> getPurchaseHistory() {
        return purchaseHistory;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Tên: %s | SĐT: %s | Email: %s | Điểm tích lũy: %d",
                id, name, phone, email, loyaltyPoints);
    }
}
