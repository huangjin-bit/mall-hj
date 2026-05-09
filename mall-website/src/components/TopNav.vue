<template>
  <div class="top-nav">
    <div class="nav-container">
      <div class="nav-left">
        <router-link to="/" class="logo">
          <h1>Mall-HJ</h1>
        </router-link>
      </div>

      <div class="nav-center">
        <div class="search-bar">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品"
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button :icon="Search" @click="handleSearch" />
            </template>
          </el-input>
        </div>
      </div>

      <div class="nav-right">
        <router-link to="/cart" class="cart-link">
          <el-badge :value="cartStore.totalCount" :hidden="cartStore.totalCount === 0">
            <el-icon :size="24"><ShoppingCart /></el-icon>
          </el-badge>
        </router-link>

        <div class="user-menu">
          <template v-if="userStore.isLoggedIn">
            <el-dropdown>
              <span class="user-name">
                {{ userStore.username || '用户' }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/user')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/order/list')">我的订单</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="auth-link">登录</router-link>
            <span class="separator">|</span>
            <router-link to="/register" class="auth-link">注册</router-link>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Search, ArrowDown, ShoppingCart } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const searchKeyword = ref('')

// 初始化时获取购物车数据
if (userStore.isLoggedIn) {
  cartStore.fetchCart()
}

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({
      name: 'Search',
      query: { keyword: searchKeyword.value }
    })
  }
}

const handleLogout = async () => {
  await userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/')
}
</script>

<style scoped lang="scss">
.top-nav {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.nav-left {
  .logo {
    text-decoration: none;
    color: #409eff;

    h1 {
      font-size: 24px;
      margin: 0;
    }
  }
}

.nav-center {
  flex: 1;
  max-width: 500px;
  margin: 0 40px;

  .search-bar {
    width: 100%;
  }
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;

  .cart-link {
    display: flex;
    align-items: center;
    color: #333;
    text-decoration: none;
    cursor: pointer;

    &:hover {
      color: #409eff;
    }
  }

  .user-menu {
    .user-name {
      cursor: pointer;
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #333;

      &:hover {
        color: #409eff;
      }
    }

    .auth-link {
      color: #333;
      text-decoration: none;
      font-size: 14px;

      &:hover {
        color: #409eff;
      }
    }

    .separator {
      margin: 0 8px;
      color: #ddd;
    }
  }
}
</style>
