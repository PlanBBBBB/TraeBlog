<template>
  <nav class="navbar" :class="{ 'navbar-hidden': isNavbarHidden }">
    <div class="nav-container">
      <div class="nav-logo">
        <router-link to="/">PlanB's Blog</router-link>
      </div>
      <div class="nav-right">
        <ul class="nav-menu">
          <li><router-link to="/">主页</router-link></li>
          <li><router-link to="/articles">文章</router-link></li>
          <li><router-link to="/tags">标签</router-link></li>
          <li><router-link to="/categories">分类</router-link></li>
          <li><router-link to="/links">友情链接</router-link></li>
          <li><router-link to="/about">关于</router-link></li>
        </ul>
        <button class="theme-toggle" @click="toggleTheme" :title="buttonTitle">
          <!-- 亮色模式：显示太阳图标 -->
          <svg v-if="themeMode === 'light'" class="theme-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="5"></circle>
            <line x1="12" y1="1" x2="12" y2="3"></line>
            <line x1="12" y1="21" x2="12" y2="23"></line>
            <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"></line>
            <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"></line>
            <line x1="1" y1="12" x2="3" y2="12"></line>
            <line x1="21" y1="12" x2="23" y2="12"></line>
            <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"></line>
            <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"></line>
          </svg>
          <!-- 暗色模式：显示月亮图标 -->
          <svg v-else-if="themeMode === 'dark'" class="theme-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"></path>
          </svg>
          <!-- 跟随系统：显示系统图标 -->
          <svg v-else class="theme-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
            <line x1="8" y1="21" x2="16" y2="21"></line>
            <line x1="12" y1="17" x2="12" y2="21"></line>
          </svg>
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'

// 三种状态：'light', 'dark', 'system'
const themeMode = ref('light')
let mediaQuery

// 导航栏隐藏相关
const isNavbarHidden = ref(false)
let lastScrollY = 0
let ticking = false

const buttonTitle = computed(() => {
  if (themeMode.value === 'light') {
    return '亮色模式'
  } else if (themeMode.value === 'dark') {
    return '暗色模式'
  } else {
    return '跟随系统'
  }
})

const getSystemTheme = () => {
  mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
  return mediaQuery.matches ? 'dark' : 'light'
}

const getActualTheme = () => {
  if (themeMode.value === 'system') {
    return getSystemTheme()
  }
  return themeMode.value
}

const initTheme = () => {
  const savedMode = localStorage.getItem('themeMode')
  if (savedMode) {
    themeMode.value = savedMode
  } else {
    themeMode.value = 'system'
  }
  applyTheme()
}

const applyTheme = () => {
  const actualTheme = getActualTheme()
  document.documentElement.setAttribute('data-theme', actualTheme)
}

const toggleTheme = () => {
  // 循环切换: light -> dark -> system -> light
  if (themeMode.value === 'light') {
    themeMode.value = 'dark'
  } else if (themeMode.value === 'dark') {
    themeMode.value = 'system'
  } else {
    themeMode.value = 'light'
  }
  localStorage.setItem('themeMode', themeMode.value)
  applyTheme()
}

const handleSystemChange = () => {
  if (themeMode.value === 'system') {
    applyTheme()
  }
}

// 处理滚动，隐藏/显示导航栏
const handleScroll = () => {
  if (!ticking) {
    window.requestAnimationFrame(() => {
      const currentScrollY = window.scrollY
      
      // 向下滚动且滚动距离大于100px时隐藏导航栏
      if (currentScrollY > lastScrollY && currentScrollY > 100) {
        isNavbarHidden.value = true
      } else {
        // 向上滚动时显示导航栏
        isNavbarHidden.value = false
      }
      
      lastScrollY = currentScrollY
      ticking = false
    })
    ticking = true
  }
}

onMounted(() => {
  initTheme()
  mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
  mediaQuery.addEventListener('change', handleSystemChange)
  // 添加滚动监听
  window.addEventListener('scroll', handleScroll, { passive: true })
})

onUnmounted(() => {
  if (mediaQuery) {
    mediaQuery.removeEventListener('change', handleSystemChange)
  }
  // 移除滚动监听
  window.removeEventListener('scroll', handleScroll)
})
</script>

<style scoped>
.navbar {
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  left: 0;
  right: 0;
  width: 100%;
  z-index: 1000;
  margin: 0;
  padding: 0;
  transition: background 0.3s, box-shadow 0.3s, transform 0.3s ease-in-out;
}

.navbar-hidden {
  transform: translateY(-100%);
}

:global([data-theme="dark"]) .navbar {
  background: rgba(27, 27, 31, 0.95);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
  top: 0 !important;
  margin-top: 0 !important;
  padding-top: 0 !important;
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.nav-logo a {
  font-size: 1.5rem;
  font-weight: bold;
  color: #667eea;
  text-decoration: none;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.nav-menu {
  display: flex;
  list-style: none;
  gap: 30px;
}

.nav-menu li a {
  color: #333;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .nav-menu li a {
  color: #ffffff;
}

.nav-menu li a:hover,
.nav-menu li a.router-link-active {
  color: #667eea;
}

.theme-toggle {
  background: #f5f7fa;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

:global([data-theme="dark"]) .theme-toggle {
  background: #2a2a4e;
}

.theme-toggle:hover {
  background: #f5f7fa;
}

:global([data-theme="dark"]) .theme-toggle:hover {
  background: #2a2a4e;
}

.theme-icon {
  width: 20px;
  height: 20px;
  color: #667eea;
  transition: transform 0.3s ease;
}

:global([data-theme="dark"]) .theme-icon {
  color: #667eea;
}

.theme-toggle:hover .theme-icon {
  transform: rotate(15deg);
}
</style>
