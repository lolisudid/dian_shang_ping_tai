import axios from 'axios'
import { useUserStore } from '../stores/user'
import router from '../router'

const request = axios.create({
  baseURL: '',
  timeout: 15000
})

request.interceptors.request.use(config => {
  const user = useUserStore()
  if (user.token) {
    config.headers.Authorization = Bearer 
  }
  return config
})

request.interceptors.response.use(
  res => {
    const body = res.data
    if (body && body.code !== undefined && body.code !== 200) {
      return Promise.reject(new Error(body.message || '请求失败'))
    }
    return body
  },
  err => {
    if (err.response && err.response.status === 401) {
      const user = useUserStore()
      if (user.isLogin) {
        user.logout()
        const currentPath = router.currentRoute.value.fullPath
        if (currentPath !== '/login') {
          router.push({ name: 'login', query: { redirect: currentPath, expired: '1' } })
        }
      }
    }
    const msg = err.response?.data?.message || err.message || '网络请求失败'
    return Promise.reject(new Error(msg))
  }
)

export default request
