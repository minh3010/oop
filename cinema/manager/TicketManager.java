package cinema.manager;

import cinema.model.Ticket;
import cinema.model.ShowTime;
import cinema.model.Customer;
import cinema.model.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketManager {
    private List<Ticket> tickets;
    private int ticketCounter;

    public TicketManager() {
        this.tickets = new ArrayList<>();
        this.ticketCounter = 1;
    }

    public Ticket createTicket(ShowTime showTime, Customer customer, List<String> seats, List<Service> services) {
        String ticketId = "T" + String.format("%04d", ticketCounter++);
        Ticket ticket = new Ticket(ticketId, showTime, customer);
        
        for (String seat : seats) {
            if (showTime.bookSeat(seat)) {
                ticket.addSeat(seat);
            } else {
                System.out.println("Ghế " + seat + " đã được đặt!");
            }
        }
        
        for (Service service : services) {
            if (service.reduceQuantity(1)) {
                ticket.addService(service);
            } else {
                System.out.println("Dịch vụ " + service.getName() + " đã hết!");
            }
        }
        
        if (!ticket.getSeats().isEmpty()) {
            tickets.add(ticket);
            customer.addPurchase(ticket);
            return ticket;
        }
        
        return null;
    }

    public Optional<Ticket> findTicketById(String id) {
        return tickets.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public List<Ticket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

    public List<Ticket> getTicketsByCustomer(Customer customer) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getCustomer().getId().equals(customer.getId())) {
                result.add(ticket);
            }
        }
        return result;
    }

    public List<Ticket> getTicketsByShowTime(ShowTime showTime) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getShowTime().getId().equals(showTime.getId())) {
                result.add(ticket);
            }
        }
        return result;
    }

    public void displayAllTickets() {
        if (tickets.isEmpty()) {
            System.out.println("Chưa có vé nào được bán.");
            return;
        }
        System.out.println("\n=== DANH SÁCH VÉ ĐÃ BÁN ===");
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
            System.out.println();
        }
    }
}
