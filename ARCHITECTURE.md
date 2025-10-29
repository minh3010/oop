# Kiến trúc Hệ thống Quản lý Rạp Phim

## Sơ đồ tổng quan

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Console Interface)                       │
└──────────────────────┬──────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────┐
│                    CinemaSystem.java                         │
│              (Central Management System)                     │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
        ▼              ▼              ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│   Manager    │ │   Manager    │ │   Manager    │
│   Classes    │ │   Classes    │ │   Classes    │
└──────┬───────┘ └──────┬───────┘ └──────┬───────┘
       │                │                │
       ▼                ▼                ▼
┌──────────────────────────────────────────────────────────────┐
│                      Model Classes                            │
│  (Movie, Room, ShowTime, Customer, Ticket, Service)          │
└──────────────────────────────────────────────────────────────┘
```

## Chi tiết các lớp

### 1. Model Package (cinema.model)

#### Movie.java
```
Thuộc tính:
- id: String
- title: String
- genre: String
- duration: int
- director: String
- description: String
- isShowing: boolean
- cast: List<String>

Phương thức:
+ getters/setters
+ toString(): String
```

#### Room.java
```
Thuộc tính:
- id: String
- name: String
- totalSeats: int
- rows: int
- seatsPerRow: int
- roomType: String

Phương thức:
+ getters/setters
+ toString(): String
```

#### ShowTime.java
```
Thuộc tính:
- id: String
- movie: Movie
- room: Room
- startTime: LocalDateTime
- ticketPrice: double
- bookedSeats: Set<String>

Phương thức:
+ bookSeat(String): boolean
+ isSeatAvailable(String): boolean
+ getAvailableSeats(): int
+ getters/setters
+ toString(): String
```

#### Customer.java
```
Thuộc tính:
- id: String
- name: String
- phone: String
- email: String
- loyaltyPoints: int
- purchaseHistory: List<Ticket>

Phương thức:
+ addPurchase(Ticket): void
+ getters/setters
+ toString(): String
```

#### Ticket.java
```
Thuộc tính:
- id: String
- showTime: ShowTime
- customer: Customer
- seats: List<String>
- services: List<Service>
- purchaseTime: LocalDateTime
- totalPrice: double

Phương thức:
+ addSeat(String): void
+ addService(Service): void
- calculateTotalPrice(): void
+ getters/setters
+ toString(): String
```

#### Service.java
```
Thuộc tính:
- id: String
- name: String
- type: String
- price: double
- quantity: int
- size: String

Phương thức:
+ isAvailable(): boolean
+ reduceQuantity(int): boolean
+ addQuantity(int): void
+ getters/setters
+ toString(): String
```

### 2. Manager Package (cinema.manager)

#### MovieManager.java
```
Chức năng:
- Quản lý danh sách phim
- Thêm/xóa phim
- Tìm kiếm phim theo ID, tên, thể loại
- Lấy danh sách phim đang chiếu
- Hiển thị danh sách phim
```

#### RoomManager.java
```
Chức năng:
- Quản lý danh sách phòng chiếu
- Thêm/xóa phòng
- Tìm kiếm phòng theo ID, loại
- Hiển thị danh sách phòng
```

#### ShowTimeManager.java
```
Chức năng:
- Quản lý danh sách suất chiếu
- Thêm/xóa suất chiếu
- Tìm kiếm suất chiếu theo ID, phim, phòng
- Lấy suất chiếu sắp tới
- Hiển thị danh sách suất chiếu
```

#### CustomerManager.java
```
Chức năng:
- Quản lý danh sách khách hàng
- Thêm/xóa khách hàng
- Tìm kiếm khách hàng theo ID, SĐT, tên
- Lấy top khách hàng VIP
- Hiển thị danh sách khách hàng
```

#### ServiceManager.java
```
Chức năng:
- Quản lý danh sách dịch vụ
- Thêm/xóa dịch vụ
- Tìm kiếm dịch vụ theo ID, loại
- Lấy dịch vụ còn hàng
- Hiển thị danh sách dịch vụ
```

#### TicketManager.java
```
Chức năng:
- Quản lý danh sách vé
- Tạo vé mới
- Tìm kiếm vé theo ID, khách hàng, suất chiếu
- Hiển thị danh sách vé đã bán
```

#### RevenueManager.java
```
Chức năng:
- Tính tổng doanh thu
- Doanh thu theo ngày/khoảng thời gian
- Doanh thu theo phim
- Doanh thu từ vé/dịch vụ
- Thống kê số vé đã bán
- Hiển thị báo cáo doanh thu
```

### 3. CinemaSystem.java

```
Chức năng:
- Khởi tạo tất cả các Manager
- Khởi tạo dữ liệu mẫu
- Cung cấp API cho các chức năng chính:
  + sellTicket()
  + addNewMovie()
  + addNewShowTime()
  + addNewCustomer()
  + addNewRoom()
  + addNewService()
