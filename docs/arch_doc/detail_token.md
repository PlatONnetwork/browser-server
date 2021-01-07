#### 1 协议设计

##### 1.1 ERC165（必须）

- function supportsInterface(bytes4 _interfaceID) external view returns (bool)

##### 1.2 ERC721（必须，并继承ERC165） 

- event Transfer(address indexed _from, address indexed _to, uint256 indexed _tokenId)  转账、 tokenId创建（0地址到owner地址）、tokenId销毁（owner地址到0地址）时需要触发事件
- event Approval(address indexed _owner, address indexed _approved, uint256 indexed _tokenId)
- event ApprovalForAll(address indexed _owner, address indexed _operator, bool _approved)
- function safeTransferFrom(address _from, address _to, uint256 _tokenId, bytes calldata _data) external
- function safeTransferFrom(address _from, address _to, uint256 _tokenId) external
- function transferFrom(address _from, address _to, uint256 _tokenId) external;
- function approve(address _approved, uint256 _tokenId) external
- function setApprovalForAll(address _operator, bool _approved) external
- function balanceOf(address _owner) external view returns (uint256)
- function ownerOf(uint256 _tokenId) external view returns (address)
- function getApproved(uint256 _tokenId) external view returns (address)
- function isApprovedForAll(address _owner, address _operator)  external view returns (bool)


##### 1.3 ERC721 metadata (可选，需继承ERC721)

- function name() external view returns (string memory _name)
- function symbol() external view returns (string memory _symbol)
- function tokenURI(uint256 _tokenId) external view returns (string memory)

```
ERC721 Metadata JSON Schema

{
    "title": "Asset Metadata",
    "type": "object",
    "properties": {
        "name": {
            "type": "string",
            "description": "Identifies the asset to which this NFT represents"
        },
        "description": {
            "type": "string",
            "description": "Describes the asset to which this NFT represents"
        },
        "image": {
            "type": "string",
            "description": "A URI pointing to a resource with mime type image/* representing the asset to which this NFT represents. Consider making any images at a width between 320 and 1080 pixels and aspect ratio between 1.91:1 and 4:5 inclusive."
        }
    }
}

```

##### 1.4 ERC721 enumeration (可选，需继承ERC721)

- function totalSupply() external view returns (uint256)
- function tokenByIndex(uint256 _index) external view returns (uint256)
- function tokenOfOwnerByIndex(address _owner, uint256 _index) external view returns (uint256)

##### 1.5 ERC721 tokenreceiver (可选，需继承ERC721) 

- function onERC721Received(address _operator, address _from, uint256 _tokenId, bytes calldata _data) external returns(bytes4)

##### 1.6 ERC20

- event Transfer(address indexed _from, address indexed _to, uint256 _value)
- event Approval(address indexed _owner, address indexed _spender, uint256 _value)
- function totalSupply() external view returns (uint256)
- function balanceOf(address _owner) external view returns (uint256 balance)
- function transfer(address _to, uint256 _value) external returns (bool success)
- function transferFrom(address _from, address _to, uint256 _value) external returns (bool success)
- function approve(address _spender, uint256 _value) external returns (bool success)
- function allowance(address _owner, address _spender) external view returns (uint256 remaining)
- function name() public view returns (string)
- function symbol() public view returns (string)
- function decimals() public view returns (uint8)
- function totalSupply() public view returns (uint256)

#### 2 合约识别

##### 2.1 ERC-165 识别

1. The source contract makes a STATICCALL to the destination address with input data: 0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000 and gas 30,000. This corresponds to contract.supportsInterface(0x01ffc9a7).
2. If the call fails or return false, the destination contract does not implement ERC-165.
3. If the call returns true, a second call is made with input data 0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000.
4. If the second call fails or returns true, the destination contract does not implement ERC-165.
5. Otherwise it implements ERC-165.

