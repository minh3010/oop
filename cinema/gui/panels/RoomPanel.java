package cinema.gui.panels;

import cinema.dao.RoomDAO;
import cinema.dao.impl.RoomDAOImpl;
import cinema.model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoomPanel extends JPanel {
    private RoomDAO roomDAO;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, rowsField, seatsPerRowField;
    private JComboBox<String> typeComboBox;

    public RoomPanel() {
        roomDAO = new RoomDAOImpl();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadRooms();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("QUẢN LÝ PHÒNG CHIẾU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã phòng", "Tên phòng", "Loại", "Số ghế", "Hàng", "Ghế/hàng"};
        tableModel = new DefaultTableModel(columns, 0);
        roomTable = new JTable(tableModel);
        add(new JScrollPane(roomTable), BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phòng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã phòng:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên phòng:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 1;
        typeComboBox = new JComboBox<>(new String[]{"Standard", "VIP", "IMAX"});
        formPanel.add(typeComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Số hàng:"), gbc);
        gbc.gridx = 1;
        rowsField = new JTextField(15);
        formPanel.add(rowsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Ghế mỗi hàng:"), gbc);
        gbc.gridx = 1;
        seatsPerRowField = new JTextField(15);
        formPanel.add(seatsPerRowField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addRoom());
        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> updateRoom());
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> deleteRoom());
        JButton clearButton = new JButton("Làm mới");
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.EAST);
        
        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && roomTable.getSelectedRow() != -1) {
                loadSelectedRoom();
            }
        });
    }

    private void loadRooms() {
        try {
            tableModel.setRowCount(0);
            List<Room> rooms = roomDAO.findAll();
            for (Room room : rooms) {
                tableModel.addRow(new Object[]{
                    room.getId(), room.getName(), room.getRoomType(),
                    room.getTotalSeats(), room.getRows(), room.getSeatsPerRow()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void addRoom() {
        try {
            Room room = new Room(
                idField.getText().trim(),
                nameField.getText().trim(),
                Integer.parseInt(rowsField.getText().trim()),
                Integer.parseInt(seatsPerRowField.getText().trim()),
                (String) typeComboBox.getSelectedItem()
            );
            roomDAO.save(room);
            JOptionPane.showMessageDialog(this, "Thêm phòng thành công!");
            loadRooms();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void updateRoom() {
        try {
            Room room = new Room(
                idField.getText().trim(),
                nameField.getText().trim(),
                Integer.parseInt(rowsField.getText().trim()),
                Integer.parseInt(seatsPerRowField.getText().trim()),
                (String) typeComboBox.getSelectedItem()
            );
            roomDAO.update(room);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadRooms();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void deleteRoom() {
        int row = roomTable.getSelectedRow();
        if (row == -1) return;
        
        if (JOptionPane.showConfirmDialog(this, "Xóa phòng này?") == JOptionPane.YES_OPTION) {
            try {
                roomDAO.delete((String) tableModel.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadRooms();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void loadSelectedRoom() {
        int row = roomTable.getSelectedRow();
        if (row != -1) {
            idField.setText((String) tableModel.getValueAt(row, 0));
            nameField.setText((String) tableModel.getValueAt(row, 1));
            typeComboBox.setSelectedItem(tableModel.getValueAt(row, 2));
            rowsField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
            seatsPerRowField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        rowsField.setText("");
        seatsPerRowField.setText("");
        typeComboBox.setSelectedIndex(0);
        roomTable.clearSelection();
    }
}
