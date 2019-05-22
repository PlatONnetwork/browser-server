CREATE TABLE `pending_tx` (
      `hash` varchar(128) NOT NULL DEFAULT '' COMMENT '交易hash',
      `from` varchar(64) NOT NULL COMMENT '交易发起方地址',
      `to` varchar(64) NOT NULL DEFAULT '0x0000000000000000000000000000000000000000' COMMENT '交易接收方地址',
      `input` text NOT NULL COMMENT '交易输入数据',
      `energon_used` varchar(255) DEFAULT '0' COMMENT '能量消耗',
      `energon_limit` varchar(255) NOT NULL COMMENT '能量限制',
      `energon_price` varchar(255) NOT NULL COMMENT '能量限制',
      `timestamp` timestamp NOT NULL COMMENT '交易时间（单位：秒）',
      `chain_id` varchar(64) NOT NULL DEFAULT '' COMMENT '链id',
      `tx_type` varchar(32) NOT NULL COMMENT '交易类型\r\ntransfer ：转账\r\nMPCtransaction ： MPC交易\r\ncontractCreate ： 合约创建\r\nvote ： 投票\r\ntransactionExecute ： 合约执行\r\nauthorization ： 权限',
      `value` varchar(64) NOT NULL COMMENT '交易金额',
      `receive_type` varchar(32) NOT NULL DEFAULT '' COMMENT '交易接收者类型（to是合约还是账户）contract合约、 account账户',
      `tx_info` text NOT NULL COMMENT '交易信息',
      `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`hash`)
) ;
