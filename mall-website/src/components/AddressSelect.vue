<template>
  <div class="address-select">
    <el-radio-group v-model="selectedAddressId" @change="handleSelect">
      <div v-for="address in addresses" :key="address.id" class="address-item">
        <el-radio :label="address.id" border>
          <div class="address-content">
            <div class="address-header">
              <span class="receiver-name">{{ address.receiverName }}</span>
              <span class="receiver-phone">{{ address.receiverPhone }}</span>
              <el-tag v-if="address.isDefault" type="success" size="small">默认</el-tag>
            </div>
            <div class="address-detail">
              {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detailAddress }}
            </div>
          </div>
        </el-radio>
      </div>
    </el-radio-group>

    <el-button type="primary" @click="showAddDialog = true" :icon="Plus">
      新增收货地址
    </el-button>

    <!-- 新增地址对话框 -->
    <el-dialog v-model="showAddDialog" title="新增收货地址" width="500px">
      <el-form :model="newAddress" label-width="80px">
        <el-form-item label="收货人">
          <el-input v-model="newAddress.receiverName" placeholder="请输入收货人姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="newAddress.receiverPhone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="地区">
          <el-cascader
            v-model="regionCodes"
            :options="regionOptions"
            placeholder="请选择省市区"
            clearable
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input
            v-model="newAddress.detailAddress"
            type="textarea"
            :rows="3"
            placeholder="请输入详细地址"
          />
        </el-form-item>
        <el-form-item label="设为默认">
          <el-switch v-model="newAddress.isDefault" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleAddAddress">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { getAddressList, addAddress } from '@/api/member/address'
import { useUserStore } from '@/stores/user'
import type { MemberAddress } from '@/api/member/address'
import { ElMessage } from 'element-plus'

const emit = defineEmits<{
  select: [address: MemberAddress]
}>()

const userStore = useUserStore()
const addresses = ref<MemberAddress[]>([])
const selectedAddressId = ref<number>()
const showAddDialog = ref(false)

const newAddress = ref({
  receiverName: '',
  receiverPhone: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: false
})

const regionCodes = ref<string[]>([])

// 简化的地区数据（实际项目应该从后端获取或使用完整地区库）
const regionOptions = ref([
  {
    value: '110000',
    label: '北京市',
    children: [
      {
        value: '110100',
        label: '北京市',
        children: [
          { value: '110101', label: '东城区' },
          { value: '110102', label: '西城区' },
          { value: '110105', label: '朝阳区' }
        ]
      }
    ]
  },
  {
    value: '310000',
    label: '上海市',
    children: [
      {
        value: '310100',
        label: '上海市',
        children: [
          { value: '310101', label: '黄浦区' },
          { value: '310104', label: '徐汇区' },
          { value: '310105', label: '长宁区' }
        ]
      }
    ]
  }
])

const loadAddresses = async () => {
  try {
    const res = await getAddressList(userStore.memberId)
    addresses.value = res.data || []

    // 默认选中第一个地址
    if (addresses.value.length > 0) {
      const defaultAddr = addresses.value.find(addr => addr.isDefault) || addresses.value[0]
      selectedAddressId.value = defaultAddr.id
      emit('select', defaultAddr)
    }
  } catch (error) {
    console.error('[AddressSelect] Load addresses failed', error)
  }
}

const handleSelect = () => {
  const address = addresses.value.find(addr => addr.id === selectedAddressId.value)
  if (address) {
    emit('select', address)
  }
}

const handleAddAddress = async () => {
  if (!newAddress.value.receiverName || !newAddress.value.receiverPhone || !newAddress.value.detailAddress) {
    ElMessage.warning('请填写完整信息')
    return
  }

  try {
    await addAddress({
      ...newAddress.value,
      memberId: userStore.memberId,
      isDefault: newAddress.value.isDefault ? 1 : 0
    })
    ElMessage.success('添加成功')
    showAddDialog.value = false
    await loadAddresses()
  } catch (error) {
    console.error('[AddressSelect] Add address failed', error)
  }
}

onMounted(() => {
  loadAddresses()
})
</script>

<style scoped lang="scss">
.address-select {
  .address-item {
    margin-bottom: 10px;

    &:last-of-type {
      margin-bottom: 20px;
    }
  }

  .address-content {
    width: 100%;
    padding: 10px;
  }

  .address-header {
    margin-bottom: 8px;
    display: flex;
    align-items: center;
    gap: 10px;

    .receiver-name {
      font-weight: 500;
      color: #333;
    }

    .receiver-phone {
      color: #666;
      font-size: 14px;
    }
  }

  .address-detail {
    color: #999;
    font-size: 14px;
    line-height: 1.5;
  }
}
</style>
