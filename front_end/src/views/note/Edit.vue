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
          <el-select
            v-model="selectedTags"
            multiple
            filterable
            allow-create
            default-first-option
            placeholder="选择或输入标签，按回车添加"
            style="width: 100%"
            @change="handleTagsChange"
          >
            <el-option
              v-for="tag in availableTags"
              :key="tag.tagId"
              :label="tag.name"
              :value="tag.name"
            >
              <span :style="{ color: tag.color || '#667eea' }">{{ tag.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{ tag.count }} 次</span>
            </el-option>
          </el-select>
          <div v-if="selectedTags.length > 0" class="selected-tags">
            <el-tag
              v-for="tag in selectedTags"
              :key="tag"
              closable
              @close="removeTag(tag)"
              style="margin-right: 8px; margin-top: 8px"
            >
              {{ tag }}
            </el-tag>
          </div>
        </el-form-item>

        <el-form-item label="内容" class="editor-form-item">
          <QuillEditor
            ref="quillEditorRef"
            v-model:content="noteForm.content"
            contentType="html"
            theme="snow"
            :toolbar="toolbarOptions"
            placeholder="请输入笔记内容..."
            class="note-editor"
            @ready="onEditorReady"
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
import { QuillEditor } from '@vueup/vue-quill'
import { uploadNoteImage } from '@/api/file'
import { processNoteContent } from '@/utils/content'
import { getTagList } from '@/api/tag'

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

// 编辑器引用
const quillEditorRef = ref(null)

// 可用标签列表
const availableTags = ref([])

// 选中的标签
const selectedTags = ref([])

// 笔记表单
const noteForm = reactive({
  title: '',
  content: '',
  categoryId: null,
  tags: ''
})

// Quill 编辑器工具栏配置
const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'],        // 加粗、斜体、下划线、删除线
  ['blockquote', 'code-block'],                     // 引用、代码块
  [{ 'header': 1 }, { 'header': 2 }],               // 标题
  [{ 'list': 'ordered'}, { 'list': 'bullet' }],     // 有序列表、无序列表
  [{ 'script': 'sub'}, { 'script': 'super' }],      // 下标、上标
  [{ 'indent': '-1'}, { 'indent': '+1' }],          // 缩进
  [{ 'direction': 'rtl' }],                         // 文本方向
  [{ 'size': ['small', false, 'large', 'huge'] }],  // 字体大小
  [{ 'header': [1, 2, 3, 4, 5, 6, false] }],        // 标题级别
  [{ 'color': [] }, { 'background': [] }],          // 字体颜色、背景颜色
  [{ 'font': [] }],                                 // 字体
  [{ 'align': [] }],                                // 对齐方式
  ['clean'],                                        // 清除格式
  ['link', 'image']                                 // 链接、图片
]

// 编辑器准备就绪
const onEditorReady = (quill) => {
  // 添加工具栏按钮的提示文字
  addToolbarTooltips()
  
  // 自定义链接处理
  const toolbar = quill.getModule('toolbar')
  toolbar.addHandler('link', () => {
    const range = quill.getSelection()
    if (range) {
      const currentLink = quill.getFormat(range).link
      const url = prompt('请输入链接地址:', currentLink || 'https://')
      if (url) {
        quill.format('link', url)
      }
    } else {
      ElMessage.warning('请先选择要添加链接的文字')
    }
  })
  
  // 自定义图片处理
  toolbar.addHandler('image', () => {
    selectImage(quill)
  })
}

