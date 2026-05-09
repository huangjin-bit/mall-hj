import { post, get } from './request'

export function adminLogin(data: { username: string; password: string }) {
  return post<any>('/sys/login', data)
}

export function getAdminInfo() {
  return get<any>('/sys/user/info')
}

export function getRouters() {
  return get<any>('/sys/user/routers')
}

export function adminLogout() {
  return post('/sys/logout')
}
