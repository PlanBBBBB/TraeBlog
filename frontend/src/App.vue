<template>
  <div id="app">
    <Navbar />
    <main class="main-content">
      <router-view />
    </main>
    <footer class="footer">
      <p>&copy; 2026 PlanB's Blog. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
}

:root {
  --bg-primary: #f5f7fa;
  --bg-secondary: #ffffff;
  --text-primary: #333333;
  --text-secondary: #666666;
  --text-tertiary: #999999;
  --border-color: #eeeeee;
  --shadow-color: rgba(0, 0, 0, 0.1);
}

[data-theme="dark"] {
  --bg-primary: #1b1b1f;
  --bg-secondary: #1b1b1f;
  --text-primary: #eaeaea;
  --text-secondary: #b8b8b8;
  --text-tertiary: #888888;
  --border-color: #2a2a4e;
  --shadow-color: rgba(0, 0, 0, 0.3);
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  background: var(--bg-primary);
  color: var(--text-primary);
  transition: background 0.3s, color 0.3s;
}

#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
}

.footer {
  background: white;
  color: #333;
  text-align: center;
  padding: 30px 20px;
  margin-top: auto;
  transition: background 0.3s, color 0.3s;
}

[data-theme="dark"] .footer {
  background: #1b1b1f;
  color: white;
}

[data-theme="dark"] .navbar {
  background: rgba(32, 33, 38, 0.95) !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

[data-theme="dark"] .nav-menu li a {
  color: #eaeaea !important;
}

[data-theme="dark"] .theme-toggle {
  background: #2a2a4e !important;
}

.footer p {
  opacity: 0.8;
}

/* 确保 html 和 body 在任何模式下都没有 margin/padding */
[data-theme="dark"] html, 
[data-theme="dark"] body {
  margin: 0 !important;
  padding: 0 !important;
}

[data-theme="dark"] #app {
  margin: 0 !important;
  padding: 0 !important;
}

/* 全局暗色模式样式 - 确保所有组件都能正确应用 */
[data-theme="dark"] .article-card {
  background: #1b1b1f !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

[data-theme="dark"] .article-card h3,
[data-theme="dark"] .article-card h2,
[data-theme="dark"] .article-list h2 {
  color: #ffffff !important;
}

[data-theme="dark"] .profile h3 {
  color: #ffffff !important;
}

[data-theme="dark"] .profile p {
  color: #cccccc !important;
}

[data-theme="dark"] .article-card .summary {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .article-meta .date {
  color: #888 !important;
}

[data-theme="dark"] .sidebar-card {
  background: #1b1b1f !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

[data-theme="dark"] .card-header {
  border-bottom-color: #2a2a4e !important;
}

[data-theme="dark"] .card-header h4 {
  color: #eaeaea !important;
}

[data-theme="dark"] .stat-label {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .contact-link {
  background: #2a2a4e !important;
}

[data-theme="dark"] .category-name {
  color: #eaeaea !important;
}

[data-theme="dark"] .category-count {
  color: #888 !important;
}

[data-theme="dark"] .tag-item {
  background: #2a2a4e !important;
  color: #b8b8b8 !important;
}

[data-theme="dark"] .outline-item {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .outline-item:hover {
  background: #3d345e !important;
  color: #b89fff !important;
}

[data-theme="dark"] .article-full {
  background: #1b1b1f !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

[data-theme="dark"] .article-title {
  color: #eaeaea !important;
  border-bottom-color: #2a2a4e !important;
}

[data-theme="dark"] .article-meta .date,
[data-theme="dark"] .article-meta .tags,
[data-theme="dark"] .article-meta .category {
  background: transparent !important;
  padding: 8px 0 !important;
}

[data-theme="dark"] .markdown-body {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .markdown-body h1,
[data-theme="dark"] .markdown-body h2,
[data-theme="dark"] .markdown-body h3,
[data-theme="dark"] .markdown-body h4,
[data-theme="dark"] .markdown-body h5,
[data-theme="dark"] .markdown-body h6 {
  color: #eaeaea !important;
}

[data-theme="dark"] .pagination-wrapper {
  border-top-color: #2a2a4e !important;
}

/* 全局代码块样式 */
.markdown-body pre {
  padding: 0 !important;
  overflow-x: visible !important;
  font-size: 0.9em !important;
  line-height: 1.0 !important;
  background-color: #f6f8fa !important;
  border-radius: 6px !important;
  margin: 0 0 16px 0 !important;
  max-width: 100% !important;
  box-sizing: border-box !important;
  border: 1px solid #e8e8e8 !important;
  counter-reset: line !important;
}

[data-theme="dark"] .markdown-body pre {
  background-color: #1e1e2e !important;
  border: 1px solid #2a2a4e !important;
}

.markdown-body pre code {
  padding: 16px 0 !important;
  background-color: transparent !important;
  border-radius: 0 !important;
  font-size: 1em !important;
  color: #24292f !important;
  display: block !important;
  overflow-x: visible !important;
  line-height: 1.3 !important;
}

[data-theme="dark"] .markdown-body pre code {
  background-color: transparent !important;
  color: #cdd6f4 !important;
}

/* 行号样式 - 使用CSS计数器 */
.markdown-body pre code .code-line {
  display: block !important;
  position: relative !important;
  padding-left: 56px !important;
  line-height: 1.3 !important;
}

.markdown-body pre code .code-line::before {
  counter-increment: line !important;
  content: counter(line) !important;
  position: absolute !important;
  left: 0 !important;
  width: 40px !important;
  text-align: right !important;
  padding-right: 16px !important;
  color: #999 !important;
  user-select: none !important;
  border-right: 1px solid #e8e8e8 !important;
}

[data-theme="dark"] .markdown-body pre code .code-line::before {
  color: #6c7086 !important;
  border-right-color: #2a2a4e !important;
}

/* 全局 Element UI 组件暗色模式样式 */
[data-theme="dark"] .el-pagination {
  color: white !important;
}

[data-theme="dark"] .el-pagination button,
[data-theme="dark"] .el-pager li {
  background: #1b1b1f !important;
  color: white !important;
  border-color: #2a2a4e !important;
}

[data-theme="dark"] .el-pagination button:hover,
[data-theme="dark"] .el-pager li:hover {
  color: #667eea !important;
  background: #2a2a4e !important;
}

[data-theme="dark"] .el-pager li.is-active {
  background: #667eea !important;
  color: white !important;
  border-color: #667eea !important;
}

[data-theme="dark"] .el-pagination button:disabled {
  color: #999 !important;
  cursor: not-allowed;
}

[data-theme="dark"] .el-pagination .btn-prev,
[data-theme="dark"] .el-pagination .btn-next {
  background: #1b1b1f !important;
  color: white !important;
  border-color: #2a2a4e !important;
}
</style>
