import request from '@/utils/request'

/**
 * 查询所有菜单列表
 */
export function adminMenuListAPI() {
  return request({
    url: '/adminMenu/queryAllMenuList',
    method: 'post'
  })
}

/**
 * 查询菜单树形结构
 */
export function adminMenuTreeAPI() {
  return request({
    url: '/adminMenu/tree',
    method: 'post'
  })
}

/**
 * 查询菜单详情
 */
export function adminMenuQueryByIdAPI(menuId) {
  return request({
    url: '/adminMenu/queryById',
    method: 'post',
    params: { menuId }
  })
}

/**
 * 添加菜单
 */
export function adminMenuAddAPI(data) {
  return request({
    url: '/adminMenu/add',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新菜单
 */
export function adminMenuUpdateAPI(data) {
  return request({
    url: '/adminMenu/update',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 删除菜单
 */
export function adminMenuDeleteAPI(menuId) {
  return request({
    url: '/adminMenu/delete',
    method: 'post',
    params: { menuId }
  })
}

/**
 * 启用/禁用菜单
 */
export function adminMenuSetStatusAPI(menuId, status) {
  return request({
    url: '/adminMenu/setStatus',
    method: 'post',
    params: { menuId, status }
  })
}
