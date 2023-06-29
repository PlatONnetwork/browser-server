USE `scan_platon`;


-- 修改 address 表
ALTER TABLE `address`
    MODIFY COLUMN `address` char(42)  NOT NULL COMMENT '地址',
    MODIFY COLUMN `type` tinyint UNSIGNED NOT NULL COMMENT '地址类型 :1账号,2内置合约,3EVM合约,4WASM合约,5Erc20,6Erc721',
    MODIFY COLUMN `balance`               decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '余额(von)',
    MODIFY COLUMN `restricting_balance`   decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '锁仓余额(von)',
    MODIFY COLUMN `staking_value`         decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '质押的金额(von)',
    MODIFY COLUMN `delegate_value`        decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '委托的金额(von)',
    MODIFY COLUMN `redeemed_value`        decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '赎回中的质押金额(von)',
    MODIFY COLUMN `candidate_count`       int(11)        NOT NULL DEFAULT '0' COMMENT '已委托的验证人数',
    MODIFY COLUMN `delegate_hes`          decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '未锁定委托(von)',
    MODIFY COLUMN `delegate_locked`       decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '已锁定委托(von)',
    MODIFY COLUMN `delegate_released`     decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '待赎回委托金额(von,需要用户主动发起赎回交易)',
    MODIFY COLUMN `have_reward`           decimal(32, 0) NOT NULL DEFAULT '0' COMMENT '已领取委托奖励',
    MODIFY COLUMN `contract_create` varchar(42) DEFAULT '' COMMENT '合约创建者地址',
    MODIFY COLUMN `contract_createHash` varchar(66) DEFAULT '' COMMENT '创建合约的交易Hash',
    MODIFY COLUMN `contract_destroy_hash` varchar(66) DEFAULT '' COMMENT '销毁合约的交易Hash',
    ADD INDEX (`create_time`);

ALTER TABLE `block_node`
    MODIFY COLUMN `node_id` char(130) NOT NULL COMMENT '节点id';

ALTER TABLE `delegation`
    MODIFY COLUMN `delegate_addr`           char(42)        NOT NULL COMMENT '委托交易地址',
    MODIFY COLUMN `node_id`                 char(130)       NOT NULL COMMENT '节点id',
    MODIFY COLUMN `delegate_hes`            decimal(32, 0)  NOT NULL DEFAULT 0 COMMENT '未锁定委托金额(von)',
    MODIFY COLUMN `delegate_locked`         decimal(32, 0)  NOT NULL DEFAULT 0 COMMENT '已锁定委托金额(von)',
    MODIFY COLUMN `delegate_released`       decimal(32, 0)  NOT NULL DEFAULT 0 COMMENT '待提取的金额(von)',
    MODIFY COLUMN `is_history`              tinyint         NOT NULL DEFAULT 2 COMMENT '是否为历史:\r\n1是,\r\n2否';

ALTER TABLE `gas_estimate`
    COMMENT '委托用户未领取委托奖励持续的epoch数量',
    MODIFY COLUMN `addr`            varchar(42)     NOT NULL COMMENT '委托交易地址',
    MODIFY COLUMN `node_id`         char(130)       NOT NULL COMMENT '委托的质押节点id',
    MODIFY COLUMN `sbn`             bigint          NOT NULL COMMENT '委托的质押节点的质押块高',
    MODIFY COLUMN `epoch`           bigint          NOT NULL DEFAULT 0 COMMENT '委托未计算奖励的周期数',
    ADD COLUMN `node_id_hash_code`  int             COMMENT '委托的质押节点id的hashCode',
    ADD INDEX (`node_id_hash_code`,`sbn`);

ALTER TABLE `n_opt_bak`
    MODIFY COLUMN `node_id`     char(130)       NOT NULL COMMENT '节点id',
    MODIFY COLUMN `tx_hash`     char(66)        DEFAULT NULL COMMENT '交易hash',
    MODIFY COLUMN `desc`        varchar(1024)   DEFAULT NULL COMMENT '操作描述';

ALTER TABLE `network_stat`
    MODIFY COLUMN `cur_number`               bigint         NOT NULL COMMENT '当前块号',
    MODIFY COLUMN `cur_block_hash`           char(66)       NOT NULL DEFAULT '' COMMENT '当前区块Hash',
    MODIFY COLUMN `node_id`                  char(130)      NOT NULL COMMENT '节点ID',
    MODIFY COLUMN `node_name`                varchar(256)   NOT NULL,
    MODIFY COLUMN `tx_qty`                   int            NOT NULL DEFAULT 0 COMMENT '交易总数',
    MODIFY COLUMN `cur_tps`                  int            NOT NULL DEFAULT 0 COMMENT '当前交易TPS',
    MODIFY COLUMN `max_tps`                  int            NOT NULL DEFAULT 0 COMMENT '最大交易TPS',
    MODIFY COLUMN `issue_value`              decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前发行量(von)',
    MODIFY COLUMN `turn_value`               decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前流通量(von)',
    MODIFY COLUMN `available_staking`        decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '可用总质押量(von)',
    MODIFY COLUMN `staking_delegation_value` decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '实时质押委托总数(von)',
    MODIFY COLUMN `staking_value`            decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '实时质押总数(von)',
    MODIFY COLUMN `doing_proposal_qty`       int            NOT NULL DEFAULT 0 COMMENT '进行中提案总数',
    MODIFY COLUMN `proposal_qty`             int            NOT NULL DEFAULT 0 COMMENT '提案总数',
    MODIFY COLUMN `address_qty`              int(10) unsigned NOT NULL DEFAULT 0 COMMENT '地址数',
    MODIFY COLUMN `block_reward`             decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前出块奖励(von)',
    MODIFY COLUMN `staking_reward`           decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前质押奖励(von)',
    MODIFY COLUMN `settle_staking_reward`    decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前结算周期总质押奖励',
    MODIFY COLUMN `add_issue_begin`          bigint       NOT NULL COMMENT '当前增发周期的开始块号',
    MODIFY COLUMN `add_issue_end`            bigint       NOT NULL COMMENT '当前增发周期的结束块号',
    MODIFY COLUMN `next_settle`              bigint       NOT NULL COMMENT '离下个结算周期的剩余块数',
    MODIFY COLUMN `node_opt_seq`             bigint       NOT NULL COMMENT '节点操作记录最新序号',
    MODIFY COLUMN `issue_rates`              varchar(20)  COMMENT '增发比例',
    MODIFY COLUMN `avg_pack_time`            bigint       NOT NULL DEFAULT 0 COMMENT '平均区块打包时间',
    MODIFY COLUMN `erc1155_tx_qty`           int          NOT NULL DEFAULT 0 COMMENT 'erc1155 token对应的交易数',
    MODIFY COLUMN `erc721_tx_qty`            int          NOT NULL DEFAULT 0 COMMENT 'erc721 token对应的交易数',
    MODIFY COLUMN `erc20_tx_qty`             int          NOT NULL DEFAULT 0 COMMENT 'erc20 token对应的交易数',
    MODIFY COLUMN `year_num`                 int          DEFAULT 0 COMMENT '第几年';

