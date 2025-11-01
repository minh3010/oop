package cinema.gui.panels;

import cinema.dao.CustomerDAO;
import cinema.dao.impl.CustomerDAOImpl;
import cinema.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CustomerPanel extends JPanel {
    private CustomerDAO customerDAO;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, phoneField, emailField;

    public CustomerPanel() {
        customerDAO = new CustomerDAOImpl();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadCustomers();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã KH", "Tên", "Số điện thoại", "Email", "Điểm tích lũy"};
        tableModel = new DefaultTableModel(columns, 0);
        customerTable = new JTable(tableModel);
        add(new JScrollPane(customerTable), BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã KH:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(15);
        formPanel.add(phoneField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addCustomer());
        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> updateCustomer());
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> deleteCustomer());
        JButton clearButton = new JButton("Làm mới");
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.EAST);
        
        customerTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && customerTable.getSelectedRow() != -1) {
                loadSelectedCustomer();
            }
        });
    }

    private void loadCustomers() {
        try {
            tableModel.setRowCount(0);
            List<Customer> customers = customerDAO.findAll();
            for (Customer customer : customers) {
                tableModel.addRow(new Object[]{
                    customer.getId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getLoyaltyPoints()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void addCustomer() {
        try {
            Customer customer = new Customer(
                idField.getText().trim(),
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim()
            );
            customerDAO.save(customer);
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            loadCustomers();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void updateCustomer() {
        try {
            Customer customer = new Customer(
                idField.getText().trim(),
                nameField.getText().trim(),
                phoneField.getText().trim(),
                emailField.getText().trim()
            );
            customerDAO.update(customer);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadCustomers();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        int row = customerTable.getSelectedRow();
        if (row == -1) return;
        
        if (JOptionPane.showConfirmDialog(this, "Xóa khách hàng này?") == JOptionPane.YES_OPTION) {
            try {
                customerDAO.delete((String) tableModel.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadCustomers();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void loadSelectedCustomer() {
        int row = customerTable.getSelectedRow();
        if (row != -1) {
            idField.setText((String) tableModel.getValueAt(row, 0));
            nameField.setText((String) tableModel.getValueAt(row, 1));
            phoneField.setText((String) tableModel.getValueAt(row, 2));
            emailField.setText((String) tableModel.getValueAt(row, 3));
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        customerTable.clearSelection();
    }
}
