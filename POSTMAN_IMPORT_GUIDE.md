# 📮 Hướng dẫn Import Postman Collection

## 🚀 Cách 1: Import File JSON

### Bước 1: Mở Postman
- Khởi động ứng dụng Postman

### Bước 2: Import Collection
1. Click vào nút **"Import"** ở góc trên bên trái
2. Chọn tab **"File"**
3. Click **"Upload Files"** hoặc kéo thả file `Spring_Boot_API.postman_collection.json`
4. Click **"Import"**

### Bước 3: Sử dụng
- Collection **"Spring Boot User API"** sẽ xuất hiện trong sidebar bên trái
- Mở rộng collection để xem tất cả các requests

---

## 📋 Danh sách API trong Collection

### **Folder: Users** (8 requests)

1. ✅ **Get All Users** - `GET /api/users`
2. ✅ **Get User By ID** - `GET /api/users/{id}`
3. ✅ **Create User - Nguyen Van A** - `POST /api/users`
4. ✅ **Create User - Tran Thi B** - `POST /api/users`
5. ✅ **Create User - Le Van C** - `POST /api/users`
6. ✅ **Update User** - `PUT /api/users/{id}`
7. ✅ **Search User By Email** - `GET /api/users/search?email=...`
8. ✅ **Delete User** - `DELETE /api/users/{id}`

### **Folder: Test Cases** (5 requests)

1. ❌ **Test - Invalid Email** - Kiểm tra validation email
2. ❌ **Test - Missing Name** - Kiểm tra required field
3. ❌ **Test - Duplicate Email** - Kiểm tra unique constraint
4. ❌ **Test - User Not Found** - Kiểm tra 404 error
5. ❌ **Test - Delete Non-Existing User** - Kiểm tra delete error

---

## 🎯 Hướng dẫn Test theo thứ tự

### **Bước 1: Khởi động Server**
```bash
mvn spring-boot:run
```
Hoặc chạy từ IntelliJ IDEA

### **Bước 2: Test CRUD Operations**

#### 1️⃣ Tạo Users
Chạy lần lượt:
- **Create User - Nguyen Van A** → Nhận ID = 1
- **Create User - Tran Thi B** → Nhận ID = 2
- **Create User - Le Van C** → Nhận ID = 3

#### 2️⃣ Lấy danh sách
- **Get All Users** → Xem tất cả 3 users

#### 3️⃣ Lấy theo ID
- **Get User By ID** → Thay `1` trong URL để xem user cụ thể

#### 4️⃣ Tìm kiếm
- **Search User By Email** → Tìm user theo email

#### 5️⃣ Cập nhật
- **Update User** → Cập nhật thông tin user ID = 1

#### 6️⃣ Xóa
- **Delete User** → Xóa user ID = 1

### **Bước 3: Test Validation & Error Handling**

Chạy các requests trong folder **"Test Cases"**:

1. **Test - Invalid Email** 
   - Expected: `400 Bad Request`
   - Response: `{"email": "Email should be valid"}`

2. **Test - Missing Name**
   - Expected: `400 Bad Request`
   - Response: `{"name": "Name is required"}`

3. **Test - Duplicate Email**
   - Expected: `400 Bad Request`
   - Response: `{"error": "Email already exists"}`

4. **Test - User Not Found**
   - Expected: `404 Not Found`

5. **Test - Delete Non-Existing User**
   - Expected: `404 Not Found`

---

## 🔧 Cách 2: Import từ cURL (Thủ công)

Nếu bạn muốn tạo từng request thủ công:

### 1. Get All Users
```bash
curl http://localhost:8080/api/users
```

### 2. Create User
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

### 3. Get User By ID
```bash
curl http://localhost:8080/api/users/1
```

### 4. Update User
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

### 5. Delete User
```bash
curl -X DELETE http://localhost:8080/api/users/1
```

### 6. Search By Email
```bash
curl "http://localhost:8080/api/users/search?email=tranthib@example.com"
```

---

## 💡 Tips cho Postman

### 1. Sử dụng Variables
Tạo Environment với biến:
- `base_url` = `http://localhost:8080`
- `user_id` = `1`

Sau đó dùng: `{{base_url}}/api/users/{{user_id}}`

### 2. Sử dụng Tests
Thêm vào tab **Tests** của mỗi request:

```javascript
// Test status code
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

// Test response time
pm.test("Response time is less than 500ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(500);
});

// Test response body
pm.test("Response has id", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('id');
});
```

### 3. Chạy Collection Runner
1. Click vào **"..."** bên cạnh collection name
2. Chọn **"Run collection"**
3. Chọn các requests muốn chạy
4. Click **"Run Spring Boot User API"**

---

## 🐛 Debug Tips

### Xem Request Details
- Tab **Headers**: Xem request headers
- Tab **Body**: Xem request body
- Tab **Pre-request Script**: Chạy code trước khi gửi request

### Xem Response Details
- Tab **Body**: Xem response data
- Tab **Headers**: Xem response headers
- Tab **Test Results**: Xem kết quả tests
- **Status**: Xem HTTP status code và response time

### Console
- Mở **Postman Console** (View → Show Postman Console)
- Xem chi tiết request/response logs

---

## 📊 Expected Results

### Success Cases
- **POST /api/users** → `201 Created` + User object với ID
- **GET /api/users** → `200 OK` + Array of users
- **GET /api/users/{id}** → `200 OK` + User object
- **PUT /api/users/{id}** → `200 OK` + Updated user object
- **DELETE /api/users/{id}** → `204 No Content`

### Error Cases
- **Invalid email** → `400 Bad Request`
- **Missing required field** → `400 Bad Request`
- **Duplicate email** → `400 Bad Request`
- **User not found** → `404 Not Found`

---

## 🎓 Học thêm

### Postman Features
- **Collections**: Nhóm các requests
- **Environments**: Quản lý variables cho dev/staging/prod
- **Tests**: Tự động test responses
- **Mock Servers**: Tạo mock API
- **Documentation**: Tự động generate API docs

### Keyboard Shortcuts
- `Ctrl/Cmd + Enter`: Send request
- `Ctrl/Cmd + S`: Save request
- `Ctrl/Cmd + N`: New request
- `Ctrl/Cmd + K`: Search

---

Chúc bạn test vui vẻ! 🚀
