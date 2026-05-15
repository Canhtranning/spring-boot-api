# 🧪 Hướng dẫn Test API

Server đang chạy tại: **http://localhost:8080**

## ✅ Kết quả Test

### 1. POST - Tạo User Mới
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

### 2. GET - Lấy Tất Cả Users
```bash
curl http://localhost:8080/api/users
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Nguyen Van A Updated",
    "email": "nguyenvana.updated@example.com",
    "phone": "0999999999",
    "address": "Da Nang"
  },
  {
    "id": 2,
    "name": "Tran Thi B",
    "email": "tranthib@example.com",
    "phone": "0987654321",
    "address": "Ho Chi Minh"
  }
]
```

### 3. GET - Lấy User Theo ID
```bash
curl http://localhost:8080/api/users/1
```

**Response:**
```json
{
  "id": 1,
  "name": "Nguyen Van A Updated",
  "email": "nguyenvana.updated@example.com",
  "phone": "0999999999",
  "address": "Da Nang"
}
```

### 4. PUT - Cập Nhật User
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Nguyen Van A Updated",
    "email": "nguyenvana.updated@example.com",
    "phone": "0999999999",
    "address": "Da Nang"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Nguyen Van A Updated",
  "email": "nguyenvana.updated@example.com",
  "phone": "0999999999",
  "address": "Da Nang"
}
```

### 5. GET - Tìm User Theo Email
```bash
curl "http://localhost:8080/api/users/search?email=tranthib@example.com"
```

**Response:**
```json
{
  "id": 2,
  "name": "Tran Thi B",
  "email": "tranthib@example.com",
  "phone": "0987654321",
  "address": "Ho Chi Minh"
}
```

### 6. DELETE - Xóa User
```bash
curl -X DELETE http://localhost:8080/api/users/2
```

**Response:** HTTP 204 No Content

---

## 🎯 Các API Endpoints

| Method | Endpoint | Mô tả |
|--------|----------|-------|
| GET | `/api/users` | Lấy tất cả users |
| GET | `/api/users/{id}` | Lấy user theo ID |
| POST | `/api/users` | Tạo user mới |
| PUT | `/api/users/{id}` | Cập nhật user |
| DELETE | `/api/users/{id}` | Xóa user |
| GET | `/api/users/search?email={email}` | Tìm user theo email |

---

## 🔧 Test với Postman hoặc Insomnia

Bạn có thể import các request sau vào Postman:

1. **Base URL:** `http://localhost:8080`
2. **Headers:** `Content-Type: application/json`
3. Sử dụng các endpoint ở bảng trên

---

## 🗄️ H2 Database Console

Truy cập: **http://localhost:8080/h2-console**

**Thông tin kết nối:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (để trống)

---

## 📊 Kiểm tra dữ liệu trong database

```sql
SELECT * FROM users;
```

---

## ✨ Tính năng đã test thành công

✅ POST - Tạo user mới  
✅ GET - Lấy tất cả users  
✅ GET - Lấy user theo ID  
✅ PUT - Cập nhật user  
✅ GET - Tìm user theo email  
✅ Validation email và required fields  
✅ Unique email constraint  

---

## 🚀 Bạn có thể test thêm

```bash
# Test validation - Email không hợp lệ
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"invalid-email","phone":"123"}'

# Test duplicate email
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"nguyenvana@example.com","phone":"123"}'

# Test user không tồn tại
curl http://localhost:8080/api/users/999
```
