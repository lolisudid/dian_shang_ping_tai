<template>
  <div>
    <p v-if="loading">加载中...</p>
    <p v-if="error" class="text-danger">{{ error }}</p>
    <div v-if="product">
      <h3>{{ product.name }}</h3>
      <p class="text-muted">{{ product.category }} · 楼{{ product.price }} · 库存 {{ product.stock }}</p>
      <img v-if="product.imageUrl" :src="product.imageUrl" class="img-thumbnail" style="max-height:200px" />
      <p>{{ product.description }}</p>
      <div class="form-inline" v-if="product.stock > 0">
        <input v-model.number="qty" type="number" min="1" :max="product.stock" class="form-control" style="width:80px" />
        <button class="btn btn-primary" @click="addCart">加入购物车</button>
      </div>
      <p v-else class="text-warning">该商品暂时缺货</p>
      <hr />
      <h4>用户评价</h4>
      <ul v-if="reviews.length">
        <li v-for="r in reviews" :key="r.id">{{ r.username }}：{{ r.rating }}星 - {{ r.content || '无评论' }}</li>
      </ul>
      <p v-else class="text-muted">暂无评价</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../api/request'
import { useUserStore } from '../stores/user'
import { useCartStore } from '../stores/cart'

const route = useRoute()
const router = useRouter()
const user = useUserStore()
const cart = useCartStore()
const product = ref(null)
const reviews = ref([])
const qty = ref(1)
const loading = ref(false)
const error = ref('')

onMounted(async () => {
  loading.value = true
  try {
    const id = route.params.id
    product.value = (await request.get(`/api/products/${id}`)).data
    reviews.value = (await request.get(`/api/reviews/product/${id}`)).data || []
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
})

async function addCart() {
  if (!user.isLogin) {
    router.push({ name: 'login', query: { redirect: route.fullPath } })
    return
  }
  try {
    await request.post('/api/cart', { productId: product.value.id, quantity: qty.value })
    await cart.refreshCount()
    alert('已加入购物车')
  } catch (e) {
    alert('加入失败：' + e.message)
  }
}
</script>
