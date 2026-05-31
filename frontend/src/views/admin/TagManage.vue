<template>
  <div class="tag-manage">
    <div class="page-header">
      <h2>标签管理</h2>
      <el-button type="primary" @click="handleAdd">+ 添加标签</el-button>
    </div>

    <!-- 查询表单 -->
    <div class="search-wrapper">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="标签名称">
          <el-input v-model="searchForm.name" placeholder="请输入标签名称" clearable></el-input>
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
      <el-table :data="tagPage.records" stripe style="width: 100%">
        <el-table-column prop="name" label="标签名称" />
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
          :page-count="tagPage.pages"
          :page-size="10"
          :total="tagPage.total"
          layout="total, prev, pager, next, jumper"
          @current-change="handleCurrentChange"
        />
      </div>
    </template>

    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑标签' : '添加标签'"
      width="400px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="标签名称">
          <el-input v-model="form.name" placeholder="请输入标签名称" />
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { tagApi } from '../../api/index'

const tagPage = ref({ records: [], pages: 0, total: 0 })
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

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadTags()
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const loadTags = async () => {
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
    const res = await tagApi.listAdmin(params)
    tagPage.value = res.data
  } catch (error) {
    console.error('加载标签失败', error)
  }
  loading.value = false
}

const handleSearch = () => {
  currentPage.value = 1
  loadTags()
}

const handleReset = () => {
  searchForm.value = {
    name: ''
  }
  dateRange.value = []
  currentPage.value = 1
  loadTags()
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
  ElMessageBox.confirm('确定要删除该标签吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await tagApi.delete(row.id)
      ElMessage.success('删除成功')
      loadTags()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleSubmit = async () => {
  if (!form.value.name.trim()) { ElMessage.warning('请输入标签名称'); return }

  try {
    if (isEdit.value) {
      await tagApi.update(form.value.id, form.value)
      ElMessage.success('编辑成功')
    } else {
      await tagApi.add(form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadTags()
  } catch (error) {
    ElMessage.error('操作失败，该标签可能已存在')
  }
}

onMounted(() => {
  loadTags()
})
</script>

<style scoped>
.tag-manage .page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.tag-manage .page-header h2 {
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
