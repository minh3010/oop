package cinema;

import cinema.model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static CinemaSystem cinemaSystem;
    private static Scanner scanner;

    public static void main(String[] args) {
        cinemaSystem = new CinemaSystem();
        scanner = new Scanner(System.in);

        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   HỆ THỐNG QUẢN LÝ RẠP PHIM          ║");
        System.out.println("╚════════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Chọn chức năng: ");

            switch (choice) {
                case 1:
                    movieMenu();
                    break;
                case 2:
                    showTimeMenu();
                    break;
                case 3:
                    roomMenu();
                    break;
                case 4:
                    customerMenu();
                    break;
                case 5:
                    serviceMenu();
                    break;
                case 6:
                    sellTicketMenu();
                    break;
                case 7:
                    revenueMenu();
                    break;
                case 0:
                    System.out.println("\nCảm ơn bạn đã sử dụng hệ thống!");
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        }
        scanner.close();
    }

    private static void displayMainMenu() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│           MENU CHÍNH                   │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Quản lý phim                        │");
        System.out.println("│ 2. Quản lý suất chiếu                  │");
        System.out.println("│ 3. Quản lý phòng chiếu                 │");
        System.out.println("│ 4. Quản lý khách hàng                  │");
        System.out.println("│ 5. Quản lý dịch vụ                     │");
        System.out.println("│ 6. Bán vé                              │");
        System.out.println("│ 7. Thống kê doanh thu                  │");
        System.out.println("│ 0. Thoát                               │");
        System.out.println("└────────────────────────────────────────┘");
    }

    private static void movieMenu() {
        System.out.println("\n=== QUẢN LÝ PHIM ===");
        System.out.println("1. Xem danh sách phim");
        System.out.println("2. Thêm phim mới");
        System.out.println("3. Tìm kiếm phim");
        System.out.println("0. Quay lại");

        int choice = getIntInput("Chọn: ");
        switch (choice) {
            case 1:
                cinemaSystem.getMovieManager().displayAllMovies();
                break;
            case 2:
                addNewMovie();
                break;
            case 3:
                searchMovie();
                break;
        }
    }

    private static void addNewMovie() {
        System.out.println("\n--- THÊM PHIM MỚI ---");
        String id = getStringInput("Mã phim: ");
        String title = getStringInput("Tên phim: ");
        String genre = getStringInput("Thể loại: ");
        int duration = getIntInput("Thời lượng (phút): ");
        String director = getStringInput("Đạo diễn: ");
        String description = getStringInput("Mô tả: ");

        cinemaSystem.addNewMovie(id, title, genre, duration, director, description);
    }

    private static void searchMovie() {
        System.out.println("\n--- TÌM KIẾM PHIM ---");
        System.out.println("1. Tìm theo tên");
        System.out.println("2. Tìm theo thể loại");
        int choice = getIntInput("Chọn: ");

        if (choice == 1) {
            String title = getStringInput("Nhập tên phim: ");
            List<Movie> results = cinemaSystem.getMovieManager().searchByTitle(title);
            if (results.isEmpty()) {
                System.out.println("Không tìm thấy phim nào!");
            } else {
                for (Movie movie : results) {
                    System.out.println(movie);
                }
            }
        } else if (choice == 2) {
            String genre = getStringInput("Nhập thể loại: ");
            List<Movie> results = cinemaSystem.getMovieManager().searchByGenre(genre);
            if (results.isEmpty()) {
                System.out.println("Không tìm thấy phim nào!");
            } else {
                for (Movie movie : results) {
                    System.out.println(movie);
                }
            }
        }
    }

    private static void showTimeMenu() {
        System.out.println("\n=== QUẢN LÝ SUẤT CHIẾU ===");
        System.out.println("1. Xem tất cả suất chiếu");
        System.out.println("2. Xem suất chiếu sắp tới");
        System.out.println("3. Thêm suất chiếu mới");
        System.out.println("0. Quay lại");

        int choice = getIntInput("Chọn: ");
        switch (choice) {
            case 1:
                cinemaSystem.getShowTimeManager().displayAllShowTimes();
                break;
            case 2:
                cinemaSystem.getShowTimeManager().displayUpcomingShowTimes();
                break;
            case 3:
                addNewShowTime();
                break;
        }
    }

    private static void addNewShowTime() {
        System.out.println("\n--- THÊM SUẤT CHIẾU MỚI ---");
        cinemaSystem.getMovieManager().displayAllMovies();
        String movieId = getStringInput("Mã phim: ");

        cinemaSystem.getRoomManager().displayAllRooms();
        String roomId = getStringInput("Mã phòng: ");

        String id = getStringInput("Mã suất chiếu: ");
        System.out.println("Nhập thời gian chiếu (dd/MM/yyyy HH:mm): ");
        String timeStr = scanner.nextLine();
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            LocalDateTime startTime = LocalDateTime.parse(timeStr, formatter);
            double price = getDoubleInput("Giá vé: ");

            cinemaSystem.addNewShowTime(id, movieId, roomId, startTime, price);
        } catch (Exception e) {
            System.out.println("Định dạng thời gian không hợp lệ!");
        }
    }

    private static void roomMenu() {
        System.out.println("\n=== QUẢN LÝ PHÒNG CHIẾU ===");
        System.out.println("1. Xem danh sách phòng");
        System.out.println("2. Thêm phòng mới");
        System.out.println("0. Quay lại");

        int choice = getIntInput("Chọn: ");
        switch (choice) {
            case 1:
                cinemaSystem.getRoomManager().displayAllRooms();
                break;
            case 2:
                addNewRoom();
                break;
        }
    }

    private static void addNewRoom() {
        System.out.println("\n--- THÊM PHÒNG MỚI ---");
        String id = getStringInput("Mã phòng: ");
        String name = getStringInput("Tên phòng: ");
        int rows = getIntInput("Số hàng ghế: ");
        int seatsPerRow = getIntInput("Số ghế mỗi hàng: ");
        String type = getStringInput("Loại phòng (Standard/VIP/IMAX): ");

        cinemaSystem.addNewRoom(id, name, rows, seatsPerRow, type);
    }

    private static void customerMenu() {
        System.out.println("\n=== QUẢN LÝ KHÁCH HÀNG ===");
        System.out.println("1. Xem danh sách khách hàng");
        System.out.println("2. Thêm khách hàng mới");
        System.out.println("3. Tìm kiếm khách hàng");
        System.out.println("0. Quay lại");

        int choice = getIntInput("Chọn: ");
        switch (choice) {
            case 1:
                cinemaSystem.getCustomerManager().displayAllCustomers();
                break;
            case 2:
                addNewCustomer();
                break;
            case 3:
                searchCustomer();
                break;
        }
    }

    private static void addNewCustomer() {
        System.out.println("\n--- THÊM KHÁCH HÀNG MỚI ---");
        String id = getStringInput("Mã khách hàng: ");
        String name = getStringInput("Tên khách hàng: ");
        String phone = getStringInput("Số điện thoại: ");
        String email = getStringInput("Email: ");

        cinemaSystem.addNewCustomer(id, name, phone, email);
    }

    private static void searchCustomer() {
        System.out.println("\n--- TÌM KIẾM KHÁCH HÀNG ---");
        String name = getStringInput("Nhập tên khách hàng: ");
        List<Customer> results = cinemaSystem.getCustomerManager().searchByName(name);
        
        if (results.isEmpty()) {
            System.out.println("Không tìm thấy khách hàng nào!");
        } else {
            for (Customer customer : results) {
                System.out.println(customer);
            }
        }
    }

    private static void serviceMenu() {
        System.out.println("\n=== QUẢN LÝ DỊCH VỤ ===");
        System.out.println("1. Xem danh sách dịch vụ");
        System.out.println("2. Xem dịch vụ còn hàng");
        System.out.println("3. Thêm dịch vụ mới");
        System.out.println("0. Quay lại");

        int choice = getIntInput("Chọn: ");
        switch (choice) {
            case 1:
                cinemaSystem.getServiceManager().displayAllServices();
                break;
            case 2:
                cinemaSystem.getServiceManager().displayAvailableServices();
                break;
            case 3:
                addNewService();
                break;
        }
    }

    private static void addNewService() {
        System.out.println("\n--- THÊM DỊCH VỤ MỚI ---");
        String id = getStringInput("Mã dịch vụ: ");
        String name = getStringInput("Tên dịch vụ: ");
        String type = getStringInput("Loại (DRINK/POPCORN/COMBO): ");
        double price = getDoubleInput("Giá: ");
        int quantity = getIntInput("Số lượng: ");
        String size = getStringInput("Size (S/M/L): ");

        cinemaSystem.addNewService(id, name, type, price, quantity, size);
    }

    private static void sellTicketMenu() {
        System.out.println("\n=== BÁN VÉ ===");
        
        // Hiển thị suất chiếu sắp tới
        cinemaSystem.getShowTimeManager().displayUpcomingShowTimes();
        String showTimeId = getStringInput("Mã suất chiếu: ");

        // Hiển thị khách hàng
        cinemaSystem.getCustomerManager().displayAllCustomers();
        String customerId = getStringInput("Mã khách hàng: ");

        // Nhập ghế
        System.out.println("Nhập danh sách ghế (cách nhau bởi dấu phẩy, VD: A1,A2,A3): ");
        String seatsStr = scanner.nextLine();
        List<String> seats = new ArrayList<>();
        for (String seat : seatsStr.split(",")) {
            seats.add(seat.trim());
        }

        // Hiển thị dịch vụ
        cinemaSystem.getServiceManager().displayAvailableServices();
        System.out.println("Nhập mã dịch vụ (cách nhau bởi dấu phẩy, bỏ trống nếu không mua): ");
        String servicesStr = scanner.nextLine();
        List<String> serviceIds = new ArrayList<>();
        if (!servicesStr.trim().isEmpty()) {
            for (String serviceId : servicesStr.split(",")) {
                serviceIds.add(serviceId.trim());
            }
        }

        // Bán vé
        cinemaSystem.sellTicket(showTimeId, customerId, seats, serviceIds);
    }

    private static void revenueMenu() {
        System.out.println("\n=== THỐNG KÊ DOANH THU ===");
        System.out.println("1. Xem báo cáo tổng quan");
        System.out.println("2. Xem danh sách vé đã bán");
        System.out.println("0. Quay lại");

        int choice = getIntInput("Chọn: ");
        switch (choice) {
            case 1:
                cinemaSystem.getRevenueManager().displayRevenueReport();
                break;
            case 2:
                cinemaSystem.getTicketManager().displayAllTickets();
                break;
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ!");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số hợp lệ!");
            }
        }
    }
}
