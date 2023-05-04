## 镜像的构建
```bash
docker build -t sqli-detector-java:1.0 .
```
## 镜像的运行
```bash
docker run -p 8000:8000 --name sqli-detector-java sqli-detector-java:1.0 
```