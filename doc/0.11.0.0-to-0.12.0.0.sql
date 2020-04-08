ALTER TABLE `node`
    CHANGE `un_stake_freeze_duration` `un_stake_freeze_duration` int(11)   NOT NULL COMMENT '解质押理论上锁定的结算周期数' after `exception_status` ,
    ADD COLUMN `un_stake_end_block` bigint(20)   NULL COMMENT '解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者' after `un_stake_freeze_duration`;

ALTER TABLE `staking`
    CHANGE `un_stake_freeze_duration` `un_stake_freeze_duration` int(11)   NOT NULL COMMENT '解质押理论上锁定的结算周期数' after `exception_status` ,
    ADD COLUMN `un_stake_end_block` bigint(20)   NULL COMMENT '解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者' after `un_stake_freeze_duration`;