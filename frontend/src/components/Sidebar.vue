<template>
  <aside class="sidebar">
    <div class="sidebar-card">
      <div class="profile">
        <img :src="avatar" alt="头像" class="avatar" />
        <h3>PlanB</h3>
        <p>0基础，学IT</p>
      </div>
    </div>

    <div class="sidebar-card">
      <div class="card-header">
        <span class="icon">🔗</span>
        <h4>联系方式</h4>
      </div>
      <div class="contact-links">
        <a href="#" class="contact-link">
          <svg class="contact-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
            <polyline points="14 2 14 8 20 8"/>
            <line x1="16" y1="13" x2="8" y2="13"/>
            <line x1="16" y1="17" x2="8" y2="17"/>
            <polyline points="10 9 9 9 8 9"/>
          </svg>
          <span>个人简历</span>
        </a>
        <a href="https://github.com/PlanBBBBB" target="_blank" class="contact-link">
          <svg class="contact-icon" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
            <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"/>
          </svg>
          <span>GitHub</span>
        </a>
        <a href="mailto:panchanghao0102@163.com" class="contact-link">
          <svg class="contact-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
            <rect x="2" y="4" width="20" height="16" rx="2"/>
            <path d="M22 7l-10 7L2 7"/>
          </svg>
          <span>Email</span>
        </a>
      </div>
    </div>

    <div class="sidebar-card">
      <div class="card-header">
        <span class="icon">📊</span>
        <h4>文章统计</h4>
      </div>
      <div class="stat-item">
        <router-link to="/articles" class="stat-number">{{ totalArticles }}</router-link>
        <router-link to="/articles" class="stat-label">文章</router-link>
      </div>
      <div class="stat-item">
        <router-link to="/tags" class="stat-number">{{ tags.length }}</router-link>
        <router-link to="/tags" class="stat-label">标签</router-link>
      </div>
      <div class="stat-item">
        <router-link to="/categories" class="stat-number">{{ categories.length }}</router-link>
        <router-link to="/categories" class="stat-label">分类</router-link>
      </div>
    </div>

    <div class="sidebar-card">
      <div class="card-header">
        <span class="icon">📁</span>
        <h4>分类</h4>
      </div>
      <div class="category-list">
        <div v-for="category in categories" :key="category.id" class="category-item">
          <router-link :to="'/category/' + category.name" class="category-name">{{ category.name }}</router-link>
          <span class="category-count">{{ getCategoryCount(category.name) }}</span>
        </div>
      </div>
    </div>

    <div class="sidebar-card">
      <div class="card-header">
        <span class="icon">🏷️</span>
        <h4>标签</h4>
      </div>
      <div class="tag-list">
        <router-link v-for="tag in tags" :key="tag.id" :to="'/tag/' + tag.name" class="tag-item">
          {{ tag.name }}
        </router-link>
      </div>
    </div>

    <div v-if="showOutline && articleOutline.length > 0" class="sidebar-card outline-card">
      <div class="card-header">
        <span class="icon">📑</span>
        <h4>文章大纲</h4>
      </div>
      <div class="outline-list">
        <div
          v-for="(item, index) in articleOutline"
          :key="index"
          class="outline-item"
          :class="'outline-h' + item.level"
          @click="scrollToHeading(item.id)"
        >
          <span class="outline-number">{{ item.number }}.</span>
          <span class="outline-text">{{ item.text }}</span>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import avatar from '../assets/avatar.jpg'
import { categoryApi, tagApi, articleApi } from '../api/index'

const props = defineProps({
  articleContent: {
    type: String,
    default: ''
  },
  isLocked: {
    type: Boolean,
    default: false
  },
  isUnlocked: {
    type: Boolean,
    default: false
  }
})

const categories = ref([])
const tags = ref([])
const allArticles = ref([])
const totalArticles = ref(0)
const articleOutline = ref([])

const showOutline = computed(() => {
  return !props.isLocked || props.isUnlocked
})

