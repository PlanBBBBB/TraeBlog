<template>
  <div class="about">
    <div class="content">
      <div class="about-card">
        <div class="about-header">
          <h2>关于我</h2>
        </div>
        <div class="about-content">
          <div class="profile">
            <img :src="avatar" alt="头像" class="avatar" />
            <h3>PlanB</h3>
            <p class="bio">后端工程师 / 技术博主</p>
          </div>
          <div class="info-section">
            <h4>🎯 关于博客</h4>
            <p>这是我的个人博客，用于记录学习和技术文章分享，帮助0基础的人也能轻松学IT。</p>
          </div>
          <div class="info-section">
            <h4>🛠️ 技术栈</h4>
            <ul>
              <li>后端：Java / Spring Boot / MySQL</li>
              <li>前端：Vue.js / HTML / CSS</li>
              <li>工具：Git / Docker / Linux</li>
            </ul>
          </div>
          <div class="info-section">
            <h4>📚 学习理念</h4>
            <p>不积跬步，无以至千里；不积小流，无以成江海。</p>
          </div>
          <div class="info-section">
            <h4>🚀 联系方式</h4>
            <div class="social-links">
              <a href="https://github.com/PlanBBBBB" target="_blank" class="social-link">
                <span class="icon">🐙</span> GitHub
              </a>
            </div>
          </div>
          <div class="info-section">
            <h4>⚙️ 管理入口</h4>
            <div class="admin-link-wrapper">
              <router-link to="/admin/login" class="admin-link">
                进入博客管理后台
              </router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import avatar from '../assets/avatar.jpg'
import BackToTop from '../components/BackToTop.vue'
</script>

<style scoped>
.about {
  min-height: 100vh;
}

.content {
  max-width: 800px;
  margin: 0 auto;
  padding: 50px 20px;
}

.about-card {
  background: white;
  border-radius: 15px;
  box-shadow: 0 5px 25px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.about-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 40px;
  text-align: center;
}

.about-header h2 {
  font-size: 2rem;
  margin: 0;
}

.about-content {
  padding: 40px;
}

.profile {
  text-align: center;
  margin-bottom: 40px;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
}

.profile h3 {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 10px;
}

.profile .bio {
  color: #666;
  font-size: 1.1rem;
  margin: 0;
}

.info-section {
  margin-bottom: 30px;
}

.info-section h4 {
  color: #333;
  margin-bottom: 15px;
  font-size: 1.2rem;
}

.info-section p {
  color: #666;
  line-height: 1.8;
  margin: 0;
}

.info-section ul {
  list-style: none;
  padding: 0;
}

.info-section li {
  color: #666;
  padding: 8px 0;
}

.social-links {
  display: flex;
  gap: 15px;
}

.social-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  text-decoration: none;
  transition: transform 0.3s;
}

.social-link:hover {
  transform: translateY(-3px);
}

.social-link .icon {
  font-size: 1.3rem;
}

.admin-link-wrapper {
  margin-top: 10px;
}

.admin-link {
  display: inline-flex;
  align-items: center;
  padding: 12px 28px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  transition: transform 0.3s, box-shadow 0.3s;
}

.admin-link:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}
</style>
