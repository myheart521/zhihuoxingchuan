<script setup lang="ts">
import { ref, onMounted, reactive, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElLoading } from 'element-plus'
import { formatDate } from '@/utils/formatTime'
import { ChatLineRound, Warning, Star, User, Key } from '@element-plus/icons-vue'
import { BlogApi } from '@/api/blog/blog'
import { CommentReplyApi, CommentApi } from '@/api/blog/comment'
import { useUserStore } from '@/store/modules/user'
import { AccuseApi } from '@/api/blog/accuse'
import { getIntDictOptions } from '@/utils/dict'
import { DICT_TYPE } from '@/utils/dict'
import { getAccessToken } from '@/utils/auth'

const route = useRoute()
const router = useRouter()

const userStore = useUserStore()
const user = userStore.user

// 定义接口
interface Author {
  id: number
  username: string
  name: string[] // 角色名称列表
  code: string[] // 角色编码列表
  reputation: number
  blogCount: number
  likeCount: number
}

interface Category {
  id: number
  name: string
  parentId: number
}

interface Tag {
  id: number
  name: string
}

interface BlogDetail {
  id: number
  userId: number
  title: string
  content: string
  summary: string
  frontCover: string
  status: number
  isTop: number
  topTime: Date
  visibility: number
  viewCount: number
  likeCount: number
  auditUserId: number
  auditTime: Date
  auditComment: string
  createTime: Date
  updateTime: Date
  author: Author
  category: Category
  tags: Tag[]
  praise: boolean // 添加点赞状态字段
}

interface Comment {
  id: number
  postId: number
  userId: number
  content: string
  status: number
  auditUserId: number
  createTime: number
  username: string
  userRole: string
  children?: CommentReply[]
}

interface CommentReply {
  id: number
  parentId: number
  postId: number
  userId: number
  replyToUserId: number
  content: string
  status: number
  auditUserId: number
  createTime: number
  username: string
  userRole: string
}

// 状态常量
const statusMap = {
  0: { label: '草稿', type: 'info' },
  1: { label: '待审核', type: 'warning' },
  2: { label: '已发布', type: 'success' },
  3: { label: '已下架', type: 'danger' }
}

const visibilityMap = {
  0: { label: '公开', type: 'success' },
  1: { label: '仅自己可见', type: 'warning' },
  2: { label: '粉丝可见', type: 'info' }
}

// 博客详情数据
const loading = ref(false)
const blogDetail = ref<BlogDetail>({
  id: 0,
  userId: 0,
  title: '',
  content: '',
  summary: '',
  frontCover: '',
  status: 0,
  isTop: 0,
  topTime: new Date(),
  visibility: 0,
  viewCount: 0,
  likeCount: 0,
  auditUserId: 0,
  auditTime: new Date(),
  auditComment: '',
  createTime: new Date(),
  updateTime: new Date(),
  author: {
    id: 0,
    username: '',
    name: [],
    code: [],
    reputation: 0,
    blogCount: 0,
    likeCount: 0
  },
  category: {
    id: 0,
    name: '',
    parentId: 0
  },
  tags: [],
  praise: false
})

// 添加评论列表数据
const commentList = ref<Comment[]>([])
const commentContent = ref('')
const replyTo = ref<{
  commentId: number
  username: string
} | null>(null)

const commentLoading = ref(false)

// 添加提交状态
const submitting = ref(false)

// 修改评论提交接口定义
interface CommentCreateReq {
  postId: number
  userId: number
  content: string
  status: number
  id?: number
  auditUserId?: number
}

// 添加回复弹窗相关的响应式变量
const replyDialogVisible = ref(false)
const replyForm = reactive({
  parentId: 0,
  postId: 0,
  userId: 0,
  replyToUserId: 0,
  content: '',
  status: 1
})

// 添加分页相关的响应式变量
const commentQuery = reactive({
  pageNo: 1,
  pageSize: 10,
  total: 0
})

