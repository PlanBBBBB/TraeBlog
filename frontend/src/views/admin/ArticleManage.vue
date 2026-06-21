<template>
  <div class="article-manage">
    <div class="page-header">
      <h2>文章管理</h2>
      <el-button type="primary" @click="handleAdd">+ 添加文章</el-button>
    </div>

    <!-- 查询表单 -->
    <div class="search-wrapper">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="searchForm.category" placeholder="请选择分类" clearable filterable style="width: 200px">
            <el-option v-for="cat in allCategories" :key="cat.id" :label="cat.name" :value="cat.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select v-model="searchForm.tag" placeholder="请选择标签" clearable filterable style="width: 200px">
            <el-option v-for="tag in allTags" :key="tag.id" :label="tag.name" :value="tag.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="发布日期">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div v-if="loading" class="loading">加载中...</div>
    <template v-else>
      <el-table :data="articlePage.records" stripe style="width: 100%">
        <el-table-column label="标题" min-width="200">
          <template #default="{ row }">
            <span v-if="row.isLocked" style="margin-right: 8px;">🔒</span>
            {{ row.title }}
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="120" />
        <el-table-column prop="tags" label="标签" width="200" />
        <el-table-column label="发布日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="currentPage"
          :page-count="articlePage.pages"
          :page-size="10"
          :total="articlePage.total"
          layout="total, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
        />
      </div>
    </template>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑文章' : '添加文章'"
      width="700px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.category" placeholder="请选择分类" filterable style="width: 100%">
            <el-option
              v-for="cat in categories"
              :key="cat.id"
              :label="cat.name"
              :value="cat.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标签">
          <el-select
            v-model="form.tags"
            multiple
            filterable
            placeholder="请选择标签"
            style="width: 100%"
          >
            <el-option
              v-for="tag in allTags"
              :key="tag.id"
              :label="tag.name"
              :value="tag.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input
            v-model="form.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入文章摘要"
          />
        </el-form-item>
        <el-form-item label="密码保护">
          <el-switch v-model="form.isLocked" />
        </el-form-item>
        <el-form-item v-if="form.isLocked" label="访问密码">
          <el-input v-model="form.password" type="password" placeholder="请输入文章访问密码" />
        </el-form-item>
        <el-form-item label="内容">
          <el-tabs v-model="activeTab" class="content-tabs">
            <el-tab-pane label="编辑" name="edit">
              <el-input
                v-model="form.content"
                type="textarea"
                :rows="12"
                placeholder="请输入文章内容（支持Markdown）"
              />
            </el-tab-pane>
            <el-tab-pane label="预览" name="preview">
              <div class="markdown-preview markdown-body" v-html="previewContent"></div>
            </el-tab-pane>
          </el-tabs>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { marked } from 'marked'
import { articleApi, categoryApi, tagApi } from '../../api/index'

// 配置marked
marked.setOptions({
  gfm: true,
  breaks: true
})

const activeTab = ref('edit')
const previewContent = computed(() => {
  if (!form.value.content) return '<p style="color: #999;">暂无内容可预览</p>'
  return marked.parse(form.value.content)
})

const articlePage = ref({ records: [], pages: 0, total: 0 })
const categories = ref([])
const allCategories = ref([])
const allTags = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentPage = ref(1)
const loading = ref(true)
const form = ref({
  id: null,
  title: '',
  category: '',
  tags: '',
  summary: '',
  content: '',
  isLocked: false,
  password: ''
})

const searchForm = ref({
  title: '',
  category: '',
  tag: ''
})
const dateRange = ref([])

// 防抖自动查询
let searchTimer = null
const autoSearch = () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    currentPage.value = 1
    loadArticles()
  }, 300)
}

// 监听筛选条件变化自动查询
watch(() => searchForm.value.title, autoSearch)
watch(() => searchForm.value.category, autoSearch)
watch(() => searchForm.value.tag, autoSearch)
watch(dateRange, autoSearch)

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

const loadArticles = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: 10,
      title: searchForm.value.title || undefined,
      category: searchForm.value.category || undefined,
      tag: searchForm.value.tag || undefined
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    const res = await articleApi.listAdmin(params)
    articlePage.value = res.data
  } catch (error) {
    console.error('加载文章失败', error)
  }
  loading.value = false
}

