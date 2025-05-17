<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="公司名" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入公司名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司位置" prop="companyLocation">
        <el-input
          v-model="queryParams.companyLocation"
          placeholder="请输入公司位置"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公司性质" prop="companyType">
        <el-select v-model="queryParams.companyType" placeholder="请选择公司性质" clearable size="small">
          <el-option
            v-for="dict in companyTypeOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="公司行业" prop="companyIndustry">
        <el-select v-model="queryParams.companyIndustry" placeholder="请输入公司行业" clearable size="small">
          <el-option
            v-for="dict in companyIndustryOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="招聘状态" prop="recruitStatus">
        <el-select v-model="queryParams.recruitStatus" placeholder="请选择招聘状态" clearable size="small">
          <el-option
            v-for="dict in recruitStatusOption"
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
      <el-col :span="1.5">
        <el-button
          plain
          type="info"
          icon="el-icon-check"
          size="mini"
          :disabled="single"
          @click="handleCheck"
        >审核</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="公司名" align="center" prop="companyName"/>
      <el-table-column label="公司位置" align="center" prop="companyLocation" />
      <el-table-column label="公司性质" width="80" align="center" prop="companyType" :formatter="companyTypeFormatter"/>
      <el-table-column label="公司行业" width="80" align="center" prop="companyIndustry" :formatter="companyIndustryFormatter"/>
      <el-table-column label="公司负责人" align="center" prop="companyPrincipal" />
      <el-table-column label="负责人电话" align="center" prop="principalPhone" />
      <el-table-column label="负责人email" align="center" prop="principalEmail" />
      <el-table-column label="招聘状态" align="center" prop="recruitStatus" :formatter="recruitStatusFormatter" />
      <el-table-column label="审核状态" align="center" prop="status" :formatter="statusFormatter"/>
      <el-table-column label="创建时间" align="center" prop="createTime" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
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
    <!-- 添加或修改企业信息对话框 -->
    <el-dialog :title="title" :visible.sync="open"  width="1000px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="95px" inline>
          <el-form-item label="公司名" prop="companyName">
            <el-input v-model="form.companyName" placeholder="请输入公司名" />
          </el-form-item>
          <el-form-item label="法人代表" prop="legalRepresentative">
            <el-input v-model="form.legalRepresentative" placeholder="请输入法人代表"/>
          </el-form-item>
          <el-form-item label="成立时间" prop="established">
            <el-date-picker clearable size="small" style="width: 200px"
                            v-model="form.established"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="选择成立时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="公司介绍" prop="companyProfile">
            <editor  v-model="form.companyProfile" placeholder="请输入公司介绍" :min-height="144" />
          </el-form-item>
          <el-form-item label="公司位置" prop="companyLocation">
            <el-input v-model="form.companyLocation" placeholder="请输入公司位置" />
          </el-form-item>
          <el-form-item label="公司性质" prop="companyType">
            <el-select v-model="form.companyType" placeholder="请选择公司性质">
              <el-option
                v-for="dict in companyTypeOption"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="公司行业" prop="companyIndustry">
            <el-select v-model="form.companyIndustry" placeholder="请输入公司行业" clearable>
              <el-option
                v-for="dict in companyIndustryOption"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="公司负责人" prop="companyPrincipal">
            <el-input v-model="form.companyPrincipal" placeholder="请输入公司负责人" />
          </el-form-item>
          <el-form-item label="负责人电话" prop="principalPhone">
            <el-input v-model="form.principalPhone" placeholder="请输入负责人电话" style="width: 205px"/>
          </el-form-item>
          <el-form-item label="负责人email" prop="principalEmail">
            <el-input v-model="form.principalEmail" placeholder="请输入负责人email" style="width: 205px"/>
          </el-form-item>
          <el-form-item label="负责人QQ" prop="principalQq">
            <el-input v-model="form.principalQq" placeholder="请输入负责人QQ" />
          </el-form-item>
          <el-form-item label="招聘网址" prop="recruitmentWebsite">
            <el-input v-model="form.recruitmentWebsite" placeholder="请输入招聘网址或公司网址" style="width: 500px" />
          </el-form-item>
        </el-form>
      <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--审核对话框-->
    <el-dialog
      title="审批"
      :visible.sync="checkStatus"
      :width = 'def < 720 ? "520px" : "60%"'>
      <el-row>
        <el-form ref="form" :model="form">
          <el-form-item label="审批结果">
            <el-select v-model="form.status" placeholder="请选择审批结果" clearable size="small">
              <el-option
                v-for="dict in reviewStatusOption"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item abel="审批意见">
            <el-input type="textarea"  placeholder="请输入审批意见"></el-input>
          </el-form-item>
        </el-form>
      </el-row>
      <span slot="footer" class="dialog-footer">
    <el-button @click="checkCancel">取 消</el-button>
    <el-button type="primary" @click="checkSubmitForm">确 定</el-button>
  </span>
    </el-dialog>

  </div>
