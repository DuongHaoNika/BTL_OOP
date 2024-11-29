# Sử dụng hình ảnh Maven chính thức để build ứng dụng
FROM maven:3.9.9-ibm-semeru-23-jammy AS build
WORKDIR /app

# Copy toàn bộ mã nguồn vào container
COPY . .

# Build ứng dụng bằng Maven
RUN mvn clean package -DskipTests

# Sử dụng hình ảnh JDK chính thức để chạy ứng dụng
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copy file JAR từ build stage vào container
COPY --from=build /app/target/*.jar app.jar

# Expose cổng ứng dụng (thay đổi nếu cổng khác)
EXPOSE 8080

# Command để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
