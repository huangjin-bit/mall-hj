import request from './request'

// 上传文件
export const uploadFile = (file: File) => {
  const formData = new FormData()
  formData.append('file', file)

  return request.post<any, string>('/third-party/oss/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}