- Cung cấp getter cho các Manager
```

### 4. Main.java

```
Chức năng:
- Giao diện console
- Menu điều hướng
- Xử lý input từ người dùng
- Gọi các phương thức từ CinemaSystem
- Hiển thị kết quả
```

## Luồng hoạt động chính

### Luồng bán vé

```
1. User chọn "Bán vé" từ menu
   ↓
2. Main.java hiển thị danh sách suất chiếu
   ↓
3. User nhập: mã suất chiếu, mã khách hàng, ghế, dịch vụ
   ↓
4. Main.java gọi CinemaSystem.sellTicket()
   ↓
5. CinemaSystem kiểm tra:
   - Suất chiếu có tồn tại?
   - Khách hàng có tồn tại?
   - Ghế còn trống?
   - Dịch vụ còn hàng?
   ↓
6. TicketManager.createTicket()
   ↓
7. ShowTime.bookSeat() - đánh dấu ghế đã đặt
   ↓
8. Service.reduceQuantity() - giảm số lượng dịch vụ
   ↓
9. Customer.addPurchase() - thêm vào lịch sử mua hàng
   ↓
10. Tính điểm tích lũy cho khách hàng
   ↓
11. Trả về Ticket và hiển thị thông tin vé
```

### Luồng thống kê doanh thu

```
1. User chọn "Thống kê doanh thu"
   ↓
2. Main.java gọi RevenueManager
   ↓
3. RevenueManager lấy danh sách vé từ TicketManager
   ↓
4. Tính toán:
   - Tổng doanh thu
   - Doanh thu từ vé
   - Doanh thu từ dịch vụ
   - Doanh thu theo phim
   ↓
5. Hiển thị báo cáo
```

## Nguyên tắc thiết kế

### 1. Single Responsibility Principle (SRP)
- Mỗi class chỉ có một trách nhiệm duy nhất
- Model classes: Chỉ chứa dữ liệu và logic liên quan đến chính nó
- Manager classes: Chỉ quản lý một loại entity

### 2. Open/Closed Principle (OCP)
- Có thể mở rộng chức năng mà không cần sửa code cũ
- VD: Thêm loại phòng mới, loại dịch vụ mới

### 3. Dependency Inversion Principle (DIP)
- CinemaSystem phụ thuộc vào abstraction (Manager interfaces)
- Dễ dàng thay đổi implementation

### 4. Encapsulation
- Tất cả thuộc tính đều private
- Truy cập thông qua getter/setter

### 5. Separation of Concerns
- Model: Dữ liệu
- Manager: Logic nghiệp vụ
- CinemaSystem: Điều phối
- Main: Giao diện người dùng

## Mở rộng trong tương lai

### 1. Thêm Database Layer
```
┌─────────────┐
│   Manager   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│     DAO     │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│  Database   │
└─────────────┘
```

### 2. Thêm API Layer
```
┌─────────────┐
│  REST API   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Service   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Manager   │
└─────────────┘
```

### 3. Thêm GUI Layer
```
┌─────────────┐
│  JavaFX UI  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Controller  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Manager   │
└─────────────┘
```
