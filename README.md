# Spring Boot REST API Demo

Ứng dụng Spring Boot với các API cơ bản (CRUD) để quản lý User.

## Công nghệ sử dụng

- **Spring Boot 3.2.0**
- **Spring Data JPA** - Quản lý database
- **H2 Database** - In-memory database
- **Lombok** - Giảm boilerplate code
- **Validation** - Validate dữ liệu đầu vào
- **Maven** - Build tool

## Cài đặt và chạy

### Yêu cầu
- Java 17 hoặc cao hơn
- Maven 3.6+

### Chạy ứng dụng

```bash
# Build project
mvn clean install

# Chạy ứng dụng
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: `http://localhost:8080`

## API Endpoints

### 1. GET - Lấy tất cả users
```
GET http://localhost:8080/api/users
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Nguyen Van A",
    "email": "nguyenvana@example.com",
    "phone": "0123456789",
    "address": "Ha Noi"
  }
]
```

### 2. GET - Lấy user theo ID
```
GET http://localhost:8080/api/users/{id}
```

**Example:**
```
GET http://localhost:8080/api/users/1
```

### 3. POST - Tạo user mới
```
POST http://localhost:8080/api/users
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "phone": "0123456789",
  "address": "Ha Noi"
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "phone": "0123456789",
  "address": "Ha Noi"
}
```

### 4. PUT - Cập nhật user
```
PUT http://localhost:8080/api/users/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "Nguyen Van B",
  "email": "nguyenvanb@example.com",
  "phone": "0987654321",
  "address": "Ho Chi Minh"
}
```

### 5. DELETE - Xóa user
```
DELETE http://localhost:8080/api/users/{id}
```

**Example:**
```
DELETE http://localhost:8080/api/users/1
```

### 6. GET - Tìm user theo email
```
GET http://localhost:8080/api/users/search?email=nguyenvana@example.com
```

## Test API với cURL

### Tạo user mới
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A",
    "email": "nguyenvana@example.com",
    "phone": "0123456789",
    "address": "Ha Noi"
  }'
```

### Lấy tất cả users
```bash
curl http://localhost:8080/api/users
```

### Lấy user theo ID
```bash
curl http://localhost:8080/api/users/1
```

### Cập nhật user
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van B",
    "email": "nguyenvanb@example.com",
    "phone": "0987654321",
    "address": "Ho Chi Minh"
  }'
```

### Xóa user
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

## H2 Database Console

Truy cập H2 Console tại: `http://localhost:8080/h2-console`

**Thông tin kết nối:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (để trống)

## Cấu trúc project

```
src/
├── main/
│   ├── java/com/example/api/
│   │   ├── Application.java              # Main class
│   │   ├── controller/
│   │   │   └── UserController.java       # REST Controller
│   │   ├── model/
│   │   │   └── User.java                 # Entity
│   │   ├── repository/
│   │   │   └── UserRepository.java       # JPA Repository
│   │   ├── service/
│   │   │   └── UserService.java          # Business Logic
│   │   ├── dto/
│   │   │   └── ApiResponse.java          # Response wrapper
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java # Exception handling
│   └── resources/
│       └── application.properties         # Configuration
└── pom.xml                                # Maven dependencies
```

## Tính năng

✅ CRUD operations đầy đủ (Create, Read, Update, Delete)  
✅ Validation dữ liệu đầu vào  
✅ Exception handling toàn cục  
✅ H2 in-memory database  
✅ JPA/Hibernate ORM  
✅ RESTful API design  
✅ CORS enabled  

## Mở rộng

Bạn có thể mở rộng project này bằng cách:
- Thêm authentication/authorization (Spring Security)
- Thêm pagination và sorting
- Thêm các entity khác (Product, Order, etc.)
- Chuyển sang database thực (MySQL, PostgreSQL)
- Thêm API documentation (Swagger/OpenAPI)
- Thêm unit tests và integration tests
