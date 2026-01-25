# 1. Java 17 muhitini tanlaymiz (Oracle GraalVM yoki OpenJDK)
FROM container-registry.oracle.com/graalvm/jdk:17-ol9

# 2. Ishchi papkani yaratamiz
WORKDIR /app

# 3. Target papkasidagi tayyor .jar faylni konteyner ichiga 'app.jar' nomi bilan ko'chiramiz
# Rasmda ko'ringan aniq nom: track-menegment-0.0.1-SNAPSHOT.jar
COPY target/track-menegment-0.0.1-SNAPSHOT.jar app.jar

# 4. Railway yoki Render beradigan portda ishlashi uchun o'zgaruvchi
EXPOSE 8081

# 5. Loyihani ishga tushirish (Spring Boot xususiyatlarini optimallashtirgan holda)
ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]-b"]