#!/bin/bash

# Script tự động deploy lên AWS EC2
# Sử dụng: ./deploy-ec2.sh

set -e

echo "=========================================="
echo "Spring Boot API - Deploy to AWS EC2"
echo "=========================================="
echo ""

# Cấu hình (Thay đổi theo thông tin của bạn)
EC2_HOST="your-ec2-public-ip"
EC2_USER="ubuntu"
KEY_FILE="your-key.pem"
APP_DIR="/home/ubuntu/spring-boot-api"
JAR_NAME="spring-boot-api-1.0.0.jar"

echo "1. Building application..."
mvn clean package -DskipTests

echo ""
echo "2. Stopping application on EC2..."
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "sudo systemctl stop springboot-api || true"

echo ""
echo "3. Creating application directory on EC2..."
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "mkdir -p $APP_DIR"

echo ""
echo "4. Uploading JAR file to EC2..."
scp -i "$KEY_FILE" "target/$JAR_NAME" "$EC2_USER@$EC2_HOST:$APP_DIR/"

echo ""
echo "5. Creating systemd service file..."
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" << 'EOF'
sudo tee /etc/systemd/system/springboot-api.service > /dev/null << 'SERVICE'
[Unit]
Description=Spring Boot API
After=syslog.target

[Service]
User=ubuntu
ExecStart=/usr/bin/java -jar /home/ubuntu/spring-boot-api/spring-boot-api-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
SERVICE
EOF

echo ""
echo "6. Starting application..."
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" << 'EOF'
sudo systemctl daemon-reload
sudo systemctl enable springboot-api
sudo systemctl start springboot-api
EOF

echo ""
echo "7. Checking application status..."
sleep 5
ssh -i "$KEY_FILE" "$EC2_USER@$EC2_HOST" "sudo systemctl status springboot-api"

echo ""
echo "=========================================="
echo "Deployment completed!"
echo "=========================================="
echo ""
echo "Application URL: http://$EC2_HOST:8080/api/users"
echo ""
echo "Useful commands:"
echo "  - Check status: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'sudo systemctl status springboot-api'"
echo "  - View logs: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'sudo journalctl -u springboot-api -f'"
echo "  - Restart: ssh -i $KEY_FILE $EC2_USER@$EC2_HOST 'sudo systemctl restart springboot-api'"
echo ""
