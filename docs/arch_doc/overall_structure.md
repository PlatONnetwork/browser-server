[回到首页](../../README.md)
### 整体架构
![Image text](image/overall_structure-logical_view.png)

#### browser-web
> Alaya 区块链浏览器web端。 [browser-web](https://github.com/PlatONnetwork/browser-web)

#### browser-api
> 对browser-web提供api查询服务。对应[接口文档](https://platonnetwork.github.io/browser-server/)。

#### browser-agent
> 区块同步分析服务，主要功能是对区块数据采集分析，最终存入数据库系统

#### 特殊节点
> 区别于普通节点，提供批量查询账户余额接口、查询历史验证人列表接口等接口。 [特殊节点](https://github.com/PlatONnetwork/PlatON-Go/tree/special-alaya-develop)

#### 数据存储
##### redis
> 热点数据的存储查询

##### mysql
> 小表数据的存储查询

##### elasticsearch
> 大表数据的存储查询，如区块、交易等