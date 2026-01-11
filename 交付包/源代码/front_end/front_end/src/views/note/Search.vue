<template>
  <div class="search-page">
    <div class="page-header">
      <h2>搜索笔记</h2>
    </div>

    <el-card class="search-card">
      <div class="search-box">
        <el-input
          v-model="keyword"
          placeholder="输入关键词搜索笔记标题或内容"
          size="large"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
          <template #append>
            <el-button type="primary" @click="handleSearch" :loading="loading">
              搜索
            </el-button>
          </template>
        </el-input>
      </div>

      <div v-if="hasSearched" class="search-result">
        <div class="result-header">
          <span v-if="total > 0">找到 {{ total }} 条相关笔记</span>
          <span v-else>未找到相关笔记</span>
        </div>

        <div v-if="noteList.length > 0" class="note-list">
          <el-card
            v-for="note in noteList"
            :key="note.noteId"
            class="note-card"
            shadow="hover"
            @click="goToNote(note.noteId)"
          >
            <div class="note-title">{{ note.title || '无标题' }}</div>
            <div class="note-content">{{ getContentPreview(note.content) }}</div>
            <div class="note-meta">
              <span>{{ formatTime(note.updatedAt) }}</span>
              <span v-if="note.tags" class="note-tags">{{ note.tags }}</span>
            </div>
          </el-card>
        </div>

        <div v-if="total > pageSize" class="pagination">
          <el-pagination
            v-model:current-page="pageNum"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handleSearch"
          />
        </div>
      </div>

      <div v-else class="search-tip">
        <el-icon :size="48" color="#909399"><Search /></el-icon>
        <p>输入关键词搜索笔记</p>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { searchNotes } from '@/api/note'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import dayjs from 'dayjs'

const router = useRouter()
const keyword = ref('')
const noteList = ref([])
const total = ref(0)
const pageNum = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const hasSearched = ref(false)

const handleSearch = async () => {
  if (!keyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  loading.value = true
  hasSearched.value = true

  try {
    const data = await searchNotes({
      keyword: keyword.value.trim(),
      pageNum: pageNum.value,
      pageSize: pageSize.value
    })
    noteList.value = data.records || []
    total.value = data.total || 0
  } catch (error) {
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

const goToNote = (noteId) => {
  router.push(`/notes/${noteId}/edit`)
}

const getContentPreview = (content) => {
  if (!content) return '无内容'
  const text = content.replace(/<[^>]+>/g, '')
  return text.length > 100 ? text.substring(0, 100) + '...' : text
}

const formatTime = (time) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm') : '-'
}
</script>

<style scoped>
.search-page {
  padding: 24px;
  max-width: 900px;
  margin: 0 auto;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
  margin: 0 0 24px 0;
}

.search-card {
  border-radius: 8px;
}

.search-box {
  margin-bottom: 24px;
}

.search-result {
  margin-top: 16px;
}

.result-header {
  font-size: 14px;
  color: #909399;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.note-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.note-card {
  cursor: pointer;
  transition: all 0.3s;
}

.note-card:hover {
  transform: translateX(4px);
  border-left: 3px solid #409EFF;
}

.note-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.note-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 8px;
}

.note-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.note-tags {
  color: #409EFF;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.search-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 60px 0;
  color: #909399;
}

.search-tip p {
  margin-top: 16px;
  font-size: 14px;
}

@media (max-width: 768px) {
  .search-page {
    padding: 16px;
  }
}
</style>
