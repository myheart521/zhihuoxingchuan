<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="图片URL" prop="image">
        <UploadImg v-model="formData.image" />
      </el-form-item>
      <el-form-item label="标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入标题" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <Editor v-model="formData.description" height="150px" />
      </el-form-item>
      <el-form-item label="链接地址" prop="link">
        <el-input v-model="formData.link" placeholder="请输入链接地址" />
      </el-form-item>
      <el-form-item label="排序号" prop="sort">
        <el-input v-model="formData.sort" placeholder="请输入排序号" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="formData.status" placeholder="请选择状态">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.BLOG_USER_LOCK)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { CarouselApi, CarouselVO } from '@/api/blog/carousel'

/** 轮播图 表单 */
defineOptions({ name: 'CarouselForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  image: undefined,
  title: undefined,
  description: undefined,
  link: undefined,
  sort: undefined,
  status: undefined
})
const formRules = reactive({
  image: [{ required: true, message: '图片URL不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态：0-禁用 1-启用不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await CarouselApi.getCarousel(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as CarouselVO
    if (formType.value === 'create') {
      await CarouselApi.createCarousel(data)
      message.success(t('common.createSuccess'))
    } else {
      await CarouselApi.updateCarousel(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    image: undefined,
    title: undefined,
    description: undefined,
    link: undefined,
    sort: undefined,
    status: undefined
  }
  formRef.value?.resetFields()
}
</script>
