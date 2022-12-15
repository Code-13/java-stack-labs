import type {RouteRecordRaw} from 'vue-router'
import Home from '@/views/Home.vue'

/**
 *  404
 * 所有为定义的页面均跳转至 404 页面
 */
const notFound: RouteRecordRaw = {
  path: '/:pathMatch(.*)*',
  component: () => import('@/views/NotFound.vue'),
}

const home: RouteRecordRaw = {
  path: '/',
  name: 'Home',
  component: Home,
}

const login: RouteRecordRaw = {
  path: '/login',
  name: 'Login',
  component: () => import("@/views/Login.vue"),
}

const consent: RouteRecordRaw = {
  path: '/consent',
  name: 'Consent',
  component: () => import("@/views/Consent.vue"),
}

export default [notFound, home, login, consent]