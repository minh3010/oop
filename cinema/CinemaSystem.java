package cinema;

import cinema.manager.*;
import cinema.model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class CinemaSystem {
    private MovieManager movieManager;
    private RoomManager roomManager;
    private ShowTimeManager showTimeManager;
    private CustomerManager customerManager;
    private ServiceManager serviceManager;
    private TicketManager ticketManager;
    private RevenueManager revenueManager;

    public CinemaSystem() {
        this.movieManager = new MovieManager();
        this.roomManager = new RoomManager();
        this.showTimeManager = new ShowTimeManager();
        this.customerManager = new CustomerManager();
        this.serviceManager = new ServiceManager();
        this.ticketManager = new TicketManager();
        this.revenueManager = new RevenueManager(ticketManager);
        initializeSampleData();
    }

    private void initializeSampleData() {
        // Thêm phim mẫu
        Movie movie1 = new Movie("M001", "Avengers: Endgame", "Hành động", 181, "Russo Brothers", "Cuộc chiến cuối cùng của các siêu anh hùng");
        Movie movie2 = new Movie("M002", "Parasite", "Tâm lý", 132, "Bong Joon-ho", "Câu chuyện về hai gia đình");
        Movie movie3 = new Movie("M003", "The Batman", "Hành động", 176, "Matt Reeves", "Người dơi trở lại");
        movieManager.addMovie(movie1);
        movieManager.addMovie(movie2);
        movieManager.addMovie(movie3);

        // Thêm phòng chiếu mẫu
        Room room1 = new Room("R001", "Phòng 1", 10, 12, "Standard");
        Room room2 = new Room("R002", "Phòng 2", 8, 10, "VIP");
        Room room3 = new Room("R003", "Phòng 3", 12, 15, "IMAX");
        roomManager.addRoom(room1);
        roomManager.addRoom(room2);
        roomManager.addRoom(room3);

        // Thêm suất chiếu mẫu
        ShowTime st1 = new ShowTime("ST001", movie1, room1, LocalDateTime.now().plusDays(1).withHour(14).withMinute(0), 80000);
        ShowTime st2 = new ShowTime("ST002", movie1, room2, LocalDateTime.now().plusDays(1).withHour(18).withMinute(30), 120000);
        ShowTime st3 = new ShowTime("ST003", movie2, room1, LocalDateTime.now().plusDays(2).withHour(15).withMinute(0), 75000);
        ShowTime st4 = new ShowTime("ST004", movie3, room3, LocalDateTime.now().plusDays(1).withHour(20).withMinute(0), 150000);
        showTimeManager.addShowTime(st1);
        showTimeManager.addShowTime(st2);
        showTimeManager.addShowTime(st3);
        showTimeManager.addShowTime(st4);

        // Thêm khách hàng mẫu
        Customer customer1 = new Customer("C001", "Nguyễn Văn A", "0901234567", "nguyenvana@email.com");
        Customer customer2 = new Customer("C002", "Trần Thị B", "0912345678", "tranthib@email.com");
        customerManager.addCustomer(customer1);
        customerManager.addCustomer(customer2);

        // Thêm dịch vụ mẫu
        Service service1 = new Service("S001", "Bỏng ngô", "POPCORN", 50000, 100, "L");
        Service service2 = new Service("S002", "Coca Cola", "DRINK", 30000, 150, "M");
        Service service3 = new Service("S003", "Combo 1", "COMBO", 70000, 50, "L");
        Service service4 = new Service("S004", "Nước suối", "DRINK", 15000, 200, "S");
        serviceManager.addService(service1);
        serviceManager.addService(service2);
        serviceManager.addService(service3);
        serviceManager.addService(service4);
    }

    // Getters cho các manager
    public MovieManager getMovieManager() {
        return movieManager;
    }

    public RoomManager getRoomManager() {
        return roomManager;
    }

    public ShowTimeManager getShowTimeManager() {
        return showTimeManager;
    }

    public CustomerManager getCustomerManager() {
        return customerManager;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public RevenueManager getRevenueManager() {
        return revenueManager;
    }

    // Phương thức bán vé
    public Ticket sellTicket(String showTimeId, String customerId, List<String> seats, List<String> serviceIds) {
        Optional<ShowTime> showTimeOpt = showTimeManager.findShowTimeById(showTimeId);
        Optional<Customer> customerOpt = customerManager.findCustomerById(customerId);

        if (!showTimeOpt.isPresent()) {
            System.out.println("Không tìm thấy suất chiếu!");
            return null;
        }

        if (!customerOpt.isPresent()) {
            System.out.println("Không tìm thấy khách hàng!");
            return null;
        }

        ShowTime showTime = showTimeOpt.get();
        Customer customer = customerOpt.get();

        // Kiểm tra ghế còn trống
        for (String seat : seats) {
            if (!showTime.isSeatAvailable(seat)) {
                System.out.println("Ghế " + seat + " đã được đặt!");
                return null;
            }
        }

        // Lấy danh sách dịch vụ
        List<Service> services = new ArrayList<>();
        for (String serviceId : serviceIds) {
            Optional<Service> serviceOpt = serviceManager.findServiceById(serviceId);
            if (serviceOpt.isPresent() && serviceOpt.get().isAvailable()) {
                services.add(serviceOpt.get());
            }
        }

        // Tạo vé
        Ticket ticket = ticketManager.createTicket(showTime, customer, seats, services);
        if (ticket != null) {
            System.out.println("\n✓ Bán vé thành công!");
            System.out.println(ticket);
        }
        return ticket;
    }

    // Phương thức thêm phim mới
    public void addNewMovie(String id, String title, String genre, int duration, String director, String description) {
        Movie movie = new Movie(id, title, genre, duration, director, description);
        movieManager.addMovie(movie);
        System.out.println("✓ Đã thêm phim: " + title);
    }

    // Phương thức thêm suất chiếu mới
    public void addNewShowTime(String id, String movieId, String roomId, LocalDateTime startTime, double price) {
        Optional<Movie> movieOpt = movieManager.findMovieById(movieId);
        Optional<Room> roomOpt = roomManager.findRoomById(roomId);

        if (!movieOpt.isPresent() || !roomOpt.isPresent()) {
            System.out.println("Không tìm thấy phim hoặc phòng chiếu!");
            return;
        }

        ShowTime showTime = new ShowTime(id, movieOpt.get(), roomOpt.get(), startTime, price);
        showTimeManager.addShowTime(showTime);
        System.out.println("✓ Đã thêm suất chiếu mới");
    }

    // Phương thức thêm khách hàng mới
    public void addNewCustomer(String id, String name, String phone, String email) {
        Customer customer = new Customer(id, name, phone, email);
        customerManager.addCustomer(customer);
        System.out.println("✓ Đã thêm khách hàng: " + name);
    }

    // Phương thức thêm phòng chiếu mới
    public void addNewRoom(String id, String name, int rows, int seatsPerRow, String type) {
        Room room = new Room(id, name, rows, seatsPerRow, type);
        roomManager.addRoom(room);
        System.out.println("✓ Đã thêm phòng chiếu: " + name);
    }

    // Phương thức thêm dịch vụ mới
    public void addNewService(String id, String name, String type, double price, int quantity, String size) {
        Service service = new Service(id, name, type, price, quantity, size);
        serviceManager.addService(service);
        System.out.println("✓ Đã thêm dịch vụ: " + name);
    }
}
