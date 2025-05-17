<template>
  <div class="app-container">
    <div v-show="showApplicationForm">
      <el-collapse-transition>
        <el-col>
          <el-card class="box-card mb20">
            <i class="el-icon-close pull-right" @click="cancel"></i>
            <h2 style="text-align: center">实习单位变更申请表</h2>
            <el-form ref="form" :model="form" :rules="rules" label-width="150px" inline>
              <el-form-item label="实习单位" prop="companyId">
                <el-select v-model="form.companyId" clearable allow-create filterable placeholder="请选择实习单位" style="width: 400px">
                  <el-option
                    v-for="o in companyInfoList"
                    :key="o.companyId"
                    :label="o.companyName"
                    :value="o.companyId"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="实习岗位" prop="practicePost">
                <el-input v-model="form.practicePost" placeholder="请输入实习岗位" style="width: 400px"/>
              </el-form-item>
              <el-form-item label="家长姓名" prop="parentName">
                <el-input v-model="form.parentName" placeholder="请输入家长姓名"style="width: 400px"/>
              </el-form-item>
              <el-form-item label="家长联系方式" prop="parentPhone">
                <el-input v-model="form.parentPhone" placeholder="请输入家长联系方式" style="width: 400px"/>
              </el-form-item>
              <el-form-item label="学校指导老师" prop="schoolInstructor">
                <el-input v-model="form.schoolInstructor" placeholder="请输入学校指导老师" style="width: 400px"/>
              </el-form-item>
              <el-form-item label="实习指导老师" prop="internInstructor">
                <el-input v-model="form.internInstructor" placeholder="请输入实习指导老师" style="width: 400px"/>
              </el-form-item>
              <el-form-item label="实习指导老师电话" prop="internPhone">
                <el-input v-model="form.internPhone" placeholder="请输入实习指导老师电话" style="width: 400px"/>
              </el-form-item>
              <el-form-item label="实习指导老师email" prop="internEmail">
                <el-input v-model="form.internEmail" placeholder="请输入实习指导老师email" style="width: 400px"/>
              </el-form-item>
              <el-form-item label="实习开始时间" prop="beginTime">
                <el-date-picker clearable size="small" style="width: 400px"
                                v-model="form.beginTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择实习开始时间">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="实习结束时间" prop="endTime">
                <el-date-picker clearable size="small" style="width: 400px"
                                v-model="form.endTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="选择实习结束时间">
                </el-date-picker>
              </el-form-item>
              <el-form-item label="实习类型" prop="practiceType">
                <el-select v-model="form.practiceType" placeholder="请选择实习类型" clearable style="width: 400px">
                  <el-option
                    v-for="dict in practiceTypeOption"
                    :key="dict.dictValue"
                    :label="dict.dictLabel"
                    :value="dict.dictValue"
                  ></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="实习描述" prop="practiceDesc">
                <el-input type="textarea" v-model="form.practiceDesc" placeholder="请输入实习描述" style="width: 400px" />
              </el-form-item>
            </el-form>
            <div style="margin-left: 540px">
              <el-button type="primary" @click="submitForm">提交</el-button>
              <el-button @click="cancel">取 消</el-button>
            </div>
          </el-card>
        </el-col>
      </el-collapse-transition>
    </div>
    <el-card class="border-radius8">
      <el-row v-show="showCard" class="text-center">
        <img :src="nodata.img" width="500px" height="500px">
      </el-row>
      <el-row  v-for="item in practiceList" :key="item.practiceId">
        <el-row>
          <el-col :span="6">
            <p>姓名</p>
            <span style="color: #909399">{{item.nickName}}</span>
          </el-col>
          <el-col :span="6">
            <p>实习单位</p>
            <span style="color: #909399">{{item.companyName}}</span>
          </el-col>
          <el-col :span="5">
            <p>学习指导老师</p>
            <span style="color: #909399">{{item.schoolInstructor}}</span>
          </el-col>
          <el-col :span="5">
            <p>实习指导老师</p>
            <span style="color: #909399">{{item.internInstructor}}</span>
          </el-col>
          <el-col :span="2" class="pull-right">
            <p>操作</p>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <p>实习开始时间</p>
            <span style="color: #909399">{{ parseTime(item.beginTime, '{y}-{m}-{d}') }}</span>
          </el-col>
          <el-col :span="6">
            <p>实习结束时间</p>
            <span style="color: #909399">{{ parseTime(item.endTime, '{y}-{m}-{d}') }}</span>
          </el-col>
          <el-col :span="5">
            <p>审核状态</p>
            <span style="color: #909399">{{selectDictLabel(statusOption, item.status)}}</span>
          </el-col>
          <el-col :span="5">
            <p>实习状态</p>
            <el-tag :type="formatTagType(item.practiceStatus)">{{selectDictLabel(practiceStatusOption, item.practiceStatus)}}</el-tag>
          </el-col>
          <el-col :span="2" style="vertical-align: center">
            <el-button
              type="text"
              icon="el-icon-edit-outline"
              @click="handleUpdate(item)"
            >变更</el-button>
          </el-col>
        </el-row>
        <el-divider></el-divider>
      </el-row>
      <el-row class="text-center">
        <pagination
          v-show="total>4"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getPracticeList"
        />
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {getPractice, addPractice, updatePractice} from "@/api/ims/practice/practice";
import {companyListInfo} from "../../../api/ims/enterprise/info";
import {personalListPractice} from "../../../api/ims/practice/practice";
import img from "../../../assets/image/noData.png"

