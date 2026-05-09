import { post } from './request'

export interface UploadResult {
  url: string
  fileName: string
}

// 上传单个文件
export const uploadFile = (file: File): Promise<UploadResult> => {
  const formData = new FormData()
  formData.append('file', file)
  return post<UploadResult>('/thirdparty/oss/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 批量上传文件
export const uploadBatch = (files: File[]): Promise<UploadResult[]> => {
  const formData = new FormData()
  files.forEach(file => {
    formData.append('files', file)
  })
  return post<UploadResult[]>('/thirdparty/oss/upload/batch', formData, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 删除文件
export const deleteFile = (fileUrl: string) => {
  return post<void>('/thirdparty/oss/delete', { fileUrl })
}
