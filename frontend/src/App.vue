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
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from './stores/user'
import { useCartStore } from './stores/cart'

const user = useUserStore()
const cart = useCartStore()
const router = useRouter()

function logout() {
  user.logout()
  cart.count = 0
  router.push('/')
}

onMounted(() => cart.refreshCount())
</script>