const parseMarkdownOutline = (content) => {
  if (!content) return []
  
  const lines = content.split('\n')
  const outline = []
  const counters = [0, 0, 0, 0, 0, 0] // 用于追踪各级标题的计数
  let idCounter = 0
  
  lines.forEach(line => {
    const trimmedLine = line.trim()
    if (trimmedLine.startsWith('#')) {
      const match = trimmedLine.match(/^(#{1,6})\s*(.+)$/)
      if (match) {
        const level = match[1].length
        const text = match[2].trim()
        const id = `heading-${idCounter++}`
        
        // 重置当前层级以下的计数器
        for (let i = level; i < 6; i++) {
          counters[i] = 0
        }
        // 增加当前层级的计数器
        counters[level - 1]++
        
        // 生成编号，例如：1.1.1
        let number = ''
        for (let i = 0; i < level; i++) {
          number += counters[i]
          if (i < level - 1) {
            number += '.'
          }
        }
        
        outline.push({
          id,
          level,
          text,
          number
        })
      }
    }
  })
  
  return outline
}

const addIdsToHeadings = (content) => {
  if (!content) return content
  
  let idCounter = 0
  return content.replace(/^(#{1,6})\s*(.+)$/gm, (match, hashes, text) => {
    const id = `heading-${idCounter++}`
    const level = hashes.length
    return `<h${level} id="${id}">${text}</h${level}>`
  })
}

const scrollToHeading = (id) => {
  const element = document.getElementById(id)
  if (element) {
    const rect = element.getBoundingClientRect()
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop
    const targetTop = rect.top + scrollTop - 65 // 减少偏移量，让位置更靠上
    
    // 计算页面的最大可滚动高度
    const maxScrollTop = document.documentElement.scrollHeight - window.innerHeight
    
    // 如果目标位置超过了最大可滚动高度，则滚动到底部
    const finalTop = Math.min(targetTop, maxScrollTop)
    
    window.scrollTo({
      top: finalTop,
      behavior: 'smooth'
    })
  }
}

watch(() => props.articleContent, (newContent) => {
  articleOutline.value = parseMarkdownOutline(newContent)
})

const loadData = async () => {
  try {
    const [catRes, tagRes, artRes] = await Promise.all([
      categoryApi.list(),
      tagApi.list(),
      articleApi.list(1, 1000)
    ])
    categories.value = catRes.data
    tags.value = tagRes.data
    allArticles.value = artRes.data.records || []
    totalArticles.value = artRes.data.total || 0
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

const getCategoryCount = (categoryName) => {
  return allArticles.value.filter(a => a.category === categoryName).length
}

onMounted(() => {
  loadData()
  articleOutline.value = parseMarkdownOutline(props.articleContent)
})
</script>

<style scoped>
.sidebar {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: background 0.3s, box-shadow 0.3s;
}

:global([data-theme="dark"]) .sidebar-card {
  background: #1b1b1f;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.profile {
  text-align: center;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  margin: 0 auto 15px;
}

.profile h3 {
  color: #333;
  margin: 0 0 5px 0;
  font-size: 1.3rem;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .profile h3 {
  color: #ffffff;
}

.profile p {
  color: #999;
  font-size: 0.9rem;
  margin: 0;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .profile p {
  color: #cccccc;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
  transition: border-color 0.3s;
}

:global([data-theme="dark"]) .card-header {
  border-bottom-color: #2a2a4e;
}

.card-header .icon {
  font-size: 1.2rem;
}

.card-header h4 {
  margin: 0;
  color: #333;
  font-size: 1.1rem;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .card-header h4 {
  color: #eaeaea;
}

.stat-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
}

.stat-number {
  font-weight: bold;
  color: #667eea;
  font-size: 1.2rem;
  text-decoration: none;
}

.stat-number:hover {
  text-decoration: underline;
}

.stat-label {
  color: #666;
  text-decoration: none;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .stat-label {
  color: #b8b8b8;
}

.stat-label:hover {
  text-decoration: underline;
  color: #667eea;
}

.contact-links {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.contact-link {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 10px 15px;
  background: #f5f7fa;
  border-radius: 5px;
  text-decoration: none;
  color: #667eea;
  transition: background 0.3s;
  text-align: center;
}

.contact-icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

:global([data-theme="dark"]) .contact-link {
  background: #2a2a4e;
}

.contact-link:hover {
  background: #667eea;
  color: white;
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.category-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.category-name {
  text-decoration: none;
  color: #333;
  transition: color 0.3s;
  font-size: 1rem;
}

:global([data-theme="dark"]) .category-name {
  color: #eaeaea;
}

.category-name:hover {
  color: #667eea;
}

.category-count {
  color: #999;
  font-size: 0.95rem;
  font-weight: 500;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .category-count {
  color: #888;
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 6px 14px;
  background: #f5f7fa;
  border-radius: 20px;
  text-decoration: none;
  color: #666;
  font-size: 0.9rem;
  transition: background 0.3s, color 0.3s;
}

:global([data-theme="dark"]) .tag-item {
  background: #2a2a4e !important;
  color: #b8b8b8 !important;
}

.tag-item:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.outline-list {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.outline-item {
  cursor: pointer;
  padding: 6px 10px;
  border-radius: 4px;
  color: #666;
  transition: color 0.3s, background 0.3s;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

:global([data-theme="dark"]) .outline-item {
  color: #b8b8b8;
}

.outline-item:hover {
  color: #7c5cf0;
  background: #f0e6f9;
}

:global([data-theme="dark"]) .outline-item:hover {
  background: #3d345e;
  color: #b89fff;
}

.outline-h1 {
  font-weight: bold;
  font-size: 1rem;
  padding-left: 0;
}

.outline-h2 {
  font-weight: 600;
  font-size: 0.95rem;
  padding-left: 12px;
}

.outline-h3 {
  font-size: 0.9rem;
  padding-left: 24px;
}

.outline-h4 {
  font-size: 0.85rem;
  padding-left: 36px;
}

.outline-h5 {
  font-size: 0.8rem;
  padding-left: 48px;
}

.outline-h6 {
  font-size: 0.75rem;
  padding-left: 60px;
}

.outline-number {
  display: inline-block;
  min-width: 40px;
  margin-right: 8px;
  color: #667eea;
  font-weight: 500;
  user-select: none;
}

:global([data-theme="dark"]) .outline-number {
  color: #8a92ea;
}

.outline-text {
  display: inline;
}
</style>
