import { get, post, put, del } from './request'

export function getDeptTree() {
  return get('/sys/dept/tree')
}

export function getDeptList(params?: any) {
  return get('/sys/dept/list', params)
}

export function saveDept(data: any) {
  return post('/sys/dept', data)
}

export function updateDept(data: any) {
  return put('/sys/dept', data)
}

export function deleteDept(id: number) {
  return del(`/sys/dept/${id}`)
}
