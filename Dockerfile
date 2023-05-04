# 指定使用的基础镜像
FROM adoptopenjdk/maven-openjdk8

# 设置工作目录
WORKDIR /app

# 拷贝Maven相关文件
COPY pom.xml ./
COPY src ./src

# 执行Maven打包
RUN mvn package

#RUN cp ./target/*.jar ./
# 声明运行时容器需要暴露的端口
EXPOSE 8000

# 容器启动时运行的命令
CMD ["java", "-jar", "./target/javaBackend-1.0-SNAPSHOT-jar-with-dependencies.jar"]
#CMD ["ls","-R"]


