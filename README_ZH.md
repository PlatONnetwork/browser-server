# Platon 区块链浏览器服务端
> 提供对Platon链的浏览功能，包括区块、交易、验证节点、治理参数、令牌等功能

## 软件架构

- [架构文档](docs/arch_doc/overall_structure.md)
- [接口文档](https://platonnetwork.github.io/browser-server/)

## 使用技术

- gradle
- junit
- mockito
- spring,springboot
- mybatis
- logback
- client-sdk(web3j)

## 项目结构

- scan-agent：区块同步服务，将特殊节点数据同步到区块链浏览器的数据库中。
- scan-api：浏览器api服务，对web页面提供api接口。
- scan-common：通用模块
- scan-export：用于报表导出的令行客户端
- scan-generator：mybatis骨架生成
- scan-press：用于生成压测数据
- scan-service：通用服务模块
- scan-sync：将区块交易同步到redis的工具
- scan-test：测试类，用于创建合约
- docs：接口文档
- estpl：Elastic Search 模板定义
- scripts：脚本文件
- testdata：单元测试数据
- tools：开发工具
- webconfig：web前端配置项


## 打包
### scan-agent的打包

```bash
gradlew clean buildTar -x test -b scan-agent/build.gradle
```

### scan-api的打包

```bash
gradlew clean buildTar -x test -b scan-api/build.gradle
```

## 组件版本

```bash
MySQL 5.7  
redis 4.0 
elasticsearch 7.4 
apollo 1.9.1
xxljob 2.3.0
```

## 关于apollo启动

```bash
scan-agent,scan-api,scan-job
1-apollo方式:application-apollo.yml
2-非apollo方式:application-platon.yml
以上两种配置方式选择一个即可
```

## scan社区团队流程
```bash
1.  仓库管理员为社区开发团队创建一个基于 develop 分支的功能分支 feature-xxx .
2.  社区开发团队fork feature-xxx 分支到本地仓库,开发并自测完成后.
3.  社区开发团队提交pull requests 到官方的 feature-xxx(注意:提交前需要merge官方develop分支的修改).
4.  仓库管理员将 feature-xxx 合并到某个提测版本分支(如:feature-platon-1.1.5).
5.  测试对提测分支 feature-platon-1.1.5 功能验证,如存在问题提issue.
6.  测试完成后合并到 develop 分支.
```