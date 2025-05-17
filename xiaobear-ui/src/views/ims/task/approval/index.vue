<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色ID" prop="roleId">
        <el-input
          v-model="queryParams.roleId"
          placeholder="请输入角色ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="院系ID" prop="deptId">
        <el-input
          v-model="queryParams.deptId"
          placeholder="请输入院系ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="子任务ID" prop="childId">
        <el-input
          v-model="queryParams.childId"
          placeholder="请输入子任务ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程配置ID" prop="processId">
        <el-input
          v-model="queryParams.processId"
          placeholder="请输入流程配置ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务状态" prop="taskStatus">
        <el-select v-model="queryParams.taskStatus" placeholder="请选择任务状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="审批状态" prop="approvalStatus">
        <el-select v-model="queryParams.approvalStatus" placeholder="请选择审批状态" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="审批结论" prop="approvalConclusion">
        <el-input
          v-model="queryParams.approvalConclusion"
          placeholder="请输入审批结论"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审批意见" prop="approvalOpinions">
        <el-input
          v-model="queryParams.approvalOpinions"
          placeholder="请输入审批意见"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建院系" prop="createDept">
        <el-input
          v-model="queryParams.createDept"
          placeholder="请输入创建院系"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新院系" prop="updateDept">
        <el-input
          v-model="queryParams.updateDept"
          placeholder="请输入更新院系"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        院系"cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="approvalList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="审批ID" align="center" prop="approvalId" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="角色ID" align="center" prop="roleId" />
      <el-table-column label="院系ID" align="center" prop="deptId" />
      <el-table-column label="子任务ID" align="center" prop="childId" />
      <el-table-column label="流程配置ID" align="center" prop="processId" />
      <el-table-column label="任务状态" align="center" prop="taskStatus" />
      <el-table-column label="审批状态" align="center" prop="approvalStatus" />
      <el-table-column label="审批结论" align="center" prop="approvalConclusion" />
      <el-table-column label="审批意见" align="center" prop="approvalOpinions" />
      <el-table-column label="创建院系" align="center" prop="createDept" />
      <el-table-column label="更新院系" align="center" prop="updateDept" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改审批明细信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="角色ID" prop="roleId">
          <el-input v-model="form.roleId" placeholder="请输入角色ID" />
        </el-form-item>
        <el-form-item label="院系ID" prop="deptId">
          <el-input v-model="form.deptId" placeholder="请输入院系ID" />
        </el-form-item>
        <el-form-item label="子任务ID" prop="childId">
          <el-input v-model="form.childId" placeholder="请输入子任务ID" />
        </el-form-item>
        <el-form-item label="流程配置ID" prop="processId">
          <el-input v-model="form.processId" placeholder="请输入流程配置ID" />
        </el-form-item>
        <el-form-item label="任务状态">
          <el-radio-group v-model="form.taskStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批状态">
          <el-radio-group v-model="form.approvalStatus">
            <el-radio label="1">请选择字典生成</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批结论" prop="approvalConclusion">
          <el-input v-model="form.approvalConclusion" placeholder="请输入审批结论" />
        </el-form-item>
        <el-form-item label="审批意见" prop="approvalOpinions">
          <el-input v-model="form.approvalOpinions" placeholder="请输入审批意见" />
        </el-form-item>
        <el-form-item label="创建院系" prop="createDept">
          <el-input v-model="form.createDept" placeholder="请输入创建院系" />
        </el-form-item>
        <el-form-item label="更新院系" prop="updateDept">
          <el-input v-model="form.updateDept" placeholder="请输入更新院系" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listApproval, getApproval, delApproval, addApproval, updateApproval, exportApproval } from "@/api/ims/task/approval";

export default {
  name: "approval",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 审批明细信息表格数据
      approvalList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        roleId: null,
        deptId: null,
        childId: null,
        processId: null,
        taskStatus: null,
        approvalStatus: null,
        approvalConclusion: null,
        approvalOpinions: null,
        createDept: null,
        updateDept: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询审批明细信息列表 */
    getList() {
      this.loading = true;
      listApproval(this.queryParams).then(response => {
        this.approvalList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        approvalId: null,
        userId: null,
        roleId: null,
        deptId: null,
        childId: null,
        processId: null,
        taskStatus: "0",
        approvalStatus: "0",
        approvalConclusion: null,
        approvalOpinions: null,
        createDept: null,
        createTime: null,
        updateDept: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.approvalId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加审批明细信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const approvalId = row.approvalId || this.ids
      getApproval(approvalId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改审批明细信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.approvalId != null) {
            updateApproval(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addApproval(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const approvalIds = row.approvalId || this.ids;
      this.$confirm('是否确认删除审批明细信息编号为"' + approvalIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delApproval(approvalIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有审批明细信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportApproval(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
