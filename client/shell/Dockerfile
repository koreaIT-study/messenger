FROM openjdk:8-jdk-alpine
ARG JAR_FILE=messenger_client*.jar
COPY ${JAR_FILE} messenger_client.jar
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "messenger_client.jar"]