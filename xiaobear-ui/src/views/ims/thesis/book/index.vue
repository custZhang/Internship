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

    <el-table v-loading="loading" :data="bookList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课题" align="center" prop="topicId" />
      <el-table-column label="研究内容" align="center" prop="researchContent" />
      <el-table-column label="预期进展" align="center" prop="process" />
      <el-table-column label="参考文献" align="center" prop="reference" />
      <el-table-column label="学院意见" align="center" prop="deptOpinion" />
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

    <!-- 添加或修改任务书信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课题" prop="topicId">
          <el-select v-model="form.topicId" filterable placeholder="请选择课题" style="width: 880px">
            <el-option
              v-for="o in topicList"
              :key="o.topicId"
              :label="o.topicTitle"
              :value="o.topicId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="研究内容">
          <editor v-model="form.researchContent" :min-height="172"/>
        </el-form-item>
        <el-form-item label="预期进展" prop="process">
          <el-input v-model="form.process" type="textarea" placeholder="请输入预期进展" />
        </el-form-item>
        <el-form-item label="参考文献" prop="reference">
          <editor v-model="form.reference" :min-height="172"/>
        </el-form-item>
        <el-form-item label="学院意见" prop="deptOpinion">
          <el-input v-model="form.deptOpinion" type="textarea" placeholder="请输入学院意见" />
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
import { listBook, getBook, delBook, addBook, updateBook, exportBook } from "@/api/ims/thesis/book";
import Editor from '@/components/Editor';
import {listTopic} from "../../../../api/ims/thesis/topic";
import {getUserProfile} from "../../../../api/system/user";

export default {
  name: "Book",
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
      // 任务书信息表格数据
      bookList: [],
      // 课题列表
      topicList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        topicId: null,
        researchContent: null,
        process: null,
        reference: null,
        deptOpinion: null,
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
    /** 查询任务书信息列表 */
    getList() {
      this.loading = true;
      listBook(this.queryParams).then(response => {
        this.bookList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询课题信息列表 */
    getTopicList() {
      getUserProfile().then(res => {
        const teacherId = res.data.userId;
        let imsThesisTopic ={
          teacherId: teacherId
        }
        listTopic(imsThesisTopic).then(response => {
          this.topicList = response.rows;
        });
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
        taskBookId: null,
        topicId: null,
        researchContent: null,
        process: null,
        reference: null,
        deptOpinion: null,
        delFlag: null,
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
      this.ids = selection.map(item => item.taskBookId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTopicList();
      this.open = true;
      this.title = "添加任务书信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTopicList();
      const taskBookId = row.taskBookId || this.ids
      getBook(taskBookId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改任务书信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskBookId != null) {
            updateBook(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBook(this.form).then(response => {
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
      const taskBookIds = row.taskBookId || this.ids;
      this.$confirm('是否确认删除任务书信息编号为"' + taskBookIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBook(taskBookIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有任务书信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBook(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