-- 修改 node 表字段类型，更合适
ALTER TABLE `node`
    MODIFY COLUMN `node_id`                      char(130)        NOT NULL COMMENT '节点id',
    MODIFY COLUMN `stat_slash_multi_qty`         int              NOT NULL DEFAULT 0 COMMENT '多签举报次数',
    MODIFY COLUMN `stat_slash_low_qty`           int              NOT NULL DEFAULT 0 COMMENT '出块率低举报次数',
    MODIFY COLUMN `stat_block_qty`               bigint           NOT NULL DEFAULT 0 COMMENT '节点处块数统计',
    MODIFY COLUMN `stat_expect_block_qty`        bigint           NOT NULL DEFAULT 0 COMMENT '节点期望出块数',
    MODIFY COLUMN `stat_verifier_time`           int              NOT NULL DEFAULT 0 COMMENT '进入共识验证轮次数',
    MODIFY COLUMN `is_recommend`                 int              NOT NULL DEFAULT 0 COMMENT '官方推荐 :1是\r\n2:否',
    MODIFY COLUMN `total_value`                  decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '有效的质押委托总数(von)',
    MODIFY COLUMN `staking_block_num`            bigint           NOT NULL COMMENT '质押时的区块号',
    MODIFY COLUMN `staking_tx_index`             int              NOT NULL COMMENT '发起质押交易的索引',
    MODIFY COLUMN `staking_hes`                  decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '犹豫期的质押金(von)',
    MODIFY COLUMN `staking_locked`               decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '锁定期的质押金(von)',
    MODIFY COLUMN `staking_reduction`            decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '退回中的质押金(von)',
    MODIFY COLUMN `staking_reduction_epoch`      int              NOT NULL DEFAULT 0 COMMENT '结算周期标识',
    MODIFY COLUMN `node_name`                    varchar(256)     NOT NULL,
    MODIFY COLUMN `node_icon`                    varchar(255)     DEFAULT '' COMMENT '节点头像(关联external_id,第三方软件获取)',
    MODIFY COLUMN `external_id`                  varchar(255)     NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id',
    MODIFY COLUMN `external_name`                varchar(128)     COMMENT '第三方社交软件关联用户名',
    MODIFY COLUMN `staking_addr`                 char(42)         NOT NULL COMMENT '发起质押的账户地址',
    MODIFY COLUMN `benefit_addr`                 char(42)         NOT NULL COMMENT '收益地址',
    MODIFY COLUMN `annualized_rate`              decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '预计年化率',
    MODIFY COLUMN `program_version`              int              NOT NULL DEFAULT 0 COMMENT '程序版本',
    MODIFY COLUMN `big_version`                  int              NOT NULL DEFAULT 0 COMMENT '大程序版本',
    MODIFY COLUMN `web_site`                     varchar(255)     COMMENT '节点的第三方主页',
    MODIFY COLUMN `details`                      varchar(256)     COMMENT '节点描述',
    MODIFY COLUMN `join_time`                    timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    MODIFY COLUMN `leave_time`                   datetime         COMMENT '离开时间',
    MODIFY COLUMN `status`                       tinyint          NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定',
    MODIFY COLUMN `is_consensus`                 tinyint          NOT NULL DEFAULT 2 COMMENT '是否共识周期验证人:1是,2否',
    MODIFY COLUMN `is_settle`                    tinyint          NOT NULL DEFAULT 2 COMMENT '是否结算周期验证人:1是,2否',
    MODIFY COLUMN `is_init`                      tinyint          NOT NULL DEFAULT 2 COMMENT '是否为链初始化时内置的候选人:1是,2否',
    MODIFY COLUMN `stat_delegate_value`          decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '有效的委托金额(von)',
    MODIFY COLUMN `stat_delegate_released`       decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '待提取的委托金额(von)',
    MODIFY COLUMN `stat_valid_addrs`             int              NOT NULL DEFAULT 0 COMMENT '有效委托地址数',
    MODIFY COLUMN `stat_invalid_addrs`           int              NOT NULL DEFAULT 0 COMMENT '待提取委托地址数',
    MODIFY COLUMN `stat_block_reward_value`      decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '出块奖励统计(激励池)(von)',
    MODIFY COLUMN `stat_staking_reward_value`    decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '质押奖励统计(激励池)(von)',
    MODIFY COLUMN `stat_fee_reward_value`        decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '出块奖励统计(手续费)(von)',
    MODIFY COLUMN `predict_staking_reward`       decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前结算周期预计可获得的质押奖励',
    MODIFY COLUMN `annualized_rate_info`         varchar(2048)    COMMENT '最近几个结算周期收益和质押信息',
    MODIFY COLUMN `reward_per`                   int              NOT NULL DEFAULT 0 COMMENT '委托奖励比例',
    MODIFY COLUMN `next_reward_per`              int              NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例',
    MODIFY COLUMN `next_reward_per_mod_epoch`    int              DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期',
    MODIFY COLUMN `have_dele_reward`             decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '所有质押已领取委托奖励',
    MODIFY COLUMN `pre_dele_annualized_rate`     decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '前一参与周期预计委托收益率',
    MODIFY COLUMN `dele_annualized_rate`         decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '预计委托收益率',
    MODIFY COLUMN `total_dele_reward`            decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前质押总的委托奖励',
    MODIFY COLUMN `pre_total_dele_reward`        decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '所有历史质押记录总的委托奖励累计字段(在质押退出时会把total_dele_reward累加到此字段)',
    MODIFY COLUMN `exception_status`             tinyint          NOT NULL DEFAULT 1 COMMENT '1正常,2低出块异常,3被双签,4因低出块被惩罚(例如在连续两个周期当选验证人，但在第一个周期出块率低),5因双签被惩罚',
    MODIFY COLUMN `un_stake_freeze_duration`     int              NOT NULL COMMENT '解质押理论上锁定的结算周期数',
    MODIFY COLUMN `un_stake_end_block`           bigint           COMMENT '解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者',
    MODIFY COLUMN `zero_produce_freeze_duration` int              COMMENT '零出块节点锁定结算周期数',
    MODIFY COLUMN `zero_produce_freeze_epoch`    int              COMMENT '零出块锁定时所在结算周期',
    MODIFY COLUMN `low_rate_slash_count`         int              NOT NULL DEFAULT 0 COMMENT '节点零出块次数',
    MODIFY COLUMN `node_settle_statis_info`      varchar(1024)    COMMENT '节点结算周期的出块统计信息',
    DROP INDEX `node_id`;

