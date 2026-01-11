<template>
  <div class="note-list-page">
    <!-- 顶部操作栏 -->
    <div class="page-header fade-in-up">
      <div>
        <h2>我的笔记</h2>
        <div v-if="filterTag" class="filter-tag">
          <span>筛选标签：</span>
          <el-tag closable @close="clearTagFilter" type="primary">
            {{ filterTag }}
          </el-tag>
        </div>
      </div>
      <el-button type="primary" :icon="Plus" @click="createNote" class="hover-lift active-scale">
        新建笔记
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <div class="search-bar fade-in-up-delay-1">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索笔记标题或内容..."
        :prefix-icon="Search"
        clearable
        class="glass-input"
        @keyup.enter="handleSearch"
        @clear="handleClearSearch"
      >
        <template #append>
          <el-button :icon="Search" @click="handleSearch" class="active-scale">搜索</el-button>
        </template>
      </el-input>
    </div>

    <!-- 笔记列表 -->
    <div v-loading="noteStore.loading" class="note-list fade-in-up-delay-2">
      <el-empty
        v-if="!noteStore.hasNotes && !noteStore.loading"
        description="还没有笔记，快去创建一个吧！"
      >
        <el-button type="primary" @click="createNote" class="bounce-in">创建笔记</el-button>
      </el-empty>

      <div v-else class="note-grid">
        <el-card
          v-for="(note, index) in noteStore.noteList"
          :key="note.noteId"
          class="note-card glass-card hover-lift active-scale"
          :class="`fade-in-up-delay-${Math.min(index % 6 + 1, 3)}`"
          shadow="hover"
          @click="viewNote(note.noteId)"
        >
          <div class="note-card-header">
            <h3 class="note-title">{{ note.title }}</h3>
            <el-dropdown @command="(cmd) => handleNoteAction(cmd, note)">
              <el-icon class="more-icon"><MoreFilled /></el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="edit">
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-dropdown-item>
                  <el-dropdown-item command="delete" divided>
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>

          <div class="note-content">
            {{ stripHtml(note.content) || '暂无内容' }}
          </div>

          <div class="note-footer">
            <div class="footer-left">
              <el-tag v-if="note.categoryId" size="small" type="info">
                {{ getCategoryName(note.categoryId) }}
              </el-tag>
              <div v-if="note.tags" class="note-tags">
                <el-tag
                  v-for="tag in note.tags.split(',').slice(0, 3)"
                  :key="tag"
                  size="small"
                  effect="plain"
                  class="tag-item"
                >
                  {{ tag.trim() }}
                </el-tag>
                <el-tag v-if="note.tags.split(',').length > 3" size="small" type="info">
                  +{{ note.tags.split(',').length - 3 }}
                </el-tag>
              </div>
            </div>
            <span class="note-time">
              {{ formatTime(note.updatedAt) }}
            </span>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 分页 -->
    <div v-if="noteStore.hasNotes" class="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="noteStore.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNoteStore, useCategoryStore } from '@/store'
import { ElMessageBox, ElMessage } from 'element-plus'
import { Plus, Search, MoreFilled, Edit, Delete } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

const router = useRouter()
const route = useRoute()
const noteStore = useNoteStore()
const categoryStore = useCategoryStore()

// 搜索关键词
const searchKeyword = ref('')

// 标签筛选
const filterTag = ref(route.query.tag || '')

// 分页
const currentPage = ref(1)
const pageSize = ref(10)

// 组件挂载时加载笔记列表
onMounted(() => {
  loadNotes()
})

// 加载笔记列表
const loadNotes = () => {
  noteStore.fetchNoteList(currentPage.value, pageSize.value)
}

// 搜索笔记
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    noteStore.searchNotes(searchKeyword.value, currentPage.value, pageSize.value)
  } else {
    loadNotes()
  }
}

// 清除搜索
const handleClearSearch = () => {
  searchKeyword.value = ''
  noteStore.clearSearch()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pageSize.value = size
  loadNotes()
}

// 页码改变
const handlePageChange = (page) => {
  currentPage.value = page
  loadNotes()
}

// 创建笔记
const createNote = () => {
  router.push('/notes/create')
}

// 查看笔记
const viewNote = (noteId) => {
  router.push(`/notes/${noteId}`)
}

// 处理笔记操作
const handleNoteAction = (command, note) => {
  if (command === 'edit') {
    router.push(`/notes/${note.noteId}/edit`)
  } else if (command === 'delete') {
    handleDelete(note)
  }
}

// 删除笔记
const handleDelete = (note) => {
  ElMessageBox.confirm(
    `确定要删除笔记"${note.title}"吗？`,
    '删除确认',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    noteStore.deleteNote(note.noteId)
  }).catch(() => {
    // 取消删除
  })
}

// 获取分类名称
const getCategoryName = (categoryId) => {
  return categoryStore.getCategoryName(categoryId)
}

// 格式化时间
const formatTime = (time) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

// 清除标签筛选
const clearTagFilter = () => {
  filterTag.value = ''
  router.push('/notes')
  loadNotes()
}

// 去除 HTML 标签，用于内容预览
const stripHtml = (html) => {
  if (!html) return ''
  const tmp = document.createElement('div')
  tmp.innerHTML = html
  return tmp.textContent || tmp.innerText || ''
}
</script>

<style scoped>
.note-list-page {
  padding: 24px;
  min-height: calc(100vh - 60px); /* 减去 header 高度 */
  background: transparent;
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

.search-bar {
  margin-bottom: 24px;
}

.note-list {
  min-height: 400px;
}

.note-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.note-card {
  cursor: pointer;
  border-radius: 16px;
  overflow: hidden;
}

.note-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
}

.note-title {
  font-size: 18px;
  color: #303133;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  flex: 1;
}

.more-icon {
  font-size: 20px;
  color: #909399;
  cursor: pointer;
}

.more-icon:hover {
  color: #409EFF;
}

.note-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
  margin-bottom: 12px;
  height: 60px;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.note-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 12px;
  border-top: 1px solid #e4e7ed;
  gap: 12px;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  flex: 1;
}

.note-tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}

.tag-item {
  cursor: pointer;
  transition: all 0.3s;
}

.tag-item:hover {
  transform: scale(1.05);
}

.filter-tag {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 8px;
  font-size: 14px;
  color: #606266;
}

.note-time {
  font-size: 12px;
  color: #909399;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 32px;
  padding: 20px 0;
  background: #fff;
  border-radius: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .note-list-page {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .page-header h2 {
    font-size: 20px;
  }

  .note-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }

  .pagination {
    overflow-x: auto;
  }
}

@media (max-width: 480px) {
  .note-list-page {
    padding: 12px;
  }

  .page-header h2 {
    font-size: 18px;
  }

  .search-bar {
    margin-bottom: 16px;
  }
}
</style>
