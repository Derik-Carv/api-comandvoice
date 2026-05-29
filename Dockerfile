# Stage 1: Build da aplicação com Maven e Java 21
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app

# Copia as configurações do Maven primeiro para cachear as dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o código-fonte
COPY src ./src

# Compila o projeto gerando o jar executável
RUN mvn clean package -DskipTests

# Stage 2: Imagem leve para rodar o Jar em produção
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o .jar gerado no primeiro estágio
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]