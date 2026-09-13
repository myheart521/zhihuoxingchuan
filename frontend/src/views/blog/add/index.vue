<script setup lang="ts">
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus,
  Document,
  Collection,
  PriceTag,
  Lock,
  User,
  Picture,
  Edit,
  Delete
} from '@element-plus/icons-vue'
import Editor from '@/components/Editor/src/Editor.vue'
import type { IDomEditor } from '@wangeditor/editor'
import { BlogApi } from '@/api/blog/blog'
import type { CreateBlogVO, PostVO } from '@/api/blog/blog'
import { getUploadUrl } from '@/components/UploadFile/src/useUpload'
import { getRefreshToken, getTenantId } from '@/utils/auth'
import { CategoryApi } from '@/api/blog/category'
import { TagApi } from '@/api/blog/tag'
import { useRoute, useRouter } from 'vue-router'

// 定义更新博客的请求类型，根据后端需要的数据格式
interface UpdateBlogDTO {
  id: string
  userId: number
  title: string
  content: string
  summary: string
  categoryId: number
  tagIds: number[]
  visibility: number
  status: number
  frontCover: string
}

// 定义创建博客的请求类型
interface CreateBlogDTO {
  title: string
  content: string
  summary: string
  categoryId: number
  tagIds: number[]
  visibility: number
  status: number
  frontCover: string
}

// 博客表单类型
interface BlogForm {
  id?: string
  userId?: number
  title: string
  content: string
  summary: string
  categoryId?: number
  tagIds: number[]
  visibility: number
  status: number
  frontCover: string
  isPractice: number // 新增：是否为实践文章字段
}

// 定义分类和标签数据结构
interface Category {
  id: number
  name: string
  parentId: number
  children?: Category[]
}

interface Tag {
  id: number
  name: string
  createTime: string
}

// 表单数据
const blogForm = reactive<BlogForm>({
  title: '',
  content: '',
  summary: '',
  categoryId: undefined,
  tagIds: [],
  visibility: 0, // 默认公开
  status: 0, // 默认为草稿
  frontCover: '',
  isPractice: 0 // 默认不是实践文章
})

// 编辑器实例引用
const editorRef = ref()

// 分类和标签数据
const categoryOptions = ref<Category[]>([])
const tagOptions = ref<Tag[]>([])

// 处理分类数据，构建树形结构
const processCategories = (categories: Category[]) => {
  const result: Category[] = []
  const map = new Map<number, Category>()

  // 先将所有分类放入map
  categories.forEach((category) => {
    map.set(category.id, { ...category, children: [] })
  })

  // 构建树形结构
  categories.forEach((category) => {
    const current = map.get(category.id)!
    if (category.parentId === 0) {
      // 顶级分类
      result.push(current)
    } else {
      // 子分类
      const parent = map.get(category.parentId)
      if (parent) {
        parent.children = parent.children || []
        parent.children.push(current)
      }
    }
  })

  return result
}

// 表单验证规则
const rules = {
  title: [
    { required: true, message: '请输入博客标题', trigger: 'blur' },
    { min: 2, max: 100, message: '标题长度在2到100个字符之间', trigger: 'blur' }
  ],
  summary: [
    { required: true, message: '请输入博客摘要', trigger: 'blur' },
    { min: 5, max: 300, message: '摘要长度在5到300个字符之间', trigger: 'blur' }
  ],
  categoryId: [{ required: true, message: '请选择博客分类', trigger: 'change' }],
  content: [{ required: true, message: '请输入博客内容', trigger: 'blur' }]
}

// 表单引用
const formRef = ref()

// 加载状态
const loading = ref(false)

// 可见性选项
const visibilityOptions = [
  { label: '公开', value: 0, icon: Document },
  { label: '仅自己可见', value: 1, icon: Lock },
  { label: '粉丝可见', value: 2, icon: User }
]

