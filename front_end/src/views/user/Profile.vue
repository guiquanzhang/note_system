<template>
  <div class="profile-page">
    <div class="page-header">
      <h2>个人信息</h2>
    </div>

    <el-card class="profile-card">
      <el-tabs v-model="activeTab">
        <!-- 基本信息 -->
        <el-tab-pane label="基本信息" name="basic">
          <el-form
            ref="basicFormRef"
            :model="basicForm"
            :rules="basicRules"
            label-width="100px"
            class="profile-form"
          >
            <!-- 头像 -->
            <el-form-item label="头像">
              <div class="avatar-upload">
                <el-avatar :size="80" :src="getAvatarUrl(basicForm.avatar)" class="avatar-preview">
                  <el-icon :size="40"><User /></el-icon>
                </el-avatar>
                <div class="avatar-actions" v-if="editingBasic">
                  <el-button size="small" @click="selectAvatar">选择头像</el-button>
                  <el-button size="small" @click="removeAvatar" v-if="basicForm.avatar">移除</el-button>
                </div>
              </div>
            </el-form-item>

            <el-form-item label="用户ID">
              <el-input v-model="userInfo.userId" disabled />
            </el-form-item>

            <el-form-item label="用户名" prop="username">
              <el-input
                v-model="basicForm.username"
                placeholder="请输入用户名"
                :disabled="!editingBasic"
              />
            </el-form-item>

            <el-form-item label="昵称" prop="nickname">
              <el-input
                v-model="basicForm.nickname"
                placeholder="请输入昵称（可选）"
                :disabled="!editingBasic"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>

            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="basicForm.email"
                placeholder="请输入邮箱"
                :disabled="!editingBasic"
              />
            </el-form-item>

            <el-form-item label="注册时间">
              <el-input :value="formatTime(userInfo.createdAt)" disabled />
            </el-form-item>

            <el-form-item label="更新时间">
              <el-input :value="formatTime(userInfo.updatedAt)" disabled />
            </el-form-item>

            <el-form-item>
              <el-button
                v-if="!editingBasic"
                type="primary"
                @click="startEditBasic"
              >
                编辑信息
              </el-button>
              <template v-else>
                <el-button type="primary" :loading="saving" @click="saveBasicInfo">
                  保存
                </el-button>
                <el-button @click="cancelEditBasic">取消</el-button>
              </template>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 修改密码 -->
        <el-tab-pane label="修改密码" name="password">
          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="100px"
            class="profile-form"
          >
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input
                v-model="passwordForm.oldPassword"
                type="password"
                placeholder="请输入旧密码"
                show-password
              />
            </el-form-item>

            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>

            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" :loading="saving" @click="savePassword">
                修改密码
              </el-button>
              <el-button @click="resetPasswordForm">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <!-- 账户安全 -->
        <el-tab-pane label="账户安全" name="security">
          <div class="security-section">
            <el-alert
              title="账户安全提示"
              type="info"
              :closable="false"
              show-icon
            >
              <ul class="security-tips">
                <li>定期修改密码，建议每 3 个月更换一次</li>
                <li>密码长度至少 6 位，建议包含字母、数字和特殊字符</li>
                <li>不要在多个网站使用相同的密码</li>
                <li>绑定邮箱可以帮助您找回密码</li>
              </ul>
            </el-alert>

            <div class="security-items">
              <div class="security-item">
                <div class="item-left">
                  <el-icon :size="24" color="#67C23A"><Lock /></el-icon>
                  <div class="item-info">
                    <div class="item-title">登录密码</div>
                    <div class="item-desc">定期修改密码可以提高账户安全性</div>
                  </div>
                </div>
                <el-button type="primary" link @click="activeTab = 'password'">
                  修改密码
                </el-button>
              </div>

              <div class="security-item">
                <div class="item-left">
                  <el-icon :size="24" color="#409EFF"><Message /></el-icon>
                  <div class="item-info">
                    <div class="item-title">绑定邮箱</div>
                    <div class="item-desc">
                      {{ userInfo.email || '未绑定邮箱' }}
                    </div>
                  </div>
                </div>
                <el-button type="primary" link @click="activeTab = 'basic'">
                  {{ userInfo.email ? '修改邮箱' : '绑定邮箱' }}
                </el-button>
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getUserInfo, updateUserInfo, updatePassword } from '@/api/user'
import { uploadAvatar } from '@/api/file'
import { ElMessage } from 'element-plus'
import { Lock, Message, User } from '@element-plus/icons-vue'
import dayjs from 'dayjs'

// 当前激活的标签页
const activeTab = ref('basic')

// 用户信息
const userInfo = reactive({
  userId: null,
  username: '',
  email: '',
  createdAt: null,
  updatedAt: null
})

// 基本信息表单
const basicForm = reactive({
  username: '',
  nickname: '',
  email: '',
  avatar: ''
})

// 是否正在编辑基本信息
const editingBasic = ref(false)

// 保存状态
const saving = ref(false)

// 基本信息表单引用
const basicFormRef = ref(null)

