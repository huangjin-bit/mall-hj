import request from './request'

// 用户名密码登录
export const loginByUsername = (data: { username: string; password: string }) => {
  return request.post<any, string>('/member/auth/login', data)
}

// 手机号登录
export const loginByPhone = (data: { phone: string; code: string }) => {
  return request.post<any, string>('/member/auth/login/phone', data)
}

// 注册
export const register = (data: { username: string; password: string; phone: string; code: string }) => {
  return request.post<any, string>('/member/auth/register', data)
}

// 发送短信验证码
export const sendSmsCode = (phone: string) => {
  return request.post<any, void>('/member/sms/send', { phone })
}

// 退出登录
export const logout = () => {
  return request.post<any, void>('/member/auth/logout')
}
