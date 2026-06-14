<template>
  <span class="promo-badge" :class="'promo-' + type">
    {{ label }}
  </span>
</template>

<script setup>
/**
 * 淇冮攢鏍囩瀛愮粍浠? * props:
 *   type  - 淇冮攢绫诲瀷: 'hot'=鐑崠 / 'new'=鏂板搧 / 'discount'=鎶樻墸 / 'limited'=闄愰噺
 *   label - 鏄剧ず鏂囧瓧锛屼笉浼犲垯鏍规嵁 type 鑷姩鐢熸垚
 */
const props = defineProps({
  type: {
    type: String,
    default: 'hot',
    validator: (v) => ['hot', 'new', 'discount', 'limited'].includes(v)
  },
  label: {
    type: String,
    default: ''
  }
})

const defaultLabels = {
  hot: '鐑崠',
  new: '鏂板搧',
  discount: '淇冮攢',
  limited: '闄愰噺'
}
import { computed } from 'vue'
const label = computed(() => props.label || defaultLabels[props.type] || '淇冮攢')
</script>

<style scoped>
.promo-badge {
  display: inline-block;
  padding: 2px 10px;
  border-radius: 3px;
  font-size: 12px;
  font-weight: bold;
  color: #fff;
  margin-right: 6px;
  vertical-align: middle;
}
.promo-hot      { background: #e74c3c; }
.promo-new      { background: #2ecc71; }
.promo-discount { background: #f39c12; }
.promo-limited  { background: #9b59b6; }
</style>