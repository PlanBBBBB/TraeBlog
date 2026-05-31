<template>
  <div class="article-detail">
    <div class="content">
      <div class="main-content">
        <div v-if="loading" class="loading">加载中...</div>
        <template v-else>
          <div v-if="article.isLocked && !isUnlocked" class="password-dialog">
            <div class="password-content">
              <div class="password-icon">🔒</div>
              <h3>这篇文章需要密码</h3>
              <p>这篇文章由于隐私保护需要验证密码才能查看，请输入正确的密码</p>
              <el-input
                v-model="passwordInput"
                type="password"
                placeholder="请输入密码"
                @keyup.enter="handleVerifyPassword"
                class="password-input"
              />
              <el-button type="primary" @click="handleVerifyPassword" class="verify-button">
                验证密码
              </el-button>
            </div>
          </div>
          <article v-else class="article-full">
            <h1 class="article-title">
              <span v-if="article.isLocked" class="lock-icon">🔒</span>
              {{ article.title }}
            </h1>
            <div class="article-meta">
              <span class="date">{{ formatDate(article.createTime) }}</span>
              <template v-for="tag in articleTags" :key="tag">
                <router-link :to="'/tag/' + encodeURIComponent(tag)" class="tags">{{ tag }}</router-link>
              </template>
              <router-link :to="'/category/' + encodeURIComponent(article.category)" class="category">{{ article.category }}</router-link>
            </div>
            <div class="article-content markdown-body" v-html="renderedContent" @click="handleImageClick"></div>
          </article>
        </template>
      </div>
      <Sidebar :article-content="article.content" :is-locked="article.isLocked" :is-unlocked="isUnlocked" />
    </div>
    <BackToTop />
    
    <!-- 图片预览弹窗 -->
    <div v-if="previewImage" class="image-preview" @click="closePreview">
      <div class="preview-content" @click.stop>
        <img :src="previewImage" alt="预览图片" />
        <div class="preview-close" @click="closePreview">×</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { marked } from 'marked'
import Sidebar from '../components/Sidebar.vue'
import BackToTop from '../components/BackToTop.vue'
import { articleApi } from '../api/index'
import { ElMessage } from 'element-plus'

const route = useRoute()
const article = ref({})
const loading = ref(true)
const passwordInput = ref('')
const isUnlocked = ref(false)
const previewImage = ref('')

marked.setOptions({
  gfm: true,
  breaks: true
})

const renderedContent = computed(() => {
  if (!article.value.content) return ''
  
  // 先正常渲染Markdown
  const html = marked.parse(article.value.content)
  
  // 然后给HTML中的标题添加ID
  let idCounter = 0
  // 匹配<h1>到<h6>标签，给它们添加ID
  let result = html.replace(/<h([1-6])>([^<]*)<\/h\1>/g, (match, level, text) => {
    const id = `heading-${idCounter++}`
    return `<h${level} id="${id}">${text}</h${level}>`
  })
  
  // 给代码块添加行号
  result = result.replace(/<pre><code(.*?)>([\s\S]*?)<\/code><\/pre>/g, (match, attrs, code) => {
    const lines = code.split('\n').filter(line => line.trim() !== '' || line === '')
    const numberedCode = lines.map((line, index) => {
      return `<span class="code-line">${line}</span>`
    }).join('\n')
    return `<pre class="code-block"><code${attrs}>${numberedCode}</code></pre>`
  })
  
  // 给图片添加样式类，方便识别
  result = result.replace(/<img /g, '<img class="article-image" style="cursor: pointer;" ')
  
  return result
})

const articleTags = computed(() => {
  return article.value.tags ? article.value.tags.split(',').map(t => t.trim()) : []
})

const handleImageClick = (event) => {
  const target = event.target
  if (target.tagName === 'IMG' && target.classList.contains('article-image')) {
    previewImage.value = target.src
  }
}

const closePreview = () => {
  previewImage.value = ''
}

