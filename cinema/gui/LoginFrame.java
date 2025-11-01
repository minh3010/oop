package cinema.gui;

import cinema.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;

    public LoginFrame() {
        setTitle("Cinema Management System - Đăng nhập");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(109, 213, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ RẠP PHIM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username label
        JLabel userLabel = new JLabel("Tên đăng nhập:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(userLabel, gbc);

        // Username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameField, gbc);

        // Password label
        JLabel passLabel = new JLabel("Mật khẩu:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(passLabel, gbc);

        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordField, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("Đăng nhập");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());

        exitButton = new JButton("Thoát");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(new Color(231, 76, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        // Info label
        JLabel infoLabel = new JLabel("<html><center>Mặc định: admin/admin123 hoặc staff/staff123</center></html>");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        gbc.gridy = 4;
        mainPanel.add(infoLabel, gbc);

        // Add Enter key listener
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });

        add(mainPanel);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập tên đăng nhập và mật khẩu!",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Test database connection first
        if (!DatabaseConnection.getInstance().testConnection()) {
            JOptionPane.showMessageDialog(this,
                "Không thể kết nối đến cơ sở dữ liệu!\nVui lòng kiểm tra cấu hình MySQL.",
                "Lỗi kết nối",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Authenticate user
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("full_name");
                String role = rs.getString("role");
                
                // Close login frame
                this.dispose();
                
                // Open main application
                SwingUtilities.invokeLater(() -> {
                    MainFrame mainFrame = new MainFrame(fullName, role);
                    mainFrame.setVisible(true);
                });
            } else {
                JOptionPane.showMessageDialog(this,
                    "Tên đăng nhập hoặc mật khẩu không đúng!",
                    "Lỗi đăng nhập",
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi đăng nhập: " + ex.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
