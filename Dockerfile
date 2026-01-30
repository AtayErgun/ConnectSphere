# 1. Aşama: Uygulamayı derle (Maven)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Aşama: Uygulamayı çalıştır (JRE sürümü daha hafif ve güvenlidir)
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# JAR ismini garantiye almak için wildcard (*) kullanıyoruz
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]