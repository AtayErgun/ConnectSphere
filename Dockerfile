# 1. Aşama: Uygulamayı derle (Maven)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# 2. Aşama: Uygulamayı çalıştır (Güncel ve stabil JRE)
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
# 🚨 DİKKAT: target altındaki JAR isminin tam olarak 'ConnectSphere-0.0.1-SNAPSHOT.jar'
# olduğundan emin ol. pom.xml'deki artifactId ve version'a göre değişebilir.
COPY --from=build /app/target/ConnectSphere-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]