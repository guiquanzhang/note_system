<template>
  <div class="trash-page">
    <div class="page-header">
      <div class="header-left">
        <h2>回收站</h2>
        <span class="note-count">共 {{ total }} 条笔记</span>
      </div>
      <div class="header-right">
        <el-button 
          type="danger" 
          :disabled="total === 0"
          @click="handleEmptyTrash"
        >
          <el-icon><Delete /></el-icon>
          清空回收站
        </el-button>
      </div>
    </div>

    <el-alert
      v-if="total === 0"
      title="回收站为空"
      type="info"
      :closable="false"
      show-icon
      class="empty-alert"
    >
      <template #default>
        <p>回收站中的笔记会在 30 天后自动永久删除</p>
      </template>
    </el-alert>

    <div v-else class="note-list">
      <el-card
        v-for="note in noteList"
        :key="note.noteId"
        class="note-card"
        shadow="hover"
      >
        <div class="note-header">
          <h3 class="note-title">{{ note.title || '无标题' }}</h3>
          <div class="note-actions">
            <el-button
              type="primary"
              size="small"
              @click="handleRestore(note.noteId)"
            >
              <el-icon><RefreshRight /></el-icon>
              恢复
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handlePermanentDelete(note.noteId)"
            >
              <el-icon><Delete /></el-icon>
              永久删除
            </el-button>
          </div>
        </div>

        <div class="note-content">
          <p class="note-preview">{{ getContentPreview(note.content) }}</p>
        </div>

        <div class="note-footer">
          <div class="note-meta">
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              删除时间：{{ formatTime(note.deletedAt) }}
            </span>
            <span class="meta-item" v-if="note.tags">
              <el-icon><PriceTag /></el-icon>
              {{ note.tags }}
            </span>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 分页 -->
    <div v-if="total > 0" class="pagination">
      <el-pagination
        v-model:current-page="pageNum"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadNotes"
        @current-change="loadNotes"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDeletedNotes, restoreNote, permanentlyDeleteNote, emptyTrash } from '@/api/note'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Delete, RefreshRight, Calendar, PriceTag } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

// 笔记列表
const noteList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)

// 加载笔记列表
const loadNotes = async () => {
  try {
    const data = await getDeletedNotes({
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    noteList.value = data.records || []
    total.value = data.total || 0
  } catch (error) {
    ElMessage.error('加载失败')
  }
}

// 恢复笔记
const handleRestore = async (noteId) => {
  try {
    await ElMessageBox.confirm(
      '确定要恢复这条笔记吗？',
      '恢复笔记',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    await restoreNote(noteId)
    ElMessage.success('恢复成功')
    loadNotes()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('恢复失败:', error)
    }
  }
}

// 永久删除笔记
const handlePermanentDelete = async (noteId) => {
  try {
    await ElMessageBox.confirm(
      '永久删除后无法恢复，确定要删除吗？',
      '永久删除',
      {
        confirmButtonText: '确定删除',
        cancelButtonText: '取消',
        type: 'error'
      }
    )

    await permanentlyDeleteNote(noteId)
    ElMessage.success('删除成功')
    loadNotes()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
    }
  }
}

// 清空回收站
const handleEmptyTrash = async () => {
  try {
    await ElMessageBox.confirm(
      '清空回收站后，所有笔记将被永久删除且无法恢复，确定要继续吗？',
      '清空回收站',
      {
        confirmButtonText: '确定清空',
        cancelButtonText: '取消',
        type: 'error'
      }
    )

    await emptyTrash()
    ElMessage.success('回收站已清空')
    loadNotes()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清空失败:', error)
    }
  }
}

// 获取内容预览
const getContentPreview = (content) => {
  if (!content) return '无内容'
  return content.length > 100 ? content.substring(0, 100) + '...' : content
}

// 格式化时间
const formatTime = (time) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : '-'
}

// 组件挂载时加载数据
onMounted(() => {
  loadNotes()
})
</script>

<style scoped>
.trash-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.header-left h2 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.note-count {
  font-size: 14px;
  color: #909399;
}

.empty-alert {
  margin-bottom: 24px;
}

.note-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.note-card {
  transition: all 0.3s;
}

.note-card:hover {
  transform: translateY(-2px);
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.note-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin: 0;
  flex: 1;
}

.note-actions {
  display: flex;
  gap: 8px;
}

.note-content {
  margin-bottom: 12px;
}

.note-preview {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin: 0;
}

.note-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #ebeef5;
}

.note-meta {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #909399;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .trash-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .header-left {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }

  .note-header {
    flex-direction: column;
    gap: 12px;
  }

  .note-actions {
    width: 100%;
  }

  .note-actions .el-button {
    flex: 1;
  }

  .note-meta {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
