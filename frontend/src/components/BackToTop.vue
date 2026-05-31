<template>
  <div
    class="back-to-top"
    v-show="visible"
    @click="scrollToTop"
  >
    <svg class="progress-ring" viewBox="0 0 100 100">
      <circle
        class="progress-ring-bg"
        cx="50"
        cy="50"
        r="45"
      />
      <circle
        class="progress-ring-progress"
        cx="50"
        cy="50"
        r="45"
        :stroke-dashoffset="strokeDashoffset"
        ref="progressCircle"
      />
    </svg>
    <svg class="arrow-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
      <path d="M12 19V5M5 12l7-7 7 7"/>
    </svg>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

const visible = ref(false)
const scrollProgress = ref(0)
const progressCircle = ref(null)

const circumference = 2 * Math.PI * 45

const strokeDashoffset = computed(() => {
  return circumference - (scrollProgress.value / 100) * circumference
})

const handleScroll = () => {
  const scrollTop = window.scrollY
  const docHeight = document.documentElement.scrollHeight - window.innerHeight
  scrollProgress.value = Math.min((scrollTop / docHeight) * 100, 100)
  visible.value = scrollTop > 300
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.back-to-top {
  position: fixed;
  right: 30px;
  bottom: 30px;
  width: 50px;
  height: 50px;
  cursor: pointer;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
}

.progress-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  transform: rotate(-90deg);
}

.progress-ring-bg {
  fill: none;
  stroke: #e8ebf0;
  stroke-width: 6;
}

:global([data-theme="dark"]) .progress-ring-bg {
  stroke: #2a2a4e;
}

.progress-ring-progress {
  fill: none;
  stroke: #667eea;
  stroke-width: 6;
  stroke-linecap: round;
  stroke-dasharray: 283;
  transition: stroke-dashoffset 0.1s;
}

.arrow-icon {
  position: absolute;
  width: 24px;
  height: 24px;
  color: #667eea;
  transition: all 0.3s;
}

.back-to-top:hover .arrow-icon {
  color: #667eea;
  transform: translateY(-2px);
}
</style>
