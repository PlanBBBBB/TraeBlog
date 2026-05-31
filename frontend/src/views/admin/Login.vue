<template>
  <div class="login-container">
    <div class="login-wrapper">
      <!-- 左侧装饰区域 -->
      <div class="decoration-section">
        <div class="bg-shape shape-1"></div>
        <div class="bg-shape shape-2"></div>
        <div class="bg-shape shape-3"></div>
        <div class="illustration">
          <svg viewBox="0 0 400 400" class="svg-illustration">
            <!-- 装饰性图形 -->
            <circle cx="200" cy="200" r="120" fill="rgba(255,255,255,0.1)"/>
            <circle cx="200" cy="200" r="90" fill="rgba(255,255,255,0.15)"/>
            <circle cx="200" cy="200" r="60" fill="rgba(255,255,255,0.2)"/>
            <!-- 抽象装饰元素 -->
            <path d="M100 150 Q150 100 200 150 T300 150" stroke="rgba(255,255,255,0.3)" stroke-width="3" fill="none"/>
            <path d="M100 250 Q150 300 200 250 T300 250" stroke="rgba(255,255,255,0.3)" stroke-width="3" fill="none"/>
            <!-- 中心图标 -->
            <g transform="translate(200, 200)">
              <rect x="-35" y="-40" width="70" height="80" rx="8" fill="rgba(255,255,255,0.9)"/>
              <rect x="-25" y="-30" width="50" height="35" rx="4" fill="#667eea"/>
              <rect x="-25" y="10" width="50" height="8" rx="2" fill="#e0e0e0"/>
              <rect x="-25" y="22" width="35" height="8" rx="2" fill="#e0e0e0"/>
            </g>
          </svg>
        </div>
        <div class="welcome-text">
          <h1>欢迎回来</h1>
          <p>管理您的博客内容</p>
        </div>
      </div>
      
      <!-- 右侧登录表单 -->
      <div class="login-section">
        <div class="login-card">
          <!-- 头像区域 -->
          <div class="avatar-wrapper">
            <div class="avatar">
              <img :src="avatarImg" alt="头像" class="avatar-img"/>
            </div>
          </div>
          
          <h2>登录</h2>
          
          <el-form :model="loginForm" @submit.native.prevent="handleLogin" class="login-form">
            <div class="form-item-wrapper">
              <label class="form-label">用户名</label>
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                size="large"
                class="custom-input"
                @keyup.enter="handleLogin"
              />
            </div>
            
            <div class="form-item-wrapper">
              <label class="form-label">密码</label>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                show-password
                size="large"
                class="custom-input"
                @keyup.enter="handleLogin"
              />
            </div>
            
            <el-button 
              type="primary" 
              size="large" 
              class="login-button" 
              @click="handleLogin" 
              :loading="loading"
            >
              登录
            </el-button>
          </el-form>
          
          <div v-if="error" class="error-message">{{ error }}</div>
          
          <router-link to="/" class="back-link">
            <span class="back-arrow">←</span>
            返回博客首页
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '../../api/index'
import avatarImg from '../../assets/avatar.jpg'

const router = useRouter()
const loading = ref(false)
const error = ref('')
const loginForm = ref({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    error.value = '请输入用户名和密码'
    return
  }

  loading.value = true
  error.value = ''
  try {
    const res = await authApi.login(loginForm.value.username, loginForm.value.password)
    if (res.data && res.data.token) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('isLoggedIn', 'true')
      ElMessage.success('登录成功！')
      router.push('/admin/articles')
    }
  } catch (err) {
    error.value = '用户名或密码错误'
    ElMessage.error('登录失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
  position: relative;
}

.login-wrapper {
  width: 900px;
  height: 550px;
  display: flex;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  overflow: hidden;
  position: relative;
  z-index: 1;
}

/* 左侧装饰区域 */
.decoration-section {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.bg-shape {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
}

.shape-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
}

.shape-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: -50px;
  background: rgba(255, 255, 255, 0.15);
}

.shape-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 20px;
  background: rgba(255, 255, 255, 0.08);
}

.illustration {
  width: 250px;
  height: 250px;
  margin-bottom: 20px;
}

.svg-illustration {
  width: 100%;
  height: 100%;
}

.welcome-text {
  text-align: center;
  color: white;
  z-index: 1;
}

.welcome-text h1 {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 10px;
}

.welcome-text p {
  font-size: 16px;
  opacity: 0.9;
}

/* 右侧登录表单 */
.login-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-card {
  width: 100%;
  max-width: 320px;
  text-align: center;
}

.avatar-wrapper {
  margin-bottom: 20px;
  margin-top: 30px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto;
  padding: 5px;
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
  overflow: hidden;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.login-card h2 {
  color: #333;
  margin-bottom: 35px;
  font-size: 28px;
  font-weight: 600;
}

.login-form {
  margin-bottom: 20px;
}

.form-item-wrapper {
  margin-bottom: 25px;
  text-align: left;
}

.form-label {
  display: block;
  color: #999;
  font-size: 14px;
  margin-bottom: 8px;
}

.custom-input {
  --el-input-border-color: #e0e0e0;
  --el-input-border-color-hover: #667eea;
  --el-input-border-color-focus: #667eea;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 8px;
  box-shadow: 0 0 0 1px var(--el-input-border-color, var(--el-border-color)) inset;
  transition: all 0.3s;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px var(--el-input-border-color-hover) inset;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px var(--el-input-border-color-focus) inset;
}



.login-button {
  width: 100%;
  border-radius: 25px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  font-size: 16px;
  font-weight: 500;
  padding: 12px 0;
  transition: all 0.3s;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.error-message {
  color: #f56c6c;
  margin-top: 15px;
  font-size: 14px;
}

.back-link {
  display: inline-flex;
  align-items: center;
  margin-top: 25px;
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
  gap: 5px;
}

.back-link:hover {
  color: #764ba2;
}

.back-arrow {
  font-size: 16px;
}
</style>