ALTER TABLE `proposal`
    MODIFY COLUMN `hash`                char(66)    NOT NULL COMMENT '提案交易hash',
    MODIFY COLUMN `yeas`                int         NOT NULL DEFAULT 0 COMMENT '赞成票',
    MODIFY COLUMN `nays`                int         NOT NULL DEFAULT 0 COMMENT '反对票',
    MODIFY COLUMN `abstentions`         int         NOT NULL DEFAULT 0 COMMENT '弃权票',
    MODIFY COLUMN `accu_verifiers`      int         NOT NULL DEFAULT 0 COMMENT '在整个投票期内有投票资格的验证人总数',
    MODIFY COLUMN `status`              tinyint     NOT NULL DEFAULT 1 COMMENT '提案状态:1投票中,2通过,3失败,4预升级,5生效,6被取消',
    MODIFY COLUMN `completion_flag`     tinyint     NOT NULL DEFAULT 2 COMMENT '提案相关数据是否补充完成标识:1是,2否';

ALTER TABLE `rp_plan`
    MODIFY COLUMN `id`          bigint          NOT NULL AUTO_INCREMENT COMMENT 'id',
    MODIFY COLUMN `address`     char(42)        NOT NULL COMMENT '发布锁仓计划地址',
    MODIFY COLUMN `epoch`       int             NOT NULL COMMENT '锁仓计划周期',
    MODIFY COLUMN `amount`      decimal(32, 0)  NOT NULL COMMENT '区块上待释放的金额(von)',
    MODIFY COLUMN `number`      bigint          NOT NULL COMMENT '锁仓计划所在区块',
    RENAME INDEX `number索引` TO `number`;

ALTER TABLE `slash`
    MODIFY COLUMN `slash_data`                   longtext         NOT NULL COMMENT '举报证据',
    MODIFY COLUMN `node_id`                      char(130)        NOT NULL COMMENT '节点Id',
    MODIFY COLUMN `tx_hash`                      char(66)         NOT NULL COMMENT '交易hash',
    MODIFY COLUMN `time`                         datetime         NOT NULL COMMENT '时间',
    MODIFY COLUMN `setting_epoch`                int              NOT NULL COMMENT '通过（block_number/每个结算周期出块数）向上取整',
    MODIFY COLUMN `staking_block_num`            bigint           NOT NULL COMMENT '质押交易所在块高',
    MODIFY COLUMN `slash_rate`                   decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '双签惩罚比例，万分之x，则存x',
    MODIFY COLUMN `slash_report_rate`            decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '惩罚金分配给举报人比例，百分之x，则存x',
    MODIFY COLUMN `benefit_address`              char(42)         NOT NULL COMMENT '交易发送者',
    MODIFY COLUMN `code_remain_redeem_amount`    decimal(32, 0)   NOT NULL DEFAULT 0.00 COMMENT '双签惩罚后剩下的质押金额，因为双签处罚后节点就被置为退出中，所有金额都会移动到待赎回字段中',
    MODIFY COLUMN `code_reward_value`            decimal(32, 0)   NOT NULL DEFAULT 0.00 COMMENT '奖励的金额',
    MODIFY COLUMN `code_status`                  tinyint          DEFAULT NULL COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定',
    MODIFY COLUMN `code_staking_reduction_epoch` int              DEFAULT NULL COMMENT '当前退出中',
    MODIFY COLUMN `code_slash_value`             decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '惩罚的金额',
    MODIFY COLUMN `un_stake_freeze_duration`     int              NOT NULL COMMENT '解质押需要经过的结算周期数',
    MODIFY COLUMN `un_stake_end_block`           bigint           NOT NULL COMMENT '解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者',
    MODIFY COLUMN `block_num`                    bigint           NOT NULL COMMENT '双签的区块',
    MODIFY COLUMN `is_quit`                      tinyint          NOT NULL DEFAULT 1 COMMENT '是否退出:1是,2否',
    MODIFY COLUMN `is_handle`                    tinyint          NOT NULL COMMENT '是否已处理，1-是，0-否';