const handleVerifyPassword = async () => {
  if (!passwordInput.value) {
    ElMessage.warning('请输入密码')
    return
  }
  try {
    await articleApi.verifyPassword(article.value.id, passwordInput.value)
    isUnlocked.value = true
    ElMessage.success('密码正确！')
  } catch (error) {
    ElMessage.error('密码错误，请重试')
  }
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const loadArticle = async () => {
  loading.value = true
  try {
    const res = await articleApi.detail(parseInt(route.params.id))
    article.value = res.data
  } catch (error) {
    console.error('加载文章失败', error)
  }
  loading.value = false
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  min-height: 100vh;
}

.content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 40px 20px;
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 30px;
  min-width: 0;
}

.main-content {
  min-width: 0;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #999;
}

.password-dialog {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.password-content {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  text-align: center;
  max-width: 400px;
  width: 100%;
  transition: background 0.3s, box-shadow 0.3s;
}

:global([data-theme="dark"]) .password-content {
  background: #1b1b1f;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.password-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.password-content h3 {
  color: #333;
  margin-bottom: 15px;
  font-size: 1.5rem;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .password-content h3 {
  color: #eaeaea;
}

.password-content p {
  color: #666;
  margin-bottom: 25px;
  line-height: 1.8;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .password-content p {
  color: #b8b8b8;
}

.password-input {
  margin-bottom: 20px;
}

.verify-button {
  width: 100%;
}

.article-full {
  background: white;
  padding: 40px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: background 0.3s, box-shadow 0.3s;
  overflow: hidden;
  min-width: 0;
}

:global([data-theme="dark"]) .article-full {
  background: #1b1b1f;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.article-title {
  color: #333;
  font-size: 2rem;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 2px solid #f0f0f0;
  transition: color 0.3s, border-color 0.3s;
  display: flex;
  align-items: center;
  gap: 10px;
}

:global([data-theme="dark"]) .article-title {
  color: #eaeaea;
  border-bottom-color: #2a2a4e;
}

.article-meta {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.article-meta .date {
  background: #f5f7fa;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  color: #999;
  transition: background 0.3s;
}

:global([data-theme="dark"]) .article-meta .date {
  background: transparent;
  color: #888;
  padding: 8px 0;
}

.article-meta .tags {
  background: #f5f7fa;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  color: #667eea;
  text-decoration: none;
  transition: background 0.3s;
}

:global([data-theme="dark"]) .article-meta .tags {
  background: transparent;
  padding: 8px 0;
}

.article-meta .category {
  background: #f5f7fa;
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 0.9rem;
  color: #764ba2;
  text-decoration: none;
  transition: background 0.3s;
}

:global([data-theme="dark"]) .article-meta .category {
  background: transparent;
  padding: 8px 0;
}

.markdown-body {
  color: #444;
  line-height: 2;
  font-size: 1.05rem;
  transition: color 0.3s;
  word-wrap: break-word;
  overflow-wrap: break-word;
  word-break: break-word;
}

:global([data-theme="dark"]) .markdown-body {
  color: #b8b8b8;
}

:deep(.markdown-body h1),
:deep(.markdown-body h2),
:deep(.markdown-body h3),
:deep(.markdown-body h4),
:deep(.markdown-body h5),
:deep(.markdown-body h6) {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
  line-height: 1.25;
  color: #333;
}

:global([data-theme="dark"]) :deep(.markdown-body h1),
:global([data-theme="dark"]) :deep(.markdown-body h2),
:global([data-theme="dark"]) :deep(.markdown-body h3),
:global([data-theme="dark"]) :deep(.markdown-body h4),
:global([data-theme="dark"]) :deep(.markdown-body h5),
:global([data-theme="dark"]) :deep(.markdown-body h6) {
  color: #eaeaea;
}

:deep(.markdown-body h1) {
  font-size: 2em;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.3em;
}

:global([data-theme="dark"]) :deep(.markdown-body h1) {
  border-bottom-color: #2a2a4e;
}

:deep(.markdown-body h2) {
  font-size: 1.5em;
  border-bottom: 1px solid #eee;
  padding-bottom: 0.3em;
}

:global([data-theme="dark"]) :deep(.markdown-body h2) {
  border-bottom-color: #2a2a4e;
}

:deep(.markdown-body h3) {
  font-size: 1.25em;
}

:deep(.markdown-body h4) {
  font-size: 1em;
}

:deep(.markdown-body p) {
  margin-top: 0;
  margin-bottom: 16px;
}

:deep(.markdown-body ul),
:deep(.markdown-body ol) {
  margin-top: 0;
  margin-bottom: 16px;
  padding-left: 2em;
}

:deep(.markdown-body li + li) {
  margin-top: 0.25em;
}

:deep(.markdown-body blockquote) {
  padding: 0 1em;
  color: #6a737d;
  border-left: 0.25em solid #dfe2e5;
  margin: 0 0 16px 0;
}

:global([data-theme="dark"]) :deep(.markdown-body blockquote) {
  color: #888;
  border-left-color: #2a2a4e;
}

:deep(.markdown-body code) {
  padding: 0.2em 0.4em;
  background-color: #f5f7fa;
  border-radius: 6px;
  font-family: SFMono-Regular, Consolas, monospace;
  font-size: 0.9em;
  color: #667eea;
}

:global([data-theme="dark"]) :deep(.markdown-body code) {
  background-color: #16213e;
  color: #667eea;
}



:deep(.markdown-body table) {
  border-spacing: 0;
  border-collapse: collapse;
  margin: 0 0 16px 0;
  width: 100%;
}

:deep(.markdown-body table th),
:deep(.markdown-body table td) {
  padding: 6px 13px;
  border: 1px solid #dfe2e5;
}

:global([data-theme="dark"]) :deep(.markdown-body table th),
:global([data-theme="dark"]) :deep(.markdown-body table td) {
  border-color: #2a2a4e;
}

:deep(.markdown-body table tr) {
  background-color: #fff;
  border-top: 1px solid #c9d1d9;
}

:global([data-theme="dark"]) :deep(.markdown-body table tr) {
  background-color: #16213e;
  border-top-color: #2a2a4e;
}

:deep(.markdown-body table tr:nth-child(2n)) {
  background-color: #f6f8fa;
}

:global([data-theme="dark"]) :deep(.markdown-body table tr:nth-child(2n)) {
  background-color: #1a1a2e;
}

:deep(.markdown-body hr) {
  height: 0.25em;
  padding: 0;
  margin: 24px 0;
  background-color: #dfe2e5;
  border: 0;
}

:global([data-theme="dark"]) :deep(.markdown-body hr) {
  background-color: #2a2a4e;
}

:deep(.markdown-body img) {
  max-width: 100%;
  box-sizing: border-box;
}

:deep(.markdown-body a) {
  color: #667eea;
  text-decoration: none;
  word-break: break-all;
  overflow-wrap: anywhere;
}

:deep(.markdown-body a:hover) {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .content {
    grid-template-columns: 1fr;
  }
}

/* 图片预览样式 */
.image-preview {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.9);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.preview-content {
  position: relative;
  max-width: 90%;
  max-height: 90%;
  animation: zoomIn 0.3s ease;
}

@keyframes zoomIn {
  from {
    transform: scale(0.8);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.preview-content img {
  max-width: 100%;
  max-height: 90vh;
  object-fit: contain;
  display: block;
  border-radius: 8px;
}

.preview-close {
  position: absolute;
  top: -40px;
  right: 0;
  font-size: 40px;
  color: white;
  cursor: pointer;
  width: 40px;
  height: 40px;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: transform 0.2s;
}

.preview-close:hover {
  transform: scale(1.2);
}

</style>
