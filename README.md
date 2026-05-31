# TraeBlog

一个现代化的个人博客系统，采用前后端分离架构，由 VibeCoding 全栈实现。

## 📋 目录

- [项目简介](#项目简介)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [功能介绍](#功能介绍)
- [快速开始](#快速开始)
- [关于作者](#关于作者)

## 项目简介

TraeBlog 是一个功能完善的个人博客系统，支持文章发布、分类管理、标签管理、友链展示等功能。系统采用现代化的技术栈，提供美观的用户界面和流畅的用户体验。

## 技术栈

### 后端技术栈
- **框架**: Spring Boot 3.2.5
- **语言**: Java 21
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.7
- **安全**: Spring Security + JWT
- **构建工具**: Maven
- **其他**: Lombok

### 前端技术栈
- **框架**: Vue 3.4.21 (Composition API)
- **路由**: Vue Router 4.3.0
- **状态管理**: Pinia 2.1.7
- **UI组件库**: Element Plus 2.6.3
- **HTTP客户端**: Axios 1.6.8
- **Markdown解析**: Marked 18.0.3
- **构建工具**: Vite 5.2.0

## 项目结构

```
TraeBlog/
├── backend/                    # 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/planb/blog/
│   │   │   │   ├── common/     # 公共类
│   │   │   │   ├── config/     # 配置类
│   │   │   │   ├── controller/ # 控制器
│   │   │   │   ├── dto/        # 数据传输对象
│   │   │   │   ├── entity/     # 实体类
│   │   │   │   ├── mapper/     # 数据访问层
│   │   │   │   ├── security/   # 安全相关
│   │   │   │   ├── service/    # 业务逻辑层
│   │   │   │   └── util/       # 工具类
│   │   │   └── resources/
│   │   │       └── application.yml
│   └── pom.xml
│
└── frontend/                   # 前端项目
    ├── src/
    │   ├── api/               # API接口
    │   ├── assets/            # 静态资源
    │   ├── components/        # 组件
    │   ├── data/              # 数据
    │   ├── router/            # 路由
    │   ├── views/             # 页面
    │   │   ├── admin/         # 管理后台页面
    │   │   └── ...            # 前端页面
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 功能介绍

### 前端功能
- 🏠 **首页**: 展示最新文章
- 📝 **文章列表**: 浏览所有文章
- 📄 **文章详情**: 查看文章完整内容
- 📁 **分类**: 按分类浏览文章
- 🏷️ **标签**: 按标签浏览文章
- 🔗 **友链**: 展示友情链接
- 👤 **关于**: 关于页面

### 后台管理功能
- 🔐 **管理员登录**: JWT身份认证
- 📝 **文章管理**: 增删改查文章
- 📁 **分类管理**: 管理文章分类
- 🏷️ **标签管理**: 管理文章标签
- 🔗 **友链管理**: 管理友情链接

## 快速开始

### 后端启动

1. 配置数据库连接信息，修改 `backend/src/main/resources/application.yml`
2. 确保已安装 Java 21 和 Maven
3. 启动后端服务：
```bash
cd backend
mvn spring-boot:run
```
后端服务将在 `http://localhost:8083` 启动

### 前端启动

1. 确保已安装 Node.js 和 npm
2. 安装依赖：
```bash
cd frontend
npm install
```
3. 启动开发服务器：
```bash
npm run dev
```
前端将在 `http://localhost:5173` 启动

## 关于作者

本项目由 **VibeCoding** 全栈实现，包括前端开发、后端开发、数据库设计等全部工作。