<template>
  <div class="col-md-4 col-md-offset-4">
    <h3>注册</h3>
    <form @submit.prevent="submit">
      <input v-model="form.username" class="form-control" placeholder="用户名" required />
      <input v-model="form.password" type="password" class="form-control" placeholder="密码" required />
      <button class="btn btn-success btn-block">注册</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'

const form = ref({ username: '', password: '' })
const router = useRouter()

async function submit() {
  await request.post('/api/auth/register', form.value)
  alert('注册成功，请登录')
  router.push('/login')
}
</script>
