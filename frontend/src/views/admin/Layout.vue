<template>
  <div class="admin-layout">
    <header class="admin-header">
      <h1>📝 博客管理后台</h1>
      <div class="header-right">
        <router-link to="/" class="back-link">返回博客首页</router-link>
        <button class="logout-btn" @click="handleLogout">退出登录</button>
      </div>
    </header>
    <div class="admin-container">
      <aside class="admin-sidebar">
        <router-link to="/admin/articles" class="sidebar-item">
          <span>📄 文章管理</span>
        </router-link>
        <router-link to="/admin/categories" class="sidebar-item">
          <span>📂 分类管理</span>
        </router-link>
        <router-link to="/admin/tags" class="sidebar-item">
          <span>🏷️ 标签管理</span>
        </router-link>
        <router-link to="/admin/links" class="sidebar-item">
          <span>🔗 友情链接管理</span>
        </router-link>
      </aside>
      <main class="admin-main">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    localStorage.removeItem('isLoggedIn')
    localStorage.removeItem('token')
    ElMessage.success('已退出登录')
    router.push('/admin/login')
  }).catch(() => {})
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

.admin-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 0 40px;
  height: 70px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.admin-header h1 {
  font-size: 1.5rem;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.back-link {
  color: white;
  text-decoration: none;
  padding: 8px 16px;
  border: 1px solid white;
  border-radius: 4px;
  transition: all 0.3s;
}

.back-link:hover {
  background: white;
  color: #667eea;
}

.logout-btn {
  color: white;
  background: transparent;
  border: 1px solid white;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
}

.logout-btn:hover {
  background: white;
  color: #667eea;
}

.admin-container {
  display: flex;
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px;
  gap: 30px;
}

.admin-sidebar {
  width: 220px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 20px 0;
  min-height: calc(100vh - 160px);
}

.sidebar-item {
  display: block;
  padding: 15px 25px;
  color: #666;
  text-decoration: none;
  transition: all 0.3s;
  border-left: 3px solid transparent;
}

.sidebar-item:hover {
  background: #f0f0f0;
  color: #667eea;
}

.sidebar-item.router-link-active {
  background: #f0f0f0;
  color: #667eea;
  border-left-color: #667eea;
}

.admin-main {
  flex: 1;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 30px;
}
</style>
