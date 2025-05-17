<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="任务ID" prop="taskId">
        <el-input
          v-model="queryParams.taskId"
          placeholder="请输入任务ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程类型" prop="processType">
        <el-select v-model="queryParams.processType" placeholder="请选择流程类型" clearable size="small">
          <el-option
            v-for="dict in processTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="子流程类型" prop="childProcessType">
        <el-select v-model="queryParams.childProcessType" placeholder="请选择子流程类型" clearable size="small">
          <el-option
            v-for="dict in childProcessTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="下一操作人" prop="nextUser">
        <el-input
          v-model="queryParams.nextUser"
          placeholder="请输入下一操作人"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下一操作人角色" prop="nextRoleUser">
        <el-input
          v-model="queryParams.nextRoleUser"
          placeholder="请输入下一操作人角色"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="下一操作院系" prop="nextDept">
        <el-input
          v-model="queryParams.nextDept"
          placeholder="请输入下一操作院系"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="子任务状态" prop="childTaskStatus">
        <el-select v-model="queryParams.childTaskStatus" placeholder="请选择子任务状态" clearable size="small">
          <el-option
            v-for="dict in childTaskStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="子任务进度" prop="childTaskProcess">
        <el-select v-model="queryParams.childTaskProcess" placeholder="请选择子任务进度" clearable size="small">
          <el-option
            v-for="dict in childTaskProcessOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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

    <el-table v-loading="loading" :data="childList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="子任务ID" align="center" prop="childId" />
      <el-table-column label="任务ID" align="center" prop="taskId" />
      <el-table-column label="流程类型" align="center" prop="processType" :formatter="processTypeFormat" />
      <el-table-column label="子流程类型" align="center" prop="childProcessType" :formatter="childProcessTypeFormat" />
      <el-table-column label="下一操作人" align="center" prop="nextUser" />
      <el-table-column label="下一操作人角色" align="center" prop="nextRoleUser" />
      <el-table-column label="下一操作院系" align="center" prop="nextDept" />
      <el-table-column label="子任务状态" align="center" prop="childTaskStatus" :formatter="childTaskStatusFormat" />
      <el-table-column label="子任务进度" align="center" prop="childTaskProcess" :formatter="childTaskProcessFormat" />
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
            style="color: #F56C6C"
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

    <!-- 添加或修改子任务信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="任务ID" prop="taskId">
          <el-input v-model="form.taskId" placeholder="请输入任务ID" />
        </el-form-item>
        <el-form-item label="流程类型" prop="processType">
          <el-select v-model="form.processType" placeholder="请选择流程类型">
            <el-option
              v-for="dict in processTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="子流程类型" prop="childProcessType">
          <el-select v-model="form.childProcessType" placeholder="请选择子流程类型">
            <el-option
              v-for="dict in childProcessTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="下一操作人" prop="nextUser">
          <el-input v-model="form.nextUser" placeholder="请输入下一操作人" />
        </el-form-item>
        <el-form-item label="下一操作人角色" prop="nextRoleUser">
          <el-input v-model="form.nextRoleUser" placeholder="请输入下一操作人角色" />
        </el-form-item>
        <el-form-item label="下一操作院系" prop="nextDept">
          <el-input v-model="form.nextDept" placeholder="请输入下一操作院系" />
        </el-form-item>
        <el-form-item label="子任务状态">
          <el-radio-group v-model="form.childTaskStatus">
            <el-radio
              v-for="dict in childTaskStatusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >{{dict.dictLabel}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="子任务进度" prop="childTaskProcess">
          <el-select v-model="form.childTaskProcess" placeholder="请选择子任务进度">
            <el-option
              v-for="dict in childTaskProcessOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
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
import { listChild, getChild, delChild, addChild, updateChild, exportChild } from "@/api/ims/task/child";

export default {
  name: "Child",
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
      // 子任务信息表格数据
      childList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 流程类型
      processTypeOptions: [],
      // 子流程类型
      childProcessTypeOptions: [],
      // 子任务状态字典
      childTaskStatusOptions: [],
      // 子任务进度字典
      childTaskProcessOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskId: null,
        processType: null,
        childProcessType: null,
        nextUser: null,
        nextRoleUser: null,
        nextDept: null,
        childTaskStatus: null,
        childTaskProcess: null,
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
    this.getDicts("process_type").then(response => {
      this.processTypeOptions = response.data;
    });
    this.getDicts("child_process_type").then(response => {
      this.childProcessTypeOptions = response.data;
    });
    this.getDicts("stage").then(response => {
      this.childTaskStatusOptions = response.data;
    });
    this.getDicts("task_progress").then(response => {
      this.childTaskProcessOptions = response.data;
    });
  },
  methods: {
    /** 查询子任务信息列表 */
    getList() {
      this.loading = true;
      listChild(this.queryParams).then(response => {
        this.childList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 流程类型
    processTypeFormat(row, column) {
      return this.selectDictLabel(this.processTypeOptions, row.processType);
    },
    // 子流程类型1-实习周记 2-实习总结 3-实习鉴定表 字典翻译
    childProcessTypeFormat(row, column) {
      return this.selectDictLabel(this.childProcessTypeOptions, row.childProcessType);
    },
    // 子任务状态字典翻译
    childTaskStatusFormat(row, column) {
      return this.selectDictLabel(this.childTaskStatusOptions, row.childTaskStatus);
    },
    // 子任务进度字典翻译
    childTaskProcessFormat(row, column) {
      return this.selectDictLabel(this.childTaskProcessOptions, row.childTaskProcess);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        childId: null,
        taskId: null,
        processType: null,
        childProcessType: null,
        nextUser: null,
        nextRoleUser: null,
        nextDept: null,
        childTaskStatus: "0",
        childTaskProcess: null,
        createDept: null,
        createBy: null,
        createTime: null,
        updateDept: null,
        updateBy: null,
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
      this.ids = selection.map(item => item.childId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加子任务信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const childId = row.childId || this.ids
      getChild(childId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改子任务信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.childId != null) {
            updateChild(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addChild(this.form).then(response => {
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
      const childIds = row.childId || this.ids;
      this.$confirm('是否确认删除子任务信息编号为"' + childIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delChild(childIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有子任务信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportChild(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
