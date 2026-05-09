<template>
  <div class="user-center-page">
    <div class="page-container">
      <div class="user-layout">
        <!-- 用户信息卡片 -->
        <div class="user-card">
          <div class="user-avatar">
            <el-avatar :size="80" :src="userStore.memberInfo?.avatar || undefined">
              {{ userStore.username?.charAt(0) }}
            </el-avatar>
          </div>
          <div class="user-info">
            <h2 class="user-name">{{ userStore.memberInfo?.nickname || userStore.username }}</h2>
            <p class="user-phone">{{ userStore.memberInfo?.phone || '未绑定手机' }}</p>
          </div>
        </div>

        <!-- 快捷入口 -->
        <div class="quick-links">
          <router-link to="/user/orders" class="link-item">
            <el-icon :size="30"><Document /></el-icon>
            <span>我的订单</span>
          </router-link>
          <router-link to="/user/coupons" class="link-item">
            <el-icon :size="30"><Ticket /></el-icon>
            <span>优惠券</span>
          </router-link>
          <router-link to="/user/collect" class="link-item">
            <el-icon :size="30"><Star /></el-icon>
            <span>我的收藏</span>
          </router-link>
          <router-link to="/user/address" class="link-item">
            <el-icon :size="30"><Location /></el-icon>
            <span>收货地址</span>
          </router-link>
        </div>

        <!-- 个人中心菜单 -->
        <div class="user-menu">
          <el-menu mode="vertical" :default-active="activeMenu" @select="handleMenuSelect">
            <el-menu-item index="profile">
              <el-icon><User /></el-icon>
              <span>个人资料</span>
            </el-menu-item>
            <el-menu-item index="address">
              <el-icon><Location /></el-icon>
              <span>收货地址</span>
            </el-menu-item>
            <el-menu-item index="collect">
              <el-icon><Star /></el-icon>
              <span>我的收藏</span>
            </el-menu-item>
            <el-menu-item index="coupons">
              <el-icon><Ticket /></el-icon>
              <span>我的优惠券</span>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { Document, Ticket, Star, Location, User } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = ref('profile')

const handleMenuSelect = (index: string) => {
  router.push(`/user/${index}`)
}

onMounted(async () => {
  // 加载用户信息
  await userStore.fetchMemberInfo()

  // 根据当前路由设置激活菜单
  const path = route.path
  if (path.includes('profile')) activeMenu.value = 'profile'
  else if (path.includes('address')) activeMenu.value = 'address'
  else if (path.includes('collect')) activeMenu.value = 'collect'
  else if (path.includes('coupons')) activeMenu.value = 'coupons'
})
</script>

<style scoped lang="scss">
.user-center-page {
  min-height: calc(100vh - 60px - 100px);
  padding: 30px 0;
}

.page-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 0 20px;
}

.user-layout {
  display: grid;
  grid-template-columns: 280px 1fr;
  gap: 20px;
}

.user-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  margin-bottom: 20px;

  .user-avatar {
    margin-bottom: 15px;
  }

  .user-name {
    margin: 0 0 10px 0;
    font-size: 20px;
    color: #333;
    font-weight: 500;
  }

  .user-phone {
    margin: 0;
    font-size: 14px;
    color: #999;
  }
}

.quick-links {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-bottom: 20px;

  .link-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    padding: 15px;
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    color: #333;
    text-decoration: none;
    transition: all 0.3s;

    &:hover {
      border-color: #409eff;
      color: #409eff;
      transform: translateY(-2px);
    }

    span {
      font-size: 14px;
    }
  }
}

.user-menu {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;

  :deep(.el-menu) {
    border-right: none;
  }
}
</style>
