# ==========================================
# Étape 1 : Build du JAR (Maven + JDK 25)
# ==========================================
FROM eclipse-temurin:25-jdk-alpine AS builder
WORKDIR /app

# 1. Copie des fichiers de configuration Maven pour mettre en cache les dépendances
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Rendre le wrapper exécutable et pré-télécharger les dépendances
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# 2. Copie du code source et compilation
COPY src/ src/

# 3. Lancement des tests
RUN ./mvnw test

# 4. Compilation du projet et création du JAR
RUN ./mvnw clean package -DskipTests

# ==========================================
# Étape 2 : Image d'exécution de production (JRE 25)
# ==========================================
FROM eclipse-temurin:25-jre-alpine AS runner
WORKDIR /app

# Création d'un utilisateur non-root pour des raisons de sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copie du JAR compilé depuis l'étape précédente
COPY --from=builder /app/target/*.jar app.jar

# Configuration des variables d'environnement par défaut
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Lancement du .jar
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
