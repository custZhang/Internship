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
      <el-form-item label="工作年限" prop="postExperience">
        <el-select v-model="queryParams.postExperience" clearable placeholder="请输入工作年限">
          <el-option
            v-for="dict in workingYearsOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学历要求" prop="education">
        <el-select v-model="queryParams.education" clearable placeholder="请输入学历要求">
          <el-option
            v-for="dict in educationOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
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
        <el-select v-model="queryParams.companyIndustry"  placeholder="请输入公司行业" clearable size="small">
          <el-option
            v-for="dict in companyIndustryOption"
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
    <div>
      <el-row>
        <el-col :span="8" v-for="o in recList" :key="o.postId">
          <div style="margin-top:15px" @click="enterpriseInfo(o.postId)">
            <el-card class="box-card border-radius8" style="height: 180px">
              <div slot="header" class="clearfix">
                <span style="color: #303133;font-weight: bolder">{{o.postName}}</span>
                <el-divider direction="vertical"></el-divider>
                <span style="color: #909399">{{parseTime(o.updateTime, '{y}-{m}-{d}') }}</span>
                <el-button style="float: right; padding: 3px 0" type="text">{{o.companyName}}</el-button>
              </div>
              <el-row class="row-text-color">
                <el-col :span="10">
                  <span style="color: #ff4949">{{o.postWage}}</span>
                </el-col>
                <el-col :span="14">
                  <span class="pull-right">{{o.companyLocation}}</span>
                </el-col>
              </el-row>
              <el-row class="mt20 row-text-color">
                <el-col :span="10">
                  <span>{{selectDictLabel(workingYearsOption, o.postExperience)}}</span>
                </el-col>
                <el-col :span="7">
                  <span>{{selectDictLabel(educationOption, o.education)}}</span>
                </el-col>
                <el-col :span="7">
                  <span class="pull-right">招<span style="color: #1e1e1e">{{o.postNumber}}</span>人</span><br>
                </el-col>
              </el-row>
              <el-row class="mt10" style="display: inline-block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;max-width: 380px;color: #909399">
                <span v-html="o.requirements"></span>
              </el-row>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>

    <pagination
      v-show="total>6"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getRecList"
    />
  </div>
</template>

<script>
import { listRec } from "@/api/ims/enterprise/post";

export default {
  name: "Post",
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
      //字典管理
      educationOption: [],
      companyTypeOption: [],
      companyIndustryOption: [],
      workingYearsOption: [],
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      //招聘信息数据
      recList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        companyName: null,
        requirements: null,
        companyType: null,
        education: null,
        companyIndustry: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getRecList();
    this.getDicts("working_years").then(response => {
      this.workingYearsOption = response.data;
    });
    this.getDicts("education").then(response => {
      this.educationOption = response.data;
    });
    this.getDicts("company_type").then(response => {
      this.companyTypeOption = response.data;
    });
    this.getDicts("company_industry").then(response => {
      this.companyIndustryOption = response.data;
    });
    this.getDicts("working_years").then(response => {
      this.workingYearsOption = response.data;
    });
  },
  methods: {
    /** 查询企业招聘岗位信息列表 */
    getRecList() {
      this.loading = true;
      listRec(this.queryParams).then(response => {
        this.recList = response.rows;
        this.total = response.total;
        this.loading = false;
        console.log(response.rows);
      });
    },
    educationFormatter(row, column) {
      return this.selectDictLabel(this.educationOption, row.education);
    },
    //跳转到具体页面信息
    enterpriseInfo(postId){
      this.$router.push({
        path: '/corporateInfo',
        query: {
          id: postId
        }
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
      this.getRecList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
  }
};
</script>

<style>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both
}

.box-card {
  width: 420px;
}
.row-text-color{
  color: #909399;
}

</style>
