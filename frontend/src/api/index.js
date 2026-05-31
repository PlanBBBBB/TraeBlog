import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: 'http://localhost:8083/api',
  timeout: 10000
})

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

api.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code === 200) {
      return res
    } else {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export const articleApi = {
  list(page = 0, size = 6) {
    return api.get('/articles', { params: { page, size } })
  },
  detail(id) {
    return api.get(`/articles/${id}`)
  },
  verifyPassword(id, password) {
    return api.post(`/articles/${id}/verify-password`, { password })
  },
  listByCategory(category, page = 0, size = 6) {
    return api.get(`/categories/${category}/articles`, { params: { page, size } })
  },
  listByTag(tag, page = 0, size = 6) {
    return api.get(`/tags/${tag}/articles`, { params: { page, size } })
  },
  listAdmin(params = {}) {
    const { page = 0, size = 10, title, category, tag, startDate, endDate } = params
    return api.get('/admin/articles', { params: { page, size, title, category, tag, startDate, endDate } })
  },
  add(data) {
    return api.post('/admin/articles', data)
  },
  update(id, data) {
    return api.put(`/admin/articles/${id}`, data)
  },
  delete(id) {
    return api.delete(`/admin/articles/${id}`)
  }
}

export const categoryApi = {
  list() {
    return api.get('/categories')
  },
  listAll() {
    return api.get('/admin/categories', { params: { page: 0, size: 1000 } })
  },
  listAdmin(params = {}) {
    const { page = 0, size = 10, name, startDate, endDate } = params
    return api.get('/admin/categories', { params: { page, size, name, startDate, endDate } })
  },
  add(data) {
    return api.post('/admin/categories', data)
  },
  update(id, data) {
    return api.put(`/admin/categories/${id}`, data)
  },
  delete(id) {
    return api.delete(`/admin/categories/${id}`)
  }
}

export const tagApi = {
  list() {
    return api.get('/tags')
  },
  listAll() {
    return api.get('/admin/tags', { params: { page: 0, size: 1000 } })
  },
  listAdmin(params = {}) {
    const { page = 0, size = 10, name, startDate, endDate } = params
    return api.get('/admin/tags', { params: { page, size, name, startDate, endDate } })
  },
  add(data) {
    return api.post('/admin/tags', data)
  },
  update(id, data) {
    return api.put(`/admin/tags/${id}`, data)
  },
  delete(id) {
    return api.delete(`/admin/tags/${id}`)
  }
}

export const linkApi = {
  list() {
    return api.get('/links')
  },
  listAdmin(params = {}) {
    const { page = 0, size = 10, keyword, startDate, endDate } = params
    return api.get('/admin/links', { params: { page, size, keyword, startDate, endDate } })
  },
  add(data) {
    return api.post('/admin/links', data)
  },
  update(id, data) {
    return api.put(`/admin/links/${id}`, data)
  },
  delete(id) {
    return api.delete(`/admin/links/${id}`)
  }
}

export const authApi = {
  login(username, password) {
    return api.post('/admin/login', { username, password })
  },
  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('isLoggedIn')
  }
}
