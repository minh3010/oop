package cinema.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String id;
    private Ticket ticket;
    private Customer customer;
    private LocalDateTime issueTime;
    private String paymentMethod;
    private double ticketSubtotal;
    private double serviceSubtotal;
    private double tax;
    private double totalAmount;
    
    // Chi tiết các mục trong hóa đơn
    private List<InvoiceItem> ticketItems;
    private List<InvoiceItem> serviceItems;

    public Invoice(String id, Ticket ticket, String paymentMethod) {
        this.id = id;
        this.ticket = ticket;
        this.customer = ticket.getCustomer();
        this.issueTime = LocalDateTime.now();
        this.paymentMethod = paymentMethod;
        this.ticketItems = new ArrayList<>();
        this.serviceItems = new ArrayList<>();
        
        calculateInvoice();
    }

    private void calculateInvoice() {
        // Tính tiền vé
        ticketSubtotal = 0;
        for (String seat : ticket.getSeats()) {
            double price = ticket.getShowTime().getTicketPrice();
            ticketItems.add(new InvoiceItem(
                "Vé ghế " + seat + " - " + ticket.getShowTime().getMovie().getTitle(),
                1,
                price,
                price
            ));
            ticketSubtotal += price;
        }

        // Tính tiền dịch vụ
        serviceSubtotal = 0;
        for (Service service : ticket.getServices()) {
            serviceItems.add(new InvoiceItem(
                service.getName() + " (" + service.getSize() + ")",
                1,
                service.getPrice(),
                service.getPrice()
            ));
            serviceSubtotal += service.getPrice();
        }

        // Tính thuế (VAT 10% cho dịch vụ, vé không tính thuế)
        tax = serviceSubtotal * 0.10;
        
        // Tổng tiền
        totalAmount = ticketSubtotal + serviceSubtotal + tax;
    }

    // Inner class để lưu chi tiết từng mục
    public static class InvoiceItem {
        private String description;
        private int quantity;
        private double unitPrice;
        private double amount;

        public InvoiceItem(String description, int quantity, double unitPrice, double amount) {
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public double getAmount() {
            return amount;
        }
    }

    // Getters
    public String getId() {
        return id;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public Customer getCustomer() {
        return customer;
    }

    public LocalDateTime getIssueTime() {
        return issueTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTicketSubtotal() {
        return ticketSubtotal;
    }

    public double getServiceSubtotal() {
        return serviceSubtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<InvoiceItem> getTicketItems() {
        return ticketItems;
    }

    public List<InvoiceItem> getServiceItems() {
        return serviceItems;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════╗\n");
        sb.append("║                    HÓA ĐƠN THANH TOÁN                      ║\n");
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Mã hóa đơn: %-46s ║\n", id));
        sb.append(String.format("║ Ngày giờ: %-48s ║\n", issueTime.format(formatter)));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Khách hàng: %-46s ║\n", customer.getName()));
        sb.append(String.format("║ Số điện thoại: %-43s ║\n", customer.getPhone()));
        sb.append(String.format("║ Email: %-51s ║\n", customer.getEmail()));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Phim: %-52s ║\n", ticket.getShowTime().getMovie().getTitle()));
        sb.append(String.format("║ Phòng: %-51s ║\n", ticket.getShowTime().getRoom().getName()));
        sb.append(String.format("║ Giờ chiếu: %-47s ║\n", 
            ticket.getShowTime().getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append("║                     CHI TIẾT HÓA ĐƠN                       ║\n");
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        
        // Chi tiết vé
        if (!ticketItems.isEmpty()) {
            sb.append("║ VÉ XEM PHIM:                                               ║\n");
            for (InvoiceItem item : ticketItems) {
                sb.append(String.format("║   %-40s %15s ║\n", 
                    item.getDescription(), 
                    String.format("%,.0f VNĐ", item.getAmount())));
            }
            sb.append(String.format("║ Tạm tính vé: %45s ║\n", 
                String.format("%,.0f VNĐ", ticketSubtotal)));
            sb.append("║                                                            ║\n");
        }
        
        // Chi tiết dịch vụ
        if (!serviceItems.isEmpty()) {
            sb.append("║ DỊCH VỤ:                                                   ║\n");
            for (InvoiceItem item : serviceItems) {
                sb.append(String.format("║   %-40s %15s ║\n", 
                    item.getDescription(), 
                    String.format("%,.0f VNĐ", item.getAmount())));
            }
            sb.append(String.format("║ Tạm tính dịch vụ: %40s ║\n", 
                String.format("%,.0f VNĐ", serviceSubtotal)));
            sb.append(String.format("║ Thuế VAT (10%%): %41s ║\n", 
                String.format("%,.0f VNĐ", tax)));
            sb.append("║                                                            ║\n");
        }
        
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ TỔNG CỘNG: %47s ║\n", 
            String.format("%,.0f VNĐ", totalAmount)));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║ Phương thức thanh toán: %-31s ║\n", paymentMethod));
        sb.append(String.format("║ Điểm tích lũy được cộng: %-29d ║\n", 
            (int)(totalAmount / 10000)));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        sb.append("║          Cảm ơn quý khách! Hẹn gặp lại!                    ║\n");
        sb.append("╚════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }

    // Phương thức in hóa đơn đơn giản
    public String toSimpleString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("Hóa đơn %s | Khách hàng: %s | Ngày: %s | Tổng tiền: %,.0f VNĐ",
            id, customer.getName(), issueTime.format(formatter), totalAmount);
    }
}
