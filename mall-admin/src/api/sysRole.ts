import { get, post, put, del } from './request'

export function getRoleList(params: any) {
  return get('/sys/role/list', params)
}

export function getAllRoles() {
  return get('/sys/role/all')
}

export function getRoleById(id: number) {
  return get(`/sys/role/${id}`)
}

export function saveRole(data: any) {
  return post('/sys/role', data)
}

export function updateRole(data: any) {
  return put('/sys/role', data)
}

export function deleteRole(id: number) {
  return del(`/sys/role/${id}`)
}

export function getRoleMenus(roleId: number) {
  return get(`/sys/role/${roleId}/menus`)
}

export function assignRoleMenus(roleId: number, menuIds: number[]) {
  return put(`/sys/role/${roleId}/menus`, { menuIds })
}
