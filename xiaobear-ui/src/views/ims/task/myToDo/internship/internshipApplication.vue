<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
      <el-form-item label="学年" prop="academicYear">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.academicYear"
                        type="year"
                        placeholder="选择学年">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实习类型" prop="practiceType" label-width="68px">
        <el-select v-model="queryParams.practiceType" placeholder="请选择实习类型" clearable mini>
          <el-option
            v-for="dict in practiceTypeOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="姓名" prop="nickName" label-width="68px">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入姓名"
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
      <el-radio-group v-model="radio" size="medium" class="ml5" @change="handleChange">
      <el-radio-button label="全部"></el-radio-button>
      <el-radio-button label="待审核"></el-radio-button>
      <el-radio-button label="已完成"></el-radio-button>
      </el-radio-group>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" class="mr5"></right-toolbar>
    </el-row>
  <el-table v-loading="loading" :data="practiceList" @selection-change="handleSelectionChange">
    <el-table-column label="学年" align="center" prop="academicYear" />
    <el-table-column label="学期" align="center" prop="semester" :formatter="semesterFormat"/>
    <el-table-column label="姓名" align="center" prop="nickName" />
    <el-table-column label="实习类型" align="center" prop="practiceType" :formatter="practiceTypeFormat"/>
    <el-table-column label="实习单位" align="center" prop="companyName" />
    <el-table-column label="实习岗位" align="center" prop="practicePost" />
    <el-table-column label="状态" align="center" prop="childTaskStatus">
      <template slot-scope="scope">
        <el-tag :type="formatTagType(scope.row.childTaskStatus)">{{selectDictLabel(childTaskStatusOptions, scope.row.childTaskStatus)}}</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="申请时间" align="center" prop="createTime" />
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
      <template slot-scope="scope">
        <el-button
          size="mini"
          type="text"
          icon="el-icon-view"
          @click="handleDetail(scope.row)"
        >查看</el-button>
        <el-button
          size="mini"
          style="color: #1ab394"
          type="text"
          icon="el-icon-s-check"
          @click="handleCheck(scope.row)"
          v-if="scope.row.childTaskStatus == 'Y04' ? false : true"
        >审核</el-button>
      </template>
    </el-table-column>
  </el-table>

  <pagination
    v-show="total>5"
    :total="total"
    :page.sync="queryParams.pageNum"
    :limit.sync="queryParams.pageSize"
    @pagination="getList"
  />
  <!--详情对话框-->
    <el-dialog :title="title" :visible.sync="open"  width="1000px" append-to-body>
      <el-card class="box-card" shadow="never" :data="practiceInfo">
        <el-row>
          <el-col :span="18">
            <span style="font-size: 16px;font-weight: bolder;color: #F56C6C">{{practiceInfo.remark}}</span>
          </el-col>
          <el-col :span="6">
            <el-tag type="success">{{selectDictLabel(practiceTypeOption, practiceInfo.practiceType)}}</el-tag>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="8">
            <span class="text-color">实习时间：</span>
            <span>{{practiceInfo.beginTime}}至{{practiceInfo.endTime}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习单位：</span>
            <span >{{practiceInfo.companyName}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习岗位：</span>
            <span >{{practiceInfo.practicePost}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="8">
            <span class="text-color">实习指导老师：</span>
            <span>{{practiceInfo.internInstructor}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习老师联系方式：</span>
            <span>{{practiceInfo.internPhone}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习老师Email：</span>
            <span>{{practiceInfo.internEmail}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="8">
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
  <!--审核对话框-->
  <el-dialog title="审批" :visible.sync="checkStatus" :width = 'def < 720 ? "520px" : "40%"'>
    <el-row>
      <el-form ref="form" label-width="80px" :model="form">
        <el-form-item label="审批结果">
          <el-select v-model="form.approvalConclusion" placeholder="请选择审批结果" clearable style="width: 80%" size="small">
            <el-option
              v-for="dict in reviewStatusOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="批注">
          <el-input type="textarea" v-model="form.approvalOpinions" placeholder="请输入审批意见" style="width: 80%"></el-input>
        </el-form-item>
      </el-form>
    </el-row>
    <span slot="footer" class="dialog-footer">
    <el-button @click="cancel">取 消</el-button>
    <el-button type="primary" plain @click="checkSubmitForm">确 定</el-button>
  </span>
  </el-dialog>
  </div>
</template>

<script>
  import {getChild, listChildPractice, listPracticeApplication, updateChild} from "../../../../../api/ims/task/child";
  import {getWeekly, updateWeekly} from "../../../../../api/ims/practice/weekly";
  import {addApproval, updateApproval} from "../../../../../api/ims/task/approval";
  import {getUser} from "../../../../../api/system/user";
  import {getPractice} from "../../../../../api/ims/practice/practice";
export default {
  name: "internshipApplication",
  data() {
    return {
      radio:"全部",
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
      // 是否显示弹出层
      checkStatus:false,
      // 是否显示弹出层
      open: false,
      def: 0,
      // 总条数
      total: 0,
      // 任务信息表格数据
      practiceList: [],
      // 弹出层标题
      title: "",
      //学期
      semesterOptions: [],
      //实习类型
      practiceTypeOption: [],
      //审批状态
      reviewStatusOption: [],
      // 子任务状态字典
      childTaskStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        nickName: null,
        academicYear: null,
        semester: null,
        practiceType: null,
      },
      // 表单参数
      form: {},
      practiceInfo: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("stage").then(response => {
      this.childTaskStatusOptions = response.data;
    });
    //审批状态 0-退回 1-通过
    this.getDicts("review_status").then(response => {
      this.reviewStatusOption = response.data;
    });
    //实习类型 0-统一实习 1-自主实习
    this.getDicts("practice_type").then(response => {
      this.practiceTypeOption = response.data;
    });
    this.getDicts("semester").then(response => {
      this.semesterOptions = response.data;
    });
  },
  methods: {
    /** 查询任务信息列表 */
    getList() {
      this.loading = true;
      listPracticeApplication(this.queryParams).then(response => {
        this.practiceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    //tag标签获取类型
    formatTagType(status) {
      if (status == 'Y01') return ''
      else if (status == 'Y04' ) return 'success'
    },
    /** 单选框值发生变化 */
    handleChange(){
      let childTaskProcess;
      if (this.radio == '已完成') {
        childTaskProcess = '02';
      }
      if (this.radio == '全部') {
        childTaskProcess = null;
      }
      if (this.radio == '待审核') {
        childTaskProcess = '01';
      }
      this.loading = true;
      listPracticeApplication({childTaskProcess}).then(response => {
        this.practiceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 子任务状态字典翻译
    childTaskStatusFormat(row, column) {
      return this.selectDictLabel(this.childTaskStatusOptions, row.childTaskStatus);
    },
    // 考核类型 0-实习周记 1-实习总结字典翻译
    practiceTypeFormat(row, column) {
      return this.selectDictLabel(this.practiceTypeOption, row.practiceType);
    },
    // 学期字典翻译
    semesterFormat(row, column) {
      return this.selectDictLabel(this.semesterOptions, row.semester);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.checkStatus = false;
    },
    // 表单重置
    reset() {
      this.form = {
        taskId: null,
        taskName: null,
        processType: null,
        academicYear: null,
        semester: null,
        endTime: null,
        taskProgress: null,
        delFlag: null,
        status: null
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
      this.ids = selection.map(item => item.taskId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 详情按钮 */
    handleDetail(row){
      this.reset();
      const practiceId = row.practiceId || this.ids
      const taskName = row.taskName;
      getPractice(practiceId).then(response => {
        this.form = response.data;
        this.practiceInfo = response.data;
        this.practiceInfo.remark = taskName;
        this.title = '实习详情';
        this.open = true;
      });
    },
    /** 审核按钮 */
    handleCheck(row){
      this.checkStatus = true;
      const userId = row.nextUser;
      this.form.userId = userId;
      this.form.taskStatus = row.childTaskStatus;
      this.form.childId = row.childId;
      getUser(userId).then(response => {
        console.log(response.data)
        this.form.roleId = response.data.roleId;
        this.form.deptId = response.data.deptId;
        this.form.createDept = response.data.dept.deptName;
      });
      console.log(this.form)
    },
    /** 审核按钮提交 */
    checkSubmitForm(){
      this.$refs["form"].validate(valid => {
        if (valid) {
          addApproval(this.form).then(response => {
            this.msgSuccess("审批成功");
            this.checkStatus = false;
            this.getList();
          });
        }
      });
    },
  }
  };
</script>


