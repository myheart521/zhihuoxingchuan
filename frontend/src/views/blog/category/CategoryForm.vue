<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="分类名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入分类名称" />
      </el-form-item>
      <el-form-item label="父分类" prop="parentId">
        <el-tree-select
          v-model="defaultSelect"
          :data="categoryTree"
          lazy
          check-strictly
          :load="loadData"
          placeholder="请选择分类，不选择则为顶级分类"
          clearable
          style="width: 100%"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { CategoryApi, CategoryVO } from '@/api/blog/category'

/** 博客分类 表单 */
defineOptions({ name: 'CategoryForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  name: undefined,
  parentId: undefined
})
const formRules = reactive({
  name: [{ required: true, message: '分类名称不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const categoryTree = ref<any[]>([]) // 树形结构

const defaultSelect = ref()

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  if(type == 'create'){
    const res = await getData(0)
    defaultSelect.value = res[0].value
  }else{
    defaultSelect.value=""
  }
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await CategoryApi.getCategory(id)
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
    const data = formData.value as unknown as CategoryVO
    if (formType.value === 'create') {
      formData.value.parentId = defaultSelect.value
      await CategoryApi.createCategory(data)
      message.success(t('common.createSuccess'))
    } else {
      await CategoryApi.updateCategory(data)
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
    name: undefined,
    parentId: 0 // 默认为顶级分类
  }
  formRef.value?.resetFields()
}

const loadData = async (node, resolve) => {
  if (node.isLeaf) return resolve([])
  console.log('loadData:', node, resolve)
  let parentId = node.key
  if(!node.key){
    parentId = 0
  }
  const data = await getData(parentId)
  console.log('loadData:', data)
  resolve(data)
}

const getData = async (parentId: number) => {
  const response = await CategoryApi.getCategoryList({ parentId: parentId })
  const data = response || []
  // 添加顶级分类选项
  // categoryTree.value = convertToTreeData(data)
  return data.map((item) => {
    return {
      value: item.id,
      label: item.name,
      children: []
    }
  })
}

</script>
