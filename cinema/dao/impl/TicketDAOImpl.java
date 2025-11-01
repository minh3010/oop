package cinema.dao.impl;

import cinema.dao.TicketDAO;
import cinema.dao.ShowTimeDAO;
import cinema.dao.CustomerDAO;
import cinema.dao.ServiceDAO;
import cinema.database.DatabaseConnection;
import cinema.model.Ticket;
import cinema.model.ShowTime;
import cinema.model.Customer;
import cinema.model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDAOImpl implements TicketDAO {
    private ShowTimeDAO showTimeDAO;
    private CustomerDAO customerDAO;
    private ServiceDAO serviceDAO;

    public TicketDAOImpl() {
        this.showTimeDAO = new ShowTimeDAOImpl();
        this.customerDAO = new CustomerDAOImpl();
        this.serviceDAO = new ServiceDAOImpl();
    }

    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public String save(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO tickets (id, showtime_id, customer_id, purchase_time, total_price, invoice_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticket.getId());
            stmt.setString(2, ticket.getShowTime().getId());
            stmt.setString(3, ticket.getCustomer().getId());
            stmt.setTimestamp(4, Timestamp.valueOf(ticket.getPurchaseTime()));
            stmt.setDouble(5, ticket.getTotalPrice());
            stmt.setString(6, ticket.getInvoiceId());
            stmt.executeUpdate();

            // Save seats
            saveSeats(ticket);
            
            // Save services
            saveServices(ticket);
        }
        return ticket.getId();
    }

    private void saveSeats(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket_seats (ticket_id, seat_number) VALUES (?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (String seat : ticket.getSeats()) {
                stmt.setString(1, ticket.getId());
                stmt.setString(2, seat);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void saveServices(Ticket ticket) throws SQLException {
        String sql = "INSERT INTO ticket_services (ticket_id, service_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            for (Service service : ticket.getServices()) {
                stmt.setString(1, ticket.getId());
                stmt.setString(2, service.getId());
                stmt.setInt(3, 1);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public void update(Ticket ticket) throws SQLException {
        String sql = "UPDATE tickets SET showtime_id=?, customer_id=?, purchase_time=?, total_price=?, invoice_id=? WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticket.getShowTime().getId());
            stmt.setString(2, ticket.getCustomer().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(ticket.getPurchaseTime()));
            stmt.setDouble(4, ticket.getTotalPrice());
            stmt.setString(5, ticket.getInvoiceId());
            stmt.setString(6, ticket.getId());
            stmt.executeUpdate();

            // Update seats and services
            deleteSeats(ticket.getId());
            deleteServices(ticket.getId());
            saveSeats(ticket);
            saveServices(ticket);
        }
    }

    private void deleteSeats(String ticketId) throws SQLException {
        String sql = "DELETE FROM ticket_seats WHERE ticket_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        }
    }

    private void deleteServices(String ticketId) throws SQLException {
        String sql = "DELETE FROM ticket_services WHERE ticket_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticketId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM tickets WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<Ticket> findById(String id) throws SQLException {
        String sql = "SELECT * FROM tickets WHERE id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(extractTicket(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Ticket> findAll() throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY purchase_time DESC";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByCustomerId(String customerId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE customer_id=? ORDER BY purchase_time DESC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public List<Ticket> findByShowTimeId(String showTimeId) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE showtime_id=? ORDER BY purchase_time DESC";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, showTimeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        }
        return tickets;
    }

    @Override
    public int getNextTicketNumber() throws SQLException {
        String sql = "SELECT COUNT(*) + 1 AS next_num FROM tickets";
        try (Statement stmt = getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("next_num");
            }
        }
        return 1;
    }

    private Ticket extractTicket(ResultSet rs) throws SQLException {
        String showTimeId = rs.getString("showtime_id");
        String customerId = rs.getString("customer_id");
        
        Optional<ShowTime> showTimeOpt = showTimeDAO.findById(showTimeId);
        Optional<Customer> customerOpt = customerDAO.findById(customerId);
        
        if (!showTimeOpt.isPresent() || !customerOpt.isPresent()) {
            throw new SQLException("ShowTime or Customer not found for Ticket");
        }
        
        Ticket ticket = new Ticket(
            rs.getString("id"),
            showTimeOpt.get(),
            customerOpt.get()
        );
        
        ticket.setInvoiceId(rs.getString("invoice_id"));
        
        // Load seats
        loadSeats(ticket);
        
        // Load services
        loadServices(ticket);
        
        return ticket;
    }

    private void loadSeats(Ticket ticket) throws SQLException {
        String sql = "SELECT seat_number FROM ticket_seats WHERE ticket_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticket.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ticket.addSeat(rs.getString("seat_number"));
            }
        }
    }

    private void loadServices(Ticket ticket) throws SQLException {
        String sql = "SELECT service_id FROM ticket_services WHERE ticket_id=?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, ticket.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String serviceId = rs.getString("service_id");
                Optional<Service> serviceOpt = serviceDAO.findById(serviceId);
                if (serviceOpt.isPresent()) {
                    ticket.addService(serviceOpt.get());
                }
            }
        }
    }
}
