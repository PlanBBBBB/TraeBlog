// 原始文章数据，供博客页面使用
export const mockArticles = [
  {
    id: 1,
    title: 'AIGC 广告推荐系统',
    date: '2024-08-04',
    tags: 'AIGC, 推荐系统',
    category: 'AI/ML',
    summary: '本项目是一个基于 AIGC 的个性化广告推荐系统，区别于传统推荐系统，本系统通过当前流行的人工智能技术，替代了传统的内容过滤和协同过滤等算法，进行广告推荐。',
    content: '详细内容...'
  },
  {
    id: 2,
    title: 'Redis 相关知识 - 底层数据结构',
    date: '2024-07-28',
    tags: 'Redis, 数据库',
    category: '数据库',
    summary: '深入理解 Redis 的底层数据结构，包括 String、List、Hash、Set、ZSet 等数据类型的实现原理。',
    content: '详细内容...'
  },
  {
    id: 3,
    title: 'Redis 持久化机制详解',
    date: '2024-07-20',
    tags: 'Redis, 数据库',
    category: '数据库',
    summary: '详细介绍 Redis 的 RDB 和 AOF 两种持久化机制的工作原理、配置方法以及优缺点对比。',
    content: '详细内容...'
  },
  {
    id: 4,
    title: 'JVM 内存结构深度解析',
    date: '2024-07-15',
    tags: 'Java, JVM',
    category: '后端开发',
    summary: '深入了解 JVM 的内存结构，包括堆、栈、方法区、程序计数器等各部分的作用和特点。',
    content: '详细内容...'
  },
  {
    id: 5,
    title: 'JVM 垃圾回收机制',
    date: '2024-07-10',
    tags: 'Java, JVM',
    category: '后端开发',
    summary: '学习 JVM 的垃圾回收机制，包括 GC 算法、垃圾回收器的种类和选择策略。',
    content: '详细内容...'
  },
  {
    id: 6,
    title: 'MySQL 索引优化实战',
    date: '2024-07-05',
    tags: 'MySQL, 数据库',
    category: '数据库',
    summary: 'MySQL 索引的最佳实践，包括索引设计原则、执行计划分析、慢查询优化等内容。',
    content: '详细内容...'
  },
  {
    id: 7,
    title: 'MySQL 事务与隔离级别',
    date: '2024-06-28',
    tags: 'MySQL, 数据库',
    category: '数据库',
    summary: '深入理解 MySQL 事务的 ACID 特性以及四种隔离级别的实现原理和区别。',
    content: '详细内容...'
  },
  {
    id: 8,
    title: 'Vue 3 组合式 API 详解',
    date: '2024-06-20',
    tags: 'Vue, 前端',
    category: '前端',
    summary: '全面学习 Vue 3 的组合式 API，包括 ref、reactive、computed、watch 等核心概念。',
    content: '详细内容...'
  },
  {
    id: 9,
    title: 'Vue Router 路由管理',
    date: '2024-06-15',
    tags: 'Vue, 前端',
    category: '前端',
    summary: 'Vue Router 的使用指南，包括路由配置、动态路由、导航守卫等高级功能。',
    content: '详细内容...'
  },
  {
    id: 10,
    title: 'Spring Boot 快速入门',
    date: '2024-06-10',
    tags: 'Spring Boot, Java',
    category: '后端开发',
    summary: '从零开始学习 Spring Boot，包括项目创建、配置、依赖管理等基础知识。',
    content: '详细内容...'
  },
  {
    id: 11,
    title: 'Spring Boot 数据访问层',
    date: '2024-06-05',
    tags: 'Spring Boot, Java',
    category: '后端开发',
    summary: 'Spring Boot 中使用 JPA、MyBatis 等数据访问框架的最佳实践。',
    content: '详细内容...'
  },
  {
    id: 12,
    title: 'Spring Security 安全框架',
    date: '2024-05-28',
    tags: 'Spring Security, Java',
    category: '后端开发',
    summary: 'Spring Security 的核心概念和使用方法，包括认证、授权、权限管理等。',
    content: '详细内容...'
  },
  {
    id: 13,
    title: 'Docker 容器化部署',
    date: '2024-05-20',
    tags: 'Docker, 工具',
    category: '工具',
    summary: 'Docker 的基础使用，包括镜像管理、容器操作、Dockerfile 编写等内容。',
    content: '详细内容...'
  },
  {
    id: 14,
    title: 'Git 版本控制详解',
    date: '2024-05-15',
    tags: 'Git, 工具',
    category: '工具',
    summary: 'Git 的常用命令和工作流程，包括分支管理、代码合并、冲突解决等技巧。',
    content: '详细内容...'
  },
  {
    id: 15,
    title: 'Linux 常用命令大全',
    date: '2024-05-10',
    tags: 'Linux, 工具',
    category: '工具',
    summary: 'Linux 系统常用命令汇总，包括文件操作、进程管理、网络配置等实用命令。',
    content: '详细内容...'
  },
  {
    id: 16,
    title: 'Python 基础语法',
    date: '2024-05-05',
    tags: 'Python, 后端开发',
    category: '后端开发',
    summary: 'Python 编程语言的基础语法学习，包括数据类型、控制流、函数等内容。',
    content: '详细内容...'
  },
  {
    id: 17,
    title: 'Python 数据分析入门',
    date: '2024-04-28',
    tags: 'Python, 数据分析',
    category: 'AI/ML',
    summary: '使用 Python 进行数据分析，包括 NumPy、Pandas、Matplotlib 等库的使用。',
    content: '详细内容...'
  },
  {
    id: 18,
    title: '机器学习基础算法',
    date: '2024-04-20',
    tags: '机器学习, AI',
    category: 'AI/ML',
    summary: '机器学习的基础算法介绍，包括线性回归、逻辑回归、决策树等经典算法。',
    content: '详细内容...'
  },
  {
    id: 19,
    title: 'React 组件开发',
    date: '2024-04-15',
    tags: 'React, 前端',
    category: '前端',
    summary: 'React 组件化开发的最佳实践，包括组件设计、Props 传递、状态管理等。',
    content: '详细内容...'
  },
  {
    id: 20,
    title: 'TypeScript 类型系统',
    date: '2024-04-10',
    tags: 'TypeScript, 前端',
    category: '前端',
    summary: 'TypeScript 的类型系统详解，包括接口、泛型、类型体操等高级特性。',
    content: '详细内容...'
  },
  {
    id: 21,
    title: 'Node.js Web 开发',
    date: '2024-04-05',
    tags: 'Node.js, 后端开发',
    category: '后端开发',
    summary: '使用 Node.js 和 Express 框架进行 Web 应用开发的完整指南。',
    content: '详细内容...'
  },
  {
    id: 22,
    title: 'MongoDB 数据库入门',
    date: '2024-03-28',
    tags: 'MongoDB, 数据库',
    category: '数据库',
    summary: 'MongoDB NoSQL 数据库的基础使用，包括文档操作、索引、聚合等内容。',
    content: '详细内容...'
  },
  {
    id: 23,
    title: 'Elasticsearch 搜索引擎',
    date: '2024-03-20',
    tags: 'Elasticsearch, 数据库',
    category: '数据库',
    summary: 'Elasticsearch 搜索引擎的使用，包括索引创建、文档搜索、聚合分析等。',
    content: '详细内容...'
  },
  {
    id: 24,
    title: 'RabbitMQ 消息队列',
    date: '2024-03-15',
    tags: 'RabbitMQ, 后端开发',
    category: '后端开发',
    summary: 'RabbitMQ 消息队列的使用，包括消息模型、交换机、队列等核心概念。',
    content: '详细内容...'
  },
  {
    id: 25,
    title: 'Kubernetes 容器编排',
    date: '2024-03-10',
    tags: 'Kubernetes, 工具',
    category: '工具',
    summary: 'Kubernetes 容器编排平台的入门教程，包括 Pod、Service、Deployment 等概念。',
    content: '详细内容...'
  },
  {
    id: 26,
    title: 'Nginx 反向代理配置',
    date: '2024-03-05',
    tags: 'Nginx, 工具',
    category: '工具',
    summary: 'Nginx 的反向代理配置详解，包括负载均衡、静态资源服务、SSL 配置等。',
    content: '详细内容...'
  },
  {
    id: 27,
    title: 'Zookeeper 分布式协调服务',
    date: '2024-02-28',
    tags: 'Zookeeper, 后端开发',
    category: '后端开发',
    summary: 'Zookeeper 的核心概念和使用场景，包括分布式锁、配置管理、服务发现等。',
    content: '详细内容...'
  },
  {
    id: 28,
    title: 'Redis Cluster 集群部署',
    date: '2024-02-20',
    tags: 'Redis, 数据库',
    category: '数据库',
    summary: 'Redis Cluster 集群的搭建和管理，包括分片机制、主从复制、故障转移等。',
    content: '详细内容...'
  },
  {
    id: 29,
    title: 'Spring Cloud 微服务架构',
    date: '2024-02-15',
    tags: 'Spring Cloud, 后端开发',
    category: '后端开发',
    summary: 'Spring Cloud 微服务架构的核心组件，包括 Eureka、Ribbon、Feign、Hystrix 等。',
    content: '详细内容...'
  },
  {
    id: 30,
    title: '微前端架构实践',
    date: '2024-02-10',
    tags: '微前端, 前端',
    category: '前端',
    summary: '微前端架构的设计理念和实践，包括 single-spa、qiankun 等框架的使用。',
    content: '详细内容...'
  },
  {
    id: 31,
    title: 'Vue 3 + TypeScript 最佳实践',
    date: '2024-02-05',
    tags: 'Vue, TypeScript, 前端',
    category: '前端',
    summary: 'Vue 3 与 TypeScript 结合的最佳实践，包括类型定义、组合式 API 使用等。',
    content: '详细内容...'
  },
  {
    id: 32,
    title: 'GraphQL 查询语言',
    date: '2024-01-28',
    tags: 'GraphQL, 后端开发',
    category: '后端开发',
    summary: 'GraphQL 的核心概念和使用，包括 Schema、Query、Mutation、Subscription 等。',
    content: '详细内容...'
  },
  {
    id: 33,
    title: 'CI/CD 持续集成部署',
    date: '2024-01-20',
    tags: 'CI/CD, 工具',
    category: '工具',
    summary: 'CI/CD 持续集成部署的最佳实践，包括 Jenkins、GitLab CI、GitHub Actions 等。',
    content: '详细内容...'
  },
  {
    id: 34,
    title: 'Kafka 消息队列详解',
    date: '2024-01-15',
    tags: 'Kafka, 后端开发',
    category: '后端开发',
    summary: 'Kafka 消息队列的核心概念和使用，包括 Topic、Partition、Consumer Group 等。',
    content: '详细内容...'
  },
  {
    id: 35,
    title: 'CSS Grid 布局完全指南',
    date: '2024-01-10',
    tags: 'CSS, 前端',
    category: '前端',
    summary: 'CSS Grid 布局的完整指南，包括网格定义、轨道大小、对齐方式等高级特性。',
    content: '详细内容...'
  },
  {
    id: 36,
    title: 'Flexbox 弹性布局详解',
    date: '2024-01-05',
    tags: 'CSS, 前端',
    category: '前端',
    summary: 'Flexbox 弹性布局的详细教程，包括容器属性、项目属性、常见布局实现。',
    content: '详细内容...'
  },
  {
    id: 37,
    title: 'Webpack 5 模块打包',
    date: '2023-12-28',
    tags: 'Webpack, 前端',
    category: '前端',
    summary: 'Webpack 5 模块打包器的使用，包括配置、Loader、Plugin、性能优化等。',
    content: '详细内容...'
  },
  {
    id: 38,
    title: 'Vite 新一代构建工具',
    date: '2023-12-20',
    tags: 'Vite, 前端',
    category: '前端',
    summary: 'Vite 新一代前端构建工具的使用，包括开发服务器、构建优化、插件开发等。',
    content: '详细内容...'
  },
  {
    id: 39,
    title: 'Python Django Web 框架',
    date: '2023-12-15',
    tags: 'Python, Django, 后端开发',
    category: '后端开发',
    summary: 'Django Web 框架的使用，包括 MTV 架构、ORM、Admin、中间件等。',
    content: '详细内容...'
  },
  {
    id: 40,
    title: 'Python Flask 轻量级框架',
    date: '2023-12-10',
    tags: 'Python, Flask, 后端开发',
    category: '后端开发',
    summary: 'Flask 轻量级 Web 框架的使用，包括路由、模板、扩展等。',
    content: '详细内容...'
  }
]

