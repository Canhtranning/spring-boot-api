# 🚀 Hướng dẫn Deploy Spring Boot lên AWS

## 📋 Mục lục
1. [AWS EC2 (Máy chủ ảo)](#1-aws-ec2-máy-chủ-ảo) - **Khuyên dùng cho người mới**
2. [AWS Elastic Beanstalk](#2-aws-elastic-beanstalk) - **Dễ nhất, tự động hóa**
3. [AWS ECS (Docker Container)](#3-aws-ecs-docker-container)
4. [AWS Lambda (Serverless)](#4-aws-lambda-serverless)

---

## 1. AWS EC2 (Máy chủ ảo)

### ✅ Ưu điểm:
- Kiểm soát hoàn toàn server
- Dễ hiểu, phù hợp người mới
- Chi phí rõ ràng

### 📝 Các bước thực hiện:

#### **Bước 1: Tạo EC2 Instance**

1. Đăng nhập [AWS Console](https://console.aws.amazon.com/)
2. Vào **EC2** → Click **"Launch Instance"**
3. Cấu hình:
   - **Name**: `spring-boot-api-server`
   - **AMI**: Ubuntu Server 22.04 LTS (Free tier eligible)
   - **Instance type**: t2.micro (Free tier - 1GB RAM)
   - **Key pair**: Tạo mới hoặc chọn existing (để SSH)
   - **Security Group**: 
     - SSH (port 22) - Your IP
     - HTTP (port 80) - Anywhere
     - Custom TCP (port 8080) - Anywhere
4. Click **"Launch Instance"**

#### **Bước 2: Kết nối SSH vào EC2**

```bash
# Download file .pem key từ AWS
chmod 400 your-key.pem

# SSH vào EC2
ssh -i "your-key.pem" ubuntu@your-ec2-public-ip
```

#### **Bước 3: Cài đặt Java và Maven trên EC2**

```bash
# Update system
sudo apt update && sudo apt upgrade -y

# Cài đặt Java 17
sudo apt install openjdk-17-jdk -y

# Kiểm tra Java version
java -version

# Cài đặt Maven
sudo apt install maven -y

# Kiểm tra Maven version
mvn -version

# Cài đặt Git
sudo apt install git -y
```

#### **Bước 4: Upload code lên EC2**

**Cách 1: Dùng Git (Khuyên dùng)**
```bash
# Clone repository
git clone https://github.com/your-username/your-repo.git
cd your-repo
```

**Cách 2: Dùng SCP (Copy từ máy local)**
```bash
# Từ máy local, copy toàn bộ project
scp -i "your-key.pem" -r /path/to/Code_SpringBoot ubuntu@your-ec2-ip:~/
```

#### **Bước 5: Build và chạy application**

```bash
# Di chuyển vào thư mục project
cd ~/Code_SpringBoot

# Build project
mvn clean package -DskipTests

# Chạy application
java -jar target/spring-boot-api-1.0.0.jar

# Hoặc chạy với Maven
mvn spring-boot:run
```

#### **Bước 6: Chạy application như service (Background)**

Tạo file systemd service:

```bash
sudo nano /etc/systemd/system/springboot-api.service
```

Nội dung file:
```ini
[Unit]
Description=Spring Boot API
After=syslog.target

[Service]
User=ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/Code_SpringBoot/target/spring-boot-api-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Khởi động service:
```bash
# Reload systemd
sudo systemctl daemon-reload

# Enable service (tự động chạy khi khởi động)
sudo systemctl enable springboot-api

# Start service
sudo systemctl start springboot-api

# Kiểm tra status
sudo systemctl status springboot-api

# Xem logs
sudo journalctl -u springboot-api -f
```

#### **Bước 7: Cấu hình Nginx (Optional - Reverse Proxy)**

```bash
# Cài đặt Nginx
sudo apt install nginx -y

# Tạo config file
sudo nano /etc/nginx/sites-available/springboot-api
```

Nội dung:
```nginx
server {
    listen 80;
    server_name your-domain.com;  # Hoặc EC2 public IP

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Enable và restart Nginx:
```bash
# Enable site
sudo ln -s /etc/nginx/sites-available/springboot-api /etc/nginx/sites-enabled/

# Test config
sudo nginx -t

# Restart Nginx
sudo systemctl restart nginx
```

#### **Bước 8: Test API**

```bash
# Từ EC2
curl http://localhost:8080/api/users

# Từ máy local
curl http://your-ec2-public-ip:8080/api/users

# Nếu dùng Nginx
curl http://your-ec2-public-ip/api/users
```

---

## 2. AWS Elastic Beanstalk

### ✅ Ưu điểm:
- Tự động hóa deployment
- Tự động scaling
- Dễ dùng nhất

### 📝 Các bước thực hiện:

#### **Bước 1: Chuẩn bị JAR file**

```bash
# Build project
mvn clean package -DskipTests

# File JAR sẽ ở: target/spring-boot-api-1.0.0.jar
```

#### **Bước 2: Tạo Elastic Beanstalk Application**

1. Vào [AWS Console](https://console.aws.amazon.com/) → **Elastic Beanstalk**
2. Click **"Create Application"**
3. Cấu hình:
   - **Application name**: `spring-boot-user-api`
   - **Platform**: Java
   - **Platform branch**: Corretto 17
   - **Application code**: Upload your code
   - Upload file `target/spring-boot-api-1.0.0.jar`
4. Click **"Create application"**

#### **Bước 3: Cấu hình Environment**

1. Sau khi tạo xong, vào **Configuration**
2. **Software** → Edit:
   - **Environment properties**:
     - `SERVER_PORT` = `5000` (Beanstalk yêu cầu port 5000)
3. **Instances** → Edit:
   - Instance type: t2.micro (Free tier)
4. **Capacity** → Edit:
   - Environment type: Single instance (cho dev/test)

#### **Bước 4: Update application.properties**

Thêm vào `src/main/resources/application.properties`:
```properties
# Beanstalk yêu cầu port 5000
server.port=${SERVER_PORT:8080}
```

Rebuild và upload lại:
```bash
mvn clean package -DskipTests
```

#### **Bước 5: Deploy**

1. Vào Elastic Beanstalk console
2. Click **"Upload and deploy"**
3. Chọn file JAR mới
4. Click **"Deploy"**

#### **Bước 6: Test**

```bash
# URL sẽ có dạng:
# http://spring-boot-user-api.us-east-1.elasticbeanstalk.com

curl http://your-beanstalk-url.elasticbeanstalk.com/api/users
```

---

## 3. AWS ECS (Docker Container)

### ✅ Ưu điểm:
- Dễ scale
- Consistent environment
- Modern approach

### 📝 Các bước thực hiện:

#### **Bước 1: Tạo Dockerfile**

File đã được tạo sẵn: `Dockerfile`

#### **Bước 2: Build Docker Image**

```bash
# Build image
docker build -t spring-boot-api .

# Test local
docker run -p 8080:8080 spring-boot-api

# Test
curl http://localhost:8080/api/users
```

#### **Bước 3: Push lên Amazon ECR**

```bash
# Cài đặt AWS CLI
# macOS
brew install awscli

# Configure AWS CLI
aws configure

# Tạo ECR repository
aws ecr create-repository --repository-name spring-boot-api --region us-east-1

# Login vào ECR
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com

# Tag image
docker tag spring-boot-api:latest YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/spring-boot-api:latest

# Push image
docker push YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/spring-boot-api:latest
```

#### **Bước 4: Tạo ECS Cluster**

1. Vào **ECS** → **Clusters** → **Create Cluster**
2. Chọn **Fargate** (serverless)
3. Cluster name: `spring-boot-cluster`

#### **Bước 5: Tạo Task Definition**

1. **Task Definitions** → **Create new Task Definition**
2. Cấu hình:
   - Launch type: Fargate
   - Task memory: 1GB
   - Task CPU: 0.5 vCPU
   - Container:
     - Image: `YOUR_ACCOUNT_ID.dkr.ecr.us-east-1.amazonaws.com/spring-boot-api:latest`
     - Port mappings: 8080

#### **Bước 6: Tạo Service**

1. Vào Cluster → **Create Service**
2. Cấu hình:
   - Launch type: Fargate
   - Task Definition: Chọn task vừa tạo
   - Service name: `spring-boot-service`
   - Number of tasks: 1
   - Load balancer: Application Load Balancer (optional)

---

## 4. AWS Lambda (Serverless)

### ✅ Ưu điểm:
- Không cần quản lý server
- Chỉ trả tiền khi có request
- Auto scaling

### 📝 Yêu cầu:
- Cần thêm AWS Lambda adapter
- Phù hợp cho API nhẹ, không cần database persistent

---

## 💰 So sánh Chi phí (Free Tier)

| Service | Free Tier | Sau Free Tier |
|---------|-----------|---------------|
| **EC2 t2.micro** | 750 giờ/tháng (12 tháng) | ~$8-10/tháng |
| **Elastic Beanstalk** | Free (chỉ trả EC2) | ~$8-10/tháng |
| **ECS Fargate** | Không có Free Tier | ~$15-20/tháng |
| **Lambda** | 1M requests/tháng | $0.20/1M requests |

---

## 🎯 Khuyến nghị

### **Cho người mới bắt đầu:**
→ **AWS EC2** hoặc **Elastic Beanstalk**

### **Cho production:**
→ **ECS với Load Balancer** hoặc **Elastic Beanstalk**

### **Cho API nhẹ, ít traffic:**
→ **AWS Lambda**

---

## 📊 Checklist Deploy

- [ ] Build JAR file thành công
- [ ] Test local trước khi deploy
- [ ] Cấu hình Security Group đúng ports
- [ ] Cấu hình database (nếu dùng RDS)
- [ ] Setup monitoring (CloudWatch)
- [ ] Setup backup
- [ ] Cấu hình domain name (Route 53)
- [ ] Setup SSL certificate (ACM)
- [ ] Setup CI/CD (CodePipeline)

---

## 🔧 Troubleshooting

### **Application không start:**
```bash
# Xem logs
sudo journalctl -u springboot-api -n 100

# Kiểm tra port
sudo netstat -tulpn | grep 8080

# Kiểm tra Java process
ps aux | grep java
```

### **Port bị chiếm:**
```bash
# Kill process trên port 8080
sudo kill -9 $(sudo lsof -t -i:8080)
```

### **Out of memory:**
```bash
# Chạy với memory limit
java -Xmx512m -jar target/spring-boot-api-1.0.0.jar
```

---

Bạn muốn deploy theo cách nào? Tôi sẽ hướng dẫn chi tiết hơn! 🚀
