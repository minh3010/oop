package cinema.gui.panels;

import cinema.dao.MovieDAO;
import cinema.dao.impl.MovieDAOImpl;
import cinema.model.Movie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class MoviePanel extends JPanel {
    private MovieDAO movieDAO;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private JTextField idField, titleField, genreField, durationField, directorField;
    private JTextArea descriptionArea;
    private JCheckBox showingCheckBox;

    public MoviePanel() {
        movieDAO = new MovieDAOImpl();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initComponents();
        loadMovies();
    }

    private void initComponents() {
        // Title
        JLabel titleLabel = new JLabel("QUẢN LÝ PHIM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"Mã phim", "Tên phim", "Thể loại", "Thời lượng", "Đạo diễn", "Đang chiếu"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        movieTable = new JTable(tableModel);
        movieTable.setRowHeight(25);
        movieTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(movieTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin phim"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Mã phim:"), gbc);
        gbc.gridx = 1;
        idField = new JTextField(15);
        formPanel.add(idField, gbc);
        
        // Title
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên phim:"), gbc);
        gbc.gridx = 1;
        titleField = new JTextField(15);
        formPanel.add(titleField, gbc);
        
        // Genre
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Thể loại:"), gbc);
        gbc.gridx = 1;
        genreField = new JTextField(15);
        formPanel.add(genreField, gbc);
        
        // Duration
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Thời lượng (phút):"), gbc);
        gbc.gridx = 1;
        durationField = new JTextField(15);
        formPanel.add(durationField, gbc);
        
        // Director
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Đạo diễn:"), gbc);
        gbc.gridx = 1;
        directorField = new JTextField(15);
        formPanel.add(directorField, gbc);
        
        // Description
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 15);
        descriptionArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descriptionArea), gbc);
        
        // Showing checkbox
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        showingCheckBox = new JCheckBox("Đang chiếu");
        showingCheckBox.setSelected(true);
        formPanel.add(showingCheckBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton addButton = new JButton("Thêm");
        addButton.addActionListener(e -> addMovie());
        buttonPanel.add(addButton);
        
        JButton updateButton = new JButton("Cập nhật");
        updateButton.addActionListener(e -> updateMovie());
        buttonPanel.add(updateButton);
        
        JButton deleteButton = new JButton("Xóa");
        deleteButton.addActionListener(e -> deleteMovie());
        buttonPanel.add(deleteButton);
        
        JButton clearButton = new JButton("Làm mới");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);
        
        gbc.gridy = 7;
        formPanel.add(buttonPanel, gbc);
        
        add(formPanel, BorderLayout.EAST);
        
        // Table selection listener
        movieTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && movieTable.getSelectedRow() != -1) {
                loadSelectedMovie();
            }
        });
    }

    private void loadMovies() {
        try {
            tableModel.setRowCount(0);
            List<Movie> movies = movieDAO.findAll();
            for (Movie movie : movies) {
                tableModel.addRow(new Object[]{
                    movie.getId(),
                    movie.getTitle(),
                    movie.getGenre(),
                    movie.getDuration() + " phút",
                    movie.getDirector(),
                    movie.isShowing() ? "Có" : "Không"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh sách phim: " + e.getMessage());
        }
    }

    private void addMovie() {
        try {
            Movie movie = new Movie(
                idField.getText().trim(),
                titleField.getText().trim(),
                genreField.getText().trim(),
                Integer.parseInt(durationField.getText().trim()),
                directorField.getText().trim(),
                descriptionArea.getText().trim()
            );
            movie.setShowing(showingCheckBox.isSelected());
            
            movieDAO.save(movie);
            JOptionPane.showMessageDialog(this, "Thêm phim thành công!");
            loadMovies();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm phim: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng phải là số!");
        }
    }

    private void updateMovie() {
        try {
            Movie movie = new Movie(
                idField.getText().trim(),
                titleField.getText().trim(),
                genreField.getText().trim(),
                Integer.parseInt(durationField.getText().trim()),
                directorField.getText().trim(),
                descriptionArea.getText().trim()
            );
            movie.setShowing(showingCheckBox.isSelected());
            
            movieDAO.update(movie);
            JOptionPane.showMessageDialog(this, "Cập nhật phim thành công!");
            loadMovies();
            clearForm();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật phim: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Thời lượng phải là số!");
        }
    }

    private void deleteMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phim cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phim này?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                movieDAO.delete(id);
                JOptionPane.showMessageDialog(this, "Xóa phim thành công!");
                loadMovies();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa phim: " + e.getMessage());
            }
        }
    }

    private void loadSelectedMovie() {
        int selectedRow = movieTable.getSelectedRow();
        if (selectedRow != -1) {
            idField.setText((String) tableModel.getValueAt(selectedRow, 0));
            titleField.setText((String) tableModel.getValueAt(selectedRow, 1));
            genreField.setText((String) tableModel.getValueAt(selectedRow, 2));
            String duration = (String) tableModel.getValueAt(selectedRow, 3);
            durationField.setText(duration.replace(" phút", ""));
            directorField.setText((String) tableModel.getValueAt(selectedRow, 4));
            
            try {
                Movie movie = movieDAO.findById(idField.getText()).orElse(null);
                if (movie != null) {
                    descriptionArea.setText(movie.getDescription());
                    showingCheckBox.setSelected(movie.isShowing());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        titleField.setText("");
        genreField.setText("");
        durationField.setText("");
        directorField.setText("");
        descriptionArea.setText("");
        showingCheckBox.setSelected(true);
        movieTable.clearSelection();
    }
}
