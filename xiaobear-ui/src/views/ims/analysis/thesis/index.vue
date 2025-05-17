<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :rules="rules" label-width="68px" inline>
      <el-form-item label="学年" prop="academicYear">
        <el-date-picker clearable size="small" style="width: 200px"
                        v-model="queryParams.academicYear"
                        type="year"
                        value-format="yyyy"
                        placeholder="选择学年">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="学院" prop="deptId">
        <el-select v-model="queryParams.deptId" placeholder="请选择学院" @change="isSelectDept" clearable size="small">
          <el-option
            v-for="item in CollegeList"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="专业" prop="professionDeptId">
        <el-select v-model="queryParams.professionDeptId" placeholder="请选择专业" @change="isSelectProfessionDept" clearable size="small">
          <el-option
            v-for="item in ProfessionList"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="班级" prop="classDeptId">
        <el-select v-model="queryParams.classDeptId" placeholder="请选择班级" clearable size="small">
          <el-option
            v-for="item in ClassList"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptId"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!--标签页切换-->
    <el-tabs v-model="activeName" :stretch="true" @tab-click="handleClick">
      <el-tab-pane label="学生选题统计" name="1">
        <thesis-topic-statistics ref="thesisData" v-if="activeName === '1'" />
        </el-tab-pane>
      <el-tab-pane label="学生开题统计" name="2">
        <thesis-open-statistics ref="thesisData" v-if="activeName === '2'"/>
      </el-tab-pane>
      <el-tab-pane label="实习论文统计" name="3">
        <thesis-paper-statistics ref="thesisData" v-if="activeName === '3'" />
      </el-tab-pane>
<!--      <el-tab-pane label="论文分数统计" name="4">
        <intern-ware-statistics ref="wareData" v-if="activeName === '4'"/>
      </el-tab-pane>-->
    </el-tabs>
  </div>

</template>

<script>
import {listDept} from "@/api/system/dept";
import {childrenDeptList} from "@/api/system/dept";
import internshipBarChart from "../components/chart/barChart";
import internshipPieChart from "../components/chart/pieChart";
import internshipPlateGroup from "../components/plateGroup";
import InternStatistics from "../practice/internStatistics";
import {
  practiceAnalysis,
  practiceSummaryAnalysis, practiceWareAnalysis,
  practiceWeeklyAnalysis
} from "../../../../api/ims/analysis/analysisPractice";
import InternWeeklyStatistics from "../practice/internWeeklyStatistics";
import InternSummaryStatistics from "../practice/internSummaryStatistics";
import InternSalaryStatistics from "../practice/internSalaryStatistics";
import InternWareStatistics from "../practice/internWareStatistics";
import ThesisTopicStatistics from "./thesisTopicStatistics";
import {thesisTopicAnalysis} from "../../../../api/ims/analysis/analysisThesis";
import ThesisOpenStatistics from "./thesisOpenStatistics";
import ThesisPaperStatistics from "./thesisPaperStatistics";

