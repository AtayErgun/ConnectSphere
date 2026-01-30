# 1. Aşama: Uygulamayı derle
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Aşama: Uygulamayı çalıştır
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/ConnectSphere-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]