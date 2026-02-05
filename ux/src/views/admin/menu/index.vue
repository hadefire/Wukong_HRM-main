<template>
  <div class="main">
    <xr-header label="菜单管理">
      <el-button
        slot="ft"
        type="primary"
        @click="handleAdd(null)">新建菜单</el-button>
    </xr-header>

    <div class="main-body">
      <!-- 菜单树表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        :class="WKConfig.tableStyle.class"
        :stripe="WKConfig.tableStyle.stripe"
        :height="tableHeight"
        row-key="menuId"
        :tree-props="{ children: 'childMenu', hasChildren: 'hasChildren' }"
        class="main-table"
        style="width: 100%;">
        <el-table-column prop="menuName" label="菜单名称" min-width="200" />
        <el-table-column prop="realm" label="权限标识" min-width="150" show-overflow-tooltip />
        <el-table-column label="菜单类型" min-width="100">
          <template slot-scope="scope">
            <el-tag :type="getMenuTypeTagType(scope.row.menuType)" size="small">
              {{ getMenuTypeLabel(scope.row.menuType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="状态" width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button
              type="primary-text"
              style="padding: 0;"
              @click="handleAdd(scope.row)">添加子菜单</el-button>
            <el-button
              type="primary-text"
              style="padding: 0;"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              type="primary-text"
              style="padding: 0; color: #F56C6C;"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑菜单弹窗 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="550px"
      @close="handleDialogClose">
      <el-form
        ref="form"
        :model="formData"
        :rules="formRules"
        label-width="100px">
        <el-form-item label="上级菜单">
          <el-cascader
            v-model="formData.parentIdPath"
            :options="menuOptions"
            :props="{ checkStrictly: true, value: 'menuId', label: 'menuName', children: 'childMenu', emitPath: true }"
            placeholder="选择上级菜单（不选则为顶级菜单）"
            clearable
            style="width: 100%;"
            @change="handleParentChange" />
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="formData.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="权限标识" prop="realm">
          <el-input v-model="formData.realm" placeholder="如：system、hrm、manage" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="formData.menuType">
            <el-radio :label="1">目录</el-radio>
            <el-radio :label="2">菜单</el-radio>
            <el-radio :label="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sort" :min="0" :max="9999" />
        </el-form-item>
        <el-form-item label="权限URL">
          <el-input v-model="formData.realmUrl" placeholder="请输入权限URL" />
        </el-form-item>
        <el-form-item label="所属模块">
          <el-input v-model="formData.realmModule" placeholder="如：hrm、manage" />
        </el-form-item>
        <el-form-item label="菜单说明">
          <el-input v-model="formData.remarks" type="textarea" placeholder="请输入菜单说明" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  adminMenuTreeAPI,
  adminMenuAddAPI,
  adminMenuUpdateAPI,
  adminMenuDeleteAPI,
  adminMenuSetStatusAPI
} from '@/api/admin/menu'
import XrHeader from '@/components/XrHeader'

export default {
  name: 'MenuManage',

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
        menuId: null,
        parentId: 0,
        parentIdPath: [],
        menuName: '',
        realm: '',
        realmUrl: '',
        realmModule: '',
        menuType: 2,
        sort: 0,
        remarks: ''
      },
      formRules: {
        menuName: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' }
        ],
        menuType: [
          { required: true, message: '请选择菜单类型', trigger: 'change' }
        ]
      },
      submitLoading: false,
      // 菜单选项（用于级联选择器）
      menuOptions: []
    }
  },

  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑菜单' : '新建菜单'
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
     * 获取菜单列表
     */
    async fetchList() {
      this.loading = true
      try {
        const res = await adminMenuTreeAPI()
        this.tableData = res.data || []
        // 更新菜单选项
        this.menuOptions = this.buildMenuOptions(res.data || [])
      } catch (error) {
        console.error('获取菜单列表失败', error)
      } finally {
        this.loading = false
      }
    },

    /**
     * 构建菜单选项
     */
    buildMenuOptions(menus) {
      return menus.map(menu => ({
        menuId: menu.menuId,
        menuName: menu.menuName,
        childMenu: menu.childMenu ? this.buildMenuOptions(menu.childMenu) : []
      }))
    },

    /**
     * 获取菜单类型标签
     */
    getMenuTypeLabel(type) {
      const map = {
        1: '目录',
        2: '菜单',
        3: '按钮',
        4: '特殊'
      }
      return map[type] || '未知'
    },

    /**
     * 获取菜单类型标签颜色
     */
    getMenuTypeTagType(type) {
      const map = {
        1: '',
        2: 'success',
        3: 'warning',
        4: 'info'
      }
      return map[type] || ''
    },

    /**
     * 状态切换
     */
    async handleStatusChange(row) {
      try {
        await adminMenuSetStatusAPI(row.menuId, row.status)
        this.$message.success('操作成功')
      } catch (error) {
        // 恢复状态
        row.status = row.status === 1 ? 0 : 1
        this.$message.error('操作失败')
      }
    },

    /**
     * 新建菜单
     */
    handleAdd(parentRow) {
      this.isEdit = false
      this.formData = {
        menuId: null,
        parentId: parentRow ? parentRow.menuId : 0,
        parentIdPath: parentRow ? this.getParentPath(parentRow.menuId) : [],
        menuName: '',
        realm: '',
        realmUrl: '',
        realmModule: '',
        menuType: 2,
        sort: 0,
        remarks: ''
      }
      this.dialogVisible = true
    },

    /**
     * 获取父级路径
     */
    getParentPath(menuId, menus = this.tableData, path = []) {
      for (const menu of menus) {
        if (menu.menuId === menuId) {
          return [...path, menuId]
        }
        if (menu.childMenu && menu.childMenu.length > 0) {
          const result = this.getParentPath(menuId, menu.childMenu, [...path, menu.menuId])
          if (result.length > 0) {
            return result
          }
        }
      }
      return []
    },

    /**
     * 编辑菜单
     */
    handleEdit(row) {
      this.isEdit = true
      this.formData = {
        menuId: row.menuId,
        parentId: row.parentId || 0,
        parentIdPath: row.parentId ? this.getParentPath(row.parentId) : [],
        menuName: row.menuName,
        realm: row.realm,
        realmUrl: row.realmUrl,
        realmModule: row.realmModule,
        menuType: row.menuType,
        sort: row.sort,
        remarks: row.remarks
      }
      this.dialogVisible = true
    },

    /**
     * 上级菜单变化
     */
    handleParentChange(value) {
      if (value && value.length > 0) {
        this.formData.parentId = value[value.length - 1]
      } else {
        this.formData.parentId = 0
      }
    },

    /**
     * 删除菜单
     */
    handleDelete(row) {
      this.$confirm(`确定要删除菜单「${row.menuName}」吗？删除后不可恢复。`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          await adminMenuDeleteAPI(row.menuId)
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
          const submitData = { ...this.formData }
          delete submitData.parentIdPath // 不需要提交此字段

          if (this.isEdit) {
            await adminMenuUpdateAPI(submitData)
            this.$message.success('更新成功')
          } else {
            await adminMenuAddAPI(submitData)
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
</style>
