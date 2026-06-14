import { ref } from 'vue'

const toasts = ref([])
let id = 0

export function useToast() {
  function show(message, type = 'info', duration = 3000) {
    const toast = { id: ++id, message, type, visible: true }
    toasts.value.push(toast)
    if (duration > 0) {
      setTimeout(() => {
        const idx = toasts.value.findIndex(t => t.id === toast.id)
        if (idx > -1) toasts.value.splice(idx, 1)
      }, duration)
    }
  }

  function success(msg) { show(msg, 'success') }
  function error(msg) { show(msg, 'error', 5000) }
  function warning(msg) { show(msg, 'warning', 4000) }
  function info(msg) { show(msg, 'info') }

  function remove(id) {
    const idx = toasts.value.findIndex(t => t.id === id)
    if (idx > -1) toasts.value.splice(idx, 1)
  }

  return { toasts, show, success, error, warning, info, remove }
}
