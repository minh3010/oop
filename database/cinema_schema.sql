-- Cinema Management System Database Schema
-- MySQL Database

DROP DATABASE IF EXISTS cinema_db;
CREATE DATABASE cinema_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cinema_db;

-- Table: movies
CREATE TABLE movies (
    id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    genre VARCHAR(100),
    duration INT NOT NULL,
    director VARCHAR(255),
    description TEXT,
    is_showing BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: movie_cast
CREATE TABLE movie_cast (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id VARCHAR(50) NOT NULL,
    actor_name VARCHAR(255) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Table: rooms
CREATE TABLE rooms (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_seats INT NOT NULL,
    rows INT NOT NULL,
    seats_per_row INT NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: showtimes
CREATE TABLE showtimes (
    id VARCHAR(50) PRIMARY KEY,
    movie_id VARCHAR(50) NOT NULL,
    room_id VARCHAR(50) NOT NULL,
    start_time DATETIME NOT NULL,
    ticket_price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE
);

-- Table: showtime_booked_seats
CREATE TABLE showtime_booked_seats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    showtime_id VARCHAR(50) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id) ON DELETE CASCADE,
    UNIQUE KEY unique_seat_per_showtime (showtime_id, seat_number)
);

-- Table: customers
CREATE TABLE customers (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    loyalty_points INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_phone (phone)
);

-- Table: services
CREATE TABLE services (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    size VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: tickets
CREATE TABLE tickets (
    id VARCHAR(50) PRIMARY KEY,
    showtime_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    purchase_time DATETIME NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    invoice_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (showtime_id) REFERENCES showtimes(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Table: ticket_seats
CREATE TABLE ticket_seats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE
);

-- Table: ticket_services
CREATE TABLE ticket_services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    service_id VARCHAR(50) NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE
);

-- Table: invoices
CREATE TABLE invoices (
    id VARCHAR(50) PRIMARY KEY,
    ticket_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    issue_time DATETIME NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    ticket_subtotal DECIMAL(10, 2) NOT NULL,
    service_subtotal DECIMAL(10, 2) NOT NULL,
    tax DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ticket_id) REFERENCES tickets(id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Table: users (for login authentication)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'STAFF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data

-- Sample movies
INSERT INTO movies (id, title, genre, duration, director, description, is_showing) VALUES
('M001', 'Avengers: Endgame', 'Hành động', 181, 'Russo Brothers', 'Cuộc chiến cuối cùng của các siêu anh hùng', TRUE),
('M002', 'Parasite', 'Tâm lý', 132, 'Bong Joon-ho', 'Câu chuyện về hai gia đình', TRUE),
('M003', 'The Batman', 'Hành động', 176, 'Matt Reeves', 'Người dơi trở lại', TRUE);

-- Sample rooms
INSERT INTO rooms (id, name, total_seats, rows, seats_per_row, room_type) VALUES
('R001', 'Phòng 1', 120, 10, 12, 'Standard'),
('R002', 'Phòng 2', 80, 8, 10, 'VIP'),
('R003', 'Phòng 3', 180, 12, 15, 'IMAX');

-- Sample customers
INSERT INTO customers (id, name, phone, email, loyalty_points) VALUES
('C001', 'Nguyễn Văn A', '0901234567', 'nguyenvana@email.com', 0),
('C002', 'Trần Thị B', '0912345678', 'tranthib@email.com', 0);

-- Sample services
INSERT INTO services (id, name, type, price, quantity, size) VALUES
('S001', 'Bỏng ngô', 'POPCORN', 50000, 100, 'L'),
('S002', 'Coca Cola', 'DRINK', 30000, 150, 'M'),
('S003', 'Combo 1', 'COMBO', 70000, 50, 'L'),
('S004', 'Nước suối', 'DRINK', 15000, 200, 'S');

-- Sample showtimes (dates will be in the future when inserted)
INSERT INTO showtimes (id, movie_id, room_id, start_time, ticket_price) VALUES
('ST001', 'M001', 'R001', DATE_ADD(NOW(), INTERVAL 1 DAY), 80000),
('ST002', 'M001', 'R002', DATE_ADD(NOW(), INTERVAL 1 DAY), 120000),
('ST003', 'M002', 'R001', DATE_ADD(NOW(), INTERVAL 2 DAY), 75000),
('ST004', 'M003', 'R003', DATE_ADD(NOW(), INTERVAL 1 DAY), 150000);

-- Sample users for login
INSERT INTO users (username, password, full_name, role) VALUES
('admin', 'admin123', 'Administrator', 'ADMIN'),
('staff', 'staff123', 'Staff User', 'STAFF');

-- Create indexes for better performance
CREATE INDEX idx_showtimes_movie ON showtimes(movie_id);
CREATE INDEX idx_showtimes_room ON showtimes(room_id);
CREATE INDEX idx_showtimes_time ON showtimes(start_time);
CREATE INDEX idx_tickets_customer ON tickets(customer_id);
CREATE INDEX idx_tickets_showtime ON tickets(showtime_id);
CREATE INDEX idx_invoices_customer ON invoices(customer_id);
CREATE INDEX idx_customers_phone ON customers(phone);

-- Views for common queries

-- View: Upcoming showtimes with movie and room details
CREATE VIEW v_upcoming_showtimes AS
SELECT 
    s.id,
    s.start_time,
    s.ticket_price,
    m.title AS movie_title,
    m.genre,
    m.duration,
    r.name AS room_name,
    r.room_type,
    r.total_seats,
    (r.total_seats - COALESCE(bs.booked_count, 0)) AS available_seats
FROM showtimes s
JOIN movies m ON s.movie_id = m.id
JOIN rooms r ON s.room_id = r.id
LEFT JOIN (
    SELECT showtime_id, COUNT(*) AS booked_count
    FROM showtime_booked_seats
    GROUP BY showtime_id
) bs ON s.id = bs.showtime_id
WHERE s.start_time > NOW()
ORDER BY s.start_time;

-- View: Revenue summary
CREATE VIEW v_revenue_summary AS
SELECT 
    DATE(t.purchase_time) AS sale_date,
    COUNT(DISTINCT t.id) AS total_tickets,
    SUM(t.total_price) AS total_revenue,
    SUM(i.ticket_subtotal) AS ticket_revenue,
    SUM(i.service_subtotal) AS service_revenue,
    SUM(i.tax) AS total_tax
FROM tickets t
LEFT JOIN invoices i ON t.id = i.ticket_id
GROUP BY DATE(t.purchase_time);

-- View: Customer purchase history
CREATE VIEW v_customer_purchases AS
SELECT 
    c.id AS customer_id,
    c.name AS customer_name,
    c.phone,
    c.loyalty_points,
    COUNT(t.id) AS total_purchases,
    SUM(t.total_price) AS total_spent
FROM customers c
LEFT JOIN tickets t ON c.id = t.customer_id
GROUP BY c.id, c.name, c.phone, c.loyalty_points;

COMMIT;
