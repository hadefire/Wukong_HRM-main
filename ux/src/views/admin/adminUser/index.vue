<template>
  <div class="main">
    <xr-header label="管理员管理">
      <el-button
        slot="ft"
        type="primary"
        @click="handleAdd">添加管理员</el-button>
    </xr-header>

    <div class="main-body">
      <!-- 搜索筛选 -->
      <div class="search-bar">
        <el-input
          v-model="searchForm.keyword"
          class="search-input"
          placeholder="搜索姓名/手机号"
          clearable
          @clear="handleSearch"
          @keyup.enter.native="handleSearch" />
        <el-select
          v-model="searchForm.roleType"
          placeholder="角色类型"
          clearable
          style="width: 150px; margin-left: 10px;"
          @change="handleSearch">
          <el-option label="系统管理员" :value="1" />
          <el-option label="HRM管理员" :value="3" />
        </el-select>
        <el-button type="primary" style="margin-left: 10px;" @click="handleSearch">搜索</el-button>
      </div>

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
        <el-table-column prop="employeeName" label="姓名" min-width="120" show-overflow-tooltip />
        <el-table-column prop="mobile" label="手机号" min-width="150" show-overflow-tooltip />
        <el-table-column prop="deptName" label="部门" min-width="150" show-overflow-tooltip />
        <el-table-column prop="post" label="职位" min-width="120" show-overflow-tooltip />
        <el-table-column label="角色" min-width="200">
          <template slot-scope="scope">
            <el-tag
              v-for="role in scope.row.roles"
              :key="role.roleId"
              :type="getRoleTagType(role.roleType)"
              size="small"
              style="margin-right: 5px;">
              {{ role.roleName }}
            </el-tag>
            <span v-if="!scope.row.roles || scope.row.roles.length === 0">-</span>
          </template>
        </el-table-column>
        <el-table-column label="管理员类型" min-width="180">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isSuperAdmin" type="danger" size="small" style="margin-right: 5px;">超级管理员</el-tag>
            <el-tag v-if="scope.row.isHrmAdmin" type="warning" size="small">HRM管理员</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="primary-text"
              style="padding: 0;"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              type="primary-text"
              style="padding: 0; color: #F56C6C;"
              @click="handleDelete(scope.row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="p-contianer">
        <el-pagination
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size.sync="pagination.limit"
          :total="pagination.total"
          class="p-bar"
          background
          layout="prev, pager, next, sizes, total, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </div>
    </div>

    <!-- 添加/编辑弹窗 -->
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
        <el-form-item v-if="!isEdit" label="选择员工" prop="employeeId">
          <wk-user-select
            v-model="formData.employeeId"
            :props="{
              label: 'employeeName',
              value: 'employeeId',
              dataType: 'hrm'
            }"
            placeholder="请选择员工"
            style="width: 100%;"
            radio
            @change="handleEmployeeChange"
          />
        </el-form-item>
        <el-form-item v-else label="员工">
          <span>{{ formData.employeeName }}</span>
        </el-form-item>
        <el-form-item label="分配角色" prop="roleIds">
          <el-checkbox-group v-model="formData.roleIds">
            <el-checkbox
              v-for="role in availableRoles"
              :key="role.roleId"
              :label="role.roleId">
              {{ role.roleName }}
              <span class="role-type-label">({{ getRoleTypeLabel(role.roleType) }})</span>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  adminUserListAPI,
  adminUserAddAPI,
  adminUserUpdateAPI,
  adminUserDeleteAPI,
  adminUserQueryRolesAPI
} from '@/api/admin/adminUser'
import XrHeader from '@/components/XrHeader'
import WkUserSelect from '@/components/NewCom/WkUserSelect'

