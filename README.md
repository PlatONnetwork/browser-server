# 项目变更说明

### 一、springboot升级到2.1.3，原因：
##### 1、springboot1.5使用的jedis客户端不支持redis集群的pipeline批量查询键值操作（此为改进程序性能所需要的特性）；
##### 2、简化mybatis、generator配置；
### 二、合并模块并统一名称
##### 1、manager更名为browser-service,主要提供业务层逻辑和一些实用工具；
##### 2、dao、pojo、common合并到browser-common，主要提供dao操作类和dto类；
##### 3、agent更名为browser-agent，主要负责采集链上数据入库；
##### 4、server更名为browser-api，主要负责为前端提供访问api接口；
### 三、把项目构建工具更换为Gradle，简化依赖引入配置
### 四、各模块间依赖关系：
```
     _____________    ___________
    |browser-agent|  |browser-api|
     -------------    -----------
                 \    /
              _______________
             |browser-service|
              ---------------
                    |
              ______________
             |browser-common|
              --------------
```

### 五、接口文档生成说明
###### 1、安装NodeJs
```
https://nodejs.org/dist/v10.15.3/node-v10.15.3-x64.msi
```
###### 2、安装apidoc
```
npm install apidoc -g
```
###### 3、生成文档
```
apidoc -i browser-api/ -o apidoc/
```

### 六、IDEA Docker开发环境设置
###### 0、docker-machine安装
```
http://192.168.9.85:10080/tools/docker-machine.exe
docker-machine create -d virtualbox --virtualbox-boot2docker-url http://192.168.9.85:10080/tools/rancheros.iso --virtualbox-memory 2048 m1
```
###### 1、Docker服务端配置
![avatar](doc/docker-daemon-config.png)
###### 2、browser-agent部署配置
![avatar](doc/browser-agent-docker-run-config.png)
###### 3、browser-api部署配置
![avatar](doc/browser-api-docker-run-config.png)