ALTER TABLE `staking`
    MODIFY COLUMN `node_id`                      char(130)        NOT NULL COMMENT '质押节点地址',
    MODIFY COLUMN `staking_block_num`            bigint           NOT NULL COMMENT '质押区块高度',
    MODIFY COLUMN `staking_tx_index`             int              NOT NULL COMMENT '发起质押交易的索引',
    MODIFY COLUMN `staking_hes`                  decimal(32, 0)   NOT NULL DEFAULT '0' COMMENT '犹豫期的质押金(von)',
    MODIFY COLUMN `staking_locked`               decimal(32, 0)   NOT NULL DEFAULT '0' COMMENT '锁定期的质押金(von)',
    MODIFY COLUMN `staking_reduction`            decimal(32, 0)   NOT NULL DEFAULT '0' COMMENT '退回中的质押金(von)',
    MODIFY COLUMN `staking_reduction_epoch`      int              NOT NULL DEFAULT '0' COMMENT '撤销质押时的结算周期轮数',
    MODIFY COLUMN `node_name`                    varchar(256)     NOT NULL,
    MODIFY COLUMN `node_icon`                    varchar(255)     COMMENT '节点头像(关联external_id，第三方软件获取)',
    MODIFY COLUMN `external_id`                  varchar(255)     NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id',
    MODIFY COLUMN `external_name`                varchar(128)     COMMENT '第三方社交软件关联用户名',
    MODIFY COLUMN `staking_addr`                 char(42)         NOT NULL COMMENT '发起质押的账户地址',
    MODIFY COLUMN `benefit_addr`                 char(42)         NOT NULL DEFAULT '' COMMENT '收益地址',
    MODIFY COLUMN `annualized_rate`              decimal(10, 2)    NOT NULL DEFAULT 0.00 COMMENT '预计年化率,百分之x,则存x',
    MODIFY COLUMN `program_version`              varchar(10)      NOT NULL DEFAULT '0' COMMENT '程序版本',
    MODIFY COLUMN `big_version`                  varchar(10)      NOT NULL DEFAULT '0' COMMENT '大程序版本',
    MODIFY COLUMN `web_site`                     varchar(255)     NOT NULL DEFAULT '' COMMENT '节点的第三方主页',
    MODIFY COLUMN `details`                      varchar(256)     NOT NULL,
    MODIFY COLUMN `join_time`                    timestamp        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    MODIFY COLUMN `leave_time`                   datetime         COMMENT '离开时间',
    MODIFY COLUMN `status`                       tinyint          NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定',
    MODIFY COLUMN `is_consensus`                 tinyint          NOT NULL DEFAULT 2 COMMENT '是否共识周期验证人:1是,2否',
    MODIFY COLUMN `is_settle`                    tinyint          NOT NULL DEFAULT 2 COMMENT '是否结算周期验证人:1是,2否',
    MODIFY COLUMN `is_init`                      tinyint          NOT NULL DEFAULT 2 COMMENT '是否为链初始化时内置的候选人:1是,2否',
    MODIFY COLUMN `stat_delegate_hes`            decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '未锁定的委托(von)',
    MODIFY COLUMN `stat_delegate_locked`         decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '锁定的委托(von)',
    MODIFY COLUMN `stat_delegate_released`       decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '待提取的委托(von)',
    MODIFY COLUMN `block_reward_value`           decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '出块奖励统计(激励池)(von)',
    MODIFY COLUMN `predict_staking_reward`       decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '当前结算周期预计可获得的质押奖励',
    MODIFY COLUMN `staking_reward_value`         decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '质押奖励(激励池)(von)',
    MODIFY COLUMN `fee_reward_value`             decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '出块奖励统计(手续费)(von)',
    MODIFY COLUMN `cur_cons_block_qty`           int              NOT NULL DEFAULT 0 COMMENT '当前共识周期出块数',
    MODIFY COLUMN `pre_cons_block_qty`           int              NOT NULL DEFAULT 0 COMMENT '上个共识周期出块数',
    MODIFY COLUMN `annualized_rate_info`         varchar(2048)    COMMENT '最近几个结算周期收益和质押信息',
    MODIFY COLUMN `reward_per`                   int              NOT NULL COMMENT '委托奖励比例',
    MODIFY COLUMN `next_reward_per`              int              NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例',
    MODIFY COLUMN `next_reward_per_mod_epoch`    int              DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期',
    MODIFY COLUMN `have_dele_reward`             decimal(32, 0)   NOT NULL DEFAULT 0 COMMENT '节点当前质押已领取委托奖励',
    MODIFY COLUMN `pre_dele_annualized_rate`     decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '前一参与周期预计委托收益率',
    MODIFY COLUMN `dele_annualized_rate`         decimal(10, 2)   NOT NULL DEFAULT 0.00 COMMENT '预计委托收益率',
    MODIFY COLUMN `total_dele_reward`            decimal(32, 0)   DEFAULT 0 COMMENT '节点当前质押总的委托奖励',
    MODIFY COLUMN `exception_status`             tinyint          NOT NULL DEFAULT 1 COMMENT '1正常,2低出块异常,3被双签,4因低出块被惩罚(例如在连续两个周期当选验证人，但在第一个周期出块率低),5因双签被惩罚',
    MODIFY COLUMN `un_stake_freeze_duration`     int              NOT NULL COMMENT '解质押理论上锁定的结算周期数',
    MODIFY COLUMN `un_stake_end_block`           bigint           DEFAULT NULL COMMENT '解质押冻结的最后一个区块：理论结束块与投票结束块中的最大者',
    MODIFY COLUMN `zero_produce_freeze_duration` int              DEFAULT NULL COMMENT '零出块节点锁定结算周期数',
    MODIFY COLUMN `zero_produce_freeze_epoch`    int              DEFAULT NULL COMMENT '零出块锁定时所在结算周期',
    MODIFY COLUMN `low_rate_slash_count`         int              NOT NULL DEFAULT 0 COMMENT '节点零出块次数';

