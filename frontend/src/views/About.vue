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
            <p class="bio">软件开发工程师</p>
          </div>
          <div class="info-section">
            <h4>🎯 关于博客</h4>
            <p>2 年软件开发经验，专注于构建稳定、高效的系统，追求系统性能、可维护性与工程规范。</p>
          </div>
          <div class="info-section">
            <h4>📄 个人简历</h4>
            <a href="#" target="_blank" class="resume-link">
              <svg class="resume-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/>
                <polyline points="14 2 14 8 20 8"/>
                <line x1="16" y1="13" x2="8" y2="13"/>
                <line x1="16" y1="17" x2="8" y2="17"/>
                <polyline points="10 9 9 9 8 9"/>
              </svg>
              <span class="resume-text">
                <span class="resume-title">查看我的在线简历</span>
                <span class="resume-desc">了解我的工作经历、项目经验与技能详情</span>
              </span>
              <svg class="resume-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/>
                <polyline points="15 3 21 3 21 9"/>
                <line x1="10" y1="14" x2="21" y2="3"/>
              </svg>
            </a>
          </div>
          <div class="link-row">
            <a href="https://github.com/PlanBBBBB" target="_blank" class="link-btn link-github">
              <svg class="link-icon" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z"/>
              </svg>
              GitHub
            </a>
            <a href="mailto:panchanghao0102@163.com" class="link-btn link-email">
              <svg class="link-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                <rect x="2" y="4" width="20" height="16" rx="2"/>
                <path d="M22 7l-10 7L2 7"/>
              </svg>
              个人邮箱
            </a>
            <router-link to="/admin/login" class="link-btn link-admin">
              <svg class="link-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
              </svg>
              博客后台
              <svg class="link-arrow" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg">
                <path d="M5 12h14"/>
                <path d="M12 5l7 7-7 7"/>
              </svg>
            </router-link>
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
  background: var(--bg-primary);
}

.content {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px 20px;
}

.about-card {
  background: white;
  border-radius: 15px;
  overflow: hidden;
  transition: background 0.3s;
}

:global([data-theme="dark"]) .about-card {
  background: #1b1b1f !important;
  box-shadow: none !important;
}

.about-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #f093fb 100%);
  color: white;
  padding: 28px 40px;
  text-align: center;
  position: relative;
}

.about-header h2 {
  font-size: 1.6rem;
  margin: 0;
  font-weight: 700;
  letter-spacing: 2px;
}

.about-content {
  padding: 28px 32px;
}

.profile {
  text-align: center;
  margin-bottom: 24px;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 4px;
  border: 3px solid #f0f0f0;
  transition: border-color 0.3s;
}

:global([data-theme="dark"]) .avatar {
  border-color: #2a2a4e !important;
}

.profile h3 {
  font-size: 1.3rem;
  color: #333;
  margin-bottom: 2px;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .profile h3 {
  color: #eaeaea !important;
}

.profile .bio {
  color: #888;
  font-size: 1rem;
  margin: 0;
}

.info-section {
  margin-bottom: 20px;
}

.info-section h4 {
  color: #333;
  margin-bottom: 10px;
  font-size: 1.1rem;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .info-section h4 {
  color: #eaeaea !important;
}

.info-section p {
  color: #666;
  line-height: 1.7;
  margin: 0;
  transition: color 0.3s;
}

:global([data-theme="dark"]) .info-section p {
  color: #b8b8b8 !important;
}

.info-section ul {
  list-style: none;
  padding: 0;
}

.resume-link {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #eef1f5 100%);
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  text-decoration: none;
  color: #333;
  transition: transform 0.3s, box-shadow 0.3s, border-color 0.3s;
}

.resume-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.15);
  border-color: #667eea;
}

.resume-icon {
  width: 40px;
  height: 40px;
  padding: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 10px;
  color: white;
  flex-shrink: 0;
}

.resume-text {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.resume-title {
  font-weight: 600;
  font-size: 1rem;
  color: #333;
}

.resume-desc {
  font-size: 0.85rem;
  color: #888;
}

.resume-arrow {
  width: 18px;
  height: 18px;
  color: #999;
  flex-shrink: 0;
  transition: transform 0.3s, color 0.3s;
}

.resume-link:hover .resume-arrow {
  transform: translate(2px, -2px);
  color: #667eea;
}

:global([data-theme="dark"]) .resume-link {
  background: linear-gradient(135deg, #1e1e2e 0%, #252535 100%) !important;
  border-color: #2a2a4e !important;
  color: #eaeaea !important;
}

:global([data-theme="dark"]) .resume-title {
  color: #eaeaea !important;
}

:global([data-theme="dark"]) .resume-desc {
  color: #888;
}

:global([data-theme="dark"]) .resume-link:hover {
  border-color: #667eea;
  box-shadow: 0 6px 16px rgba(102, 126, 234, 0.2);
}

.info-section li {
  color: #666;
  padding: 6px 0;
}

:global([data-theme="dark"]) .info-section li {
  color: #b8b8b8 !important;
}

.link-row {
  display: flex;
  gap: 12px;
}

.link-btn {
  flex: 1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 12px 10px;
  border-radius: 10px;
  text-decoration: none;
  font-weight: 500;
  font-size: 0.9rem;
  color: white;
  transition: transform 0.3s, box-shadow 0.3s, background 0.3s;
  white-space: nowrap;
  overflow: hidden;
}

.link-btn:hover {
  transform: translateY(-3px);
}

.link-icon {
  width: 18px;
  height: 18px;
  flex-shrink: 0;
}

.link-arrow {
  width: 14px;
  height: 14px;
  opacity: 0.7;
  flex-shrink: 0;
  transition: transform 0.3s, opacity 0.3s;
}

.link-btn:hover .link-arrow {
  transform: translateX(4px);
  opacity: 1;
}

.link-github {
  background: #24292e;
  box-shadow: 0 4px 12px rgba(36, 41, 46, 0.25);
}

.link-github:hover {
  background: #2f363d;
  box-shadow: 0 8px 20px rgba(36, 41, 46, 0.35);
}

.link-email {
  background: #d44638;
  box-shadow: 0 4px 12px rgba(212, 70, 56, 0.25);
}

.link-email:hover {
  background: #e05a4c;
  box-shadow: 0 8px 20px rgba(212, 70, 56, 0.35);
}

.link-admin {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.link-admin:hover {
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.45);
}
</style>
