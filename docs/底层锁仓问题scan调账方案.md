### 1. 总体方案。
1. 特殊节点负责采集、保存底层调整的异常数据，并对agent提供查询接口。数据为委托、质押扣减明细。
2. agent在升级提案生效块高，查询特殊节点异常数据接口，并调整委托、质押、统计数据。
	
### 2. 异常流程
> 因为现网的数据复杂程度远远高于测试的,如果agent在调账过程中出现数据异常，需要人工介入
	
### 3. 详细设计

#### 3.1 特殊节点调账接口

```
入参：blockNumber                  -- 块高
出参：[
	{
		"optType":"",             -- 操作类型 staking、delegate
		"nodeId":"",              -- 节点id
		"stakingBlockNum":"",     -- 验证人块高
		"addr":"",                -- 质押或委托地址
		"lock":"",                -- 需要减少的锁定金额
		"has":""                  -- 需要减少的犹豫金额
	}
]

```

#### 3.2 特殊节点设计	
	
1. 新增数据库，用保存调整信息
2. 在底层兼容逻辑执行完成后，将调整信息入库
3. 提供合约查询接口

#### 3.3 agent设计

##### 3.3.1 执行时机
> 在升级提案ActiveBlock块高处执行，交易执行前。 并且调整接口存在数据。

##### 3.3.2 委托信息调账

##### 3.3.2.1 调账准备

- List<DiffItem> adjustParamList: 委托调账明细
- List<Delegation> delegationList： 待调整委托信息列表
- List<Staking> stakingList： 委托关联的质押信息
- List<Node> nodeList: 委托关联的节点信息
- List<Address> addressList: 地址信息，目前机制已经加载到进程内存中

##### 3.3.2.2 调账逻辑

1. 金额校验，如果失败则改明细调账失败。delegation.delegate_hes >= diffItem.has 并且 delegation.delegate_locked >= diffItem.locked
2. 委托信息调整（同委托赎回逻辑）

##### 3.3.2.3 调账结果
> 结果输出到指定日志中. diff.log

```
blockNumber=number | srcData=JSON.string(diffItem) | result = (success|error)

```

##### 3.3.3 质押信息调账

##### 3.3.3.1 调账准备

- List<DiffItem> adjustParamList: 待调账质押明细
- List<Staking> stakingList： 待调整质押信息
- List<Node> nodeList: 委托关联的节点信息
- List<Address> addressList: 地址信息，目前机制已经加载到进程内存中

##### 3.3.3.2 调账逻辑

1. 金额校验，如果失败则改明细调账失败。staking.staking_hes >= diffItem.has 并且 staking.staking_locked >= diffItem.locked
2. 质押信息调整（金额扣减同委托解除质押逻辑，如果扣减小于最小质押阀值，质押退出）

##### 3.3.3.3 调账结果
> 结果输出到指定日志中. diff.log

```
blockNumber=number | srcData=JSON.string(diffItem) | result = (success|error)

```

##### 3.3.4 调账完成。

- 无错误： 退出调账逻辑后正常执行。
- 有错误： 打印错误日志后退出。 （reconciliation failed）


#### 3.4 失败明细补偿
1. 根据agent吐出的数据，人工分析原因后，生成sql脚本。
2. 执行sql脚本。
3. 非补偿模式启动agent。
		
### 4. 对用户影响
1. 如果调账过程解除委托关系，并委托存在奖励，aton和scan看不到奖励领取记录。
2. 如果调账过程解除委托关系，我的委托记录会消失，不会存在交易记录。