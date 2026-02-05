-- =====================================================
-- 修复 MySQL ONLY_FULL_GROUP_BY 模式问题
-- 适用于 MySQL 5.7+ 和 MySQL 8.0+
-- =====================================================

-- 1. 查看当前的 sql_mode
SELECT @@sql_mode;

-- 2. 设置当前会话的 sql_mode（临时生效，仅当前连接）
SET sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));

-- 3. 设置全局 sql_mode（永久生效，需要重启 MySQL 客户端）
SET GLOBAL sql_mode=(SELECT REPLACE(@@GLOBAL.sql_mode,'ONLY_FULL_GROUP_BY',''));

-- 4. 验证修改是否生效
SELECT @@GLOBAL.sql_mode;
SELECT @@SESSION.sql_mode;

-- =====================================================
-- 说明：
-- 执行步骤 3 后，需要重启你的应用程序
-- 或者重新连接数据库以使更改生效
-- =====================================================
