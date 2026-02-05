-- =====================================================
-- 检查超级管理员的权限配置
-- =====================================================

-- 1. 检查超级管理员角色是否存在
SELECT '=======================' AS separator;
SELECT '检查超级管理员角色:' AS info;
SELECT * FROM wk_admin_role WHERE role_id = 1559019593879896074;

-- 2. 检查用户角色关联
SELECT '=======================' AS separator;
SELECT '检查用户角色关联:' AS info;
SELECT * FROM wk_admin_user_role WHERE user_id = 1559019593470803969;

-- 3. 检查角色菜单权限数量
SELECT '=======================' AS separator;
SELECT '超级管理员的菜单权限数量:' AS info;
SELECT COUNT(*) AS permission_count 
FROM wk_admin_role_menu 
WHERE role_id = 1559019593879896074;

-- 4. 检查组织管理相关的菜单
SELECT '=======================' AS separator;
SELECT '组织管理相关菜单:' AS info;
SELECT menu_id, parent_id, menu_name, realm, menu_type, sorts 
FROM wk_admin_menu 
WHERE menu_id IN (830, 831, 832, 833, 834, 835)
   OR menu_name LIKE '%组织%'
ORDER BY menu_id;

-- 5. 检查超级管理员是否有组织管理权限
SELECT '=======================' AS separator;
SELECT '超级管理员的组织管理权限:' AS info;
SELECT rm.*, m.menu_name, m.realm
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
  AND rm.menu_id IN (830, 831, 832, 833, 834, 835)
ORDER BY rm.menu_id;

-- 6. 列出前30个权限详情
SELECT '=======================' AS separator;
SELECT '前30个菜单权限详情:' AS info;
SELECT 
    rm.id,
    rm.role_id,
    rm.menu_id,
    m.menu_name,
    m.realm,
    m.menu_type
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
ORDER BY rm.menu_id
LIMIT 30;
