<template>
  <div>
    <h3>我的订单</h3>
    <!-- 状态筛选 -->
    <div class="btn-group" style="margin-bottom:15px">
      <button v-for="s in statuses" :key="s.value" class="btn btn-default btn-sm"
              :class="{ active: filterStatus === s.value }" @click="filterStatus = s.value; page=1; load()">
        {{ s.label }}
      </button>
    </div>

    <p v-if="loading">加载中...</p>
    <div v-if="error" class="alert alert-danger">{{ error }}</div>
    <p v-if="!loading && !error && orders.length===0" class="text-muted">暂无订单</p>

    <div v-for="o in orders" :key="o.id" class="panel panel-default">
      <div class="panel-heading">
        <strong>订单 #{{ o.id }}</strong>
        <span class="label" :class="statusClass(o.status)">{{ statusText(o.status) }}</span>
        <span class="pull-right">¥{{ o.totalAmount }}</span>
        <div class="text-muted small">{{ o.createTime }}</div>
        <button v-if="canReview(o)" class="btn btn-xs btn-info" style="margin-top:5px" @click="openReview(o)">评价</button>
      </div>
      <ul class="list-group">
        <li v-for="it in o.items" :key="it.id" class="list-group-item">
          {{ it.productName }} × {{ it.quantity }} · ¥{{ it.price }}
        </li>
      </ul>
    </div>

    <!-- 分页 -->
    <div v-if="total > size" class="text-center">
      <button class="btn btn-default" :disabled="page <= 1" @click="page--; load()">上一页</button>
      <span>第 {{ page }} / {{ Math.ceil(total / size) || 1 }} 页（共 {{ total }} 条）</span>
      <button class="btn btn-default" :disabled="page >= Math.ceil(total / size)" @click="page++; load()">下一页</button>
    </div>

    <!-- 评价弹窗 -->
    <div v-if="showReview" class="well" style="margin-top:15px">
      <h4>提交评价</h4>
      <div class="form-group">
        <select v-model="review.productId" class="form-control">
          <option v-for="it in reviewOrder?.items" :key="it.id" :value="it.productId">{{ it.productName }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>评分</label>
        <div>
          <span v-for="star in 5" :key="star" style="cursor:pointer;font-size:20px"
                @click="review.rating = star" :style="{ color: star <= review.rating ? '#f0ad4e' : '#ccc' }">★</span>
        </div>
      </div>
      <div class="form-group">
        <textarea v-model="review.content" class="form-control" rows="3" placeholder="写下你的评价..."></textarea>
      </div>
      <button class="btn btn-primary" :disabled="reviewLoading" @click="submitReview">
        {{ reviewLoading ? '提交中...' : '提交评价' }}
      </button>
      <button class="btn btn-default" @click="showReview = false" style="margin-left:5px">取消</button>
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
const filterStatus = ref('ALL')

const showReview = ref(false)
const reviewOrder = ref(null)
const review = ref({ productId: null, orderId: null, rating: 5, content: '' })
const reviewLoading = ref(false)

const statuses = [
  { value: 'ALL', label: '全部' },
  { value: 'PENDING', label: '待发货' },
  { value: 'SHIPPED', label: '已发货' },
  { value: 'COMPLETED', label: '已完成' },
  { value: 'CANCELLED', label: '已取消' }
]

function statusText(s) {
  const map = { PENDING: '待发货', SHIPPED: '已发货', COMPLETED: '已完成', CANCELLED: '已取消' }
  return map[s] || s
}

function statusClass(s) {
  const map = { PENDING: 'label-warning', SHIPPED: 'label-info', COMPLETED: 'label-success', CANCELLED: 'label-default' }
  return map[s] || 'label-default'
}

function canReview(o) {
  return o.status === 'SHIPPED' || o.status === 'COMPLETED'
}

function openReview(o) {
  reviewOrder.value = o
  review.value = { productId: o.items[0]?.productId, orderId: o.id, rating: 5, content: '' }
  showReview.value = true
}

async function submitReview() {
  if (!review.value.content.trim()) {
    alert('请输入评价内容')
    return
  }
  reviewLoading.value = true
  try {
    await request.post('/api/reviews', review.value)
    showReview.value = false
    alert('评价成功')
  } catch (e) {
    alert('评价失败：' + e.message)
  } finally {
    reviewLoading.value = false
  }
}

async function load() {
  loading.value = true
  error.value = ''
  try {
    const params = { page: page.value, size: size.value }
    if (filterStatus.value !== 'ALL') {
      params.status = filterStatus.value
    }
    const res = await request.get('/api/orders/my', { params })
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
