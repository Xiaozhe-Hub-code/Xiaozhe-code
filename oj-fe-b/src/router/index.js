import { getToken } from '@/utils/cookie'
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/oj/login',
      name: 'login',
      component: () => import('@/views/Login.vue')
    },
    {
      path: '/',
      redirect: 'oj/login' ,
    },
    {
      path: '/oj/system',
      name: 'system',
      component: () => import('@/views/System.vue'),
      children: [
        {
          path: 'question',
          name: 'question',
          component: () => import('@/views/Question.vue'),

        },
        {
          path: 'exam',
          name: 'exam',
          component: () => import('@/views/Exam.vue')
        },
        {
          path: 'updateExam',
          name: 'updateExam',
          component: () => import('@/views/UpdateExam.vue')
        },
        {
          path: 'cuser',
          name: 'cuser',
          component: () => import('@/views/Cuser.vue')
        }
      ]
    },
  ],
})

// 在路由跳转之前进行判断
router.beforeEach((to,from,next) => {
  if (getToken()) {
    // 已经登陆过
    if (to.path == '/oj/login') {
      next({
        path : 'oj/system/cuser'
      })
    } else {
      next(); // 映射原本的路由页（不可省略否则白屏）
    }
  } else {
    // token不存在，跳转的不是login页面
    if (to.path != '/oj/login') {
      next({
        path : 'oj/login'
      })
    } else {
      next(); // 映射原本的路由页（不可省略否则白屏）
    }
  }
})

export default router
