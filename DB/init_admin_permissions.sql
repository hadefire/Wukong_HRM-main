-- =====================================================
-- 为超级管理员角色添加完整的菜单权限
-- 超级管理员应该拥有所有模块的访问权限
-- =====================================================

-- 删除可能存在的旧权限（如果有的话）
DELETE FROM `wk_admin_role_menu` WHERE role_id = 1559019593879896074;

-- 为超级管理员添加所有 HRM 相关菜单权限
-- 注意：这里使用子查询从 wk_admin_menu 表中获取所有菜单ID

-- 方案1：为超级管理员添加所有 HRM 模块的菜单权限
-- 使用一个较小的基数来生成ID，避免超出BIGINT范围
INSERT INTO `wk_admin_role_menu` (id, role_id, menu_id, create_time, create_user_id, update_time, update_user_id)
SELECT 
    1559019593879896000 + menu_id AS id,                         -- 生成唯一ID（基数 + menu_id）
    1559019593879896074 AS role_id,                              -- 超级管理员角色ID
    menu_id,                                                      -- 菜单ID
    NOW() AS create_time,
    1559019593470803969 AS create_user_id,                       -- 创建人ID
    NOW() AS update_time,
    1559019593470803969 AS update_user_id                        -- 更新人ID
FROM wk_admin_menu
WHERE parent_id = 5                                               -- HRM 模块(menu_id=5)的直接子菜单
   OR menu_id = 5                                                 -- HRM 模块本身
   OR parent_id IN (SELECT menu_id FROM wk_admin_menu WHERE parent_id = 5)  -- HRM 子菜单的子菜单（三级菜单）
   OR menu_id = 3                                                 -- 管理模块
   OR parent_id = 3                                               -- 管理模块的子菜单
   OR parent_id = 160                                             -- 企业首页的子菜单
   OR parent_id = 900                                             -- 人力资源管理的子菜单
ORDER BY menu_id;

-- 验证权限是否添加成功
SELECT '=======================' AS separator;
SELECT '超级管理员角色的菜单权限数量:' AS info;
SELECT COUNT(*) AS permission_count 
FROM wk_admin_role_menu 
WHERE role_id = 1559019593879896074;

SELECT '=======================' AS separator;
SELECT '部分菜单权限详情 (前20条):' AS info;
SELECT 
    rm.id,
    rm.role_id,
    rm.menu_id,
    m.menu_name,
    m.realm
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
ORDER BY rm.menu_id
LIMIT 20;

SELECT '=======================' AS separator;
SELECT '组织管理相关权限:' AS info;
SELECT 
    rm.id,
    rm.role_id,
    rm.menu_id,
    m.menu_name,
    m.realm
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
  AND m.menu_name LIKE '%组织%'
ORDER BY rm.menu_id;
