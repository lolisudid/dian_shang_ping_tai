<template>
  <div>
    <nav class="navbar navbar-default">
      <div class="container-fluid">
        <div class="navbar-header">
          <router-link to="/" class="navbar-brand">电商平台</router-link>
        </div>
        <ul class="nav navbar-nav">
          <li><router-link to="/products">商品列表</router-link></li>
          <li v-if="user.isLogin"><router-link to="/cart">购物车 ({{ cart.count }})</router-link></li>
          <li v-if="user.isLogin"><router-link to="/orders">我的订单</router-link></li>
          <li v-if="user.isAdmin"><router-link to="/admin/products">商品管理</router-link></li>
          <li v-if="user.isAdmin"><router-link to="/admin/orders">订单管理</router-link></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li v-if="!user.isLogin"><router-link to="/login">登录</router-link></li>
          <li v-if="!user.isLogin"><router-link to="/register">注册</router-link></li>
          <li v-if="user.isLogin">
            <a href="#">{{ user.username }} ({{ user.isAdmin ? '管理员' : '用户' }})</a>
          </li>
          <li v-if="user.isLogin"><a href="#" @click.prevent="logout">退出</a></li>
        </ul>
      </div>
    </nav>
    <div class="container">
      <router-view />
    </div>

    <!-- 全局 Toast 通知 -->
    <div class="toast-container">
      <div v-for="toast in toasts" :key="toast.id" class="toast-item" :class="'toast-' + toast.type"
           @click="remove(toast.id)">
        {{ toast.message }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from './stores/user'
import { useCartStore } from './stores/cart'
import { useToast } from './composables/toast'

const user = useUserStore()
const cart = useCartStore()
const router = useRouter()
const { toasts, remove } = useToast()

function logout() {
  user.logout()
  cart.count = 0
  router.push('/')
}

onMounted(() => cart.refreshCount())
</script>

<style>
.toast-container {
  position: fixed;
  top: 60px;
  right: 20px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-width: 360px;
}
.toast-item {
  padding: 12px 20px;
  border-radius: 6px;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  animation: toast-in 0.3s ease;
}
.toast-success { background: #5cb85c; }
.toast-error { background: #d9534f; }
.toast-warning { background: #f0ad4e; }
.toast-info { background: #5bc0de; }
@keyframes toast-in {
  from { opacity: 0; transform: translateX(30px); }
  to { opacity: 1; transform: translateX(0); }
}
</style>
