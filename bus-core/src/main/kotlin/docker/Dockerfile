FROM openjdk:8u252-jre-slim
LABEL maintainer="宋志宗"
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone
EXPOSE 8080
ENV JVM_OPTS="-Xms64m -Xmx1024m"
ENTRYPOINT ["sh","-c","java -server -jar $JVM_OPTS -Dfile.encoding=UTF-8 /app.jar --server.port=8080 --spring.profiles.active=docker"]
