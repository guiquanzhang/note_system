<template>
  <div class="ai-assistant-page">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h2>🤖 AI 学习助手</h2>
        <el-tag type="success" size="small">免费</el-tag>
      </div>
      <div class="header-right">
        <el-button :icon="Setting" @click="showSettings = true">设置</el-button>
        <el-button :icon="Delete" @click="clearMessages">清空对话</el-button>
      </div>
    </div>

    <!-- 主体内容 -->
    <div class="page-content">
      <!-- 消息列表 -->
      <div ref="messageContainer" class="message-list">
        <!-- 欢迎界面 -->
        <div v-if="messages.length === 0" class="welcome-section">
          <div class="welcome-icon">
            <el-icon :size="80" color="#409EFF"><ChatLineRound /></el-icon>
          </div>
          <h2>👋 你好！我是 AI 学习助手</h2>
          <p class="welcome-desc">我可以帮你解答编程和技术问题，让学习更高效！</p>
          
          <div class="quick-questions">
            <h3>💡 试试这些问题：</h3>
            <div class="question-grid">
              <el-card
                v-for="(q, index) in quickQuestions"
                :key="index"
                class="question-card"
                shadow="hover"
                @click="sendQuickQuestion(q.question)"
              >
                <div class="question-icon">{{ q.icon }}</div>
                <div class="question-text">{{ q.question }}</div>
              </el-card>
            </div>
          </div>

          <el-alert
            v-if="!settings.apiKey"
            type="warning"
            :closable="false"
            style="margin-top: 30px;"
          >
            <template #title>
              <strong>⚠️ 请先配置 API Key</strong>
            </template>
            点击右上角"设置"按钮，配置免费的 Groq API Key 即可使用。
            <el-link type="primary" @click="showApiHelp = true">如何获取？</el-link>
          </el-alert>
        </div>

        <!-- 对话消息 -->
        <div
          v-for="(msg, index) in messages"
          :key="index"
          :class="['message-item', msg.role]"
        >
          <div class="message-avatar">
            <el-avatar v-if="msg.role === 'user'" :size="40" :icon="UserFilled" />
            <el-icon v-else :size="40" color="#409EFF"><ChatLineRound /></el-icon>
          </div>
          <div class="message-bubble">
            <div class="message-header">
              <span class="message-sender">{{ msg.role === 'user' ? '你' : 'AI 助手' }}</span>
              <span class="message-time">{{ msg.time }}</span>
            </div>
            <div class="message-content" v-html="formatMessage(msg.content)"></div>
          </div>
        </div>

        <!-- 加载中 -->
        <div v-if="loading" class="message-item assistant">
          <div class="message-avatar">
            <el-icon :size="40" color="#409EFF"><ChatLineRound /></el-icon>
          </div>
          <div class="message-bubble">
            <div class="message-header">
              <span class="message-sender">AI 助手</span>
            </div>
            <div class="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 输入区域 -->
      <div class="input-area">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="输入你的问题... (Enter 发送 / Shift+Enter 换行)"
          @keydown.enter.exact.prevent="sendMessage"
          @keydown.shift.enter="inputMessage += '\n'"
        />
        <div class="input-actions">
          <div class="input-tip">
            <el-icon><InfoFilled /></el-icon>
            <span>Enter 发送 / Shift+Enter 换行</span>
          </div>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            :disabled="!inputMessage.trim() || !settings.apiKey"
            @click="sendMessage"
          >
            <el-icon><Promotion /></el-icon>
            发送
          </el-button>
        </div>
      </div>
    </div>

    <!-- 设置对话框 -->
    <el-dialog
      v-model="showSettings"
      title="AI 助手设置"
      width="600px"
    >
      <el-form label-width="120px">
        <el-form-item label="API 提供商">
          <el-select v-model="settings.provider" style="width: 100%">
            <el-option value="groq" label="Groq (推荐)" />
            <el-option value="cohere" label="Cohere" />
            <el-option value="huggingface" label="HuggingFace" />
          </el-select>
        </el-form-item>

        <el-form-item label="API Key">
          <el-input
            v-model="settings.apiKey"
            type="password"
            placeholder="输入你的 API Key"
            show-password
          />
          <div class="form-tip">
            <el-link type="primary" @click="showApiHelp = true">如何获取免费 API Key？</el-link>
          </div>
        </el-form-item>

        <el-form-item label="模型">
          <el-select v-model="settings.model" style="width: 100%">
            <el-option
              v-for="model in availableModels"
              :key="model.value"
              :value="model.value"
              :label="model.label"
            />
          </el-select>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="showSettings = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存</el-button>
      </template>
    </el-dialog>

    <!-- API Key 帮助对话框 -->
    <el-dialog
      v-model="showApiHelp"
      title="如何获取免费 API Key"
      width="700px"
    >
      <div class="api-help-content">
        <el-steps :active="1" align-center>
          <el-step title="选择提供商" />
          <el-step title="注册账号" />
          <el-step title="创建 API Key" />
          <el-step title="配置使用" />
        </el-steps>

        <el-tabs style="margin-top: 30px;">
          <el-tab-pane label="🚀 Groq (推荐)">
            <div class="help-section">
              <h3>为什么推荐 Groq？</h3>
              <ul>
                <li>✅ 完全免费，无需信用卡</li>
                <li>✅ 速度超快（1-3秒响应）</li>
                <li>✅ 每天 14,400 次请求</li>
                <li>✅ 支持中文，质量优秀</li>
              </ul>

              <h3>获取步骤：</h3>
              <ol>
                <li>访问 <el-link href="https://console.groq.com" target="_blank" type="primary">https://console.groq.com</el-link></li>
                <li>使用 Google 或 GitHub 账号登录</li>
                <li>点击左侧菜单的 "API Keys"</li>
                <li>点击 "Create API Key" 按钮</li>
                <li>输入名称（如 "CloudNote"）</li>
                <li>点击 "Submit" 创建</li>
                <li>复制生成的 API Key（只显示一次）</li>
                <li>粘贴到上面的设置中</li>
              </ol>

              <el-alert type="success" :closable="false">
                <strong>完全免费！</strong>每天 14,400 次请求，对于学习使用完全足够！
              </el-alert>
            </div>
          </el-tab-pane>

          <el-tab-pane label="🌟 Cohere">
            <div class="help-section">
              <h3>Cohere API</h3>
              <ol>
                <li>访问 <el-link href="https://dashboard.cohere.com" target="_blank" type="primary">https://dashboard.cohere.com</el-link></li>
                <li>注册免费账号</li>
                <li>在 Dashboard 中找到 API Key</li>
                <li>复制并使用</li>
              </ol>
              <p>免费额度：每月 1,000 次调用</p>
            </div>
          </el-tab-pane>

          <el-tab-pane label="🤗 HuggingFace">
            <div class="help-section">
              <h3>HuggingFace API</h3>
              <ol>
                <li>访问 <el-link href="https://huggingface.co" target="_blank" type="primary">https://huggingface.co</el-link></li>
                <li>注册账号</li>
                <li>进入 Settings → Access Tokens</li>
                <li>创建新的 Token（选择 Read 权限）</li>
                <li>复制使用</li>
              </ol>
              <p>免费额度：无限制（速度较慢）</p>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ChatLineRound,
  Setting,
  Delete,
  UserFilled,
  InfoFilled,
  Promotion
} from '@element-plus/icons-vue'
import { marked } from 'marked'

