import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../api/request'

/** Pinia：购物车条数（列表仍从接口拉取） */
export const useCartStore = defineStore('cart', () => {
  const count = ref(0)

  async function refreshCount() {
    try {
      const res = await request.get('/api/cart')
      count.value = (res.data || []).length
    } catch {
      count.value = 0
    }
  }

  return { count, refreshCount }
})