// 获取分类列表
const getCategoryList = async () => {
  try {
    const res = await CategoryApi.getCategoryList()
    if (res) {
      console.log('获取分类数据:', res)
      // 处理分类数据，构建树形结构
      categoryOptions.value = processCategories(res)
    } else {
      ElMessage.error(res.message || '获取分类列表失败')
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
    ElMessage.error('获取分类列表失败')
  }
}

// 获取标签列表
const getTagList = async () => {
  try {
    const res = await TagApi.getTagList()
    if (res) {
      console.log('获取标签数据:', res)
      tagOptions.value = res
    } else {
      ElMessage.error(res.message || '获取标签列表失败')
    }
  } catch (error) {
    console.error('获取标签列表失败:', error)
    ElMessage.error('获取标签列表失败')
  }
}

// 编辑器内容变化处理
const handleEditorChange = (editor: IDomEditor) => {
  blogForm.content = editor.getHtml()
}

const route = useRoute()
const router = useRouter()

// 获取博客详情
const getBlogDetail = async (id: string) => {
  try {
    const res = await BlogApi.getBlogDetail(Number(id))
    if (res) {
      // 将获取到的博客数据填充到表单中
      blogForm.id = id
      blogForm.userId = res.userId
      blogForm.title = res.title
      blogForm.content = res.content
      blogForm.summary = res.summary || ''
      blogForm.categoryId = res.category?.id // 确保这里正确赋值
      blogForm.tagIds = res.tags?.map(tag => tag.id) || [] // 确保这里正确赋值
      blogForm.visibility = res.visibility
      blogForm.status = res.status
      blogForm.frontCover = res.frontCover || ''
      blogForm.isPractice = res.isPractice || 0 // 设置实践文章状态

      // 更新编辑器内容
      const editor = await editorRef.value.getEditorRef()
      if (editor) {
        editor.setHtml(res.content)
      }

      console.log('加载的博客数据:', {
        categoryId: blogForm.categoryId,
        tagIds: blogForm.tagIds,
        isPractice: blogForm.isPractice
      })

      ElMessage.info('已加载博客数据，您可以开始编辑了')
    }
  } catch (error) {
    console.error('获取博客详情失败:', error)
    ElMessage.error('获取博客详情失败')
  }
}

// 修改页面标题
const pageTitle = computed(() => {
  return route.query.type === 'edit' ? '编辑博客' : '创建新博客'
})

// 修改提交方法
const submitForm = async (type: 'draft' | 'publish') => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    // 设置状态（草稿或提交审核）
    blogForm.status = type === 'draft' ? 0 : 1

    // 获取编辑器实例并获取内容
    const editor = await editorRef.value.getEditorRef()
    blogForm.content = editor.getHtml()

    if (!blogForm.content || blogForm.content === '<p><br></p>') {
      ElMessage.warning('博客内容不能为空')
      return
    }

    // 分类ID必须存在
    if (!blogForm.categoryId) {
      ElMessage.warning('请选择博客分类')
      return
    }

    loading.value = true

    // 判断是新增还是编辑
    const blogId = route.query.id
    let res

    // 打印提交前的数据，用于调试
    console.log('提交前的表单数据:', {
      categoryId: blogForm.categoryId,
      tagIds: blogForm.tagIds
    })
    
    if (blogId) {
      // 编辑博客
      const updateData = {
        id: blogId as string,
        userId: blogForm.userId,
        title: blogForm.title,
        content: blogForm.content,
        summary: blogForm.summary,
        categoryId: Number(blogForm.categoryId), // 确保转换为数字
        tagIds: blogForm.tagIds.map(id => Number(id)), // 确保数组中的每个ID都是数字
        visibility: blogForm.visibility,
        status: blogForm.status,
        frontCover: blogForm.frontCover || '',
        isPractice: blogForm.isPractice // 新增：传递实践文章字段
      }
      
      res = await BlogApi.updatePost(updateData)
    } else {
      // 新增博客
      const createData = {
        title: blogForm.title,
        content: blogForm.content,
        summary: blogForm.summary,
        categoryId: Number(blogForm.categoryId), // 确保转换为数字
        tagIds: blogForm.tagIds.map(id => Number(id)), // 确保数组中的每个ID都是数字
        visibility: blogForm.visibility,
        status: blogForm.status,
        frontCover: blogForm.frontCover || '',
        isPractice: blogForm.isPractice // 新增：传递实践文章字段
      }
      
      res = await BlogApi.createPost(createData)
    }

    // 根据响应处理结果
    if (res === true || res?.id) {
      ElMessage.success(type === 'draft' ? '保存草稿成功' : '提交审核成功')
      
      if (type === 'draft') {
        // 保存草稿后跳转到个人博客列表页
        router.push('/blog/detail/' + blogId)
      } else {
        // 提交审核后跳转到博客详情页
        if (res?.id) {
          // 如果返回了博客ID，使用返回的ID跳转
          router.push('/blog/detail/' + res.id)
        } else if (blogId) {
          // 否则使用当前编辑的博客ID跳转
          router.push('/blog/detail/' + blogId)
        } else {
          // 如果没有ID可用，跳转到博客列表页
          router.push('/blog/userbloglist')
        }
      }
    } else {
      ElMessage.error('提交失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请检查表单')
  } finally {
    loading.value = false
  }
}