ALTER TABLE `staking_history`
    MODIFY COLUMN `node_id`                   char(130)           NOT NULL COMMENT '质押节点地址',
    MODIFY COLUMN `staking_block_num`         bigint              NOT NULL COMMENT '质押区块高度',
    MODIFY COLUMN `staking_tx_index`          int                 NOT NULL COMMENT '发起质押交易的索引',
    MODIFY COLUMN `staking_hes`               decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '犹豫期的质押金(von)',
    MODIFY COLUMN `staking_locked`            decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '锁定期的质押金(von)',
    MODIFY COLUMN `staking_reduction`         decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '退回中的质押金(von)',
    MODIFY COLUMN `staking_reduction_epoch`   int                 NOT NULL DEFAULT 0 COMMENT '结算周期标识',
    MODIFY COLUMN `node_name`                 varchar(256)        NOT NULL,
    MODIFY COLUMN `node_icon`                 varchar(255)        DEFAULT '' COMMENT '节点头像(关联external_id，第三方软件获取)',
    MODIFY COLUMN `external_id`               varchar(255)        NOT NULL DEFAULT '' COMMENT '第三方社交软件关联id',
    MODIFY COLUMN `external_name`             varchar(128)        DEFAULT NULL COMMENT '第三方社交软件关联用户名',
    MODIFY COLUMN `staking_addr`              char(42)            NOT NULL COMMENT '发起质押的账户地址',
    MODIFY COLUMN `benefit_addr`              char(42)            NOT NULL DEFAULT '' COMMENT '收益地址',
    MODIFY COLUMN `annualized_rate`           decimal(10, 2)      NOT NULL DEFAULT 0.00 COMMENT '预计年化率',
    MODIFY COLUMN `program_version`           varchar(10)         NOT NULL DEFAULT '0' COMMENT '程序版本',
    MODIFY COLUMN `big_version`               varchar(10)         NOT NULL DEFAULT '0' COMMENT '大程序版本',
    MODIFY COLUMN `web_site`                  varchar(255)        NOT NULL DEFAULT '' COMMENT '节点的第三方主页',
    MODIFY COLUMN `details`                   varchar(256)        NOT NULL,
    MODIFY COLUMN `join_time`                 timestamp           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    MODIFY COLUMN `leave_time`                datetime            COMMENT '离开时间',
    MODIFY COLUMN `status`                    tinyint             NOT NULL DEFAULT 1 COMMENT '节点状态:1候选中,2退出中,3已退出,4已锁定',
    MODIFY COLUMN `is_consensus`              tinyint             NOT NULL DEFAULT 2 COMMENT '是否共识周期验证人:1是,2否',
    MODIFY COLUMN `is_settle`                 tinyint             NOT NULL DEFAULT 2 COMMENT '是否结算周期验证人:1是,2否',
    MODIFY COLUMN `is_init`                   tinyint             NOT NULL DEFAULT 2 COMMENT '是否为链初始化时内置的候选人:1是,2否',
    MODIFY COLUMN `stat_delegate_hes`         decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '未锁定的委托(von)',
    MODIFY COLUMN `stat_delegate_locked`      decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '锁定的委托(von)',
    MODIFY COLUMN `stat_delegate_released`    decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '待提取的委托(von)',
    MODIFY COLUMN `block_reward_value`        decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '出块奖励统计(激励池)(von)',
    MODIFY COLUMN `fee_reward_value`          decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '出块奖励统计(手续费)(von)',
    MODIFY COLUMN `staking_reward_value`      decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '质押奖励(激励池)(von)',
    MODIFY COLUMN `predict_staking_reward`    decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '当前结算周期预计可获得的质押奖励',
    MODIFY COLUMN `cur_cons_block_qty`        int                 NOT NULL DEFAULT 0 COMMENT '当前共识周期出块数',
    MODIFY COLUMN `pre_cons_block_qty`        int                 NOT NULL DEFAULT 0 COMMENT '上个共识周期出块数',
    MODIFY COLUMN `annualized_rate_info`      varchar(2048)       COMMENT '最近几个结算周期收益和质押信息',
    MODIFY COLUMN `reward_per`                int                 NOT NULL DEFAULT 0 COMMENT '委托奖励比例',
    MODIFY COLUMN `next_reward_per`           int                 NOT NULL DEFAULT 0 COMMENT '下一结算周期委托奖励比例',
    MODIFY COLUMN `next_reward_per_mod_epoch` int                 DEFAULT 0 COMMENT '【下一结算周期委托奖励比例】修改所在结算周期',
    MODIFY COLUMN `have_dele_reward`          decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '已领取委托奖励(初始值等于前一条历史质押记录的【已领取委托奖励】)',
    MODIFY COLUMN `pre_dele_annualized_rate`  decimal(10, 2)      DEFAULT 0.00 COMMENT '前一参与周期预计委托收益率',
    MODIFY COLUMN `dele_annualized_rate`      decimal(10, 2)      NOT NULL DEFAULT 0.00 COMMENT '预计委托收益率',
    MODIFY COLUMN `total_dele_reward`         decimal(32, 0)      NOT NULL DEFAULT 0 COMMENT '节点总的委托奖励(前一条历史质押记录的【节点总的委托奖励】+ 当前质押实时查询出来的奖励)';


