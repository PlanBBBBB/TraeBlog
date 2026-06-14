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
    if (level === '2') {
      return `<h${level} id="${id}"><span>${text}</span></h${level}>`
    }
    return `<h${level} id="${id}">${text}</h${level}>`
  })
  
  // 给代码块添加行号
  result = result.replace(/<pre><code(.*?)>([\s\S]*?)<\/code><\/pre>/g, (match, attrs, code) => {
    const lines = code.replace(/\n$/, '').split('\n')
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
  line-height: 1.6;
  font-size: 1rem;
  transition: color 0.3s;
  word-wrap: break-word;
  overflow-wrap: break-word;
  word-break: break-word;
  font-family: Optima-Regular, Optima, PingFangSC-light, PingFangTC-light, 'PingFang SC', Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
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
  margin: 1.2em 0 1em;
  font-weight: bold;
  line-height: 1.6;
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
  font-size: 1.6rem;
  text-align: center;
  border-bottom: 2px solid rgb(247, 247, 248);
  padding-bottom: 0.3em;
}

:global([data-theme="dark"]) :deep(.markdown-body h1) {
  border-bottom-color: #2a2a4e;
}

:deep(.markdown-body h2) {
  font-size: 1.4rem;
  display: flex;
  align-items: flex-end;
  font-weight: bold;
  border-bottom: 2px solid rgb(239, 112, 96);
  margin: 1.2em 0 1em;
  padding: 0;
  line-height: 1.6;
}

:deep(.markdown-body h2 span) {
  display: inline-block;
  background: rgb(239, 112, 96);
  color: #ffffff;
  padding: 3px 10px 1px;
  border-top-left-radius: 3px;
  border-top-right-radius: 3px;
  font-weight: bold;
  margin-right: 3px;
}

:deep(.markdown-body h2)::after {
  content: "";
  border-bottom: 1.25rem solid #efebe9;
  border-right: 1.25rem solid transparent;
  display: inline-block;
  vertical-align: bottom;
}

:global([data-theme="dark"]) :deep(.markdown-body h2) {
  border-bottom-color: rgb(239, 112, 96);
}

:global([data-theme="dark"]) :deep(.markdown-body h2 span) {
  background: rgb(239, 112, 96);
  color: #ffffff;
}

:global([data-theme="dark"]) :deep(.markdown-body h2)::after {
  border-bottom-color: #2a2a3e;
}

:deep(.markdown-body h3) {
  font-size: 1.3rem;
}

:deep(.markdown-body h4) {
  font-size: 1.1rem;
}

:deep(.markdown-body p) {
  font-size: 1rem;
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
  margin: 0 0 16px 0;
  line-height: 1.6;
}

:deep(.markdown-body ul),
:deep(.markdown-body ol) {
  margin-top: 8px;
  margin-bottom: 8px;
  padding-left: 25px;
}

:deep(.markdown-body ul) {
  list-style-type: disc;
}

:deep(.markdown-body ul ul) {
  list-style-type: square;
}

:deep(.markdown-body ol) {
  list-style-type: decimal;
}

:deep(.markdown-body li) {
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 1.7rem;
}

:deep(.markdown-body blockquote) {
  display: block;
  font-size: 0.9em;
  overflow: auto;
  border-left: 3px solid rgb(239, 112, 96);
  color: #6a737d;
  padding: 10px 10px 10px 20px;
  margin: 20px 0;
  background: #fff9f9;
}

:global([data-theme="dark"]) :deep(.markdown-body blockquote) {
  color: #999;
  border-left-color: rgb(239, 112, 96);
  background: rgba(239, 112, 96, 0.06);
}

:deep(.markdown-body code) {
  font-size: 0.9rem;
  word-wrap: break-word;
  padding: 2px 4px;
  border-radius: 4px;
  margin: 0 2px;
  color: rgb(239, 112, 96);
  background-color: rgba(27, 31, 35, 0.05);
  font-family: Operator Mono, Consolas, Monaco, Menlo, monospace;
}

:global([data-theme="dark"]) :deep(.markdown-body code) {
  background-color: rgba(239, 112, 96, 0.1);
  color: rgb(239, 112, 96);
}

:deep(.markdown-body pre code) {
  color: #24292f;
  background-color: transparent;
  padding: 16px 0;
  border-radius: 0;
  margin: 0;
  font-size: 1em;
}

:global([data-theme="dark"]) :deep(.markdown-body pre code) {
  color: #cdd6f4;
  background-color: transparent;
}



:deep(.markdown-body table) {
  display: table;
  text-align: left;
  border-spacing: 0;
  border-collapse: collapse;
  margin: 0 0 16px 0;
  width: 100%;
}

:deep(.markdown-body table th),
:deep(.markdown-body table td) {
  font-size: 1rem;
  border: 1px solid #ccc;
  padding: 5px 10px;
  text-align: left;
}

:deep(.markdown-body table tr) {
  border: 0;
  border-top: 1px solid #ccc;
  background-color: white;
}

:deep(.markdown-body table tr:nth-child(2n)) {
  background-color: #F8F8F8;
}

:deep(.markdown-body table tr th) {
  font-weight: bold;
  background-color: #f0f0f0;
}

:global([data-theme="dark"]) :deep(.markdown-body table th),
:global([data-theme="dark"]) :deep(.markdown-body table td) {
  border-color: #2a2a4e;
}

:global([data-theme="dark"]) :deep(.markdown-body table tr) {
  background-color: #1b1b1f;
  border-top-color: #2a2a4e;
}

:global([data-theme="dark"]) :deep(.markdown-body table tr:nth-child(2n)) {
  background-color: #1a1a2e;
}

:global([data-theme="dark"]) :deep(.markdown-body table tr th) {
  background-color: #252535;
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
  display: block;
  margin: 0 auto;
  max-width: 100%;
  box-sizing: border-box;
}

:deep(.markdown-body a) {
  color: rgb(239, 112, 96);
  text-decoration: none;
  word-break: break-all;
  overflow-wrap: anywhere;
  font-weight: bold;
  border-bottom: 1px solid rgb(239, 112, 96);
}

:deep(.markdown-body a:hover) {
  text-decoration: none;
  opacity: 0.8;
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
