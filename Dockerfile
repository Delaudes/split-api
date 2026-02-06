# Étape 1 : Build de l'application avec Maven et Corretto 25
FROM maven:3.9-amazoncorretto-25 AS build
WORKDIR /app

# Copier le pom.xml et télécharger les dépendances
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier le code source et builder
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : Image légère pour exécuter l'application avec Corretto 25
FROM amazoncorretto:25-alpine
WORKDIR /app

# Copier le JAR depuis l'étape de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]