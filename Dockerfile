# 使用Maven构建
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/cloudnote-1.0.0.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "app.jar"]
