<template>
  <div class="tags">
    <div class="content">
      <h2>所有标签</h2>
      <div class="tag-cloud-container">
        <div class="tag-cloud">
          <router-link v-for="(tag, index) in tags" :key="tag.id" :to="'/tag/' + tag.name" class="tag-item"
            :style="{
              color: getTagColor(tag),
              fontSize: getTagSize(tag, index),
              top: getTagPosition(tag, index).top,
              left: getTagPosition(tag, index).left,
              '--tag-rotation': getTagRotation(tag)
            }">
            <span class="tag-name">{{ tag.name }}</span>
            <span class="tag-count" v-if="tagCount[tag.name]">({{ tagCount[tag.name] }})</span>
          </router-link>
        </div>
      </div>
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import BackToTop from '../components/BackToTop.vue'
import { tagApi, articleApi } from '../api/index'

const tags = ref([])
const allArticles = ref([])
const tagCount = ref({})

// 热词颜色数组
const tagColors = [
  '#667eea', '#764ba2', '#f093fb', '#f5576c', '#4facfe',
  '#00f2fe', '#43e97b', '#38f9d7', '#fa709a', '#fee140',
  '#ff9a9e', '#fecfef', '#a18cd1', '#fbc2eb', '#ffd89b',
  '#19547b', '#cc2b5e', '#753a88', '#2c3e50', '#4ca1af',
  '#e0c3fc', '#8ec5fc', '#667eea', '#764ba2'
]

// 获取标签颜色
const getTagColor = (tag) => {
  let hash = 0
  for (let i = 0; i < tag.name.length; i++) {
    hash = tag.name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return tagColors[Math.abs(hash) % tagColors.length]
}

// 获取标签字体大小 - 热门标签更大
const getTagSize = (tag, index) => {
  const minSize = 16
  const maxSize = 56
  const percentage = Math.min(index / (tags.value.length * 0.2), 1)
  const size = maxSize - (percentage * (maxSize - minSize))
  return Math.max(size, minSize) + 'px'
}

// 获取标签旋转角度
const getTagRotation = (tag) => {
  let hash = 0
  for (let i = 0; i < tag.name.length; i++) {
    hash = tag.name.charCodeAt(i) + ((hash << 5) - hash)
  }
  const rotation = (Math.abs(hash) % 50) - 25
  return rotation + 'deg'
}

// 获取标签位置 - 从中心向外扩散
const getTagPosition = (tag, index) => {
  let hash = 0
  for (let i = 0; i < tag.name.length; i++) {
    hash = tag.name.charCodeAt(i) + ((hash << 5) - hash)
  }
  
  const totalTags = tags.value.length
  const corePercentage = Math.min(index / (totalTags * 0.2), 1)
  const radius = 5 + (corePercentage * 40)
  const angle = (index * 137.5) + (Math.abs(hash) % 360)
  const radian = (angle * Math.PI) / 180
  const centerX = 50
  const centerY = 50
  
  const top = centerY + Math.sin(radian) * radius
  const left = centerX + Math.cos(radian) * radius
  
  return { top: top + '%', left: left + '%' }
}

// 加载标签和文章
const loadData = async () => {
  try {
    const tagRes = await tagApi.list()
    tags.value = tagRes.data

    const articleRes = await articleApi.list(0, 1000)
    allArticles.value = articleRes.data.records || []

    const countMap = {}
    allArticles.value.forEach(article => {
      if (article.tags) {
        const articleTags = article.tags.split(',').map(t => t.trim())
        articleTags.forEach(t => {
          countMap[t] = (countMap[t] || 0) + 1
        })
      }
    })
    tagCount.value = countMap

    tags.value.sort((a, b) => (countMap[b.name] || 0) - (countMap[a.name] || 0))
  } catch (error) {
    console.error('加载数据失败', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.tags {
  min-height: 100vh;
  background: var(--bg-primary);
  transition: background 0.3s;
}

.content {
  max-width: 1100px;
  margin: 0 auto;
  padding: 50px 20px;
}

.content h2 {
  margin-bottom: 20px;
  color: var(--text-primary);
  font-size: 1.7rem;
  text-align: center;
  transition: color 0.3s;
}

.tag-cloud-container {
  width: 100%;
  height: 600px;
  position: relative;
  border-radius: 20px;
  background: var(--bg-secondary);
  box-shadow: 0 4px 20px var(--shadow-color);
  overflow: hidden;
  transition: background 0.3s, box-shadow 0.3s;
}

.tag-cloud {
  position: absolute;
  width: 100%;
  height: 100%;
}

.tag-item {
  position: absolute;
  text-decoration: none;
  font-weight: 800;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
  cursor: pointer;
  transition: transform 0.3s, text-shadow 0.3s, opacity 0.3s;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  padding: 8px 16px;
  border-radius: 25px;
  transform: translate(-50%, -50%) rotate(var(--tag-rotation));
}

.tag-item:hover {
  transform: translate(-50%, -50%) rotate(var(--tag-rotation)) scale(1.15);
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
  opacity: 0.95;
  z-index: 10;
}

.tag-name {
  line-height: 1.2;
}

.tag-count {
  font-size: 0.55em;
  opacity: 0.75;
  font-weight: 600;
}
</style>