// 管理后台使用的可变数据
import { ref } from 'vue'

let articlesData = [...mockArticles]
let nextArticleId = 41

const articles = ref(articlesData)

export const articleApi = {
  getAll() {
    return [...articles.value]
  },
  getById(id) {
    return articles.value.find(a => a.id === id)
  },
  add(article) {
    const newArticle = {
      ...article,
      id: nextArticleId++
    }
    articles.value.push(newArticle)
    return newArticle
  },
  update(id, article) {
    const index = articles.value.findIndex(a => a.id === id)
    if (index !== -1) {
      articles.value[index] = { ...articles.value[index], ...article }
      return articles.value[index]
    }
    return null
  },
  delete(id) {
    const index = articles.value.findIndex(a => a.id === id)
    if (index !== -1) {
      articles.value.splice(index, 1)
      return true
    }
    return false
  }
}

export const categoryApi = {
  getAll() {
    const categoryMap = {}
    articles.value.forEach(article => {
      if (categoryMap[article.category]) {
        categoryMap[article.category]++
      } else {
        categoryMap[article.category] = 1
      }
    })
    return Object.entries(categoryMap).map(([name, count]) => ({ name, count }))
  },
  add(name) {
    const categoryMap = {}
    articles.value.forEach(article => {
      categoryMap[article.category] = true
    })
    if (categoryMap[name]) {
      return false
    }
    articles.value.push({
      id: nextArticleId++,
      title: `新分类文章 - ${name}`,
      date: new Date().toISOString().split('T')[0],
      tags: name,
      category: name,
      summary: '新分类的第一篇文章',
      content: '文章内容...'
    })
    return true
  },
  update(oldName, newName) {
    articles.value.forEach(article => {
      if (article.category === oldName) {
        article.category = newName
      }
    })
    return true
  },
  delete(name) {
    const hasArticles = articles.value.some(a => a.category === name)
    if (hasArticles) {
      return false
    }
    return true
  }
}

