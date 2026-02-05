/** 系统管理路由 */
import Layout from '@/views/layout/AdminLayout'

const layout = function(meta = {}, path = '/manage', requiresAuth = true) {
  return {
    path: path,
    component: Layout,
    meta: {
      requiresAuth: requiresAuth,
      ...meta
    }
  }
}

export default [
  {
    ...layout({
      permissions: ['manage'],
      title: '系统管理',
      icon: 's-setting-line'
    }, '/manage/system'),
    alwaysShow: true,
    children: [
      // 管理员管理
      {
        path: 'admin-user',
        name: 'AdminUser',
        component: () => import('@/views/admin/adminUser'),
        meta: {
          title: '管理员管理',
          requiresAuth: true,
          permissions: ['manage']
        }
      },
      // 角色管理
      {
        path: 'role',
        name: 'RoleManage',
        component: () => import('@/views/admin/role'),
        meta: {
          title: '角色管理',
          requiresAuth: true,
          permissions: ['manage']
        }
      },
      // 菜单管理
      {
        path: 'menu',
        name: 'MenuManage',
        component: () => import('@/views/admin/menu'),
        meta: {
          title: '菜单管理',
          requiresAuth: true,
          permissions: ['manage']
        }
      },
      // 恢复密码
      {
        path: 'reset-password',
        name: 'ResetPassword',
        component: () => import('@/views/admin/resetPassword'),
        meta: {
          title: '恢复密码',
          requiresAuth: true,
          permissions: ['manage']
        }
      },
      // === 原人力资源管理下的菜单（已合并到系统管理） ===
      {
        path: 'custom-field',
        component: () => import('@/views/admin/hrm/customField'),
        meta: {
          title: '自定义字段设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'field']
        }
      },
      {
        name: 'hrmExamine',
        path: 'examine',
        component: () => import('@/views/admin/examine'),
        meta: {
          title: '业务审批流',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'examine']
        }
      },
      {
        name: 'hrmCustomField',
        path: 'customField/:type/:label/:id',
        component: () => import('@/views/admin/fields'),
        hidden: true,
        meta: {
          activeMenu: '/manage/system/custom-field',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'field']
        }
      },
      {
        path: 'salary/rules',
        name: 'salaryRules',
        component: () => import('@/views/admin/hrm/salary/Rules'),
        meta: {
          title: '薪资设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'salary']
        }
      },
      {
        path: 'salary/compute',
        name: 'salaryCompute',
        component: () => import('@/views/admin/hrm/salary/Compute'),
        meta: {
          title: '计薪设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'computeSalary']
        }
      },
      {
        path: 'salary/options',
        name: 'salaryOptions',
        component: () => import('@/views/admin/hrm/salary/Options'),
        meta: {
          title: '工资表设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'optionSalary']
        }
      },
      {
        name: 'insuranceSchemeSet',
        path: 'insurance-scheme',
        component: () => import('@/views/admin/hrm/insuranceScheme'),
        meta: {
          title: '社保方案管理',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'insurance']
        }
      },
      {
        path: 'schedule',
        component: () => import('@/views/admin/hrm/schedule'),
        meta: {
          title: '考勤规则设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'attendanceRule']
        }
      },
      {
        path: 'biz-param',
        component: () => import('@/views/admin/hrm/bizParam'),
        meta: {
          title: '业务参数设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'params']
        }
      },
      {
        name: 'employeeManageSet',
        path: 'employee-manage',
        component: () => import('@/views/admin/hrm/employeeManage'),
        meta: {
          title: '员工管理设置',
          requiresAuth: true,
          permissions: ['manage', 'hrm', 'archives']
        }
      }
    ]
  },
  // === 人力资源管理（已隐藏，路由保留以便将来恢复） ===
  {
    ...layout({
      permissions: ['manage', 'hrm'],
      title: '人力资源管理',
      icon: 's-contacts-line'
    }, '/manage/hrm'),
    hidden: true, // 隐藏菜单，但保留路由配置
    alwaysShow: true,
    children: [{
      path: 'custom-field',
      component: () => import('@/views/admin/hrm/customField'),
      meta: {
        title: '自定义字段设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'field']
      }
    }, {
      name: 'hrmExamineOld',
      path: 'examine',
      component: () => import('@/views/admin/examine'),
      meta: {
        title: '业务审批流',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'examine']
      }
    }, {
      name: 'hrmCustomFieldOld',
      path: 'customField/:type/:label/:id',
      component: () => import('@/views/admin/fields'),
      hidden: true,
      meta: {
        activeMenu: '/manage/hrm/custom-field',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'field']
      }
    }, {
      path: 'salary/rules',
      name: 'salaryRulesOld',
      component: () => import('@/views/admin/hrm/salary/Rules'),
      meta: {
        title: '薪资设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'salary']
      }
    }, {
      path: 'salary/compute',
      name: 'salaryComputeOld',
      component: () => import('@/views/admin/hrm/salary/Compute'),
      meta: {
        title: '计薪设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'computeSalary']
      }
    }, {
      path: 'salary/options',
      name: 'salaryOptionsOld',
      component: () => import('@/views/admin/hrm/salary/Options'),
      meta: {
        title: '工资表设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'optionSalary']
      }
    }, {
      name: 'insuranceSchemeSetOld',
      path: 'insurance-scheme',
      component: () => import('@/views/admin/hrm/insuranceScheme'),
      meta: {
        title: '社保方案管理',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'insurance']
      }
    }, {
      path: 'schedule',
      component: () => import('@/views/admin/hrm/schedule'),
      meta: {
        title: '考勤规则设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'attendanceRule']
      }
    }, {
      path: 'biz-param',
      component: () => import('@/views/admin/hrm/bizParam'),
      meta: {
        title: '业务参数设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'params']
      }
    }, {
      name: 'employeeManageSetOld',
      path: 'employee-manage',
      component: () => import('@/views/admin/hrm/employeeManage'),
      meta: {
        title: '员工管理设置',
        requiresAuth: true,
        permissions: ['manage', 'hrm', 'archives']
      }
    }]
  },
  {
    ...layout({
      permissions: ['manage']
    }, '/manage', true),
    hidden: true, // 隐藏流程委托菜单
    children: [{
      path: 'process-delegation',
      component: () => import('@/views/admin/processDelegation'),
      meta: {
        title: '流程委托',
        icon: 'icon-stage'
      }
    }]
  }
]
