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
          \          /
           \        /
            \      /
             \    /
          _______________
         |browser-service|
          ---------------
                |
          ______________
         |browser-common|
          --------------
```