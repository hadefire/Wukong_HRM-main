import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} params - { username: 手机号, password: 密码 }
 */
export function loginAPI(params) {
  return request({
    url: '/auth/login',
    method: 'post',
    data: params,
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    }
  })
}

export function authorizationAPI(params) {
  return request({
    url: '/adminUser/authorization',
    method: 'post',
    data: params
  })
}

export function logoutAPI() {
  return request({
    url: '/auth/logout',
    method: 'post'
  })
}

/**
 * 修改密码
 */
export function changePasswordAPI(params) {
  return request({
    url: '/auth/changePassword',
    method: 'post',
    params: params
  })
}

/**
 * 重置密码（管理员操作）
 */
export function resetPasswordAPI(employeeId) {
  return request({
    url: '/auth/resetPassword',
    method: 'post',
    params: { employeeId }
  })
}
