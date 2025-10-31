package cinema.manager;

import cinema.model.Invoice;
import cinema.model.Ticket;
import cinema.model.Customer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvoiceManager {
    private List<Invoice> invoices;
    private int invoiceCounter;

    public InvoiceManager() {
        this.invoices = new ArrayList<>();
        this.invoiceCounter = 1;
    }

    public Invoice createInvoice(Ticket ticket, String paymentMethod) {
        String invoiceId = "INV" + String.format("%05d", invoiceCounter++);
        Invoice invoice = new Invoice(invoiceId, ticket, paymentMethod);
        invoices.add(invoice);
        return invoice;
    }

    public Optional<Invoice> findInvoiceById(String id) {
        return invoices.stream()
                .filter(inv -> inv.getId().equals(id))
                .findFirst();
    }

    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices);
    }

    public List<Invoice> getInvoicesByCustomer(Customer customer) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getCustomer().getId().equals(customer.getId())) {
                result.add(invoice);
            }
        }
        return result;
    }

    public List<Invoice> getInvoicesByDate(LocalDate date) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            if (invoice.getIssueTime().toLocalDate().equals(date)) {
                result.add(invoice);
            }
        }
        return result;
    }

    public List<Invoice> getInvoicesByDateRange(LocalDateTime start, LocalDateTime end) {
        List<Invoice> result = new ArrayList<>();
        for (Invoice invoice : invoices) {
            LocalDateTime issueTime = invoice.getIssueTime();
            if (!issueTime.isBefore(start) && !issueTime.isAfter(end)) {
                result.add(invoice);
            }
        }
        return result;
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTotalAmount();
        }
        return total;
    }

    public double getTotalRevenueByDate(LocalDate date) {
        double total = 0;
        for (Invoice invoice : getInvoicesByDate(date)) {
            total += invoice.getTotalAmount();
        }
        return total;
    }

    public double getTotalTicketRevenue() {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTicketSubtotal();
        }
        return total;
    }

    public double getTotalServiceRevenue() {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getServiceSubtotal();
        }
        return total;
    }

    public double getTotalTaxCollected() {
        double total = 0;
        for (Invoice invoice : invoices) {
            total += invoice.getTax();
        }
        return total;
    }

    public void displayAllInvoices() {
        if (invoices.isEmpty()) {
            System.out.println("Chưa có hóa đơn nào.");
            return;
        }
        System.out.println("\n=== DANH SÁCH HÓA ĐƠN ===");
        for (Invoice invoice : invoices) {
            System.out.println(invoice.toSimpleString());
        }
        System.out.println("\nTổng số hóa đơn: " + invoices.size());
    }

    public void displayInvoiceDetails(String invoiceId) {
        Optional<Invoice> invoiceOpt = findInvoiceById(invoiceId);
        if (invoiceOpt.isPresent()) {
            System.out.println(invoiceOpt.get());
        } else {
            System.out.println("Không tìm thấy hóa đơn với mã: " + invoiceId);
        }
    }

    public void displayInvoicesByCustomer(Customer customer) {
        List<Invoice> customerInvoices = getInvoicesByCustomer(customer);
        if (customerInvoices.isEmpty()) {
            System.out.println("Khách hàng chưa có hóa đơn nào.");
            return;
        }
        System.out.println("\n=== HÓA ĐƠN CỦA KHÁCH HÀNG: " + customer.getName() + " ===");
        for (Invoice invoice : customerInvoices) {
            System.out.println(invoice.toSimpleString());
        }
    }

    public void displayRevenueReport() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              BÁO CÁO DOANH THU TỪ HÓA ĐƠN                  ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println(String.format("║ Tổng số hóa đơn: %-42d ║", invoices.size()));
        System.out.println(String.format("║ Tổng doanh thu: %43s ║", 
            String.format("%,.0f VNĐ", getTotalRevenue())));
        System.out.println(String.format("║ Doanh thu từ vé: %40s ║", 
            String.format("%,.0f VNĐ", getTotalTicketRevenue())));
        System.out.println(String.format("║ Doanh thu từ dịch vụ: %36s ║", 
            String.format("%,.0f VNĐ", getTotalServiceRevenue())));
        System.out.println(String.format("║ Tổng thuế VAT thu được: %32s ║", 
            String.format("%,.0f VNĐ", getTotalTaxCollected())));
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    public void displayDailyReport(LocalDate date) {
        List<Invoice> dailyInvoices = getInvoicesByDate(date);
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  BÁO CÁO DOANH THU NGÀY                    ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println(String.format("║ Ngày: %-52s ║", date));
        System.out.println(String.format("║ Số hóa đơn: %-47d ║", dailyInvoices.size()));
        System.out.println(String.format("║ Tổng doanh thu: %43s ║", 
            String.format("%,.0f VNĐ", getTotalRevenueByDate(date))));
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
