import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Articles from '../views/Articles.vue'
import ArticleDetail from '../views/ArticleDetail.vue'
import Tags from '../views/Tags.vue'
import Categories from '../views/Categories.vue'
import Links from '../views/Links.vue'
import About from '../views/About.vue'
import TagDetail from '../views/TagDetail.vue'
import CategoryDetail from '../views/CategoryDetail.vue'
import Login from '../views/admin/Login.vue'
import AdminLayout from '../views/admin/Layout.vue'
import ArticleManage from '../views/admin/ArticleManage.vue'
import CategoryManage from '../views/admin/CategoryManage.vue'
import TagManage from '../views/admin/TagManage.vue'
import LinkManage from '../views/admin/LinkManage.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/articles', name: 'Articles', component: Articles },
  { path: '/article/:id', name: 'ArticleDetail', component: ArticleDetail },
  { path: '/tags', name: 'Tags', component: Tags },
  { path: '/tag/:tagName', name: 'TagDetail', component: TagDetail },
  { path: '/categories', name: 'Categories', component: Categories },
  { path: '/category/:categoryName', name: 'CategoryDetail', component: CategoryDetail },
  { path: '/links', name: 'Links', component: Links },
  { path: '/about', name: 'About', component: About },
  { path: '/admin/login', name: 'AdminLogin', component: Login },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminLayout,
    redirect: '/admin/articles',
    children: [
      { path: 'articles', name: 'AdminArticles', component: ArticleManage },
      { path: 'categories', name: 'AdminCategories', component: CategoryManage },
      { path: 'tags', name: 'AdminTags', component: TagManage },
      { path: 'links', name: 'AdminLinks', component: LinkManage }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true'
  
  // 如果是去管理后台（除了登录页面），需要检查登录状态
  if (to.path.startsWith('/admin') && to.path !== '/admin/login') {
    if (!isLoggedIn) {
      next('/admin/login')
    } else {
      next()
    }
  } else if (to.path === '/admin/login' && isLoggedIn) {
    // 如果已经登录，直接去文章管理页面
    next('/admin/articles')
  } else {
    next()
  }
})

export default router
