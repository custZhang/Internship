<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实习单位" prop="practiceId">
        <el-input
          v-model="queryParams.practiceId"
          placeholder="请输入实习单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工资" prop="postWage">
        <el-input
          v-model="queryParams.postWage"
          placeholder="请输入工资"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="待遇" prop="postTreatment">
        <el-input
          v-model="queryParams.postTreatment"
          placeholder="请输入待遇"
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

    <el-table v-loading="loading" :data="benefitsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实习单位" align="center" prop="companyName" />
      <el-table-column label="工资" align="center" prop="postWage" />
      <el-table-column label="待遇" align="center" prop="postTreatment" />
      <el-table-column label="福利管理" align="center" prop="welfare" />
      <el-table-column label="备注" align="center" prop="remark" />
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

    <!-- 添加或修改工资待遇信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-form-item label="实习单位" prop="practiceId">
          <el-select v-model="form.practiceId" clearable placeholder="请选择实习单位">
            <el-option
              v-for="o in practiceList"
              :key="o.practiceId"
              :label="o.companyName"
              :value="o.practiceId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工资" prop="postWage">
          <el-input v-model="form.postWage" placeholder="例如：12k" />
        </el-form-item>
        <el-form-item label="待遇" prop="postTreatment">
          <el-input type="textarea" v-model="form.postTreatment" placeholder="请输入待遇" style="width: 520px"/>
        </el-form-item>
        <el-form-item label="福利管理" prop="welfare">
          <el-input type="textarea" v-model="form.welfare" placeholder="请输入福利管理" style="width: 520px"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" style="width: 520px"/>
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
import { listBenefits, getBenefits, delBenefits, addBenefits, updateBenefits, exportBenefits } from "@/api/ims/entitlement/benefits";
import {personalListPractice} from "@/api/ims/practice/practice";

export default {
  name: "Benefits",
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
      // 工资待遇信息表格数据
      benefitsList: [],
      //实习列表
      practiceList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        practiceId: null,
        welfare: null,
        postWage: null,
        postTreatment: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        practiceId: [
          { required: true, message: "实习ID不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询工资待遇信息列表 */
    getList() {
      this.loading = true;
      listBenefits(this.queryParams).then(response => {
        this.benefitsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询个人实习列表 */
    getPracticeList(){
      personalListPractice(this.queryParams).then(response => {
        this.practiceList = response.rows;
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
        benefitsId: null,
        practiceId: null,
        welfare: null,
        postWage: null,
        postTreatment: null,
        remark: null,
        delFlag: "0",
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
      this.ids = selection.map(item => item.benefitsId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工资待遇信息";
      this.getPracticeList();
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const benefitsId = row.benefitsId || this.ids
      getBenefits(benefitsId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工资待遇信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.benefitsId != null) {
            updateBenefits(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBenefits(this.form).then(response => {
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
      const benefitsIds = row.benefitsId || this.ids;
      this.$confirm('是否确认删除工资待遇信息编号为"' + benefitsIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delBenefits(benefitsIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有工资待遇信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportBenefits(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
