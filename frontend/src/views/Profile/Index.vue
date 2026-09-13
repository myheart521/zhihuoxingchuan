<template>
  <div class="profile-container">
    <!-- 用户信息卡片 -->
    <el-card class="profile-card user-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>{{ t('profile.user.title') }}</span>
        </div>
      </template>
      <ProfileUser />
    </el-card>

    <!-- 主要内容卡片 -->
    <el-card class="profile-card main-card" shadow="hover">
      <template #header>
        <div class="card-header">
          <span>{{ t('profile.info.title') }}</span>
        </div>
      </template>
      <div>
        <el-tabs v-model="activeName" class="profile-tabs">
          <el-tab-pane :label="t('profile.info.basicInfo')" name="basicInfo">
            <BasicInfo />
          </el-tab-pane>
          <el-tab-pane :label="t('profile.info.resetPwd')" name="resetPwd">
            <ResetPwd />
          </el-tab-pane>
          <el-tab-pane :label="t('profile.info.userSocial')" name="userSocial">
            <UserSocial v-model:activeName="activeName" />
          </el-tab-pane>
          <!-- 身份认证标签页 -->
          <el-tab-pane label="身份认证" name="identityVerification">
            <IdentityVerification />
          </el-tab-pane>
          <!-- 申请管理员标签页（单独拆分） -->
          <el-tab-pane label="申请管理员" name="adminApplication">
            <AdminApplication />
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { BasicInfo, ProfileUser, ResetPwd, UserSocial, IdentityVerification } from './components'
import AdminApplication from './components/AdminApplication.vue'

const { t } = useI18n()
defineOptions({ name: 'Profile' })
const activeName = ref('basicInfo')
</script>

<style lang="scss" scoped>
.profile-container {
  display: flex;
  gap: 24px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  
  @media (max-width: 992px) {
    flex-direction: column;
  }
}

.profile-card {
  background-color: #fff;
  border-radius: 8px;
  transition: all 0.3s ease;
  
  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  }
}

.user-card {
  width: 30%;
  max-height: 960px;
  
  @media (max-width: 992px) {
    width: 100%;
  }
}

.main-card {
  flex: 1;
  
  @media (max-width: 992px) {
    width: 100%;
  }
}

.card-header {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-card .el-card__header) {
  padding: 20px !important;
  border-bottom: 1px solid #f0f2f5;
}

:deep(.el-card .el-card__body) {
  padding: 24px !important;
}

.profile-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 25px;
  }
  
  :deep(.el-tabs__nav) {
    border-radius: 4px;
  }
  
  :deep(.el-tabs__item) {
    height: 40px;
    line-height: 40px;
    font-size: 14px;
    
    &.is-active {
      font-weight: 600;
    }
  }
  
  :deep(.el-tabs__content) {
    padding: 20px 0;
    min-height: 400px;
  }
}
</style>
