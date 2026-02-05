-- 为超级管理员分配完整的HRM权限（包括所有子模块）
-- 角色ID: 1559019593879896074 (超级管理员)
-- 用户ID: 1559019593470803969 (本地测试用户)

-- 1. 删除旧的HRM相关权限
DELETE FROM wk_admin_role_menu 
WHERE role_id = 1559019593879896074 
  AND menu_id IN (
    SELECT menu_id FROM wk_admin_menu 
    WHERE menu_id = 5 OR parent_id = 5 OR parent_id IN (SELECT menu_id FROM wk_admin_menu WHERE parent_id = 5)
  );

-- 2. 插入HRM模块的所有权限（包括三级菜单）
INSERT INTO wk_admin_role_menu (id, role_id, menu_id, create_time, create_user_id, update_time, update_user_id)
SELECT
    1559019593879896000 + menu_id AS id,
    1559019593879896074 AS role_id,
    menu_id,
    NOW() AS create_time,
    1559019593470803969 AS create_user_id,
    NOW() AS update_time,
    1559019593470803969 AS update_user_id
FROM wk_admin_menu
WHERE 
    menu_id = 5                                                                     -- HRM模块本身
    OR parent_id = 5                                                                -- HRM的直接子模块（员工管理、组织管理等）
    OR parent_id IN (SELECT menu_id FROM wk_admin_menu WHERE parent_id = 5)        -- 子模块的子菜单（如组织管理下的新建、编辑等）
    OR menu_id = 3                                                                  -- 管理后台模块
    OR parent_id = 3                                                                -- 管理后台的子模块
    OR menu_id = 900                                                                -- 人力资源管理
ORDER BY menu_id;

-- 3. 验证插入结果
SELECT '=== 超级管理员角色信息 ===' AS info;
SELECT role_id, role_name, role_type FROM wk_admin_role WHERE role_id = 1559019593879896074;

SELECT '=== 用户-角色关联 ===' AS info;
SELECT user_id, role_id FROM wk_admin_user_role WHERE user_id = 1559019593470803969;

SELECT '=== HRM权限菜单数量 ===' AS info;
SELECT COUNT(*) AS total_hrm_permissions 
FROM wk_admin_role_menu rm
WHERE rm.role_id = 1559019593879896074
  AND rm.menu_id IN (
    SELECT menu_id FROM wk_admin_menu 
    WHERE menu_id = 5 OR parent_id = 5 OR parent_id IN (SELECT menu_id FROM wk_admin_menu WHERE parent_id = 5)
  );

SELECT '=== 组织管理权限详情 ===' AS info;
SELECT m.menu_id, m.parent_id, m.menu_name, m.menu_type, m.realm
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
  AND (m.menu_id = 830 OR m.parent_id = 830)
ORDER BY m.menu_id;

SELECT '=== 所有HRM相关权限 ===' AS info;
SELECT m.menu_id, m.parent_id, m.menu_name, m.menu_type, m.realm
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
  AND m.menu_id IN (
    SELECT menu_id FROM wk_admin_menu 
    WHERE menu_id = 5 OR parent_id = 5 OR parent_id IN (SELECT menu_id FROM wk_admin_menu WHERE parent_id = 5)
  )
ORDER BY m.menu_id
LIMIT 50;
