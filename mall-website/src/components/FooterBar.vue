<template>
  <footer class="footer-bar">
    <div class="footer-container">
      <div class="footer-links">
        <router-link to="/">首页</router-link>
        <span class="separator">|</span>
        <router-link to="/seckill">秒杀</router-link>
        <span class="separator">|</span>
        <router-link to="/user/coupons">优惠券</router-link>
      </div>
      <div class="footer-copyright">
        <p>&copy; 2026 Mall-HJ. All rights reserved.</p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
</script>

<style scoped lang="scss">
.footer-bar {
  background-color: #23262e;
  color: #999;
  padding: 30px 0;
  margin-top: 40px;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  text-align: center;
}

.footer-links {
  margin-bottom: 15px;

  a {
    color: #999;
    text-decoration: none;
    margin: 0 10px;

    &:hover {
      color: #fff;
    }
  }

  .separator {
    color: #555;
    margin: 0 5px;
  }
}

.footer-copyright {
  p {
    margin: 0;
    font-size: 14px;
  }
}
</style>
