import request from '@/utils/request'

/**
 * 查询角色列表
 */
export function adminRoleListAPI(roleType) {
  return request({
    url: '/adminRole/list',
    method: 'post',
    params: roleType !== undefined ? { roleType } : {}
  })
}

/**
 * 查询角色详情（包含菜单权限）
 */
export function adminRoleQueryByIdAPI(roleId) {
  return request({
    url: '/adminRole/queryById',
    method: 'post',
    params: { roleId }
  })
}

/**
 * 添加角色
 */
export function adminRoleAddAPI(data) {
  return request({
    url: '/adminRole/add',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新角色
 */
export function adminRoleUpdateAPI(data) {
  return request({
    url: '/adminRole/update',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除角色
 */
export function adminRoleDeleteAPI(roleId) {
  return request({
    url: '/adminRole/delete',
    method: 'post',
    params: { roleId }
  })
}

/**
 * 启用/禁用角色
 */
export function adminRoleSetStatusAPI(roleId, status) {
  return request({
    url: '/adminRole/setStatus',
    method: 'post',
    params: { roleId, status }
  })
}
