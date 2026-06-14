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
  background: var(--bg-primary);
}

.main-content {
  flex: 1;
  background: var(--bg-primary);
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

[data-theme="dark"] .category-detail h1,
[data-theme="dark"] .tag-detail h1 {
  color: #eaeaea !important;
}

[data-theme="dark"] .sidebar .tag-item {
  background: #2a2a4e !important;
  color: #b8b8b8 !important;
}

[data-theme="dark"] .sidebar .tag-item:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%) !important;
  color: white !important;
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

/* About 页面暗色模式 */
[data-theme="dark"] .about-card {
  background: #1b1b1f !important;
  box-shadow: none !important;
}

[data-theme="dark"] .about-card .profile h3 {
  color: #eaeaea !important;
}

[data-theme="dark"] .about-card .profile .bio {
  color: #888 !important;
}

[data-theme="dark"] .about-card .info-section h4 {
  color: #eaeaea !important;
}

[data-theme="dark"] .about-card .info-section p {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .about-card .info-section li {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .about-card .avatar {
  border-color: #2a2a4e !important;
}

[data-theme="dark"] .about-card .resume-link {
  background: linear-gradient(135deg, #1e1e2e 0%, #252535 100%) !important;
  border-color: #2a2a4e !important;
}

[data-theme="dark"] .about-card .resume-title {
  color: #eaeaea !important;
}

[data-theme="dark"] .about-card .resume-desc {
  color: #888 !important;
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

/* ===== Orangeheart 主题风格 ===== */
.markdown-body h1,
.markdown-body h2,
.markdown-body h3,
.markdown-body h4,
.markdown-body h5,
.markdown-body h6 {
  margin: 1.2em 0 1em !important;
  padding: 0 !important;
  font-weight: bold !important;
  color: black !important;
  line-height: 1.6 !important;
}

[data-theme="dark"] .markdown-body h1,
[data-theme="dark"] .markdown-body h2,
[data-theme="dark"] .markdown-body h3,
[data-theme="dark"] .markdown-body h4,
[data-theme="dark"] .markdown-body h5,
[data-theme="dark"] .markdown-body h6 {
  color: #eaeaea !important;
}

.markdown-body h1 {
  font-size: 1.6rem !important;
  text-align: center !important;
  border-bottom: 2px solid rgb(247, 247, 248) !important;
  padding-bottom: 0.3em !important;
}

[data-theme="dark"] .markdown-body h1 {
  border-bottom-color: #2a2a4e !important;
}

.markdown-body h2 {
  font-size: 1.4rem !important;
  display: flex !important;
  align-items: flex-end !important;
  font-weight: bold !important;
  border-bottom: 2px solid rgb(239, 112, 96) !important;
  margin: 1.2em 0 1em !important;
  padding: 0 !important;
  line-height: 1.6 !important;
}

.markdown-body h2 span {
  display: inline-block !important;
  background: rgb(239, 112, 96) !important;
  color: #ffffff !important;
  padding: 3px 10px 1px !important;
  border-top-left-radius: 3px !important;
  border-top-right-radius: 3px !important;
  font-weight: bold !important;
  margin-right: 3px !important;
}

.markdown-body h2::after {
  content: "";
  border-bottom: 1.25rem solid #efebe9;
  border-right: 1.25rem solid transparent;
  display: inline-block;
  vertical-align: bottom;
}

[data-theme="dark"] .markdown-body h2 {
  border-bottom-color: rgb(239, 112, 96) !important;
}

[data-theme="dark"] .markdown-body h2 span {
  background: rgb(239, 112, 96) !important;
  color: #ffffff !important;
}

[data-theme="dark"] .markdown-body h2::after {
  border-bottom-color: #2a2a3e;
}

.markdown-body h3 {
  font-size: 1.3rem !important;
}

.markdown-body h4 {
  font-size: 1.1rem !important;
}

.markdown-body h5 {
  font-size: 1rem !important;
}

.markdown-body h6 {
  font-size: 1rem !important;
}

/* Orangeheart 段落 */
.markdown-body p {
  font-size: 1rem !important;
  padding-top: 0.5rem !important;
  padding-bottom: 0.5rem !important;
  margin: 0 0 16px 0 !important;
  line-height: 1.6 !important;
}

/* Orangeheart 列表 */
.markdown-body ul,
.markdown-body ol {
  margin-top: 8px !important;
  margin-bottom: 8px !important;
  padding-left: 25px !important;
}

.markdown-body ul {
  list-style-type: disc !important;
}

.markdown-body ul ul {
  list-style-type: square !important;
}

.markdown-body ol {
  list-style-type: decimal !important;
}

.markdown-body li {
  margin-top: 5px !important;
  margin-bottom: 5px !important;
  line-height: 1.7rem !important;
}

/* Orangeheart 引用 */
.markdown-body blockquote {
  display: block !important;
  font-size: 0.9em !important;
  overflow: auto !important;
  border-left: 3px solid rgb(239, 112, 96) !important;
  color: #6a737d !important;
  padding: 10px 10px 10px 20px !important;
  margin-bottom: 20px !important;
  margin-top: 20px !important;
  background: #fff9f9 !important;
}

[data-theme="dark"] .markdown-body blockquote {
  color: #999 !important;
  border-left-color: rgb(239, 112, 96) !important;
  background: rgba(239, 112, 96, 0.06) !important;
}

/* Orangeheart 链接 */
.markdown-body a {
  text-decoration: none !important;
  word-wrap: break-word !important;
  font-weight: bold !important;
  color: rgb(239, 112, 96) !important;
  border-bottom: 1px solid rgb(239, 112, 96) !important;
}

.markdown-body a:hover {
  text-decoration: none !important;
  opacity: 0.8;
}

/* Orangeheart 行内代码 */
.markdown-body code {
  font-size: 0.9rem !important;
  word-wrap: break-word !important;
  padding: 2px 4px !important;
  border-radius: 4px !important;
  margin: 0 2px !important;
  color: rgb(239, 112, 96) !important;
  background-color: rgba(27, 31, 35, 0.05) !important;
  font-family: Operator Mono, Consolas, Monaco, Menlo, monospace !important;
}

[data-theme="dark"] .markdown-body code {
  background-color: rgba(239, 112, 96, 0.1) !important;
  color: rgb(239, 112, 96) !important;
}

/* 代码块内的 code 不受行内代码样式影响 */
.markdown-body pre code {
  color: #24292f !important;
  background-color: transparent !important;
  padding: 16px 0 !important;
  border-radius: 0 !important;
  margin: 0 !important;
  font-size: 1em !important;
  word-wrap: normal !important;
}

[data-theme="dark"] .markdown-body pre code {
  color: #cdd6f4 !important;
  background-color: transparent !important;
}

/* Orangeheart 表格 */
.markdown-body table {
  display: table !important;
  text-align: left !important;
  border-spacing: 0 !important;
  border-collapse: collapse !important;
  margin: 0 0 16px 0 !important;
  width: 100% !important;
}

.markdown-body table tr {
  border: 0 !important;
  border-top: 1px solid #ccc !important;
  background-color: white !important;
}

.markdown-body table tr:nth-child(2n) {
  background-color: #F8F8F8 !important;
}

.markdown-body table tr th,
.markdown-body table tr td {
  font-size: 1rem !important;
  border: 1px solid #ccc !important;
  padding: 5px 10px !important;
  text-align: left !important;
}

.markdown-body table tr th {
  font-weight: bold !important;
  background-color: #f0f0f0 !important;
}

[data-theme="dark"] .markdown-body table tr {
  background-color: #1b1b1f !important;
  border-top-color: #2a2a4e !important;
}

[data-theme="dark"] .markdown-body table tr:nth-child(2n) {
  background-color: #1a1a2e !important;
}

[data-theme="dark"] .markdown-body table tr th,
[data-theme="dark"] .markdown-body table tr td {
  border-color: #2a2a4e !important;
}

[data-theme="dark"] .markdown-body table tr th {
  background-color: #252535 !important;
}

/* Orangeheart 图片 */
.markdown-body img {
  display: block !important;
  margin: 0 auto !important;
  max-width: 100% !important;
}

/* Orangeheart 分割线 */
.markdown-body hr {
  height: 0.25em !important;
  padding: 0 !important;
  margin: 24px 0 !important;
  background-color: #dfe2e5 !important;
  border: 0 !important;
}

[data-theme="dark"] .markdown-body hr {
  background-color: #2a2a4e !important;
}

[data-theme="dark"] .pagination-wrapper {
  border-top-color: #2a2a4e !important;
}

/* ========== 管理后台暗色模式 ========== */
[data-theme="dark"] .admin-layout {
  background: #181818 !important;
}

[data-theme="dark"] .admin-header {
  background: linear-gradient(135deg, #4a5a9a 0%, #5a3a8a 100%) !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
}

[data-theme="dark"] .admin-sidebar {
  background: #1e1e2e !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2) !important;
}

[data-theme="dark"] .sidebar-item {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .sidebar-item:hover {
  background: #2a2a3e !important;
  color: #a0b0f0 !important;
}

[data-theme="dark"] .sidebar-item.router-link-active {
  background: #2a2a3e !important;
  color: #a0b0f0 !important;
  border-left-color: #a0b0f0 !important;
}

[data-theme="dark"] .admin-main {
  background: #1e1e2e !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2) !important;
}

[data-theme="dark"] .admin-main .page-header h2 {
  color: #eaeaea !important;
}

[data-theme="dark"] .admin-main .search-wrapper {
  background: #252535 !important;
}

[data-theme="dark"] .admin-main .loading {
  color: #888 !important;
}

[data-theme="dark"] .admin-main .pagination-wrapper {
  border-top-color: #2a2a4e !important;
}

/* Element Plus 组件暗色模式 */
[data-theme="dark"] .admin-main .el-table {
  --el-table-bg-color: #1e1e2e !important;
  --el-table-tr-bg-color: #1e1e2e !important;
  --el-table-header-bg-color: #252535 !important;
  --el-table-row-hover-bg-color: #2a2a3e !important;
  --el-table-border-color: #2a2a4e !important;
  --el-table-text-color: #e0e0e0 !important;
  --el-table-header-text-color: #eaeaea !important;
}

[data-theme="dark"] .admin-main .el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell {
  background: #252535 !important;
}

[data-theme="dark"] .admin-main .el-input__wrapper,
[data-theme="dark"] .admin-main .el-textarea__inner {
  background-color: #252535 !important;
  box-shadow: 0 0 0 1px #3a3a5a inset !important;
  color: #e0e0e0 !important;
}

[data-theme="dark"] .admin-main .el-input__inner,
[data-theme="dark"] .admin-main .el-textarea__inner {
  color: #e0e0e0 !important;
}

[data-theme="dark"] .admin-main .el-select-dropdown {
  background-color: #1e1e2e !important;
  border-color: #2a2a4e !important;
}

[data-theme="dark"] .admin-main .el-form-item__label {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .admin-main .el-dialog {
  background-color: #1e1e2e !important;
}

[data-theme="dark"] .admin-main .el-dialog__title {
  color: #eaeaea !important;
}

[data-theme="dark"] .admin-main .el-tabs__item {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .admin-main .el-tabs__item.is-active {
  color: #a0b0f0 !important;
}

[data-theme="dark"] .admin-main .el-tabs__nav-wrap::after {
  background-color: #2a2a4e !important;
}

[data-theme="dark"] .admin-main .markdown-preview {
  color: #e0e0e0 !important;
}

[data-theme="dark"] .admin-main .markdown-body h1,
[data-theme="dark"] .admin-main .markdown-body h2,
[data-theme="dark"] .admin-main .markdown-body h3,
[data-theme="dark"] .admin-main .markdown-body h4 {
  color: #eaeaea !important;
}

[data-theme="dark"] .admin-main .markdown-body code {
  background-color: rgba(239, 112, 96, 0.1) !important;
  color: rgb(239, 112, 96) !important;
}

[data-theme="dark"] .admin-main .markdown-body pre {
  background-color: #1e1e2e !important;
  border-color: #2a2a4e !important;
}

[data-theme="dark"] .admin-main .markdown-body blockquote {
  color: #999 !important;
  border-left-color: rgb(239, 112, 96) !important;
  background: rgba(239, 112, 96, 0.06) !important;
}

/* 全局代码块样式 */
.markdown-body pre {
  padding: 0 !important;
  overflow-x: auto !important;
  font-size: 0.9em !important;
  line-height: 1.0 !important;
  background-color: #f6f8fa !important;
  border-radius: 6px !important;
  margin: 0 0 16px 0 !important;
  max-width: 100% !important;
  box-sizing: border-box !important;
  border: 1px solid #e8e8e8 !important;
  counter-reset: line !important;
  scrollbar-width: thin !important;
  scrollbar-color: #c1c1c1 transparent !important;
}

.markdown-body pre::-webkit-scrollbar {
  height: 6px;
}

.markdown-body pre::-webkit-scrollbar-track {
  background: transparent;
}

.markdown-body pre::-webkit-scrollbar-thumb {
  background-color: #c1c1c1;
  border-radius: 3px;
}

.markdown-body pre::-webkit-scrollbar-thumb:hover {
  background-color: #a0a0a0;
}

[data-theme="dark"] .markdown-body pre {
  background-color: #1e1e2e !important;
  border: 1px solid #2a2a4e !important;
  scrollbar-color: #45475a transparent !important;
}

[data-theme="dark"] .markdown-body pre::-webkit-scrollbar-thumb {
  background-color: #45475a;
}

[data-theme="dark"] .markdown-body pre::-webkit-scrollbar-thumb:hover {
  background-color: #585b70;
}

.markdown-body pre code {
  padding: 16px 0 !important;
  background-color: transparent !important;
  border-radius: 0 !important;
  font-size: 1em !important;
  color: #24292f !important;
  display: block !important;
  overflow-x: auto !important;
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

/* ========== Element Plus 弹出层暗色模式（全局） ========== */
/* 下拉选择框 */
[data-theme="dark"] .el-select-dropdown {
  background-color: #1e1e2e !important;
  border: 1px solid #2a2a4e !important;
}

[data-theme="dark"] .el-select-dropdown__item {
  color: #e0e0e0 !important;
}

[data-theme="dark"] .el-select-dropdown__item:hover,
[data-theme="dark"] .el-select-dropdown__item.hover {
  background-color: #2a2a3e !important;
}

[data-theme="dark"] .el-select-dropdown__item.is-selected {
  color: #667eea !important;
}

[data-theme="dark"] .el-select-dropdown__empty {
  color: #888 !important;
}

/* 日期选择器 */
[data-theme="dark"] .el-picker-panel {
  background-color: #1e1e2e !important;
  border-color: #2a2a4e !important;
  color: #e0e0e0 !important;
}

[data-theme="dark"] .el-picker-panel__body-wrapper,
[data-theme="dark"] .el-date-range-picker__content,
[data-theme="dark"] .el-date-picker__header,
[data-theme="dark"] .el-date-range-picker__header {
  background-color: #1e1e2e !important;
  color: #eaeaea !important;
}

[data-theme="dark"] .el-picker-panel__icon-btn {
  color: #b8b8b8 !important;
}

[data-theme="dark"] .el-picker-panel__icon-btn:hover {
  color: #667eea !important;
}

[data-theme="dark"] .el-date-table th {
  color: #888 !important;
  border-bottom-color: #2a2a4e !important;
}

[data-theme="dark"] .el-date-table td .el-date-table-cell {
  color: #e0e0e0 !important;
}

[data-theme="dark"] .el-date-table td.current:not(.disabled) .el-date-table-cell__text {
  background-color: #667eea !important;
}

[data-theme="dark"] .el-date-table td .el-date-table-cell:hover {
  color: #667eea !important;
}

[data-theme="dark"] .el-date-table td.next-month .el-date-table-cell,
[data-theme="dark"] .el-date-table td.prev-month .el-date-table-cell {
  color: #555 !important;
}

[data-theme="dark"] .el-date-table td.in-range .el-date-table-cell {
  background-color: rgba(102, 126, 234, 0.2) !important;
}

/* 输入框后缀图标 */
[data-theme="dark"] .el-input__suffix-inner .el-icon {
  color: #888 !important;
}

/* Select 输入框本身 */
[data-theme="dark"] .admin-main .el-select .el-select__wrapper {
  background-color: #252535 !important;
  box-shadow: 0 0 0 1px #3a3a5a inset !important;
}

[data-theme="dark"] .admin-main .el-select .el-select__placeholder {
  color: #888 !important;
}

[data-theme="dark"] .admin-main .el-select .el-select__caret {
  color: #888 !important;
}

/* Popper 箭头 */
[data-theme="dark"] .el-popper.is-dark {
  background-color: #1e1e2e !important;
  border-color: #2a2a4e !important;
  color: #e0e0e0 !important;
}

[data-theme="dark"] .el-popper.is-dark .el-popper__arrow::before {
  background-color: #1e1e2e !important;
  border-color: #2a2a4e !important;
}
</style>
