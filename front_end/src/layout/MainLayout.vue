<template>
  <div class="main-layout">
    <!-- 顶部导航栏 -->
    <el-header class="main-header">
      <div class="header-left">
        <el-icon :size="28" color="#409EFF">
          <Document />
        </el-icon>
        <span class="logo-text">云笔记</span>
      </div>

      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="36" :icon="UserFilled" />
            <span class="username">{{ userStore.username }}</span>
            <el-icon><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人信息
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>
                设置
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <!-- 主体内容区 -->
    <el-container class="main-container">
      <!-- 左侧边栏 -->
      <el-aside width="240px" class="main-aside">
        <el-menu
          :default-active="activeMenu"
          class="aside-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/notes">
            <el-icon><Document /></el-icon>
            <span>我的笔记</span>
          </el-menu-item>
          
          <el-menu-item index="/categories">
            <el-icon><Folder /></el-icon>
            <span>分类管理</span>
          </el-menu-item>

          <el-menu-item index="/search">
            <el-icon><Search /></el-icon>
            <span>搜索笔记</span>
          </el-menu-item>
        </el-menu>

        <!-- 分类列表 -->
        <div class="category-section">
          <div class="section-header">
            <span>分类</span>
            <el-button
              :icon="Plus"
              circle
              size="small"
              @click="showAddCategory"
            />
          </div>
          
          <div v-if="categoryStore.loading" class="loading-text">
            加载中...
          </div>
          
          <div v-else-if="categoryStore.hasCategories" class="category-list">
            <div
              v-for="category in categoryStore.categoryList"
              :key="category.categoryId"
              class="category-item"
              @click="selectCategory(category.categoryId)"
            >
              <el-icon><Folder /></el-icon>
              <span>{{ category.name }}</span>
            </div>
          </div>
          
          <div v-else class="empty-text">
            暂无分类
          </div>
        </div>
      </el-aside>

      <!-- 右侧内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore, useCategoryStore } from '@/store'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  Document,
  UserFilled,
  ArrowDown,
  User,
  Setting,
  SwitchButton,
  Folder,
  Search,
  Plus
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const categoryStore = useCategoryStore()

// 当前激活的菜单
const activeMenu = computed(() => route.path)

// 组件挂载时加载分类列表
onMounted(() => {
  categoryStore.fetchCategoryList()
})

// 处理菜单选择
const handleMenuSelect = (index) => {
  router.push(index)
}

// 处理用户下拉菜单
const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人信息功能开发中...')
      break
    case 'settings':
      ElMessage.info('设置功能开发中...')
      break
    case 'logout':
      handleLogout()
      break
  }
}

// 退出登录
const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.logout()
    router.push('/login')
  }).catch(() => {
    // 取消退出
  })
}

// 显示添加分类对话框
const showAddCategory = () => {
  ElMessageBox.prompt('请输入分类名称', '新建分类', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /\S+/,
    inputErrorMessage: '分类名称不能为空'
  }).then(({ value }) => {
    categoryStore.createCategory({ name: value })
  }).catch(() => {
    // 取消
  })
}

// 选择分类
const selectCategory = (categoryId) => {
  router.push(`/notes?categoryId=${categoryId}`)
}
</script>

<style scoped>
.main-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.main-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #606266;
}

/* 主体容器 */
.main-container {
  flex: 1;
  overflow: hidden;
}

/* 左侧边栏 */
.main-aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
  display: flex;
  flex-direction: column;
}

.aside-menu {
  border-right: none;
}

/* 分类区域 */
.category-section {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 14px;
  font-weight: bold;
  color: #606266;
}

.category-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-size: 14px;
  color: #606266;
}

.category-item:hover {
  background-color: #f5f7fa;
}

.loading-text,
.empty-text {
  text-align: center;
  font-size: 14px;
  color: #909399;
  padding: 20px 0;
}

/* 右侧内容区 */
.main-content {
  background: #f5f7fa;
  overflow-y: auto;
}
</style>
