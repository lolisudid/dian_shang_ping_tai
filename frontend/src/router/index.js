import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  { path: '/', name: 'home', component: () => import('../views/Home.vue') },
  { path: '/products', name: 'products', component: () => import('../views/ProductList.vue') },
  { path: '/products/:id', name: 'product-detail', component: () => import('../views/ProductDetail.vue') },
  { path: '/cart', name: 'cart', meta: { auth: true }, component: () => import('../views/Cart.vue') },
  { path: '/orders', name: 'orders', meta: { auth: true }, component: () => import('../views/Orders.vue') },
  { path: '/login', name: 'login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'register', component: () => import('../views/Register.vue') },
  { path: '/admin/products', name: 'admin-products', meta: { auth: true, admin: true }, component: () => import('../views/admin/ProductManage.vue') },
  { path: '/admin/orders', name: 'admin-orders', meta: { auth: true, admin: true }, component: () => import('../views/admin/OrderManage.vue') }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/** 路由守卫：需登录或管理员 */
router.beforeEach((to, from, next) => {
  const user = useUserStore()
  if (to.meta.auth && !user.isLogin) {
    next({ name: 'login', query: { redirect: to.fullPath } })
    return
  }
  if (to.meta.admin && !user.isAdmin) {
    next({ name: 'home' })
    return
  }
  next()
})

export default router
