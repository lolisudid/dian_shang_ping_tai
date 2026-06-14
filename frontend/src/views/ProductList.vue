<template>
  <div>
    <h3>商品列表</h3>
    <form class="form-inline" @submit.prevent="search">
      <input v-model="query.name" class="form-control" placeholder="名称" />
      <input v-model="query.category" class="form-control" placeholder="分类" />
      <button class="btn btn-default" type="submit">搜索</button>
    </form>
    <p v-if="loading">加载中...</p>
    <p v-if="error" class="text-danger">{{ error }}</p>
    <table class="table table-striped" style="margin-top:15px">
      <thead><tr><th>名称</th><th>分类</th><th>价格</th><th>库存</th><th></th></tr></thead>
      <tbody>
        <tr v-for="p in list" :key="p.id">
          <td>{{ p.name }}</td>
          <td>{{ p.category }}</td>
          <td>楼{{ p.price }}</td>
          <td>{{ p.stock }}</td>
          <td><router-link :to="`/products/${p.id}`">详情</router-link></td>
        </tr>
      </tbody>
    </table>
    <div v-if="total > size" class="text-center">
      <button class="btn btn-default" :disabled="page <= 1" @click="page--; load()">上一页</button>
      <span>第 {{ page }} / {{ Math.ceil(total / size) }} 页（共 {{ total }} 条）</span>
      <button class="btn btn-default" :disabled="page >= Math.ceil(total / size)" @click="page++; load()">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../api/request'

const query = ref({ name: '', category: '' })
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
    const res = await request.get('/api/products', {
      params: { page: page.value, size: size.value, ...query.value }
    })
    list.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

function search() {
  page.value = 1
  load()
}

onMounted(load)
</script>
