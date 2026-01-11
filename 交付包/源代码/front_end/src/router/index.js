import { createRouter, createWebHistory } from 'vue-router'
import auth from '@/utils/auth'

const routes = [
  // 首页（测试页面）
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/Index.vue'),
    meta: { title: '首页' }
  },
  
  // 认证相关路由
  {
    path: '/login',
    component: () => import('@/layout/AuthLayout.vue'),
    children: [
      {
        path: '',
        name: 'Login',
        component: () => import('@/views/auth/Login.vue'),
        meta: { title: '登录' }
      }
    ]
  },
  {
    path: '/register',
    component: () => import('@/layout/AuthLayout.vue'),
    children: [
      {
        path: '',
        name: 'Register',
        component: () => import('@/views/auth/Register.vue'),
        meta: { title: '注册' }
      }
    ]
  },

  // 主应用路由
  {
    path: '/notes',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'NoteList',
        component: () => import('@/views/note/List.vue'),
        meta: { title: '我的笔记' }
      },
      {
        path: 'create',
        name: 'NoteCreate',
        component: () => import('@/views/note/Edit.vue'),
        meta: { title: '新建笔记' }
      },
      {
        path: ':id/edit',
        name: 'NoteEdit',
        component: () => import('@/views/note/Edit.vue'),
        meta: { title: '编辑笔记' }
      },
      {
        path: ':id',
        name: 'NoteDetail',
        component: () => import('@/views/note/Detail.vue'),
        meta: { title: '笔记详情' }
      },
      {
        path: 'trash',
        name: 'Trash',
        component: () => import('@/views/note/Trash.vue'),
        meta: { title: '回收站' }
      }
    ]
  },

  // 搜索路由
  {
    path: '/search',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Search',
        component: () => import('@/views/note/Search.vue'),
        meta: { title: '搜索笔记' }
      }
    ]
  },

  // 用户中心路由
  {
    path: '/profile',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'Profile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人信息' }
      }
    ]
  },

  // 分类管理路由
  {
    path: '/categories',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'CategoryList',
        component: () => import('@/views/category/List.vue'),
        meta: { title: '分类管理' }
      }
    ]
  },

  // 标签管理路由
  {
    path: '/tags',
    component: () => import('@/layout/MainLayout.vue'),
    meta: { requiresAuth: true },
    children: [
      {
        path: '',
        name: 'TagList',
        component: () => import('@/views/tag/List.vue'),
        meta: { title: '标签管理' }
      }
    ]
  },

  // 404 页面
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 云笔记` : '云笔记'

  // 检查是否已登录
  const isAuthenticated = auth.isAuthenticated()

  // 如果已登录，访问首页，重定向到笔记列表
  if (isAuthenticated && to.path === '/') {
    next('/notes')
    return
  }

  // 如果已登录，访问登录/注册页面，重定向到笔记列表
  if (isAuthenticated && (to.path === '/login' || to.path === '/register')) {
    next('/notes')
    return
  }

  // 如果未登录，访问需要登录的页面，重定向到登录页
  if (!isAuthenticated && to.meta.requiresAuth) {
    next('/login')
    return
  }

  next()
})

export default router