ALTER TABLE `token`
    MODIFY COLUMN `address`                       char(42)        NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `type`                          varchar(64)     NOT NULL COMMENT '合约类型 erc20 erc721',
    MODIFY COLUMN `name`                          varchar(64)     DEFAULT NULL COMMENT '合约名称',
    MODIFY COLUMN `symbol`                        varchar(64)     DEFAULT NULL COMMENT '合约符号',
    MODIFY COLUMN `total_supply`                  varchar(128)    DEFAULT NULL COMMENT '总供应量',
    MODIFY COLUMN `decimal`                       int             DEFAULT NULL COMMENT '合约精度',
    MODIFY COLUMN `is_support_erc165`             tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持erc165接口： 0-不支持 1-支持',
    MODIFY COLUMN `is_support_erc20`              tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持erc20接口： 0-不支持 1-支持',
    MODIFY COLUMN `is_support_erc721`             tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持erc721接口： 0-不支持 1-支持',
    MODIFY COLUMN `is_support_erc721_enumeration` tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持erc721 enumeration接口： 0-不支持 1-支持',
    MODIFY COLUMN `is_support_erc721_metadata`    tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持metadata接口： 0-不支持 1-支持',
    MODIFY COLUMN `is_support_erc1155`            tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持erc1155接口： 0-不支持 1-支持',
    MODIFY COLUMN `is_support_erc1155_metadata`   tinyint         NOT NULL DEFAULT 0 COMMENT '是否支持erc1155 metadata接口： 0-不支持 1-支持',
    MODIFY COLUMN `token_tx_qty`                  int             NOT NULL DEFAULT 0 COMMENT 'token对应的交易数',
    MODIFY COLUMN `holder`                        int             NOT NULL DEFAULT 0 COMMENT 'token对应的持有人的数量',
    MODIFY COLUMN `contract_destroy_block`        bigint          DEFAULT 0 COMMENT '合约的销毁块高',
    MODIFY COLUMN `contract_destroy_update`       tinyint         NOT NULL DEFAULT 0 COMMENT '销毁的合约是否已更新，1为是，0为否，默认是0',
    ADD COLUMN `created_block_number`             bigint          DEFAULT 0 COMMENT '合约创建块高' AFTER `contract_destroy_update`;

ALTER TABLE `token_holder`
    MODIFY COLUMN `token_address` char(42)    NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `address`       char(42)    NOT NULL COMMENT '用户地址',
    MODIFY COLUMN `balance`       decimal(64, 0) DEFAULT 0 COMMENT '地址代币余额, ERC20为金额，ERC721为tokenId数',
    MODIFY COLUMN `token_tx_qty`  int         NOT NULL DEFAULT 0 COMMENT 'erc721 token对应的交易数';


ALTER TABLE `tx_bak`
    MODIFY COLUMN `hash`             char(66)         NOT NULL COMMENT '交易hash',
    MODIFY COLUMN `b_hash`           char(66)         DEFAULT NULL COMMENT '区块hash',
    MODIFY COLUMN `num`              bigint           DEFAULT NULL COMMENT '块高',
    MODIFY COLUMN `index`            int              DEFAULT NULL COMMENT '交易index',
    MODIFY COLUMN `time`             timestamp        NULL DEFAULT NULL COMMENT '交易时间',
    MODIFY COLUMN `nonce`            int              DEFAULT NULL COMMENT '随机值',
    MODIFY COLUMN `status`           tinyint          DEFAULT NULL COMMENT '状态,1.成功,2.失败',
    MODIFY COLUMN `gas_price`        bigint           DEFAULT 0 COMMENT 'gas价格',
    MODIFY COLUMN `gas_used`         bigint           DEFAULT 0 COMMENT 'gas花费',
    MODIFY COLUMN `gas_limit`        bigint           DEFAULT 0 COMMENT 'gas限制',
    MODIFY COLUMN `from`             char(42)         DEFAULT NULL COMMENT 'from地址',
    MODIFY COLUMN `to`               char(42)         DEFAULT NULL COMMENT 'to地址',
    MODIFY COLUMN `value`            decimal(32, 0)   DEFAULT NULL COMMENT '转账金额',
    MODIFY COLUMN `type`             SMALLINT         DEFAULT NULL COMMENT '交易类型',
    MODIFY COLUMN `cost`             decimal(32, 0)   DEFAULT NULL COMMENT '成本',
    MODIFY COLUMN `to_type`          SMALLINT         DEFAULT NULL COMMENT 'to地址类型(1EOA,2内置合约,3EVM合约,4WASM合约,5ERC20EVM合约,6ERC721EVM合约,7ERC1155EVM合约)',
    MODIFY COLUMN `seq`              bigint           DEFAULT NULL COMMENT 'seq=num*100000+index',
    MODIFY COLUMN `input`            longtext,
    MODIFY COLUMN `info`             longtext,
    MODIFY COLUMN `erc1155_tx_info`  longtext         COMMENT 'erc1155交易列表信息',
    MODIFY COLUMN `erc721_tx_info`   longtext         COMMENT 'erc721交易列表信息',
    MODIFY COLUMN `erc20_tx_info`    longtext         COMMENT 'erc20交易列表信息',
    MODIFY COLUMN `transfer_tx_info` longtext         COMMENT '内部转账交易列表信息',
    MODIFY COLUMN `ppos_tx_info`     longtext         COMMENT 'ppos调用交易列表信息',
    MODIFY COLUMN `fail_reason`      longtext,
    MODIFY COLUMN `contract_type`    tinyint          DEFAULT NULL COMMENT '合约类型(0:INNER;1:EVM;2:WASM;3:UNKNOWN;4:ERC20_EVM;5:ERC721_EVM;6:ERC1155_EVM',
    MODIFY COLUMN `method`           varchar(64),
    MODIFY COLUMN `contract_address` char(42)         DEFAULT NULL COMMENT '合约地址';


