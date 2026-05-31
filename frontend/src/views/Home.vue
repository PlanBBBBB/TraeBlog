<template>
  <div class="home">
    <div class="banner" :style="{ backgroundImage: 'url(' + homepageImg + ')' }">
      <h1>PlanB's Blog</h1>
      <p class="typing-text">{{ displayText }}</p>
    </div>
    <div class="content">
      <div class="main-content">
        <div class="article-list">
          <h2>最新文章</h2>
          <div v-if="loading" class="loading">加载中...</div>
          <template v-else>
            <router-link v-for="article in articlePage.records" :key="article.id" :to="'/article/' + article.id" class="article-card">
              <h3>
                <span v-if="article.isLocked" class="lock-icon">🔒</span>
                {{ article.title }}
              </h3>
              <div class="article-meta">
                <span class="date">{{ formatDate(article.createTime) }}</span>
                <span class="tags">{{ article.tags }}</span>
              </div>
              <p class="summary">{{ article.summary }}</p>
            </router-link>
            <div class="pagination-wrapper" v-if="articlePage.pages > 1">
              <el-pagination
                :current-page="currentPage"
                :page-count="articlePage.pages"
                :total="articlePage.total"
                layout="prev, pager, next"
                @current-change="handleCurrentChange"
              />
            </div>
          </template>
        </div>
      </div>
      <Sidebar />
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import Sidebar from '../components/Sidebar.vue'
import BackToTop from '../components/BackToTop.vue'
import { articleApi } from '../api/index'
import homepageImg from '../assets/homepage.png'

const articlePage = ref({ records: [], pages: 0, total: 0 })
const currentPage = ref(1)
const loading = ref(true)
const fullText = '0基础，学IT'
const displayText = ref('')
let typingIndex = 0
let isDeleting = false
let typingTimer = null

const startTyping = () => {
  if (!isDeleting) {
    if (typingIndex < fullText.length) {
      displayText.value += fullText[typingIndex]
      typingIndex++
      typingTimer = setTimeout(startTyping, 150)
    } else {
      typingTimer = setTimeout(() => {
        isDeleting = true
        startTyping()
      }, 2000)
    }
  } else {
    if (typingIndex > 0) {
      displayText.value = fullText.slice(0, typingIndex - 1)
      typingIndex--
      typingTimer = setTimeout(startTyping, 100)
    } else {
      typingTimer = setTimeout(() => {
        isDeleting = false
        startTyping()
      }, 1000)
    }
  }
}

const loadArticles = async () => {
  loading.value = true
  try {
    const res = await articleApi.list(currentPage.value, 6)
    articlePage.value = res.data
  } catch (error) {
    console.error('加载文章失败', error)
  }
  loading.value = false
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadArticles()
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

onMounted(() => {
  startTyping()
  loadArticles()
})

onUnmounted(() => {
  if (typingTimer) {
    clearTimeout(typingTimer)
  }
})
</script>

<style scoped>
.home {
  min-height: 100vh;
}

.banner {
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  color: white;
  padding: 120px 20px;
  text-align: center;
  position: relative;
}

.banner::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.15);
  z-index: 1;
}

.banner h1, .banner p {
  position: relative;
  z-index: 2;
}

.banner h1 {
  font-size: 3.5rem;
  margin-bottom: 15px;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2);
}

.banner p {
  font-size: 1.5rem;
  opacity: 0.9;
  height: 2.5rem;
  line-height: 2.5rem;
  margin: 0;
}

.typing-text {
  display: inline-block;
  min-height: 2.5rem;
  line-height: 2.5rem;
  vertical-align: top;
}

.content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 30px;
}

.article-list h2 {
  margin-bottom: 30px;
  color: #333;
  font-size: 1.5rem;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .article-list h2 {
  color: #ffffff;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #999;
}

.article-card {
  background: white;
  padding: 30px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s, box-shadow 0.3s, background 0.3s;
  text-decoration: none;
  display: block;
}

:global([data-theme="dark"]) .article-card {
  background: #1b1b1f;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.article-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

.article-card h3 {
  color: #333;
  margin-bottom: 15px;
  font-size: 1.3rem;
  transition: color 0.3s;
  display: flex;
  align-items: center;
  gap: 10px;
}

.lock-icon {
  font-size: 1rem;
}

:global([data-theme="dark"]) .article-card h3 {
  color: #eaeaea;
}

.article-card:hover h3 {
  color: #667eea;
}

.article-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.article-meta .date {
  color: #999;
  font-size: 0.9rem;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .article-meta .date {
  color: #888;
}

.article-meta .tags {
  color: #667eea;
  font-size: 0.9rem;
}

.article-card .summary {
  color: #666;
  line-height: 1.8;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .article-card .summary {
  color: #b8b8b8;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
  transition: border-color 0.3s;
}

:global([data-theme="dark"]) .pagination-wrapper {
  border-top-color: #2a2a4e;
}

@media (max-width: 768px) {
  .content {
    grid-template-columns: 1fr;
  }
}
</style>
