define({ "api": [
  {
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "optional": false,
            "field": "varname1",
            "description": "<p>No type.</p>"
          },
          {
            "group": "Success 200",
            "type": "String",
            "optional": false,
            "field": "varname2",
            "description": "<p>With type.</p>"
          }
        ]
      }
    },
    "type": "",
    "url": "",
    "version": "0.0.0",
    "filename": "./apidoc/main.js",
    "group": "D__GitWorkspace_browser_server_server_apidoc_main_js",
    "groupTitle": "D__GitWorkspace_browser_server_server_apidoc_main_js",
    "name": ""
  },
  {
    "type": "post",
    "url": "block/blockDetails",
    "title": "b.区块详情",
    "version": "1.0.0",
    "name": "blockDetails",
    "group": "blcok",
    "description": "<p>区块详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"height\": 123,//区块高度(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"height\": 19988,//块高\n          \"timeStamp\": 123123123879,//出块时间\n          \"transaction\": 1288,//块内交易总数\n          \"hash\": \"0x1238\",//区块hash\n          \"parentHash\": \"0x234\",//父区块hash\n          \"miner\": \"0x234\", // 出块节点（多少时间内）\n          \"size\": 123,//区块大小\n          \"energonLimit\": 24234,//能量消耗限制\n          \"energonUsed\": 2342,//能量消耗\n          \"blockReward\": \"123123\",//区块奖励\n          \"extraData\": \"xxx\"//附加数据\n          }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/BlockController.java",
    "groupTitle": "blcok",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": true,
            "field": "Content-Type",
            "defaultValue": "application/json;charset=UTF-8",
            "description": ""
          }
        ]
      }
    }
  },
  {
    "type": "post",
    "url": "block/blockList",
    "title": "a.区块列表",
    "version": "1.0.0",
    "name": "blockList",
    "group": "blcok",
    "description": "<p>区块列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"query\": \"\",//区块高度/地址/块哈希/交易哈希(非必填)\n     \"pageNo\": 1,//页数(必填)\n     \"pageSize\": 10,//页大小(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"totalCount\":18,//总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n                 {\n                 \"height\": 17888,//块高\n                 \"timeStamp\": 1798798798798,//出块时间\n                 \"transaction\": 10000,//块内交易数\n                 \"size\": 188,//块大小\n                 \"miner\": \"0x234\", // 出块节点\n                 \"energonUsed\": 111,//能量消耗\n                 \"energonAverage\": 11, //平均能量价值\n                 \"blockReward\": \"123123\",//区块奖励\n                 \"serverTime\": 1708098077  //服务器时间\n                 }\n             ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/BlockController.java",
    "groupTitle": "blcok",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": true,
            "field": "Content-Type",
            "defaultValue": "application/json;charset=UTF-8",
            "description": ""
          }
        ]
      }
    }
  },
  {
    "type": "put",
    "url": "/app/block/init",
    "title": "g.实时区块列表（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "block_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"result\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n    {\n   \t    \"height\": ,//区块高度\n   \t    \"timeStamp\":\"\",//出块时间\n   \t    \"serverTime\": //服务器时间\n   \t    \"node\": \"\"//出块节点\n   \t    \"transaction\": //交易数\n   \t    \"blockReward\": //区块奖励\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "get",
    "url": "/topic/block/new",
    "title": "h.实时区块列表（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "block_new",
    "group": "home",
    "description": "<p>增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送</p>",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "put",
    "url": "/app/index/init",
    "title": "c.实时监控指标（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "index_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"result\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n        {\n   \t    \"currentHeight\": ,//当前区块高度\n   \t    \"node\":\"\",//出块节点\n   \t    \"currentTransaction\": //当前交易笔数\n   \t    \"consensusNodeAmount\": //共识节点数\n   \t    \"addressAmount\": //地址数\n   \t    \"voteAmount\": //投票数\n   \t    \"proportion\": //占比\n   \t    \"ticketPrice\": //票价\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "get",
    "url": "/topic/index/new",
    "title": "d.实时监控指标（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "index_new",
    "group": "home",
    "description": "<p>增量数据</p>",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "put",
    "url": "/app/node/init",
    "title": "a.节点监控图标数据（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "node_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"result\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n        {\n   \t    \"longitude\": \"\",//经度\n   \t    \"latitude\":\"\",//纬度\n   \t    \"nodeType\": //节点状态：1-共识节点 2-非共识\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "get",
    "url": "/topic/node/new",
    "title": "b.节点监控图标数据（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "node_new",
    "group": "home",
    "description": "<p>增量数据</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "post",
    "url": "/home/query",
    "title": "k.搜索",
    "version": "1.0.0",
    "name": "query",
    "group": "home",
    "description": "<p>根据区块高度，区块hash，交易hash等查询信息</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n    \"parameter\":\"\"//块高，块hash，交易hash等\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"result\": 0,//成功（0），失败则由相关失败码\n   \"data\":{\n       \"type\":\"\",//区块block，交易transaction，节点node,合约contract,\n        \"struct\":{\n   \t        \"height\": 17888,//块高\n               \"timeStamp\": 1798798798798,//出块时间\n               \"transaction\": 10000,//块内交易数\n               \"size\": 188,//块大小\n               \"miner\": \"0x234\", // 出块节点\n               \"energonUsed\": 111,//能量消耗\n               \"energonAverage\": 11, //平均能量价值\n               \"blockReward\": \"123123\",//区块奖励\n               \"serverTime\": 1708098077  //服务器时间\n        }\n     }\n\n\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "put",
    "url": "/app/statis/init",
    "title": "e.出块时间及交易数据（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "statis_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"result\": 0,//成功（0），失败则由相关失败码\n   \"data\": {\n   \t    \"avgTime\": ,//平均出块时长\n   \t    \"current\":\"\",//当前交易数量\n   \t    \"maxTps\": //最大交易TPS\n   \t    \"avgTransaction\": //平均区块交易数\n   \t    \"dayTransaction\": //过去24小时交易笔数\n   \t    \"blockStatisList\": [\n   \t    {\n   \t        \"height\": ,//区块高度\n   \t        \"time\":, //出块的时间\n   \t        \"transaction\":  //区块打包数量\n\n   \t    }\n   \t    ]//投票数\n\n        }\n\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "get",
    "url": "/topic/statis/new",
    "title": "f.出块时间及交易数据（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "statis_new",
    "group": "home",
    "description": "<p>增量数据</p>",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "put",
    "url": "/app/transaction/init",
    "title": "i.实时交易列表（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "transaction_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"result\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n    {\n   \t    \"txHash\": \"\",//交易Hash\n   \t    \"from\":\"\",//交易发起方地址\n   \t    \"to\": //交易接收方地址\n   \t    \"value\": \"\"//数额\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "get",
    "url": "/topic/transaction/new",
    "title": "j.实时交易列表（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "transaction_new",
    "group": "home",
    "description": "<p>增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送</p>",
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "post",
    "url": "transaction/transactionDetails",
    "title": "b.交易详情",
    "version": "1.0.0",
    "name": "transactionDetails",
    "group": "transaction",
    "description": "<p>交易详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"txHash\": \"\",//交易Hash(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"txHash\": \"0x234234\",//交易hash\n          \"timeStamp\": 123123123879,//交易时间\n          \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n          \"blockHeight\": \"15566\",//交易所在区块高度\n          \"from\": \"0x667766\",//发送者\n          \"to\": \"0x667766\",//接收者\n          \"txType\": \"\", // 交易类型\n                    transfer ：转账\n                    MPCtransaction ： MPC交易\n                    contractCreate ： 合约创建\n                    vote ： 投票\n                    transactionExecute ： 合约执行\n                    authorization ： 权限\n          \"value\": \"222\",//数额\n          \"actualTxCoast\": \"22\",//实际交易手续费\n          \"energonLimit\": 232,//能量限制\n          \"energonUsed\": 122,//能量消耗\n          \"energonPrice\": \"123\",//能量价格\n          \"inputData\": \"\",//附加输入数据\n          \"expectTime\": 12312333 // 预计确认时间\n          }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/TransactionController.java",
    "groupTitle": "transaction",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": true,
            "field": "Content-Type",
            "defaultValue": "application/json;charset=UTF-8",
            "description": ""
          }
        ]
      }
    }
  },
  {
    "type": "post",
    "url": "transaction/transactionList",
    "title": "a.交易列表",
    "version": "1.0.0",
    "name": "transactionList",
    "group": "transaction",
    "description": "<p>交易列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"query\": \"\",//区块高度/地址/块哈希/交易哈希(非必填)\n     \"pageNo\": 1,//页数(必填)\n     \"pageSize\": 10,//页大小(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"totalCount\":18,//总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n          {\n          \"txHash\": \"0x234234\",//交易hash\n          \"blockHeight\": \"15566\",//交易所在区块高度\n          \"blockTime\": 18080899999,//出块时间\n          \"from\": \"0x667766\",//发送方\n          \"to\": \"0x667766\",//接收方\n          \"value\": \"222\",//数额\n          \"actualTxCoast\": \"22\",//交易费用\n          \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n          \"txType\": \"\", // 交易类型\n                        transfer ：转账\n                        MPCtransaction ： MPC交易\n                        contractCreate ： 合约创建\n                        vote ： 投票\n                        transactionExecute ： 合约执行\n                        authorization ： 权限\n          \"serverTime\": 1123123//服务器时间\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./src/main/java/com/platon/browser/Controller/TransactionController.java",
    "groupTitle": "transaction",
    "header": {
      "fields": {
        "Header": [
          {
            "group": "Header",
            "type": "String",
            "optional": true,
            "field": "Content-Type",
            "defaultValue": "application/json;charset=UTF-8",
            "description": ""
          }
        ]
      }
    }
  }
] });
