<template>
  <div class="member-detail-container">
    <el-card>
      <template #header>
        <div class="flex-between">
          <span>会员详情</span>
          <el-button @click="$router.back()">返回</el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户名">{{ memberData.username }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ memberData.phone }}</el-descriptions-item>
            <el-descriptions-item label="昵称">{{ memberData.nickname }}</el-descriptions-item>
            <el-descriptions-item label="会员等级">{{ memberData.levelName }}</el-descriptions-item>
            <el-descriptions-item label="积分">{{ memberData.integration }}</el-descriptions-item>
            <el-descriptions-item label="成长值">{{ memberData.growth }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <StatusTag :status="memberData.status" type="status" />
            </el-descriptions-item>
            <el-descriptions-item label="注册时间">{{ memberData.createTime }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>

        <el-tab-pane label="登录日志" name="loginLog">
          <el-table :data="loginLogs" border>
            <el-table-column prop="loginIp" label="登录IP" />
            <el-table-column prop="loginDevice" label="登录设备" />
            <el-table-column prop="createTime" label="登录时间" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="积分记录" name="integration">
          <el-table :data="integrationRecords" border>
            <el-table-column prop="integration" label="积分变化" width="100">
              <template #default="{ row }">
                <span :style="{ color: row.type === 1 ? 'green' : 'red' }">
                  {{ row.type === 1 ? '+' : '-' }}{{ row.integration }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="原因" />
            <el-table-column prop="createTime" label="时间" />
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="成长记录" name="growth">
          <el-table :data="growthRecords" border>
            <el-table-column prop="growth" label="成长值变化" width="100">
              <template #default="{ row }">
                <span :style="{ color: row.type === 1 ? 'green' : 'red' }">
                  {{ row.type === 1 ? '+' : '-' }}{{ row.growth }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="reason" label="原因" />
            <el-table-column prop="createTime" label="时间" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getMemberById } from '@/api/member/member'
import { getLoginLogList } from '@/api/member/loginLog'
import { getIntegrationRecordList } from '@/api/member/integrationRecord'
import { getGrowthRecordList } from '@/api/member/growthRecord'
import StatusTag from '@/components/StatusTag.vue'

const route = useRoute()
const activeTab = ref('basic')
const memberData = ref<any>({})
const loginLogs = ref<any[]>([])
const integrationRecords = ref<any[]>([])
const growthRecords = ref<any[]>([])

onMounted(async () => {
  try {
    const memberId = Number(route.params.id)
    memberData.value = await getMemberById(memberId)

    const [logData, integrationData, growthData] = await Promise.all([
      getLoginLogList({ memberId, current: 1, size: 20 }),
      getIntegrationRecordList({ memberId, current: 1, size: 20 }),
      getGrowthRecordList({ memberId, current: 1, size: 20 })
    ])

    loginLogs.value = logData.records
    integrationRecords.value = integrationData.records
    growthRecords.value = growthData.records
  } catch (error) {
    console.error('[Member Detail] Failed to load data:', error)
  }
})
</script>

<style scoped lang="scss">
.member-detail-container {
  :deep(.el-tabs__content) {
    padding-top: 20px;
  }
}
</style>