const replyQueryMap = reactive(
  new Map<
    number,
    {
      pageNo: number
      pageSize: number
      total: number
    }
  >()
)

// 添加举报相关的响应式变量
const accuseDialogVisible = ref(false)
const accuseForm = reactive({
  postId: 0,
  accuseUserId: 0,
  type: '',
  content: ''
})
const accuseTypeOptions = getIntDictOptions(DICT_TYPE.BLOG_ACCUSE_TYPE)

// 检查用户是否已登录
const isLoggedIn = computed(() => !!getAccessToken())

// 获取博客详情
const getBlogDetail = async (id: string | number) => {
  loading.value = true
  try {
    const res = await BlogApi.getBlogDetail(Number(id))
    if (res) {
      blogDetail.value = res
      // 获取博客详情后加载评论列表
      await getCommentList(res.id)
    } else {
      ElMessage.error('获取博客详情失败')
    }
  } catch (error) {
    console.error('获取博客详情失败:', error)
    ElMessage.error('获取博客详情失败')
  } finally {
    loading.value = false
  }
}

// 获取评论列表
const getCommentList = async (postId: number, isLoadMore = false) => {
  commentLoading.value = true
  try {
    const res = await CommentApi.getCommentPage({
      pageNo: commentQuery.pageNo,
      pageSize: commentQuery.pageSize,
      postId,
      status: 1
    })
    if (res) {
      if (isLoadMore) {
        commentList.value = [...commentList.value, ...res.list]
      } else {
        commentList.value = res.list
      }
      commentQuery.total = res.total
      // 获取每个评论的回复列表
      for (const comment of res.list) {
        await getReplyList(comment.id)
      }
    }
  } catch (error) {
    console.error('获取评论列表失败:', error)
    ElMessage.error('获取评论列表失败')
  } finally {
    commentLoading.value = false
  }
}

// 获取回复列表
const getReplyList = async (commentId: number, isLoadMore = false) => {
  try {
    let query = replyQueryMap.get(commentId)
    if (!query) {
      query = { pageNo: 1, pageSize: 5, total: 0 }
      replyQueryMap.set(commentId, query)
    }

    const res = await CommentReplyApi.getCommentReplyPage({
      pageNo: query.pageNo,
      pageSize: query.pageSize,
      parentId: commentId,
      status: 1
    })
    if (res) {
      const comment = commentList.value.find((c) => c.id === commentId)
      if (comment) {
        if (isLoadMore) {
          comment.children = [...(comment.children || []), ...res.list]
        } else {
          comment.children = res.list
        }
      }
      query.total = res.total
      replyQueryMap.set(commentId, query)
    }
  } catch (error) {
    console.error('获取回复列表失败:', error)
  }
}

// 修改提交评论方法
const submitComment = async () => {
  console.log('点击提交评论', {
    isLoggedIn: isLoggedIn.value,
    content: commentContent.value,
    userId: userStore.user.id,
    postId: blogDetail.value.id
  })
  
  if (submitting.value) return // 防止重复提交
  
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录后再发表评论')
    router.push(`/login?redirect=${route.fullPath}`)
    return
  }

  if (!commentContent.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  submitting.value = true
  try {
    const params: CommentCreateReq = {
      postId: blogDetail.value.id,
      userId: userStore.user.id,
      content: commentContent.value.trim(),
      status: 1,
      auditUserId: 0
    }

    const res = await CommentApi.createComment(params)
    if (res) {
      ElMessage.success('评论成功')
      commentContent.value = ''
      replyTo.value = null
      await getCommentList(blogDetail.value.id)
    }
  } catch (error) {
    console.error('提交评论失败:', error)
    ElMessage.error('提交评论失败')
  } finally {
    submitting.value = false
  }
}

