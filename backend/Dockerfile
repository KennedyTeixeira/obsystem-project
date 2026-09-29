# ----------------------------------------------------------------------------
# 1. Build Stage
# ----------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia pom.xml e faz cache das dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia codigo fonte e compila pacote final
COPY src ./src
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------------
# 2. Runtime Stage
# ----------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia o jar compilado
COPY --from=build /app/target/*.jar app.jar

# Variaveis de ambiente padrao
ENV PORT=8080
EXPOSE 8080

# Inicia a aplicacao
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
