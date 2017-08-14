FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/smoker-*.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS \
-Djava.security.egd=file:/dev/./urandom \
-Dspring.data.mongodb.host=$mongohost \
-Dspring.data.mongodb.port=$mongoport \
-Dspring.data.mongodb.database=$database \
-Dspring.data.mongodb.username=$mongousername \
-Dspring.data.mongodb.password=$mongopasswd \
-jar /app.jar" ]