```
public boolean isSupportErc165(String contractAddress) throws Exception {
	Transaction transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress,"0x01ffc9a701ffc9a700000000000000000000000000000000000000000000000000000000");
	PlatonCall platonCall = web3j.platonCall(transaction, DefaultBlockParameterName.LATEST).send();
	if(!"0x0000000000000000000000000000000000000000000000000000000000000001".equals(platonCall.getResult())){
		return false;
	}

	transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress,"0x01ffc9a7ffffffff00000000000000000000000000000000000000000000000000000000");
	platonCall = web3j.platonCall(transaction, DefaultBlockParameterName.LATEST).send();
	if("0x0000000000000000000000000000000000000000000000000000000000000000".equals(platonCall.getResult())){
		return true;
	}
	return false;
}
```

##### 2.2 ERC-721 识别

1. check support ERC-165.
2. The source contract makes a STATICCALL to the destination address with input data: 0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000 . This corresponds to contract.supportsInterface(0x80ac58cd).

```
public boolean isSupportErc721(String contractAddress) throws Exception {
	Transaction transaction = Transaction.createEthCallTransaction(Credentials.create(Keys.createEcKeyPair()).getAddress(), contractAddress,"0x01ffc9a780ac58cd00000000000000000000000000000000000000000000000000000000");
	PlatonCall platonCall = web3j.platonCall(transaction, DefaultBlockParameterName.LATEST).send();
	if("0x0000000000000000000000000000000000000000000000000000000000000001".equals(platonCall.getResult())){
		return true;
	}
	return false;
}
```

##### 2.3 ERC-721 metadata 识别

1. check support ERC-165.
2. The source contract makes a STATICCALL to the destination address with input data: 0x01ffc9a75b5e139f00000000000000000000000000000000000000000000000000000000 . This corresponds to contract.supportsInterface(0x5b5e139f).


##### 2.4 ERC-721 enumerable 识别

1. check support ERC-165.
2. The source contract makes a STATICCALL to the destination address with input data: 0x01ffc9a7780e9d6300000000000000000000000000000000000000000000000000000000 . This corresponds to contract.supportsInterface(0x780e9d63).

##### 2.5 ERC-20 识别
1. 如果非erc721
2. check function name()
3. check function symbol()
4. check function decimals()
5. check function totalSupply()

#### 3 架构设计

##### 3.1 数据库设计

![Image text](image/detail_token-db.png)

###### 3.1.1 新增token表

```
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `address` VARCHAR(64) NOT NULL COMMENT '合约地址',
  `type` VARCHAR(64) NOT NULL COMMENT '合约类型 erc20 erc721',
  `name` VARCHAR(64) COMMENT '合约名称',
  `symbol` VARCHAR(64) COMMENT '合约符号',
  `total_supply` bigint(80) COMMENT '供应总量',
  `decimal` bigint(80) COMMENT '合约精度',
  `is_support_erc165` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持erc165接口： 0-不支持 1-支持',
  `is_support_erc20` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持erc20接口： 0-不支持 1-支持',
  `is_support_erc721` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持erc721接口： 0-不支持 1-支持',
  `is_support_erc721_enumeration` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否支持erc721 enumeration接口： 0-不支持 1-支持',
  `is_support_erc721_metadata` tinyint(1) NOT NULL COMMENT '是否支持metadata接口： 0-不支持 1-支持',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`address`),
  UNIQUE KEY `token_address` (`address`)
)
```

###### 3.1.2 新增token_expand表

