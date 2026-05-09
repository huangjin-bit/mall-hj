import { get, post, put, del } from './request'

export function getMenuList(params?: any) {
  return get('/sys/menu/list', params)
}

export function getMenuTree() {
  return get('/sys/menu/tree')
}

export function getMenuById(id: number) {
  return get(`/sys/menu/${id}`)
}

export function saveMenu(data: any) {
  return post('/sys/menu', data)
}

export function updateMenu(data: any) {
  return put('/sys/menu', data)
}

export function deleteMenu(id: number) {
  return del(`/sys/menu/${id}`)
}
