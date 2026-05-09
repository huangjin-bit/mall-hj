<template>
  <div class="home-page">
    <!-- 轮播图 -->
    <div class="banner-section">
      <el-carousel height="400px" indicator-position="outside">
        <el-carousel-item v-for="adv in advertisements" :key="adv.id">
          <a :href="adv.linkUrl || '#'" target="_blank">
            <img :src="adv.imageUrl" :alt="adv.name" class="banner-image" />
          </a>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!-- 分类导航和热销商品 -->
    <div class="main-section">
      <div class="section-left">
        <CategoryNav />
      </div>

      <div class="section-right">
        <!-- 热销商品 -->
        <div class="hot-products">
          <h2 class="section-title">热销商品</h2>
          <div class="product-grid">
            <ProductCard
              v-for="product in hotProducts"
              :key="product.id"
              :product="product"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- 秒杀专区 -->
    <div class="seckill-section">
      <h2 class="section-title">限时秒杀</h2>
      <div v-if="seckillSessions.length > 0" class="seckill-sessions">
        <div
          v-for="session in seckillSessions"
          :key="session.id"
          class="seckill-session"
        >
          <div class="session-header">
            <h3>{{ session.name }}</h3>
            <div class="session-time">
              <span>距结束：</span>
              <CountdownTimer :end-time="session.endTime" />
            </div>
          </div>
          <div class="session-products">
            <ProductCard
              v-for="sku in getSessionSkus(session.id)"
              :key="sku.id"
              :product="{
                ...sku,
                spuName: sku.spuName,
                spuId: sku.spuId,
                id: sku.id
              }"
            />
          </div>
        </div>
      </div>
      <EmptyState v-else message="暂无秒杀活动" />
    </div>

    <!-- 专题推荐 -->
    <div class="subject-section">
      <h2 class="section-title">专题推荐</h2>
      <div class="subject-grid">
        <div
          v-for="subject in subjects"
          :key="subject.id"
          class="subject-item"
        >
          <router-link v-if="subject.linkUrl" :to="subject.linkUrl">
            <img :src="subject.imageUrl || ''" :alt="subject.name" class="subject-image" />
            <div class="subject-name">{{ subject.name }}</div>
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import CategoryNav from '@/components/CategoryNav.vue'
import ProductCard from '@/components/ProductCard.vue'
import CountdownTimer from '@/components/CountdownTimer.vue'
import EmptyState from '@/components/EmptyState.vue'
import { getActiveAdvs } from '@/api/seckill/homeAdv'
import { getSpuList } from '@/api/product/spu'
import { getActiveSessions, getSessionSkus } from '@/api/seckill/seckillSession'
import { getActiveSubjects } from '@/api/seckill/homeSubject'
import type { HomeAdv } from '@/api/seckill/homeAdv'
import type { Sku } from '@/api/product/spu'
import type { SeckillSession, SeckillSku } from '@/api/seckill/seckillSession'
import type { HomeSubject } from '@/api/seckill/homeSubject'

const advertisements = ref<HomeAdv[]>([])
const hotProducts = ref<(Sku & { spuName: string; spuId: number })[]>([])
const seckillSessions = ref<SeckillSession[]>([])
const sessionSkuMap = ref<Map<number, SeckillSku[]>>(new Map())
const subjects = ref<HomeSubject[]>([])

// 加载广告
const loadAdvertisements = async () => {
  try {
    const res = await getActiveAdvs()
    advertisements.value = res.data || []
  } catch (error) {
    console.error('[Home] Load advertisements failed', error)
  }
}

// 加载热销商品
const loadHotProducts = async () => {
  try {
    const res = await getSpuList({ page: 1, size: 8 })
    if (res.data?.records) {
      // 获取每个 SPU 的第一个 SKU
      hotProducts.value = res.data.records
        .filter(spu => spu.skus && spu.skus.length > 0)
        .map(spu => ({
          ...spu.skus![0],
          spuName: spu.spuName,
          spuId: spu.id
        }))
    }
  } catch (error) {
    console.error('[Home] Load hot products failed', error)
  }
}

// 加载秒杀场次
const loadSeckillSessions = async () => {
  try {
    const res = await getActiveSessions()
    seckillSessions.value = res.data || []

    // 加载每个场次的商品
    for (const session of seckillSessions.value) {
      try {
        const skuRes = await getSessionSkus(session.id)
        sessionSkuMap.value.set(session.id, skuRes.data || [])
      } catch (error) {
        console.error(`[Home] Load session ${session.id} skus failed`, error)
      }
    }
  } catch (error) {
    console.error('[Home] Load seckill sessions failed', error)
  }
}

// 获取场次商品
const getSessionSkus = (sessionId: number) => {
  return sessionSkuMap.value.get(session.id) || []
}

// 加载专题
const loadSubjects = async () => {
  try {
    const res = await getActiveSubjects()
    subjects.value = res.data || []
  } catch (error) {
    console.error('[Home] Load subjects failed', error)
  }
}

onMounted(() => {
  loadAdvertisements()
  loadHotProducts()
  loadSeckillSessions()
  loadSubjects()
})
</script>

<style scoped lang="scss">
.home-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.banner-section {
  margin-bottom: 30px;

  .banner-image {
    width: 100%;
    height: 400px;
    object-fit: cover;
  }
}

.main-section {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: 20px;
  margin-bottom: 40px;
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

.hot-products {
  .product-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
  }
}

.seckill-section {
  margin-bottom: 40px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;

  .seckill-sessions {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
  }

  .seckill-session {
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    padding: 20px;

    .session-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;

      h3 {
        margin: 0;
        font-size: 18px;
        color: #333;
      }

      .session-time {
        display: flex;
        align-items: center;
        gap: 5px;
        font-size: 14px;
        color: #666;
      }
    }

    .session-products {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 15px;
    }
  }
}

.subject-section {
  .subject-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
  }

  .subject-item {
    a {
      display: block;
      background-color: #fff;
      border-radius: 8px;
      overflow: hidden;
      transition: all 0.3s;

      &:hover {
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        transform: translateY(-4px);
      }
    }

    .subject-image {
      width: 100%;
      height: 200px;
      object-fit: cover;
    }

    .subject-name {
      padding: 15px;
      text-align: center;
      font-size: 16px;
      color: #333;
    }
  }
}
</style>
