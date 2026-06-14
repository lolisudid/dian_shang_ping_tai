<template>
  <div>
    <h3>商品管理</h3>
    <form class="well" @submit.prevent="save">
      <input v-model="form.name" class="form-control" placeholder="名称" required />
      <input v-model="form.category" class="form-control" placeholder="分类" />
      <input v-model.number="form.price" type="number" step="0.01" class="form-control" placeholder="价格" />
      <input v-model.number="form.stock" type="number" class="form-control" placeholder="库存" />
      <input v-model="form.imageUrl" class="form-control" placeholder="图片URL" />
      <textarea v-model="form.description" class="form-control" placeholder="描述"></textarea>
      <button type="button" class="btn btn-info btn-sm" @click="genAi">AI 生成描述</button>
      <button class="btn btn-primary">{{ form.id ? '更新' : '新增' }}</button>
      <button v-if="form.id" type="button" class="btn btn-default" @click="reset">取消编辑</button>
    </form>
    <p v-if="loading">加载中...</p>
    <p v-if="error" class="text-danger">{{ error }}</p>
    <table class="table table-bordered">
      <thead><tr><th>ID</th><th>名称</th><th>价格</th><th>库存</th><th>操作</th></tr></thead>
      <tbody>
        <tr v-for="p in list" :key="p.id">
          <td>{{ p.id }}</td>
          <td>{{ p.name }}</td>
          <td>{{ p.price }}</td>
          <td>{{ p.stock }}</td>
          <td>
            <button class="btn btn-xs btn-warning" @click="edit(p)">编辑</button>
            <button class="btn btn-xs btn-danger" @click="del(p.id)">下架</button>
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
const form = ref({ id: null, name: '', category: '', price: 0, stock: 0, imageUrl: '', description: '' })

async function load() {
  loading.value = true
  error.value = ''
  try {
    const res = await request.get('/api/products', { params: { page: page.value, size: size.value, adminView: true } })
    list.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

async function genAi() {
  const res = await request.post('/api/products/ai-description', { name: form.value.name, category: form.value.category })
  form.value.description = res.data
}

async function save() {
  try {
    if (form.value.id) {
      await request.put(`/api/products/${form.value.id}`, form.value)
    } else {
      await request.post('/api/products', form.value)
    }
    reset()
    await load()
  } catch (e) {
    alert('操作失败：' + e.message)
  }
}

function edit(p) {
  form.value = { ...p }
}

function reset() {
  form.value = { id: null, name: '', category: '', price: 0, stock: 0, imageUrl: '', description: '' }
}

async function del(id) {
  if (!confirm('确认下架？')) return
  try {
    await request.delete(`/api/products/${id}`)
    await load()
  } catch (e) {
    alert('下架失败：' + e.message)
  }
}

onMounted(load)
</script>
