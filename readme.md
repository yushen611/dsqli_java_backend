## 镜像的构建
```bash
docker build -t sqli-detector-java:1.0 .
```
## 镜像的运行
```bash
docker run --rm -d -p 8000:8000 --name sqli-detector-java sqli-detector-java:1.0 
```
>  "--rm" 选项表示容器停止后，自动将其删除。 -d 后台运行