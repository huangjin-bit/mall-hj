import { get, post, put, del } from './request'

export function getUserList(params: any) {
  return get('/sys/user/list', params)
}

export function getUserById(id: number) {
  return get(`/sys/user/${id}`)
}

export function saveUser(data: any) {
  return post('/sys/user', data)
}

export function updateUser(data: any) {
  return put('/sys/user', data)
}

export function deleteUser(id: number) {
  return del(`/sys/user/${id}`)
}

export function resetPassword(id: number, password: string) {
  return put(`/sys/user/${id}/reset-password`, { password })
}

export function assignRoles(id: number, roleIds: number[]) {
  return put(`/sys/user/${id}/roles`, { roleIds })
}

export function updateProfile(data: any) {
  return put('/sys/user/profile', data)
}

export function updatePassword(data: { oldPassword: string; newPassword: string }) {
  return put('/sys/user/password', data)
}
