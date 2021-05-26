## 部署步骤
### 1、到 http://192.168.18.31:8080/job/PlatScan/job/Plat-job/build?delay=0sec 使用指定的commitid 构建 scan-job
### 2、下载构建好的程序包，并上传至目标部署机器，解压
### 3、在浏览器数据库执行update.sql，创建地址表
### 4、修改 application-prod.yml配置文件
#### 4.1、修改mysql数据库链接、用户名、密码
#### 4.2、修改链ID、web3j链接
#### 4.3、按需要修改cron表达式
### 5、启动程序：
```
nohup java -jar scan-job*.jar --spring.profiles.active=prod &
```
