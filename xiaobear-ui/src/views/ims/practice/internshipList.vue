<template>
  <div class="app-container">
    <el-row v-show="showStep">
      <el-col class="mb20">
        <el-card class="box-card">
          <i class="el-icon-close pull-right" @click="cancel"></i>
          <h3 style="text-align: center;color: #ff4949">{{this.queryParams.companyName}}实习状态</h3>
          <el-steps :active="active" process-status="success" >
            <el-step title="实习申请" icon="el-icon-edit"></el-step>
            <el-step title="待审核" icon="el-icon-s-check"></el-step>
            <el-step title="审核已通过" icon="el-icon-s-check"></el-step>
            <el-step title="实习中" icon="el-icon-loading"></el-step>
            <el-step title="实习结束" icon="el-icon-success"></el-step>
          </el-steps>
        </el-card>
      </el-col>
    </el-row>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="用户名" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入用户名"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实习单位" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入实习单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="学校指导老师" prop="schoolInstructor">
        <el-input
          v-model="queryParams.schoolInstructor"
          placeholder="请输入学校指导老师"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实习指导老师" prop="internInstructor">
        <el-input
          v-model="queryParams.internInstructor"
          placeholder="请输入实习指导老师"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择审核状态" clearable size="small">
          <el-option
            v-for="dict in statusOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="实习状态" prop="practiceStatus">
        <el-select v-model="queryParams.practiceStatus" placeholder="请选择实习状态" clearable size="small">
          <el-option
            v-for="dict in practiceStatusOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="实习类型" prop="createDept">
        <el-select v-model="form.practiceType" placeholder="请选择实习类型" clearable mini>
          <el-option
            v-for="dict in practiceTypeOption"
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
          type="info"
          icon="el-icon-view"
          size="mini"
          :disabled="single"
          @click="handleDetails"
        >详情</el-button>
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

    <el-table v-loading="loading" :data="practiceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="姓名" align="center" prop="nickName" />
      <el-table-column label="实习单位" align="center" prop="companyName" width="200px"/>
      <el-table-column label="学校指导老师" align="center" prop="schoolInstructor" />
      <el-table-column label="实习指导老师" align="center" prop="internInstructor" />
      <el-table-column label="实习指导老师电话" align="center" prop="internPhone" width="160px" />
      <el-table-column label="实习开始时间" align="center" prop="beginTime" width="140">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实习结束时间" align="center" prop="endTime" width="140">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="status" :formatter="statusFormatter"/>
      <el-table-column label="实习状态" align="center" prop="practiceStatus">
        <template slot-scope="scope">
          <el-tag :type="formatTagType(scope.row.practiceStatus)" size="small">{{selectDictLabel(practiceStatusOption, scope.row.practiceStatus)}}</el-tag>
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
    <el-dialog :title="title" :visible.sync="open"  width="800px" append-to-body>
      <el-form :span="4" ref="form" :model="form" :rules="rules" label-width="150px" inline>
        <el-form-item label="实习单位" prop="companyId">
          <el-select v-model="form.companyId" clearable allow-create filterable placeholder="请选择实习单位" style="width: 560px">
            <el-option
              v-for="o in companyInfoList"
              :key="o.companyId"
              :label="o.companyName"
              :value="o.companyId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实习岗位" prop="practicePost">
          <el-input v-model="form.practicePost" placeholder="请输入实习岗位" style="width: 200px" />
        </el-form-item>
        <el-form-item label="家长姓名" prop="parentName">
          <el-input v-model="form.parentName" placeholder="请输入家长姓名" style="width: 200px"/>
        </el-form-item>
        <el-form-item label="家长联系方式" prop="parentPhone">
          <el-input v-model="form.parentPhone" placeholder="请输入家长联系方式" style="width: 200px" />
        </el-form-item>
        <el-form-item label="学校指导老师" prop="schoolInstructor">
          <el-input v-model="form.schoolInstructor" placeholder="请输入学校指导老师" style="width: 200px" />
        </el-form-item>
        <el-form-item label="实习指导老师" prop="internInstructor">
          <el-input v-model="form.internInstructor" placeholder="请输入实习指导老师" style="width: 200px" />
        </el-form-item>
        <el-form-item label="实习指导老师电话" prop="internPhone">
          <el-input v-model="form.internPhone" placeholder="请输入实习指导老师电话" style="width: 200px" />
        </el-form-item>
        <el-form-item label="实习指导老师email" prop="internEmail">
          <el-input v-model="form.internEmail" placeholder="请输入实习指导老师email" style="width: 200px" />
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
        <el-form-item label="实习类型" prop="practiceType">
          <el-select v-model="form.practiceType" placeholder="请选择实习类型" clearable mini>
            <el-option
              v-for="dict in practiceTypeOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <br>
        <el-form-item label="实习描述" prop="practiceDesc">
          <el-input type="textarea" v-model="form.practiceDesc" placeholder="请输入实习描述" style="width: 560px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!---->
    <el-dialog :title="title" :visible.sync="openDetail"  width="1000px" append-to-body>
      <el-card class="box-card" shadow="never" :data="practiceInfo">
        <el-row>
          <el-col :span="6">
            <el-tag type="success">{{selectDictLabel(practiceTypeOption, practiceInfo.practiceType)}}</el-tag>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">实习时间：</span>
            <span>{{practiceInfo.beginTime}}至{{practiceInfo.endTime}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习单位：</span>
            <span >{{practiceInfo.companyName}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">实习岗位：</span>
            <span >{{practiceInfo.practicePost}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">实习指导老师：</span>
            <span>{{practiceInfo.internInstructor}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习老师联系方式：</span>
            <span>{{practiceInfo.internPhone}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">实习老师Email：</span>
            <span>{{practiceInfo.internEmail}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">学校指导老师：</span>
            <span>{{practiceInfo.schoolInstructor}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">家长姓名：</span>
            <span>{{practiceInfo.parentName}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">家长联系方式：</span>
            <span>{{practiceInfo.parentPhone}}</span>
          </el-col>
        </el-row>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import { listPractice, getPractice, delPractice, addPractice, updatePractice, exportPractice } from "@/api/ims/practice/practice";
import {getInfo, updateInfo} from "@/api/ims/enterprise/info";
import {companyListInfo} from "../../../api/ims/enterprise/info";

export default {
  name: "Practice",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //标签样式
      tagType: '',
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 显示步骤条
      showStep: false,
      // 总条数
      total: 0,
      def:0,
      active: 0,
      // 实习信息表格数据
      practiceList: [],
      // 公司列表
      companyInfoList: [],
      //字典管理
      statusOption:[],
      reviewStatusOption: [],
      practiceStatusOption:[],
      practiceTypeOption:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层
      openDetail: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        nickName:null,
        companyId: null,
        companyName: null,
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
      // 表单参数
      practiceInfo: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        companyName: [
          { required: true, message: "实习单位不能为空", trigger: "blur" }
        ],
        schoolInstructor: [
          { required: true, message: "学校指导老师不能为空", trigger: "blur" }
        ],
        internInstructor: [
          { required: true, message: "实习指导老师不能为空", trigger: "blur" }
        ],
        internPhone: [
          {
            required:true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        parentName:[
          { required: true, message: "家长姓名不能为空", trigger: "blur" }
        ],
        parentPhone: [
          {
            required:true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        beginTime: [
          { required: true, message: "实习开始时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    //审核状态 0-审核中  1-审核通过
    this.getDicts("status").then(response => {
      this.statusOption = response.data;
    });
    //实习类型 0-统一实习 1-自主实习
    this.getDicts("practice_type").then(response => {
      this.practiceTypeOption = response.data;
    });
    //审批状态 0-退回 1-通过
    this.getDicts("review_status").then(response => {
      this.reviewStatusOption = response.data;
    });
    //实习状态 0-未实习 1-实习中 2-实习结束
    this.getDicts("practice_status").then(response => {
      this.practiceStatusOption = response.data;
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
    /** 招聘公司列表 */
    getCompanyList() {
      companyListInfo(this.queryParams).then(response => {
        this.companyInfoList = response.rows;
      });
    },
    //tag标签获取类型
    formatTagType(status) {
      if (status == '0') return 'info'
      else if (status == '1' ) return 'warning'
      else if (status == '2' ) return 'success'
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
        parentName: null,
        parentPhone: null,
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
    /** 实习状态 */
    practiceStatusFormatter(row, column){
      return this.selectDictLabel(this.practiceStatusOption, row.practiceStatus);
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
    /** 详情按钮 */
    handleDetails(row){
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.practiceInfo = response.data;
        this.openDetail = true;
        this.title = "实习详情";
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getCompanyList();
      this.open = true;
      this.title = "添加实习信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getCompanyList();
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.form = response.data;
        /** 审核通过后不可以修改 */
        if (this.form.status == 1){
          this.msgInfo("你的实习申请已审核，不能进行修改！");
        }else {
          this.title = "修改实习信息";
          this.open = true;
        }
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
      practiceIds.forEach(practiceId => {
        getPractice(practiceId).then(response => {
          this.form = response.data;
          /** 审核通过后不可以删除 */
          if (this.form.status == 1){
            this.msgError("你的实习申请已审核，不能删除！");
          }else {
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
          }
        });
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
