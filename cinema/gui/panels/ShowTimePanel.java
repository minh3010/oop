package cinema.gui.panels;

import cinema.dao.*;
import cinema.dao.impl.*;
import cinema.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ShowTimePanel extends JPanel {
    private ShowTimeDAO showTimeDAO;
    private MovieDAO movieDAO;
    private RoomDAO roomDAO;
    private JTable showTimeTable;
    private DefaultTableModel tableModel;
    private JTextField idField, priceField, dateTimeField;
    private JComboBox<String> movieComboBox, roomComboBox;

    public ShowTimePanel() {
        showTimeDAO = new ShowTimeDAOImpl();
        movieDAO = new MovieDAOImpl();
        roomDAO = new RoomDAOImpl();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadShowTimes();
    }

    private void initComponents() {
        JLabel titleLabel = new JLabel("QUẢN LÝ SUẤT CHIẾU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);
        
        String[] columns = {"Mã", "Phim", "Phòng", "Thời gian", "Giá vé", "Ghế trống"};
        tableModel = new DefaultTableModel(columns, 0);
        showTimeTable = new JTable(tableModel);
        add(new JScrollPane(showTimeTable), BorderLayout.CENTER);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin suất chiếu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã suất chiếu:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phim:"), gbc);
        gbc.gridx = 1;
        movieComboBox = new JComboBox<>();
        loadMovieComboBox();
        formPanel.add(movieComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Phòng:"), gbc);
        gbc.gridx = 1;
        roomComboBox = new JComboBox<>();
        loadRoomComboBox();
        formPanel.add(roomComboBox, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Thời gian:"), gbc);
        gbc.gridx = 1;
        dateTimeField = new JTextField(15);
        dateTimeField.setToolTipText("dd/MM/yyyy HH:mm");
        formPanel.add(dateTimeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Giá vé:"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(15);
        formPanel.add(priceField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addShowTime());
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> deleteShowTime());
        JButton clearButton = new JButton("Làm mới");
        clearButton.addActionListener(e -> clearForm());
        
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.EAST);
    }

    private void loadMovieComboBox() {
        try {
            movieComboBox.removeAllItems();
            List<Movie> movies = movieDAO.findAll();
            for (Movie movie : movies) {
                movieComboBox.addItem(movie.getId() + " - " + movie.getTitle());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRoomComboBox() {
        try {
            roomComboBox.removeAllItems();
            List<Room> rooms = roomDAO.findAll();
            for (Room room : rooms) {
                roomComboBox.addItem(room.getId() + " - " + room.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadShowTimes() {
        try {
            tableModel.setRowCount(0);
            List<ShowTime> showTimes = showTimeDAO.findAll();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (ShowTime st : showTimes) {
                tableModel.addRow(new Object[]{
                    st.getId(),
                    st.getMovie().getTitle(),
                    st.getRoom().getName(),
                    st.getStartTime().format(formatter),
                    String.format("%,.0f VNĐ", st.getTicketPrice()),
                    st.getAvailableSeats() + "/" + st.getRoom().getTotalSeats()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void addShowTime() {
        try {
            String movieId = ((String) movieComboBox.getSelectedItem()).split(" - ")[0];
            String roomId = ((String) roomComboBox.getSelectedItem()).split(" - ")[0];
            
            Movie movie = movieDAO.findById(movieId).orElse(null);
            Room room = roomDAO.findById(roomId).orElse(null);
            
            if (movie == null || room == null) {
                JOptionPane.showMessageDialog(this, "Phim hoặc phòng không tồn tại!");
                return;
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(dateTimeField.getText().trim(), formatter);
            
            ShowTime showTime = new ShowTime(
                idField.getText().trim(),
                movie,
                room,
                startTime,
                Double.parseDouble(priceField.getText().trim())
            );
            
            showTimeDAO.save(showTime);
            JOptionPane.showMessageDialog(this, "Thêm suất chiếu thành công!");
            loadShowTimes();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
        }
    }

    private void deleteShowTime() {
        int row = showTimeTable.getSelectedRow();
        if (row == -1) return;
        
        if (JOptionPane.showConfirmDialog(this, "Xóa suất chiếu này?") == JOptionPane.YES_OPTION) {
            try {
                showTimeDAO.delete((String) tableModel.getValueAt(row, 0));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadShowTimes();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        dateTimeField.setText("");
        priceField.setText("");
        if (movieComboBox.getItemCount() > 0) movieComboBox.setSelectedIndex(0);
        if (roomComboBox.getItemCount() > 0) roomComboBox.setSelectedIndex(0);
    }
}