// 状态
const loading = ref(false)
const inputMessage = ref('')
const messages = ref([])
const messageContainer = ref(null)
const showSettings = ref(false)
const showApiHelp = ref(false)

// 设置
const settings = reactive({
  provider: localStorage.getItem('ai_provider') || 'groq',
  apiKey: localStorage.getItem('ai_api_key') || '',
  model: localStorage.getItem('ai_model') || 'llama-3.3-70b-versatile'
})

// 快速问题
const quickQuestions = [
  { icon: '🎯', question: '什么是 Vue 3 的 Composition API？' },
  { icon: '🔒', question: '解释一下 JavaScript 闭包' },
  { icon: '🚀', question: 'Spring Boot 如何连接 MySQL 数据库？' },
  { icon: '⚡', question: 'MySQL 索引优化有哪些技巧？' },
  { icon: '🎨', question: 'CSS Flexbox 和 Grid 的区别？' },
  { icon: '🔧', question: 'Git 常用命令有哪些？' }
]

// 可用模型
const availableModels = computed(() => {
  if (settings.provider === 'groq') {
    return [
      { value: 'llama-3.3-70b-versatile', label: 'Llama 3.3 70B (推荐)' },
      { value: 'llama-3.1-8b-instant', label: 'Llama 3.1 8B (快速)' },
      { value: 'mixtral-8x7b-32768', label: 'Mixtral 8x7B' }
    ]
  } else if (settings.provider === 'cohere') {
    return [
      { value: 'command', label: 'Command' },
      { value: 'command-light', label: 'Command Light' }
    ]
  } else {
    return [
      { value: 'mistralai/Mistral-7B-Instruct-v0.2', label: 'Mistral 7B' }
    ]
  }
})

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim()) return
  
  if (!settings.apiKey) {
    ElMessage.warning('请先在设置中配置 API Key')
    showSettings.value = true
    return
  }

  const userMessage = inputMessage.value.trim()
  inputMessage.value = ''

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: userMessage,
    time: getCurrentTime()
  })

  scrollToBottom()
  loading.value = true

  try {
    const response = await callAI(userMessage)
    
    // 添加 AI 回复
    messages.value.push({
      role: 'assistant',
      content: response,
      time: getCurrentTime()
    })
  } catch (error) {
    console.error('AI 调用失败:', error)
    ElMessage.error('AI 调用失败: ' + error.message)
    
    // 添加错误消息
    messages.value.push({
      role: 'assistant',
      content: '抱歉，我遇到了一些问题。请检查你的 API Key 是否正确，或稍后再试。',
      time: getCurrentTime()
    })
  } finally {
    loading.value = false
    scrollToBottom()
  }
}