// 修改回复评论方法
const handleReply = (comment: Comment | CommentReply) => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录后再回复评论')
    router.push(`/login?redirect=${route.fullPath}`)
    return
  }

  // 如果是回复二级评论，parentId使用原评论的parentId
  const isReply = 'parentId' in comment
  replyForm.parentId = isReply ? comment.parentId : comment.id
  replyForm.postId = blogDetail.value.id
  replyForm.userId = userStore.user.id
  replyForm.replyToUserId = comment.userId // 保存被回复用户的ID
  replyForm.content = ''
  replyForm.status = 1

  replyDialogVisible.value = true
}

// 提交回复
const submitReply = async () => {
  if (!replyForm.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }

  try {
    const res = await CommentReplyApi.createCommentReply(replyForm)
    if (res) {
      ElMessage.success('回复成功')
      replyDialogVisible.value = false
      // 重新获取该评论的回复列表
      await getReplyList(replyForm.parentId)
    }
  } catch (error) {
    console.error('回复失败:', error)
    ElMessage.error('回复失败')
  }
}

// 取消回复
const cancelReply = () => {
  replyDialogVisible.value = false
  replyForm.content = ''
}

// 加载更多一级评论
const loadMoreComments = async () => {
  if (commentLoading.value) return
  if (commentQuery.pageNo * commentQuery.pageSize >= commentQuery.total) {
    ElMessage.info('没有更多评论了')
    return
  }

  commentQuery.pageNo++
  await getCommentList(blogDetail.value.id, true)
}

// 加载更多回复
const loadMoreReplies = async (commentId: number) => {
  const query = replyQueryMap.get(commentId)
  if (!query) return

  if (query.pageNo * query.pageSize >= query.total) {
    ElMessage.info('没有更多回复了')
    return
  }

  query.pageNo++
  await getReplyList(commentId, true)
}

// 修改 formatDate 的调用方式，添加时间格式化函数
const formatCommentTime = (timeStr: string) => {
  return formatDate(new Date(timeStr))
}

// 检查用户是否举报过该博客
const checkUserAccused = (userId: number, postId: number): boolean => {
  const accusedList = localStorage.getItem('userAccusedPosts')
  if (!accusedList) return false
  
  try {
    const accusedPosts = JSON.parse(accusedList)
    return accusedPosts.some((item: { userId: number; postId: number }) => 
      item.userId === userId && item.postId === postId
    )
  } catch (error) {
    console.error('解析举报记录失败:', error)
    return false
  }
}

// 记录用户举报记录
const saveUserAccused = (userId: number, postId: number) => {
  try {
    const accusedList = localStorage.getItem('userAccusedPosts')
    let accusedPosts = []
    
    if (accusedList) {
      accusedPosts = JSON.parse(accusedList)
    }
    
    accusedPosts.push({ userId, postId })
    localStorage.setItem('userAccusedPosts', JSON.stringify(accusedPosts))
  } catch (error) {
    console.error('保存举报记录失败:', error)
  }
}

// 修改举报按钮的点击处理方法
const handleAccuse = () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录后再举报')
    router.push(`/login?redirect=${route.fullPath}`)
    return
  }
  
  // 检查是否已经举报过
  if (checkUserAccused(userStore.user.id, blogDetail.value.id)) {
    ElMessage.warning('您已经举报过该博客')
    return
  }
  
  accuseForm.postId = blogDetail.value.id
  accuseForm.accuseUserId = userStore.user.id
  accuseForm.type = ''
  accuseForm.content = ''
  accuseDialogVisible.value = true
}

// 修改提交举报的方法
const submitAccuse = async () => {
  if (!accuseForm.type) {
    ElMessage.warning('请选择举报类型')
    return
  }
  if (!accuseForm.content) {
    ElMessage.warning('请输入举报详情')
    return
  }

  try {
    const selectedType = accuseTypeOptions.find(item => item.value === accuseForm.type)
    if (!selectedType) {
      ElMessage.warning('举报类型无效')
      return
    }

    const params = {
      ...accuseForm,
      type: selectedType.label
    }

    const res = await AccuseApi.createAccuse(params)
    if (res) {
      // 举报成功后保存记录
      saveUserAccused(accuseForm.userId, accuseForm.postId)
      ElMessage.success('举报成功')
      accuseDialogVisible.value = false
    }
  } catch (error) {
    console.error('举报失败:', error)
    ElMessage.error('举报失败')
  }
}

