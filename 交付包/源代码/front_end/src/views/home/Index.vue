<template>
  <div class="home-page">
    <!-- 顶部导航栏 -->
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <el-icon :size="32" color="#409EFF">
            <Document />
          </el-icon>
          <span class="logo-text">云笔记</span>
        </div>
        <div class="header-actions">
          <el-button text @click="goToLogin">登录</el-button>
          <el-button type="primary" @click="goToRegister">注册</el-button>
        </div>
      </div>
    </header>

    <!-- Hero 区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">
          <span class="gradient-text">云端笔记</span>
          <br />
          随时随地记录灵感
        </h1>
        <p class="hero-description">
          简洁高效的云笔记平台，支持分类管理、标签搜索、实时同步
          <br />
          让您的想法永不丢失
        </p>
        <div class="hero-actions">
          <el-button type="primary" size="large" @click="goToRegister">
            <el-icon><Edit /></el-icon>
            开始使用
          </el-button>
          <el-button size="large" @click="goToLogin">
            <el-icon><User /></el-icon>
            立即登录
          </el-button>
        </div>
      </div>
      <div class="hero-image">
        <div class="floating-card card-1">
          <el-icon :size="40" color="#409EFF"><Document /></el-icon>
          <div class="card-text">我的笔记</div>
        </div>
        <div class="floating-card card-2">
          <el-icon :size="40" color="#67C23A"><Folder /></el-icon>
          <div class="card-text">分类管理</div>
        </div>
        <div class="floating-card card-3">
          <el-icon :size="40" color="#E6A23C"><Search /></el-icon>
          <div class="card-text">快速搜索</div>
        </div>
      </div>
    </section>

    <!-- 功能特性区域 -->
    <section class="features-section">
      <h2 class="section-title">核心功能</h2>
      <p class="section-subtitle">强大而简洁的笔记管理工具</p>
      
      <div class="features-grid">
        <div class="feature-card" v-for="feature in features" :key="feature.title">
          <div class="feature-icon" :style="{ background: feature.color }">
            <el-icon :size="32">
              <component :is="feature.icon" />
            </el-icon>
          </div>
          <h3 class="feature-title">{{ feature.title }}</h3>
          <p class="feature-description">{{ feature.description }}</p>
        </div>
      </div>
    </section>

    <!-- 统计数据区域 -->
    <section class="stats-section">
      <div class="stats-grid">
        <div class="stat-item" v-for="stat in stats" :key="stat.label">
          <div class="stat-value">{{ stat.value }}</div>
          <div class="stat-label">{{ stat.label }}</div>
        </div>
      </div>
    </section>

    <!-- CTA 区域 -->
    <section class="cta-section">
      <div class="cta-content">
        <h2 class="cta-title">准备好开始了吗？</h2>
        <p class="cta-description">立即注册，开启您的云笔记之旅</p>
        <el-button type="primary" size="large" @click="goToRegister">
          免费注册
        </el-button>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="footer-content">
        <div class="footer-left">
          <el-icon :size="24" color="#909399">
            <Document />
          </el-icon>
          <span>云笔记 © 2024</span>
        </div>
        <div class="footer-right">
          <span>基于 Vue 3 + Spring Boot 构建</span>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { 
  Document, 
  Edit, 
  User, 
  Folder, 
  Search,
  Lock,
  Connection,
  TrendCharts
} from '@element-plus/icons-vue'

const router = useRouter()

// 功能特性数据
const features = [
  {
    icon: Edit,
    title: '快速创建',
    description: '一键创建笔记，支持富文本编辑，让记录更加便捷',
    color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)'
  },
  {
    icon: Folder,
    title: '分类管理',
    description: '灵活的分类系统，让您的笔记井井有条',
    color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)'
  },
  {
    icon: Search,
    title: '智能搜索',
    description: '强大的搜索功能，快速找到您需要的内容',
    color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)'
  },
  {
    icon: Lock,
    title: '安全可靠',
    description: 'JWT 认证保护，数据加密存储，安全无忧',
    color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)'
  },
  {
    icon: Connection,
    title: '实时同步',
    description: '云端存储，多端同步，随时随地访问',
    color: 'linear-gradient(135deg, #fa709a 0%, #fee140 100%)'
  },
  {
    icon: TrendCharts,
    title: '数据统计',
    description: '笔记数量、分类统计，了解您的记录习惯',
    color: 'linear-gradient(135deg, #30cfd0 0%, #330867 100%)'
  }
]

// 统计数据
const stats = [
  { value: '100%', label: '免费使用' },
  { value: '∞', label: '无限笔记' },
  { value: '7x24', label: '全天候访问' },
  { value: '< 1s', label: '响应速度' }
]

// 跳转到登录页
const goToLogin = () => {
  router.push('/login')
}

// 跳转到注册页
const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #fff;
}

/* 顶部导航栏 */
.header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #e4e7ed;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  height: 100%;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.header-actions {
  display: flex;
  gap: 12px;
}

/* Hero 区域 */
.hero-section {
  padding: 120px 24px 80px;
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-content {
  animation: fadeInUp 0.8s ease-out;
}

.hero-title {
  font-size: 56px;
  font-weight: 800;
  line-height: 1.2;
  color: #303133;
  margin-bottom: 24px;
}

.gradient-text {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-description {
  font-size: 18px;
  line-height: 1.8;
  color: #606266;
  margin-bottom: 40px;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.hero-image {
  position: relative;
  height: 400px;
  animation: fadeInRight 0.8s ease-out;
}

.floating-card {
  position: absolute;
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  animation: float 3s ease-in-out infinite;
}

.card-1 {
  top: 20px;
  left: 40px;
  animation-delay: 0s;
}

.card-2 {
  top: 140px;
  right: 60px;
  animation-delay: 0.5s;
}

.card-3 {
  bottom: 40px;
  left: 80px;
  animation-delay: 1s;
}

.card-text {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

/* 功能特性区域 */
.features-section {
  padding: 80px 24px;
  background: #f5f7fa;
}

.section-title {
  font-size: 40px;
  font-weight: bold;
  text-align: center;
  color: #303133;
  margin-bottom: 12px;
}

.section-subtitle {
  font-size: 18px;
  text-align: center;
  color: #909399;
  margin-bottom: 60px;
}

.features-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 32px;
}

.feature-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px;
  text-align: center;
  transition: transform 0.3s, box-shadow 0.3s;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 24px;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.feature-title {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 12px;
}

.feature-description {
  font-size: 14px;
  line-height: 1.6;
  color: #606266;
}

/* 统计数据区域 */
.stats-section {
  padding: 80px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stats-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
}

.stat-item {
  text-align: center;
  color: #fff;
}

.stat-value {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

/* CTA 区域 */
.cta-section {
  padding: 100px 24px;
  background: #fff;
}

.cta-content {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.cta-title {
  font-size: 40px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 16px;
}

.cta-description {
  font-size: 18px;
  color: #606266;
  margin-bottom: 40px;
}

/* 页脚 */
.footer {
  padding: 32px 24px;
  background: #f5f7fa;
  border-top: 1px solid #e4e7ed;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.footer-left,
.footer-right {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 14px;
  color: #909399;
}

/* 动画 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(30px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-20px);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .hero-section {
    grid-template-columns: 1fr;
    padding: 80px 24px 60px;
  }

  .hero-title {
    font-size: 36px;
  }

  .hero-image {
    display: none;
  }

  .section-title {
    font-size: 32px;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .footer-content {
    flex-direction: column;
    gap: 16px;
  }
}
</style>
