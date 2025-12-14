<template>
  <div class="note-edit-page">
    <div class="page-header">
      <el-button :icon="ArrowLeft" @click="goBack">返回</el-button>
      <div class="header-actions">
        <el-button @click="goBack">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">
          {{ saving ? '保存中...' : '保存' }}
        </el-button>
      </div>
    </div>

    <div class="edit-container">
      <el-form :model="noteForm" label-position="top">
        <el-form-item label="标题">
          <el-input
            v-model="noteForm.title"
            placeholder="请输入笔记标题"
            size="large"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="分类">
          <el-select
            v-model="noteForm.categoryId"
            placeholder="请选择分类（可选）"
            clearable
            style="width: 300px"
          >
            <el-option
              v-for="category in categoryStore.categoryList"
              :key="category.categoryId"
              :label="category.name"
              :value="category.categoryId"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="标签">
          <el-input
            v-model="noteForm.tags"
            placeholder="多个标签用逗号分隔，如：学习,Java,Spring"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="内容">
          <el-input
            v-model="noteForm.content"
            type="textarea"
            :rows="15"
            placeholder="请输入笔记内容..."
          />
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useNoteStore, useCategoryStore } from '@/store'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const noteStore = useNoteStore()
const categoryStore = useCategoryStore()

// 笔记ID（编辑模式）
const noteId = route.params.id

// 是否为编辑模式
const isEditMode = !!noteId

// 保存状态
const saving = ref(false)

// 笔记表单
const noteForm = reactive({
  title: '',
  content: '',
  categoryId: null,
  tags: ''
})

// 组件挂载
onMounted(async () => {
  // 加载分类列表
  await categoryStore.fetchCategoryList()

  // 如果是编辑模式，加载笔记详情
  if (isEditMode) {
    await loadNoteDetail()
  }
})

// 加载笔记详情
const loadNoteDetail = async () => {
  try {
    const note = await noteStore.fetchNoteDetail(noteId)
    noteForm.title = note.title
    noteForm.content = note.content || ''
    noteForm.categoryId = note.categoryId
    noteForm.tags = note.tags || ''
  } catch (error) {
    ElMessage.error('加载笔记失败')
    goBack()
  }
}

// 保存笔记
const handleSave = async () => {
  // 验证标题
  if (!noteForm.title.trim()) {
    ElMessage.warning('请输入笔记标题')
    return
  }

  try {
    saving.value = true

    if (isEditMode) {
      // 更新笔记
      await noteStore.updateNote(noteId, noteForm)
    } else {
      // 创建笔记
      await noteStore.createNote(noteForm)
    }

    // 保存成功，返回列表
    goBack()
  } catch (error) {
    console.error('保存失败:', error)
  } finally {
    saving.value = false
  }
}

// 返回
const goBack = () => {
  router.push('/notes')
}
</script>

<style scoped>
.note-edit-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 24px;
  border-bottom: 1px solid #e4e7ed;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.edit-container {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}
</style>