</template>

<script>
import { listInfo, getInfo, delInfo, addInfo, updateInfo, exportInfo } from "@/api/ims/enterprise/info";
import Editor from '@/components/Editor';

export default {
  name: "Info",
  components: { Editor },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //步骤条
      active: 1,
      labelPosition: 'right',
      //字典管理
      companyTypeOption: [],
      workingYearsOption: [],
      educationOption: [],
      companyIndustryOption: [],
      recruitStatusOption: [],
      statusOption: [],
      reviewStatusOption: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      /*下一步显示*/
      isNext: false,
      // 总条数
      total: 0,
      def:0,
      // 企业信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      checkStatus:false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        companyName: null,
        companyProfile: null,
        companyLocation: null,
        legalRepresentative: null,
        established: null,
        companyType: null,
        companyIndustry: null,
        companyPrincipal: null,
        principalPhone: null,
        principalEmail: null,
        principalQq: null,
        recruitStatus: null,
        recruitmentWebsite: null,
        status: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      postForm: {},
      // 表单校验
      rules: {
        companyName: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
        ],
        legalRepresentative: [
          { required: true, message: "法人代表不能为空", trigger: "blur" }
        ],
        companyLocation: [
          { required: true, message: "公司位置不能为空", trigger: "blur" }
        ],
        companyType: [
          { required: true, message: "公司性质不能为空", trigger: "blur" }
        ],
        companyPrincipal: [
          { required: true, message: "公司负责人不能为空", trigger: "blur" }
        ],
        postName: [
          { required: true, message: "岗位名称不能为空", trigger: "blur" }
        ],
        principalPhone: [
        {
          required:true,
          pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
          message: "请输入正确的手机号码",
          trigger: "blur"
        }
      ]
      }
    };
  },
  created() {
    this.getList();
    //字典管理
    this.getDicts("company_type").then(response => {
      this.companyTypeOption = response.data;
    });
    this.getDicts("working_years").then(response => {
      this.workingYearsOption = response.data;
    });
    this.getDicts("education").then(response => {
      this.educationOption = response.data;
    });
    this.getDicts("company_industry").then(response => {
      this.companyIndustryOption = response.data;
    });
    this.getDicts("recruitStatus").then(response => {
      this.recruitStatusOption = response.data;
    });
    //审核状态 0-审核中  1-审核通过
    this.getDicts("status").then(response => {
      this.statusOption = response.data;
    });
    //审批状态 0-退回 1-通过
    this.getDicts("review_status").then(response => {
      this.reviewStatusOption = response.data;
    });

  },
  methods: {
    /** 查询企业信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    //新增下一步
    next() {
      if (this.active++ > 2) this.active = 1;
    },
    //新增上一步
    previous(){
      if (this.active-- == 1) this.active = 1;
    },
    /** 审核按钮 */
    handleCheck(row){
      this.checkStatus = true;
      const companyId = row.companyId || this.ids
      getInfo(companyId).then(response => {
        this.form = response.data;
        this.form.status = null;
      });
    },
    /** 审核提交按钮 */
    checkSubmitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.companyId != null) {
            updateInfo(this.form).then(response => {
              this.msgSuccess("审核成功");
              this.open = false;
              this.checkStatus = false;
              this.getList();
            });
          }
        }
      });
    },

    // 字典状态字典翻译
    //公司性质
    companyTypeFormatter(row, column) {
      return this.selectDictLabel(this.companyTypeOption, row.companyType);
    },
    // 公司行业
    companyIndustryFormatter(row, column) {
      return this.selectDictLabel(this.companyIndustryOption, row.companyIndustry);
    },
    //招聘状态
    recruitStatusFormatter(row, column){
      return this.selectDictLabel(this.recruitStatusOption, row.recruitStatus);
    },
    statusFormatter(row, column){
      return this.selectDictLabel(this.statusOption, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.active = 1 ;
    },
    //审核取消按钮
    checkCancel(){
      this.checkStatus = false;
    },
    // 表单重置
    reset() {
      this.form = {
        companyId: null,
        companyName: null,
        companyProfile: null,
        companyLocation: null,
        legalRepresentative: null,
        established: null,
        companyType: null,
        companyIndustry: null,
        companyPrincipal: null,
        principalPhone: null,
        principalEmail: null,
        principalQq: null,
        recruitStatus: "0",
        delFlag: null,
        status: null,
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
      this.ids = selection.map(item => item.companyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      //this.$router.push('/add');
      this.title = "添加企业信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const companyId = row.companyId || this.ids
      getInfo(companyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改企业信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.companyId != null) {
            updateInfo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.checkStatus = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
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
      const companyIds = row.companyId || this.ids;
      this.$confirm('是否确认删除企业信息编号为"' + companyIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delInfo(companyIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有企业信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
