<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
      <el-row>
        <el-form-item label="标题" prop="weeklyTitle">
          <el-col>
            <el-input v-model="form.weeklyTitle" content="专业实习周记" style="width: 480%" placeholder="请输入标题"/>
          </el-col>
        </el-form-item>
        <el-row class="pull-right">
          <el-button plain type="success" class="ml5" icon="el-icon-check" @click="saveForm">保存草稿</el-button>
          <el-button plain type="danger"  icon="el-icon-s-promotion" @click="submitForm">提交</el-button>
        </el-row>
      </el-row>
      <el-row>
        <el-col :span="span">
          <el-form-item label="实习单位" prop="practiceId">
            <el-select v-model="form.practiceId" placeholder="请选择实习单位">
              <el-option
                v-for="dict in userListPractice"
                :key="dict.practiceId"
                :label="dict.companyName"
                :value="dict.practiceId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="span">
          <el-form-item label="开始时间" prop="beginTime">
            <el-date-picker clearable size="small" style="width: 200px"
                            v-model="form.beginTime"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="选择开始时间">
            </el-date-picker>
          </el-form-item>
        </el-col>
       <el-col :span="span">
         <el-form-item label="结束时间" prop="endTime">
           <el-date-picker clearable size="small" style="width: 200px"
                           v-model="form.endTime"
                           type="date"
                           value-format="yyyy-MM-dd"
                           placeholder="选择结束时间">
           </el-date-picker>
         </el-form-item>
       </el-col>
        <el-col :span="span" v-show="weekTimeShow">
          <el-form-item label="周次" prop="week" >
            <el-select v-model="form.week" :disabled="isAdd"  placeholder="请选择周次">
              <el-option
                v-for="dict in weekTimesOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="内容">
        <editor v-model="form.weeklyContent" :min-height="520"  style="width: 1150px" />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {addWeekly, updateWeekly, getWeekly} from "../../../../api/ims/practice/weekly";
import Editor from '@/components/Editor';
import {personalListPractice} from "../../../../api/ims/practice/practice";
import {listWeekly} from "../../../../api/ims/practice/weekly";

export default {
  name: "Weekly",
  components: { Editor },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 总条数
      total: 0,
      //栅格占据的列数
      span: 6,
      //周次显示
      weekTimeShow:true,
      //是否为新增跳转
      isAdd: null,
      //考核类型
      assessmenType:null,
      // 实习考核信息表格数据
      weeklyList: [],
      // 弹出层标题
      title: "",
      // 考核类型 0-实习周记 1-实习总结字典
      assessmentTypeOptions: [],
      //周次
      weekTimesOptions:[],
      //个人实习列表
      userListPractice:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        practiceId: null,
        assessmenType: null,
        weeklyTitle: null,
        weeklyContent: null,
        commentabled: null,
        beginTime: null,
        endTime: null,
        week: null,
        status: null,
        createDept: null,
      },
      // 表单参数
      form: {
        weeklyTitle: undefined,
      },
      // 表单校验
      rules: {
        weeklyTitle: [
          { required: true, message: "标题不能为空", trigger: "blur" }
        ],
        week: [
          { required: this.weekTimeShow, message: "周次不能为空", trigger: "blur" }
        ],
        practiceId: [
          { required: true, message: "实习单位不能为空", trigger: "blur" }
        ],
        weeklyContent: [
          { required: true, message: "内容不能为空", trigger: "blur" }
        ],
        beginTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getAssessmentType();
    this.getPersonalListPractice();
    this.getDicts("实习考核类型").then(response => {
      this.assessmentTypeOptions = response.data;
    });
    //实习周次
    this.getDicts("week_times").then(response => {
      this.weekTimesOptions = response.data;
    });
  },
  methods: {
    /** 查询实习考核信息列表 */
    getAssessmentType() {
      this.loading = true;
      this.assessmenType = this.$route.query.assessmenType;
      if (this.assessmenType == 1){
        this.form.weeklyTitle = '实习总结'
        this.weekTimeShow = false;
        this.span = 12;
      }else {
        this.form.weeklyTitle = '专业实习周记'
      }
      const weeklyId = this.$route.query.weeklyId;
      if (weeklyId != null){
        getWeekly(weeklyId).then(response => {
          this.form = response.data;
          this.isAdd = true;
        });
      }else {
        this.isAdd = false;
      }
    },
    /** 查询个人实习列表 */
    getPersonalListPractice(){
      personalListPractice().then(res =>{
        this.userListPractice = res.rows;
      })
    },
    // 考核类型 0-实习周记 1-实习总结字典翻译
    assessmenTypeFormat(row, column) {
      return this.selectDictLabel(this.assessmenTypeOptions, row.assessmenType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        weeklyId: null,
        userId: null,
        practiceId: null,
        assessmenType: null,
        weeklyTitle: null,
        weeklyContent: null,
        commentabled: null,
        beginTime: null,
        endTime: null,
        week: null,
        delFlag: null,
        status: "0",
        createDept: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 提交按钮 */
    submitForm() {
      this.form.assessmenType = this.assessmenType;
      this.form.published = "1";
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.addWeek();
        }
      });
    },
    /** 保存按钮 */
    saveForm(){
      this.form.assessmenType = this.assessmenType;
      this.form.published = "0";
      console.log(this.isAdd)
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.addWeek();
        }
      });
    },
    addWeek(){
      const assessmenType = this.assessmenType;
      if (assessmenType == 1) {
        this.form.week = '99'
      }
      if (this.isAdd){
        if (this.form.weeklyId != null) {
          updateWeekly(this.form).then(response => {
            this.msgSuccess("修改成功");
            this.$router.go(-1);
          });
        }
      }else {
        if (this.form.week != null){
          let flag = false;
          const week = this.form.week;
          listWeekly({assessmenType,week}).then(response => {
            if (0 < response.total){
              for (let i = 0; i < response.total; i++){
                if (response.rows[i].week === week){
                  if (assessmenType == 1){
                    this.msgInfo("实习总结已存在");
                    flag = true;
                  }else if(assessmenType == 0){
                    this.msgError(this.selectDictLabel(this.weekTimesOptions, week)+"的周记已存在");
                    flag = true;
                  }
                  if (flag == true){
                    break;
                  }
                }
              }
              if (flag !== true) {
                addWeekly(this.form).then(response => {
                  this.msgSuccess("新增成功");
                  this.$router.go(-1);
                });
              }
            }else {
              addWeekly(this.form).then(response => {
                this.msgSuccess("新增成功");
                this.$router.go(-1);
              });
            }
          });
        }
      }
    }
  }
};
</script>
