# Hệ Thống Quản Lý Rạp Phim (Cinema Management System)

## Mô tả
Hệ thống quản lý rạp phim được xây dựng bằng Java với kiến trúc OOP, bao gồm đầy đủ các chức năng:

### Chức năng chính:
1. **Quản lý phim** - Thêm, xem, tìm kiếm phim
2. **Quản lý suất chiếu** - Tạo và quản lý lịch chiếu phim
3. **Quản lý phòng chiếu** - Quản lý các phòng chiếu (Standard, VIP, IMAX)
4. **Quản lý khách hàng** - Lưu trữ thông tin khách hàng và điểm tích lũy
5. **Bán vé** - Đặt vé xem phim với chọn ghế ngồi
6. **Quản lý dịch vụ** - Bán bỏng ngô, nước uống, combo
7. **Thống kê doanh thu** - Báo cáo doanh thu chi tiết

## Cấu trúc dự án

```
cinema/
├── model/              # Các entity classes
│   ├── Movie.java      # Lớp Phim
│   ├── Room.java       # Lớp Phòng chiếu
│   ├── ShowTime.java   # Lớp Suất chiếu
│   ├── Customer.java   # Lớp Khách hàng
│   ├── Ticket.java     # Lớp Vé
│   └── Service.java    # Lớp Dịch vụ
│
├── manager/            # Các manager classes
│   ├── MovieManager.java       # Quản lý phim
│   ├── RoomManager.java        # Quản lý phòng
│   ├── ShowTimeManager.java    # Quản lý suất chiếu
│   ├── CustomerManager.java    # Quản lý khách hàng
│   ├── ServiceManager.java     # Quản lý dịch vụ
│   ├── TicketManager.java      # Quản lý vé
│   └── RevenueManager.java     # Quản lý doanh thu
│
├── CinemaSystem.java   # Lớp hệ thống chính
└── Main.java           # Lớp main với menu console
```

## Yêu cầu hệ thống
- Java JDK 8 trở lên

## Hướng dẫn cài đặt và chạy

### 1. Cài đặt Java (nếu chưa có)

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install default-jdk
```

**macOS:**
```bash
brew install openjdk
```

**Windows:**
Tải và cài đặt từ [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) hoặc [OpenJDK](https://adoptium.net/)

### 2. Compile chương trình

```bash
# Di chuyển đến thư mục dự án
cd /vercel/sandbox

# Compile tất cả các file Java
javac cinema/model/*.java cinema/manager/*.java cinema/*.java
```

### 3. Chạy chương trình

```bash
# Chạy từ thư mục gốc
java cinema.Main
```

## Hướng dẫn sử dụng

### Menu chính
Khi chạy chương trình, bạn sẽ thấy menu chính với các tùy chọn:

```
┌────────────────────────────────────────┐
│           MENU CHÍNH                   │
├────────────────────────────────────────┤
│ 1. Quản lý phim                        │
│ 2. Quản lý suất chiếu                  │
│ 3. Quản lý phòng chiếu                 │
│ 4. Quản lý khách hàng                  │
│ 5. Quản lý dịch vụ                     │
│ 6. Bán vé                              │
│ 7. Thống kê doanh thu                  │
│ 0. Thoát                               │
└────────────────────────────────────────┘
```

### Dữ liệu mẫu
Hệ thống đã được khởi tạo với dữ liệu mẫu bao gồm:
- 3 phim: Avengers: Endgame, Parasite, The Batman
- 3 phòng chiếu: Standard, VIP, IMAX
- 4 suất chiếu sắp tới
- 2 khách hàng
- 4 dịch vụ: Bỏng ngô, Coca Cola, Combo, Nước suối

### Ví dụ bán vé

1. Chọn **6. Bán vé** từ menu chính
2. Chọn mã suất chiếu (VD: ST001)
3. Chọn mã khách hàng (VD: C001)
4. Nhập ghế ngồi (VD: A1,A2,A3)
5. Chọn dịch vụ (VD: S001,S002) hoặc bỏ trống
6. Hệ thống sẽ in vé và cập nhật doanh thu

### Thống kê doanh thu

Chọn **7. Thống kê doanh thu** để xem:
- Tổng doanh thu
- Doanh thu từ vé
- Doanh thu từ dịch vụ
- Số vé đã bán
- Doanh thu theo từng phim

## Kiến trúc OOP

### Các nguyên tắc OOP được áp dụng:

1. **Encapsulation (Đóng gói)**
   - Tất cả thuộc tính đều private
   - Truy cập thông qua getter/setter

2. **Abstraction (Trừu tượng)**
   - Tách biệt logic nghiệp vụ vào các Manager classes
   - Ẩn chi tiết triển khai

3. **Inheritance (Kế thừa)**
   - Có thể mở rộng với các loại phòng chiếu đặc biệt
   - Có thể tạo các loại khách hàng khác nhau (VIP, Regular)

4. **Polymorphism (Đa hình)**
   - Override phương thức toString() cho tất cả các entity
   - Có thể mở rộng với các loại dịch vụ khác nhau

## Tính năng nổi bật

### 1. Quản lý ghế ngồi
- Kiểm tra ghế trống/đã đặt
- Đặt nhiều ghế cùng lúc
- Hiển thị số ghế còn trống

### 2. Hệ thống điểm tích lũy
- Khách hàng nhận điểm khi mua vé
- 1 điểm cho mỗi 10,000 VNĐ chi tiêu

### 3. Quản lý dịch vụ
- Theo dõi số lượng tồn kho
- Tự động giảm số lượng khi bán
- Hiển thị dịch vụ còn hàng

### 4. Báo cáo doanh thu
- Tổng doanh thu
- Phân tích theo phim
- Phân tích theo nguồn (vé/dịch vụ)

## Mở rộng trong tương lai

Có thể mở rộng hệ thống với:
- Giao diện đồ họa (GUI) với JavaFX hoặc Swing
- Lưu trữ dữ liệu vào database (MySQL, PostgreSQL)
- API REST để tích hợp với web/mobile
- Hệ thống đặt vé online
- Thanh toán điện tử
- Quản lý nhân viên
- Hệ thống khuyến mãi và voucher

## Tác giả
Hệ thống quản lý rạp phim - Cinema Management System

## License
MIT License
