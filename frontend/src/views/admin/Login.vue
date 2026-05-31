<template>
  <div class="login-container">
    <div class="login-card">
      <h2>博客管理后台</h2>
      <p>请输入账号密码登录</p>
      <el-form :model="loginForm" @submit.native.prevent="handleLogin">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-button type="primary" size="large" style="width: 100%" @click="handleLogin" :loading="loading">
          登录
        </el-button>
      </el-form>
      <div v-if="error" class="error-message">{{ error }}</div>
      <router-link to="/" class="back-link">← 返回博客首页</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { authApi } from '../../api/index'

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
}

.login-card {
  background: white;
  padding: 50px;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
  width: 400px;
  text-align: center;
}

.login-card h2 {
  color: #333;
  margin-bottom: 10px;
  font-size: 24px;
}

.login-card p {
  color: #999;
  margin-bottom: 30px;
  font-size: 14px;
}

.error-message {
  color: #f56c6c;
  margin-top: 15px;
  font-size: 14px;
}

.back-link {
  display: inline-block;
  margin-top: 25px;
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
  transition: color 0.3s;
}

.back-link:hover {
  color: #764ba2;
}
</style>