export default {
  name: "practice",
  components: {
    ThesisPaperStatistics,
    ThesisOpenStatistics,
    ThesisTopicStatistics,
    internshipPlateGroup,
    internshipPieChart,
    internshipBarChart
  },
  data() {
    return {
      activeName: '1',
      practice: 'practice',
      week: 'week',
      chart: null,
      loading: false,
      //统计人数内容
      analysisMap: {},
      //统计周记内容
      analysisWeeklyMap: {},
      //统计总结内容
      analysisSummaryMap: {},
      //统计总结内容
      analysisWareMap: {},
      //周次
      weekTimesOptions:[],
      //院系列表
      CollegeList: [],
      //专业列表
      ProfessionList: [],
      //班级列表
      ClassList:[],
      //登录用户信息
      user: null,
      //论文状态
      topicStatus: '1',
      // 查询参数
      queryParams: {
        deptType: null,
        //查询参数
        deptId: null,
        academicYear: null,
        professionDeptId: null,
        classDeptId: null,
        week: null,
      },
      data: [],
      // 表单校验
      rules: {
        academicYear: [
          { required: true, message: "年份不能为空", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "学院不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getCollegeList();
    this.getThesisTopicAnalysis();
    //实习周次
    this.getDicts("week_times").then(response => {
      this.weekTimesOptions = response.data;
    });
  },
  methods:{
    /** 查询学生开题人数相关分析 */
    getThesisTopicAnalysis(){
      let deptId;
      let deptType;
      let academicYear = new Date().getFullYear();
      if (this.queryParams.deptId !== null || this.queryParams.professionDeptId !== null || this.queryParams.classDeptId !== null){
        deptId = this.isSelectDeptQuery().get("deptId")
        deptType = this.isSelectDeptQuery().get("deptType");
      }
      if (this.queryParams.academicYear !== null || this.queryParams.academicYear !== "" || this.queryParams.academicYear !== undefined){
        academicYear = this.queryParams.academicYear;
      }
      console.log(this.queryParams)
      const topicStatus = this.topicStatus
      thesisTopicAnalysis({deptId,academicYear,deptType,topicStatus}).then(response =>{
        this.analysisMap = response.data;
        console.log(response.data)
        this.$refs.thesisData.getAnalysisValue(this.analysisMap);
      })
    },
    //获取查询的机构信息
    isSelectDeptQuery(){
      let deptId = null;
      let deptType = null;
      const formData = new FormData;
      const collegeId = this.queryParams.deptId === '' ? null : this.queryParams.deptId;
      const professionDeptId = this.queryParams.professionDeptId === '' ? null : this.queryParams.professionDeptId;
      const classDeptId = this.queryParams.classDeptId === '' ? null : this.queryParams.classDeptId;
      if (null != collegeId){
        deptId = collegeId;
        deptType = '2';
        if (null != professionDeptId){
          deptId = professionDeptId;
          deptType = '3';
        }
        if (null != classDeptId){
          deptId = classDeptId;
          deptType = '3';
        }
      }
      formData.append("deptId",deptId);
      formData.append("deptType",deptType);
      return formData;
    },
    /** 查询院系列表 */
    getCollegeList() {
      const deptType = '1';
      listDept({deptType}).then(response => {
        this.CollegeList = response.data;
      });
    },
    /** 查询专业选择器事件 */
    isSelectDept(value){
      this.queryParams.professionDeptId = undefined;
      this.queryParams.classDeptId = undefined;
      const deptId = value;
      const deptType = '2';
      childrenDeptList({deptId,deptType}).then(res => {
        this.ProfessionList = res.data;
      });
    },
    /** 查询班级选择器事件 */
    isSelectProfessionDept(value){
      this.queryParams.classDeptId = undefined;
      const deptId = value;
      const deptType = '3';
      childrenDeptList({deptId,deptType}).then(res => {
        this.ClassList = res.data;
      });
    },
    /** 搜索按钮 */
    handleQuery(){
      this.queryParams.pageNum = 1;
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          if (this.activeName === '1'){
            this.topicStatus = '1'
            this.getThesisTopicAnalysis();
          }
          if (this.activeName === '2'){
            this.topicStatus = '5'
            this.getThesisTopicAnalysis();
          }
          if (this.activeName === '3'){
            this.topicStatus = '7'
            this.getThesisTopicAnalysis();
          }
          if (this.activeName === '4'){
            this.getPracticeWareAnalysis();
          }
        }
      });
    },
    /** 重置按钮 */
    resetQuery(){
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 标签页切换 */
    handleClick(tab, event){
      this.loading = true;
      this.activeName = tab.name;
      this.resetForm("queryForm");
      if (this.activeName === '1'){
        this.topicStatus = '1'
        this.getThesisTopicAnalysis();
        this.loading = false;
      }
      if (this.activeName === '2'){
        this.topicStatus = '5'
        this.getThesisTopicAnalysis();
        this.loading = false;
      }
      if (this.activeName === '3'){
        this.topicStatus = '7'
        this.getThesisTopicAnalysis();
        this.loading = false;
      }
      if (this.activeName === '4'){
        this.getPracticeWareAnalysis();
        this.loading = false;
      }
    },
  },
}
</script>

<style scoped>

</style>
