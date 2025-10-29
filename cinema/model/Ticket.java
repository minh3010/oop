package cinema.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private String id;
    private ShowTime showTime;
    private Customer customer;
    private List<String> seats;
    private List<Service> services;
    private LocalDateTime purchaseTime;
    private double totalPrice;

    public Ticket(String id, ShowTime showTime, Customer customer) {
        this.id = id;
        this.showTime = showTime;
        this.customer = customer;
        this.seats = new ArrayList<>();
        this.services = new ArrayList<>();
        this.purchaseTime = LocalDateTime.now();
        this.totalPrice = 0;
    }

    public void addSeat(String seat) {
        seats.add(seat);
        calculateTotalPrice();
    }

    public void addService(Service service) {
        services.add(service);
        calculateTotalPrice();
    }

    private void calculateTotalPrice() {
        totalPrice = seats.size() * showTime.getTicketPrice();
        for (Service service : services) {
            totalPrice += service.getPrice();
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShowTime getShowTime() {
        return showTime;
    }

    public void setShowTime(ShowTime showTime) {
        this.showTime = showTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<String> getSeats() {
        return seats;
    }

    public List<Service> getServices() {
        return services;
    }

    public LocalDateTime getPurchaseTime() {
        return purchaseTime;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== VÉ XEM PHIM ===\n"));
        sb.append(String.format("Mã vé: %s\n", id));
        sb.append(String.format("Khách hàng: %s\n", customer.getName()));
        sb.append(String.format("Phim: %s\n", showTime.getMovie().getTitle()));
        sb.append(String.format("Phòng: %s\n", showTime.getRoom().getName()));
        sb.append(String.format("Giờ chiếu: %s\n", showTime.getStartTime().format(formatter)));
        sb.append(String.format("Ghế: %s\n", String.join(", ", seats)));
        if (!services.isEmpty()) {
            sb.append("Dịch vụ: ");
            for (Service s : services) {
                sb.append(s.getName()).append(" ");
            }
            sb.append("\n");
        }
        sb.append(String.format("Tổng tiền: %.0f VNĐ\n", totalPrice));
        sb.append(String.format("Thời gian mua: %s\n", purchaseTime.format(formatter)));
        sb.append("==================");
        return sb.toString();
    }
}
