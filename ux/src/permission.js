import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css' // Progress 进度条样式
import { isArray } from '@/utils/types'
import Lockr from 'lockr'
import Cookies from 'js-cookie'
import { LOCAL_ADMIN_TOKEN, COOKIE_ADMIN_TOKEN } from '@/utils/constants.js'

import {
  getAuth,
  removeAuth,
  redirectLogin
} from '@/utils/auth' // 验权

/**
 * 获取本地存储的 token
 */
function getLocalToken() {
  const cookieToken = Cookies.get(COOKIE_ADMIN_TOKEN)
  const lockrToken = Lockr.get(LOCAL_ADMIN_TOKEN)
  console.log('[Auth] Cookies token:', cookieToken ? '存在' : '不存在')
  console.log('[Auth] Lockr token:', lockrToken ? '存在' : '不存在')
  return cookieToken || lockrToken
}

const pathWhiteList = ['/404', '/noAuth', '/login'] // 不重定向白名单
let tryCount = 3 // 如果0 清空授权
// 是否能继续出错
function canTryFun() {
  return tryCount > 0
}

// 出错
function executeTryFun() {
  return tryCount--
}

// 重置
function resetTryFun() {
  tryCount = 3
}

/**
 * 获取 url 中携带参数
 */
function getUrlSearch() {
  const result = {}
  window.location.search
    .slice(1)
    .split('&')
    .filter(o => !!o)
    .forEach(o => {
      const temp = o.split('=')
      result[temp[0]] = temp[1]
    })

  const hash = decodeURIComponent(window.location.hash)
  const index = hash.lastIndexOf('?')
  if (index !== -1) {
    hash
      .slice(index + 1)
      .split('&')
      .filter(o => !!o)
      .forEach(o => {
        const temp = o.split('=')
        result[temp[0]] = temp[1]
      })
  }
  return result
}

router.beforeEach(async(to, from, next) => {
  store.commit('CLEAR_CANCEL_TOKEN') // 取消请求
  NProgress.start()

  const token = getLocalToken()
  const isLoginPage = to.path === '/login' || to.name === 'login'

  // 1. 登录页特殊处理
  if (isLoginPage) {
    // 如果有 token 且不是从其他页面跳转来的（带 redirect 参数），跳转到首页
    if (token && !to.query.redirect) {
      window.__requireLogin = false
      window.__localAuth = true
      next({ path: '/' })
      NProgress.done()
      return
    }
    // 否则放行到登录页
    next()
    NProgress.done()
    return
  }

  // 2. 检查是否有 token
  if (!token) {
    // 没有 token，跳转登录页
    const redirect = encodeURIComponent(to.fullPath || '/')
    next(`/login?redirect=${redirect}`)
    NProgress.done()
    return
  }

  // 3. 有 token，进入本地登录模式
  window.__requireLogin = false
  window.__localAuth = true

  // 本地登录模式
  if (window.__localAuth) {
    NProgress.start()
    // 本地登录模式：强制每次都重新获取权限和路由
    if (store.getters.addRouters.length === 0) {
      console.log('[LocalAuth] 初次加载，获取权限数据...')
      // 获取用户信息
      await store.dispatch('GetUserInfo')
      console.log('[LocalAuth] 用户信息已加载')
      // 获取权限数据
      const auths = await store.dispatch('getAuth')
      await store.dispatch('GenerateRoutes', auths)
      router.addRoutes(store.getters.addRouters)
      router.addRoutes([{ path: '*', redirect: '/404', hidden: true }])
      console.log('[LocalAuth] 权限数据已加载，路由数量:', store.getters.addRouters.length)
      next({ ...to, replace: true })
    } else {
      next()
    }
    NProgress.done()
    return
  }

  let redirectedFrom = to.redirectedFrom
  if (from.name === 'login' && to.path === '/404' && redirectedFrom.includes('404')) {
    redirectedFrom = '/' // 可能因重定向导致404 校准一次
  }

  if (to.meta.disabled) {
    next(false)
    return
  }
  NProgress.start()
  // 允许进入登录页面  1005 需要完善信息 不能清除登录信息
  if (window.accessLogin) {
    next()
    NProgress.done()
    return
  }

  // 请求头包含授权信息 并且 页面必须授权 直接进入
  try {
    const params = getUrlSearch()
    if (params && params.authCode) {
      await store.dispatch('AuthorizationLogin', { code: params.authCode })
      const url = new URL(window.location.href)
      url.searchParams.delete('authCode')
      // eslint-disable-next-line require-atomic-updates
      window.location.href = url.toString()
    }
    await getAuth()
    if (to.name === 'login') {
      next({
        path: '/'
      })
    } else {
      if (store.getters.addRouters.length === 0) { // 判断当前用户是否获取权限
        // await store.dispatch('QueryModules')
        const auths = await store.dispatch('getAuth')
        await store.dispatch('GenerateRoutes', auths)
        router.addRoutes(store.getters.addRouters)
        // 最后添加通配符路由，确保它在所有动态路由之后
        router.addRoutes([{ path: '*', redirect: '/404', hidden: true }])
        resetTryFun()
        if (pathWhiteList.includes(to.path)) {
          if (isArray(store.getters.addRouters) && store.getters.addRouters.length === 0) {
            throw new Error('routeError')
          } else {
            next({
              path: redirectedFrom || '/',
              replace: true
            })
          }
        } else {
          next({
            ...to,
            replace: true
          })
        }
      } else {
        resetTryFun()
        next()
      }
      NProgress.done()
    }
  } catch (error) {
    executeTryFun()
    // 302 登录信息失效
    if (error && (error.code === 302) || !canTryFun()) {
      resetTryFun()
      await removeAuth()
      // 没权限
      redirectLogin()
      NProgress.done()
    } else {
      // 因为网络原因，但已登录，转404页面。 如果没有，跳转登录
      getAuth().then(res => {
        if (error) {
          to.name === 'noAuth' ? next() : next('/noAuth')
        } else {
          if (pathWhiteList.includes(to.path)) {
            next(redirectedFrom || '/')
          } else {
            next()
          }
        }
        NProgress.done()
      }).catch(() => {
        // 没权限，跳转id center登录
        redirectLogin()
        NProgress.done()
      })
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})

router.onError((error) => {
  const pattern = /Loading chunk (\d)+ failed/g
  const isChunkLoadFailed = error.message.match(pattern)
  const targetPath = router.history.pending.fullPath
  if (isChunkLoadFailed) {
    router.replace(targetPath)
  }
})
