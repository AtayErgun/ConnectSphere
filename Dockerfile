# 1. Aşama: Derleme
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Aşama: Çalıştırma
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# target altındaki tek jar dosyasını app.jar adıyla kopyala
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]