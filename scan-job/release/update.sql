CREATE TABLE IF NOT EXISTS `internal_address` (
    `address` VARCHAR(42) NOT NULL COMMENT '地址',
    `type` INT(11) NOT NULL DEFAULT '0' COMMENT '地址类型 :0-基金会账户  1-锁仓合约地址  2-质押合约  3-激励池合约  6-委托奖励池合约 ',
    `balance` DECIMAL(65,0) NOT NULL DEFAULT '0' COMMENT '余额(von)',
    `restricting_balance` DECIMAL(65,0) NOT NULL DEFAULT '0' COMMENT '锁仓余额(von)',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`address`),
    KEY `type` (`type`) USING BTREE
);

-- 初始化数据
INSERT INTO `internal_address` (`address`,`type`)
VALUES ('atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqp8h9fxw', 1),
       ('atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzfyslg3', 2),
       ('atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqr5jy24r', 3),
       ('atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqxxwje8t', 6);


-- 激励池合约地址: atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqr5jy24r
-- 委托奖励合约地址: atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqxxwje8t
-- 质押合约地址: atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqzfyslg3
-- 锁仓合约地址: atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqp8h9fxw