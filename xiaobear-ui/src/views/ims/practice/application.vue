<template>
  <div class="app-container">

    <div class="border-style" style="margin-bottom: 15px">
      <el-form ref="form" :model="form" :rules="rules" label-width="135px" inline style="margin-top: 15px;">
        <el-form-item label="姓名" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入姓名" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="企业名" prop="companyId">
          <el-input v-model="form.companyId" placeholder="请输入企业名" style="width: 350px" />
        </el-form-item>
        <el-form-item label="学校指导老师" prop="schoolInstructor">
          <el-input v-model="form.schoolInstructor" placeholder="请输入学校指导老师" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="实习指导老师" prop="internInstructor">
          <el-input v-model="form.internInstructor" placeholder="请输入实习指导老师" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="实习指导老师电话" prop="internPhone">
          <el-input v-model="form.internPhone" placeholder="请输入实习指导老师电话" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="实习指导老师email" prop="internEmail">
          <el-input v-model="form.internEmail" placeholder="请输入实习指导老师email" style="width: 350px"/>
        </el-form-item>
        <el-form-item label="实习开始时间" prop="beginTime">
          <el-date-picker clearable size="small" style="width: 350px"
                          v-model="form.beginTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择实习开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实习结束时间" prop="endTime">
          <el-date-picker clearable size="small" style="width: 350px"
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择实习结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实习描述" prop="practiceDesc">
          <el-input type="textarea" v-model="form.practiceDesc" placeholder="请输入实习描述" style="width: 800px" />
        </el-form-item>
      </el-form>
      <div slot="footer" style="margin-bottom: 15px;margin-left: 500px">
        <el-button type="primary" @click="submitForm">提交</el-button>
      </div>
    </div>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['ims:practice:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ims:practice:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ims:practice:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ims:practice:export']"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="practiceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户名" align="center" prop="userId" />
      <el-table-column label="企业名" align="center" prop="companyId" />
      <el-table-column label="学校指导老师" align="center" prop="schoolInstructor" />
      <el-table-column label="实习指导老师" align="center" prop="internInstructor" />
      <el-table-column label="实习指导老师电话" align="center" prop="internPhone" />
      <el-table-column label="实习开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实习结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="status" :formatter="statusFormatter"/>
      <el-table-column label="实习状态" align="center" prop="practiceStatus" />
      <el-table-column label="创建院系" align="center" prop="createDept" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ims:practice:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ims:practice:remove']"
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

    <!-- 添加或修改实习信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="135px" inline>
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="企业ID" prop="companyId">
          <el-input v-model="form.companyId" placeholder="请输入企业ID" />
        </el-form-item>
        <el-form-item label="学校指导老师" prop="schoolInstructor">
          <el-input v-model="form.schoolInstructor" placeholder="请输入学校指导老师" />
        </el-form-item>
        <el-form-item label="实习指导老师" prop="internInstructor">
          <el-input v-model="form.internInstructor" placeholder="请输入实习指导老师" />
        </el-form-item>
        <el-form-item label="实习指导老师电话" prop="internPhone">
          <el-input v-model="form.internPhone" placeholder="请输入实习指导老师电话" />
        </el-form-item>
        <el-form-item label="实习指导老师email" prop="internEmail">
          <el-input v-model="form.internEmail" placeholder="请输入实习指导老师email" />
        </el-form-item>
        <el-form-item label="实习开始时间" prop="beginTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.beginTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择实习开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实习结束时间" prop="endTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择实习结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实习描述" prop="practiceDesc">
          <el-input type="textarea" v-model="form.practiceDesc" placeholder="请输入实习描述" style="width: 800px" />
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
import { listPractice, getPractice, delPractice, addPractice, updatePractice, exportPractice } from "@/api/ims/practice/practice";

export default {
  name: "Practice",
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
      // 实习信息表格数据
      practiceList: [],
      //字典管理
      statusOption:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        companyId: null,
        schoolInstructor: null,
        internInstructor: null,
        internPhone: null,
        internEmail: null,
        beginTime: null,
        endTime: null,
        practiceDesc: null,
        status: null,
        practiceStatus: null,
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
    //审核状态 0-审核中  1-审核通过
    this.getDicts("status").then(response => {
      this.statusOption = response.data;
    });
  },
  methods: {
    /** 查询实习信息列表 */
    getList() {
      this.loading = true;
      listPractice(this.queryParams).then(response => {
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
        practiceId: null,
        userId: null,
        companyId: null,
        schoolInstructor: null,
        internInstructor: null,
        internPhone: null,
        internEmail: null,
        beginTime: null,
        endTime: null,
        practiceDesc: null,
        status: "0",
        delFlag: null,
        practiceStatus: "0",
        createDept: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    //字典翻译
    /** 审核状态 */
    statusFormatter(row, column){
      return this.selectDictLabel(this.statusOption, row.status);
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
      this.ids = selection.map(item => item.practiceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加实习信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改实习信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.practiceId != null) {
            updatePractice(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPractice(this.form).then(response => {
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
      const practiceIds = row.practiceId || this.ids;
      this.$confirm('是否确认删除实习信息编号为"' + practiceIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delPractice(practiceIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有实习信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPractice(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
