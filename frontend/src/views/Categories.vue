<template>
  <div class="categories">
    <div class="content">
      <h2>所有分类</h2>
      <div class="category-list">
        <router-link v-for="category in categories" :key="category.id" :to="'/category/' + category.name" class="category-item">
          <div class="category-icon">📁</div>
          <div class="category-info">
            <h3 class="category-name">{{ category.name }}</h3>
          </div>
        </router-link>
      </div>
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import BackToTop from '../components/BackToTop.vue'
import { categoryApi } from '../api/index'

const categories = ref([])

const loadCategories = async () => {
  try {
    const res = await categoryApi.list()
    categories.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.categories {
  min-height: 100vh;
  background: var(--bg-primary);
  transition: background 0.3s;
}

.content {
  max-width: 800px;
  margin: 0 auto;
  padding: 50px 20px;
}

.content h2 {
  margin-bottom: 35px;
  color: var(--text-primary);
  font-size: 1.7rem;
  transition: color 0.3s;
}

.category-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.category-item {
  background: var(--bg-secondary);
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 2px 10px var(--shadow-color);
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: transform 0.3s, box-shadow 0.3s, background 0.3s;
}

.category-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px var(--shadow-color);
}

.category-icon {
  font-size: 2rem;
}

.category-info h3 {
  color: var(--text-primary);
  font-size: 1.2rem;
  margin-bottom: 5px;
  transition: color 0.3s;
}
</style>
