import type { Directive } from 'vue'
import { usePermissionStore } from '@/stores/permission'

export const hasPermi: Directive = {
  mounted(el, binding) {
    const { value } = binding
    const permissionStore = usePermissionStore()
    if (value && Array.isArray(value) && value.length > 0) {
      const hasPermission = value.some(perm => permissionStore.hasPermission(perm))
      if (!hasPermission) {
        el.parentNode?.removeChild(el)
      }
    }
  }
}
