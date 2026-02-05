/**
 * 管理员用户管理 API
 * Created for local admin management
 */
import request from '@/utils/request'

/**
 * 分页查询管理员列表
 * @param {Object} data - 查询参数
 * @param {number} data.page - 页码
 * @param {number} data.limit - 每页数量
 * @param {string} [data.keyword] - 搜索关键字（姓名/手机号）
 * @param {number} [data.roleType] - 角色类型（1=系统管理，3=人事）
 * @param {number} [data.deptId] - 部门ID
 */
export function adminUserListAPI(data) {
  return request({
    url: 'adminUser/list',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 查询所有管理员（不分页）
 * @param {number} [roleType] - 角色类型（1=系统管理，3=人事）
 */
export function adminUserQueryAllAPI(roleType) {
  return request({
    url: 'adminUser/queryAll',
    method: 'post',
    data: {},
    params: roleType ? { roleType } : {}
  })
}

/**
 * 根据员工ID查询管理员详情
 * @param {number} employeeId - 员工ID
 */
export function adminUserQueryByIdAPI(employeeId) {
  return request({
    url: 'adminUser/queryById',
    method: 'post',
    params: { employeeId }
  })
}

/**
 * 添加管理员（为员工分配管理角色）
 * @param {Object} data - 保存参数
 * @param {number} data.employeeId - 员工ID
 * @param {number[]} data.roleIds - 角色ID列表
 */
export function adminUserAddAPI(data) {
  return request({
    url: 'adminUser/add',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 更新管理员角色
 * @param {Object} data - 保存参数
 * @param {number} data.employeeId - 员工ID
 * @param {number[]} data.roleIds - 角色ID列表
 */
export function adminUserUpdateAPI(data) {
  return request({
    url: 'adminUser/update',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

/**
 * 移除管理员身份
 * @param {number} employeeId - 员工ID
 */
export function adminUserDeleteAPI(employeeId) {
  return request({
    url: 'adminUser/delete',
    method: 'post',
    params: { employeeId }
  })
}

/**
 * 查询可分配的角色列表
 * @param {number} [roleType] - 角色类型（1=系统管理，3=人事）
 */
export function adminUserQueryRolesAPI(roleType) {
  return request({
    url: 'adminUser/queryAvailableRoles',
    method: 'post',
    params: roleType ? { roleType } : {}
  })
}
