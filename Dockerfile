FROM maven:3.6.3-openjdk-14
RUN mkdir job4j_wget
WORKDIR job4j_wget
COPY . .
RUN mvn package -Dmaven.skip.test=true
EXPOSE 9000/tcp
CMD ["java", "-jar", "target/Wget.jar"]
