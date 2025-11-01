package cinema.gui.panels;

import cinema.dao.ServiceDAO;
import cinema.dao.impl.ServiceDAOImpl;
import cinema.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ServicePanel extends JPanel {
    private ServiceDAO serviceDAO;
    private JTable serviceTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, priceField, quantityField;
    private JComboBox<String> typeComboBox, sizeComboBox;

    public ServicePanel() {
        serviceDAO = new ServiceDAOImpl();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadServices();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("QUẢN LÝ DỊCH VỤ");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã", "Tên", "Loại", "Giá", "Số lượng", "Size"};
        tableModel = new DefaultTableModel(columns, 0);
        serviceTable = new JTable(tableModel);
        add(new JScrollPane(serviceTable), BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin dịch vụ"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Loại:"), gbc);
        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"DRINK", "POPCORN", "COMBO"});
        formPanel.add(typeComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(15);
        formPanel.add(priceField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(15);
        formPanel.add(quantityField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Size:"), gbc);
        gbc.gridx = 1;
        sizeComboBox = new JComboBox<>(new String[]{"S", "M", "L"});
        formPanel.add(sizeComboBox, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addService());
        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> updateService());
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> deleteService());
        JButton clearButton = new JButton("Làm mới");
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.EAST);
        
        serviceTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && serviceTable.getSelectedRow() != -1) {
                loadSelectedService();
            }
        });
    }

    private void loadServices() {
        try {
            tableModel.setRowCount(0);
            List<Service> services = serviceDAO.findAll();
            for (Service service : services) {
                tableModel.addRow(new Object[]{
                    service.getId(),
                    service.getName(),
                    service.getType(),
                    String.format("%,.0f VNĐ", service.getPrice()),
                    service.getQuantity(),
                    service.getSize()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void addService() {
        try {
            Service service = new Service(
                idField.getText().trim(),
                nameField.getText().trim(),
                (String) typeComboBox.getSelectedItem(),
                Double.parseDouble(priceField.getText().trim()),
                Integer.parseInt(quantityField.getText().trim()),
                (String) sizeComboBox.getSelectedItem()
            );
            serviceDAO.save(service);
            JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
            loadServices();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void updateService() {
        try {
            Service service = new Service(
                idField.getText().trim(),
                nameField.getText().trim(),
                (String) typeComboBox.getSelectedItem(),
                Double.parseDouble(priceField.getText().trim()),
                Integer.parseInt(quantityField.getText().trim()),
                (String) sizeComboBox.getSelectedItem()
            );
            serviceDAO.update(service);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadServices();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void deleteService() {
        int row = serviceTable.getSelectedRow();
        if (row == -1) return;
        
        if (JOptionPane.showConfirmDialog(this, "Xóa dịch vụ này?") == JOptionPane.YES_OPTION) {
            try {
                serviceDAO.delete((String) tableModel.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadServices();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void loadSelectedService() {
        int row = serviceTable.getSelectedRow();
        if (row != -1) {
            idField.setText((String) tableModel.getValueAt(row, 0));
            nameField.setText((String) tableModel.getValueAt(row, 1));
            typeComboBox.setSelectedItem(tableModel.getValueAt(row, 2));
            String price = (String) tableModel.getValueAt(row, 3);
            priceField.setText(price.replace(" VNĐ", "").replace(",", ""));
            quantityField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            sizeComboBox.setSelectedItem(tableModel.getValueAt(row, 5));
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        typeComboBox.setSelectedIndex(0);
        sizeComboBox.setSelectedIndex(0);
        serviceTable.clearSelection();
    }
}
