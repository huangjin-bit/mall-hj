<template>
  <div class="countdown-timer">
    <span class="time-text">{{ formattedTime }}</span>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'

const props = defineProps<{
  endTime: string | number | Date
}>()

const emit = defineEmits<{
  finished: []
}>()

const currentTime = ref(Date.now())
const timer = ref<number>()

const targetTime = computed(() => {
  return new Date(props.endTime).getTime()
})

const remainingTime = computed(() => {
  const diff = targetTime.value - currentTime.value
  return Math.max(0, diff)
})

const formattedTime = computed(() => {
  const seconds = Math.floor(remainingTime.value / 1000)
  const hours = Math.floor(seconds / 3600)
  const minutes = Math.floor((seconds % 3600) / 60)
  const secs = seconds % 60

  const pad = (n: number) => String(n).padStart(2, '0')

  return `${pad(hours)}:${pad(minutes)}:${pad(secs)}`
})

const startTimer = () => {
  timer.value = window.setInterval(() => {
    currentTime.value = Date.now()

    if (remainingTime.value === 0) {
      stopTimer()
      emit('finished')
    }
  }, 1000)
}

const stopTimer = () => {
  if (timer.value) {
    clearInterval(timer.value)
    timer.value = undefined
  }
}

onMounted(() => {
  startTimer()
})

onUnmounted(() => {
  stopTimer()
})
</script>

<style scoped lang="scss">
.countdown-timer {
  display: inline-block;

  .time-text {
    font-size: 20px;
    font-weight: 500;
    color: #f56c6c;
    font-family: 'Courier New', monospace;
  }
}
</style>
