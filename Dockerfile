# Build: compila o jar com Maven
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia o pom primeiro pra aproveitar o cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Depois o código e empacota
COPY src ./src
RUN mvn clean package -DskipTests -B

# Runtime: só a JRE, imagem enxuta
FROM eclipse-temurin:21-jre-alpine

# curl pro healthcheck do compose
RUN apk add --no-cache curl

# Roda como usuário sem root (boa prática, o container não precisa de root)
RUN addgroup -S carpinaon && adduser -S carpinaon -G carpinaon
USER carpinaon

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# JAVA_TOOL_OPTIONS (heap, GC) é lida pela JVM automaticamente
ENTRYPOINT ["java", "-jar", "app.jar"]
