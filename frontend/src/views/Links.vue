<template>
  <div class="links">
    <div class="content">
      <h2>友情链接</h2>
      <div class="link-list">
        <a v-for="link in links" :key="link.id" :href="link.url" target="_blank" class="link-item">
          <div class="link-icon">🔗</div>
          <div class="link-info">
            <h3 class="link-name">{{ link.name }}</h3>
            <p class="link-desc">{{ link.description }}</p>
          </div>
        </a>
      </div>
    </div>
    <BackToTop />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import BackToTop from '../components/BackToTop.vue'
import { linkApi } from '../api/index'

const links = ref([])

const loadLinks = async () => {
  try {
    const res = await linkApi.list()
    links.value = res.data
  } catch (error) {
    console.error('加载链接失败', error)
  }
}

onMounted(() => {
  loadLinks()
})
</script>

<style scoped>
.links {
  min-height: 100vh;
}

.content {
  max-width: 800px;
  margin: 0 auto;
  padding: 50px 20px;
}

.content h2 {
  margin-bottom: 35px;
  color: #333;
  font-size: 1.7rem;
}

.link-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}

.link-item {
  background: white;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: transform 0.3s, box-shadow 0.3s;
}

.link-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.15);
}

.link-icon {
  font-size: 2rem;
}

.link-info h3 {
  color: #333;
  font-size: 1.2rem;
  margin-bottom: 5px;
}

.link-info p {
  color: #999;
  font-size: 0.9rem;
  margin: 0;
}
</style>
