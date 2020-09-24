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
    "filename": "./serverdoc/main.js",
    "group": "D__GitWorkspace_browser_server_serverdoc_main_js",
    "groupTitle": "D__GitWorkspace_browser_server_serverdoc_main_js",
    "name": ""
  },
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
    "filename": "./serverdoc/serverdoc/main.js",
    "group": "D__GitWorkspace_browser_server_serverdoc_serverdoc_main_js",
    "groupTitle": "D__GitWorkspace_browser_server_serverdoc_serverdoc_main_js",
    "name": ""
  },
  {
    "type": "",
    "url": "接口请求头设置说明",
    "title": "",
    "version": "1.0.0",
    "group": "about_request_header",
    "name": "_________",
    "parameter": {
      "examples": [
        {
          "title": ":",
          "content": "\n本接口文档中定义的接口中的通用参数统一使用请求头传输，例如访问交易列表接口：\ncurl -X POST \\\n  http://192.168.9.190:20060/app-203/v060//transaction/list \\\n  -H 'Accept: application/json' \\\n    -H 'Content-Type: application/json' \\\n    -H 'x-aton-cid: 203' \\ # 链ID\n    -H 'content-length: 136' \\\n    -d '{\n    \"beginSequence\":1,\n    \"listSize\":30,\n    \"address\":\"0x493301712671ada506ba6ca7891f436d29185821\",\n    \"cid\":\"20d 3\"\n    }'",
          "type": "json"
        }
      ]
    },
    "filename": "./app-api/src/main/java/com/platon/browser/controller/BaseController.java",
    "groupTitle": "about_request_header"
  },
  {
    "type": "post",
    "url": "api/getBatchVoteSummary",
    "title": "a.获取统计统计信息",
    "version": "1.0.0",
    "name": "GetBatchVoteSummary",
    "group": "api",
    "description": "<p>获取统计统计信息</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n    \"cid\":\"\", // 链ID (必填)\n     \"addressList\":[\n      addlist1,      //地址列表list<String>\n      addlist2\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": [\n              {\n              \"locked\":\"1999900000\",//String类型,投票锁定,单位Energon\n              \"earnings\":\"1999900000\",//String类型,投票收益,单位Energon\n              \"totalTicketNum\": \"100\",//string！！！ 总票数\n              \"validNum\":\"2\"//String类型,有效票数\n              }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/getBatchVoteTransaction",
    "title": "b.批量获选票交易",
    "version": "1.0.0",
    "name": "getBatchVoteTransaction",
    "group": "api",
    "description": "<p>批量获选票交易</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"pageNo\": 1,//页数(必填)默认1\n     \"pageSize\": 10,//页大小(必填)默认10,\n       \"cid\":\"\", // 链ID (必填)\n      \"walletAddrs\" :\n         [\n         addlist1,      //地址列表list<String>\n         addlist2\n         ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"totalCount\":18,//总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n          {\n          \"TransactionHash\":\"0xbbbbbb...\",//String类型\n          \"candidateId\":\"0xffffff....\",//候选人Id\n          \"owner\":\"0xbbb...\"//投票人钱包地址，\n          \"earnings\":\"90000000000000000\",//此次交易投票获得的收益，单位Energon\n          \"transactiontime\":\"1231231233\".//Unix时间戳，毫秒级,交易时间\n          \"deposit\":\"1000000000000\",// 当时的购票价格，单位Energon\n          \"totalTicketNum\": \"100\",// 总票数\n          \"validNum\": \"50\",// 有效票\n      }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/getCandidateTicketCount",
    "title": "c.批量获取节点有效选票",
    "version": "1.0.0",
    "name": "getCandidateTicketCount",
    "group": "api",
    "description": "<p>批量获取节点有效选票</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n\n      \"nodeIds\" :\n         [\n         nodeId1,      //节点id列表list<String>\n         nodeId2\n         ],\n        \"cid\":\"\", // 链ID (必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": [\n          {\n          \"nodeId1\" : 10, //节点id : 有效票\n          \"nodeId2\" : 1,\n          \"nodeId3\" : 3\n      }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/getTicketCountByTxHash",
    "title": "d.批量获取投票交易相关信息",
    "version": "1.0.0",
    "name": "getTicketCountByTxHash",
    "group": "api",
    "description": "<p>批量获取投票交易相关信息</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n      \"hashList\" :\n         [\n         \"0xqew1652131d3....\"      //hash列表list<String>\n         ]\n      \"cid\":\"\", // 链ID (必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"totalCount\":18,//总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n          {\n          \"nodeId\":\"0xbbbbbb...\",//节点Id\n          \"nodeName\":\"123\",//节点名称\n          \"vailVoteCount\":12,//有效票数\n          \"voteSum\":22,//总票数\n          \"deadLine\":\"1231231233\".//实际过期时间\n          \"walletAddress\":\"0xbbbbbb\",//钱包地址\n          \"price\":\"100\"//票价\n          \"income\":,//收益\n      }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/node/details",
    "title": "g.获取节点详情",
    "version": "1.0.0",
    "name": "node_details",
    "group": "api",
    "description": "<p>获取节点详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n      \"cid\":\"\",                        //链ID (必填)\n      \"nodeId\":\"0x\",                   //节点ID\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                      //描述信息\n     \"code\":0,                         //成功（0），失败则由相关失败码\n     \"data\": {\n          \"nodeId\":\"0x\",               //节点ID\n          \"ranking\":1,                 //质押排名\n          \"name\":\"node-1\",             //节点名称\n          \"deposit\":\"1.254555555\",     //质押金(单位:Energon)\n          \"rewardRatio\":0.02           //投票激励:小数\n          \"orgName\":\"\",                //机构名称\n          \"orgWebsite\":\"\",             //机构官网\n          \"intro\":\"\",                  //节点简介\n          \"nodeUrl\":\"\",                //节点地址\n          \"ticketCount\":\"\",            //得票数\n          \"joinTime\":199880011,        //加入时间，单位-毫秒\n          \"electionStatus\":1,          //竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名 ？\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/node/list",
    "title": "f.获取节点列表",
    "version": "1.0.0",
    "name": "node_list",
    "group": "api",
    "description": "<p>获取节点列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n      \"cid\":\"\",                        //链ID (必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                      //描述信息\n     \"code\":0,                         //成功（0），失败则由相关失败码\n     \"voteCount\":90,                   //已投票\n     \"proportion\":86,                  //投票率:小数\n     \"ticketPrice\":3.66,               //票价（单位Energon）\n     \"data\":[\n          {\n          \"nodeId\":\"0x\",               //节点ID\n          \"ranking\":1,                 //质押排名\n          \"name\":\"node-1\",             //节点名称\n          \"countryCode\":\"CN\",          //国家代码\n          \"countryEnName\":\"\",          //国家英文名称\n          \"countryCnName\":\"\",          //国家中文名称\n          \"countrySpellName\":\"\",       //国家拼音名称，中文环境下，区域进行排序\n          \"deposit\":\"1.254555555\",     //质押金(单位:Energon)\n          \"rewardRatio\":0.02           //投票激励:小数\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/transaction/list",
    "title": "e.通过地址和指定交易序号查询交易列表",
    "version": "1.0.0",
    "name": "transaction_list",
    "group": "api",
    "description": "<p>通过地址和指定交易序号查询交易列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n      \"address\":\"0xsdfsdfsdfsdf\", // 地址 (必填)\n      \"beginSequence\":120, // 起始序号 (必填)\n      \"listSize\":100, // 列表大小 (必填)\n      \"cid\":\"\", // 链ID (必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "[\n    {\n    \"actualTxCost\":\"21168000000000\", // 交易实际花费值(手续费)，单位：wei\n    \"blockHash\":\"0x985447eb289ca2e277b62356252504ea63ecb7a167d641321a64179d4c7ef797\", // 区块hash\n    \"blockNumber\":187566, // 区块高度\n    \"chainId\":\"203\", // 链id\n    \"createTime\":1557484976000, //  创建时间\n    \"energonLimit\":\"210000\", // 能量限制\n    \"energonPrice\":\"1000000000\", // 能量价格\n    \"energonUsed\":\"21168\", // 能量消耗\n    \"from\":\"0xbae514b5f89a90e16535c87bcc72ea0619046a62\", // 交易发起方地址\n    \"hash\":\"0x9dd74d1bb44afc8b5be8c21263824ea4acdc7e153b4e6bac3691ce9186500b8c\", // 交易hash\n    \"nonce\":\"10\", // Nonce值\n    \"receiveType\":\"account\", // 交易接收者类型（to是合约还是账户）contract合约、 account账户\n    \"sequence\":153, // 排列序号：由区块号和交易索引拼接而成\n    \"timestamp\":1557484976000, // 交易时间（单位：毫秒）\n    \"to\":\"0x72adbbfd846f34ff54456219ef750e53621b6cc1\", // 交易接收方地址\n    \"transactionIndex\":0, // 交易在区块中位置\n    \"txInfo\":\"{\\\"parameters\\\":{},\\\"type\\\":\\\"0\\\"}\", // 交易信息\n    \"txReceiptStatus\":1, // 交易状态 1 成功 0 失败\n    \"txType\":\"transfer\", // 交易类型 transfer ：转账 MPCtransaction ： MPC交易 contractCreate ： 合约创建 vote ： 投票 transactionExecute ： 合约执行 authorization ： 权限 candidateDeposit：竞选质押 candidateApplyWithdraw：减持质押 candidateWithdraw：提取质押 unknown：未知\n    \"updateTime\":1557484976000, // 更新时间\n    \"value\":\"1000000000000000000\" // 交易金额\n    }\n]",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/vote/groupByNode",
    "title": "h.获取投票列表",
    "version": "1.0.0",
    "name": "vote_groupByNode",
    "group": "api",
    "description": "<p>获取投票列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n      \"cid\":\"\",                        //链ID (必填)\n      \"walletAddrs\":[                  //地址列表\n         \"address1\",\n         \"address2\"\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                      //描述信息\n     \"code\":0,                         //成功（0），失败则由相关失败码\n     \"data\":[\n          {\n          \"nodeId\":\"0x\",               //节点ID\n          \"name\":\"node-1\",             //节点名称\n          \"countryCode\":\"CN\",          //国家代码\n          \"countryEnName\":\"\",          //国家英文名称\n          \"countryCnName\":\"\",          //国家中文名称\n          \"validNum\":\"50\",             //有效票\n          \"totalTicketNum\":\"100\",      //总票数\n          \"locked\":\"\",                 //投票锁定,单位Energon\n          \"earnings\":\"\",               //投票收益,单位Energon\n          \"transactiontime\":\"\"         //最新投票时间，单位-毫秒\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "api/vote/listTxByNodeAndAdress",
    "title": "i.获取投票交易列表通过节点和地址",
    "version": "1.0.0",
    "name": "vote_listTxByNodeAndAdress",
    "group": "api",
    "description": "<p>获取投票列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n      \"cid\":\"\",                        //链ID (必填)\n      \"nodeId\":\"0x\",                   //节点ID\n      \"walletAddrs\":[                  //地址列表\n         \"address1\",\n         \"address2\"\n      ],\n      \"beginSequence\":120,             //起始序号 (必填)\n      \"listSize\":100,                  //列表大小 (必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                      //描述信息\n     \"code\":0,                         //成功（0），失败则由相关失败码\n     \"data\":[\n          {\n          \"nodeId\":\"0x\",               //节点ID\n          \"name\":\"node-1\",             //节点名称\n          \"validNum\":\"50\",             //有效票\n          \"totalTicketNum\":\"100\",      //总票数\n          \"locked\":\"\",                 //投票锁定,单位Energon\n          \"earnings\":\"\",               //投票收益,单位Energon\n          \"transactiontime\":\"\"         //最新投票时间，单位-毫秒\n          \"deposit\":\"1000000000000\",   //当时的购票价格，单位Energon\n          \"owner\":\"0x...\"              //投票人钱包地址\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/ApiController.java",
    "groupTitle": "api"
  },
  {
    "type": "post",
    "url": "aton/delegateDetails",
    "title": "c.通过【地址列表】参数该钱包参与过的所有的委托",
    "version": "1.0.0",
    "name": "aton_delegateDetails",
    "group": "aton",
    "description": "<p>获取委托详情通过钱包地址</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n         \"walletAddrs\":\"address1\"        //地址列表\n         \"beginSequence\":120,            //起始序号 (必填) 客户端首次进入页面时传-1，-1：代表最新记录\n         \"listSize\":100,                 //列表大小 (必填)\n         \"direction\":\"\"                  //方向 (必填) new：朝最新记录方向, old：朝最旧记录方向,\n                                             客户端首次进入页面时或者上拉时传old。客户端自动获取最新记录时传new。\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                         //描述信息\n     \"code\":0,                            //成功（0），失败则由相关失败码\n     \"data\": [\n            {\n                 \"noadeId\":\"0x21312fdf\",      //投票节点Id（节点地址 ）\n                 \"nodeName\":\"\",               //节点名称\n                 \"website\":\"\",                //机构官网（链接）\n                 \"url\":\"ASD123\"               //节点头像\n                 \"nodeStatus\":\"\",             //竞选状态:\n                                          Active —— 活跃中\n                                          Candidate —— 候选中\n                                          Exiting —— 退出中\n                                          Exited —— 已退出\n                 \"redeem\":   ,           //赎回中委托\n                 \"locked\":   ,           //已锁定委托\n                 \"unLocked\":   ,         //未锁定委托\n                 \"released\":   ,         //已接触委托\n                 \"sequence\":153                           //排列序号：由区块号和交易索引拼接而成\n            }\n     ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./app-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "aton"
  },
  {
    "type": "post",
    "url": "aton/delegateRecord",
    "title": "d.通过【地址列表】参数该钱包参与过的委托记录",
    "version": "1.0.0",
    "name": "aton_delegateRecord",
    "group": "aton",
    "description": "<p>获取全部的委托记录通过钱包地址</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"beginSequence\":120,            //起始序号 (必填) 客户端首次进入页面时传-1，-1：代表最新记录\n     \"listSize\":100,                 //列表大小 (必填)\n     \"direction\":\"\"                  //方向 (必填) new：朝最新记录方向, old：朝最旧记录方向,\n                                             客户端首次进入页面时或者上拉时传old。客户端自动获取最新记录时传new。\n     \"type\":\"all\",              //类型\n                                 all —— 全部\n                                 redeem —— 赎回\n                                 delegate —— 委托\n     \"walletAddrs\":[            //地址列表\n        \"address1\",\n        \"address2\"\n     ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                         //描述信息\n     \"code\":0,                            //成功（0），失败则由相关失败码\n     \"data\": [\n            {\n                 \"delegateTime\":  ,                       //委托时间，时间戳（秒）\n                 \"url\":\"ASD123\"                           //节点头像\n                 \"walletAddress\":\"0xa1j1nu14nt53t3\",      //委托钱包地址\n                 \"nodeName\":\"\"                            //委托节点名称\n                 \"nodeAddress\": \"0xa1j1nu14nt53t3\",       //委托的节点地址\n                 \"number\":120000,                         //委托数/赎回数\n                 \"sequence\":153                           //排列序号：由区块号和交易索引拼接而成\n                 \"delegateStatus\":\"\"                      //委交易状态\n                                                     确认中 —— confirm\n                                                     委托成功 —— delegateSucc\n                                                     委托失败 —— delegateFail\n                                                     赎回中 —— redeem\n                                                     赎回成功 —— redeemSucc\n                                                     赎回失败 —— redeemFail\n            }\n     ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./app-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "aton"
  },
  {
    "type": "post",
    "url": "aton/list",
    "title": "a.通过地址和指定交易序号查询交易列表",
    "version": "1.0.0",
    "name": "aton_list",
    "group": "aton",
    "description": "<p>通过地址和指定交易序号查询交易列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"walletAddrs\":[                      //地址列表 (必填)\n        \"address1\",\n        \"address2\"\n     ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                         //描述信息\n     \"code\":0,                            //成功（0），失败则由相关失败码\n     \"data\":[\n          {\n            \"actualTxCost\":\"211680\",      //交易实际花费值(手续费)，单位：E\n            \"chainId\":\"203\",              //链id\n            \"from\":\"0x...\",               //交易发起方地址\n            \"hash\":\"0x...\",               //交易hash\n            \"timestamp\":\"1557484976000\",  //交易时间（单位：毫秒）\n            \"to\":\"0x...\",                 //交易接收方地址\n            \"toType\":\"contract\",          //to的类型\n                                             contract —— 合约\n                                             address —— 地址\n            \"txInfo\":\"{json}\",            //交易详细信息\n            \"txReceiptStatus\":1,          //交易状态 1 成功 0 失败\n            \"txType\":\"CreateProposal\",          //交易类型\n                                                 Sending\\Sent发送\n     *                                           Receiving\\Receive接收\n                                                 CreateProposal：创建提案\n                                                 VotingProposal：提案投票\n                                                 DeclareVersion：版本声明\n                                                 ContractCreation：合约创建\n                                                 ContractExecution：合约执行\n                                                 CreateRestricting：创建锁仓\n                                                 Others：其他\n                                                 CreateValidator：创建验证人\n                                                 EditValidator：编辑验证人\n                                                 ExitValidator：退出验证人\n                                                 ReportValidator：举报验证人\n                                                 IncreaseStaking：增加自有质押\n                                                 Delegate：委托\n                                                 Undelegate：赎回委托\n           \"nodeName\":\"节点名称\"            //txType = Delegate,\n                                                      Undelegate,\n                                                      CreateValidator,\n                                                      EditValidator,\n                                                      IncreaseStaking,\n                                                      ExitValidator,\n                                                      ReportValidator,\n                                                      DeclareVersion,\n                                                      nodeName不为空\n            \"value\":\"1000000000000000000\" //交易金额，单位LAT\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./app-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "aton"
  },
  {
    "type": "post",
    "url": "aton/myDelegate",
    "title": "b.通过【地址列表】参数我的委托列表",
    "version": "1.0.0",
    "name": "aton_myDelegate",
    "group": "aton",
    "description": "<p>获取我的委托列表通过钱包地址</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"walletAddrs\":[                      //地址列表\n        \"address1\",\n        \"address2\"\n     ]\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                         //描述信息\n     \"code\":0,                            //成功（0），失败则由相关失败码\n     \"data\": {\n             \"delegateTotel\":   ,            //委托总计\n             \"delegateInfo\":\n                 [\n                     {\n                       \"walletName\":    ,            //钱包名称\n                       \"walletAddress\":\"0x\",         //钱包地址\n                       \"delegate\":   ,               //委托金额\n                       \"redeem\":     ,               //赎回中\n                     } ,\n                     {\n                       \"walletName\":    ,            //钱包名称\n                       \"walletAddress\":\"0x\",         //钱包地址\n                       \"delegate\":   ,               //委托金额\n                       \"redeem\":     ,               //赎回中\n                      }\n                  ]\n          }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./app-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "aton"
  },
  {
    "type": "post",
    "url": "aton/nodelist",
    "title": "a.获取验证节点列表",
    "version": "1.0.0",
    "name": "aton_nodelist",
    "group": "aton",
    "description": "<p>获取节点列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"beginSequence\":120,            //起始序号 (必填) 客户端首次进入页面时传-1，-1：代表最新记录\n     \"listSize\":100,                 //列表大小 (必填)\n     \"sortType\":\"\",                  //rangking —— 排名  PA —— 年化率\n     \"direction\":\"\"                  //方向 (必填) new：朝最新记录方向, old：朝最旧记录方向,\n                                             客户端首次进入页面时或者上拉时传old。客户端自动获取最新记录时传new。\n      \"screenType\":\"all\"             //筛选类型\n                                         all —— 全部\n                                         active —— 活跃中\n                                         candidate —— 候选中\n\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\":\"\",                           //描述信息\n     \"code\":0,                              //成功（0），失败则由相关失败码\n     \"data\": {\n         \"list\":[\n            {\n               \"nodeId\":\"0x\",               //节点ID\n               \"ranking\":1,                 //质押排名\n               \"name\":\"node-1\",             //节点名称\n               \"deposit\":\"100\",             //总押金(单位:ATP)\n               \"website\":\"\",                //机构官网\n               \"intro\":\"\",                  //节点简介\n               \"url\":\"\",                    //节点头像\n               \"nodeUrl\":\"\",                //节点地址\n               \"nodeStatus\":\"\",             //竞选状态:\n                                          Active —— 活跃中\n                                          Candidate —— 候选中\n                                          Exiting —— 退出中\n                                          Exited —— 已退出\n               \"ratePA\":\"\" ,                 //预计年化率\n               \"punishNumber\": ,             //处罚次数\n               \"delegateNumber\":,            //委托次数\n               \"delegate\":\"\",                //委托者\n               \"blockOutNumber\":,            //出块数\n               \"blockRate\":\"\" ,              //出块率\n               \"sequence\":153                //排列序号：由区块号和交易索引拼接而成\n            }\n        ]\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./app-api/src/main/java/com/platon/browser/controller/NodeController.java",
    "groupTitle": "aton"
  },
  {
    "type": "post",
    "url": "block/blockDetailNavigate",
    "title": "c.区块详情前后跳转浏览",
    "version": "1.0.0",
    "name": "blockDetailNavigate",
    "group": "block",
    "description": "<p>区块详情前后跳转浏览</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"direction\":\"\", // 方向：prev-上一个，next-下一个 (必填)\n     \"height\": 123,//区块高度(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"height\": 19988,//块高\n          \"timestamp\": 123123123879,//出块时间\n          \"transaction\": 1288,//块内交易总数\n          \"hash\": \"0x1238\",//区块hash\n          \"parentHash\": \"0x234\",//父区块hash\n          \"miner\": \"0x234\", // 出块节点地址\n          \"nodeName\": \"node-01\", // 出块节点名称\n          \"size\": 123,//区块大小\n          \"energonLimit\": 24234,//能量消耗限制\n          \"energonUsed\": 2342,//能量消耗\n          \"blockReward\": \"123123\",//区块奖励(单位:Energon)\n          \"extraData\": \"xxx\",//附加数据\n          \"first\":false, // 是否第一条记录\n          \"last\":true // 是否最后一条记录\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/BlockController.java",
    "groupTitle": "block"
  },
  {
    "type": "post",
    "url": "block/blockDetails",
    "title": "b.区块详情",
    "version": "1.0.0",
    "name": "blockDetails",
    "group": "block",
    "description": "<p>区块详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"height\": 123,//区块高度(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"height\": 19988,//块高\n          \"timestamp\": 123123123879,//出块时间\n          \"hash\": \"0x1238\",//区块hash\n          \"parentHash\": \"0x234\",//父区块hash\n          \"miner\": \"0x234\", // 出块节点地址\n          \"nodeName\": \"node-01\", // 出块节点名称\n          \"timeDiff\":424234, // 当前块出块时间距离上一个块出块时间之差（毫秒）\n          \"size\": 123,//区块大小\n          \"energonLimit\": 24234,//能量消耗限制\n          \"energonUsed\": 2342,//能量消耗\n          \"blockReward\": \"123123\",//区块奖励(单位:Energon)\n          \"extraData\": \"xxx\",//附加数据\n          \"first\":false, // 是否第一条记录\n          \"last\":true // 是否最后一条记录\n          \"transaction\": 1288,//块内交易总数\n          \"blockVoteAmount\":,//区块内投票交易个数\n               \"blockVoteNumber\":,//区块中包含的选票张数\n               \"blockCampaignAmount\"://区块内竞选交易个数\n          }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/BlockController.java",
    "groupTitle": "block"
  },
  {
    "type": "post",
    "url": "block/blockList",
    "title": "a.区块列表",
    "version": "1.0.0",
    "name": "blockList",
    "group": "block",
    "description": "<p>区块列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"pageNo\": 1,//页数(必填)\n     \"pageSize\": 10,//页大小(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"displayTotalCount\":18,//显示总数\n     \"totalCount\":18,// 小于等于500000记录的总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n                 {\n                 \"height\": 17888,//块高\n                 \"timestamp\": 1798798798798,//出块时间\n                 \"transaction\": 10000,//块内交易数\n                 \"size\": 188,//块大小\n                 \"miner\": \"0x234\", // 出块节点地址\n                 \"nodeName\": \"node-01\", // 出块节点名称\n                 \"energonUsed\": 111,//能量消耗\n                 \"energonLimit\": 24234,//能量消耗限制\n                 \"energonAverage\": 11, //平均能量价值(单位:Energon)\n                 \"blockReward\": \"123123\",//区块奖励(单位:Energon)\n                 \"serverTime\": 1708098077,  //服务器时间\n                 \"blockVoteAmount\":,//区块内投票交易个数\n                 \"blockVoteNumber\":,//区块中包含的选票张数\n                 \"blockCampaignAmount\"://区块内竞选交易个数\n                 }\n             ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/BlockController.java",
    "groupTitle": "block"
  },
  {
    "type": "post",
    "url": "block/transactionList",
    "title": "d.区块交易列表",
    "version": "1.0.0",
    "name": "transactionList",
    "group": "block",
    "description": "<p>区块交易列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n\"cid\":\"\", // 链ID (必填)\n\"pageNo\": 1,//页数(必填)\n\"pageSize\": 10,//页大小(必填)\n\"blockNumber\":500, // 区块号(必填)\n\"txType\":\"\", // 交易类型 (可选), 可以设置多个类型，使用逗号“,”分隔\n    transfer ：转账\n    MPCtransaction ： MPC交易\n    contractCreate ： 合约创建\n    voteTicket ： 投票\n    transactionExecute ： 合约执行\n    authorization ： 权限\n    candidateDeposit ： 竞选质押\n    candidateApplyWithdraw ： 减持质押\n    candidateWithdraw ： 提取质押\n    unknown ： 未知\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n\"errMsg\": \"\",//描述信息\n\"code\": 0,//成功（0），失败则由相关失败码\n\"displayTotalCount\":18,//显示总数\n\"totalCount\":18,// 小于等于500000记录的总数\n\"totalPages\":1,//总页数\n\"data\": [\n    {\n    \"txHash\": \"0x234234\",//交易hash\n    \"blockHeight\": \"15566\",//交易所在区块高度\n    \"blockTime\": 18080899999,//出块时间\n    \"from\": \"0x667766\",//发送方, 必定是钱包地址\n    \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n    // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n    \"value\": \"222\",//数额(单位:Energon)\n    \"actualTxCost\": \"22\",//交易费用(单位:Energon)\n    \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n    \"txType\": \"\", // 交易类型\n        transfer ：转账\n        MPCtransaction ： MPC交易\n        contractCreate ： 合约创建\n        voteTicket ： 投票\n        transactionExecute ： 合约执行\n        authorization ： 权限\n        candidateDeposit ： 竞选质押\n        candidateApplyWithdraw ： 减持质押\n        candidateWithdraw ： 提取质押\n        unknown ： 未知\n    \"txInfo\": \"{\n        \"functionName\":\"\",//方法名称\n        \"parameters\":{},//参数\n        \"type\":\"1\"//交易类型\n            0：转账\n            1：合约发布\n            2：合约调用\n            4：权限\n            5：MPC交易\n            1000：投票\n            1001：竞选质押\n            1002：减持质押\n            1003：提取质押\n    }\"//返回交易解析结构\n\n    \"serverTime\": 1123123,//服务器时间\n    \"failReason\":\"\",//失败原因\n    \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，\n    // 前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情\n  }\n]",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/BlockController.java",
    "groupTitle": "block"
  },
  {
    "type": "subscribe",
    "url": "/app/block/init?cid=:chainId",
    "title": "g.实时区块列表（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "block_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      },
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
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"code\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n    {\n   \t    \"height\":33 ,//区块高度\n   \t    \"timestamp\":33333,//出块时间\n   \t    \"serverTime\":44444, //服务器时间\n   \t    \"node\": \"node-1\",//出块节点\n   \t    \"transaction\":333, //交易数\n   \t    \"blockReward\":333 //区块奖励\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/topic/block/new?cid=:chainId",
    "title": "h.实时区块列表（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "block_new",
    "group": "home",
    "description": "<p>增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      }
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
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/app/index/init?cid=:chainId",
    "title": "c.实时监控指标（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "index_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      },
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
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"code\": 0,//成功（0），失败则由相关失败码\n   \"data\": {\n   \t    \"currentHeight\": ,//当前区块高度\n   \t    \"miner\":\"\",//出块节点地址\n   \t    \"nodeName\":\"\",//出块节点名称\n   \t    \"currentTransaction\": //当前交易笔数\n   \t    \"consensusNodeAmount\": //竞选节点数\n   \t    \"addressAmount\": //地址数\n   \t    \"voteCount\": //投票数\n   \t    \"proportion\": //占比 (小数)\n   \t    \"ticketPrice\": //票价 (单位: Energon)\n        }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/topic/index/new?cid=:chainId",
    "title": "d.实时监控指标（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "index_new",
    "group": "home",
    "description": "<p>增量数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      }
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
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/app/node/init?cid=:chainId",
    "title": "a.节点监控图标数据（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "node_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      },
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
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"code\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n        {\n   \t    \"longitude\": \"\",//经度\n   \t    \"latitude\":\"\",//纬度\n   \t    \"nodeType\": ,//节点状态：1-共识节点 2-非共识\n   \t    \"netState\": ,//节点状态 1 正常 2 异常\n   \t    \"nodeName\": //节点名称\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/topic/node/new?cid=:chainId",
    "title": "b.节点监控图标数据（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "node_new",
    "group": "home",
    "description": "<p>增量数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      },
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
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "post",
    "url": "/home/search",
    "title": "k.搜索",
    "version": "1.0.0",
    "name": "search",
    "group": "home",
    "description": "<p>根据区块高度，区块hash，交易hash等查询信息</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n    \"cid\":\"\", // 链ID (必填)\n    \"parameter\":\"\"//块高，块hash，交易hash等\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"code\": 0,//成功（0），失败则由相关失败码\n   \"data\":{\n       \"type\":\"\",//区块block，交易transaction，节点node,合约contract,账户account,挂起交易pending\n        \"struct\":{\n   \t        \"height\": 17888,//块高\n               \"timestamp\": 1798798798798,//出块时间\n               \"transaction\": 10000,//块内交易数\n               \"size\": 188,//块大小\n               \"miner\": \"0x234\", // 出块节点\n               \"energonUsed\": 111,//能量消耗\n               \"energonAverage\": 11, //平均能量价值\n               \"blockReward\": \"123123\",//区块奖励\n               \"serverTime\": 1708098077  //服务器时间\n        }\n     }\n\n\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/SearchController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/app/statistic/init?cid=:chainId",
    "title": "e.出块时间及交易数据（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "statistic_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      },
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
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"code\": 0,//成功（0），失败则由相关失败码\n   \"data\": {\n   \t    \"avgTime\":365 ,//平均出块时长\n   \t    \"current\":333,//当前交易数量\n   \t    \"maxTps\":333, //最大交易TPS\n   \t    \"avgTransaction\":33, //平均区块交易数\n   \t    \"dayTransaction\":33, //过去24小时交易笔数\n   \t    \"graphData\": {\n \t        \"x\":[] ,//区块高度\n \t        \"ya\":[], //出块的时间\n \t        \"yb\":[]  //交易数量数量\n \t    }\n        }\n\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/topic/statistic/new?cid=:chainId",
    "title": "f.出块时间及交易数据（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "statistic_new",
    "group": "home",
    "description": "<p>增量数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      }
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
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/app/transaction/init?cid=:chainId",
    "title": "i.实时交易列表（websocket请求）初始数据",
    "version": "1.0.0",
    "name": "transaction_init",
    "group": "home",
    "description": "<p>初始数据</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      },
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
          "content": "HTTP/1.1 200 OK\n{\n   \"errMsg\": \"\",//描述信息\n   \"code\": 0,//成功（0），失败则由相关失败码\n   \"data\":[\n        {\n   \t    \"txHash\": \"x3222\",//交易Hash\n   \t    \"blockHeight\":5555, // 区块高度\n   \t    \"transactionIndex\": 33, // 交易在区块中位置\n   \t    \"from\":\"ddddd\",//交易发起方地址\n   \t    \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                            // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n           \"txType\": \"\", // 交易类型\n                      transfer ：转账\n                      MPCtransaction ： MPC交易\n                      contractCreate ： 合约创建\n                      vote ： 投票\n                      transactionExecute ： 合约执行\n                      authorization ： 权限\n                      candidateDeposit ： 竞选质押\n                      candidateApplyWithdraw ： 减持质押\n                      candidateWithdraw ： 提取质押\n                      unknown ： 未知\n   \t    \"value\": 3.6,//数额\n   \t    \"timestamp\"：155788,//交易时间\n   \t    \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n        }\n   ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "subscribe",
    "url": "/topic/transaction/new?cid=:chainId",
    "title": "j.实时交易列表（websocket请求）增量数据",
    "version": "1.0.0",
    "name": "transaction_new",
    "group": "home",
    "description": "<p>增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID.</p>"
          }
        ]
      }
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
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/HomeController.java",
    "groupTitle": "home"
  },
  {
    "type": "get",
    "url": "node/blockDownload?cid=:cid&nodeId=:nodeId&date=:date",
    "title": "e.导出节点区块详情",
    "version": "1.0.0",
    "name": "blockDownload",
    "group": "node",
    "description": "<p>导出节点区块详情</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "nodeId",
            "description": "<p>节点地址</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "date",
            "description": "<p>数据起始日期</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n响应为 二进制文件流",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/NodeController.java",
    "groupTitle": "node"
  },
  {
    "type": "post",
    "url": "node/blockList",
    "title": "d.节点区块列表(显示最新20条)",
    "version": "1.0.0",
    "name": "blockList",
    "group": "node",
    "description": "<p>节点区块列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"id\":\"\", // 数据库ID (必填)\n     \"beginNumber\":444, // 开始区块\n     \"endNumber\":555, // 结束区块\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\":[{\n         \"height\": 17888,//块高\n         \"timestamp\": 1798798798798,//出块时间\n         \"transaction\": 10000,//块内交易数\n         \"size\": 188,//块大小\n         \"miner\": \"0x234\", // 出块节点\n         \"energonUsed\": 111,//能量消耗\n         \"energonLimit\": 24234,//能量消耗限制\n         \"energonAverage\": 11, //平均能量价值(单位:Energon)\n         \"blockReward\": \"123123\",//区块奖励(单位:Energon)\n         \"serverTime\": 1708098077  //服务器时间\n      }]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/NodeController.java",
    "groupTitle": "node"
  },
  {
    "type": "post",
    "url": "node/detail",
    "title": "c.节点详情",
    "version": "1.0.0",
    "name": "detail",
    "group": "node",
    "description": "<p>节点详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"id\": \"0xsfjl34jfljsl435kd\", // 数据库ID (查看历史节点详情必须传id)\n     \"nodeId\": \"0xsfjl34jfljsl435kd\", // 节点ID (必选)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"id\": \"0xsfjl34jfljsl435kd\", // 库标识\n          \"nodeId\": \"0xsfjl34jfljsl435kd\", // 节点ID\n          \"address\": \"0xsfjl34jfljsl435kd\", // 节点地址\n          \"name\": \"node-1\",// 账户名称\n          \"logo\":\"\", // 节点LOGO，具体形式待定\n          \"electionStatus\": 1,// 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名\n          \"location\": \"中国广东深圳\",// 所属区域\n          \"joinTime\": 199880011,// 加入时间，单位-毫秒\n          \"deposit\": \"1.254555555\", // 质押金(单位:Energon)\n          \"rewardRatio\": 0.02,// 分红比例:小数\n          \"ranking\": 1,// 质押排名\n          \"profitAmount\": \"2.12425451222222\",// 累计收益(单位:Energon)\n          \"verifyCount\": 44554, // 节点验证次数\n          \"blockCount\": 252125,// 累计出块数\n          \"avgBlockTime\": 1.312, // 平均出块时长,单位-秒\n          \"rewardAmount\": \"0.12425451222222\",// 累计分红(单位:Energon)\n          \"nodeUrl\":\"http://mainnet.abc.cn:10332\", // 节点URL地址\n          \"publicKey\":\"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DE\", // 节点公钥\n          \"wallet\":\"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4\", // 节点钱包\n          \"intro\":\"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\", // 节点简介\n          \"orgName\":\"platon\", // 机构名称\n          \"orgWebsite\":\"https://www.platon.network\", // 机构官网\n          \"ticketCount\":\"\",//得票数\n          \"ticketEpoch\":555, // 票龄\n          \"beginNumber\":343, // 开始区块\n          \"endNumber\":555, // 结束区块\n          \"hitCount\":555 // 中选次数\n       }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/NodeController.java",
    "groupTitle": "node"
  },
  {
    "type": "post",
    "url": "node/historyList",
    "title": "b.历史节点列表",
    "version": "1.0.0",
    "name": "historyList",
    "group": "node",
    "description": "<p>节点列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"keyword\": \"node-1\"// 节点账户名称(可选)，用于节点列表的筛选\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": [\n          {\n          \"id\": \"0b9a39c791fdcbda987ff64717ef72f\", // 库标识\n          \"ranking\": 1,// 排名\n          \"logo\":\"\", // 节点LOGO，具体形式待定\n          \"name\": \"node-1\",// 账户名称\n          \"electionStatus\": 1,// 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名\n          \"countryCode\":\"CN\", // 国家代码\n          \"location\": \"中国广东深圳\",// 地理位置\n          \"deposit\": \"1.254555555\", // 质押金(单位:Energon)\n          \"blockCount\": 252125,// 产生的总区块数\n          \"rewardRatio\": 0.02,// 分红比例:小数\n          \"address\": \"0xsfjl34jfljsl435kd\", // 节点地址\n          \"eliminateTime\":\"\" // 淘汰时间 (毫秒)\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/NodeController.java",
    "groupTitle": "node"
  },
  {
    "type": "post",
    "url": "node/list",
    "title": "a.节点列表",
    "version": "1.0.0",
    "name": "list",
    "group": "node",
    "description": "<p>节点列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"keyword\": \"node-1\"// 节点账户名称(可选)，用于节点列表的筛选\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"voteCount\":90, // 投票数\n     \"proportion\":86,//占比: 小数\n     \"blockReward\":56.33,//每个区块奖励（单位Energon）\n     \"ticketPrice\":3.66,//票价（单位Energon）\n     \"selectedNodeCount\":33,//已选中节点数\n     \"totalNodeCount\":200,//总节点数\n     \"lowestDeposit\":545.44, // 最低质押（单位Energon）\n     \"highestDeposit\":545.44, // 最高质押（单位Energon）\n     \"data\": [\n          {\n          \"id\": \"0b9a39c791fdcbda987ff64717ef72f\", // 节点ID\n          \"ranking\": 1,// 排名\n          \"logo\":\"\", // 节点LOGO，具体形式待定\n          \"name\": \"node-1\",// 账户名称\n          \"electionStatus\": 1,// 竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名\n          \"countryCode\":\"CN\", // 国家代码\n          \"location\": \"中国广东深圳\",// 地理位置\n          \"deposit\": \"1.254555555\", // 质押金(单位:Energon)\n          \"blockCount\": 252125,// 产生的总区块数\n          \"rewardRatio\": 0.02,// 分红比例:小数\n          \"address\": \"0xsfjl34jfljsl435kd\", // 节点地址\n          \"ticketCount\":\"\",//得票数\n          \"nodeType\":\"\"//节点类型\n             candidates—候选节点\n             nominees—提名节点\n             validator—验证节点\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/NodeController.java",
    "groupTitle": "node"
  },
  {
    "type": "post",
    "url": "transaction/addressDetails",
    "title": "g.查询地址详情",
    "version": "1.0.0",
    "name": "addressDetails",
    "group": "transaction",
    "description": "<p>查询地址详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"address\": \"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4\",// 账户地址(必填)\n     \"txTypes\":['transfer','MPCtransaction'], // 交易类型, 字符串数组, 可取值如下所示：\n          transfer ：转账\n          MPCtransaction ： MPC交易\n          contractCreate ： 合约创建\n          voteTicket ： 投票\n          transactionExecute ： 合约执行\n          authorization ： 权限\n          candidateDeposit ： 竞选质押\n          candidateApplyWithdraw ： 减持质押\n          candidateWithdraw ： 提取质押\n          unknown ： 未知\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n        \"balance\":131,165,156.62618849461651616321, // 余额(单位:Energon)\n        \"tradeCount\":236, // 交易数\n        \"votePledge\":131,165,156.62618, // 投票质押\n        \"nodeCount\":3, // 投票节点数\n        \"trades\":[\n           {\n               \"txHash\": \"0x234234\",//交易hash\n               \"blockTime\": 18080899999,//确认时间(出块时间)\n               \"from\": \"0x667766\",//发送方\n               \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                                 // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n               \"value\": \"222\",//数额(单位:Energon)\n               \"actualTxCost\": \"22\",//交易费用(单位:Energon)\n               \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n               \"txType\": \"\", // 交易类型\n                   transfer ：转账\n                   MPCtransaction ： MPC交易\n                   contractCreate ： 合约创建\n                   voteTicket ： 投票\n                   transactionExecute ： 合约执行\n                   authorization ： 权限\n                   candidateDeposit ： 竞选质押\n                   candidateApplyWithdraw ： 减持质押\n                   candidateWithdraw ： 提取质押\n                   unknown ： 未知\n               \"txInfo\":\"\", // 交易参数\n               \"serverTime\": 1123123,//服务器时间\n               \"failReason\":\"\",//失败原因\n               \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n               \"nodeId\": \"\", // 节点ID\n               \"nodeName\",\"\"//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）\n               \"deposit\":\"\",//质押金 (竞选交易此字段才有值)\n               ---------以下字段只有txType=voteTicket有效----------\n               \"ticketPrice\":\"\",//票价\n               \"voteCount\":\"\",//投票数\n               \"validVoteCount\":33,//有效票\n               \"income\":\"\",//收益\n            }\n         ]\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "get",
    "url": "transaction/addressDownload?cid=:cid&address=:address&date=:date&tab=:tab",
    "title": "h.导出地址详情",
    "version": "1.0.0",
    "name": "addressDownload",
    "group": "transaction",
    "description": "<p>导出地址详情</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "address",
            "description": "<p>合约地址</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "date",
            "description": "<p>数据结束日期</p>"
          },
          {
            "group": "Parameter",
            "type": "Integer",
            "optional": false,
            "field": "tab",
            "description": "<p>标签页索引：0-交易，1-投票，2-声明</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n响应为 二进制文件流",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "get",
    "url": "transaction/blockTransaction",
    "title": "k.查询区块交易信息",
    "version": "1.0.0",
    "name": "blockTransaction",
    "group": "transaction",
    "description": "<p>查询区块交易信息</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"pageNo\": 1,//页数(必填)\n     \"pageSize\": 10,//页大小(必填),\n     \"height\": 123,//区块高度(必填)\n     \"txType\": \"\", // 交易类型\n                    trancaction ：交易\n                    voteTicket ： 投票\n                    candidateDeposit ： 竞选\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"totalCount\":18,//总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n          {\n          \"txHash\": \"0x234234\",//交易hash\n          \"blockHeight\": \"15566\",//交易所在区块高度\n          \"blockTime\": 18080899999,//出块时间\n          \"from\": \"0x667766\",//发送方\n          \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                           // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n          \"value\": \"222\",//数额(单位:Energon)\n          \"actualTxCost\": \"22\",//交易费用(单位:Energon)\n          \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n          \"txType\": \"\", // 交易类型\n                    transfer ：转账\n                    MPCtransaction ： MPC交易\n                    contractCreate ： 合约创建\n                    voteTicket ： 投票\n                    transactionExecute ： 合约执行\n                    authorization ： 权限\n          \"serverTime\": 1123123,//服务器时间\n          \"failReason\":\"\",//失败原因\n          \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n          \"txInfo\": \"{\n                 \"functionName\":\"\",//方法名称\n                 \"parameters\":{},//参数\n                 \"type\":\"1\"//交易类型\n                     0：转账\n                     1：合约发布\n                     2：合约调用\n                     4：权限\n                     5：MPC交易\n                     1000：投票\n                     1001：竞选质押\n                     1002：减持质押\n                     1003：提取质押\n                 }\"//返回交易解析结构\n          }\n          \"nodeName\",\"\"//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）\n\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "post",
    "url": "transaction/contractDetails",
    "title": "i.查询合约详情",
    "version": "1.0.0",
    "name": "contractDetails",
    "group": "transaction",
    "description": "<p>查询合约详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"address\": \"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4\",// 账户地址(必填)\n     \"txTypes\":[\"transfer\",\"MPCtransaction\"], // 交易类型, 字符串数组, 可取值如下所示：\n          voteTicket ： 投票\n          transactionExecute ： 合约执行\n          authorization ： 权限\n          candidateDeposit ： 竞选质押\n          candidateApplyWithdraw ： 减持质押\n          candidateWithdraw ： 提取质押\n          unknown ： 未知\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n         \"balance\":131,165,156.62618849461651616321, // 余额(单位:Energon)\n                \"tradeCount\":236, // 交易数\n                \"developer\":131,165,156.62618, // 合约开发者\n                \"ownerCount\":3, // 合约拥有者\n         \"trades\":[\n                {\n                \"txHash\": \"0x234234\",//交易hash\n                \"blockTime\": 18080899999,//确认时间(出块时间)\n                \"from\": \"0x667766\",//发送方\n                \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                                // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n                \"value\": \"222\",//数额(单位:Energon)\n                \"actualTxCost\": \"22\",//交易费用(单位:Energon)\n                \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n                \"txType\": \"\", // 交易类型\n                    transfer ：转账\n                    MPCtransaction ： MPC交易\n                    contractCreate ： 合约创建\n                    voteTicket ： 投票\n                    transactionExecute ： 合约执行\n                    authorization ： 权限\n                \"txInfo\":\"\", // 交易参数信息\n                \"serverTime\": 1123123,//服务器时间\n                \"failReason\":\"\",//失败原因\n                \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n                \"ticketPrice\": null, // 投票交易此字段才有值\n                \"income\": 0, // 投票交易此字段才有值\n                \"voteCount\": 0, // 投票交易此字段才有值\n                \"deposit\": 1200000, // 质押金，竞选交易此字段才有值\n                \"nodeId\": \"\", // 节点ID\n                \"nodeName\": \"Platon-Beijing\" // 节点名称\n                }\n            ]\n     }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "get",
    "url": "transaction/contractDownload?cid=:cid&address=:address&date=:date",
    "title": "j.导出合约详情",
    "version": "1.0.0",
    "name": "contractDownload",
    "group": "transaction",
    "description": "<p>导出合约详情</p>",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "cid",
            "description": "<p>链ID</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "address",
            "description": "<p>合约地址</p>"
          },
          {
            "group": "Parameter",
            "type": "String",
            "optional": false,
            "field": "date",
            "description": "<p>数据起始日期</p>"
          }
        ]
      }
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n响应为 二进制文件流",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "post",
    "url": "transaction/pendingDetails",
    "title": "e.待处理交易详情",
    "version": "1.0.0",
    "name": "pendingDetails",
    "group": "transaction",
    "description": "<p>待处理交易详情</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"txHash\": \"\",//交易Hash(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n {\n     \"errMsg\": \"成功\",\n     \"code\": 0,\n     \"data\": {\n         \"type\": \"pending\", // 结果类型:pending-待处理，transaction-已处理交易；\n                             // 原因：待处理交易可能随时在变化，在查询开始前可能此笔交易已被处理，所以这里需要添加结果类型作为区分;\n                             // 前端页面处理时，如果发现此值为transaction，则需要使用交易hash作为参数跳转到交易详情页面\n         \"pending\": { // 待处理交易数据，当type的值为transaction，此字段为空\n             \"txHash\": \"0x234234\",//交易hash\n             \"timestamp\": 123123123879,//交易接收时间\n             \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n             \"blockHeight\": \"15566\",//交易所在区块高度\n             \"from\": \"0x667766\",//发送者\n             \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                              // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n             \"txType\": \"\", // 交易类型\n                 transfer ：转账\n                 MPCtransaction ： MPC交易\n                 contractCreate ： 合约创建\n                 voteTicket ： 投票\n                 transactionExecute ： 合约执行\n                 authorization ： 权限\n                 authorization ： 权限\n                 candidateDeposit ： 竞选质押\n                 candidateApplyWithdraw ： 减持质押\n                 candidateWithdraw ： 提取质押\n                 unknown ： 未知\n             \"value\": \"222\",//数额(单位:Energon)\n             \"actualTxCost\": \"22\",//实际交易手续费(单位:Energon)\n             \"energonLimit\": 232,//能量限制\n             \"energonUsed\": 122,//能量消耗\n             \"priceInE\":\"1000000000000000000\", // 能量价格(单位:E)\n             \"priceInEnergon\":\"0.1\", // 能量价格(单位:Energon)\n             \"inputData\": \"\",//附加输入数据\n             \"expectTime\": 12312333, // 预计确认时间\n             \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n             \"txInfo\": \"{\n                    \"functionName\":\"\",//方法名称\n                    \"parameters\":{},//参数\n                    \"type\":\"1\"//交易类型\n                        0：转账\n                        1：合约发布\n                        2：合约调用\n                        4：权限\n                        5：MPC交易\n                        1000：投票\n                        1001：竞选质押\n                        1002：减持质押\n                        1003：提取质押\n                    }\"//返回交易解析结构\n             }\n             \"nodeName\",\"\"//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）\n             \"nodeId\",\"\"//节点Id（只有type=1000，1001，1002，1003时候，该字段才有值）\n             \"voteCount\",33// 投票数（只有投票交易此字段才有值）\n             \"deposit\":445,//质押金 (竞选交易此字段才有值)\n         }\n     }\n }",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "post",
    "url": "transaction/pendingList",
    "title": "d.待处理交易列表",
    "version": "1.0.0",
    "name": "pendingList",
    "group": "transaction",
    "description": "<p>待处理交易列表</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"pageNo\": 1,//页数(必填)\n     \"pageSize\": 10,//页大小(必填),\n     \"address\":\"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4\", // 地址(可选)，用于筛选功能\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"totalCount\":18,//总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n          {\n          \"txHash\": \"0x234234\",//交易hash\n          \"timestamp\": 33,// 交易接收时间\n          \"energonLimit\": 55555,//能量限制\n          \"priceInE\":\"1000000000000000000\", // 能量价格(单位:E)\n          \"priceInEnergon\":\"0.1\", // 能量价格(单位:Energon)\n          \"from\": \"0x667766\",//发送方\n          \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                           // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n          \"value\": \"222\",//数额(单位:Energon)\n          \"txType\": \"\", // 交易类型\n                 transfer ：转账\n                 MPCtransaction ： MPC交易\n                 contractCreate ： 合约创建\n                 voteTicket ： 投票\n                 transactionExecute ： 合约执行\n                 authorization ： 权限\n                 authorization ： 权限\n                 candidateDeposit ： 竞选质押\n                 candidateApplyWithdraw ： 减持质押\n                 candidateWithdraw ： 提取质押\n                 unknown ： 未知\n          \"serverTime\": 1123123,//服务器时间\n          \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n          \"txInfo\": \"{\n                 \"functionName\":\"\",//方法名称\n                 \"parameters\":{},//参数\n                 \"type\":\"1\"//交易类型\n                     0：转账\n                     1：合约发布\n                     2：合约调用\n                     4：权限\n                     5：MPC交易\n                     1000：投票\n                     1001：竞选质押\n                     1002：减持质押\n                     1003：提取质押\n                 }\"//返回交易解析结构\n          }\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  },
  {
    "type": "post",
    "url": "transaction/transactionDetailNavigate",
    "title": "c.交易详情前后跳转浏览",
    "version": "1.0.0",
    "name": "transactionDetailNavigate",
    "group": "transaction",
    "description": "<p>交易详情前后跳转浏览</p>",
    "parameter": {
      "examples": [
        {
          "title": "Request-Example:",
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"direction\":\"\", // 方向：prev-上一个，next-下一个 (必填)\n     \"txHash\": \"\",//交易Hash(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"txHash\": \"0x234234\",//交易hash\n          \"timestamp\": 123123123879,//交易接收时间\n          \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n          \"blockHeight\": \"15566\",//交易所在区块高度,\n          \"from\": \"0x667766\",//发送者\n          \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                           // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n          \"txType\": \"\", // 交易类型\n             transfer ：转账\n             MPCtransaction ： MPC交易\n             contractCreate ： 合约创建\n             voteTicket ： 投票\n             transactionExecute ： 合约执行\n             authorization ： 权限\n             authorization ： 权限\n             candidateDeposit ： 竞选质押\n             candidateApplyWithdraw ： 减持质押\n             candidateWithdraw ： 提取质押\n             unknown ： 未知\n          \"value\": \"222\",//数额(单位:Energon)\n          \"actualTxCost\": \"22\",//实际交易手续费(单位:Energon)\n          \"energonLimit\": 232,//能量限制\n          \"energonUsed\": 122,//能量消耗\n          \"priceInE\":\"1000000000000000000\", // 能量价格(单位:E)\n          \"priceInEnergon\":\"0.1\", // 能量价格(单位:Energon)\n          \"inputData\": \"\",//附加输入数据\n          \"expectTime\": 12312333, // 预计确认时间\n          \"first\":false, // 是否第一条记录\n          \"last\":true, // 是否最后一条记录\n          \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n          \"txInfo\": \"{\n                 \"functionName\":\"\",//方法名称\n                 \"parameters\":{},//参数\n                 \"type\":\"1\"//交易类型\n                     0：转账\n                     1：合约发布\n                     2：合约调用\n                     4：权限\n                     5：MPC交易\n                     1000：投票\n                     1001：竞选质押\n                     1002：减持质押\n                     1003：提取质押\n                 }\"//返回交易解析结构\n          }\n          \"nodeName\",\"\"//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）\n          \"nodeId\",\"\"//节点Id（只有type=1000，1001，1002，1003时候，该字段才有值）\n          \"voteCount\",33// 投票数（只有投票交易此字段才有值）\n          \"deposit\":445,//质押金 (竞选交易此字段才有值)\n    }\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
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
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"txHash\": \"\",//交易Hash(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"data\": {\n          \"txHash\": \"0x234234\",//交易hash\n          \"timestamp\": 123123123879,//交易时间\n          \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n          \"blockHeight\": \"15566\",//交易所在区块高度\n          \"confirmNum\":444, // 区块确认数\n          \"from\": \"0x667766\",//发送者\n          \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                       // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n          \"txType\": \"\", // 交易类型\n             transfer ：转账\n             MPCtransaction ： MPC交易\n             contractCreate ： 合约创建\n             voteTicket ： 投票\n             transactionExecute ： 合约执行\n             authorization ： 权限\n             candidateDeposit ： 竞选质押\n             candidateApplyWithdraw ： 减持质押\n             candidateWithdraw ： 提取质押\n             unknown ： 未知\n          \"value\": \"222\",//数额(单位:Energon)\n          \"actualTxCost\": \"22\",//实际交易手续费(单位:Energon)\n          \"energonLimit\": 232,//能量限制\n          \"energonUsed\": 122,//能量消耗\n          \"priceInE\":\"1000000000000000000\", // 能量价格(单位:E)\n          \"priceInEnergon\":\"0.1\", // 能量价格(单位:Energon)\n          \"inputData\": \"\",//附加输入数据\n          \"expectTime\": 12312333, // 预计确认时间\n          \"failReason\":\"\",//失败原因\n          \"first\":false, // 是否第一条记录\n          \"last\":true // 是否最后一条记录\n          \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址\n          \"txInfo\": \"{\n                 \"functionName\":\"\",//方法名称\n                 \"parameters\":{},//参数\n                 \"type\":\"1\"//交易类型\n                     0：转账\n                     1：合约发布\n                     2：合约调用\n                     4：权限\n                     5：MPC交易\n                     1000：投票\n                     1001：竞选质押\n                     1002：减持质押\n                     1003：提取质押\n                 }\"//返回交易解析结构\n          }\n          \"nodeName\",\"\"//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）\n          \"nodeId\",\"\"//节点Id（只有type=1000，1001，1002，1003时候，该字段才有值）\n          \"voteCount\",33// 投票数（只有投票交易此字段才有值）\n          \"deposit\":445,//质押金 (竞选交易此字段才有值)\n          \"ticketPrice\":3333 // 票价\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
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
          "content": "{\n     \"cid\":\"\", // 链ID (必填)\n     \"pageNo\": 1,//页数(必填)\n     \"pageSize\": 10,//页大小(必填)\n}",
          "type": "json"
        }
      ]
    },
    "success": {
      "examples": [
        {
          "title": "Success-Response:",
          "content": "HTTP/1.1 200 OK\n{\n     \"errMsg\": \"\",//描述信息\n     \"code\": 0,//成功（0），失败则由相关失败码\n     \"displayTotalCount\":18,//显示总数\n     \"totalCount\":18,// 小于等于500000记录的总数\n     \"totalPages\":1,//总页数\n     \"data\": [\n          {\n          \"txHash\": \"0x234234\",//交易hash\n          \"blockHeight\": \"15566\",//交易所在区块高度\n          \"blockTime\": 18080899999,//出块时间\n          \"from\": \"0x667766\",//发送方, 必定是钱包地址\n          \"to\": \"0x667766\",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：\n                           // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址\n          \"value\": \"222\",//数额(单位:Energon)\n          \"actualTxCost\": \"22\",//交易费用(单位:Energon)\n          \"txReceiptStatus\": 1,//交易状态 -1 pending 1 成功  0 失败\n          \"txType\": \"\", // 交易类型\n                  transfer ：转账\n                  MPCtransaction ： MPC交易\n                  contractCreate ： 合约创建\n                  voteTicket ： 投票\n                  transactionExecute ： 合约执行\n                  authorization ： 权限\n                  candidateDeposit ： 竞选质押\n                  candidateApplyWithdraw ： 减持质押\n                  candidateWithdraw ： 提取质押\n                  unknown ： 未知\n          \"txInfo\": \"{\n                 \"functionName\":\"\",//方法名称\n                 \"parameters\":{},//参数\n                 \"type\":\"1\"//交易类型\n                     0：转账\n                     1：合约发布\n                     2：合约调用\n                     4：权限\n                     5：MPC交易\n                     1000：投票\n                     1001：竞选质押\n                     1002：减持质押\n                     1003：提取质押\n                 }\"//返回交易解析结构\n          }\n          \"serverTime\": 1123123,//服务器时间\n          \"failReason\":\"\",//失败原因\n          \"receiveType\":\"account\" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，\n                                 // 前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情\n          }\n      ]\n}",
          "type": "json"
        }
      ]
    },
    "filename": "./browser-api/src/main/java/com/platon/browser/controller/TransactionController.java",
    "groupTitle": "transaction"
  }
] });