const loadCategories = async () => {
  try {
    const res = await categoryApi.list()
    categories.value = res.data
    allCategories.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const loadAllTags = async () => {
  try {
    const res = await tagApi.list()
    allTags.value = res.data
  } catch (error) {
    console.error('加载所有标签失败', error)
  }
}

const handleSearch = () => {
  currentPage.value = 1
  loadArticles()
}

const handleReset = () => {
  searchForm.value = {
    title: '',
    category: '',
    tag: ''
  }
  dateRange.value = []
  currentPage.value = 1
  loadArticles()
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    id: null,
    title: '',
    category: categories.value[0]?.name || '',
    tags: [],
    summary: '',
    content: '',
    isLocked: false,
    password: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  // 将逗号分隔的字符串转换为数组
  const tagsArray = row.tags ? row.tags.split(',').map(tag => tag.trim()).filter(tag => tag) : []
  form.value = {
    id: row.id,
    title: row.title,
    category: row.category,
    tags: tagsArray,
    summary: row.summary,
    content: row.content,
    isLocked: row.isLocked || false,
    password: row.password || ''
  }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该文章吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await articleApi.delete(row.id)
      ElMessage.success('删除成功')
      loadArticles()
    } catch (error) {
      // 拦截器已统一处理错误提示
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!form.value.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }
  if (!form.value.category) {
    ElMessage.warning('请选择分类')
    return
  }

  try {
    // 将标签数组转换为逗号分隔的字符串
    const submitData = {
      ...form.value,
      tags: Array.isArray(form.value.tags) ? form.value.tags.join(', ') : form.value.tags
    }
    
    if (isEdit.value) {
      await articleApi.update(submitData.id, submitData)
      ElMessage.success('编辑成功')
    } else {
      await articleApi.add(submitData)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadArticles()
  } catch (error) {
    // 拦截器已统一处理错误提示
  }
}

onMounted(() => {
  loadCategories()
  loadAllTags()
  loadArticles()
})
</script>

<style scoped>
.article-manage .page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.article-manage .page-header h2 {
  margin: 0;
  color: #333;
}

/* 内容tabs对齐调整 */
.content-tabs {
  width: 100%;
}

.content-tabs :deep(.el-tabs__header) {
  margin: 0 0 8px 0;
}

.content-tabs :deep(.el-tabs__content) {
  padding: 0;
}

.content-tabs :deep(.el-tab-pane) {
  padding: 0;
}

.content-tabs :deep(.el-textarea__inner) {
  box-sizing: border-box;
  width: 100%;
}

.search-wrapper {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.loading {
  text-align: center;
  padding: 50px;
  color: #999;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.markdown-preview {
  min-height: 400px;
  padding: 20px;
  color: #444;
  line-height: 2;
  font-size: 1rem;
}

/* Markdown 基础样式 */
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

:deep(.markdown-body h1) { font-size: 2em; border-bottom: 1px solid #eee; padding-bottom: 0.3em; }
:deep(.markdown-body h2) { font-size: 1.5em; border-bottom: 1px solid #eee; padding-bottom: 0.3em; }
:deep(.markdown-body h3) { font-size: 1.25em; }
:deep(.markdown-body h4) { font-size: 1em; }

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

:deep(.markdown-body code) {
  padding: 0.2em 0.4em;
  background-color: #f5f7fa;
  border-radius: 6px;
  font-family: SFMono-Regular, Consolas, monospace;
  font-size: 0.9em;
}

:deep(.markdown-body pre) {
  padding: 16px;
  overflow: auto;
  font-size: 0.9em;
  line-height: 1.5;
  background-color: #f5f7fa;
  border-radius: 6px;
  margin: 0 0 16px 0;
}

:deep(.markdown-body pre code) {
  padding: 0;
  background-color: transparent;
  border-radius: 0;
  font-size: 1em;
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

:deep(.markdown-body table tr) {
  background-color: #fff;
  border-top: 1px solid #c9d1d9;
}

:deep(.markdown-body table tr:nth-child(2n)) {
  background-color: #f6f8fa;
}

:deep(.markdown-body hr) {
  height: 0.25em;
  padding: 0;
  margin: 24px 0;
  background-color: #dfe2e5;
  border: 0;
}

:deep(.markdown-body img) {
  max-width: 100%;
  box-sizing: border-box;
}

:deep(.markdown-body a) {
  color: #667eea;
  text-decoration: none;
}

:deep(.markdown-body a:hover) {
  text-decoration: underline;
}
</style>
