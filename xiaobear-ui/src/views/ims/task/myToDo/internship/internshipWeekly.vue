<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" >
      <el-form-item label="学年" prop="academicYear">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.academicYear"
                        type="year"
                        placeholder="选择学年">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="周次" prop="week" label-width="68px">
        <el-select v-model="queryParams.week" placeholder="请选择周次" clearable size="small">
          <el-option
            v-for="dict in weekTimesOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
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
  <el-table v-loading="loading" :data="taskChildList" @selection-change="handleSelectionChange">
    <el-table-column label="学年" align="center" prop="academicYear" />
    <el-table-column label="学期" align="center" prop="semester" :formatter="semesterFormat"/>
    <el-table-column label="姓名" align="center" prop="nickName" />
    <el-table-column label="类型" align="center" prop="assessmentType" :formatter="assessmenTypeFormat"/>
    <el-table-column label="周次" align="center" prop="week" :formatter="weekTimesFormat"/>
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
  <el-dialog title="周记详情" :visible.sync="weekDetail" width="888px" >
      <span ref="weeklyInfo" :model="weeklyInfo">
        <el-row>
          <el-col >
            <h2 class="text-center">{{weeklyInfo.weeklyTitle}}({{selectDictLabel(weekTimesOptions, weeklyInfo.week)}})</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="font-bolder">姓名：{{weeklyInfo.nickName}}</el-col>
          <el-col :span="8" class="font-bolder">学号：{{weeklyInfo.userName}}</el-col>
          <el-col :span="8" class="font-bolder">班级：{{weeklyInfo.deptName}}</el-col>
        </el-row>
        <el-row>
          <el-col class="p-line-height">
            <p v-html="weeklyInfo.weeklyContent"></p>
          </el-col>
        </el-row>
        <el-row>
           <el-col v-if="weeklyInfo.commentabled !== null && weeklyInfo.commentabled !== undefined && weeklyInfo.commentabled.length !== 0">
            <h4>辅导员的评价</h4>
            <span>{{weeklyInfo.commentabled}}</span>
          </el-col>
        </el-row>
         <el-row>
          <el-col v-if=" weeklyInfo.commentabled == null || weeklyInfo.commentabled.length == 0 || weeklyInfo.commentabled == undefined">
            <el-input type="textarea" v-model="commentabled" placeholder="请输入评价内容"/>
          </el-col>
         </el-row>
          <el-row class="text-center mt10">
            <el-button type="primary" v-show="isBlankValue" size="medium" @click="detailSubmitForm">提交</el-button>
            <el-button @click="cancel">取 消</el-button>
          </el-row>
      </span>
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
import {listChildPractice} from "../../../../../api/ims/task/child";
import {getWeekly, updateWeekly} from "../../../../../api/ims/practice/weekly";
import {addApproval} from "../../../../../api/ims/task/approval";
import {getUser, getUserProfile} from "../../../../../api/system/user";
import {listUserNotice} from "../../../../../api/system/userNotice";
export default {
  name: "internshipWeekly",
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
      weekDetail: false,
      // 是否显示弹出层
      checkStatus:false,
      //评论是否有值
      isBlankValue: true,
      def: 0,
      // 总条数
      total: 0,
      // 任务信息表格数据
      taskChildList: [],
      // 弹出层标题
      title: "",
      //评论
      commentabled: "",
      // 下一操作人
      nextUser: null,
      //学期
      semesterOptions: [],
      //周次
      weekTimesOptions:[],
      //考核类型
      assessmenTypeOptions: [],
      //审批状态
      reviewStatusOption: [],
      // 子任务状态字典
      childTaskStatusOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        assessmenType: '0',
        childProcessType: 'I01',
        nickname: null,
        academicYear: null,
        week: null,
        nextUser: null,
      },
      // 表单参数
      form: {},
      // 表单参数
      weeklyInfo: {},
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
    this.getDicts("assessmen_type").then(response => {
      this.assessmenTypeOptions = response.data;
    });
    //实习周次
    this.getDicts("week_times").then(response => {
      this.weekTimesOptions = response.data;
    });
    this.getDicts("semester").then(response => {
      this.semesterOptions = response.data;
    });

  },
  methods: {
    /** 查询任务信息列表 */
    getList() {
      this.loading = true;
      getUserProfile().then(response => {
        this.nextUser = response.data.userId;
        this.queryParams.nextUser = this.nextUser;
        listChildPractice(this.queryParams).then(response => {
          this.taskChildList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
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
      const assessmenType = '0';
      const nextUser = this.nextUser;
      console.log(nextUser)
      listChildPractice({childTaskProcess,assessmenType,nextUser}).then(response => {
        this.taskChildList = response.rows;
        console.log(this.taskChildList)
        this.total = response.total;
        this.loading = false;
      });
    },
    // 子任务状态字典翻译
    childTaskStatusFormat(row, column) {
      return this.selectDictLabel(this.childTaskStatusOptions, row.childTaskStatus);
    },
    // 考核类型 0-实习周记 1-实习总结字典翻译
    assessmenTypeFormat(row, column) {
      return this.selectDictLabel(this.assessmenTypeOptions, row.assessmenType);
    },
    weekTimesFormat(row, column){
      return this.selectDictLabel(this.weekTimesOptions, row.week);
    },
    // 学期字典翻译
    semesterFormat(row, column) {
      return this.selectDictLabel(this.semesterOptions, row.semester);
    },
    // 取消按钮
    cancel() {
      this.weekDetail = false;
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
      this.ids = selection.map(item => item.taskId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 详情按钮 */
    handleDetail(row){
      this.reset();
      const weeklyId = row.weeklyId || this.ids
      getWeekly(weeklyId).then(response => {
        this.weeklyInfo = response.data;
        this.weekDetail = true;
        if (this.weeklyInfo.commentabled !== null && this.weeklyInfo.commentabled !== undefined && this.weeklyInfo.commentabled.length !==0){
          this.isBlankValue = false;
        }
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
    /** 评论提交按钮 */
    detailSubmitForm() {
      console.log(this.weeklyInfo.weeklyId)
      if (this.weeklyInfo.weeklyId != null) {
        this.weeklyInfo.commentabled = this.commentabled;
        updateWeekly(this.weeklyInfo).then(response => {
          this.msgSuccess("评价成功");
          this.commentabled = '';
        });
      }
    },
  }
  };
</script>
<style lang="scss" scoped>

</style>