ALTER TABLE `token_inventory`
    MODIFY COLUMN `token_address` char(42) NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `owner` char(42)  NOT NULL COMMENT 'token id 对应持有者地址',
    MODIFY COLUMN `description` varchar(512) COMMENT 'Describes the asset to which this NFT represents',
    MODIFY COLUMN `token_url` varchar(2048) COMMENT 'url',
    MODIFY COLUMN `retry_num` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '重试次数',
    ALTER COLUMN `token_tx_qty` SET DEFAULT 0,
    ALTER COLUMN `token_owner_tx_qty` SET DEFAULT 0,
    ADD INDEX (`token_url`(1)),
    ADD INDEX (`image`(1)),
    ADD INDEX (`retry_num`);

ALTER TABLE `tx_erc_20_bak`
    MODIFY COLUMN `seq`       bigint          NOT NULL COMMENT '序号ID',
    MODIFY COLUMN `name`      varchar(64)     NOT NULL COMMENT '合约名称',
    MODIFY COLUMN `symbol`    varchar(64)     DEFAULT NULL COMMENT '单位',
    MODIFY COLUMN `decimal`   int             DEFAULT NULL COMMENT '精度',
    MODIFY COLUMN `contract`  char(42)        NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `hash`      char(66)        NOT NULL COMMENT '交易哈希',
    MODIFY COLUMN `from`      char(42)        NOT NULL COMMENT 'from地址',
    MODIFY COLUMN `from_type` tinyint         NOT NULL DEFAULT 0 COMMENT '发送方类型',
    MODIFY COLUMN `to`        char(42)        NOT NULL COMMENT 'to地址',
    MODIFY COLUMN `to_type`   tinyint         NOT NULL DEFAULT 0 COMMENT '接收方类型',
    MODIFY COLUMN `value`     decimal(64, 0)  NOT NULL COMMENT '交易value',
    MODIFY COLUMN `bn`        bigint          DEFAULT NULL COMMENT '区块高度',
    MODIFY COLUMN `b_time`    datetime        DEFAULT NULL COMMENT '区块时间',
    MODIFY COLUMN `tx_fee`    decimal(32, 0)  DEFAULT NULL COMMENT '手续费',
    MODIFY COLUMN `remark`    varchar(256) COMMENT '备注';

ALTER TABLE `tx_erc_721_bak`
    MODIFY COLUMN `seq`       bigint          NOT NULL COMMENT '序号ID',
    MODIFY COLUMN `name`      varchar(64)     DEFAULT NULL COMMENT '合约名称',
    MODIFY COLUMN `symbol`    varchar(64)     DEFAULT NULL COMMENT '单位',
    MODIFY COLUMN `decimal`   int(20)         DEFAULT NULL COMMENT '精度',
    MODIFY COLUMN `contract`  char(42)        NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `token_id`  varchar(255)    NOT NULL COMMENT 'tokenId',
    MODIFY COLUMN `hash`      char(66)        NOT NULL COMMENT '交易哈希',
    MODIFY COLUMN `from`      char(42)        NOT NULL COMMENT 'from地址',
    MODIFY COLUMN `from_type` tinyint         NOT NULL DEFAULT 0 COMMENT '发送方类型',
    MODIFY COLUMN `to`        char(42)        NOT NULL COMMENT 'to地址',
    MODIFY COLUMN `to_type`   tinyint         NOT NULL DEFAULT 0 COMMENT '接收方类型',
    MODIFY COLUMN `value`     decimal(64, 0)  NOT NULL COMMENT '交易value',
    MODIFY COLUMN `bn`        bigint          DEFAULT NULL COMMENT '区块高度',
    MODIFY COLUMN `b_time`    datetime        DEFAULT NULL COMMENT '区块时间',
    MODIFY COLUMN `tx_fee`    decimal(32, 0)  DEFAULT NULL COMMENT '手续费',
    MODIFY COLUMN `remark`    varchar(256)    COMMENT '备注';

ALTER TABLE `tx_delegation_reward_bak`
    MODIFY COLUMN `hash` char(66) NOT NULL COMMENT '交易哈希',
    MODIFY COLUMN `addr` char(42) DEFAULT NULL COMMENT '地址';


ALTER TABLE `vote`
    MODIFY COLUMN `hash` char(66) NOT NULL COMMENT '投票交易Hash(如果此值带有"-",则表示投票操作是通过普通合约代理执行的,"-"号前面的是合约交易hash)',
    MODIFY COLUMN `node_id` char(130) NOT NULL COMMENT '投票验证人(节点ID)',
    MODIFY COLUMN `proposal_hash` char(66) NOT NULL COMMENT '提案交易Hash';


ALTER TABLE `internal_address`
    MODIFY COLUMN `address`             char(42)        NOT NULL COMMENT '地址',
    MODIFY COLUMN `type`                tinyint         NOT NULL DEFAULT 0 COMMENT '地址类型 :0-基金会账户  1-锁仓合约地址  2-质押合约  3-激励池合约  6-委托奖励池合约 ',
    MODIFY COLUMN `balance`             decimal(32, 0)  NOT NULL DEFAULT 0 COMMENT '余额(von)',
    MODIFY COLUMN `restricting_balance` decimal(32, 0)  NOT NULL DEFAULT 0 COMMENT '锁仓余额(von)',
    ALTER COLUMN `is_show` SET DEFAULT 1,
    ALTER COLUMN `is_calculate` SET DEFAULT 1,
    ALTER COLUMN `create_id` SET DEFAULT 1,
    ALTER COLUMN `update_id` SET DEFAULT 1;