const cancelAccuse = () => {
  accuseDialogVisible.value = false
  accuseForm.type = ''
  accuseForm.content = ''
}

// 修改点赞方法
const handleLike = async () => {
  if (!isLoggedIn.value) {
    ElMessage.warning('请先登录后再点赞')
    router.push(`/login?redirect=${route.fullPath}`)
    return
  }
  
  try {
    const res = await BlogApi.likeBlog(blogDetail.value.id)
    if (res) {
      blogDetail.value.likeCount += 1
      blogDetail.value.praise = true
      ElMessage.success('点赞成功')
    }
  } catch (error) {
    console.error('点赞失败:', error)
    ElMessage.error('点赞失败')
  }
}

// 检查是否为未登录状态访问博客详情页
onMounted(() => {
  // 获取路由参数中的博客ID
  const blogId = route.params.id
  if (!blogId) {
    ElMessage.error('博客ID不能为空')
    router.push('/')
    return
  }

  // 无论是否登录都获取博客详情
  getBlogDetail(blogId)
})

// 修改角色标签类型
const getRoleTagType = (role: string): 'danger' | 'warning' | '' => {
  switch (role) {
    case '超级管理员':
      return 'danger'
    case '博客管理员':
      return 'warning'
    default:
      return ''
  }
}

// 修改二级评论显示部分
const getReplyToUsername = (parentComment: Comment, replyToUserId: number): string => {
  // 如果回复的是一级评论
  if (parentComment.userId === replyToUserId) {
    return parentComment.username
  }
  
  // 如果回复的是二级评论，在二级评论列表中查找
  const replyTo = parentComment.children?.find(reply => reply.userId === replyToUserId)
  return replyTo ? replyTo.username : '未知用户'
}
</script>

