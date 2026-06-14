import axios from 'axios'
import { useUserStore } from '../stores/user'
import router from '../router'

/** Axios 实例：自动携带 token，统一处理 Result 与 401 */
const request = axios.create({
  baseURL: '',
  timeout: 15000
})

request.interceptors.request.use(config => {
  const user = useUserStore()
  if (user.token) {
    config.headers.Authorization = `Bearer ${user.token}`
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
      user.logout()
      router.push({ name: 'login', query: { redirect: router.currentRoute.value.fullPath } })
    }
    const msg = err.response?.data?.message || err.message
    return Promise.reject(new Error(msg))
  }
)

export default request
