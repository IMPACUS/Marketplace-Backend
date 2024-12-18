FROM openjdk:17-ea-33-jdk-buster
EXPOSE 8080
ARG JAR_FILE=build/libs/*-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
ENTRYPOINT ["java", "-jar", "-Duser.timezone=Asia/Seoul", "app.jar"]