// 保存为草稿
const saveDraft = () => {
  submitForm('draft')
}

// 提交审核方法
const submitForReview = () => {
  ElMessageBox.confirm('确定要提交审核吗？提交后将无法修改，直到审核完成。', '提交确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      submitForm('publish')
    })
    .catch(() => {
      // 取消操作
    })
}

// 重置表单
const resetForm = async () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }

  // 重置编辑器内容
  try {
    const editor = await editorRef.value.getEditorRef()
    editor.setHtml('<p><br></p>')
  } catch (error) {
    console.error('重置编辑器内容失败:', error)
  }

  // 重置表单数据
  Object.assign(blogForm, {
    title: '',
    content: '',
    summary: '',
    categoryId: undefined,
    tagIds: [],
    visibility: 0,
    status: 0,
    frontCover: '',
    isPractice: 0 // 重置实践文章状态
  })
}

// 上传封面图片
const handleCoverUpload = async (file: File) => {
  try {
    const formData = new FormData()
    formData.append('file', file)

    const response = await fetch(getUploadUrl(), {
      method: 'POST',
      headers: {
        Accept: '*',
        Authorization: '' + getRefreshToken(),
        'tenant-id': getTenantId()
      },
      body: formData
    })

    const result = await response.json()
    if (result.code === 0) {
      blogForm.frontCover = result.data
      return true
    } else {
      ElMessage.error('上传失败：' + result.message)
      return false
    }
  } catch (error) {
    console.error('上传失败:', error)
    ElMessage.error('上传失败，请重试')
    return false
  }
}

// 移除封面图片
const handleRemoveCover = () => {
  blogForm.frontCover = ''
}

// 添加分类选择的监听
const handleCategoryChange = (value: number) => {
  console.log('分类已更改:', value)
  blogForm.categoryId = value
}

// 添加标签选择的监听
const handleTagChange = (value: number[]) => {
  console.log('标签已更改:', value)
  // 限制最多选择5个标签
  if (value.length > 5) {
    ElMessage.warning('最多只能选择5个标签')
    blogForm.tagIds = value.slice(0, 5)
  } else {
    blogForm.tagIds = value
  }
}

// 新增：切换实践文章状态
const togglePracticeMode = () => {
  blogForm.isPractice = blogForm.isPractice === 1 ? 0 : 1
  ElMessage.success(blogForm.isPractice === 1 ? '发布为实践文章' : '取消发布为实践文章')
}

// 移除标签
const removeTag = (tagId: number) => {
  const index = blogForm.tagIds.indexOf(tagId)
  if (index > -1) {
    blogForm.tagIds.splice(index, 1)
  }
}

// 生命周期钩子
onMounted(async () => {
  // 先获取分类和标签数据
  await Promise.all([getCategoryList(), getTagList()])
  
  // 检查是否是编辑模式
  const { id, type } = route.query
  if (type === 'edit' && id) {
    // 如果是编辑模式，获取博客详情
    await getBlogDetail(id as string)
  } else {
    // 如果是新增模式，重置表单
    resetForm()
  }
})
</script>

