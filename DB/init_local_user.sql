-- =====================================================
-- 本地开发模式初始化数据
-- 用于配合 wukong.auth.local 本地认证
-- =====================================================

-- 1. 插入测试部门（如果不存在）
INSERT IGNORE INTO `wk_hrm_dept` (`dept_id`, `parent_id`, `dept_type`, `name`, `code`, `main_employee_id`, `leader_employee_id`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) 
VALUES (1, 0, 1, '本地测试企业', 'LOCAL-001', NULL, NULL, 1, NOW(), NULL, NOW());

-- 2. 插入本地测试员工
-- employee_id 与 application.yml 中的 wukong.auth.local.user-id 保持一致
-- mobile 与 application.yml 中的 wukong.auth.local.mobile 保持一致
-- 默认密码: 123456 (BCrypt加密)
INSERT INTO `wk_hrm_employee` (
    `employee_id`,
    `employee_name`,
    `mobile`,
    `password`,
    `sex`,
    `email`,
    `entry_time`,
    `become_time`,
    `job_number`,
    `dept_id`,
    `post`,
    `status`,
    `entry_status`,
    `employment_forms`,
    `is_del`,
    `create_user_id`,
    `create_time`
) VALUES (
    1559019593470803969,       -- employee_id (与 application.yml 中 user-id 一致)
    '本地管理员',               -- employee_name
    '13800000000',             -- mobile (与 application.yml 中 mobile 一致)
    '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', -- password (默认: 123456)
    1,                          -- sex (1=男)
    'local-admin@example.com',  -- email
    NOW(),                      -- entry_time
    NOW(),                      -- become_time
    'LOCAL-001',                -- job_number
    1,                          -- dept_id (对应上面创建的部门)
    '系统管理员',               -- post
    1,                          -- status (1=正式)
    1,                          -- entry_status (1=在职)
    1,                          -- employment_forms (1=正式)
    0,                          -- is_del (0=未删除)
    1,                          -- create_user_id
    NOW()                       -- create_time
) ON DUPLICATE KEY UPDATE
    employee_name = VALUES(employee_name),
    mobile = VALUES(mobile),
    password = VALUES(password),
    update_time = NOW();

-- 3. 插入超级管理员角色（如果不存在）
INSERT IGNORE INTO `wk_admin_role` (
    `role_id`,
    `role_name`,
    `role_type`,
    `sorting`,
    `remark`,
    `status`,
    `data_type`,
    `is_hidden`,
    `label`,
    `create_time`,
    `create_user_id`,
    `update_time`,
    `update_user_id`
) VALUES (
    1559019593879896074,     -- role_id (超级管理员角色ID)
    '超级管理员',             -- role_name
    1,                        -- role_type (1=管理角色)
    0,                        -- sorting
    'admin',                  -- remark
    1,                        -- status (1=启用)
    5,                        -- data_type (5=全部数据权限)
    1,                        -- is_hidden (1=不隐藏)
    5,                        -- label
    NOW(),                    -- create_time
    1559019593470803969,      -- create_user_id
    NOW(),                    -- update_time
    1559019593470803969       -- update_user_id
);

-- 4. 插入 HRM 管理员角色（如果不存在）
-- role_type = 3 表示人事角色
INSERT IGNORE INTO `wk_admin_role` (
    `role_id`,
    `role_name`,
    `role_type`,
    `sorting`,
    `remark`,
    `status`,
    `data_type`,
    `is_hidden`,
    `label`,
    `create_time`,
    `create_user_id`,
    `update_time`,
    `update_user_id`
) VALUES (
    1559019593879896076,     -- role_id (HRM管理员角色ID)
    'HRM管理员',              -- role_name
    3,                        -- role_type (3=人事角色)
    1,                        -- sorting
    '人力资源管理员',          -- remark
    1,                        -- status (1=启用)
    5,                        -- data_type (5=全部数据权限)
    1,                        -- is_hidden (1=不隐藏)
    91,                       -- label (91=HRM管理员)
    NOW(),                    -- create_time
    1559019593470803969,      -- create_user_id
    NOW(),                    -- update_time
    1559019593470803969       -- update_user_id
);

-- 5. 建立员工与超级管理员角色的关联
INSERT IGNORE INTO `wk_admin_user_role` (
    `id`,
    `user_id`,
    `role_id`,
    `create_time`,
    `create_user_id`,
    `update_time`,
    `update_user_id`
) VALUES (
    1559019593879896075,      -- id (关联记录ID)
    1559019593470803969,      -- user_id (对应 employee_id)
    1559019593879896074,      -- role_id (超级管理员角色ID)
    NOW(),                    -- create_time
    1559019593470803969,      -- create_user_id
    NOW(),                    -- update_time
    1559019593470803969       -- update_user_id
);

-- 6. 建立员工与 HRM 管理员角色的关联
INSERT IGNORE INTO `wk_admin_user_role` (
    `id`,
    `user_id`,
    `role_id`,
    `create_time`,
    `create_user_id`,
    `update_time`,
    `update_user_id`
) VALUES (
    1559019593879896077,      -- id (关联记录ID)
    1559019593470803969,      -- user_id (对应 employee_id)
    1559019593879896076,      -- role_id (HRM管理员角色ID)
    NOW(),                    -- create_time
    1559019593470803969,      -- create_user_id
    NOW(),                    -- update_time
    1559019593470803969       -- update_user_id
);

-- =====================================================
-- 验证数据
-- =====================================================

SELECT '=======================' AS separator;
SELECT '部门数据:' AS info;
SELECT * FROM wk_hrm_dept WHERE dept_id = 1;

SELECT '=======================' AS separator;
SELECT '员工数据:' AS info;
SELECT employee_id, employee_name, mobile, dept_id, post, status FROM wk_hrm_employee WHERE mobile = '13800000000';

SELECT '=======================' AS separator;
SELECT '角色数据 (管理角色):' AS info;
SELECT role_id, role_name, role_type, data_type, label FROM wk_admin_role WHERE role_type IN (1, 3);

SELECT '=======================' AS separator;
SELECT '用户角色关联:' AS info;
SELECT ur.id, ur.user_id, ur.role_id, r.role_name, r.role_type,
       CASE r.role_type WHEN 1 THEN '系统管理' WHEN 3 THEN '人事管理' ELSE '其他' END AS role_type_desc
FROM wk_admin_user_role ur 
LEFT JOIN wk_admin_role r ON ur.role_id = r.role_id 
WHERE ur.user_id = 1559019593470803969;
