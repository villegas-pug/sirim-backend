FROM openjdk:11
VOLUME [ "/tmp" ]
ADD ./target/interpol-0.0.1-SNAPSHOT.jar microservicio-interpol.jar
ENTRYPOINT [ "java", "-jar", "microservicio-interpol.jar"]