export default {
  name: 'AdminUser',

  components: {
    XrHeader,
    WkUserSelect
  },

  data() {
    return {
      loading: false,
      tableData: [],
      tableHeight: document.documentElement.clientHeight - 260, // 表格高度，自适应屏幕
      searchForm: {
        keyword: '',
        roleType: null
      },
      pagination: {
        page: 1,
        limit: 10,
        total: 0
      },
      // 弹窗相关
      dialogVisible: false,
      isEdit: false,
      formData: {
        employeeId: null,
        employeeName: '',
        roleIds: []
      },
      formRules: {
        employeeId: [
          { required: true, message: '请选择员工', trigger: 'change' }
        ],
        roleIds: [
          { required: true, message: '请选择至少一个角色', trigger: 'change' }
        ]
      },
      submitLoading: false,
      // 可用角色
      availableRoles: []
    }
  },

  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑管理员' : '添加管理员'
    }
  },

  created() {
    this.fetchList()
    this.fetchAvailableRoles()
  },

  mounted() {
    // 监听窗口大小变化，动态调整表格高度
    window.onresize = () => {
      this.tableHeight = document.documentElement.clientHeight - 260
    }
  },

  methods: {
    /**
     * 获取管理员列表
     */
    async fetchList() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.page,
          limit: this.pagination.limit,
          ...this.searchForm
        }
        const res = await adminUserListAPI(params)
        if (res.data) {
          this.tableData = res.data.list || []
          this.pagination.total = res.data.totalRow || 0
        }
      } catch (error) {
        console.error('获取管理员列表失败', error)
      } finally {
        this.loading = false
      }
    },

    /**
     * 获取可用角色列表
     */
    async fetchAvailableRoles() {
      try {
        const res = await adminUserQueryRolesAPI()
        this.availableRoles = res.data || []
      } catch (error) {
        console.error('获取角色列表失败', error)
      }
    },

    /**
     * 员工选择变化
     */
    handleEmployeeChange(value, items) {
      // 选择了员工后，保存员工姓名用于显示
      if (items && items.length > 0) {
        this.formData.employeeName = items[0].employeeName || ''
      }
    },

    /**
     * 搜索
     */
    handleSearch() {
      this.pagination.page = 1
      this.fetchList()
    },

    /**
     * 分页大小改变
     */
    handleSizeChange(val) {
      this.pagination.limit = val
      this.fetchList()
    },

    /**
     * 页码改变
     */
    handleCurrentChange(val) {
      this.pagination.page = val
      this.fetchList()
    },

    /**
     * 添加管理员
     */
    handleAdd() {
      this.isEdit = false
      this.formData = {
        employeeId: null,
        employeeName: '',
        roleIds: []
      }
      this.dialogVisible = true
    },

    /**
     * 编辑管理员
     */
    handleEdit(row) {
      this.isEdit = true
      this.formData = {
        employeeId: row.employeeId,
        employeeName: row.employeeName,
        roleIds: row.roles ? row.roles.map(r => r.roleId) : []
      }
      this.dialogVisible = true
    },

    /**
     * 删除管理员
     */
    handleDelete(row) {
      this.$confirm(`确定要移除 "${row.employeeName}" 的管理员身份吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await adminUserDeleteAPI(row.employeeId)
          this.$message.success('移除成功')
          this.fetchList()
        } catch (error) {
          this.$message.error('移除失败')
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
          const params = {
            employeeId: this.formData.employeeId,
            roleIds: this.formData.roleIds
          }

          if (this.isEdit) {
            await adminUserUpdateAPI(params)
            this.$message.success('更新成功')
          } else {
            await adminUserAddAPI(params)
            this.$message.success('添加成功')
          }

          this.dialogVisible = false
          this.fetchList()
        } catch (error) {
          this.$message.error(this.isEdit ? '更新失败' : '添加失败')
        } finally {
          this.submitLoading = false
        }
      })
    },

    /**
     * 弹窗关闭
     */
    handleDialogClose() {
      this.$refs.form && this.$refs.form.resetFields()
    },

    /**
     * 获取角色标签类型
     */
    getRoleTagType(roleType) {
      const typeMap = {
        1: 'danger',
        3: 'warning'
      }
      return typeMap[roleType] || ''
    },

    /**
     * 获取角色类型标签
     */
    getRoleTypeLabel(roleType) {
      const labelMap = {
        1: '系统管理',
        3: '人事管理'
      }
      return labelMap[roleType] || '其他'
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

.search-bar {
  display: flex;
  align-items: center;
  margin-top: 0;
  margin-bottom: 20px;
}

.role-type-label {
  color: #909399;
  font-size: 12px;
}

.el-button--small {
  padding: 0;
}
</style>
