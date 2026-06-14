<template>
  <div class="countdown" v-if="remaining > 0">
    <span class="countdown-label">{{ prefix }}</span>
    <span class="countdown-digit">{{ days }}</span>澶?    <span class="countdown-digit">{{ pad(hours) }}</span>:<span class="countdown-digit">{{ pad(minutes) }}</span>:<span class="countdown-digit">{{ pad(seconds) }}</span>
  </div>
  <div class="countdown countdown-ended" v-else>
    {{ endedText }}
  </div>
</template>

<script setup>
/**
 * 鍊掕鏃跺瓙缁勪欢
 * props:
 *   endTime   - 缁撴潫鏃堕棿锛圛SO 瀛楃涓叉垨鏃堕棿鎴虫绉掞級
 *   prefix    - 鍓嶇紑鏂囧瓧锛屽 "璺濈粨鏉?
 *   endedText - 鍊掕鏃剁粨鏉熷悗鏄剧ず鐨勬枃瀛? */
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  endTime: { type: [String, Number], required: true },
  prefix: { type: String, default: '璺濈粨鏉? },
  endedText: { type: String, default: '宸茬粨鏉? }
})

const now = ref(Date.now())
let timer = null

onMounted(() => {
  timer = setInterval(() => { now.value = Date.now() }, 1000)
})
onUnmounted(() => {
  clearInterval(timer)
})

const remaining = computed(() => {
  const end = new Date(props.endTime).getTime()
  return Math.max(0, end - now.value)
})

const days = computed(() => Math.floor(remaining.value / 86400000))
const hours = computed(() => Math.floor((remaining.value % 86400000) / 3600000))
const minutes = computed(() => Math.floor((remaining.value % 3600000) / 60000))
const seconds = computed(() => Math.floor((remaining.value % 60000) / 1000))

function pad(n) { return n < 10 ? '0' + n : n }
</script>

<style scoped>
.countdown {
  display: inline-flex;
  align-items: center;
  gap: 2px;
  font-size: 13px;
  color: #e74c3c;
  margin: 8px 0;
}
.countdown-label { margin-right: 4px; color: #666; }
.countdown-digit {
  background: #333;
  color: #fff;
  padding: 1px 5px;
  border-radius: 2px;
  font-family: monospace;
  font-size: 14px;
}
.countdown-ended {
  color: #999;
  font-size: 13px;
}
</style>