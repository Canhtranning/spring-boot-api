#!/bin/bash

# Spring Boot User API - cURL Commands
# Đảm bảo server đang chạy tại http://localhost:8080

echo "=========================================="
echo "Spring Boot User API - Test Commands"
echo "=========================================="
echo ""

# 1. Get All Users
echo "1. GET All Users"
echo "Command:"
echo 'curl http://localhost:8080/api/users'
echo ""
echo "Response:"
curl http://localhost:8080/api/users
echo ""
echo ""

# 2. Create User - Nguyen Van A
echo "2. POST Create User - Nguyen Van A"
echo "Command:"
echo 'curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '"'"'{"name":"Nguyen Van A","email":"nguyenvana@example.com","phone":"0123456789","address":"Ha Noi"}'"'"
echo ""
echo "Response:"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Nguyen Van A","email":"nguyenvana@example.com","phone":"0123456789","address":"Ha Noi"}'
echo ""
echo ""

# 3. Create User - Tran Thi B
echo "3. POST Create User - Tran Thi B"
echo "Command:"
echo 'curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '"'"'{"name":"Tran Thi B","email":"tranthib@example.com","phone":"0987654321","address":"Ho Chi Minh"}'"'"
echo ""
echo "Response:"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Tran Thi B","email":"tranthib@example.com","phone":"0987654321","address":"Ho Chi Minh"}'
echo ""
echo ""

# 4. Create User - Le Van C
echo "4. POST Create User - Le Van C"
echo "Command:"
echo 'curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '"'"'{"name":"Le Van C","email":"levanc@example.com","phone":"0111222333","address":"Can Tho"}'"'"
echo ""
echo "Response:"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Le Van C","email":"levanc@example.com","phone":"0111222333","address":"Can Tho"}'
echo ""
echo ""

# 5. Get All Users Again
echo "5. GET All Users (After Creating)"
echo "Command:"
echo 'curl http://localhost:8080/api/users'
echo ""
echo "Response:"
curl http://localhost:8080/api/users
echo ""
echo ""

# 6. Get User By ID
echo "6. GET User By ID (ID=1)"
echo "Command:"
echo 'curl http://localhost:8080/api/users/1'
echo ""
echo "Response:"
curl http://localhost:8080/api/users/1
echo ""
echo ""

# 7. Update User
echo "7. PUT Update User (ID=1)"
echo "Command:"
echo 'curl -X PUT http://localhost:8080/api/users/1 -H "Content-Type: application/json" -d '"'"'{"name":"Nguyen Van A Updated","email":"nguyenvana.updated@example.com","phone":"0999999999","address":"Da Nang"}'"'"
echo ""
echo "Response:"
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Nguyen Van A Updated","email":"nguyenvana.updated@example.com","phone":"0999999999","address":"Da Nang"}'
echo ""
echo ""

# 8. Search User By Email
echo "8. GET Search User By Email"
echo "Command:"
echo 'curl "http://localhost:8080/api/users/search?email=tranthib@example.com"'
echo ""
echo "Response:"
curl "http://localhost:8080/api/users/search?email=tranthib@example.com"
echo ""
echo ""

# 9. Delete User
echo "9. DELETE User (ID=1)"
echo "Command:"
echo 'curl -X DELETE http://localhost:8080/api/users/1'
echo ""
echo "Response:"
curl -X DELETE http://localhost:8080/api/users/1
echo ""
echo ""

# 10. Get All Users After Delete
echo "10. GET All Users (After Deleting ID=1)"
echo "Command:"
echo 'curl http://localhost:8080/api/users'
echo ""
echo "Response:"
curl http://localhost:8080/api/users
echo ""
echo ""

echo "=========================================="
echo "Test Cases - Validation & Error Handling"
echo "=========================================="
echo ""

# Test 1: Invalid Email
echo "Test 1: Invalid Email (Expected: 400 Bad Request)"
echo "Command:"
echo 'curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '"'"'{"name":"Test User","email":"invalid-email","phone":"0123456789","address":"Ha Noi"}'"'"
echo ""
echo "Response:"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"invalid-email","phone":"0123456789","address":"Ha Noi"}'
echo ""
echo ""

# Test 2: Missing Name
echo "Test 2: Missing Name (Expected: 400 Bad Request)"
echo "Command:"
echo 'curl -X POST http://localhost:8080/api/users -H "Content-Type: application/json" -d '"'"'{"name":"","email":"test@example.com","phone":"0123456789","address":"Ha Noi"}'"'"
echo ""
echo "Response:"
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"","email":"test@example.com","phone":"0123456789","address":"Ha Noi"}'
echo ""
echo ""

# Test 3: User Not Found
echo "Test 3: User Not Found (Expected: 404 Not Found)"
echo "Command:"
echo 'curl http://localhost:8080/api/users/999'
echo ""
echo "Response:"
curl http://localhost:8080/api/users/999
echo ""
echo ""

# Test 4: Delete Non-Existing User
echo "Test 4: Delete Non-Existing User (Expected: 404 Not Found)"
echo "Command:"
echo 'curl -X DELETE http://localhost:8080/api/users/999'
echo ""
echo "Response:"
curl -X DELETE http://localhost:8080/api/users/999
echo ""
echo ""

echo "=========================================="
echo "All Tests Completed!"
echo "=========================================="
