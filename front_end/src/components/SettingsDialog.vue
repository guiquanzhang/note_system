<template>
  <el-dialog
    v-model="visible"
    title="设置"
    width="600px"
    :fullscreen="isMobile"
    :before-close="handleClose"
    class="settings-dialog"
  >
    <el-form label-width="120px">
      <!-- 主题设置 -->
      <el-form-item label="主题模式">
        <el-radio-group v-model="settings.theme" @change="changeTheme">
          <el-radio-button value="light">
            <el-icon><Sunny /></el-icon>
            亮色
          </el-radio-button>
          <el-radio-button value="dark">
            <el-icon><Moon /></el-icon>
            暗色
          </el-radio-button>
          <el-radio-button value="blue">
            <el-icon><View /></el-icon>
            护眼蓝
          </el-radio-button>
          <el-radio-button value="auto">
            <el-icon><Monitor /></el-icon>
            跟随系统
          </el-radio-button>
        </el-radio-group>
      </el-form-item>

      <!-- 列表布局 -->
      <el-form-item label="笔记列表布局">
        <el-radio-group v-model="settings.listLayout" @change="changeListLayout">
          <el-radio-button value="grid">
            <el-icon><Grid /></el-icon>
            网格
          </el-radio-button>
          <el-radio-button value="list">
            <el-icon><List /></el-icon>
            列表
          </el-radio-button>
        </el-radio-group>
      </el-form-item>

      <!-- 每页显示数量 -->
      <el-form-item label="每页显示">
        <el-select v-model="settings.pageSize" @change="changePageSize" style="width: 150px">
          <el-option :value="10" label="10 条" />
          <el-option :value="20" label="20 条" />
          <el-option :value="50" label="50 条" />
          <el-option :value="100" label="100 条" />
        </el-select>
      </el-form-item>

      <!-- 自动保存 -->
      <el-form-item label="自动保存">
        <el-switch
          v-model="settings.autoSave"
          @change="changeAutoSave"
        />
        <div class="form-tip">编辑笔记时自动保存草稿</div>
      </el-form-item>

      <!-- 全局字体大小 -->
      <el-form-item label="全局字体大小">
        <el-select v-model="settings.globalFontSize" @change="changeGlobalFontSize" style="width: 150px">
          <el-option value="small" label="小号" />
          <el-option value="medium" label="标准" />
          <el-option value="large" label="大号" />
          <el-option value="xlarge" label="超大号" />
        </el-select>
        <div class="form-tip">调整整个应用的字体大小</div>
      </el-form-item>

      <!-- 编辑器字体大小 -->
      <el-form-item label="编辑器字体">
        <el-select v-model="settings.editorFontSize" @change="changeEditorFontSize" style="width: 150px">
          <el-option :value="12" label="小 (12px)" />
          <el-option :value="14" label="中 (14px)" />
          <el-option :value="16" label="大 (16px)" />
          <el-option :value="18" label="超大 (18px)" />
        </el-select>
        <div class="form-tip">仅调整笔记编辑器的字体大小</div>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="resetSettings">恢复默认</el-button>
      <el-button type="primary" @click="handleClose">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Sunny, Moon, Monitor, Grid, List, View } from '@element-plus/icons-vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const visible = ref(props.modelValue)

// 检测是否为移动端
const isMobile = computed(() => {
  return window.innerWidth <= 768
})

watch(() => props.modelValue, (val) => {
  visible.value = val
})

watch(visible, (val) => {
  emit('update:modelValue', val)
})

// 设置
const settings = reactive({
  theme: localStorage.getItem('theme') || 'light',
  listLayout: localStorage.getItem('listLayout') || 'grid',
  pageSize: parseInt(localStorage.getItem('pageSize') || '20'),
  autoSave: localStorage.getItem('autoSave') === 'true',
  globalFontSize: localStorage.getItem('globalFontSize') || 'medium',
  editorFontSize: parseInt(localStorage.getItem('editorFontSize') || '14')
})

// 切换主题
const changeTheme = (theme) => {
  localStorage.setItem('theme', theme)
  applyTheme(theme)
  ElMessage.success('主题已切换')
}