// 发送快速问题
const sendQuickQuestion = (question) => {
  inputMessage.value = question
  sendMessage()
}

// 清空对话
const clearMessages = () => {
  ElMessageBox.confirm('确定要清空所有对话记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    messages.value = []
    ElMessage.success('对话已清空')
  }).catch(() => {})
}

// 调用 AI API
const callAI = async (message) => {
  if (settings.provider === 'groq') {
    return await callGroqAPI(message)
  } else if (settings.provider === 'cohere') {
    return await callCohereAPI(message)
  } else {
    return await callHuggingFaceAPI(message)
  }
}

// Groq API 调用
const callGroqAPI = async (message) => {
  const response = await fetch('https://api.groq.com/openai/v1/chat/completions', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${settings.apiKey}`
    },
    body: JSON.stringify({
      model: settings.model,
      messages: [
        {
          role: 'system',
          content: '你是一个友好的学习助手，专门帮助用户理解编程和技术知识。请用简洁、清晰的中文回答问题，并在适当时提供代码示例。'
        },
        ...messages.value.slice(-5).map(m => ({
          role: m.role,
          content: m.content
        })),
        {
          role: 'user',
          content: message
        }
      ],
      temperature: 0.7,
      max_tokens: 2048
    })
  })

  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.error?.message || '请求失败')
  }

  const data = await response.json()
  return data.choices[0].message.content
}

// Cohere API 调用
const callCohereAPI = async (message) => {
  const response = await fetch('https://api.cohere.ai/v1/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${settings.apiKey}`
    },
    body: JSON.stringify({
      model: settings.model,
      message: message,
      chat_history: messages.value.slice(-5).map(m => ({
        role: m.role === 'assistant' ? 'CHATBOT' : 'USER',
        message: m.content
      }))
    })
  })

  if (!response.ok) {
    throw new Error('Cohere API 调用失败')
  }

  const data = await response.json()
  return data.text
}