<template>
  <div class="blog-detail-container" v-loading="loading">
    <!-- 未登录用户提示登录卡片 - 美化版本 -->
    <div v-if="!isLoggedIn" class="login-tip-wrapper">
      <el-card class="login-tip-card" shadow="hover">
        <div class="login-tip-content">
          <el-image 
            class="login-tip-image" 
            src="/public/login.png"
            fit="contain"
          />
          <div class="login-tip-info">
            <h3 class="login-tip-title">登录后体验更多功能</h3>
            <p class="login-tip-description">点赞支持作者、参与评论讨论、举报不良内容</p>
            <div class="login-tip-actions">
              <el-button 
                type="primary" 
                @click="router.push(`/login?redirect=${route.fullPath}`)"
                round
                :icon="User"
              >
                立即登录
              </el-button>
              <el-button 
                type="success" 
                @click="router.push(`/login?redirect=${route.fullPath}`)" 
                plain 
                round
                :icon="Key"
              >
                注册账号
              </el-button>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 博客详情卡片 -->
    <el-card class="blog-card" shadow="never" v-if="blogDetail.id">
      <!-- 博客封面图片 - 移动到顶部 -->
      <div class="blog-cover" v-if="blogDetail.frontCover">
        <el-image 
          :src="blogDetail.frontCover" 
          :alt="blogDetail.title"
          fit="contain"
          :preview-src-list="[blogDetail.frontCover]"
        />
      </div>

      <!-- 博客头部信息 -->
      <div class="blog-header">
        <div class="blog-title-wrapper">
          <h1 class="blog-title">
            <el-tag v-if="blogDetail.isTop" type="danger" effect="dark" class="top-tag">置顶</el-tag>
            {{ blogDetail.title }}
          </h1>
          <div class="blog-actions">
            <el-button 
              type="danger" 
              :disabled="blogDetail.praise || !isLoggedIn"
              @click="handleLike"
              class="like-btn"
            >
              <el-icon><Star /></el-icon>
              {{ !isLoggedIn ? '登录后点赞' : (blogDetail.praise ? '已点赞' : '点赞') }}
              <span class="like-count" v-if="blogDetail.likeCount > 0">({{ blogDetail.likeCount }})</span>
            </el-button>
            <el-button 
              type="danger" 
              link 
              @click="handleAccuse"
              class="accuse-btn"
              :disabled="!isLoggedIn || checkUserAccused(userStore.user.id, blogDetail.id)"
            >
              <el-icon><Warning /></el-icon>
              {{ !isLoggedIn ? '登录后举报' : (checkUserAccused(userStore.user.id, blogDetail.id) ? '已举报' : '举报') }}
            </el-button>
          </div>
        </div>
        <div class="blog-meta">
          <el-avatar :size="32" class="author-avatar">
            {{ blogDetail.author?.username?.charAt(0) || '?' }}
          </el-avatar>
          <div class="meta-info">
            <div class="author-info">
              <span class="author-name">{{ blogDetail.author?.username }}</span>
              <el-tag
                v-for="(role, index) in blogDetail.author?.name"
                :key="index"
                size="small"
                :type="blogDetail.author?.code[index]?.includes('admin') ? 'danger' : 'info'"
              >
                {{ role }}
              </el-tag>
            </div>
            <div class="post-info">
              <span>发布于 {{ formatDate(new Date(blogDetail.createTime)) }}</span>
              <el-divider direction="vertical" />
              <span>阅读 {{ blogDetail.viewCount }}</span>
              <el-divider direction="vertical" />
              <span>点赞 {{ blogDetail.likeCount }}</span>
            </div>
          </div>
        </div>
        <div class="blog-tags">
          <el-tag class="status-tag" :type="statusMap[blogDetail.status].type">
            {{ statusMap[blogDetail.status].label }}
          </el-tag>
          <el-tag class="visibility-tag" :type="visibilityMap[blogDetail.visibility].type">
            {{ visibilityMap[blogDetail.visibility].label }}
          </el-tag>
          <el-tag type="success">{{ blogDetail.category.name }}</el-tag>
          <el-tag v-for="tag in blogDetail.tags" :key="tag.id" type="info">{{ tag.name }}</el-tag>
        </div>
      </div>

      <!-- 博客摘要 -->
      <div class="blog-summary" v-if="blogDetail.summary">
        <p class="summary-text">{{ blogDetail.summary }}</p>
      </div>

      <!-- 博客内容 -->
      <el-divider />
      <div class="blog-content" v-html="blogDetail.content"></div>

      <!-- 审核信息 -->
      <el-divider v-if="blogDetail.auditComment" />
      <div v-if="blogDetail.auditComment" class="audit-info">
        <h3>审核信息</h3>
        <p>{{ blogDetail.auditComment }}</p>
        <p class="audit-time">审核时间：{{ formatDate(blogDetail.auditTime) }}</p>
      </div>

      <!-- 在博客内容后添加评论区域 -->
      <div class="blog-comments">
        <el-divider>
          <el-icon>
            <ChatLineRound />
          </el-icon>
          <span>评论区</span>
        </el-divider>

        <!-- 评论输入框 -->
        <div class="comment-input">
          <el-input
            v-model="commentContent"
            type="textarea"
            :rows="3"
            :placeholder="isLoggedIn ? '写下你的评论...' : '请先登录后发表评论'"
            :disabled="!isLoggedIn"
          />
          <div class="comment-actions">
            <el-button 
              type="primary" 
              @click="submitComment"
              :loading="submitting"
              :disabled="!isLoggedIn || !commentContent.trim()"
            >
              {{ isLoggedIn ? '发表评论' : '登录后评论' }}
            </el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comment-list" v-loading="commentLoading">
          <template v-if="commentList.length">
            <div v-for="comment in commentList" :key="comment.id" class="comment-item">
              <!-- 一级评论 -->
              <div class="comment-main">
                <el-avatar :size="40" class="user-avatar">
                  {{ comment.username?.charAt(0)?.toUpperCase() }}
                </el-avatar>
                <div class="comment-content">
                  <div class="comment-header">
                    <div class="user-info">
                      <span class="username">{{ comment.username }}</span>
                      <el-tag 
                        v-if="comment.userRole && comment.userRole !== '博客审核用户'" 
                        :type="getRoleTagType(comment.userRole)"
                        size="small"
                        effect="plain"
                        class="role-tag"
                      >
                        {{ comment.userRole }}
                      </el-tag>
                    </div>
                    <span class="time">{{ formatCommentTime(comment.createTime) }}</span>
                  </div>
                  <div class="comment-text" v-html="comment.content"></div>
                  <div class="comment-actions">
                    <el-button link type="primary" @click="handleReply(comment)">回复</el-button>
                  </div>
                </div>
              </div>

              <!-- 二级评论 -->
              <div v-if="comment.children?.length" class="comment-replies">
                <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                  <div class="comment-main">
                    <el-avatar :size="32" class="user-avatar">
                      {{ reply.username?.charAt(0)?.toUpperCase() }}
                    </el-avatar>
                    <div class="comment-content">
                      <div class="comment-header">
                        <div class="user-info">
                          <span class="username">{{ reply.username }}</span>
                          <el-tag 
                            v-if="reply.userRole && reply.userRole !== '博客审核用户'" 
                            :type="getRoleTagType(reply.userRole)"
                            size="small"
                            effect="plain"
                            class="role-tag"
                          >
                            {{ reply.userRole }}
                          </el-tag>
                          <template v-if="reply.replyToUserId">
                            <span class="reply-to">回复</span>
                            <span class="reply-username">{{ getReplyToUsername(comment, reply.replyToUserId) }}</span>
                          </template>
                        </div>
                        <span class="time">{{ formatCommentTime(reply.createTime) }}</span>
                      </div>
                      <div class="comment-text" v-html="reply.content"></div>
                      <div class="comment-actions">
                        <el-button link type="primary" @click="handleReply(reply)">回复</el-button>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- 加载更多回复按钮 -->
                <div
                  v-if="comment.children && replyQueryMap.get(comment.id) && replyQueryMap.get(comment.id)!.total > comment.children.length"
                  class="load-more-replies"
                >
                  <el-button link type="primary" @click="loadMoreReplies(comment.id)">
                    加载更多回复
                  </el-button>
                </div>
              </div>
            </div>
          </template>
          <el-empty v-else description="暂无评论" />

          <!-- 加载更多评论按钮 -->
          <div 
            v-if="commentList.length > 0 && commentQuery.total > commentList.length" 
            class="load-more"
          >
            <el-button 
              type="primary" 
              link 
              :loading="commentLoading" 
              @click="loadMoreComments"
            >
              加载更多评论（{{ commentList.length }}/{{ commentQuery.total }}）
            </el-button>
          </div>
        </div>
      </div>
    </el-card>
    <el-empty v-else description="博客不存在或已被删除" />

    <!-- 回复弹窗 -->
    <el-dialog
      v-model="replyDialogVisible"
      title="回复评论"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="replyForm" label-width="0">
        <el-form-item>
          <el-input
            v-model="replyForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入回复内容..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelReply">取消</el-button>
          <el-button type="primary" @click="submitReply"> 发表回复 </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 举报弹窗 -->
    <el-dialog
      v-model="accuseDialogVisible"
      title="举报博客"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="accuseForm" label-width="80px">
        <el-form-item label="举报类型" required>
          <el-select 
            v-model="accuseForm.type" 
            placeholder="请选择举报类型" 
            class="w-full"
            clearable
          >
            <el-option
              v-for="item in accuseTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="举报详情" required>
          <el-input
            v-model="accuseForm.content"
            type="textarea"
            :rows="4"
            placeholder="请详细描述举报原因..."
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="cancelAccuse">取消</el-button>
          <el-button type="primary" @click="submitAccuse">提交举报</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.blog-detail-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;

  .blog-card {
    /* 修改封面图样式，移到顶部并保持宽高比 */
    .blog-cover {
      margin: 0 0 20px 0;
      border-radius: 8px;
      overflow: hidden;
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f5f7fa;

      .el-image {
        width: 100%;
        max-height: 400px;
        object-fit: contain;
        
        :deep(img) {
          object-fit: contain;
          max-width: 100%;
          max-height: 400px;
        }
      }
    }

    .blog-header {
      .blog-title-wrapper {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 20px;

        .blog-title {
          margin: 0;
          flex: 1;
        }

        .blog-actions {
          display: flex;
          gap: 10px;
          align-items: center;
          
          .like-btn {
            display: flex;
            align-items: center;
            gap: 5px;
            
            .el-icon {
              margin-right: 3px;
            }
            
            .like-count {
              font-size: 0.9em;
            }
            
            &:disabled {
              opacity: 0.8;
            }
          }
          
          .accuse-btn {
            padding: 4px 0;
            
            .el-icon {
              margin-right: 4px;
            }
          }
        }
      }

      .blog-meta {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 20px;

        .meta-info {
          .author-info {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 4px;

            .author-name {
              font-size: 16px;
              font-weight: 500;
            }
          }

          .post-info {
            font-size: 14px;
            color: var(--el-text-color-secondary);
          }
        }
      }

      .blog-tags {
        display: flex;
        gap: 8px;
        flex-wrap: wrap;
        margin-bottom: 20px;
      }
    }

    .blog-content {
      font-size: 16px;
      line-height: 1.8;
      
      :deep(h1, h2, h3, h4, h5, h6) {
        margin: 24px 0 16px;
        font-weight: 600;
      }

      :deep(p) {
        margin: 16px 0;
      }

      :deep(img) {
        max-width: 100%;
        height: auto;
      }

      :deep(pre) {
        background-color: var(--el-fill-color-light);
        padding: 16px;
        border-radius: 4px;
        overflow-x: auto;
      }
    }

    .audit-info {
      background-color: var(--el-fill-color-light);
      padding: 16px;
      border-radius: 4px;

      h3 {
        margin: 0 0 12px;
        font-size: 18px;
        font-weight: 600;
      }

      .audit-time {
        margin-top: 8px;
        font-size: 14px;
        color: var(--el-text-color-secondary);
      }
    }

    /* 博客摘要样式 */
    .blog-summary {
      margin: 15px 0;
      padding: 15px;
      background-color: var(--el-fill-color-light);
      border-radius: 8px;
      
      .summary-text {
        color: var(--el-text-color-secondary);
        font-size: 14px;
        line-height: 1.6;
        margin: 0;
      }
    }
  }

  .blog-comments {
    margin-top: 30px;

    .el-divider {
      .el-icon {
        margin-right: 8px;
        vertical-align: middle;
      }

      span {
        vertical-align: middle;
        font-size: 16px;
        color: var(--el-text-color-primary);
      }
    }

    .comment-input {
      margin: 20px 0;

      .comment-actions {
        margin-top: 12px;
        display: flex;
        justify-content: flex-end;
        gap: 12px;
      }
    }

    .comment-list {
      .comment-item {
        margin-bottom: 20px;

        .comment-main {
          display: flex;
          gap: 12px;

          .comment-content {
            flex: 1;

            .comment-header {
              margin-bottom: 4px;

              .user-info {
                display: flex;
                align-items: center;
                gap: 8px;
              }

              .username {
                font-weight: 500;
                color: var(--el-text-color-primary);
              }

              .reply-to {
                margin: 0 4px;
                color: var(--el-text-color-secondary);
              }

              .time {
                margin-left: 8px;
                font-size: 12px;
                color: var(--el-text-color-secondary);
              }
            }

            .comment-text {
              line-height: 1.6;
              color: var(--el-text-color-regular);
            }

            .comment-actions {
              margin-top: 8px;
            }
          }
        }

        .comment-replies {
          margin-left: 52px;
          margin-top: 12px;

          .reply-item {
            margin-bottom: 12px;

            &:last-child {
              margin-bottom: 0;
            }
          }
        }
      }

      .load-more {
        text-align: center;
        margin-top: 20px;
        padding: 10px 0;
      }
    }
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
  }

  :deep(.el-dialog__body) {
    padding-top: 10px;
  }

  .comment-list {
    .comment-item {
      .comment-replies {
        .load-more-replies {
          text-align: center;
          padding: 8px 0;
          border-top: 1px solid var(--el-border-color-lighter);
        }
      }
    }
  }

  .load-more {
    text-align: center;
    padding: 8px 0;
    border-top: 1px solid var(--el-border-color-lighter);
  }

  .login-tip-wrapper {
    margin: 0 0 20px;
  }

  .login-tip-card {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border: none;
    overflow: hidden;
    
    :deep(.el-card__body) {
      padding: 0;
    }
    
    .login-tip-content {
      display: flex;
      align-items: center;
      padding: 20px;
      
      @media (max-width: 768px) {
        flex-direction: column;
        text-align: center;
      }
      
      .login-tip-image {
        width: 100px;
        height: 100px;
        margin-right: 24px;
        flex-shrink: 0;
        
        @media (max-width: 768px) {
          margin-right: 0;
          margin-bottom: 16px;
        }
      }
      
      .login-tip-info {
        flex: 1;
        
        .login-tip-title {
          font-size: 20px;
          font-weight: 600;
          color: var(--el-color-primary);
          margin: 0 0 8px;
        }
        
        .login-tip-description {
          font-size: 14px;
          color: var(--el-text-color-secondary);
          margin: 0 0 16px;
        }
        
        .login-tip-actions {
          display: flex;
          gap: 12px;
          
          @media (max-width: 768px) {
            justify-content: center;
          }
        }
      }
    }
  }

  .login-tip {
    margin: 0 0 20px;
    
    .login-actions {
      margin-top: 10px;
      text-align: right;
    }
  }
}

