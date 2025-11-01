package cinema.gui.panels;

import cinema.dao.*;
import cinema.dao.impl.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class DashboardPanel extends JPanel {
    private MovieDAO movieDAO;
    private CustomerDAO customerDAO;
    private TicketDAO ticketDAO;
    private ShowTimeDAO showTimeDAO;

    public DashboardPanel() {
        movieDAO = new MovieDAOImpl();
        customerDAO = new CustomerDAOImpl();
        ticketDAO = new TicketDAOImpl();
        showTimeDAO = new ShowTimeDAOImpl();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        initComponents();
    }

    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(52, 152, 219));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("BẢNG ĐIỀU KHIỂN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        try {
            int movieCount = movieDAO.findAll().size();
            int customerCount = customerDAO.findAll().size();
            int ticketCount = ticketDAO.findAll().size();
            int showTimeCount = showTimeDAO.findUpcoming().size();
            
            statsPanel.add(createStatCard("Tổng số phim", String.valueOf(movieCount), new Color(52, 152, 219)));
            statsPanel.add(createStatCard("Khách hàng", String.valueOf(customerCount), new Color(46, 204, 113)));
            statsPanel.add(createStatCard("Vé đã bán", String.valueOf(ticketCount), new Color(155, 89, 182)));
            statsPanel.add(createStatCard("Suất chiếu sắp tới", String.valueOf(showTimeCount), new Color(230, 126, 34)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        add(statsPanel, BorderLayout.CENTER);
        
        // Welcome message
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.WHITE);
        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống Quản lý Rạp Phim");
        welcomeLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        welcomeLabel.setForeground(new Color(127, 140, 141));
        bottomPanel.add(welcomeLabel);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 48));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}
