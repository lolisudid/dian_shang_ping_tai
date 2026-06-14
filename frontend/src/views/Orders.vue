<template>
  <div>
    <h3>我的订单</h3>
    <p v-if="loading">加载中...</p>
    <p v-if="error" class="text-danger">{{ error }}</p>
    <div v-for="o in orders" :key="o.id" class="panel panel-default">
      <div class="panel-heading">
        订单 #{{ o.id }} · 楼{{ o.totalAmount }} · {{ o.status }}
        <span class="text-muted">（{{ o.createTime }}）</span>
        <button v-if="canReview(o)" class="btn btn-xs btn-info pull-right" @click="openReview(o)">评价</button>
      </div>
      <ul class="list-group">
        <li v-for="it in o.items" :key="it.id" class="list-group-item">
          {{ it.productName }} x {{ it.quantity }} · 楼{{ it.price }}
        </li>
      </ul>
    </div>
    <div v-if="total > size" class="text-center">
      <button class="btn btn-default" :disabled="page <= 1" @click="page--; load()">上一页</button>
      <span>第 {{ page }} / {{ Math.ceil(total / size) }} 页（共 {{ total }} 条）</span>
      <button class="btn btn-default" :disabled="page >= Math.ceil(total / size)" @click="page++; load()">下一页</button>
    </div>
    <div v-if="showReview" class="well">
      <h4>提交评价</h4>
      <select v-model="review.productId" class="form-control">
        <option v-for="it in reviewOrder.items" :key="it.id" :value="it.productId">{{ it.productName }}</option>
      </select>
      <input v-model.number="review.rating" type="number" min="1" max="5" class="form-control" placeholder="评分1-5" />
      <textarea v-model="review.content" class="form-control" placeholder="评论"></textarea>
      <button class="btn btn-primary" @click="submitReview">提交</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api/request'

const orders = ref([])
const loading = ref(false)
const error = ref('')
const page = ref(1)
const size = ref(5)
const total = ref(0)
const showReview = ref(false)
const reviewOrder = ref(null)
const review = ref({ productId: null, orderId: null, rating: 5, content: '' })

function canReview(o) {
  return o.status === 'SHIPPED' || o.status === 'COMPLETED'
}

function openReview(o) {
  reviewOrder.value = o
  review.value = { productId: o.items[0]?.productId, orderId: o.id, rating: 5, content: '' }
  showReview.value = true
}

async function submitReview() {
  try {
    await request.post('/api/reviews', review.value)
    showReview.value = false
    alert('评价成功')
  } catch (e) {
    alert('评价失败：' + e.message)
  }
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await request.get('/api/orders/my', { params: { page: page.value, size: size.value } })
    orders.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
