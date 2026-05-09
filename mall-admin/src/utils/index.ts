// 格式化日期
export const formatDate = (date: string | Date, format = 'YYYY-MM-DD HH:mm:ss') => {
  if (!date) return ''

  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

// 订单状态映射
export const orderStatusMap: Record<number, { label: string; type: string }> = {
  1: { label: '待支付', type: 'warning' },
  2: { label: '待发货', type: 'primary' },
  3: { label: '已发货', type: 'primary' },
  4: { label: '已完成', type: 'success' },
  5: { label: '已取消', type: 'info' },
  6: { label: '售后中', type: 'warning' }
}

// 发布状态映射
export const publishStatusMap: Record<number, { label: string; type: string }> = {
  0: { label: '未发布', type: 'info' },
  1: { label: '已发布', type: 'success' },
  2: { label: '已下架', type: 'danger' }
}

// 审核状态映射
export const auditStatusMap: Record<number, { label: string; type: string }> = {
  0: { label: '未审核', type: 'info' },
  1: { label: '审核通过', type: 'success' },
  2: { label: '审核拒绝', type: 'danger' }
}

// 通用启用状态映射
export const statusMap: Record<number, { label: string; type: string }> = {
  0: { label: '禁用', type: 'danger' },
  1: { label: '启用', type: 'success' }
}

// 秒杀场次状态映射
export const sessionStatusMap: Record<number, { label: string; type: string }> = {
  1: { label: '未开始', type: 'info' },
  2: { label: '进行中', type: 'success' },
  3: { label: '已结束', type: 'info' }
}

// 退货申请状态映射
export const returnStatusMap: Record<number, { label: string; type: string }> = {
  1: { label: '待审核', type: 'warning' },
  2: { label: '审核通过', type: 'success' },
  3: { label: '审核拒绝', type: 'danger' },
  4: { label: '退款中', type: 'primary' },
  5: { label: '已退款', type: 'success' }
}

// 采购单状态映射
export const purchaseStatusMap: Record<number, { label: string; type: string }> = {
  1: { label: '未完成', type: 'warning' },
  2: { label: '已完成', type: 'success' },
  3: { label: '已取消', type: 'info' }
}
