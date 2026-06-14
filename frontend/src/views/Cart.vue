<template>
  <div>
    <h3>购物车</h3>
    <table class="table">
      <thead><tr><th>商品</th><th>单价</th><th>数量</th><th>小计</th><th></th></tr></thead>
      <tbody>
        <tr v-for="item in items" :key="item.id">
          <td>{{ item.productName }}</td>
          <td>￥{{ item.productPrice }}</td>
          <td>
            <input v-model.number="item.quantity" type="number" min="1" class="form-control" style="width:70px"
                   @change="updateQty(item)" />
          </td>
          <td>￥{{ (item.productPrice * item.quantity).toFixed(2) }}</td>
          <td><button class="btn btn-xs btn-danger" @click="remove(item.id)">删除</button></td>
        </tr>
      </tbody>
    </table>
    <p v-if="loading">加载中...</p>
    <p v-if="error" class="text-danger">{{ error }}</p>
    <button class="btn btn-success" :disabled="!items.length" @click="submit">提交订单</button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../api/request'
import { useCartStore } from '../stores/cart'

const items = ref([])
const loading = ref(false)
const error = ref('')
const cart = useCartStore()
const router = useRouter()

async function load() {
  loading.value = true
  error.value = ''
  try {
    items.value = (await request.get('/api/cart')).data || []
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function updateQty(item) {
  try {
    await request.put(`/api/cart/${item.id}`, { quantity: item.quantity })
  } catch (e) {
    alert(e.message)
    await load() // 还原正确的数量
  }
}

async function remove(id) {
  await request.delete(`/api/cart/${id}`)
  await load()
  await cart.refreshCount()
}

async function submit() {
  try {
    await request.post('/api/orders/submit')
    await cart.refreshCount()
    alert('下单成功')
    router.push('/orders')
  } catch (e) {
    alert('下单失败：' + e.message)
  }
}

onMounted(load)
</script>
