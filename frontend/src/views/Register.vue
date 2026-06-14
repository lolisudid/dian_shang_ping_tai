<template>
  <div class="col-md-4 col-md-offset-4">
    <h3>注册</h3>
    <div v-if="errorMsg" class="alert alert-danger">{{ errorMsg }}</div>
    <form @submit.prevent="submit">
      <div class="form-group">
        <input v-model="form.username" class="form-control" placeholder="用户名（3-20位字符）" required autocomplete="username" />
      </div>
      <div class="form-group">
        <input v-model="form.password" type="password" class="form-control" placeholder="密码（至少6位）" required autocomplete="new-password" />
      </div>
      <div class="form-group">
        <input v-model="form.confirmPassword" type="password" class="form-control" placeholder="确认密码" required autocomplete="new-password" />
      </div>
      <button class="btn btn-success btn-block" :disabled="loading">
        <span v-if="loading">注册中...</span>
        <span v-else>注册</span>
      </button>
    </form>
    <p class="text-muted" style="margin-top:10px">已有账号？<router-link to="/login">立即登录</router-link></p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const form = ref({ username: '', password: '', confirmPassword: '' })
const loading = ref(false)
const errorMsg = ref('')
const router = useRouter()

async function submit() {
  errorMsg.value = ''
  if (!form.value.username.trim()) {
    errorMsg.value = '请输入用户名'
    return
  }
  if (form.value.username.trim().length < 3) {
    errorMsg.value = '用户名至少3个字符'
    return
  }
  if (!form.value.password) {
    errorMsg.value = '请输入密码'
    return
  }
  if (form.value.password.length < 6) {
    errorMsg.value = '密码至少6位'
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    errorMsg.value = '两次输入的密码不一致'
    return
  }
  loading.value = true
  try {
    await request.post('/api/auth/register', {
      username: form.value.username.trim(),
      password: form.value.password
    })
    router.push({ name: 'login', query: { registered: '1' } })
  } catch (e) {
    errorMsg.value = e.message || '注册失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>
