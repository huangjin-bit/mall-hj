<template>
  <div class="website-layout">
    <TopNav />
    <main class="main-content">
      <router-view />
    </main>
    <FooterBar />
  </div>
</template>

<script setup lang="ts">
import TopNav from '@/components/TopNav.vue'
import FooterBar from '@/components/FooterBar.vue'
</script>

<style scoped lang="scss">
.website-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  background-color: #f5f5f5;
}
</style>
