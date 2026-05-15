#!/bin/bash

# Script cài đặt môi trường trên AWS EC2
# Chạy script này trên EC2 instance sau khi SSH vào

set -e

echo "=========================================="
echo "AWS EC2 - Setup Environment"
echo "=========================================="
echo ""

echo "1. Updating system..."
sudo apt update && sudo apt upgrade -y

echo ""
echo "2. Installing Java 17..."
sudo apt install openjdk-17-jdk -y
java -version

echo ""
echo "3. Installing Maven..."
sudo apt install maven -y
mvn -version

echo ""
echo "4. Installing Git..."
sudo apt install git -y
git --version

echo ""
echo "5. Installing Nginx..."
sudo apt install nginx -y
sudo systemctl enable nginx
sudo systemctl start nginx

echo ""
echo "6. Installing Docker (Optional)..."
sudo apt install docker.io -y
sudo systemctl enable docker
sudo systemctl start docker
sudo usermod -aG docker $USER

echo ""
echo "7. Installing Docker Compose (Optional)..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
docker-compose --version

echo ""
echo "8. Configuring firewall..."
sudo ufw allow 22/tcp
sudo ufw allow 80/tcp
sudo ufw allow 8080/tcp
sudo ufw --force enable

echo ""
echo "=========================================="
echo "Setup completed!"
echo "=========================================="
echo ""
echo "Installed versions:"
java -version
mvn -version
git --version
nginx -v
docker --version
docker-compose --version
echo ""
echo "Next steps:"
echo "1. Clone your repository: git clone https://github.com/your-username/your-repo.git"
echo "2. Build application: cd your-repo && mvn clean package"
echo "3. Run application: java -jar target/spring-boot-api-1.0.0.jar"
echo ""
