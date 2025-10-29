package cinema.manager;

import cinema.model.Ticket;
import cinema.model.Movie;
import cinema.model.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RevenueManager {
    private TicketManager ticketManager;

    public RevenueManager(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Ticket ticket : ticketManager.getAllTickets()) {
            total += ticket.getTotalPrice();
        }
        return total;
    }

    public double getRevenueByDate(LocalDate date) {
        double total = 0;
        for (Ticket ticket : ticketManager.getAllTickets()) {
            if (ticket.getPurchaseTime().toLocalDate().equals(date)) {
                total += ticket.getTotalPrice();
            }
        }
        return total;
    }

    public double getRevenueByDateRange(LocalDateTime start, LocalDateTime end) {
        double total = 0;
        for (Ticket ticket : ticketManager.getAllTickets()) {
            LocalDateTime purchaseTime = ticket.getPurchaseTime();
            if (!purchaseTime.isBefore(start) && !purchaseTime.isAfter(end)) {
                total += ticket.getTotalPrice();
            }
        }
        return total;
    }

    public Map<String, Double> getRevenueByMovie() {
        Map<String, Double> movieRevenue = new HashMap<>();
        for (Ticket ticket : ticketManager.getAllTickets()) {
            String movieTitle = ticket.getShowTime().getMovie().getTitle();
            double ticketPrice = ticket.getSeats().size() * ticket.getShowTime().getTicketPrice();
            movieRevenue.put(movieTitle, movieRevenue.getOrDefault(movieTitle, 0.0) + ticketPrice);
        }
        return movieRevenue;
    }

    public double getServiceRevenue() {
        double total = 0;
        for (Ticket ticket : ticketManager.getAllTickets()) {
            for (Service service : ticket.getServices()) {
                total += service.getPrice();
            }
        }
        return total;
    }

    public double getTicketRevenue() {
        double total = 0;
        for (Ticket ticket : ticketManager.getAllTickets()) {
            total += ticket.getSeats().size() * ticket.getShowTime().getTicketPrice();
        }
        return total;
    }

    public int getTotalTicketsSold() {
        int total = 0;
        for (Ticket ticket : ticketManager.getAllTickets()) {
            total += ticket.getSeats().size();
        }
        return total;
    }

    public void displayRevenueReport() {
        System.out.println("\n========== BÁO CÁO DOANH THU ==========");
        System.out.println("Tổng doanh thu: " + String.format("%.0f", getTotalRevenue()) + " VNĐ");
        System.out.println("Doanh thu từ vé: " + String.format("%.0f", getTicketRevenue()) + " VNĐ");
        System.out.println("Doanh thu từ dịch vụ: " + String.format("%.0f", getServiceRevenue()) + " VNĐ");
        System.out.println("Tổng số vé đã bán: " + getTotalTicketsSold());
        
        System.out.println("\n--- Doanh thu theo phim ---");
        Map<String, Double> movieRevenue = getRevenueByMovie();
        for (Map.Entry<String, Double> entry : movieRevenue.entrySet()) {
            System.out.println(entry.getKey() + ": " + String.format("%.0f", entry.getValue()) + " VNĐ");
        }
        System.out.println("======================================");
    }

    public void displayDailyRevenue(LocalDate date) {
        double revenue = getRevenueByDate(date);
        System.out.println("\n=== DOANH THU NGÀY " + date + " ===");
        System.out.println("Tổng doanh thu: " + String.format("%.0f", revenue) + " VNĐ");
    }
}