ALTER TABLE `token_expand`
    MODIFY COLUMN `address` char(42) NOT NULL COMMENT '合约地址',
    ALTER COLUMN `is_show_in_aton_admin` SET DEFAULT 0,
    ALTER COLUMN `is_show_in_scan_admin` SET DEFAULT 0;


ALTER TABLE `token_1155_holder`
    MODIFY COLUMN `token_address`   char(42)        NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `address`         char(42)        NOT NULL COMMENT '用户地址',
    MODIFY COLUMN `balance`         decimal(64, 0)  DEFAULT 0 COMMENT '地址代币余额，job更新';


-- token_url, image, 都只需要一个字符作为前缀索引即可，能标识is null就可以满足查询需求
ALTER TABLE `token_1155_inventory`
    MODIFY COLUMN `token_address` char(42)  NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `token_url` varchar(2048) COMMENT 'url',
    MODIFY COLUMN `description` varchar(512) COMMENT 'Describes the asset to which this NFT represents',
    MODIFY COLUMN `retry_num` tinyint unsigned NOT NULL DEFAULT 0 COMMENT '重试次数',
    ALTER COLUMN `token_tx_qty` SET DEFAULT 0,
    ADD INDEX (`token_url`(1)),
    ADD INDEX (`image`(1)),
    ADD INDEX (`retry_num`);


ALTER TABLE `tx_erc_1155_bak`
    MODIFY COLUMN `contract`    char(42) NOT NULL COMMENT '合约地址',
    MODIFY COLUMN `hash`        char(66) NOT NULL COMMENT '交易哈希',
    MODIFY COLUMN `from`        char(42) NOT NULL COMMENT 'from地址',
    MODIFY COLUMN `from_type`   tinyint NOT NULL DEFAULT 0 COMMENT '发送方类型',
    MODIFY COLUMN `to`          char(42) NOT NULL COMMENT 'to地址',
    MODIFY COLUMN `to_type`     tinyint NOT NULL DEFAULT 0 COMMENT '接收方类型',
    MODIFY COLUMN `value`       decimal(64, 0) NOT NULL COMMENT '交易value',
    MODIFY COLUMN `tx_fee`      decimal(32, 0) DEFAULT 0 COMMENT '手续费';

-- 把node_settle_statis_info多余的参数node_id去掉
UPDATE node set `node_settle_statis_info` = CONCAT(SUBSTRING_INDEX(node_settle_statis_info, ',"nodeId":"', 1), "}")  WHERE `node_settle_statis_info` REGEXP ',"nodeId":".+"';

-- token增加字段created_block_number后，需要补上数据
UPDATE token
    INNER JOIN (
        select t.address, tx_bak.num
        from tx_bak
                 inner join (
            select token.address, address.contract_createHash
            from address
                     inner join token on address.address = token.address
        ) t on tx_bak.hash = t.contract_createHash
    ) tt ON token.address = tt.address
SET token.created_block_number = tt.num;

-- tx_bak记录增加后，修改address的交易数量
DROP TRIGGER IF EXISTS trigger_tx_bak_insert;
DELIMITER ||
CREATE TRIGGER trigger_tx_bak_insert AFTER INSERT
    ON tx_bak FOR EACH ROW
BEGIN
    update `address`
    set
        tx_qty = tx_qty + 1,
        transfer_qty = case when NEW.`type` = 0 then transfer_qty + 1 else transfer_qty end,
        staking_qty = case when NEW.`type` in (13,14,15,16,26) then staking_qty + 1 else staking_qty end,
        delegate_qty = case when NEW.`type` in (17,19,28) then delegate_qty + 1 else delegate_qty end,
        proposal_qty = case when NEW.`type` in (20,21,22,23,24,25) then proposal_qty + 1 else proposal_qty end
    where `address` = NEW.`from` OR address = NEW.`to`;
END||
DELIMITER ;

-- tx_erc_20_bak记录增加后，修改address的erc_20交易数量
DROP TRIGGER IF EXISTS trigger_tx_erc_20_bak_insert;
DELIMITER ||
CREATE TRIGGER trigger_tx_erc_20_bak_insert AFTER INSERT
    ON tx_erc_20_bak FOR EACH ROW
BEGIN
    update `token`
    set token_tx_qty = token_tx_qty + 1
    where `address` = NEW.`contract`;

    update `address`
    set erc20_tx_qty = erc20_tx_qty + 1
    where `address` = NEW.`from` OR address = NEW.`to`;

END||
DELIMITER ;


-- tx_erc_721_bak记录增加后，修改address的erc_20交易数量
DROP TRIGGER IF EXISTS trigger_tx_erc_721_bak_insert;
DELIMITER ||
CREATE TRIGGER trigger_tx_erc_721_bak_insert AFTER INSERT
    ON tx_erc_721_bak FOR EACH ROW
BEGIN
    update `token`
    set token_tx_qty = token_tx_qty + 1
    where `address` = NEW.`contract`;

    update `address`
    set erc721_tx_qty = erc721_tx_qty + 1
    where `address` = NEW.`from` OR address = NEW.`to`;

END||
DELIMITER ;


-- tx_erc_1155_bak记录增加后，修改address的erc_20交易数量
DROP TRIGGER IF EXISTS trigger_tx_erc_1155_bak_insert;
DELIMITER ||
CREATE TRIGGER trigger_tx_erc_1155_bak_insert AFTER INSERT
    ON tx_erc_1155_bak FOR EACH ROW
BEGIN
    update `token`
    set token_tx_qty = token_tx_qty + 1
    where `address` = NEW.`contract`;

    update `address`
    set erc1155_tx_qty = erc1155_tx_qty + 1
    where `address` = NEW.`from` OR address = NEW.`to`;

END||
DELIMITER ;
