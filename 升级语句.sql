ALTER TABLE TRANSACTION ADD col1 VARCHAR(255) NULL;
ALTER TABLE TRANSACTION ADD col2 VARCHAR(255) NULL;
ALTER TABLE TRANSACTION ADD col3 VARCHAR(255) NULL;
ALTER TABLE TRANSACTION ADD col4 VARCHAR(255) NULL;
ALTER TABLE TRANSACTION ADD col5 VARCHAR(255) NULL;

ALTER TABLE `transaction` MODIFY tx_type VARCHAR(32) NOT NULL COMMENT '交易类型 transfer ：转账 MPCtransaction ： MPC交易 contractCreate ： 合约创建 voteTicket ： 投票-使用col1-col5存储投票参数信息，参数含义与tx_info的json字段顺序一致 transactionExecute ： 合约执行 authorization ： 权限 candidateDeposit：竞选质押 candidateApplyWithdraw：减持质押 candidateWithdraw：提取质押 unknown：未知';
