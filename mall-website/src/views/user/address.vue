<template>
  <div class="user-address-page">
    <div class="page-container">
      <h1 class="page-title">收货地址</h1>

      <div class="address-content">
        <!-- 地址列表 -->
        <div class="address-list">
          <div
            v-for="address in addresses"
            :key="address.id"
            class="address-card"
            :class="{ default: address.isDefault }"
          >
            <div class="address-info">
              <div class="address-header">
                <span class="receiver-name">{{ address.receiverName }}</span>
                <span class="receiver-phone">{{ address.receiverPhone }}</span>
                <el-tag v-if="address.isDefault" type="success" size="small">默认</el-tag>
              </div>
              <div class="address-detail">
                {{ address.province }} {{ address.city }} {{ address.district }} {{ address.detailAddress }}
              </div>
            </div>
            <div class="address-actions">
              <el-button link @click="handleEdit(address)">编辑</el-button>
              <el-button v-if="!address.isDefault" link type="primary" @click="handleSetDefault(address)">
                设为默认
              </el-button>
              <el-button link type="danger" @click="handleDelete(address)">删除</el-button>
            </div>
          </div>
        </div>

        <!-- 添加地址按钮 -->
        <div class="add-address">
          <el-button type="primary" :icon="Plus" @click="showAddDialog = true">
            新增收货地址
          </el-button>
        </div>
      </div>

      <!-- 添加/编辑地址对话框 -->
      <el-dialog
        v-model="showDialog"
        :title="editingAddress ? '编辑地址' : '新增地址'"
        width="500px"
      >
        <el-form :model="addressForm" label-width="80px">
          <el-form-item label="收货人">
            <el-input v-model="addressForm.receiverName" placeholder="请输入收货人姓名" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="addressForm.receiverPhone" placeholder="请输入手机号" />
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
              v-model="addressForm.detailAddress"
              type="textarea"
              :rows="3"
              placeholder="请输入详细地址"
            />
          </el-form-item>
          <el-form-item label="设为默认">
            <el-switch v-model="addressForm.isDefault" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="showDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { getAddressList, addAddress, updateAddress, deleteAddress, setDefaultAddress } from '@/api/member/address'
import { useUserStore } from '@/stores/user'
import type { MemberAddress } from '@/api/member/address'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()

const addresses = ref<MemberAddress[]>([])
const showDialog = ref(false)
const showAddDialog = ref(false)
const editingAddress = ref<MemberAddress | null>(null)

const addressForm = reactive({
  id: 0,
  receiverName: '',
  receiverPhone: '',
  province: '',
  city: '',
  district: '',
  detailAddress: '',
  isDefault: false
})

const regionCodes = ref<string[]>([])

// 简化的地区数据
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

// 加载地址列表
const loadAddresses = async () => {
  try {
    const res = await getAddressList(userStore.memberId)
    addresses.value = res.data || []
  } catch (error) {
    console.error('[UserAddress] Load addresses failed', error)
  }
}

// 重置表单
const resetForm = () => {
  addressForm.id = 0
  addressForm.receiverName = ''
  addressForm.receiverPhone = ''
  addressForm.province = ''
  addressForm.city = ''
  addressForm.district = ''
  addressForm.detailAddress = ''
  addressForm.isDefault = false
  regionCodes.value = []
  editingAddress.value = null
}

// 编辑地址
const handleEdit = (address: MemberAddress) => {
  editingAddress.value = address
  addressForm.id = address.id
  addressForm.receiverName = address.receiverName
  addressForm.receiverPhone = address.receiverPhone
  addressForm.province = address.province
  addressForm.city = address.city
  addressForm.district = address.district
  addressForm.detailAddress = address.detailAddress
  addressForm.isDefault = address.isDefault === 1
  showDialog.value = true
}

// 设置默认地址
const handleSetDefault = async (address: MemberAddress) => {
  try {
    await setDefaultAddress(address.id)
    ElMessage.success('设置成功')
    loadAddresses()
  } catch (error) {
    console.error('[UserAddress] Set default failed', error)
  }
}

// 删除地址
const handleDelete = async (address: MemberAddress) => {
  try {
    await ElMessageBox.confirm('确定要删除该地址吗？', '提示', {
      type: 'warning'
    })
    await deleteAddress(address.id)
    ElMessage.success('删除成功')
    loadAddresses()
  } catch (error) {
    // 用户取消操作
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!addressForm.receiverName || !addressForm.receiverPhone || !addressForm.detailAddress) {
    ElMessage.warning('请填写完整信息')
    return
  }

  try {
    const data = {
      ...addressForm,
      memberId: userStore.memberId,
      isDefault: addressForm.isDefault ? 1 : 0
    }

    if (editingAddress.value) {
      await updateAddress(data)
      ElMessage.success('修改成功')
    } else {
      await addAddress(data)
      ElMessage.success('添加成功')
    }

    showDialog.value = false
    resetForm()
    loadAddresses()
  } catch (error) {
    console.error('[UserAddress] Submit failed', error)
  }
}

// 监听添加按钮
// eslint-disable-next-line @typescript-eslint/no-unused-vars
const showAddDialogHandler = () => {
  resetForm()
  showDialog.value = true
}

onMounted(() => {
  loadAddresses()
})
</script>

<style scoped lang="scss">
.user-address-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.page-title {
  margin: 0 0 30px 0;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

.address-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 30px;
}

.address-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.address-card {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.3s;

  &.default {
    border-color: #67c23a;
    background-color: #f0f9ff;
  }

  &:hover {
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .address-info {
    margin-bottom: 15px;

    .address-header {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 10px;

      .receiver-name {
        font-weight: 500;
        color: #333;
      }

      .receiver-phone {
        font-size: 14px;
        color: #666;
      }
    }

    .address-detail {
      font-size: 14px;
      color: #666;
      line-height: 1.5;
    }
  }

  .address-actions {
    display: flex;
    gap: 15px;
    padding-top: 15px;
    border-top: 1px solid #f0f0f0;
  }
}

.add-address {
  text-align: center;
  padding: 20px 0;
}
</style>
