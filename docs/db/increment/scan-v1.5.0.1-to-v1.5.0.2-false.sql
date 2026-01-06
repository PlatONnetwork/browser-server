-- ============================================
-- 版本升级脚本: v1.5.0.1 -> v1.5.0.2
-- 创建日期: 2026-01-06
-- 说明: SQL1优化方案 - 地址表查询优化（游标分页+索引优化）
-- 是否需要清库: false
-- ============================================

USE `scan_platon`;

-- 1. 为address表添加复合索引，优化基于create_time和address的游标分页查询
-- 索引说明：create_time作为主排序字段，address作为辅助排序字段，确保排序的唯一性和稳定性
-- 注意：如果索引已存在，此语句会报错，可忽略或先手动删除索引
CREATE INDEX `idx_create_time_address` ON `address`(`create_time`, `address`);

-- 2. 为地址更新任务添加point_log记录（用于存储游标信息）
-- 游标格式：createTime|address（例如：2024-01-01 00:00:00|lat1ka8ksfzknx888pzx2y3lqkuucj66cgrsr7der8）
-- 初始值：0|0（表示从头开始）
INSERT INTO `point_log`(`id`, `type`, `name`, `desc`, `position`, `create_time`, `update_time`) 
VALUES (13, 1, 'address', '地址表信息补充任务的游标记录', '0|0', NOW(), NOW());