export default {
  name: "Practice",
  data() {
    return {
      nodata:{img},
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      //实习单位变更申请表
      showApplicationForm:false,
      //
      showCard: false,
      // 总条数
      total: 0,
      // 实习信息表格数据
      practiceList: [],
      // 企业列表
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
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        companyId: [
          { required: true, message: "公司名称不能为空", trigger: "blur" }
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
    this.getPracticeList();
    //审核状态 0-审核中  1-审核通过
    this.getDicts("status").then(response => {
      this.statusOption = response.data;
    });
    //审批状态 0-退回 1-通过
    this.getDicts("review_status").then(response => {
      this.reviewStatusOption = response.data;
    });
    //实习状态 0-未实习 1-实习中 2-实习结束
    this.getDicts("practice_status").then(response => {
      this.practiceStatusOption = response.data;
    });
    //实习类型 0-统一实习 1-自主实习
    this.getDicts("practice_type").then(response => {
      this.practiceTypeOption = response.data;
    });
  },
  methods: {
    /** 查询实习中信息 */
    getPracticeList() {
      this.loading = true;
      let practiceStatus = '1';
      personalListPractice({practiceStatus}).then(response => {
        this.practiceList = response.rows;
        this.total = response.total;
        if (0 == this.total){
          this.showCard = true
        }
        this.loading = false;
      });
    },
    /** 招聘公司列表 */
    getCompanyList() {
      companyListInfo(this.queryParams).then(response => {
        this.companyInfoList = response.rows;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
      this.showApplicationForm = false;
    },
    //tag标签获取类型
    formatTagType(status) {
      if (status == '0') return 'info'
      else if (status == '1' ) return 'warning'
      else if (status == '2' ) return 'success'
    },
    // 表单重置
    reset() {
      this.form = {
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
    /** 实习状态 */
    practiceStatusFormatter(row, column){
      return this.selectDictLabel(this.practiceStatusOption, row.practiceStatus);
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
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getCompanyList();
      this.showApplicationForm = true;
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.form = response.data;
        this.form.companyName = null;
        this.form.companyId = null;
        this.form.schoolInstructor = null;
        this.form.internInstructor = null;
        this.form.internPhone= null;
        this.form.internEmail= null;
        this.form.beginTime= null;
        this.form.endTime= null;
        this.form.practiceDesc= null;
        this.form.practicePost= null;
        this.form.practiceType= null;
        this.form.practiceId = null;
        this.form.updateTime = null;
        this.form.updateBy = null;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.practiceId = null;
          if (this.form.practiceId != null) {
            updatePractice(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getPracticeList();
            });
          } else {
            addPractice(this.form).then(response => {
              this.msgSuccess("变更成功，待审核");
              this.open = false;
              this.getPracticeList();
            });
          }
        }
      });
    }
  }
};
</script>
