<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程子流程" prop="ProcessChildType" label-width="88px">
        <el-select v-model="queryParams.ProcessChildType" placeholder="请选择流程子流程" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="阶段" prop="stage">
        <el-select v-model="queryParams.stage" placeholder="请选择阶段" clearable size="small">
          <el-option
            v-for="dict in stageOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="阶段名称" prop="stageMappering">
        <el-input
          v-model="queryParams.stageMappering"
          placeholder="请输入阶段名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-input
          v-model="queryParams.role"
          placeholder="请输入角色"
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
          plain
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd()"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程ID" align="center"  prop="processId" />
      <el-table-column label="流程类型" align="center" prop="processType" :formatter="processTypeFormat" />
      <el-table-column label="流程序号" align="center" prop="processNum" />
      <el-table-column label="流程子流程" align="center" prop="processChildType" :formatter="childProcessTypeFormat" />
      <el-table-column label="阶段" align="center" prop="stage" :formatter="stageFormat" />
      <el-table-column label="阶段名称" align="center" prop="stageMappering" />
      <el-table-column label="角色" align="center" prop="role" />
      <el-table-column label="节点类型" align="center" prop="nodeType" />
      <el-table-column label="是否审核节点" align="center" prop="isCheckNode" :formatter="isCheckNodeFormat" />
      <el-table-column label="审核结论" align="center" prop="checkConclusion" :formatter="checkConclusionFormat" />
      <el-table-column label="流程是否结束" align="center" prop="processEnd" :formatter="processEndFormat" />
      <el-table-column label="下一流程ID" align="center" prop="nextProcessId" />
      <el-table-column label="下一院系类型" align="center" prop="nextInstitutionType" />
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

    <!-- 添加或修改流程配置信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" inline>
        <el-form-item label="流程Id" prop="processId">
          <el-input v-model="form.processId" placeholder="请输入流程Id" />
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
        <el-form-item label="流程序号" prop="processNum">
          <el-input v-model="form.processNum" placeholder="请输入流程序号" />
        </el-form-item>
        <el-form-item label="流程子流程" prop="ProcessChildType">
          <el-select v-model="form.ProcessChildType" placeholder="请选择流程子流程">
            <el-option
              v-for="dict in childProcessTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="阶段" prop="stage">
          <el-select v-model="form.stage" placeholder="请选择阶段">
            <el-option
              v-for="dict in stageOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="阶段名称" prop="stageMappering">
          <el-input v-model="form.stageMappering" placeholder="请输入阶段名称" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-input v-model="form.role" placeholder="请输入角色" />
        </el-form-item>
        <el-form-item label="节点类型" prop="nodeType">
          <el-select v-model="form.nodeType" placeholder="请选择节点类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否审核节点" prop="isCheckNode">
          <el-select v-model="form.isCheckNode" placeholder="请选择是否审核节点">
            <el-option
              v-for="dict in isCheckNodeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="审核结论" prop="checkConclusion">
          <el-select v-model="form.checkConclusion" placeholder="请选择审核结论">
            <el-option
              v-for="dict in checkConclusionOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="流程是否结束" prop="processEnd">
          <el-select v-model="form.processEnd" placeholder="请选择流程是否结束">
            <el-option
              v-for="dict in processEndOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="下一流程ID" prop="nextProcessId">
          <el-input v-model="form.nextProcessId" placeholder="请输入下一流程ID" />
        </el-form-item>
        <el-form-item label="下一院系类型" prop="nextInstitutionType">
          <el-select v-model="form.nextInstitutionType" placeholder="请选择下一院系类型">
            <el-option label="请选择字典生成" value="" />
          </el-select>
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
import { listConfig, getConfig, delConfig, addConfig, updateConfig, exportConfig } from "@/api/ims/task/config";

export default {
  name: "Config",
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
      // 流程配置信息表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否为新增按钮
      isAddButton: true,
      // 流程类型0-实习任务1-论文任务字典
      processTypeOptions: [],
      //子流程
      childProcessTypeOptions: [],
      // 阶段y01-待指导老师审核y02-待学院审核字典
      stageOptions: [],
      // 是否审核节点Y-是  N-否字典
      isCheckNodeOptions: [],
      // 审核结论T-同意F-拒绝字典
      checkConclusionOptions: [],
      // 流程是否结束Y-是  N-否字典
      processEndOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        processType: null,
        processNum: null,
        ProcessChildType: null,
        stage: null,
        stageMappering: null,
        role: null,
        nodeType: null,
        isCheckNode: null,
        checkConclusion: null,
        processEnd: null,
        nextProcessId: null,
        nextInstitutionType: null
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
    this.getDicts("stage").then(response => {
      this.stageOptions = response.data;
    });
    this.getDicts("is_check_node").then(response => {
      this.isCheckNodeOptions = response.data;
    });
    this.getDicts("check_conclusion").then(response => {
      this.checkConclusionOptions = response.data;
    });
    this.getDicts("process_end").then(response => {
      this.processEndOptions = response.data;
    });
    this.getDicts("child_process_type").then(response => {
      this.childProcessTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询流程配置信息列表 */
    getList() {
      this.loading = true;
      listConfig(this.queryParams).then(response => {
        this.configList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 子流程类型1-实习周记 2-实习总结 3-实习鉴定表 字典翻译
    childProcessTypeFormat(row, column) {
      return this.selectDictLabel(this.childProcessTypeOptions, row.processChildType);
    },
    // 流程类型0-实习任务1-论文任务字典翻译
    processTypeFormat(row, column) {
      return this.selectDictLabel(this.processTypeOptions, row.processType);
    },
    // 阶段y01-待指导老师审核y02-待学院审核字典翻译
    stageFormat(row, column) {
      return this.selectDictLabel(this.stageOptions, row.stage);
    },
    // 是否审核节点Y-是  N-否字典翻译
    isCheckNodeFormat(row, column) {
      return this.selectDictLabel(this.isCheckNodeOptions, row.isCheckNode);
    },
    // 审核结论T-同意F-拒绝字典翻译
    checkConclusionFormat(row, column) {
      return this.selectDictLabel(this.checkConclusionOptions, row.checkConclusion);
    },
    // 流程是否结束Y-是  N-否字典翻译
    processEndFormat(row, column) {
      return this.selectDictLabel(this.processEndOptions, row.processEnd);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        processId: null,
        processType: null,
        processNum: null,
        ProcessChildType: null,
        stage: null,
        stageMappering: null,
        role: null,
        nodeType: null,
        isCheckNode: null,
        checkConclusion: null,
        processEnd: null,
        nextProcessId: null,
        nextInstitutionType: null
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
      this.ids = selection.map(item => item.processId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
/*      configList.push({
        processId: null,
        processType: null,
        processNum: null,
        ProcessChildType: null,
        stage: null,
        stageMappering: null,
        role: null,
        nodeType: null,
        isCheckNode: null,
        checkConclusion: null,
        processEnd: null,
        nextProcessId: null,
        nextInstitutionType: null})*/
      this.reset();
      this.open = true;
      this.title = "添加流程配置信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const processId = row.processId || this.ids
      getConfig(processId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程配置信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.processId != null) {
            updateConfig(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交按钮 */
    updateSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.processId != null) {
            updateConfig(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addConfig(this.form).then(response => {
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
      const processIds = row.processId || this.ids;
      this.$confirm('是否确认删除流程配置信息编号为"' + processIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delConfig(processIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有流程配置信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportConfig(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
