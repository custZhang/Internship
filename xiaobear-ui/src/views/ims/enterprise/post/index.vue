<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公司ID" prop="companyId">
        <el-input
          v-model="queryParams.companyId"
          placeholder="请输入公司名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="经验" prop="postExperience">
        <el-select v-model="queryParams.postExperience" placeholder="请输入经验要求">
          <el-option
            v-for="dict in workingYearsOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学历要求" prop="education">
        <el-select v-model="queryParams.education" placeholder="请输入学历要求">
          <el-option
            v-for="dict in educationOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
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

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="公司名" align="center" prop="companyName"/>
      <el-table-column label="岗位名称" align="center" prop="postName" />
      <el-table-column label="经验" align="center" prop="postExperience" :formatter="postExperienceFormatter"/>
      <el-table-column label="学历要求" width="99" align="center" prop="education" :formatter="educationFormatter"/>
      <el-table-column label="工资 "width="99" align="center" prop="postWage" />
      <el-table-column label="待遇" align="center" prop="postTreatment" />
      <el-table-column label="招聘人数" align="center" prop="postNumber" />
      <el-table-column label="创建院系" align="center"  prop="createDept" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleCheckInfo(scope.row.postId)"
          >查看</el-button>
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

    <!-- 添加或修改企业岗位信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-form-item label="公司名" prop="companyId">
          <el-select v-model="form.companyId" placeholder="请输入公司名">
            <el-option
              v-for="o in companyInfoList"
              :key="o.companyId"
              :label="o.companyName"
              :value="o.companyId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="岗位名称" prop="postName">
          <el-input v-model="form.postName" placeholder="请输入岗位名称" />
        </el-form-item>
        <el-form-item label="经验" prop="postExperience">
          <el-select v-model="form.postExperience" placeholder="请输入经验要求">
            <el-option
              v-for="dict in workingYearsOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="任职要求" prop="requirements">
          <editor v-model="form.requirements" placeholder="请输入任职要求" :min-height="144"/>
        </el-form-item>
        <el-form-item label="工作职责" prop="responsibilities">
          <editor v-model="form.responsibilities" placeholder="请输入工作职责" :min-height="144"/>
        </el-form-item>
        <el-form-item label="学历要求" prop="education">
          <el-select v-model="form.education" placeholder="请输入学历要求">
            <el-option
              v-for="dict in educationOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工资" prop="postWage">
          <el-input v-model="form.postWage" placeholder="请输入工资" />
        </el-form-item>
        <el-form-item label="待遇" prop="postTreatment">
          <el-input v-model="form.postTreatment" placeholder="请输入待遇" />
        </el-form-item>
        <el-form-item label="招聘人数" prop="postNumber">
          <el-input-number v-model="form.postNumber" :min="1"  ></el-input-number>
<!--          <el-input v-model="form.postNumber" placeholder="请输入招聘人数" />-->
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
import { listPost, getPost, delPost, addPost, updatePost, exportPost } from "@/api/ims/enterprise/post";
import { companyListInfo } from "@/api/ims/enterprise/info"
import Editor from '@/components/Editor';
export default {
  name: "Post",
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
      // 企业岗位信息表格数据
      postList: [],
      companyInfoList: [],
      //字典管理
      educationOption: [],
      workingYearsOption: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        companyId: null,
        requirements: null,
        postExperience: null,
        responsibilities: null,
        education: null,
        postWage: null,
        postTreatment: null,
        postNumber: null,
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
    this.getCompanyList();
    this.getDicts("working_years").then(response => {
      this.workingYearsOption = response.data;
    });
    this.getDicts("education").then(response => {
      this.educationOption = response.data;
    });
  },
  methods: {
    /** 查询企业岗位信息列表 */
    getList() {
      this.loading = true;
      listPost(this.queryParams).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 招聘公司列表 */
    getCompanyList() {
      this.loading = true;
      companyListInfo(this.queryParams).then(response => {
        this.companyInfoList = response.rows;
        this.loading = false;
      });
    },
    // 字典状态字典翻译
    postExperienceFormatter(row, column) {
      return this.selectDictLabel(this.workingYearsOption, row.postExperience);
    },
    educationFormatter(row, column) {
      return this.selectDictLabel(this.educationOption, row.education);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        postId: null,
        companyId: null,
        requirements: null,
        postExperience: null,
        responsibilities: null,
        education: null,
        postWage: null,
        postTreatment: null,
        postNumber: null,
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
      this.ids = selection.map(item => item.postId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加企业岗位信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const postId = row.postId || this.ids
      getPost(postId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业岗位信息";
      });
    },
    /** 查看按钮 */
    handleCheckInfo(postId){
      this.$router.push({
        path: '/corporateInfo',
        query: {
          id: postId
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.postId != null) {
            updatePost(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            console.log(this.form);
            addPost(this.form).then(response => {
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
      const postIds = row.postId || this.ids;
      this.$confirm('是否确认删除企业岗位信息编号为"' + postIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPost(postIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有企业岗位信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPost(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
