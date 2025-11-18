package cinema.gui.panels;

import cinema.dao.*;
import cinema.dao.impl.*;
import cinema.manager.InvoiceManager;
import cinema.manager.TicketManager;
import cinema.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingPanel extends JPanel {
    private ShowTimeDAO showTimeDAO;
    private CustomerDAO customerDAO;
    private ServiceDAO serviceDAO;
    private TicketManager ticketManager;
    private InvoiceManager invoiceManager;

    // Components for showtime selection
    private JComboBox<String> showTimeComboBox;
    private JTable showTimeTable;
    private DefaultTableModel showTimeTableModel;

    // Components for seat selection
    private JPanel seatPanel;
    private List<JButton> seatButtons;
    private List<String> selectedSeats;
    private ShowTime selectedShowTime;

    // Components for customer selection
    private JComboBox<String> customerComboBox;
    private JButton newCustomerButton;
    private Customer selectedCustomer;

    // Components for service selection
    private JTable serviceTable;
    private DefaultTableModel serviceTableModel;
    private List<Service> selectedServices;

    // Components for order summary
    private JTextArea orderSummaryArea;
    private JLabel totalLabel;
    private JComboBox<String> paymentMethodComboBox;
    private JButton bookButton;
    private JButton clearButton;

    public BookingPanel() {
        showTimeDAO = new ShowTimeDAOImpl();
        customerDAO = new CustomerDAOImpl();
        serviceDAO = new ServiceDAOImpl();
        ticketManager = new TicketManager();
        invoiceManager = new InvoiceManager();

        selectedSeats = new ArrayList<>();
        selectedServices = new ArrayList<>();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
        loadShowTimes();
        loadCustomers();
        loadServices();
    }

    private void initComponents() {
        // Main split pane
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.6);

        // Left panel - Selection area
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBackground(Color.WHITE);

        // Showtime selection
        JPanel showTimePanel = createShowTimePanel();
        leftPanel.add(showTimePanel, BorderLayout.NORTH);

        // Seat selection
        JPanel seatSelectionPanel = createSeatSelectionPanel();
        leftPanel.add(seatSelectionPanel, BorderLayout.CENTER);

        mainSplit.setLeftComponent(leftPanel);

        // Right panel - Order summary and payment
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(400, 600));

        // Customer and service selection
        JPanel customerServicePanel = createCustomerServicePanel();
        rightPanel.add(customerServicePanel, BorderLayout.NORTH);

        // Order summary
        JPanel orderPanel = createOrderPanel();
        rightPanel.add(orderPanel, BorderLayout.CENTER);

        // Payment panel
        JPanel paymentPanel = createPaymentPanel();
        rightPanel.add(paymentPanel, BorderLayout.SOUTH);

        mainSplit.setRightComponent(rightPanel);

        add(mainSplit, BorderLayout.CENTER);
    }

    private JPanel createShowTimePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Chọn suất chiếu"));
        panel.setBackground(Color.WHITE);

        // Showtime table
        String[] columns = {"Mã", "Phim", "Phòng", "Thời gian", "Giá vé", "Ghế trống"};
        showTimeTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        showTimeTable = new JTable(showTimeTableModel);
        showTimeTable.setRowHeight(25);
        showTimeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && showTimeTable.getSelectedRow() != -1) {
                selectShowTime();
            }
        });

        JScrollPane scrollPane = new JScrollPane(showTimeTable);
        scrollPane.setPreferredSize(new Dimension(600, 150));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSeatSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Chọn ghế"));
        panel.setBackground(Color.WHITE);

        seatPanel = new JPanel();
        seatPanel.setBackground(Color.WHITE);
        seatPanel.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(seatPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Selected seats label
        JPanel selectedSeatsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectedSeatsPanel.setBackground(Color.WHITE);
        selectedSeatsPanel.add(new JLabel("Ghế đã chọn: "));
        JLabel selectedSeatsLabel = new JLabel("Chưa chọn ghế nào");
        selectedSeatsLabel.setForeground(Color.BLUE);
        selectedSeatsPanel.add(selectedSeatsLabel);
        panel.add(selectedSeatsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCustomerServicePanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);

        // Customer selection
        JPanel customerPanel = new JPanel(new BorderLayout(5, 5));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Chọn khách hàng"));
        customerPanel.setBackground(Color.WHITE);

        customerComboBox = new JComboBox<>();
        customerComboBox.addActionListener(e -> selectCustomer());

        newCustomerButton = new JButton("Khách hàng mới");
        newCustomerButton.addActionListener(e -> createNewCustomer());

        JPanel customerButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        customerButtonPanel.setBackground(Color.WHITE);
        customerButtonPanel.add(customerComboBox);
        customerButtonPanel.add(newCustomerButton);

        customerPanel.add(customerButtonPanel, BorderLayout.CENTER);
        panel.add(customerPanel, BorderLayout.NORTH);

        // Service selection
        JPanel servicePanel = new JPanel(new BorderLayout(5, 5));
        servicePanel.setBorder(BorderFactory.createTitledBorder("Chọn dịch vụ"));
        servicePanel.setBackground(Color.WHITE);

        String[] serviceColumns = {"Chọn", "Tên", "Loại", "Size", "Giá", "Còn lại"};
        serviceTableModel = new DefaultTableModel(serviceColumns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;
            }
        };
        serviceTable = new JTable(serviceTableModel);
        serviceTable.getColumnModel().getColumn(0).setPreferredWidth(50);

        JScrollPane serviceScrollPane = new JScrollPane(serviceTable);
        serviceScrollPane.setPreferredSize(new Dimension(350, 200));
        servicePanel.add(serviceScrollPane, BorderLayout.CENTER);

        panel.add(servicePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Tóm tắt đơn hàng"));
        panel.setBackground(Color.WHITE);

        orderSummaryArea = new JTextArea(10, 30);
        orderSummaryArea.setEditable(false);
        orderSummaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(orderSummaryArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(new JLabel("Tổng cộng: "));
        totalLabel = new JLabel("0 VNĐ");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.RED);
        totalPanel.add(totalLabel);
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Thanh toán"));
        panel.setBackground(Color.WHITE);

        // Payment method
        JPanel paymentMethodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paymentMethodPanel.setBackground(Color.WHITE);
        paymentMethodPanel.add(new JLabel("Phương thức thanh toán:"));
        paymentMethodComboBox = new JComboBox<>(new String[]{"Tiền mặt", "Thẻ tín dụng", "Ví điện tử"});
        paymentMethodPanel.add(paymentMethodComboBox);
        panel.add(paymentMethodPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        bookButton = new JButton("Đặt vé & Thanh toán");
        bookButton.setBackground(new Color(46, 204, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.addActionListener(e -> processBooking());

        clearButton = new JButton("Làm mới");
        clearButton.setBackground(new Color(149, 165, 166));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearBooking());

        buttonPanel.add(bookButton);
        buttonPanel.add(clearButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        return panel;
    }

    private void loadShowTimes() {
        try {
            showTimeTableModel.setRowCount(0);
            List<ShowTime> showTimes = showTimeDAO.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            for (ShowTime st : showTimes) {
                if (st.getStartTime().isAfter(LocalDateTime.now())) {
                    showTimeTableModel.addRow(new Object[]{
                        st.getId(),
                        st.getMovie().getTitle(),
                        st.getRoom().getName(),
                        st.getStartTime().format(formatter),
                        String.format("%,.0f VNĐ", st.getTicketPrice()),
                        st.getAvailableSeats() + "/" + st.getRoom().getTotalSeats()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải suất chiếu: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try {
            customerComboBox.removeAllItems();
            customerComboBox.addItem("-- Chọn khách hàng --");
            List<Customer> customers = customerDAO.findAll();
            for (Customer customer : customers) {
                customerComboBox.addItem(customer.getId() + " - " + customer.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách khách hàng: " + e.getMessage());
        }
    }

    private void loadServices() {
        try {
            serviceTableModel.setRowCount(0);
            List<Service> services = serviceDAO.findAll();
            for (Service service : services) {
                if (service.isAvailable()) {
                    serviceTableModel.addRow(new Object[]{
                        false,
                        service.getName(),
                        service.getType(),
                        service.getSize(),
                        String.format("%,.0f VNĐ", service.getPrice()),
                        service.getQuantity()
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
        }
    }

    private void selectShowTime() {
        int row = showTimeTable.getSelectedRow();
        if (row != -1) {
            String showTimeId = (String) showTimeTableModel.getValueAt(row, 0);
            try {
                selectedShowTime = showTimeDAO.findById(showTimeId).orElse(null);
                if (selectedShowTime != null) {
                    displaySeatMap();
                    updateOrderSummary();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin suất chiếu: " + e.getMessage());
            }
        }
    }

    private void displaySeatMap() {
        seatPanel.removeAll();
        seatButtons = new ArrayList<>();

        if (selectedShowTime == null) return;

        Room room = selectedShowTime.getRoom();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        // Screen label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = room.getSeatsPerRow();
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel screenLabel = new JLabel("MÀN HÌNH");
        screenLabel.setFont(new Font("Arial", Font.BOLD, 14));
        screenLabel.setForeground(Color.BLUE);
        seatPanel.add(screenLabel, gbc);

        // Seat buttons
        for (int row = 0; row < room.getRows(); row++) {
            gbc.gridwidth = 1;
            gbc.gridy = row + 1;

            // Row label
            gbc.gridx = 0;
            char rowChar = (char) ('A' + row);
            JLabel rowLabel = new JLabel(String.valueOf(rowChar));
            rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
            seatPanel.add(rowLabel, gbc);

            for (int col = 0; col < room.getSeatsPerRow(); col++) {
                gbc.gridx = col + 1;
                String seatNumber = rowChar + String.valueOf(col + 1);
                JButton seatButton = createSeatButton(seatNumber);
                seatButtons.add(seatButton);
                seatPanel.add(seatButton, gbc);
            }
        }

        seatPanel.revalidate();
        seatPanel.repaint();
    }

    private JButton createSeatButton(String seatNumber) {
        JButton button = new JButton(seatNumber);
        button.setPreferredSize(new Dimension(40, 30));
        button.setFont(new Font("Arial", Font.BOLD, 10));

        if (selectedShowTime.isSeatAvailable(seatNumber)) {
            button.setBackground(Color.GREEN);
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(Color.RED);
            button.setForeground(Color.WHITE);
            button.setEnabled(false);
        }

        button.addActionListener(e -> toggleSeatSelection(seatNumber, button));
        return button;
    }

    private void toggleSeatSelection(String seatNumber, JButton button) {
        if (selectedSeats.contains(seatNumber)) {
            selectedSeats.remove(seatNumber);
            button.setBackground(Color.GREEN);
            button.setForeground(Color.BLACK);
        } else {
            selectedSeats.add(seatNumber);
            button.setBackground(Color.YELLOW);
            button.setForeground(Color.BLACK);
        }
        updateOrderSummary();
    }

    private void selectCustomer() {
        String selectedItem = (String) customerComboBox.getSelectedItem();
        if (selectedItem != null && !selectedItem.equals("-- Chọn khách hàng --")) {
            String customerId = selectedItem.split(" - ")[0];
            try {
                selectedCustomer = customerDAO.findById(customerId).orElse(null);
                updateOrderSummary();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi tải thông tin khách hàng: " + e.getMessage());
            }
        }
    }

    private void createNewCustomer() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Mã KH:"));
        panel.add(idField);
        panel.add(new JLabel("Tên:"));
        panel.add(nameField);
        panel.add(new JLabel("SĐT:"));
        panel.add(phoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm khách hàng mới",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Customer newCustomer = new Customer(
                    idField.getText().trim(),
                    nameField.getText().trim(),
                    phoneField.getText().trim(),
                    emailField.getText().trim()
                );
                customerDAO.save(newCustomer);
                selectedCustomer = newCustomer;
                loadCustomers();
                customerComboBox.setSelectedItem(newCustomer.getId() + " - " + newCustomer.getName());
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng: " + e.getMessage());
            }
        }
    }

    private void updateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        double total = 0;

        if (selectedShowTime != null) {
            summary.append("SUẤT CHIẾU:\n");
            summary.append("Phim: ").append(selectedShowTime.getMovie().getTitle()).append("\n");
            summary.append("Phòng: ").append(selectedShowTime.getRoom().getName()).append("\n");
            summary.append("Thời gian: ").append(selectedShowTime.getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n\n");

            if (!selectedSeats.isEmpty()) {
                summary.append("GHẾ ĐÃ CHỌN:\n");
                for (String seat : selectedSeats) {
                    summary.append("- Ghế ").append(seat).append(": ").append(String.format("%,.0f VNĐ", selectedShowTime.getTicketPrice())).append("\n");
                    total += selectedShowTime.getTicketPrice();
                }
                summary.append("\n");
            }
        }

        // Get selected services
        selectedServices.clear();
        for (int i = 0; i < serviceTableModel.getRowCount(); i++) {
            Boolean selected = (Boolean) serviceTableModel.getValueAt(i, 0);
            if (selected) {
                try {
                    String serviceName = (String) serviceTableModel.getValueAt(i, 1);
                    String serviceType = (String) serviceTableModel.getValueAt(i, 2);
                    String serviceSize = (String) serviceTableModel.getValueAt(i, 3);
                    Service service = serviceDAO.findByNameAndSize(serviceName, serviceSize).orElse(null);
                    if (service != null) {
                        selectedServices.add(service);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!selectedServices.isEmpty()) {
            summary.append("DỊCH VỤ:\n");
            for (Service service : selectedServices) {
                summary.append("- ").append(service.getName()).append(" (").append(service.getSize()).append("): ")
                       .append(String.format("%,.0f VNĐ", service.getPrice())).append("\n");
                total += service.getPrice();
            }
            summary.append("\n");
        }

        if (selectedCustomer != null) {
            summary.append("KHÁCH HÀNG: ").append(selectedCustomer.getName()).append("\n\n");
        }

        orderSummaryArea.setText(summary.toString());
        totalLabel.setText(String.format("%,.0f VNĐ", total));
    }

    private void processBooking() {
        if (selectedShowTime == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu!");
            return;
        }

        if (selectedSeats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!");
            return;
        }

        if (selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }

        try {
            // Create ticket
            Ticket ticket = ticketManager.createTicket(selectedShowTime, selectedCustomer, selectedSeats, selectedServices);

            if (ticket != null) {
                // Create invoice
                String paymentMethod = (String) paymentMethodComboBox.getSelectedItem();
                Invoice invoice = invoiceManager.createInvoice(ticket, paymentMethod);

                // Link invoice to ticket
                ticket.setInvoiceId(invoice.getId());

                // Show success message with invoice
                JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công!\n\n" + invoice.toString(),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);

                // Clear booking
                clearBooking();
                loadShowTimes();
                loadServices();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể tạo vé. Vui lòng kiểm tra lại thông tin!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi đặt vé: " + e.getMessage());
        }
    }

    private void clearBooking() {
        showTimeTable.clearSelection();
        selectedShowTime = null;
        selectedSeats.clear();
        selectedCustomer = null;
        selectedServices.clear();
        customerComboBox.setSelectedIndex(0);
        paymentMethodComboBox.setSelectedIndex(0);

        // Clear seat selection
        seatPanel.removeAll();
        seatPanel.revalidate();
        seatPanel.repaint();

        // Clear service selection
        for (int i = 0; i < serviceTableModel.getRowCount(); i++) {
            serviceTableModel.setValueAt(false, i, 0);
        }

        updateOrderSummary();
    }

    public void refreshData() {
        loadShowTimes();
        loadCustomers();
        loadServices();
        clearBooking();
    }
}