.comment-main {
  .user-avatar {
    background-color: var(--el-color-primary);
    color: #fff;
  }

  .comment-content {
    .comment-header {
      .user-info {
        display: flex;
        align-items: center;
        gap: 8px;
        
        .username {
          font-weight: 500;
          color: var(--el-text-color-primary);
        }
        
        .role-tag {
          font-size: 10px;
          padding: 0 6px;
          height: 20px;
          line-height: 18px;
          
          &.el-tag--danger {
            // 超级管理员样式
            background-color: var(--el-color-danger-light-9);
            border-color: var(--el-color-danger-light-7);
            color: var(--el-color-danger);
          }
          
          &.el-tag--warning {
            // 博客管理员样式
            background-color: var(--el-color-warning-light-9);
            border-color: var(--el-color-warning-light-7);
            color: var(--el-color-warning);
          }
        }
        
        .reply-to {
          margin: 0 4px;
          color: var(--el-text-color-secondary);
          font-size: 12px;
        }
        
        .reply-username {
          color: var(--el-color-primary);
          font-weight: 500;
          
          &:hover {
            text-decoration: underline;
            cursor: pointer;
          }
        }
      }
      
      .time {
        margin-left: auto;
        font-size: 12px;
        color: var(--el-text-color-secondary);
      }
    }
  }
}
</style>
