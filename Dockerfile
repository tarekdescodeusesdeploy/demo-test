FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
CMD ["java", "-jar", "target/mon-app.jar", "--spring.profiles.active=docker"]