-- =====================================================
-- 员工表添加密码字段 - 支持本地登录功能
-- 执行时间: 2026-01-29
-- =====================================================

-- 1. 添加 password 字段到员工表
ALTER TABLE `wk_hrm_employee` 
ADD COLUMN `password` VARCHAR(255) NULL COMMENT '登录密码(BCrypt加密)' AFTER `mobile`;

-- 2. 为现有员工设置默认密码 (123456 的 BCrypt 加密值)
-- BCrypt 加密后的 "123456"
UPDATE `wk_hrm_employee` 
SET `password` = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW'
WHERE `password` IS NULL;

-- 3. 验证更新结果
SELECT employee_id, employee_name, mobile, 
       CASE WHEN password IS NOT NULL THEN '已设置' ELSE '未设置' END AS password_status
FROM `wk_hrm_employee` 
WHERE is_del = 0
LIMIT 10;

-- 注意事项:
-- 1. 默认密码为 123456，建议用户首次登录后修改
-- 2. 密码使用 BCrypt 加密存储，安全性较高
-- 3. 如需重置某个用户密码，可执行:
--    UPDATE wk_hrm_employee SET password = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' WHERE employee_id = xxx;
