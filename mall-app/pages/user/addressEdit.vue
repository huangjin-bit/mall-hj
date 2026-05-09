<script setup>
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getAddressDetail, saveAddress, updateAddress } from '@/api/address'

const addressId = ref(null)

// 表单数据
const formData = ref({
  receiverName: '',
  receiverPhone: '',
  provinceCode: '',
  cityCode: '',
  districtCode: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: false
})

// 地区选择器
const showRegionPicker = ref(false)
const regionIndex = ref([0, 0, 0])

onLoad((options) => {
  if (options.id) {
    addressId.value = options.id
  }
})

onMounted(async () => {
  if (addressId.value) {
    await loadAddressDetail()
  }
})

async function loadAddressDetail() {
  try {
    const res = await getAddressDetail(addressId.value)
    formData.value = { ...res }
  } catch (error) {
    console.error('[LoadAddressDetail Error]', error)
  }
}

function showRegionSelector() {
  showRegionPicker.value = true
}

function handleRegionConfirm(e) {
  const [provinceIndex, cityIndex, districtIndex] = e.detail.value
  // 这里应该根据选择的索引设置地区信息
  // 简化处理，实际需要完整的地区数据
  showRegionPicker.value = false
}

async function handleSave() {
  // 表单验证
  if (!formData.value.receiverName) {
    uni.showToast({ title: '请输入收货人姓名', icon: 'none' })
    return
  }

  if (!formData.value.receiverPhone) {
    uni.showToast({ title: '请输入手机号', icon: 'none' })
    return
  }

  if (!/^1[3-9]\d{9}$/.test(formData.value.receiverPhone)) {
    uni.showToast({ title: '请输入正确的手机号', icon: 'none' })
    return
  }

  if (!formData.value.province || !formData.value.city || !formData.value.district) {
    uni.showToast({ title: '请选择地区', icon: 'none' })
    return
  }

  if (!formData.value.detailAddress) {
    uni.showToast({ title: '请输入详细地址', icon: 'none' })
    return
  }

  try {
    uni.showLoading({ title: '保存中...' })

    if (addressId.value) {
      await updateAddress(formData.value)
    } else {
      await saveAddress(formData.value)
    }

    uni.hideLoading()
    uni.showToast({ title: '保存成功', icon: 'success' })

    setTimeout(() => {
      uni.navigateBack()
    }, 1500)
  } catch (error) {
    uni.hideLoading()
    console.error('[SaveAddress Error]', error)
  }
}
</script>

<template>
  <view class="address-edit-page">
    <view class="form-section">
      <view class="form-item">
        <view class="form-label">收货人</view>
        <input
          v-model="formData.receiverName"
          class="form-input"
          placeholder="请输入收货人姓名"
          placeholder-class="input-placeholder"
        />
      </view>

      <view class="form-item">
        <view class="form-label">手机号</view>
        <input
          v-model="formData.receiverPhone"
          class="form-input"
          type="number"
          maxlength="11"
          placeholder="请输入手机号"
          placeholder-class="input-placeholder"
        />
      </view>

      <view class="form-item" @tap="showRegionSelector">
        <view class="form-label">所在地区</view>
        <view class="form-value">
          <text v-if="formData.province" class="region-text">
            {{ formData.province }} {{ formData.city }} {{ formData.district }}
          </text>
          <text v-else class="placeholder">请选择地区</text>
          <text class="arrow">›</text>
        </view>
      </view>

      <view class="form-item">
        <view class="form-label">详细地址</view>
        <textarea
          v-model="formData.detailAddress"
          class="form-textarea"
          placeholder="街道、楼牌号等"
          placeholder-class="input-placeholder"
          maxlength="200"
        />
      </view>

      <view class="form-item">
        <view class="form-label">设为默认地址</view>
        <switch
          :checked="formData.isDefault"
          @change="formData.isDefault = $event.detail.value"
          color="#e93b3d"
        />
      </view>
    </view>

    <button class="save-btn" @tap="handleSave">保存</button>

    <!-- 地区选择器 -->
    <picker
      v-if="showRegionPicker"
      mode="region"
      :value="regionIndex"
      @change="handleRegionConfirm"
      @cancel="showRegionPicker = false"
    >
      <view></view>
    </picker>
  </view>
</template>

<style lang="scss" scoped>
.address-edit-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.form-section {
  background-color: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.form-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }
}

.form-label {
  font-size: 28rpx;
  color: #333;
  min-width: 150rpx;
}

.form-input {
  flex: 1;
  font-size: 28rpx;
  color: #333;
  text-align: right;
}

.form-value {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 10rpx;
}

.region-text {
  font-size: 28rpx;
  color: #333;
}

.placeholder {
  font-size: 28rpx;
  color: #999;
}

.arrow {
  font-size: 40rpx;
  color: #999;
}

.form-textarea {
  flex: 1;
  min-height: 120rpx;
  font-size: 28rpx;
  color: #333;
  text-align: right;
}

.input-placeholder {
  color: #999;
}

.save-btn {
  width: calc(100% - 40rpx);
  margin: 40rpx 20rpx 20rpx;
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  border-radius: 40rpx;
  padding: 28rpx;
  font-size: 32rpx;
  border: none;
}
</style>
