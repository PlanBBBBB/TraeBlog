<template>
  <div class="category-manage">
    <div class="page-header">
      <h2>分类管理</h2>
      <el-button type="primary" @click="handleAdd">+ 添加分类</el-button>
    </div>

    <!-- 查询表单 -->
    <div class="search-wrapper">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="分类名称">
          <el-input v-model="searchForm.name" placeholder="请输入分类名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="创建日期">
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
      <el-table :data="categoryPage.records" stripe style="width: 100%">
        <el-table-column prop="name" label="分类名称" />
        <el-table-column label="创建日期" width="120">
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
          :page-count="categoryPage.pages"
          :page-size="10"
          :total="categoryPage.total"
          layout="total, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
        />
      </div>
    </template>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑分类' : '添加分类'"
      width="400px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
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
import { ref, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { categoryApi } from '../../api/index'

const categoryPage = ref({ records: [], pages: 0, total: 0 })
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentPage = ref(1)
const loading = ref(true)
const form = ref({
  id: null,
  name: ''
})

const searchForm = ref({
  name: ''
})
const dateRange = ref([])

// 防抖自动查询
let searchTimer = null
const autoSearch = () => {
  clearTimeout(searchTimer)
  searchTimer = setTimeout(() => {
    currentPage.value = 1
    loadCategories()
  }, 300)
}

watch(() => searchForm.value.name, autoSearch)
watch(dateRange, autoSearch)

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadCategories()
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const loadCategories = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: 10,
      name: searchForm.value.name || undefined
    }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    const res = await categoryApi.listAdmin(params)
    categoryPage.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
  loading.value = false
}

const handleSearch = () => {
  currentPage.value = 1
  loadCategories()
}

const handleReset = () => {
  searchForm.value = {
    name: ''
  }
  dateRange.value = []
  currentPage.value = 1
  loadCategories()
}

const handleAdd = () => {
  isEdit.value = false
  form.value = { id: null, name: '' }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { id: row.id, name: row.name }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await categoryApi.delete(row.id)
      ElMessage.success('删除成功')
      loadCategories()
    } catch (error) {
      // 拦截器已统一处理错误提示
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!form.value.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }

  try {
    if (isEdit.value) {
      await categoryApi.update(form.value.id, form.value)
      ElMessage.success('编辑成功')
    } else {
      await categoryApi.add(form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadCategories()
  } catch (error) {
    // 拦截器已统一处理错误提示
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.category-manage .page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.category-manage .page-header h2 {
  margin: 0;
  color: #333;
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
</style>