export const tagApi = {
  getAll() {
    const tagMap = {}
    articles.value.forEach(article => {
      const tags = article.tags.split(',').map(t => t.trim())
      tags.forEach(tag => {
        if (tagMap[tag]) {
          tagMap[tag]++
        } else {
          tagMap[tag] = 1
        }
      })
    })
    return Object.entries(tagMap).map(([name, count]) => ({ name, count }))
  },
  add(name) {
    const tagMap = {}
    articles.value.forEach(article => {
      const tags = article.tags.split(',').map(t => t.trim())
      tags.forEach(tag => {
        tagMap[tag] = true
      })
    })
    if (tagMap[name]) {
      return false
    }
    articles.value.push({
      id: nextArticleId++,
      title: `新标签文章 - ${name}`,
      date: new Date().toISOString().split('T')[0],
      tags: name,
      category: '其他',
      summary: '新标签的第一篇文章',
      content: '文章内容...'
    })
    return true
  },
  update(oldName, newName) {
    articles.value.forEach(article => {
      const tags = article.tags.split(',').map(t => t.trim())
      const newTags = tags.map(tag => tag === oldName ? newName : tag)
      article.tags = newTags.join(', ')
    })
    return true
  },
  delete(name) {
    const hasArticles = articles.value.some(a => {
      const tags = a.tags.split(',').map(t => t.trim())
      return tags.includes(name)
    })
    if (hasArticles) {
      return false
    }
    return true
  }
}

