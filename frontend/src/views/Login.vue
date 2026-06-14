<template>
  <div class="col-md-4 col-md-offset-4">
    <h3>登录</h3>
    <div v-if="route.query.expired" class="alert alert-warning">登录已过期，请重新登录</div>
    <div v-if="route.query.registered" class="alert alert-success">注册成功，请登录</div>
    <div v-if="errorMsg" class="alert alert-danger">{{ errorMsg }}</div>
    <form @submit.prevent="submit">
      <div class="form-group">
        <input v-model="form.username" class="form-control" placeholder="用户名" required autocomplete="username" />
      </div>
      <div class="form-group">
        <input v-model="form.password" type="password" class="form-control" placeholder="密码" required autocomplete="current-password" />
      </div>
      <button class="btn btn-primary btn-block" :disabled="loading">
        <span v-if="loading">登录中...</span>
        <span v-else>登录</span>
      </button>
    </form>
    <p class="text-muted" style="margin-top:10px">还没有账号？<router-link to="/register">立即注册</router-link></p>
    <p class="text-muted small">管理员默认账号：admin / admin123</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'
import { useUserStore } from '../stores/user'
import { useCartStore } from '../stores/cart'

const form = ref({ username: '', password: '' })
const loading = ref(false)
const errorMsg = ref('')
const user = useUserStore()
const cart = useCartStore()
const route = useRoute()
const router = useRouter()

async function submit() {
  errorMsg.value = ''
  if (!form.value.username.trim() || !form.value.password.trim()) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  try {
    const res = await request.post('/api/auth/login', form.value)
    user.setUser(res.data)
    await cart.refreshCount()
    router.push(route.query.redirect || '/')
  } catch (e) {
    errorMsg.value = e.message || '登录失败，请检查用户名和密码'
  } finally {
    loading.value = false
  }
}
</script>
