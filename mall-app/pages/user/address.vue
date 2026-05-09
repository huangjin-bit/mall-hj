<script setup>
import { ref, onMounted } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getAddressList, deleteAddress, setDefaultAddress } from '@/api/address'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

const addresses = ref([])
const selectMode = ref(false) // 是否为选择地址模式

onLoad((options) => {
  selectMode.value = options.select === '1'
})

onMounted(async () => {
  await loadAddresses()
})

async function loadAddresses() {
  try {
    const res = await getAddressList()
    addresses.value = res || []
  } catch (error) {
    console.error('[LoadAddresses Error]', error)
  }
}

async function handleSetDefault(addressId) {
  try {
    await setDefaultAddress(addressId)
    await loadAddresses()
    uni.showToast({ title: '设置成功', icon: 'success' })
  } catch (error) {
    console.error('[SetDefaultAddress Error]', error)
  }
}

async function handleDelete(addressId) {
  try {
    await uni.showModal({
      title: '提示',
      content: '确定删除该地址吗？'
    })

    await deleteAddress(addressId)
    await loadAddresses()
    uni.showToast({ title: '删除成功', icon: 'success' })
  } catch (error) {
    console.error('[DeleteAddress Error]', error)
  }
}

function handleEdit(address) {
  uni.navigateTo({
    url: `/pages/user/addressEdit?id=${address.id}`
  })
}

function handleAdd() {
  uni.navigateTo({
    url: '/pages/user/addressEdit'
  })
}

function handleSelectAddress(address) {
  if (selectMode.value) {
    const pages = getCurrentPages()
    const prevPage = pages[pages.length - 2]
    if (prevPage) {
      prevPage.$vm.selectedAddress = address
    }
    uni.navigateBack()
  }
}
</script>

<template>
  <view class="address-page">
    <view v-if="addresses.length > 0" class="address-list">
      <view
        v-for="address in addresses"
        :key="address.id"
        class="address-card"
        @tap="handleSelectAddress(address)"
      >
        <view class="address-header">
          <view class="receiver-info">
            <text class="receiver-name">{{ address.receiverName }}</text>
            <text class="receiver-phone">{{ address.receiverPhone }}</text>
          </view>
          <view v-if="address.isDefault" class="default-badge">默认</view>
        </view>

        <view class="address-detail">
          {{ address.province }}{{ address.city }}{{ address.district }}{{ address.detailAddress }}
        </view>

        <view class="address-actions" @tap.stop>
          <button v-if="!address.isDefault" class="action-btn default-btn" @tap="handleSetDefault(address.id)">
            设为默认
          </button>
          <button class="action-btn edit-btn" @tap="handleEdit(address)">编辑</button>
          <button class="action-btn delete-btn" @tap="handleDelete(address.id)">删除</button>
        </view>
      </view>
    </view>

    <view v-else class="empty-state">
      <text class="empty-icon">📍</text>
      <text class="empty-text">暂无收货地址</text>
    </view>

    <!-- 添加地址按钮 -->
    <view class="add-address-btn" @tap="handleAdd">
      <text class="add-icon">+</text>
      <text class="add-text">添加新地址</text>
    </view>
  </view>
</template>

<style lang="scss" scoped>
.address-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.address-list {
  padding: 20rpx;
}

.address-card {
  background-color: #fff;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
}

.address-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.receiver-info {
  display: flex;
  align-items: center;
  gap: 20rpx;
}

.receiver-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
}

.receiver-phone {
  font-size: 28rpx;
  color: #666;
}

.default-badge {
  background-color: #e93b3d;
  color: #fff;
  font-size: 24rpx;
  padding: 8rpx 16rpx;
  border-radius: 8rpx;
}

.address-detail {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
  margin-bottom: 20rpx;
}

.address-actions {
  display: flex;
  gap: 20rpx;
}

.action-btn {
  flex: 1;
  padding: 20rpx;
  border-radius: 8rpx;
  font-size: 28rpx;
  border: 1px solid #e0e0e0;
  background-color: #fff;
}

.default-btn {
  color: #e93b3d;
  border-color: #e93b3d;
}

.edit-btn {
  color: #666;
}

.delete-btn {
  color: #e93b3d;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 0;
}

.empty-icon {
  font-size: 120rpx;
  margin-bottom: 30rpx;
  opacity: 0.5;
}

.empty-text {
  font-size: 28rpx;
  color: #999;
}

.add-address-btn {
  position: fixed;
  bottom: 30rpx;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 10rpx;
  background: linear-gradient(135deg, #ff6b6b, #e93b3d);
  color: #fff;
  padding: 24rpx 60rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  box-shadow: 0 4rpx 16rpx rgba(233, 59, 61, 0.3);
}

.add-icon {
  font-size: 36rpx;
  font-weight: bold;
}
</style>
