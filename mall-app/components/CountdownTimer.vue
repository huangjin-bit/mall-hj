<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  endTime: {
    type: [String, Number, Date],
    required: true
  }
})

const emit = defineEmits(['finished'])

const currentTime = ref(Date.now())
const timer = ref(null)

const remainingTime = computed(() => {
  const end = new Date(props.endTime).getTime()
  const remaining = Math.max(0, Math.floor((end - currentTime.value) / 1000))
  return remaining
})

const hours = computed(() => {
  return Math.floor(remainingTime.value / 3600)
})

const minutes = computed(() => {
  return Math.floor((remainingTime.value % 3600) / 60)
})

const seconds = computed(() => {
  return remainingTime.value % 60
})

const isFinished = computed(() => {
  return remainingTime.value <= 0
})

onMounted(() => {
  timer.value = setInterval(() => {
    currentTime.value = Date.now()

    if (isFinished.value) {
      clearInterval(timer.value)
      emit('finished')
    }
  }, 1000)
})

onUnmounted(() => {
  if (timer.value) {
    clearInterval(timer.value)
  }
})
</script>

<template>
  <view class="countdown-timer" :class="{ finished: isFinished }">
    <text v-if="!isFinished" class="time-text">
      {{ String(hours).padStart(2, '0') }}:{{ String(minutes).padStart(2, '0') }}:{{ String(seconds).padStart(2, '0') }}
    </text>
    <text v-else class="finished-text">已结束</text>
  </view>
</template>

<style lang="scss" scoped>
.countdown-timer {
  display: inline-flex;
  align-items: center;
  gap: 5rpx;
}

.time-text {
  font-family: 'Courier New', monospace;
  font-size: 28rpx;
  font-weight: bold;
  color: #e93b3d;

  .time-block {
    display: inline-block;
    background: linear-gradient(135deg, #ff6b6b, #e93b3d);
    color: #fff;
    padding: 4rpx 8rpx;
    border-radius: 8rpx;
    min-width: 40rpx;
    text-align: center;
  }

  .time-separator {
    color: #e93b3d;
    margin: 0 2rpx;
  }
}

.finished-text {
  font-size: 24rpx;
  color: #999;
}

.finished {
  opacity: 0.6;
}
</style>
