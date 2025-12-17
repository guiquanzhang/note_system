<template>
  <div class="note-detail-page">
    <div v-loading="loading" class="detail-container">
      <div v-if="note" class="note-content-wrapper">
        <!-- 头部操作栏 -->
        <div class="page-header">
          <el-button :icon="ArrowLeft" @click="goBack">返回</el-button>
          <div class="header-actions">
            <el-button :icon="Edit" @click="editNote">编辑</el-button>
            <el-button :icon="Delete" type="danger" @click="handleDelete">删除</el-button>
          </div>
        </div>

        <!-- 笔记标题 -->
        <h1 class="note-title">{{ note.title }}</h1>

        <!-- 笔记元信息 -->
        <div class="note-meta">
          <el-tag v-if="note.categoryId" size="small" type="info">
            {{ getCategoryName(note.categoryId) }}
          </el-tag>
          <span class="meta-item">
            <el-icon><Clock /></el-icon>
            创建于 {{ formatTime(note.createdAt) }}
          </span>
          <span class="meta-item">
            <el-icon><Edit /></el-icon>
            更新于 {{ formatTime(note.updatedAt) }}
          </span>
        </div>

        <!-- 标签 -->
        <div v-if="note.tags" class="note-tags">
          <el-tag
            v-for="tag in note.tags.split(',')"
            :key="tag"
            size="small"
            effect="plain"
          >
            {{ tag.trim() }}
          </el-tag>
        </div>

        <!-- 笔记内容 -->
        <div class="note-content ql-editor" v-html="note.content"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNoteStore, useCategoryStore } from '@/store'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Edit, Delete, Clock } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const router = useRouter()
const route = useRoute()
const noteStore = useNoteStore()
const categoryStore = useCategoryStore()

const noteId = route.params.id
const loading = ref(false)
const note = ref(null)

// 组件挂载
onMounted(async () => {
  await loadNoteDetail()
})

// 加载笔记详情
const loadNoteDetail = async () => {
  try {
    loading.value = true
    note.value = await noteStore.fetchNoteDetail(noteId)
  } catch (error) {
    ElMessage.error('加载笔记失败')
    goBack()
  } finally {
    loading.value = false
  }
}

// 编辑笔记
const editNote = () => {
  router.push(`/notes/${noteId}/edit`)
}

// 删除笔记
const handleDelete = () => {
  ElMessageBox.confirm(
    `确定要删除笔记"${note.value.title}"吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    await noteStore.deleteNote(noteId)
    goBack()
  }).catch(() => {
    // 取消删除
  })
}

// 返回
const goBack = () => {
  router.push('/notes')
}

// 获取分类名称
const getCategoryName = (categoryId) => {
  return categoryStore.getCategoryName(categoryId)
}

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<style scoped>
.note-detail-page {
  height: 100%;
  background: #fff;
}

.detail-container {
  height: 100%;
  overflow-y: auto;
}

.note-content-wrapper {
  max-width: 900px;
  margin: 0 auto;
  padding: 24px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.note-title {
  font-size: 32px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 16px 0;
  line-height: 1.4;
}

.note-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #909399;
}

.meta-item .el-icon {
  font-size: 16px;
}

.note-tags {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.note-content {
  font-size: 16px;
  line-height: 1.8;
  color: #303133;
  padding: 24px 0;
  min-height: 300px;
}

/* 使用 Quill 编辑器的样式来渲染内容 */
.note-content :deep(h1),
.note-content :deep(h2),
.note-content :deep(h3) {
  margin-top: 24px;
  margin-bottom: 16px;
  font-weight: 600;
}

.note-content :deep(p) {
  margin-bottom: 12px;
}

.note-content :deep(ul),
.note-content :deep(ol) {
  padding-left: 24px;
  margin-bottom: 12px;
}

.note-content :deep(blockquote) {
  border-left: 4px solid #409EFF;
  padding-left: 16px;
  margin: 16px 0;
  color: #606266;
}

.note-content :deep(code) {
  background: #f5f7fa;
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.note-content :deep(pre) {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 4px;
  overflow-x: auto;
  margin: 16px 0;
}

.note-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 4px;
  margin: 16px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .note-content-wrapper {
    padding: 16px;
  }

  .note-title {
    font-size: 24px;
  }

  .note-content {
    font-size: 15px;
  }

  .header-actions {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .note-content-wrapper {
    padding: 12px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }

  .note-title {
    font-size: 20px;
  }

  .note-content {
    font-size: 14px;
  }

  .note-meta {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