// HuggingFace API 调用
const callHuggingFaceAPI = async (message) => {
  const response = await fetch(
    `https://api-inference.huggingface.co/models/${settings.model}`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${settings.apiKey}`
      },
      body: JSON.stringify({
        inputs: message,
        parameters: {
          max_new_tokens: 512,
          temperature: 0.7
        }
      })
    }
  )

  if (!response.ok) {
    throw new Error('HuggingFace API 调用失败')
  }

  const data = await response.json()
  return data[0].generated_text
}

// 格式化消息（支持 Markdown）
const formatMessage = (content) => {
  try {
    return marked.parse(content)
  } catch (error) {
    return content.replace(/\n/g, '<br>')
  }
}

// 获取当前时间
const getCurrentTime = () => {
  const now = new Date()
  return `${now.getHours().toString().padStart(2, '0')}:${now.getMinutes().toString().padStart(2, '0')}`
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messageContainer.value) {
      messageContainer.value.scrollTop = messageContainer.value.scrollHeight
    }
  })
}

// 保存设置
const saveSettings = () => {
  localStorage.setItem('ai_provider', settings.provider)
  localStorage.setItem('ai_api_key', settings.apiKey)
  localStorage.setItem('ai_model', settings.model)
  showSettings.value = false
  ElMessage.success('设置已保存')
}

// 组件挂载
onMounted(() => {
  // 如果没有 API Key，显示帮助
  if (!settings.apiKey) {
    setTimeout(() => {
      ElMessage.info({
        message: '欢迎使用 AI 助手！请先配置 API Key',
        duration: 3000
      })
    }, 500)
  }
})
</script>

<style scoped>
.ai-assistant-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
}

/* 页面头部 */
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.header-left h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.header-right {
  display: flex;
  gap: 12px;
}

/* 主体内容 */
.page-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* 消息列表 */
.message-list {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

/* 欢迎界面 */
.welcome-section {
  max-width: 900px;
  margin: 0 auto;
  text-align: center;
  padding: 40px 20px;
}

.welcome-icon {
  margin-bottom: 24px;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px);
  }
}

.welcome-section h2 {
  font-size: 32px;
  color: #303133;
  margin: 0 0 16px 0;
}

.welcome-desc {
  font-size: 16px;
  color: #606266;
  margin-bottom: 40px;
}

.quick-questions {
  margin-top: 40px;
}

.quick-questions h3 {
  font-size: 20px;
  color: #303133;
  margin-bottom: 24px;
}

.question-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.question-card {
  cursor: pointer;
  transition: all 0.3s;
  text-align: left;
}

.question-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.question-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.question-text {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

/* 消息项 */
.message-item {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  animation: fadeIn 0.3s;
}

.message-item.user {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
}

.message-bubble {
  max-width: 70%;
  background: white;
  padding: 16px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.message-item.user .message-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}

.message-sender {
  font-weight: bold;
  font-size: 14px;
}

.message-time {
  font-size: 12px;
  opacity: 0.7;
}

.message-content {
  line-height: 1.8;
  word-wrap: break-word;
}

.message-content :deep(code) {
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}

.message-item.user .message-content :deep(code) {
  background: rgba(255, 255, 255, 0.2);
}

.message-content :deep(pre) {
  background: rgba(0, 0, 0, 0.05);
  padding: 12px;
  border-radius: 8px;
  overflow-x: auto;
  margin: 12px 0;
}

.message-item.user .message-content :deep(pre) {
  background: rgba(255, 255, 255, 0.2);
}

/* 输入中指示器 */
.typing-indicator {
  display: flex;
  gap: 6px;
  padding: 12px 0;
}

.typing-indicator span {
  width: 10px;
  height: 10px;
  background: #409EFF;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
  }
  30% {
    transform: translateY(-10px);
  }
}

/* 输入区域 */
.input-area {
  padding: 20px 24px;
  background: white;
  border-top: 1px solid #e4e7ed;
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.input-tip {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #909399;
}

/* 动画 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 帮助内容 */
.api-help-content {
  line-height: 1.8;
}

.help-section h3 {
  color: #303133;
  margin: 20px 0 12px;
}

.help-section ul,
.help-section ol {
  padding-left: 24px;
}

.help-section li {
  margin: 8px 0;
  color: #606266;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}

/* 响应式 */
@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    align-items: stretch;
    gap: 12px;
  }

  .header-right {
    justify-content: space-between;
  }

  .question-grid {
    grid-template-columns: 1fr;
  }

  .message-bubble {
    max-width: 85%;
  }
}
</style>
