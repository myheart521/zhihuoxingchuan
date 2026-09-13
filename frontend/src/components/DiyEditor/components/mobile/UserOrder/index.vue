<template>
  <el-image src="https://example.invalid/resource" />
</template>
<script setup lang="ts">
import { UserOrderProperty } from './config'

/** 用户订单 */
defineOptions({ name: 'UserOrder' })
// 定义属性
defineProps<{ property: UserOrderProperty }>()
</script>

<style scoped lang="scss"></style>
