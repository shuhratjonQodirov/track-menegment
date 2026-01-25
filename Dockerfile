# 1-bosqich: Build qilish (Maven yordamida)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Loyiha fayllarini ko'chiramiz
COPY . .
# .jar faylni yasaymiz (testlarni o'tkazib yuboramiz)
RUN mvn clean package -DskipTests

# 2-bosqich: Ishga tushirish (GraalVM yordamida)
FROM container-registry.oracle.com/graalvm/jdk:17-ol9
WORKDIR /app
# Build bosqichidan tayyor .jar faylni ko'chirib olamiz
COPY --from=build /app/target/track-menegment-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]