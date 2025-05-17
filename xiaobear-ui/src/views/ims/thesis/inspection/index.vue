<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="72px">
      <el-form-item label="课题编号" prop="topicId">
        <el-input
          v-model="queryParams.topicId"
          placeholder="请输入课题编号"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提交时间" prop="subTime">
        <el-date-picker clearable size="small" style="width: 200px"
          v-model="queryParams.subTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="选择提交时间">
        </el-date-picker>
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
          @click="handleAdd"
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

    <el-table v-loading="loading" :data="inspectionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="中期检查ID" align="center" prop="inspectId" />
      <el-table-column label="课题编号" align="center" prop="topicId" />
      <el-table-column label="课题进展" align="center" prop="process" />
      <el-table-column label="遇到的问题" align="center" prop="problem" />
      <el-table-column label="教师意见" align="center" prop="teaOpinion" />
      <el-table-column label="学院意见" align="center" prop="deptOpinion" />
      <el-table-column label="提交时间" align="center" prop="subTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.subTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
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

    <!-- 添加或修改论文中期检查信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="88px">
        <el-form-item label="课题编号" prop="topicId">
          <el-input v-model="form.topicId" placeholder="请输入课题编号" />
        </el-form-item>
        <el-form-item label="课题进展" prop="process">
          <el-input v-model="form.process" type="textarea" placeholder="请输入课题进展" />
        </el-form-item>
        <el-form-item label="遇到的问题" prop="problem">
          <el-input v-model="form.problem" type="textarea" placeholder="请输入遇到的问题" />
        </el-form-item>
        <el-form-item label="教师意见" v-show="isStudent" prop="teaOpinion">
          <el-input v-model="form.teaOpinion" type="textarea" placeholder="请输入教师意见" />
        </el-form-item>
        <el-form-item label="学院意见"  v-show="isStudent" prop="deptOpinion">
          <el-input v-model="form.deptOpinion" type="textarea" placeholder="请输入学院意见" />
        </el-form-item>
        <el-form-item label="提交时间" prop="subTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.subTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择提交时间">
          </el-date-picker>
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
import { listInspection, getInspection, delInspection, addInspection, updateInspection, exportInspection } from "@/api/ims/thesis/inspection";
import {getUserProfile} from "../../../../api/system/user";
import {listTopic} from "../../../../api/ims/thesis/topic";

export default {
  name: "Inspection",
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
      // 论文中期检查信息表格数据
      inspectionList: [],
      // 弹出层标题
      title: "",
      //登录用户角色
      user: {},
      roleGroup: {},
      //是否是学生
      isStudent: true,
      // 是否显示弹出层
      open: false,
      // 删除状态字典
      delFlagOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        topicId: null,
        process: null,
        problem: null,
        teaOpinion: null,
        deptOpinion: null,
        subTime: null,
        createDept: null,
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
    this.getUser();
    this.getDicts("del_flag").then(response => {
      this.delFlagOptions = response.data;
    });
  },
  methods: {
    /** 查询论文中期检查信息列表 */
    getList() {
      this.loading = true;
      listInspection(this.queryParams).then(response => {
        this.inspectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getUser(){
      getUserProfile().then(response =>{
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        if ("学生" === this.roleGroup){
          this.isStudent = false;
        }
      })
    },
    // 删除状态字典翻译
    delFlagFormat(row, column) {
      return this.selectDictLabel(this.delFlagOptions, row.delFlag);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        inspectId: null,
        topicId: null,
        process: null,
        problem: null,
        teaOpinion: null,
        deptOpinion: null,
        delFlag: null,
        subTime: null,
        createDept: null,
        createBy: null,
        createTime: null,
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
      this.ids = selection.map(item => item.inspectId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.handleSelectTopic();
      this.open = true;
      this.title = "添加论文中期检查信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const inspectId = row.inspectId || this.ids
      getInspection(inspectId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改论文中期检查信息";
      });
    },
    /** 通过用户ID查询课题信息 */
    handleSelectTopic(){
      getUserProfile().then(res =>{
        const userId = res.data.userId;
        console.log(userId)
        listTopic({userId}).then(response => {
          this.form.topicId = response.rows[0].topicId
        });
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.inspectId != null) {
            updateInspection(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInspection(this.form).then(response => {
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
      const inspectIds = row.inspectId || this.ids;
      this.$confirm('是否确认删除论文中期检查信息编号为"' + inspectIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInspection(inspectIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有论文中期检查信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportInspection(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