// 应用主题
const applyTheme = (theme) => {
  let actualTheme = theme
  if (theme === 'auto') {
    const isDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    actualTheme = isDark ? 'dark' : 'light'
  }
  
  const html = document.documentElement
  // 移除所有主题类
  html.classList.remove('dark', 'blue')
  
  if (actualTheme === 'dark') {
    html.classList.add('dark')
    html.style.colorScheme = 'dark'
  } else if (actualTheme === 'blue') {
    html.classList.add('blue')
    html.style.colorScheme = 'dark'
  } else {
    html.style.colorScheme = 'light'
  }
}

// 切换列表布局
const changeListLayout = (layout) => {
  localStorage.setItem('listLayout', layout)
  ElMessage.success('布局已切换，刷新页面后生效')
}

// 切换每页显示数量
const changePageSize = (size) => {
  localStorage.setItem('pageSize', size.toString())
  ElMessage.success('设置已保存，刷新页面后生效')
}

// 切换自动保存
const changeAutoSave = (enabled) => {
  localStorage.setItem('autoSave', enabled.toString())
  ElMessage.success(enabled ? '已开启自动保存' : '已关闭自动保存')
}

// 切换全局字体大小
const changeGlobalFontSize = (size) => {
  console.log('设置全局字体大小:', size)
  localStorage.setItem('globalFontSize', size)
  applyGlobalFontSize(size)
  ElMessage.success(`全局字体大小已设置为${getFontSizeLabel(size)}`)
}

// 应用全局字体大小
const applyGlobalFontSize = (size) => {
  const html = document.documentElement
  // 移除所有字体大小类
  html.classList.remove('font-small', 'font-medium', 'font-large', 'font-xlarge')
  // 添加新的字体大小类
  html.classList.add(`font-${size}`)
  console.log('全局字体大小类已应用:', `font-${size}`)
}

// 获取字体大小标签
const getFontSizeLabel = (size) => {
  const labels = {
    small: '小号',
    medium: '标准',
    large: '大号',
    xlarge: '超大号'
  }
  return labels[size] || '标准'
}

// 切换编辑器字体大小
const changeEditorFontSize = (size) => {
  console.log('设置字体大小:', size)
  localStorage.setItem('editorFontSize', size.toString())
  // 立即应用字体大小
  document.documentElement.style.setProperty('--editor-font-size', `${size}px`)
  console.log('CSS变量已设置:', document.documentElement.style.getPropertyValue('--editor-font-size'))
  ElMessage.success(`字体大小已设置为 ${size}px`)
}

// 恢复默认设置
const resetSettings = () => {
  settings.theme = 'light'
  settings.listLayout = 'grid'
  settings.pageSize = 20
  settings.autoSave = false
  settings.globalFontSize = 'medium'
  settings.editorFontSize = 14
  
  localStorage.setItem('theme', 'light')
  localStorage.setItem('listLayout', 'grid')
  localStorage.setItem('pageSize', '20')
  localStorage.setItem('autoSave', 'false')
  localStorage.setItem('globalFontSize', 'medium')
  localStorage.setItem('editorFontSize', '14')
  
  applyTheme('light')
  applyGlobalFontSize('medium')
  // 立即应用默认字体大小
  document.documentElement.style.setProperty('--editor-font-size', '14px')
  ElMessage.success('已恢复默认设置')
}

// 关闭对话框
const handleClose = () => {
  visible.value = false
}
</script>

<style scoped>
.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
  margin-left: 12px;
}

:deep(.el-radio-button__inner) {
  display: flex;
  align-items: center;
  gap: 4px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  :deep(.el-dialog) {
    width: 95% !important;
    margin: 5vh auto !important;
  }

  :deep(.el-form-item__label) {
    font-size: 14px;
  }

  :deep(.el-radio-button__inner) {
    padding: 8px 12px;
    font-size: 13px;
  }

  :deep(.el-select) {
    width: 100% !important;
  }

  .form-tip {
    font-size: 11px;
  }
}

@media (max-width: 480px) {
  :deep(.el-form) {
    padding: 0 8px;
  }

  :deep(.el-form-item) {
    margin-bottom: 16px;
  }

  :deep(.el-form-item__label) {
    width: 100px !important;
    font-size: 13px;
  }

  :deep(.el-radio-group) {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }

  :deep(.el-radio-button) {
    flex: 1 1 45%;
  }

  :deep(.el-radio-button__inner) {
    padding: 6px 8px;
    font-size: 12px;
    width: 100%;
  }
}
</style>
