<template>
  <div class="main">
    <xr-header label="恢复密码">
      <span slot="ft" class="tips">选择员工后点击"恢复密码"，将其密码重置为默认密码 123456</span>
    </xr-header>

    <div class="main-body">
      <el-card shadow="never">
        <div class="form-container">
          <el-form
            ref="form"
            :model="formData"
            :rules="formRules"
            label-width="100px">
            <el-form-item label="选择员工" prop="employeeId">
              <wk-user-select
                v-model="formData.employeeId"
                :props="{
                  label: 'employeeName',
                  value: 'employeeId',
                  dataType: 'hrm'
                }"
                placeholder="请选择要恢复密码的员工"
                style="width: 400px;"
                radio
                @change="handleEmployeeChange"
              />
            </el-form-item>
            <el-form-item v-if="selectedEmployee" label="员工信息">
              <div class="employee-detail-card">
                <div class="detail-row">
                  <div class="detail-item">
                    <span class="detail-label">姓名</span>
                    <span class="detail-value highlight">{{ selectedEmployee.employeeName || selectedEmployee.realname || '-' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">工号</span>
                    <span class="detail-value">{{ selectedEmployee.jobNumber || '-' }}</span>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-item">
                    <span class="detail-label">手机号</span>
                    <span class="detail-value">{{ selectedEmployee.mobile || '-' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">邮箱</span>
                    <span class="detail-value">{{ selectedEmployee.email || '-' }}</span>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-item">
                    <span class="detail-label">部门</span>
                    <span class="detail-value">{{ selectedEmployee.deptName || '-' }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">职位</span>
                    <span class="detail-value">{{ selectedEmployee.post || '-' }}</span>
                  </div>
                </div>
                <div class="detail-row">
                  <div class="detail-item">
                    <span class="detail-label">入职日期</span>
                    <span class="detail-value">{{ formatDate(selectedEmployee.entryTime) }}</span>
                  </div>
                  <div class="detail-item">
                    <span class="detail-label">员工状态</span>
                    <span class="detail-value">
                      <el-tag :type="getStatusType(selectedEmployee.status)" size="small">
                        {{ getStatusText(selectedEmployee.status) }}
                      </el-tag>
                    </span>
                  </div>
                </div>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                :disabled="!formData.employeeId"
                @click="handleReset">
                恢复密码
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-card>

      <!-- 操作结果 -->
      <el-card v-if="resetResult" shadow="never" style="margin-top: 20px;">
        <div slot="header">
          <span>操作结果</span>
        </div>
        <el-alert
          :title="resetResult.message"
          :type="resetResult.success ? 'success' : 'error'"
          :closable="false"
          show-icon>
          <template v-if="resetResult.success">
            <p style="margin-top: 10px;">
              员工 <strong>{{ resetResult.employeeName }}</strong> 的密码已重置为：<strong style="color: #E6A23C;">123456</strong>
            </p>
            <p style="color: #909399; font-size: 12px;">请通知该员工使用新密码登录，并建议尽快修改密码。</p>
          </template>
        </el-alert>
      </el-card>
    </div>
  </div>
</template>

<script>
import { resetPasswordAPI } from '@/api/login'
import XrHeader from '@/components/XrHeader'
import WkUserSelect from '@/components/NewCom/WkUserSelect'

export default {
  name: 'ResetPassword',

  components: {
    XrHeader,
    WkUserSelect
  },

  data() {
    return {
      loading: false,
      formData: {
        employeeId: null
      },
      formRules: {
        employeeId: [
          { required: true, message: '请选择员工', trigger: 'change' }
        ]
      },
      selectedEmployee: null,
      resetResult: null
    }
  },

  methods: {
    handleEmployeeChange(value, items) {
      if (items && items.length > 0) {
        this.selectedEmployee = items[0]
      } else {
        this.selectedEmployee = null
      }
      this.resetResult = null
    },

    handleReset() {
      this.$refs.form.validate(valid => {
        if (!valid) return

        const employeeName = this.selectedEmployee?.employeeName || this.selectedEmployee?.realname || '未知'

        this.$confirm(
          `确定要将员工「${employeeName}」的密码恢复为默认密码 123456 吗？`,
          '确认恢复密码',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          this.doReset(employeeName)
        }).catch(() => {})
      })
    },

    async doReset(employeeName) {
      this.loading = true
      try {
        await resetPasswordAPI(this.formData.employeeId)
        this.resetResult = {
          success: true,
          message: '密码恢复成功',
          employeeName: employeeName
        }
        this.$message.success('密码恢复成功')
      } catch (error) {
        this.resetResult = {
          success: false,
          message: '密码恢复失败：' + (error.msg || error.message || '未知错误')
        }
        this.$message.error('密码恢复失败')
      } finally {
        this.loading = false
      }
    },

    /**
     * 获取状态标签类型
     */
    getStatusType(status) {
      const typeMap = {
        1: 'success', // 正式
        2: 'info',    // 试用
        3: 'warning', // 待离职
        4: 'danger',  // 已离职
        5: 'info'     // 待入职
      }
      return typeMap[status] || 'info'
    },

    /**
     * 获取状态文字
     */
    getStatusText(status) {
      const textMap = {
        1: '正式',
        2: '试用',
        3: '待离职',
        4: '已离职',
        5: '待入职'
      }
      return textMap[status] || '未知'
    },

    /**
     * 格式化日期
     */
    formatDate(dateStr) {
      if (!dateStr) return '-'
      // 处理 ISO 格式日期
      const date = new Date(dateStr)
      if (isNaN(date.getTime())) return dateStr
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../styles/index.scss";

.tips {
  color: #909399;
  font-size: 13px;
}

.main-body {
  margin-top: #{$--interval-base * 2};
}

.form-container {
  padding: 20px;
}

.employee-detail-card {
  max-width: 700px;
  padding: 20px;
  background-color: #f8fafc;
  border: 1px solid #e4e7ed;
  border-radius: 8px;

  .detail-row {
    display: flex;
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }
  }

  .detail-item {
    flex: 1;
    display: flex;
    align-items: center;
  }

  .detail-label {
    width: 80px;
    font-size: 14px;
    color: #909399;
    flex-shrink: 0;
  }

  .detail-value {
    flex: 1;
    font-size: 14px;
    color: #606266;

    &.highlight {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }
}
</style>