// 友情链接数据
let linksData = [
  { id: 1, name: '阮一峰的网络日志', url: 'https://www.ruanyifeng.com/', description: '技术博客，分享编程和互联网技术' },
  { id: 2, name: '廖雪峰的官方网站', url: 'https://www.liaoxuefeng.com/', description: 'Python、Git、JavaScript、区块链等教程' },
  { id: 3, name: 'GitHub', url: 'https://github.com/', description: '全球最大的代码托管平台' },
  { id: 4, name: 'Stack Overflow', url: 'https://stackoverflow.com/', description: '全球最大的程序员问答社区' },
  { id: 5, name: 'MDN Web Docs', url: 'https://developer.mozilla.org/', description: 'Web 开发文档和教程' },
  { id: 6, name: '掘金', url: 'https://juejin.cn/', description: '开发者社区，分享技术内容' },
  { id: 7, name: 'SegmentFault', url: 'https://segmentfault.com/', description: '思否，中文技术社区' },
  { id: 8, name: 'V2EX', url: 'https://www.v2ex.com/', description: '创意工作者社区' },
  { id: 9, name: '知乎', url: 'https://www.zhihu.com/', description: '问答社区，你值得拥有' },
  { id: 10, name: 'CSDN', url: 'https://www.csdn.net/', description: '中文 IT 社区' },
  { id: 11, name: '51CTO', url: 'https://www.51cto.com/', description: '中国 IT 技术门户网站' },
  { id: 12, name: '开源中国', url: 'https://www.oschina.net/', description: '开源技术社区' }
]
let nextLinkId = 13

const links = ref(linksData)

export const linkApi = {
  getAll() {
    return [...links.value]
  },
  getById(id) {
    return links.value.find(l => l.id === id)
  },
  add(link) {
    const newLink = {
      ...link,
      id: nextLinkId++
    }
    links.value.push(newLink)
    return newLink
  },
  update(id, link) {
    const index = links.value.findIndex(l => l.id === id)
    if (index !== -1) {
      links.value[index] = { ...links.value[index], ...link }
      return links.value[index]
    }
    return null
  },
  delete(id) {
    const index = links.value.findIndex(l => l.id === id)
    if (index !== -1) {
      links.value.splice(index, 1)
      return true
    }
    return false
  }
}
