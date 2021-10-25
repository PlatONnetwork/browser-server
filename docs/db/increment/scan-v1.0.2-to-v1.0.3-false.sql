USE `scan_platon`;
ALTER TABLE `internal_address`
  ADD COLUMN `name` VARCHAR(64) DEFAULT '基金会地址'  NOT NULL   COMMENT '地址名称' FIRST,
  ADD COLUMN `is_show` TINYINT(1) DEFAULT 1  NOT NULL   COMMENT '是否用于展示  0-否 1-是' AFTER `restricting_balance`,
  ADD COLUMN `is_calculate` TINYINT(1) DEFAULT 1  NOT NULL   COMMENT '是否用于计算  0-否 1-是' AFTER `is_show`,
  ADD COLUMN `create_id` BIGINT(20) DEFAULT 1  NOT NULL   COMMENT '创建者' AFTER `is_calculate`,
  ADD COLUMN `create_name` VARCHAR(64) DEFAULT 'admin'  NOT NULL   COMMENT '创建者名称' AFTER `create_id`,
  ADD COLUMN `update_id` BIGINT(20) DEFAULT 1  NOT NULL   COMMENT '更新者' AFTER `create_time`,
  ADD COLUMN `update_name` VARCHAR(64) DEFAULT 'admin'  NOT NULL   COMMENT '更新者名称' AFTER `update_id`;