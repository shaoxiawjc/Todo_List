# 使用一个官方的OpenJDK镜像作为基础镜像
FROM openjdk:8-jre-slim

# 将应用的jar文件复制到容器中
COPY target/TodoServiceProvider-1.0-SNAPSHOT.jar /todoapp.jar

# 运行jar文件
ENTRYPOINT ["java", "-jar", "/todoapp.jar","--spring.profiles.active=dev"]
