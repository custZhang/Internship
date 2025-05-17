<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实习" prop="practiceId">
        <el-input
          v-model="queryParams.practiceId"
          placeholder="请输入实习"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择审核状态" clearable size="small">
          <el-option
            v-for="dict in statusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
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

    <el-table v-loading="loading" :data="gradeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="nickName" />
      <el-table-column label="实习单位" align="center" prop="companyName" />
      <el-table-column label="专业实习成绩" align="center" prop="piResult" />
      <el-table-column label="研究报告成绩" align="center" prop="srResult" />
      <el-table-column label="专业实习总成绩" align="center" prop="totalScore" />
      <el-table-column label="教师意见" align="center" prop="teaOpinion" />
      <el-table-column label="实习小组意见" align="center" prop="groupOpinion" />
      <el-table-column label="学院意见" align="center" prop="deptOpinion" />
      <el-table-column label="审核状态" align="center" prop="status" :formatter="statusFormatter"/>
      <el-table-column label="操作" align="center" v-if="false" class-name="small-padding fixed-width">
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

    <!-- 添加或修改实习成绩信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="99px" inline>
        <el-form-item label="姓名" prop="practiceId">
          <el-select v-model="form.practiceId" filterable clearable placeholder="请输入或选择">
          <el-option
            v-for="item in practiceList"
            :key="item.practiceId"
            :label="item.nickName"
            :value="item.practiceId">
          </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="专业实习成绩" label-width="118px" prop="piResult">
          <el-input v-model="form.piResult" placeholder="请输入专业实习成绩" />
        </el-form-item>
        <el-form-item label="研究报告成绩"  prop="srResult">
          <el-input v-model="form.srResult" style="width: 205px" placeholder="请输入调查与研究报告成绩" />
        </el-form-item>
        <el-form-item label="专业实习总成绩" label-width="118px" prop="totalScore">
          <el-input v-model="form.totalScore"  placeholder="请输入专业实习总成绩" />
        </el-form-item>
        <el-form-item label="教师意见" prop="teaOpinion">
          <el-input v-model="form.teaOpinion" class="input520" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="实习小组意见" prop="groupOpinion">
          <el-input v-model="form.groupOpinion" class="input520" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="学院意见" prop="deptOpinion">
          <el-input v-model="form.deptOpinion" class="input520" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="描述" prop="gradeDesc">
          <el-input v-model="form.gradeDesc" class="input520" type="textarea" placeholder="请输入内容" />
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
import { listGrade, getGrade, delGrade, addGrade, updateGrade, exportGrade } from "@/api/ims/practice/grade";
import {listPractice} from "../../../../api/ims/practice/practice";

export default {
  name: "Grade",
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
      // 实习成绩信息表格数据
      gradeList: [],
      // 实习申请列表
      practiceList: [],
      //审核字典值
      statusOptions:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        practiceId: null,
        piResult: null,
        srResult: null,
        totalScore: null,
        teaOpinion: null,
        groupOpinion: null,
        deptOpinion: null,
        gradeDesc: null,
        status: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        practiceId: [
          { required: true, message: "实习ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("status").then(response => {
      this.statusOptions = response.data;
    });
  },
  methods: {
    /** 查询实习成绩信息列表 */
    getList() {
      this.loading = true;
      listGrade(this.queryParams).then(response => {
        this.gradeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询实习申请列表 */
    getPracticeList() {
      listPractice().then(response => {
        this.practiceList = response.rows;
        console.log(response.rows)
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
        gradeId: null,
        practiceId: null,
        piResult: null,
        srResult: null,
        totalScore: null,
        teaOpinion: null,
        groupOpinion: null,
        deptOpinion: null,
        gradeDesc: null,
        status: "0",
        delFlag: null,
        createDept: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 审核状态 */
    statusFormatter(row, column){
      return this.selectDictLabel(this.statusOptions, row.status);
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
      this.ids = selection.map(item => item.gradeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.getPracticeList();
      this.title = "添加实习成绩信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getPracticeList();
      const gradeId = row.gradeId || this.ids
      getGrade(gradeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改实习成绩信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.gradeId != null) {
            updateGrade(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addGrade(this.form).then(response => {
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
      const gradeIds = row.gradeId || this.ids;
      this.$confirm('是否确认删除实习成绩信息编号为"' + gradeIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delGrade(gradeIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有实习成绩信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportGrade(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
