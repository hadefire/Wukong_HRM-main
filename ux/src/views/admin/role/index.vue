<template>
  <div class="main">
    <xr-header label="角色管理">
      <el-button
        slot="ft"
        type="primary"
        @click="handleAdd">新建角色</el-button>
    </xr-header>

    <div class="main-body">
      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        :class="WKConfig.tableStyle.class"
        :stripe="WKConfig.tableStyle.stripe"
        :height="tableHeight"
        class="main-table"
        highlight-current-row
        style="width: 100%;">
        <el-table-column prop="roleName" label="角色名称" min-width="150" show-overflow-tooltip />
        <el-table-column label="角色类型" min-width="120">
          <template slot-scope="scope">
            <el-tag :type="getRoleTypeTagType(scope.row.roleType)" size="small">
              {{ getRoleTypeLabel(scope.row.roleType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="数据权限" min-width="150">
          <template slot-scope="scope">
            {{ getDataTypeLabel(scope.row.dataType) }}
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="primary-text"
              style="padding: 0;"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              type="primary-text"
              style="padding: 0;"
              @click="handlePermission(scope.row)">权限配置</el-button>
            <el-button
              type="primary-text"
              style="padding: 0; color: #F56C6C;"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑角色弹窗 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
      @close="handleDialogClose">
      <el-form
        ref="form"
        :model="formData"
        :rules="formRules"
        label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色类型" prop="roleType">
          <el-select v-model="formData.roleType" placeholder="请选择角色类型" style="width: 100%;">
            <el-option label="系统管理员" :value="1" />
            <el-option label="人事管理员" :value="3" />
            <el-option label="自定义角色" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" prop="dataType">
          <el-select v-model="formData.dataType" placeholder="请选择数据权限" style="width: 100%;">
            <el-option label="本人" :value="1" />
            <el-option label="本人及下属" :value="2" />
            <el-option label="本部门" :value="3" />
            <el-option label="本部门及下属部门" :value="4" />
            <el-option label="全部" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" placeholder="请输入备注" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 权限配置弹窗 -->
    <el-dialog
      title="权限配置"
      :visible.sync="permissionDialogVisible"
      width="600px"
      @close="handlePermissionDialogClose">
      <div v-if="currentRole" class="permission-header">
        <span>为角色「{{ currentRole.roleName }}」配置菜单权限</span>
      </div>
      <div class="permission-tree-container">
        <el-tree
          ref="menuTree"
          v-loading="menuLoading"
          :data="menuTreeData"
          :props="menuTreeProps"
          :default-checked-keys="checkedMenuIds"
          node-key="menuId"
          show-checkbox
          default-expand-all
          @check-change="handleMenuCheck" />
      </div>
      <div slot="footer">
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="permissionLoading" @click="handlePermissionSubmit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  adminRoleListAPI,
  adminRoleQueryByIdAPI,
  adminRoleAddAPI,
  adminRoleUpdateAPI,
  adminRoleDeleteAPI,
  adminRoleSetStatusAPI
} from '@/api/admin/role'
import { adminMenuTreeAPI } from '@/api/admin/menu'
import XrHeader from '@/components/XrHeader'

export default {
  name: 'RoleManage',

  components: {
    XrHeader
  },

  data() {
    return {
      loading: false,
      tableData: [],
      tableHeight: document.documentElement.clientHeight - 220, // 表格高度，自适应屏幕
      // 弹窗相关
      dialogVisible: false,
      isEdit: false,
      formData: {
        roleId: null,
        roleName: '',
        roleType: 0,
        dataType: 5,
        remark: ''
      },
      formRules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ],
        roleType: [
          { required: true, message: '请选择角色类型', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '请选择数据权限', trigger: 'change' }
        ]
      },
      submitLoading: false,
      // 权限配置相关
      permissionDialogVisible: false,
      currentRole: null,
      menuTreeData: [],
      menuTreeProps: {
        children: 'childMenu',
        label: 'menuName'
      },
      checkedMenuIds: [],
      menuLoading: false,
      permissionLoading: false
    }
  },

  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑角色' : '新建角色'
    }
  },

  created() {
    this.fetchList()
  },

  mounted() {
    // 监听窗口大小变化，动态调整表格高度
    window.onresize = () => {
      this.tableHeight = document.documentElement.clientHeight - 220
    }
  },

  methods: {
    /**
     * 获取角色列表
     */
    async fetchList() {
      this.loading = true
      try {
        const res = await adminRoleListAPI()
        this.tableData = res.data || []
      } catch (error) {
        console.error('获取角色列表失败', error)
      } finally {
        this.loading = false
      }
    },

    /**
     * 获取角色类型标签
     */
    getRoleTypeLabel(type) {
      const map = {
        0: '自定义角色',
        1: '系统管理员',
        3: '人事管理员'
      }
      return map[type] || '其他'
    },

    /**
     * 获取角色类型标签颜色
     */
    getRoleTypeTagType(type) {
      const map = {
        1: 'danger',
        3: 'warning',
        0: ''
      }
      return map[type] || 'info'
    },

    /**
     * 获取数据权限标签
     */
    getDataTypeLabel(type) {
      const map = {
        1: '本人',
        2: '本人及下属',
        3: '本部门',
        4: '本部门及下属部门',
        5: '全部'
      }
      return map[type] || '未知'
    },

    /**
     * 状态切换
     */
    async handleStatusChange(row) {
      try {
        await adminRoleSetStatusAPI(row.roleId, row.status)
        this.$message.success('操作成功')
      } catch (error) {
        // 恢复状态
        row.status = row.status === 1 ? 0 : 1
        this.$message.error('操作失败')
      }
    },

    /**
     * 新建角色
     */
    handleAdd() {
      this.isEdit = false
      this.formData = {
        roleId: null,
        roleName: '',
        roleType: 0,
        dataType: 5,
        remark: ''
      }
      this.dialogVisible = true
    },

    /**
     * 编辑角色
     */
    handleEdit(row) {
      this.isEdit = true
      this.formData = {
        roleId: row.roleId,
        roleName: row.roleName,
        roleType: row.roleType,
        dataType: row.dataType,
        remark: row.remark
      }
      this.dialogVisible = true
    },

    /**
     * 删除角色
     */
    handleDelete(row) {
      this.$confirm(`确定要删除角色「${row.roleName}」吗？删除后不可恢复。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await adminRoleDeleteAPI(row.roleId)
          this.$message.success('删除成功')
          this.fetchList()
        } catch (error) {
          this.$message.error(error.msg || '删除失败')
        }
      }).catch(() => {})
    },

    /**
     * 提交表单
     */
    handleSubmit() {
      this.$refs.form.validate(async(valid) => {
        if (!valid) return

        this.submitLoading = true
        try {
          if (this.isEdit) {
            await adminRoleUpdateAPI(this.formData)
            this.$message.success('更新成功')
          } else {
            await adminRoleAddAPI(this.formData)
            this.$message.success('添加成功')
          }
          this.dialogVisible = false
          this.fetchList()
        } catch (error) {
          this.$message.error(error.msg || (this.isEdit ? '更新失败' : '添加失败'))
        } finally {
          this.submitLoading = false
        }
      })
    },

    /**
     * 关闭弹窗
     */
    handleDialogClose() {
      this.$refs.form && this.$refs.form.resetFields()
    },

    /**
     * 权限配置
     */
    async handlePermission(row) {
      this.currentRole = row
      this.permissionDialogVisible = true
      this.menuLoading = true

      try {
        // 获取菜单树
        const menuRes = await adminMenuTreeAPI()
        this.menuTreeData = menuRes.data || []

        // 获取角色已有的菜单权限
        const roleRes = await adminRoleQueryByIdAPI(row.roleId)
        this.checkedMenuIds = roleRes.data?.menuIds || []
      } catch (error) {
        console.error('获取权限数据失败', error)
        this.$message.error('获取权限数据失败')
      } finally {
        this.menuLoading = false
      }
    },

    /**
     * 菜单选中变化
     */
    handleMenuCheck() {
      // 可以在这里做一些处理
    },

    /**
     * 保存权限配置
     */
    async handlePermissionSubmit() {
      if (!this.currentRole) return

      this.permissionLoading = true
      try {
        // 获取选中的菜单ID（包括半选中的父节点）
        const checkedKeys = this.$refs.menuTree.getCheckedKeys()
        const halfCheckedKeys = this.$refs.menuTree.getHalfCheckedKeys()
        const menuIds = [...checkedKeys, ...halfCheckedKeys]

        await adminRoleUpdateAPI({
          roleId: this.currentRole.roleId,
          menuIds: menuIds
        })
        this.$message.success('权限配置保存成功')
        this.permissionDialogVisible = false
      } catch (error) {
        this.$message.error(error.msg || '保存失败')
      } finally {
        this.permissionLoading = false
      }
    },

    /**
     * 关闭权限配置弹窗
     */
    handlePermissionDialogClose() {
      this.currentRole = null
      this.menuTreeData = []
      this.checkedMenuIds = []
    }
  }
}
</script>

<style lang="scss" scoped>
@import "../styles/index.scss";
@import "../styles/table.scss";

.main-body {
  margin-top: #{$--interval-base * 2};
}

.permission-header {
  margin-bottom: 16px;
  color: #606266;
  font-size: 14px;
}

.permission-tree-container {
  max-height: 400px;
  overflow: auto;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;
}
</style>