```
DROP TABLE IF EXISTS `token_expand`;
CREATE TABLE `token_expand` (
  `address` varchar(64) NOT NULL COMMENT '合约地址',
  `icon` text COMMENT '合约图标',
  `web_site` varchar(256) COMMENT '合约地址',
  `details` varchar(256) COMMENT '合约官网',
  `is_show_in_aton` tinyint(1) DEFAULT '0' COMMENT 'aton中是否显示，0-隐藏 1-展示',
  `is_show_in_scan` tinyint(1) DEFAULT '0' COMMENT 'scan中是否显示，0-隐藏 1-展示',
  `is_can_transfer` tinyint(1) DEFAULT '0' COMMENT '是否可转账 0-不可转账 1-可转账',
  `create_id` bigint(20) NOT NULL COMMENT '创建者',
  `create_name` varchar(50) NOT NULL COMMENT '创建者名称',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_id` bigint(20) NOT NULL COMMENT '更新者',
  `update_name` varchar(50) NOT NULL COMMENT '更新者名称',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`address`)
)
```

###### 3.1.3 新增token_holder表

```
DROP TABLE IF EXISTS `token_holder`;
CREATE TABLE `token_holder` (
  `token_address` varchar(64) NOT NULL COMMENT '合约地址',
  `address` varchar(64) NOT NULL COMMENT '用户地址',
  `balance` bigint(80) COMMENT '地址代币余额, ERC20为金额，ERC721为tokenId数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`token_address`,`address`)
)
```

###### 3.1.4 新增token_inventory表

```
DROP TABLE IF EXISTS `token_inventory`;
CREATE TABLE `token_inventory` (
  `token_address` varchar(64) NOT NULL COMMENT '合约地址',
  `token_id` bigint(80) NOT NULL COMMENT 'token id',
  `name` varchar(256) COMMENT 'Identifies the asset to which this NFT represents',
  `description` varchar(256) COMMENT 'Describes the asset to which this NFT represents',
  `image` varchar(256) COMMENT 'A URI pointing to a resource with mime type image/* representing the asset to which this NFT represents. Consider making any images at a width between 320 and 1080 pixels and aspect ratio between 1.91:1 and 4:5 inclusive.',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`token_address`,`token_id`)
)
```

###### 3.1.5 网络维度统计

```
-- 整个网络下erc721交易数
ALTER TABLE `network_stat` ADD COLUMN `erc721_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'erc721 token对应的交易数';

-- 整个网络下erc20交易数
ALTER TABLE `network_stat` ADD COLUMN `erc20_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'erc20 token对应的交易数';

```

###### 3.1.6 token维度统计

```
-- 该token合约产生的erc20或erc721交易数
ALTER TABLE `token` ADD COLUMN `token_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'token对应的交易数';

-- 该token合约holder统计
ALTER TABLE `token` ADD COLUMN `holder` INT(11) DEFAULT 0  NOT NULL COMMENT 'token对应的持有人';

```

###### 3.1.7 token下inventory维度统计

```
-- 该token合约tokenId关联的erc721交易数
ALTER TABLE `token_inventory` ADD COLUMN `token_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'token对应的交易数';

```

###### 3.1.8 token下holder维度统计

```
-- token下address关联的erc20或erc721交易数
ALTER TABLE `token_holder` ADD COLUMN `token_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'erc721 token对应的交易数'; 

```

###### 3.1.9 address维度统计

```
-- 该地址关联的erc721交易数
ALTER TABLE `address` ADD COLUMN `erc721_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'erc721 token对应的交易数';

-- 该地址关联的erc20交易数
ALTER TABLE `address` ADD COLUMN `erc20_tx_qty` INT(11) DEFAULT 0  NOT NULL COMMENT 'erc20 token对应的交易数';

