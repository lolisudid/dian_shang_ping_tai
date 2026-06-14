<template>
  <div>
    <h3>订单管理</h3>
    <p v-if="loading">加载中...</p>
    <p v-if="error" class="text-danger">{{ error }}</p>
    <table class="table table-striped">
      <thead><tr><th>ID</th><th>用户</th><th>金额</th><th>状态</th><th>操作</th></tr></thead>
      <tbody>
        <tr v-for="o in list" :key="o.id">
          <td>{{ o.id }}</td>
          <td>{{ o.username }}</td>
          <td>￥{{ o.totalAmount }}</td>
          <td>{{ o.status }}</td>
          <td>
            <select v-model="o._newStatus" class="form-control input-sm">
              <option value="PENDING">PENDING</option>
              <option value="SHIPPED">SHIPPED</option>
              <option value="COMPLETED">COMPLETED</option>
              <option value="CANCELLED">CANCELLED</option>
            </select>
            <button class="btn btn-xs btn-primary" @click="update(o)">更新</button>
          </td>
        </tr>
      </tbody>
    </table>
    <div v-if="total > size" class="text-center">
      <button class="btn btn-default" :disabled="page <= 1" @click="page--; load()">上一页</button>
      <span>第 {{ page }} / {{ Math.ceil(total / size) }} 页</span>
      <button class="btn btn-default" :disabled="page >= Math.ceil(total / size)" @click="page++; load()">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'

const list = ref([])
const loading = ref(false)
const error = ref('')
const page = ref(1)
const size = ref(10)
const total = ref(0)

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await request.get('/api/orders/admin', { params: { page: page.value, size: size.value } })
    list.value = (res.data.records || []).map(o => ({ ...o, _newStatus: o.status }))
    total.value = res.data.total || 0
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function update(o) {
  try {
    await request.put(`/api/orders/${o.id}/status`, { status: o._newStatus })
    await load()
  } catch (e) {
    alert('状态更新失败：' + e.message)
  }
}

onMounted(load)
</script>
