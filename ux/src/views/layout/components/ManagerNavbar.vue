<template>
  <div class="navbar">
    <flexbox style="width: auto;">
      <img
        :key="logo"
        v-src="logo"
        class="logo"
        @click="enterMainPage">
      <div class="nav-title">
        系统设置
      </div>
    </flexbox>
    <flexbox style="width: auto;" align="center">
      <el-button
        type="primary"
        @click="enterHome">返回首页</el-button>

      <el-dropdown
        v-if="userInfo"
        trigger="click"
        style="margin-left: 16px;"
        @command="handleUserCommand">
        <xr-avatar
          style="cursor: pointer;"
          disabled
          :name="$getUserName(userInfo)"
          :size="32"
          :src="$getUserImg(userInfo)" />
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="changePassword">
            <i class="wk wk-lock" style="margin-right: 8px;" />修改密码
          </el-dropdown-item>
          <el-dropdown-item command="logout" divided>
            <i class="wk wk-logout" style="margin-right: 8px;" />退出登录
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </flexbox>

    <!-- 修改密码弹窗 -->
    <el-dialog
      title="修改密码"
      :visible.sync="changePasswordDialogVisible"
      width="400px"
      @close="handlePasswordDialogClose">
      <el-form
        ref="passwordForm"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px">
        <el-form-item label="原密码" prop="oldPassword">
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入原密码"
            show-password />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码（至少6位）"
            show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="changePasswordDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="passwordLoading" @click="handleChangePassword">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { Loading } from 'element-ui'
import { mapGetters } from 'vuex'
import { changePasswordAPI } from '@/api/login'

export default {
  components: {},
  props: {
    navIndex: String
  },
  data() {
    return {
      // 修改密码相关
      changePasswordDialogVisible: false,
      passwordLoading: false,
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认新密码', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['logo', 'crm', 'userInfo'])
  },
  mounted() {},
  methods: {
    enterHome() {
      this.$router.replace({
        path: '/'
      })
    },
    enterLogin() {
      this.$confirm('退出登录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          var loading = Loading.service({
            target: document.getElementById('#app')
          })
          this.$store
            .dispatch('LogOut')
            .then(() => {
              loading.close()
              location.reload()
            })
            .catch(() => {
              loading.close()
              location.reload()
            })
        })
        .catch(() => {})
    },

    /**
     * 有客户权限点击logo 进入仪表盘
     */
    enterMainPage() {
      this.$router.push('/')
    },

    /**
     * 用户下拉菜单命令
     */
    handleUserCommand(command) {
      if (command === 'changePassword') {
        this.changePasswordDialogVisible = true
      } else if (command === 'logout') {
        this.enterLogin()
      }
    },

    /**
     * 修改密码提交
     */
    handleChangePassword() {
      this.$refs.passwordForm.validate(async(valid) => {
        if (!valid) return

        if (this.passwordForm.newPassword !== this.passwordForm.confirmPassword) {
          this.$message.error('两次输入的新密码不一致')
          return
        }

        this.passwordLoading = true
        try {
          const employeeId = this.userInfo.employeeId || this.userInfo.userId
          await changePasswordAPI({
            employeeId: employeeId,
            oldPassword: this.passwordForm.oldPassword,
            newPassword: this.passwordForm.newPassword
          })
          this.$message.success('密码修改成功，请重新登录')
          this.changePasswordDialogVisible = false
          // 修改成功后退出登录
          setTimeout(() => {
            this.$store.dispatch('LogOut').then(() => {
              location.reload()
            })
          }, 1500)
        } catch (error) {
          this.$message.error(error.msg || '密码修改失败')
        } finally {
          this.passwordLoading = false
        }
      })
    },

    /**
     * 关闭修改密码弹窗
     */
    handlePasswordDialogClose() {
      this.$refs.passwordForm && this.$refs.passwordForm.resetFields()
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 56px;
  padding: 0 12px;
  background-color: white;

  .logo {
    display: block;
    flex-shrink: 0;
    width: 24px;
    height: 24px;
    margin-left: 48px;
    cursor: pointer;
    background-color: white;
  }

  .nav-title {
    margin-left: 8px;
    font-size: 18px;
    color: $--color-n600;
  }
}

</style>

