<template>
  <div class="auth-layout">
    <!-- 顶部导航栏 -->
    <header class="auth-header">
      <div class="header-content">
        <div class="logo" @click="goToHome">
          <el-icon :size="28" color="#409EFF">
            <Document />
          </el-icon>
          <span class="logo-text">云笔记</span>
        </div>
        <div class="header-nav">
          <el-button text @click="goToHome">
            <el-icon><HomeFilled /></el-icon>
            首页
          </el-button>
        </div>
      </div>
    </header>

    <div class="auth-container">
      <!-- 左侧：品牌展示 -->
      <div class="auth-brand">
        <div class="brand-content">
          <el-icon :size="80" color="#fff">
            <Document />
          </el-icon>
          <h1>云笔记</h1>
          <p>记录生活，分享知识</p>
          <div class="features">
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>云端同步</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>多端访问</span>
            </div>
            <div class="feature-item">
              <el-icon><Check /></el-icon>
              <span>安全可靠</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：表单区域 -->
      <div class="auth-form">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { Document, HomeFilled, Check } from '@element-plus/icons-vue'

const router = useRouter()

// 返回首页
const goToHome = () => {
  router.push('/')
}
</script>

<style scoped>
.auth-layout {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;
}

/* 顶部导航栏 */
.auth-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 64px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
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
  transition: opacity 0.3s;
}

.logo:hover {
  opacity: 0.8;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #fff;
}

.header-nav {
  display: flex;
  gap: 12px;
}

.header-nav .el-button {
  color: #fff;
  font-size: 15px;
}

.header-nav .el-button:hover {
  background: rgba(255, 255, 255, 0.1);
}

.auth-layout::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: float 20s ease-in-out infinite;
}

.auth-layout::after {
  content: '';
  position: absolute;
  bottom: -50%;
  left: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
  animation: float 25s ease-in-out infinite reverse;
}

.auth-container {
  display: flex;
  width: 100%;
  max-width: 1100px;
  min-height: 640px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 20px 80px rgba(0, 0, 0, 0.2);
  position: relative;
  z-index: 1;
  animation: slideUp 0.8s ease-out;
  margin: auto;
  margin-top: 84px;
  margin-bottom: 20px;
}

/* 左侧品牌区 */
.auth-brand {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  color: white;
  position: relative;
  overflow: hidden;
}

.auth-brand::before {
  content: '';
  position: absolute;
  top: -100px;
  right: -100px;
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  animation: pulse 4s ease-in-out infinite;
}

.auth-brand::after {
  content: '';
  position: absolute;
  bottom: -150px;
  left: -150px;
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 50%;
  animation: pulse 6s ease-in-out infinite reverse;
}

.brand-content {
  text-align: center;
  position: relative;
  z-index: 1;
  animation: fadeIn 1s ease-out;
}

.brand-content .el-icon {
  animation: bounce 2s ease-in-out infinite;
}

.brand-content h1 {
  font-size: 52px;
  margin: 24px 0 12px;
  font-weight: bold;
  letter-spacing: 2px;
}

.brand-content p {
  font-size: 18px;
  opacity: 0.95;
  margin-bottom: 48px;
  letter-spacing: 1px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 24px;
  margin-top: 48px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 16px;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  backdrop-filter: blur(10px);
  transition: all 0.3s;
}

.feature-item:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateX(10px);
}

.feature-item .el-icon {
  font-size: 22px;
}

/* 右侧表单区 */
.auth-form {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 60px;
  background: #fafafa;
}

/* 动画 */
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes float {
  0%, 100% {
    transform: translate(0, 0) rotate(0deg);
  }
  33% {
    transform: translate(30px, -30px) rotate(120deg);
  }
  66% {
    transform: translate(-20px, 20px) rotate(240deg);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1);
    opacity: 0.3;
  }
  50% {
    transform: scale(1.1);
    opacity: 0.5;
  }
}

@keyframes bounce {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-15px);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .auth-container {
    flex-direction: column;
    min-height: auto;
  }

  .auth-brand {
    padding: 40px 20px;
  }

  .brand-content h1 {
    font-size: 36px;
  }

  .brand-content p {
    font-size: 16px;
  }

  .features {
    margin-top: 32px;
  }

  .auth-form {
    padding: 40px 20px;
  }
}
</style>