<template>
  <div class="blog-add-container">
    <el-card class="blog-form-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="header-icon">
              <Edit />
            </el-icon>
            <h2>{{ pageTitle }}</h2>
          </div>
          <div class="header-actions">
            <el-button @click="resetForm" plain>
              <el-icon>
                <Delete />
              </el-icon>
              重置
            </el-button>
            <el-button type="info" @click="saveDraft" :loading="loading">
              <el-icon>
                <Document />
              </el-icon>
              保存草稿
            </el-button>
            <el-button type="primary" @click="submitForReview" :loading="loading">
              <el-icon>
                <Document />
              </el-icon>
              提交审核
            </el-button>
          </div>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="blogForm"
        :rules="rules"
        label-width="100px"
        label-position="top"
        class="blog-form"
      >
        <!-- 标题 -->
        <el-form-item label="博客标题" prop="title">
          <el-input
            v-model="blogForm.title"
            placeholder="请输入博客标题"
            maxlength="100"
            show-word-limit
            clearable
          >
            <template #prefix>
              <el-icon>
                <Document />
              </el-icon>
            </template>
          </el-input>
        </el-form-item>

        <!-- 摘要 -->
        <el-form-item label="博客摘要" prop="summary">
          <div class="form-item-tip">
            <el-alert
              title="摘要会显示在博客列表中，建议写一段简短的介绍"
              type="info"
              :closable="false"
              show-icon
            />
          </div>
          <el-input
            v-model="blogForm.summary"
            type="textarea"
            :rows="3"
            placeholder="请输入博客摘要，建议5-300字"
            maxlength="300"
            show-word-limit
            resize="none"
          />
        </el-form-item>

        <el-row :gutter="24">
          <!-- 分类 -->
          <el-col :xs="24" :sm="24" :md="12">
            <div class="form-item-tip">
              <el-alert
                title="分类只能选择一个，请选择最具体的分类"
                type="info"
                :closable="false"
                show-icon
              />
            </div>
            <el-form-item label="博客分类" prop="categoryId">
              <div class="category-selector">
                <el-icon class="selector-icon">
                  <Collection />
                </el-icon>
                <el-cascader
                  v-model="blogForm.categoryId"
                  :options="categoryOptions"
                  :props="{
                    value: 'id',
                    label: 'name',
                    children: 'children',
                    checkStrictly: true,
                    emitPath: false
                  }"
                  placeholder="请选择分类"
                  clearable
                  class="category-cascader"
                  @change="handleCategoryChange"
                />
              </div>
            </el-form-item>
          </el-col>

          <!-- 可见性 -->
          <el-col :xs="24" :sm="24" :md="12">
            <el-form-item label="可见性设置" prop="visibility">
              <el-radio-group v-model="blogForm.visibility" class="visibility-group">
                <el-radio-button
                  v-for="option in visibilityOptions"
                  :key="option.value"
                  :label="option.value"
                >
                  <el-icon>
                    <component :is="option.icon" />
                  </el-icon>
                  {{ option.label }}
                </el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 新增：实践文章设置行 -->
        <el-row :gutter="24">
          <el-col :xs="24" :sm="24" :md="12">
            <div class="form-item-tip">
              <el-alert
                title="实践文章将在实践专栏中展示，并参与特殊的活动"
                type="info"
                :closable="false"
                show-icon
              />
            </div>
            <el-form-item label="文章类型">
              <div class="practice-button-container">
                <button 
                  type="button" 
                  class="iridescent-button" 
                  :class="{ active: blogForm.isPractice === 1 }"
                  @click="togglePracticeMode"
                >
                  <span class="button-text">
                    {{ blogForm.isPractice === 1 ? '✓ 发布实践文章' : '设为实践文章' }}
                  </span>
                </button>
                <div class="practice-status" v-if="blogForm.isPractice === 1">
                  <el-tag type="success" effect="light">文章将发布为实践相关文章</el-tag>
                </div>
              </div>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="24" :md="12">
            <!-- 占位列，保持布局对称 -->
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <!-- 标签 -->
          <el-col :xs="24" :sm="24" :md="12">
            <div class="form-item-tip">
              <el-alert
                title="标签可以选择多个，最多选择5个标签"
                type="info"
                :closable="false"
                show-icon
              />
            </div>
            <el-form-item label="博客标签" prop="tagIds">
              <div class="tag-selector">
                <el-icon class="selector-icon">
                  <PriceTag />
                </el-icon>
                <el-select
                  v-model="blogForm.tagIds"
                  multiple
                  filterable
                  :multiple-limit="5"
                  placeholder="请选择标签"
                  style="width: 100%"
                  class="tag-select"
                  @change="handleTagChange"
                >
                  <el-option
                    v-for="item in tagOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  >
                    <div class="tag-option">
                      <el-tag
                        size="small"
                        :type="blogForm.tagIds.includes(item.id) ? 'primary' : 'info'"
                      >
                        {{ item.name }}
                      </el-tag>
                    </div>
                  </el-option>
                </el-select>
              </div>
              <div class="selected-tags" v-if="blogForm.tagIds.length > 0">
                <el-tag
                  v-for="tagId in blogForm.tagIds"
                  :key="tagId"
                  closable
                  @close="removeTag(tagId)"
                  class="tag-item"
                  effect="light"
                  type="primary"
                >
                  {{ tagOptions.find((tag) => tag.id === tagId)?.name }}
                </el-tag>
              </div>
            </el-form-item>
          </el-col>

          <!-- 封面图片 -->
          <el-col :xs="24" :sm="24" :md="12">
            <div class="form-item-tip">
              <el-alert
                title="封面图片将显示在博客列表中，建议尺寸比例16:9"
                type="info"
                :closable="false"
                show-icon
              />
            </div>
            <el-form-item label="封面图片">
              <div class="cover-container">
                <el-upload
                  class="cover-uploader"
                  action="#"
                  :show-file-list="false"
                  :before-upload="handleCoverUpload"
                >
                  <div class="cover-content">
                    <img
                      v-if="blogForm.frontCover"
                      :src="blogForm.frontCover"
                      class="cover-image"
                    />
                    <div v-else class="upload-placeholder">
                      <el-icon class="cover-uploader-icon">
                        <Picture />
                      </el-icon>
                      <span>点击上传封面图片</span>
                    </div>
                  </div>
                </el-upload>
                <el-button
                  v-if="blogForm.frontCover"
                  type="danger"
                  size="small"
                  @click="handleRemoveCover"
                  class="remove-cover-btn"
                >
                  <el-icon>
                    <Delete />
                  </el-icon>
                  移除封面
                </el-button>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 内容编辑器 -->
        <el-form-item label="博客内容" prop="content">
          <Editor
            ref="editorRef"
            v-model="blogForm.content"
            :height="500"
            @change="handleEditorChange"
          />
        </el-form-item>

        <!-- 提交按钮 -->
        <el-form-item>
          <div class="form-actions">
            <el-button @click="resetForm" plain>
              <el-icon>
                <Delete />
              </el-icon>
              重置
            </el-button>
            <el-button type="info" @click="saveDraft" :loading="loading">
              <el-icon>
                <Document />
              </el-icon>
              保存草稿
            </el-button>
            <el-button type="primary" @click="submitForReview" :loading="loading">
              <el-icon>
                <Document />
              </el-icon>
              提交审核
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.blog-add-container {
  padding: 20px;
  background-color: #f8f8f8;
  min-height: calc(100vh - 60px);
  
  // 红色主题色变量
  $primary-color: #c52121;
  $secondary-color: #8c1515;
  $text-color: #333;
  $highlight-color: #f5e3e3;

  // 虹彩按钮样式变量
  $purple: #799df1;
  $light-blue: #60c1ec;
  $green: #7bd9de;
  $pink: #cfb0d2;
  $grey: #c7c8c9;
  $shine: #f7f7f6;
  $black: #000;

  .blog-form-card {
    max-width: 1200px;
    margin: 0 auto 20px;
    border-radius: 8px;
    border-top: 4px solid $primary-color;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #eee;
      padding-bottom: 16px;

      .header-title {
        display: flex;
        align-items: center;
        gap: 10px;

        .header-icon {
          font-size: 22px;
          color: $primary-color;
        }

        h2 {
          margin: 0;
          font-size: 20px;
          font-weight: 600;
          color: $primary-color;
          position: relative;
          padding-left: 10px;
          
          &::before {
            content: '';
            position: absolute;
            left: 0;
            top: 50%;
            transform: translateY(-50%);
            width: 4px;
            height: 18px;
            background-color: $primary-color;
            border-radius: 2px;
          }
        }
      }

      .header-actions {
        display: flex;
        gap: 10px;

        .el-button {
          display: flex;
          align-items: center;
          gap: 5px;
          
          &.el-button--primary {
            background-color: $primary-color;
            border-color: $primary-color;
            
            &:hover, &:focus {
              background-color: lighten($primary-color, 5%);
              border-color: lighten($primary-color, 5%);
            }
          }
          
          &.el-button--info {
            background-color: #666;
            border-color: #666;
            
            &:hover, &:focus {
              background-color: #777;
              border-color: #777;
            }
          }
        }
      }
    }

    .blog-form {
      margin-top: 20px;

      .el-form-item__label {
        font-weight: 500;
        color: $text-color;
      }

      .el-form-item {
        &.is-required {
          .el-form-item__label::before {
            margin-right: 4px;
            color: $primary-color;
          }
        }
        
        .el-input__count {
          background: transparent;
        }
      }
      
      .form-item-tip {
        margin-bottom: 10px;
        
        .el-alert {
          padding: 8px 16px;
          background-color: $highlight-color;
          color: $secondary-color;
          
          .el-alert__icon {
            color: $primary-color;
          }
          
          .el-alert__title {
            color: $secondary-color;
          }
        }
      }

      // 新增：实践文章按钮容器样式
      .practice-button-container {
        display: flex;
        flex-direction: column;
        gap: 12px;
        align-items: flex-start;

        .practice-status {
          .el-tag {
            font-size: 12px;
          }
        }
      }

      // 新增：虹彩按钮样式
      .iridescent-button {
        position: relative;
        z-index: 0;
        padding: 12px 24px;
        border: 1px solid $purple;
        border-radius: 9999px;
        font-size: 14px;
        font-weight: bold;
        text-transform: uppercase;
        color: $black;
        box-shadow: inset 0 0 1rem 0 $shine;
        cursor: pointer;
        overflow: hidden;
        transition: all 0.5s ease;
        background: linear-gradient(
          60deg,
          $purple 5%,
          $light-blue 15%,
          $green 25%,
          $pink 35%,
          $purple 45%,
          $light-blue 55%,
          $green 65%,
          $pink 75%,
          $purple 85%,
          $light-blue 95%,
          $green
        );
        background-size: 200% 200%;
        background-position: -100% -100%;
        
        @keyframes moving1 {
          20% {
            background-position: -130% -120%;
          }
          40% {
            background-position: -140% -100%;
          }
          70% {
            background-position: -110% -130%;
          }
        }
        
        animation: moving1 8s infinite;

        &::before {
          content: "";
          position: absolute;
          top: 0.2rem;
          left: 0.2rem;
          width: calc(100% - 0.4rem);
          height: calc(100% - 0.4rem);
          border-radius: 9999px;
          border-top: 1px solid rgba($shine, 0.5);
          border-right: 1px solid rgba($shine, 0.5);
          box-shadow: 0 0 1rem 1rem rgba($grey, 0.4);
          background: linear-gradient(180deg, transparent, $grey 160%);
          overflow: hidden;
        }

        &::after {
          content: "";
          width: 200%;
          height: 200%;
          position: absolute;
          top: -50%;
          left: -50%;
          background: linear-gradient(
            150deg,
            $shine,
            transparent 20%,
            transparent 60%,
            $shine 75%,
            $shine
          );
        }

        .button-text {
          position: relative;
          z-index: 1;
          display: flex;
          align-items: center;
          gap: 6px;
        }

        &:hover {
          box-shadow: inset 0 0 1rem 0 $shine, 0 0 1rem 0 $shine;
          transform: translateY(-2px);
        }

        &.active {
          background: linear-gradient(
            60deg,
            #d62828 5%,
            #c52121 15%,
            #8c1515 25%,
            #d62828 35%,
            #c52121 45%,
            #8c1515 55%,
            #d62828 65%,
            #c52121 75%,
            #8c1515 85%,
            #d62828 95%,
            #c52121
          );
          color: white;
          border-color: #d62828;
          
          &::before {
            background: linear-gradient(180deg, transparent, rgba(#8c1515, 0.8) 160%);
          }
        }

        @media screen and (max-width: 450px) {
          font-size: 12px;
          padding: 10px 20px;
        }
      }

      .category-selector,
      .tag-selector {
        width: 100%;
        display: flex;
        align-items: center;
        gap: 10px;

        .selector-icon {
          font-size: 18px;
          color: $primary-color;
        }

        .category-cascader,
        .tag-select {
          flex: 1;
          
          :deep(.el-input__wrapper) {
            &.is-focus {
              box-shadow: 0 0 0 1px $primary-color inset;
            }
          }
        }
      }

      .selected-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 8px;
        margin-top: 10px;

        .tag-item {
          cursor: default;
          transition: all 0.3s;
          background-color: $highlight-color;
          border-color: lighten($primary-color, 25%);
          color: $primary-color;

          &:hover {
            transform: translateY(-2px);
          }
        }
      }

      .tag-option {
        display: flex;
        align-items: center;
      }

      .visibility-group {
        width: 100%;
        display: flex;

        :deep(.el-radio-button) {
          flex: 1;

          .el-radio-button__inner {
            width: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
          }
          
          &.is-active .el-radio-button__inner {
            background-color: $primary-color;
            border-color: $primary-color;
            box-shadow: -1px 0 0 0 $primary-color;
          }
        }
      }

      .cover-container {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;

        .cover-uploader {
          width: 100%;
          max-width: 320px;

          :deep(.el-upload) {
            width: 100%;
            border: 1px dashed #d9d9d9;
            border-radius: 8px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: border-color 0.3s;

            &:hover {
              border-color: $primary-color;
            }
          }

          .cover-content {
            width: 100%;
            aspect-ratio: 16/9;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: #f8f8f8;
            border-radius: 8px;
          }

          .upload-placeholder {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 10px;
            width: max-content;
            color: #8c939d;

            .cover-uploader-icon {
              font-size: 40px;
              color: $primary-color;
            }
          }

          .cover-image {
            width: 100%;
            height: 100%;
            display: block;
            object-fit: cover;
            border-radius: 8px;
          }
        }

        .remove-cover-btn {
          margin-top: 10px;
          display: flex;
          align-items: center;
          gap: 5px;
        }
      }
    }

    .form-actions {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px solid #eee;

      .el-button {
        display: flex;
        align-items: center;
        gap: 5px;
        
        &.el-button--primary {
          background-color: $primary-color;
          border-color: $primary-color;
          
          &:hover, &:focus {
            background-color: lighten($primary-color, 5%);
            border-color: lighten($primary-color, 5%);
          }
        }
      }
    }
  }
}

@media (max-width: 768px) {
  .blog-add-container {
    padding: 10px;

    .card-header {
      flex-direction: column;
      align-items: flex-start;

      .header-actions {
        margin-top: 10px;
        width: 100%;
        justify-content: flex-end;
      }
    }

    .visibility-group {
      flex-direction: column;

      :deep(.el-radio-button:first-child .el-radio-button__inner) {
        border-radius: 4px 4px 0 0;
      }

      :deep(.el-radio-button:last-child .el-radio-button__inner) {
        border-radius: 0 0 4px 4px;
      }
    }
  }
}
</style>