// 选择图片
const selectImage = (quill) => {
  // 创建一个隐藏的 input 元素
  const input = document.createElement('input')
  input.setAttribute('type', 'file')
  input.setAttribute('accept', 'image/*')
  input.style.display = 'none'
  
  input.onchange = async () => {
    const file = input.files[0]
    if (!file) return
    
    // 验证文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('请选择图片文件')
      return
    }
    
    // 验证文件大小（限制为 5MB）
    const maxSize = 5 * 1024 * 1024
    if (file.size > maxSize) {
      ElMessage.error('图片大小不能超过 5MB')
      return
    }
    
    // 显示加载提示
    const loading = ElMessage({
      message: '正在上传图片...',
      type: 'info',
      duration: 0
    })
    
    try {
      // 上传图片到服务器
      const imageUrl = await uploadNoteImage(file)
      
      // 拼接 /api 前缀（Vite 会代理到后端）
      const fullUrl = '/api' + imageUrl
      
      // 插入图片到编辑器
      const range = quill.getSelection(true)
      quill.insertEmbed(range.index, 'image', fullUrl)
      quill.setSelection(range.index + 1)
      
      loading.close()
      ElMessage.success('图片上传成功')
    } catch (error) {
      loading.close()
      ElMessage.error('图片上传失败: ' + error.message)
    }
  }
  
  // 触发文件选择
  document.body.appendChild(input)
  input.click()
  document.body.removeChild(input)
}

// 读取文件为 Base64（已废弃，改用服务器上传）
const readFileAsBase64 = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = (e) => resolve(e.target.result)
    reader.onerror = (error) => reject(error)
    reader.readAsDataURL(file)
  })
}

// 添加工具栏提示
const addToolbarTooltips = () => {
  setTimeout(() => {
    const tooltips = {
      '.ql-bold': '加粗 (Ctrl+B)',
      '.ql-italic': '斜体 (Ctrl+I)',
      '.ql-underline': '下划线 (Ctrl+U)',
      '.ql-strike': '删除线',
      '.ql-blockquote': '引用',
      '.ql-code-block': '代码块',
      '.ql-header[value="1"]': '一级标题',
      '.ql-header[value="2"]': '二级标题',
      '.ql-list[value="ordered"]': '有序列表（1.2.3.）',
      '.ql-list[value="bullet"]': '无序列表（• • •）',
      '.ql-script[value="sub"]': '下标',
      '.ql-script[value="super"]': '上标',
      '.ql-indent[value="-1"]': '减少缩进（需先创建列表）',
      '.ql-indent[value="+1"]': '增加缩进（需先创建列表）',
      '.ql-direction[value="rtl"]': '文本方向（从右到左）',
      '.ql-size .ql-picker-label': '字体大小',
      '.ql-size .ql-picker-item[data-value="small"]': '小号',
      '.ql-size .ql-picker-item[data-value=""]': '正常',
      '.ql-size .ql-picker-item[data-value="large"]': '大号',
      '.ql-size .ql-picker-item[data-value="huge"]': '超大',
      '.ql-header.ql-picker .ql-picker-label': '标题级别',
      '.ql-header .ql-picker-item[data-value="1"]': '一级标题',
      '.ql-header .ql-picker-item[data-value="2"]': '二级标题',
      '.ql-header .ql-picker-item[data-value="3"]': '三级标题',
      '.ql-header .ql-picker-item[data-value="4"]': '四级标题',
      '.ql-header .ql-picker-item[data-value="5"]': '五级标题',
      '.ql-header .ql-picker-item[data-value="6"]': '六级标题',
      '.ql-header .ql-picker-item[data-value=""]': '正文',
      '.ql-color .ql-picker-label': '字体颜色',
      '.ql-background .ql-picker-label': '背景颜色',
      '.ql-font .ql-picker-label': '字体',
      '.ql-align .ql-picker-label': '对齐方式',
      '.ql-align .ql-picker-item[data-value=""]': '左对齐',
      '.ql-align .ql-picker-item[data-value="center"]': '居中对齐',
      '.ql-align .ql-picker-item[data-value="right"]': '右对齐',
      '.ql-align .ql-picker-item[data-value="justify"]': '两端对齐',
      '.ql-clean': '清除格式',
      '.ql-link': '插入链接',
      '.ql-image': '插入图片（支持本地上传）'
    }

    Object.entries(tooltips).forEach(([selector, title]) => {
      const elements = document.querySelectorAll(selector)
      elements.forEach(el => {
        el.setAttribute('title', title)
      })
    })
  }, 100)
}

// 组件挂载
onMounted(async () => {
  // 加载分类列表
  await categoryStore.fetchCategoryList()
  
  // 加载标签列表
  await loadTags()

  // 如果是编辑模式，加载笔记详情
  if (isEditMode) {
    await loadNoteDetail()
  }
})

