package cinema.gui;

import cinema.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private String currentUser;
    private String userRole;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels
    private DashboardPanel dashboardPanel;
    private MoviePanel moviePanel;
    private RoomPanel roomPanel;
    private ShowTimePanel showTimePanel;
    private CustomerPanel customerPanel;
    private ServicePanel servicePanel;
    private BookingPanel bookingPanel;
    private RevenuePanel revenuePanel;

    public MainFrame(String userName, String role) {
        this.currentUser = userName;
        this.userRole = role;
        
        setTitle("Cinema Management System - " + userName);
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initComponents();
    }

    private void initComponents() {
        // Menu bar
        createMenuBar();
        
        // Main content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Initialize panels
        dashboardPanel = new DashboardPanel();
        moviePanel = new MoviePanel();
        roomPanel = new RoomPanel();
        showTimePanel = new ShowTimePanel();
        customerPanel = new CustomerPanel();
        servicePanel = new ServicePanel();
        bookingPanel = new BookingPanel();
        revenuePanel = new RevenuePanel();
        
        // Add panels to content
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(moviePanel, "movies");
        contentPanel.add(roomPanel, "rooms");
        contentPanel.add(showTimePanel, "showtimes");
        contentPanel.add(customerPanel, "customers");
        contentPanel.add(servicePanel, "services");
        contentPanel.add(bookingPanel, "booking");
        contentPanel.add(revenuePanel, "revenue");
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Status bar
        createStatusBar();
        
        // Show dashboard by default
        cardLayout.show(contentPanel, "dashboard");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(52, 73, 94));
        
        // Dashboard menu
        JMenu dashboardMenu = createMenu("🏠 Trang chủ");
        JMenuItem dashboardItem = createMenuItem("Bảng điều khiển");
        dashboardItem.addActionListener(e -> showPanel("dashboard"));
        dashboardMenu.add(dashboardItem);
        menuBar.add(dashboardMenu);
        
        // Management menu
        JMenu managementMenu = createMenu("📋 Quản lý");
        
        JMenuItem movieItem = createMenuItem("Quản lý phim");
        movieItem.addActionListener(e -> showPanel("movies"));
        managementMenu.add(movieItem);
        
        JMenuItem roomItem = createMenuItem("Quản lý phòng chiếu");
        roomItem.addActionListener(e -> showPanel("rooms"));
        managementMenu.add(roomItem);
        
        JMenuItem showTimeItem = createMenuItem("Quản lý suất chiếu");
        showTimeItem.addActionListener(e -> showPanel("showtimes"));
        managementMenu.add(showTimeItem);
        
        JMenuItem customerItem = createMenuItem("Quản lý khách hàng");
        customerItem.addActionListener(e -> showPanel("customers"));
        managementMenu.add(customerItem);
        
        JMenuItem serviceItem = createMenuItem("Quản lý dịch vụ");
        serviceItem.addActionListener(e -> showPanel("services"));
        managementMenu.add(serviceItem);
        
        menuBar.add(managementMenu);
        
        // Booking menu
        JMenu bookingMenu = createMenu("🎫 Bán vé");
        JMenuItem bookingItem = createMenuItem("Đặt vé xem phim");
        bookingItem.addActionListener(e -> {
            showPanel("booking");
            bookingPanel.refreshData();
        });
        bookingMenu.add(bookingItem);
        menuBar.add(bookingMenu);
        
        // Revenue menu
        JMenu revenueMenu = createMenu("📊 Thống kê");
        JMenuItem revenueItem = createMenuItem("Báo cáo doanh thu");
        revenueItem.addActionListener(e -> {
            showPanel("revenue");
            revenuePanel.refreshData();
        });
        revenueMenu.add(revenueItem);
        menuBar.add(revenueMenu);
        
        // Help menu
        JMenu helpMenu = createMenu("❓ Trợ giúp");
        JMenuItem aboutItem = createMenuItem("Giới thiệu");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        helpMenu.addSeparator();
        
        JMenuItem logoutItem = createMenuItem("Đăng xuất");
        logoutItem.addActionListener(e -> logout());
        helpMenu.add(logoutItem);
        
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private JMenu createMenu(String text) {
        JMenu menu = new JMenu(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Arial", Font.BOLD, 13));
        return menu;
    }

    private JMenuItem createMenuItem(String text) {
        JMenuItem item = new JMenuItem(text);
        item.setFont(new Font("Arial", Font.PLAIN, 12));
        return item;
    }

    private void createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(52, 73, 94));
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel userLabel = new JLabel("Người dùng: " + currentUser + " (" + userRole + ")");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel timeLabel = new JLabel();
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Update time every second
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()));
        });
        timer.start();
        
        statusBar.add(userLabel, BorderLayout.WEST);
        statusBar.add(timeLabel, BorderLayout.EAST);
        
        add(statusBar, BorderLayout.SOUTH);
    }

    private void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
            "Hệ Thống Quản Lý Rạp Phim\n" +
            "Phiên bản 2.0\n\n" +
            "Tích hợp Java Swing GUI và MySQL Database\n" +
            "© 2025 Cinema Management System",
            "Giới thiệu",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
            });
        }
    }
}
