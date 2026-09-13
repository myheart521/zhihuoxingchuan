<script lang="ts" setup>
import { PropType } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { i18nChangeLanguage, IDomEditor, IEditorConfig } from '@wangeditor/editor'
import { propTypes } from '@/utils/propTypes'
import { isNumber } from '@/utils/is'
import { ElMessage } from 'element-plus'
import { useLocaleStore } from '@/store/modules/locale'
import { getRefreshToken, getTenantId } from '@/utils/auth'
import { getUploadUrl } from '@/components/UploadFile/src/useUpload'

defineOptions({ name: 'Editor' })

type InsertFnType = (url: string, alt: string, href: string) => void

const localeStore = useLocaleStore()

const currentLocale = computed(() => localeStore.getCurrentLocale)

i18nChangeLanguage(unref(currentLocale).lang)

const props = defineProps({
  editorId: propTypes.string.def('wangeEditor-1'),
  height: propTypes.oneOfType([Number, String]).def('500px'),
  editorConfig: {
    type: Object as PropType<Partial<IEditorConfig>>,
    default: () => undefined
  },
  readonly: propTypes.bool.def(false),
  modelValue: propTypes.string.def('')
})

const emit = defineEmits(['change', 'update:modelValue'])

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef<IDomEditor>()

const valueHtml = ref('')

watch(
  () => props.modelValue,
  (val: string) => {
    if (val === unref(valueHtml)) return
    valueHtml.value = val
  },
  {
    immediate: true
  }
)

// 监听
watch(
  () => valueHtml.value,
  (val: string) => {
    emit('update:modelValue', val)
  }
)

const handleCreated = (editor: IDomEditor) => {
  editorRef.value = editor
}

// 编辑器配置
const editorConfig = computed((): IEditorConfig => {
  return {
    placeholder: '请输入内容...',
    readOnly: props.readonly,
    customAlert: (s: string, t: string) => {
      switch (t) {
        case 'success':
          ElMessage.success(s)
          break
        case 'info':
          ElMessage.info(s)
          break
        case 'warning':
          ElMessage.warning(s)
          break
        case 'error':
          ElMessage.error(s)
          break
        default:
          ElMessage.info(s)
          break
      }
    },
    autoFocus: false,
    scroll: true,
    MENU_CONF: {
      uploadImage: {
        server: getUploadUrl(),
        maxFileSize: 5 * 1024 * 1024,
        maxNumberOfFiles: 10,
        allowedFileTypes: ['image/*'],
        headers: {
          Accept: '*',
          Authorization: '' + getRefreshToken(),
          'tenant-id': getTenantId()
        },
        timeout: 15 * 1000,
        fieldName: 'file',
        onBeforeUpload(file: File) {
          return file
        },
        onProgress(progress: number) {
          console.log('上传进度:', progress)
        },
        onSuccess(file: File, res: any) {
          console.log('上传成功:', file, res)
        },
        onFailed(file: File, res: any) {
          ElMessage.error(res.message || '上传失败')
        },
        onError(file: File, err: any, res: any) {
          ElMessage.error(err.message || '上传出错')
        },
        // 自定义插入图片
        customInsert(res: any, insertFn: InsertFnType) {
          if (res.code === 0 && res.data) {
            insertFn(res.data, '', res.data)
          } else {
            ElMessage.error('插入图片失败')
          }
        }
      },
      uploadVideo: {
        server: getUploadUrl(),
        maxFileSize: 10 * 1024 * 1024,
        maxNumberOfFiles: 10,
        allowedFileTypes: ['video/*'],
        headers: {
          Accept: '*',
          Authorization: '' + getRefreshToken(),
          'tenant-id': getTenantId()
        },
        timeout: 15 * 1000,
        fieldName: 'file',
        onBeforeUpload(file: File) {
          return file
        },
        onProgress(progress: number) {
          console.log('progress', progress)
        },
        onSuccess(file: File, res: any) {
          console.log('onSuccess', file, res)
        },
        onFailed(file: File, res: any) {
          alert(res.message)
          console.log('onFailed', file, res)
        },
        onError(file: File, err: any, res: any) {
          alert(err.message)
          console.error('onError', file, err, res)
        },
        customInsert(res: any, insertFn: InsertFnType) {
          insertFn(res.data, 'mp4', res.data)
        }
      }
    },
    uploadImgShowBase64: true
  }
})

const editorStyle = computed(() => {
  return {
    height: isNumber(props.height) ? `${props.height}px` : props.height
  }
})

// 回调函数
const handleChange = (editor: IDomEditor) => {
  emit('change', editor)
}

// 组件销毁时，及时销毁编辑器
onBeforeUnmount(() => {
  const editor = unref(editorRef.value)

  // 销毁，并移除 editor
  editor?.destroy()
})

const getEditorRef = async (): Promise<IDomEditor> => {
  await nextTick()
  return unref(editorRef.value) as IDomEditor
}

defineExpose({
  getEditorRef
})
</script>

<template>
  <div class="border-1 border-solid border-[var(--tags-view-border-color)] z-10">
    <!-- 工具栏 -->
    <Toolbar
      :editor="editorRef"
      :editorId="editorId"
      class="border-0 b-b-1 border-solid border-[var(--tags-view-border-color)]"
    />
    <!-- 编辑器 -->
    <Editor
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :editorId="editorId"
      :style="editorStyle"
      @on-change="handleChange"
      @on-created="handleCreated"
    />
  </div>
</template>

<style src="@wangeditor/editor/dist/css/style.css"></style>
