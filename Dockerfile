# Stage 1: Build
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml và download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code và build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy JAR file từ build stage
COPY --from=build /app/target/spring-boot-api-1.0.0.jar app.jar

# Expose port (Railway sẽ override bằng $PORT)
EXPOSE 8080

# Run application - dùng $PORT từ Railway
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Xmx300m -jar app.jar"]
