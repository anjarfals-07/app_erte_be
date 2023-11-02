FROM openjdk:17-oracle

# Salin JAR Spring Boot aplikasi Anda ke dalam image
COPY target/erte-0.0.1-SNAPSHOT.jar /erte-app.jar

# Expose port yang digunakan oleh aplikasi Spring Boot
EXPOSE 8089

# Perintah untuk menjalankan aplikasi Spring Boot
CMD ["java", "-jar", "/erte-app.jar"]