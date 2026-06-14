import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/** Pinia锛氱櫥褰?token 涓庣敤鎴蜂俊鎭?*/
export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const username = ref(localStorage.getItem('username') || '')
  const role = ref(localStorage.getItem('role') || '')

  const isLogin = computed(() => !!token.value)
  const isAdmin = computed(() => role.value === 'admin')

  function setUser(data) {
    token.value = data.token
    userId.value = String(data.userId)
    username.value = data.username
    role.value = data.role
    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', String(data.userId))
    localStorage.setItem('username', data.username)
    localStorage.setItem('role', data.role)
  }

  function logout() {
    token.value = ''
    userId.value = ''
    username.value = ''
    role.value = ''
    localStorage.clear()
  }

  return { token, userId, username, role, isLogin, isAdmin, setUser, logout }
})