// 加载标签列表
const loadTags = async () => {
  try {
    availableTags.value = await getTagList()
  } catch (error) {
    console.error('加载标签失败:', error)
  }
}

// 标签变化处理
const handleTagsChange = (tags) => {
  noteForm.tags = tags.join(',')
}

// 移除标签
const removeTag = (tag) => {
  selectedTags.value = selectedTags.value.filter(t => t !== tag)
  noteForm.tags = selectedTags.value.join(',')
}

// 加载笔记详情
const loadNoteDetail = async () => {
  try {
    const note = await noteStore.fetchNoteDetail(noteId)
    noteForm.title = note.title
    // 将相对路径的图片 URL 转换为完整 URL，用于编辑器显示
    noteForm.content = processNoteContent(note.content || '')
    noteForm.categoryId = note.categoryId
    noteForm.tags = note.tags || ''
    
    // 解析标签
    if (note.tags) {
      selectedTags.value = note.tags.split(',').map(t => t.trim()).filter(t => t)
    }
  } catch (error) {
    ElMessage.error('加载笔记失败')
    goBack()
  }
}

// 将内容中的 /api 前缀转换回相对路径（用于保存）
const normalizeContent = (content) => {
  if (!content) return ''
  
  // 将 /api/uploads/... 转换回 /uploads/...
  return content.replace(
    /src=["'](\/api)(\/uploads\/[^"']+)["']/gi,
    'src="$2"'
  )
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

    // 准备保存的数据（将完整 URL 转换回相对路径）
    const saveData = {
      ...noteForm,
      content: normalizeContent(noteForm.content)
    }

    if (isEditMode) {
      // 更新笔记
      await noteStore.updateNote(noteId, saveData)
    } else {
      // 创建笔记
      await noteStore.createNote(saveData)
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

.selected-tags {
  margin-top: 8px;
}

/* Quill 编辑器样式调整 */
.editor-form-item {
  margin-bottom: 0;
}

.editor-form-item :deep(.el-form-item__content) {
  display: block !important;
  align-items: flex-start !important;
}

.note-editor {
  width: 100%;
}

/* 强制覆盖 Quill 默认样式 */
:deep(.ql-toolbar.ql-snow) {
  border: 1px solid #dcdfe6 !important;
  border-radius: 4px 4px 0 0 !important;
  background: #fafafa !important;
  padding: 8px !important;
  box-sizing: border-box !important;
}

/* 工具栏按钮提示样式 */
:deep(.ql-toolbar button),
:deep(.ql-toolbar .ql-picker-label) {
  cursor: pointer;
  position: relative;
}

:deep(.ql-toolbar button:hover),
:deep(.ql-toolbar .ql-picker-label:hover) {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 3px;
}

:deep(.ql-container.ql-snow) {
  border: 1px solid #dcdfe6 !important;
  border-top: none !important;
  border-radius: 0 0 4px 4px !important;
  font-size: 14px !important;
  min-height: 400px !important;
}

:deep(.ql-editor) {
  min-height: 400px !important;
  line-height: 1.8 !important;
  padding: 15px !important;
  text-align: left !important;
  direction: ltr !important;
}

:deep(.ql-editor.ql-blank::before) {
  color: #c0c4cc !important;
  font-style: normal !important;
  left: 15px !important;
  right: 15px !important;
}

:deep(.ql-editor p) {
  margin: 0 !important;
  padding: 0 !important;
  text-align: left !important;
  direction: ltr !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .page-header {
    padding: 12px 16px;
  }

  .edit-container {
    padding: 16px;
  }

  .header-actions {
    gap: 8px;
  }

  .note-editor {
    height: 400px;
  }

  :deep(.ql-editor) {
    min-height: 300px;
  }
}

@media (max-width: 480px) {
  .page-header {
    flex-direction: column;
    align-items: stretch;
  }

  .header-actions {
    width: 100%;
    justify-content: space-between;
  }

  .edit-container {
    padding: 12px;
  }

  .note-editor {
    height: 350px;
  }

  :deep(.ql-editor) {
    min-height: 250px;
    padding: 12px;
  }

  :deep(.ql-toolbar) {
    padding: 8px;
  }
}
</style>
