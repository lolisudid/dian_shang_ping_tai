<template>
  <div class="col-md-4 col-md-offset-4">
    <h3>登录</h3>
    <form @submit.prevent="submit">
      <input v-model="form.username" class="form-control" placeholder="用户名" required />
      <input v-model="form.password" type="password" class="form-control" placeholder="密码" required />
      <button class="btn btn-primary btn-block">登录</button>
    </form>
    <p class="text-muted">管理员默认：admin / admin123</p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'
import { useUserStore } from '../stores/user'
import { useCartStore } from '../stores/cart'

const form = ref({ username: '', password: '' })
const user = useUserStore()
const cart = useCartStore()
const route = useRoute()
const router = useRouter()

async function submit() {
  const res = await request.post('/api/auth/login', form.value)
  user.setUser(res.data)
  await cart.refreshCount()
  router.push(route.query.redirect || '/')
}
</script>