// 基本信息验证规则
const basicRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  nickname: [
    { max: 20, message: '昵称长度不能超过 20 个字符', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}



// 密码表单
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 密码表单引用
const passwordFormRef = ref(null)

// 密码验证规则
const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('请再次输入新密码'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 组件挂载时加载用户信息
onMounted(() => {
  loadUserInfo()
})

// 加载用户信息
const loadUserInfo = async () => {
  try {
    const data = await getUserInfo()
    Object.assign(userInfo, data)
    basicForm.username = data.username
    basicForm.nickname = data.nickname || ''
    basicForm.email = data.email || ''
    basicForm.avatar = data.avatar || ''
  } catch (error) {
    ElMessage.error('加载用户信息失败')
  }
}

// 选择头像
const selectAvatar = () => {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = 'image/*'
  
  input.onchange = async (e) => {
    const file = e.target.files[0]
    if (!file) return
    
    // 验证文件类型
    if (!file.type.startsWith('image/')) {
      ElMessage.error('请选择图片文件')
      return
    }
    
    // 验证文件大小（限制为 5MB）
    if (file.size > 5 * 1024 * 1024) {
      ElMessage.error('头像大小不能超过 5MB')
      return
    }
    
    try {
      // 显示上传提示
      const loading = ElMessage({
        message: '正在上传图片...',
        type: 'info',
        duration: 0
      })
      
      // 上传图片到服务器
      const imageUrl = await uploadAvatar(file)
      
      // 保存图片URL
      basicForm.avatar = imageUrl
      
      // 确保表单其他字段有值（如果用户没有点击编辑按钮）
      if (!basicForm.username) {
        basicForm.username = userInfo.username
        basicForm.nickname = userInfo.nickname || ''
        basicForm.email = userInfo.email || ''
      }
      
      loading.close()
      ElMessage.success('头像已上传，请点击保存按钮')
    } catch (error) {
      ElMessage.error('上传图片失败: ' + (error.message || '未知错误'))
    }
  }
  
  input.click()
}

// 移除头像
const removeAvatar = () => {
  basicForm.avatar = ''
  ElMessage.success('头像已移除，请点击保存按钮')
}

// 获取头像完整URL
const getAvatarUrl = (avatar) => {
  if (!avatar) return ''
  // 如果是完整URL，直接返回
  if (avatar.startsWith('http://') || avatar.startsWith('https://')) {
    return avatar
  }
  // 如果是相对路径，拼接后端地址（注意：后端有 /api 前缀）
  return `http://localhost:8080/api${avatar}`
}

// 开始编辑基本信息
const startEditBasic = () => {
  editingBasic.value = true
}

// 取消编辑基本信息
const cancelEditBasic = () => {
  editingBasic.value = false
  basicForm.username = userInfo.username
  basicForm.nickname = userInfo.nickname || ''
  basicForm.email = userInfo.email || ''
  basicForm.avatar = userInfo.avatar || ''
  basicFormRef.value?.clearValidate()
}

// 保存基本信息
const saveBasicInfo = async () => {
  if (!basicFormRef.value) return

  try {
    await basicFormRef.value.validate()
    saving.value = true

    await updateUserInfo(basicForm)
    
    ElMessage.success('信息更新成功')
    editingBasic.value = false
    await loadUserInfo()
  } catch (error) {
    if (error !== false) {
      console.error('更新失败:', error)
    }
  } finally {
    saving.value = false
  }
}

// 保存密码
const savePassword = async () => {
  if (!passwordFormRef.value) return

  try {
    await passwordFormRef.value.validate()
    saving.value = true

    await updatePassword(passwordForm)
    
    ElMessage.success('密码修改成功，请重新登录')
    resetPasswordForm()
    
    // 3 秒后跳转到登录页
    setTimeout(() => {
      // 清除登录状态
      localStorage.clear()
      window.location.href = '/login'
    }, 3000)
  } catch (error) {
    if (error !== false) {
      console.error('修改密码失败:', error)
    }
  } finally {
    saving.value = false
  }
}

// 重置密码表单
const resetPasswordForm = () => {
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordFormRef.value?.clearValidate()
}

// 格式化时间
const formatTime = (time) => {
  return time ? dayjs(time).format('YYYY-MM-DD HH:mm:ss') : '-'
}


</script>

<style scoped>
.profile-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.page-header {
  margin-bottom: 24px;
}

.page-header h2 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.profile-card {
  border-radius: 8px;
}

.profile-form {
  max-width: 500px;
  margin-top: 24px;
}

/* 账户安全 */
.security-section {
  padding: 24px 0;
}

.security-tips {
  margin: 0;
  padding-left: 20px;
  line-height: 1.8;
}

.security-items {
  margin-top: 32px;
}

.security-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 16px;
  transition: all 0.3s;
}

.security-item:hover {
  border-color: #409EFF;
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
}

.item-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.item-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.item-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.item-desc {
  font-size: 14px;
  color: #909399;
}

/* 头像上传 */
.avatar-upload {
  display: flex;
  align-items: center;
  gap: 20px;
}

.avatar-preview {
  background: #f5f7fa;
  border: 2px dashed #dcdfe6;
}

.avatar-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-page {
    padding: 16px;
  }

  .profile-form {
    max-width: 100%;
  }

  .security-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .item-left {
    width: 100%;
  }
}
</style>
