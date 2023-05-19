USE `scan_platon`;

DROP TABLE IF EXISTS `tx_transfer_bak`;
CREATE TABLE `tx_transfer_bak`
(
    `id`        BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `seq`       BIGINT(20) NOT NULL COMMENT '序号ID',
    `hash`      VARCHAR(72)  NOT NULL COMMENT '交易哈希',
    `from`      VARCHAR(42)  NOT NULL COMMENT 'from地址',
    `from_type` INT(1) NOT NULL COMMENT '发送方类型',
    `to`        VARCHAR(42)  NOT NULL COMMENT 'to地址',
    `to_type`   INT(1) NOT NULL COMMENT '接收方类型',
    `value`     VARCHAR(255) NOT NULL COMMENT '交易value',
    `bn`        BIGINT(20) DEFAULT NULL COMMENT '区块高度',
    `b_time`    DATETIME     DEFAULT NULL COMMENT '区块时间',
    PRIMARY KEY (`id`)
) COMMENT ='内部转账备份表';


ALTER TABLE `address`
    ADD COLUMN `transfer_tx_qty` INT(11) DEFAULT 0  NOT NULL   COMMENT '合约内部转账 对应的交易数' AFTER `erc20_tx_qty`;

INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`, `create_time`, `update_time`)
VALUES (11, 1, 'tx_transfer_bak', '从tx_transfer_bak备份表统计地址表', '0', '2021-12-10 02:44:32', '2021-12-10 02:44:32');

DROP TABLE IF EXISTS `token_tracker`;
CREATE TABLE `token_tracker` (
    `address` varchar(64) NOT NULL COMMENT '合约地址',
    `trigger` int(11) NOT NULL DEFAULT 0 COMMENT '出发检测时机 0-当交易中存在token相关事件时  1-立即',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`address`)
)  COMMENT ='token探测表';
