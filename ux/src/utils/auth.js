import axios from '@/utils/request'
import cache from './cache'
import Lockr from 'lockr'
import store from '@/store'
import Cookies from 'js-cookie'
import { getCookiesDomain } from '@/utils'
import { LOCAL_ADMIN_TOKEN, COOKIE_ADMIN_TOKEN } from '@/utils/constants.js'

/** 移除授权信息 */
export function removeAuth(props = { clearCookies: false }) {
  return new Promise((resolve, reject) => {
    cache.rmAxiosCache()
    store.commit('SET_ALLAUTH', null)
    delete axios.defaults.headers.common[LOCAL_ADMIN_TOKEN]
    if (props && props.clearCookies) {
      const domain = getCookiesDomain()
      Cookies.remove(COOKIE_ADMIN_TOKEN, { domain })
      Cookies.remove(COOKIE_ADMIN_TOKEN)
      Lockr.rm(LOCAL_ADMIN_TOKEN)
    }
    resolve(true)
  })
}

/** 注入授权信息 */
export function addAuth(adminToken) {
  return new Promise((resolve, reject) => {
    console.log('[Auth] 保存 token:', adminToken)
    axios.defaults.headers.common[LOCAL_ADMIN_TOKEN] = adminToken
    Lockr.set(LOCAL_ADMIN_TOKEN, adminToken)
    // 设置 cookie，本地开发时不设置 domain
    const domain = getCookiesDomain()
    if (domain) {
      Cookies.set(COOKIE_ADMIN_TOKEN, adminToken, { domain: domain, expires: 365 })
    } else {
      Cookies.set(COOKIE_ADMIN_TOKEN, adminToken, { expires: 365 })
    }
    console.log('[Auth] token 已保存到 Lockr:', Lockr.get(LOCAL_ADMIN_TOKEN))
    console.log('[Auth] token 已保存到 Cookies:', Cookies.get(COOKIE_ADMIN_TOKEN))
    resolve(true)
  })
}

/** 获取授权信息 */
export function getAuth() {
  return new Promise((resolve, reject) => {
    const token = Cookies.get(COOKIE_ADMIN_TOKEN) || Lockr.get(LOCAL_ADMIN_TOKEN)
    if (!token) return reject('Not Found Token')

    cache.updateAxiosCache(token)
    if (!store.state.user.userInfo) {
      store.dispatch('GetUserInfo')
        .then(() => {
          resolve()
        })
        .catch(error => {
          reject(error)
        })
    } else {
      resolve()
    }
  })
}

/**
 * 重定向到登录页
 */
export function redirectLogin() {
  if (window.location.hash && window.location.hash.startsWith('#/login')) return
  const redirect = encodeURIComponent(window.location.hash.replace('#', '') || '/')
  window.location.href = `${window.location.origin}${window.location.pathname}#/login?redirect=${redirect}`
}
