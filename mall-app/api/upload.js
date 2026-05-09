import { get } from './request'

/**
 * 获取上传token
 * @returns {Promise} - 返回上传token
 */
export function getUploadToken() {
  return get('/member/upload/token')
}

/**
 * 上传图片
 * @param {Object} file - 文件对象
 * @returns {Promise} - 返回上传结果
 */
export function uploadImage(file) {
  return new Promise((resolve, reject) => {
    // 先选择图片
    uni.chooseImage({
      count: 1,
      sizeType: ['compressed'],
      sourceType: ['album', 'camera'],
      success: (res) => {
        const tempFilePath = res.tempFilePaths[0]

        // 显示加载提示
        uni.showLoading({
          title: '上传中...'
        })

        // 上传图片
        uni.uploadFile({
          url: 'http://localhost:8080/api/member/upload/image',
          filePath: tempFilePath,
          name: 'file',
          header: {
            'Authorization': `Bearer ${uni.getStorageSync('token')}`
          },
          success: (uploadRes) => {
            uni.hideLoading()
            const data = JSON.parse(uploadRes.data)
            if (data.code === 200) {
              resolve(data.data)
            } else {
              uni.showToast({
                title: data.msg || '上传失败',
                icon: 'none'
              })
              reject(new Error(data.msg || '上传失败'))
            }
          },
          fail: (err) => {
            uni.hideLoading()
            console.error('[Upload Error]', err)
            uni.showToast({
              title: '上传失败',
              icon: 'none'
            })
            reject(err)
          }
        })
      },
      fail: (err) => {
        console.error('[ChooseImage Error]', err)
        reject(err)
      }
    })
  })
}

/**
 * 上传文件
 * @param {String} filePath - 文件路径
 * @returns {Promise} - 返回上传结果
 */
export function uploadFile(filePath) {
  return new Promise((resolve, reject) => {
    uni.showLoading({
      title: '上传中...'
    })

    uni.uploadFile({
      url: 'http://localhost:8080/api/member/upload',
      filePath: filePath,
      name: 'file',
      header: {
        'Authorization': `Bearer ${uni.getStorageSync('token')}`
      },
      success: (res) => {
        uni.hideLoading()
        const data = JSON.parse(res.data)
        if (data.code === 200) {
          resolve(data.data)
        } else {
          uni.showToast({
            title: data.msg || '上传失败',
            icon: 'none'
          })
          reject(new Error(data.msg || '上传失败'))
        }
      },
      fail: (err) => {
        uni.hideLoading()
        console.error('[Upload Error]', err)
        uni.showToast({
          title: '上传失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}
