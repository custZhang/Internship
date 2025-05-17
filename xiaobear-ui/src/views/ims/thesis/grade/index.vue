<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课题ID" prop="topicId">
        <el-input
          v-model="queryParams.topicId"
          placeholder="请输入课题ID"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="论文成绩" prop="paperGrade">
        <el-input
          v-model="queryParams.paperGrade"
          placeholder="请输入论文成绩"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评审意见" prop="reviewOpinion">
        <el-input
          v-model="queryParams.reviewOpinion"
          placeholder="请输入评审意见"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="教师意见" prop="teaOpinion">
        <el-input
          v-model="queryParams.teaOpinion"
          placeholder="请输入教师意见"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学院意见" prop="deptOpinion">
        <el-input
          v-model="queryParams.deptOpinion"
          placeholder="请输入学院意见"
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

    <el-table v-loading="loading" :data="paperList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="论文编号" align="center" prop="paperId" />
      <el-table-column label="课题ID" align="center" prop="topicId" />
      <el-table-column label="论文成绩" align="center" prop="paperGrade" />
      <el-table-column label="评审意见" align="center" prop="reviewOpinion" />
      <el-table-column label="教师意见" align="center" prop="teaOpinion" />
      <el-table-column label="学院意见" align="center" prop="deptOpinion" />
      <el-table-column label="提交时间" align="center" prop="subTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.subTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建院系" align="center" prop="createDept" />
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

    <!-- 添加或修改论文信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课题ID" prop="topicId">
          <el-input v-model="form.topicId" placeholder="请输入课题ID" />
        </el-form-item>
        <el-form-item label="论文成绩" prop="paperGrade">
          <el-input v-model="form.paperGrade" placeholder="请输入论文成绩" />
        </el-form-item>
        <el-form-item label="评审意见" prop="reviewOpinion">
          <el-input v-model="form.reviewOpinion" placeholder="请输入评审意见" />
        </el-form-item>
        <el-form-item label="教师意见" prop="teaOpinion">
          <el-input v-model="form.teaOpinion" placeholder="请输入教师意见" />
        </el-form-item>
        <el-form-item label="学院意见" prop="deptOpinion">
          <el-input v-model="form.deptOpinion" placeholder="请输入学院意见" />
        </el-form-item>
        <el-form-item label="删除状态" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除状态" />
        </el-form-item>
        <el-form-item label="提交时间" prop="subTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.subTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择提交时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="创建院系" prop="createDept">
          <el-input v-model="form.createDept" placeholder="请输入创建院系" />
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
import { listPaper, getPaper, delPaper, addPaper, updatePaper, exportPaper } from "@/api/ims/thesis/paper";

export default {
  name: "Paper",
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
      // 论文信息表格数据
      paperList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        topicId: null,
        paperGrade: null,
        reviewOpinion: null,
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
  },
  methods: {
    /** 查询论文信息列表 */
    getList() {
      this.loading = true;
      listPaper(this.queryParams).then(response => {
        this.paperList = response.rows;
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
        paperId: null,
        topicId: null,
        paperGrade: null,
        reviewOpinion: null,
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
      this.ids = selection.map(item => item.paperId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加论文信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const paperId = row.paperId || this.ids
      getPaper(paperId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改论文信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.paperId != null) {
            updatePaper(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPaper(this.form).then(response => {
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
      const paperIds = row.paperId || this.ids;
      this.$confirm('是否确认删除论文信息编号为"' + paperIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPaper(paperIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有论文信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPaper(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