```

##### 3.2 Elasticsearch设计

##### 3.2.1 browser_erc721_tx_* 模板

```
{
  "index_patterns": [
    "browser_erc721_tx_*"
  ],
  "settings": {
    "index": {
      "max_result_window": "2000000000",
      "number_of_shards": "5",
      "number_of_replicas": "1"
    }
  },
  "mappings": {
    "properties": {
      "seq": {                          //顺序号  交易所在块号*100000+本区块内部交易索引号
        "type": "long"
      },
      "name": {                         //名称
        "type": "text"
      },
      "symbol": {                       //币种符号
        "type": "keyword"
      },
      "decimal": {                      //币种精度
        "type": "integer"
      },
      "contract": {                     //合约地址
        "type": "keyword"
      },
      "hash": {                         //交易hash
        "type": "keyword"
      },
      "from": {                         //代币扣除方
        "type": "keyword"
      }, 
      "to": {                          //代币接收方
        "type": "keyword"
      },
      "value": {                       //代币数量或tokenId
        "type": "keyword"
      },
      "bn": {                           //交易所在块高
        "type": "long"
      },
      "bTime": {                        //交易时间
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis",
        "type": "date"
      },
      "toType": {                      //地址类型 1-普通地址  2-内置合约地址  3-合约地址   4-token地址
        "type": "integer"
      },
      "fromType": {                    //地址类型 1-普通地址  2-内置合约地址  3-合约地址   4-token地址
        "type": "integer"
      },
    }
  }
}
```

##### 3.2.2 browser_erc20_tx_* 模板(同 browser_erc721_tx_*)

##### 3.2.3 browser_inner_tx_* 模板
> 合约内部转账交易

```
{
  "index_patterns": [
    "browser_inner_tx_*"
  ],
  "settings": {
    "index": {
      "max_result_window": "2000000000",
      "number_of_shards": "5",
      "number_of_replicas": "1"
    }
  },
  "mappings": {
    "properties": {
      "seq": {                          //顺序号  交易所在块号*100000+本区块内部交易索引号
        "type": "long"
      },
      "contract": {                     //合约地址
        "type": "keyword"
      },
      "hash": {                         //交易hash
        "type": "keyword"
      },
      "from": {                         //主币扣除方
        "type": "keyword"
      }, 
      "to": {                          //主币接收方
        "type": "keyword"
      },
      "value": {                       //主币数量
        "type": "keyword"
      },
      "bn": {                           //交易所在块高
        "type": "long"
      },
      "bTime": {                        //交易时间
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis",
        "type": "date"
      },
      "toType": {                      //地址类型 1-普通地址  2-内置合约地址  3-合约地址   4-token地址
        "type": "integer"
      },
      "fromType": {                    //地址类型 1-普通地址  2-内置合约地址  3-合约地址   4-token地址
        "type": "integer"
      }
    }
  }
}
```

##### 3.2.4 browser_transaction_* 模板

```
{
  "index_patterns": [
    "browser_transaction_*"
  ],
  "settings": {
    "index": {
      "max_result_window": "2000000000",
      "number_of_shards": "5",
      "number_of_replicas": "1"
    }
  },
  "mappings": {
    "properties": {
      "seq": {                     //顺序号  交易所在块号*100000+本区块内部交易索引号
        "type": "long"
      },
      "hash": {                    //交易hash
        "type": "keyword"
      },


      "contract_Type": {
        "type": "integer"
      },
      "bin": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "num": {
        "type": "long"
      },
      "type": {
        "type": "short"
      },
      "to_type": {
        "type": "integer"
      },
      "gas_limit": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "from": {
        "type": "keyword"
      },
      "upd_time": {
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis",
        "type": "date"
      },
      "id": {
        "type": "long"
      },
      "value": {
        "type": "text"
      },

      "info": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "gas_price": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "cost": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "method": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "gas_used": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "index": {
        "type": "short"
      },
      "b_hash": {
        "type": "keyword"
      },
      "nonce": {
        "type": "long"
      },
      "input": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },
      "cre_time": {
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis",
        "type": "date"
      },
      "time": {
        "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis",
        "type": "date"
      },
      "to": {
        "type": "keyword"
      },
      "fail_reason": {
        "norms": false,
        "index": false,
        "type": "text",
        "doc_values": false
      },

      "status": {
        "type": "integer"
      }
    }
  }
}


```


> 合约内部转账交易


- 新增erc721List: json字符串
- 新增erc20List: json字符串
- 新增pposList: json字符串

```
[
      {
        "bTime": 1609992716356,
        "bn": 1856319,
        "contract": "atp1z54wl969sm3yzvwcnvey4epvruql8gn9z9c56k",
        "ctime": 1609992718825,
        "decimal": 16,
        "from": "atp1cy2uat0eukfrxv897s5s8lnljfka5ewjj943gf",
        "fromType": 1,
        "hash": "0xa120ab3ca192f6d1db504012d909da8dbae725f36b861def0dbde7d346111c3e",
        "info": "0x00000000000000000000000000000000000000000000000001aa535d3d0c0000",
        "name": "aLAT",
        "result": 1,
        "seq": 185631900000,
        "symbol": "aLAT",
        "tValue": "120000000000000000",
        "toType": 1,
        "tto": "atp1sc4tk9kdtctqrd53sln5ud0yhxjexrjx3vnslh",
        "txFee": "711700000000000",
        "value": "0"
      }
]

```

##### 3.3 特殊节点设计
- 修改批量修改交易回执接口。 交易hash -> 合约地址列表

