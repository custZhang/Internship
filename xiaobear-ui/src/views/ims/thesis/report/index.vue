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

    <el-table v-loading="loading" :data="reportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课题编号" align="center" prop="topicId" />
      <el-table-column label="课题简介" align="center" prop="topicTitle" />
      <el-table-column label="课题描述" align="center" prop="overview" />
      <el-table-column label="研究方法" align="center" prop="method" />
      <el-table-column label="创新之处" align="center" prop="innovation":show-overflow-tooltip="true" />
      <el-table-column label="研究内容" align="center" prop="researchContent" :show-overflow-tooltip="true"/>
      <el-table-column label="预期进展" align="center" prop="process" :show-overflow-tooltip="true"/>
      <el-table-column label="参考文献" align="center" prop="references" :show-overflow-tooltip="true"/>
      <el-table-column label="所需条件" align="center" prop="requirement" :show-overflow-tooltip="true"/>
      <el-table-column label="教师意见" align="center" prop="teaOpinion" :show-overflow-tooltip="true"/>
      <el-table-column label="学院意见" align="center" prop="deptOpinion" :show-overflow-tooltip="true"/>
      <el-table-column label="提交时间" align="center" prop="subTime" width="160">
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

    <!-- 添加或修改开题报告信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课题编号" prop="topicId">
          <el-input v-model="form.topicId" placeholder="请输入课题编号" />
        </el-form-item>
        <el-form-item label="课题简介" prop="topicTitle">
          <el-input v-model="form.topicTitle" type="textarea" placeholder="请输入课题简介" />
        </el-form-item>
        <el-form-item label="课题描述" prop="overview">
          <el-input v-model="form.overview" type="textarea" placeholder="请输入课题描述" />
        </el-form-item>
        <el-form-item label="课题目标、意义" prop="goal">
          <el-input v-model="form.goal" type="textarea" placeholder="请输入课题目标、意义" />
        </el-form-item>
        <el-form-item label="研究方法" prop="method">
          <el-input v-model="form.method" type="textarea" placeholder="请输入研究方法" />
        </el-form-item>
        <el-form-item label="创新之处" prop="innovation">
          <el-input v-model="form.innovation" type="textarea" placeholder="请输入创新之处" />
        </el-form-item>
        <el-form-item label="研究内容">
          <editor v-model="form.researchContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="预期进展" prop="process">
          <el-input v-model="form.process" type="textarea" placeholder="请输入预期进展" />
        </el-form-item>
        <el-form-item label="参考文献" prop="references">
          <el-input v-model="form.references" type="textarea" placeholder="请输入参考文献" />
        </el-form-item>
        <el-form-item label="所需条件" prop="requirement">
          <el-input v-model="form.requirement" type="textarea" placeholder="请输入所需条件" />
        </el-form-item>
        <el-form-item label="教师意见" v-show="isStudent" prop="teaOpinion">
          <el-input v-model="form.teaOpinion" type="textarea" placeholder="请输入教师意见" />
        </el-form-item>
        <el-form-item label="学院意见" v-show="isStudent" prop="deptOpinion">
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
import { listReport, getReport, delReport, addReport, updateReport, exportReport } from "@/api/ims/thesis/report";
import Editor from '@/components/Editor';
import {listTopic, topicByUserId} from "../../../../api/ims/thesis/topic";
import {getUserProfile} from "../../../../api/system/user";

export default {
  name: "Report",
  components: { Editor },
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
      // 开题报告信息表格数据
      reportList: [],
      // 课题列表
      topicList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //登录用户角色
      user: {},
      roleGroup: {},
      //是否是学生
      isStudent: true,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        topicId: null,
        topicTitle: null,
        overview: null,
        goal: null,
        method: null,
        innovation: null,
        researchContent: null,
        process: null,
        references: null,
        requirement: null,
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
  },
  methods: {
    /** 查询开题报告信息列表 */
    getList() {
      this.loading = true;
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows;
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
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        reportId: null,
        topicId: null,
        topicTitle: null,
        overview: null,
        goal: null,
        method: null,
        innovation: null,
        researchContent: null,
        process: null,
        references: null,
        requirement: null,
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
      this.ids = selection.map(item => item.reportId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 通过用户ID查询课题信息 */
    handleSelectTopic(){
      getUserProfile().then(res =>{
        const userId = res.data.userId;
        listTopic({userId}).then(response => {
          this.form.topicId = response.rows[0].topicId
        });
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.handleSelectTopic();
      this.open = true;
      this.title = "添加开题报告信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const reportId = row.reportId || this.ids
      getReport(reportId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改开题报告信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.reportId != null) {
            updateReport(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addReport(this.form).then(response => {
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
      const reportIds = row.reportId || this.ids;
      this.$confirm('是否确认删除开题报告信息编号为"' + reportIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delReport(reportIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有开题报告信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportReport(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
