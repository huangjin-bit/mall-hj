<template>
  <div class="category-nav">
    <div class="category-header">
      <el-icon><Menu /></el-icon>
      <span>商品分类</span>
    </div>
    <div class="category-content">
      <div
        v-for="category in categoryTree"
        :key="category.id"
        class="category-item"
        @mouseenter="handleMouseEnter(category)"
        @mouseleave="handleMouseLeave"
        @click="handleCategoryClick(category)"
      >
        <span>{{ category.name }}</span>
        <el-icon class="arrow" :size="14"><ArrowRight /></el-icon>
      </div>
    </div>

    <!-- 子菜单 -->
    <div
      v-if="hoveredCategory && hoveredCategory.children"
      class="category-submenu"
      @mouseenter="handleSubmenuEnter"
      @mouseleave="handleMouseLeave"
    >
      <div class="submenu-content">
        <div
          v-for="child in hoveredCategory.children"
          :key="child.id"
          class="submenu-item"
        >
          <h4>{{ child.name }}</h4>
          <div class="submenu-children">
            <span
              v-for="grandchild in child.children"
              :key="grandchild.id"
              class="submenu-child-link"
              @click="handleCategoryClick(grandchild)"
            >
              {{ grandchild.name }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Menu, ArrowRight } from '@element-plus/icons-vue'
import { getCategoryTree } from '@/api/product/category'
import type { Category } from '@/api/product/category'

const router = useRouter()
const categoryTree = ref<Category[]>([])
const hoveredCategory = ref<Category | null>(null)
const submenuTimer = ref<number | null>(null)

const loadCategories = async () => {
  try {
    const res = await getCategoryTree()
    categoryTree.value = res.data || []
  } catch (error) {
    console.error('[CategoryNav] Load categories failed', error)
  }
}

const handleMouseEnter = (category: Category) => {
  if (submenuTimer.value) {
    clearTimeout(submenuTimer.value)
    submenuTimer.value = null
  }
  hoveredCategory.value = category
}

const handleMouseLeave = () => {
  submenuTimer.value = window.setTimeout(() => {
    hoveredCategory.value = null
  }, 200)
}

const handleSubmenuEnter = () => {
  if (submenuTimer.value) {
    clearTimeout(submenuTimer.value)
    submenuTimer.value = null
  }
}

const handleCategoryClick = (category: Category) => {
  router.push({
    name: 'ProductList',
    query: { categoryId: category.id }
  })
  hoveredCategory.value = null
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped lang="scss">
.category-nav {
  position: relative;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.category-header {
  padding: 12px 16px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #333;
  border-bottom: 1px solid #f0f0f0;
}

.category-content {
  max-height: 400px;
  overflow-y: auto;
}

.category-item {
  padding: 10px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  transition: background-color 0.2s;

  &:hover {
    background-color: #f5f5f5;
  }

  .arrow {
    color: #999;
  }
}

.category-submenu {
  position: absolute;
  left: 100%;
  top: 0;
  width: 600px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  margin-left: 10px;
}

.submenu-content {
  padding: 20px;
  max-height: 400px;
  overflow-y: auto;
}

.submenu-item {
  margin-bottom: 15px;

  &:last-child {
    margin-bottom: 0;
  }

  h4 {
    margin: 0 0 10px 0;
    font-size: 14px;
    color: #333;
    font-weight: 500;
  }
}

.submenu-children {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.submenu-child-link {
  font-size: 12px;
  color: #666;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: all 0.2s;

  &:hover {
    color: #409eff;
    background-color: #ecf5ff;
  }
}
</style>
