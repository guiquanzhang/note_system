<template>
  <div class="tag-list-page">
    <!-- 顶部操作栏 -->
    <div class="page-header fade-in-up">
      <h2>标签管理</h2>
      <el-button type="primary" :icon="Plus" @click="showAddDialog" class="hover-lift active-scale">
        新建标签
      </el-button>
    </div>

    <!-- 标签统计 -->
    <div class="tag-stats fade-in-up-delay-1">
      <el-card class="glass-card stat-card">
        <div class="stat-item">
          <el-icon :size="32" color="#667eea"><PriceTag /></el-icon>
          <div class="stat-info">
            <div class="stat-value">{{ tagList.length }}</div>
            <div class="stat-label">标签总数</div>
          </div>
        </div>
      </el-card>
      <el-card class="glass-card stat-card">
        <div class="stat-item">
          <el-icon :size="32" color="#f5576c"><Document /></el-icon>
          <div class="stat-info">
            <div class="stat-value">{{ totalNotes }}</div>
            <div class="stat-label">笔记总数</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 标签云 -->
    <div class="tag-cloud-section fade-in-up-delay-2">
      <el-card class="glass-card">
        <template #header>
          <div class="card-header">
            <span>标签云</span>
            <el-tooltip content="标签大小表示使用频率">
              <el-icon><QuestionFilled /></el-icon>
            </el-tooltip>
          </div>
        </template>
        <div v-if="tagList.length > 0" class="tag-cloud">
          <div
            v-for="tag in tagList"
            :key="tag.name"
            class="tag-item hover-lift active-scale"
            :style="getTagStyle(tag.count)"
            @click="filterByTag(tag.name)"
          >
            <span>{{ tag.name }}</span>
            <el-badge :value="tag.count" class="tag-badge" />
          </div>
        </div>
        <el-empty v-else description="还没有标签" />
      </el-card>
    </div>

    <!-- 标签列表 -->
    <div class="tag-list-section fade-in-up-delay-3">
      <el-card class="glass-card">
        <template #header>
          <div class="card-header">
            <span>标签列表</span>
            <el-input
              v-model="searchKeyword"
              placeholder="搜索标签..."
              :prefix-icon="Search"
              clearable
              style="width: 300px"
            />
          </div>
        </template>
        <el-table :data="filteredTagList" stripe class="tag-table">
          <el-table-column prop="name" label="标签名称" min-width="200">
            <template #default="{ row }">
              <el-tag :color="getRandomColor(row.name)" effect="light" size="large">
                {{ row.name }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="count" label="使用次数" width="150" sortable>
            <template #default="{ row }">
              <el-badge :value="row.count" :max="999" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button
                type="primary"
                size="small"
                :icon="Search"
                @click="filterByTag(row.name)"
                class="active-scale"
              >
                查看笔记
              </el-button>
              <el-button
                type="danger"
                size="small"
                :icon="Delete"
                @click="handleDelete(row)"
                class="active-scale"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <!-- 新建/编辑标签对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      class="glass-modal"
    >
      <el-form :model="tagForm" label-width="80px">
        <el-form-item label="标签名称">
          <el-input
            v-model="tagForm.name"
            placeholder="请输入标签名称"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <el-form-item label="标签颜色">
          <el-color-picker v-model="tagForm.color" show-alpha />
          <span style="margin-left: 12px; color: #909399;">选择标签颜色（可选）</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false" class="active-scale">取消</el-button>
        <el-button type="primary" @click="handleSave" class="active-scale">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Search,
  Delete,
  PriceTag,
  Document,
  QuestionFilled
} from '@element-plus/icons-vue'
import { getTagList, createTag, deleteTag } from '@/api/tag'

const router = useRouter()

// 标签列表
const tagList = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新建标签')
const tagForm = ref({
  name: '',
  color: ''
})

// 总笔记数
const totalNotes = computed(() => {
  return tagList.value.reduce((sum, tag) => sum + tag.count, 0)
})

// 过滤后的标签列表
const filteredTagList = computed(() => {
  if (!searchKeyword.value) return tagList.value
  return tagList.value.filter(tag =>
    tag.name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

// 组件挂载
onMounted(async () => {
  await loadTags()
})

// 加载标签
const loadTags = async () => {
  try {
    tagList.value = await getTagList()
  } catch (error) {
    ElMessage.error('加载标签失败')
  }
}

// 显示新建对话框
const showAddDialog = () => {
  dialogTitle.value = '新建标签'
  tagForm.value = { name: '', color: getRandomColor('') }
  dialogVisible.value = true
}

// 保存标签
const handleSave = async () => {
  if (!tagForm.value.name.trim()) {
    ElMessage.warning('请输入标签名称')
    return
  }
  
  try {
    await createTag({
      name: tagForm.value.name.trim(),
      color: tagForm.value.color
    })
    ElMessage.success('创建成功')
    dialogVisible.value = false
    await loadTags()
  } catch (error) {
    ElMessage.error(error.message || '创建失败')
  }
}

// 删除标签
const handleDelete = (tag) => {
  ElMessageBox.confirm(
    `确定要删除标签"${tag.name}"吗？这将从所有笔记中移除该标签。`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteTag(tag.tagId)
      ElMessage.success('删除成功')
      await loadTags()
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    // 取消删除
  })
}

// 按标签筛选笔记
const filterByTag = (tagName) => {
  router.push(`/notes?tag=${encodeURIComponent(tagName)}`)
}

// 获取标签样式（根据使用次数）
const getTagStyle = (count) => {
  const maxCount = Math.max(...tagList.value.map(t => t.count), 1)
  const minSize = 14
  const maxSize = 32
  const size = minSize + (count / maxCount) * (maxSize - minSize)
  
  return {
    fontSize: `${size}px`,
    padding: `${size / 4}px ${size / 2}px`
  }
}

// 获取随机颜色
const getRandomColor = (str) => {
  const colors = [
    '#667eea', '#764ba2', '#f093fb', '#f5576c',
    '#4facfe', '#00f2fe', '#43e97b', '#38f9d7',
    '#fa709a', '#fee140', '#30cfd0', '#330867'
  ]
  if (!str) return colors[Math.floor(Math.random() * colors.length)]
  
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash)
  }
  return colors[Math.abs(hash) % colors.length]
}
</script>

<style scoped>
.tag-list-page {
  padding: 24px;
  min-height: calc(100vh - 60px);
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

/* 统计卡片 */
.tag-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 16px;
  overflow: hidden;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 标签云 */
.tag-cloud-section {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  padding: 20px;
  min-height: 200px;
  align-items: center;
  justify-content: center;
}

.tag-item {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.1), rgba(118, 75, 162, 0.1));
  border: 2px solid rgba(102, 126, 234, 0.3);
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  font-weight: 500;
  color: #667eea;
}

.tag-item:hover {
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.2), rgba(118, 75, 162, 0.2));
  border-color: rgba(102, 126, 234, 0.5);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.tag-badge {
  margin-left: 4px;
}

/* 标签列表 */
.tag-list-section {
  margin-bottom: 24px;
}

.tag-table {
  border-radius: 12px;
  overflow: hidden;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .tag-list-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .tag-stats {
    grid-template-columns: 1fr;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .card-header .el-input {
    width: 100% !important;
  }

  .tag-cloud {
    padding: 12px;
  }
}
</style>
