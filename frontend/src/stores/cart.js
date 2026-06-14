import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '../api/request'

/** Pinia锛氳喘鐗╄溅鏉℃暟锛堝垪琛ㄤ粛浠庢帴鍙ｆ媺鍙栵級 */
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
