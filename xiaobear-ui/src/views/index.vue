<template>
  <div class="app-container">
    <div class="dashboard-editor-container">
      <el-card class="border-radius8 mb10">
        <el-row>
          <el-col :span="3">
            <el-avatar :src="avatar" :size="99" class="mt10"></el-avatar>
          </el-col>
          <el-col :span="14">
            <el-row style="margin-top: 25px">
              <span style="font-size: 24px;" v-if="dateText !== 'undefined'">{{dateText}}</span>
            </el-row>
            <el-row class="mt10">
              <span style="font-size: 22px;" :style="{color:textColor,fontSize:textSize}" v-if="dateText !== 'undefined'">{{text}}</span>
            </el-row>
          </el-col>
          <el-col :span="7">
            <el-row>
              <el-divider content-position="left">快捷导航</el-divider>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-button type="primary" plain @click="handleToWeekly">撰写周记</el-button>
              </el-col>
              <el-col :span="8">
                <el-button type="danger" plain @click="handleToInternApp">实习申请</el-button>
              </el-col>
              <el-col :span="8">
                <el-button type="success" plain @click="handleToTopic">论文题库</el-button>
              </el-col>
            </el-row>
            <el-row class="mt5">
              <el-col :span="8">
                <el-button type="primary" plain @click="handleToSummary">撰写总结</el-button>
              </el-col>
              <el-col :span="8">
                <el-button type="danger" plain @click="handleToInternChange">单位变更</el-button>
              </el-col>
              <el-col :span="8">
                <el-button type="success" plain @click="handleToProposalRepSub">开题报告</el-button>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-card>
      <el-card shadow="hover" class="border-radius8 mb10">
        <div slot="header">
          <el-row>
            <el-col :span="4">
              <span>{{plateGroupCardTitle}}</span>
            </el-col>
            <el-col :span="4" style="float: right" >
              <span class="mr20" style="color: #409EFF" @click="handleGetData('I')">实习数据</span>
              <span style="color: #409EFF" @click="handleGetData('P')">论文数据</span>
            </el-col>
          </el-row>
        </div>
        <el-row class="mt10">
          <internship-plate-group :info-list="infoCardData"/>
        </el-row>
      </el-card>
     <!-- <panel-group @handleSetLineChartData="handleSetLineChartData" />-->
      <el-row :gutter="16" class="mb10">
        <el-col :xs="24" :sm="24" :lg="12">
          <el-card style="border-radius: 8px;height: 400px">
            <div slot="header">
              <span><span class="el-icon-bell mr5"></span>最新动态</span>
            </div>
            <el-timeline :reverse="reverse">
              <el-timeline-item
                v-for="(item, index) in noticeList"
                :key="index"
                :timestamp="parseTime(item.createTime, '{y}-{m}-{d}')">
                {{escape2Html(item.noticeContent)}}
              </el-timeline-item>
            </el-timeline>
          </el-card>
        </el-col>
        <el-col :xs="24" :sm="24" :lg="12">
          <el-card style="border-radius: 8px;height: 400px">
            <div slot="header">
              <span><span class="el-icon-s-flag mr5"></span>我的任务</span>
            </div>
            <el-table
              :data="taskList"
              style="width: 100%">
              <el-table-column label="类型" prop="processType" :formatter="processTypeFormat"/>
              <el-table-column prop="endTime" align="center" label="截止日期">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="taskName" align="center" label="进度" width="200px">
                <template slot-scope="scope">
                  <el-progress :percentage="parseInt(scope.row.schedule)" :stroke-width="8" :color="customColorMethod"></el-progress>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
<!--        <el-col :xs="24" :sm="24" :lg="8">
          <el-card style="border-radius: 8px;height: 400px">
            <div slot="header">
              <span><span class="el-icon-s-opportunity mr5"></span>我的目标</span>
            </div>
            <el-row class="text-center mt20">
              <el-progress type="dashboard" :width="216" :percentage="100" ></el-progress>
            </el-row>
            <el-row class="text-center mb20">
              <span>恭喜，本月目标已达标！</span>
            </el-row>
          </el-card>
        </el-col>-->
      </el-row>
    </div>
  </div>

</template>

<script>
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RaddarChart from './dashboard/RaddarChart'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import {mapGetters} from "vuex";
import internshipPlateGroup from "./ims/analysis/components/plateGroup";
import {dashBoard, dashBoardNotice, dashBoardTask, dashThesisBoard} from "../api/ims/dashBoard/dashBoard";

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  }
}
export default {
  name: 'Index',
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
    internshipPlateGroup,
  },
  data() {
    return {
      lineChartData: lineChartData.newVisitis,
      infoCardData: [],
      taskList: [],
      noticeList: [],
      processTypeOptions:[],
      reverse: true,
      //plateGroup 标题
      plateGroupCardTitle: '实习数据',
      //根据时间展示文本
      text: undefined,
      textColor: String,
      textSize: String,
      //根据日期展示文本
      dateText: undefined,
    }
  },
  computed: {
    ...mapGetters([
      'avatar',
    ]),
  },created() {
    this.handleInitialization();
    this.getDicts("process_type").then(response => {
      this.processTypeOptions = response.data;
    });
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    },
    // 流程类型0-实习任务1-论文任务字典翻译
    processTypeFormat(row, column) {
      return this.selectDictLabel(this.processTypeOptions, row.processType);
    },
    handleInitialization(){
      this.handleGetHours();
      this.handlePlate();
      this.handleDashBoardTask();
      this.handleDashBoardNotice();
    },
    /** 实习or论文数据 */
    handleGetData(type){
      if ('I' === type){
        this.plateGroupCardTitle = '实习数据'
        this.handlePlate();
      }
      if ('P' === type){
        this.plateGroupCardTitle = '论文数据'
        this.handleThesisPlate();
      }
    },
   /** 撰写周记 */
    handleToWeekly(){
     //跳转到具体页面信息
     this.$router.push({
       path: '/addWeekly',
       query:{
         assessmenType: 0
       }
     });
    },
    /** 撰写总结 */
    handleToSummary(){
      //跳转到具体页面信息
      this.$router.push({
        path: '/addWeekly',
        query:{
          assessmenType: 1/practice/internApplication
        }
      });
    },
    handleToInternApp(){
      //跳转到具体页面信息
      this.$router.push({
        path: '/practice/internApplication',
      });
    },
    handleToInternChange(){
      //跳转到具体页面信息
      this.$router.push({
        path: '/practice/internshipChange',
      });
    },
    handleToTopic(){
      //跳转到具体页面信息
      this.$router.push({
        path: '/thesis/topic',
      });
    },
    handleToProposalRepSub(){
      //跳转到具体页面信息
      this.$router.push({
        path: '/thesis/proposalRepSub',
      });
    },
    handlePlate(){
      dashBoard().then(response =>{
        for (const item in response.data[0][0]) {
          if (item < 3){
            response.data[0][0][item].title += '周记'
          }else {
            response.data[0][0][item].title += '总结'
          }
        }
        this.infoCardData = response.data[0][0];
      })
    },
    handleThesisPlate(){
      dashThesisBoard().then(res =>{
        this.infoCardData = res.data[0];
      })
    },
    /** 首页-- 我的任务 */
    handleDashBoardTask(){
      dashBoardTask().then(response =>{
        this.taskList = response.data;
      })
    },
    /** 首页-最新动态 */
    handleDashBoardNotice(){
      dashBoardNotice().then(response =>{
        this.noticeList = response.data;
      })
    },
    escape2Html(str) {
      str = str.replace(/<\/?[^>]*>/g,''); //去除HTML tag
      str = str.replace(/[ | ]*\n/g,'\n'); //去除行尾空白
      str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
      str=str.replace(/ /ig,'');//去掉
      return str;
    },
    /** 进度条颜色方法 */
    customColorMethod(percentage) {
      if (percentage < 30) {
        return '#409eff';
      } else if (percentage < 70) {
        return '#e6a23c';
      } else {
        return '#67c23a';
      }
    },
    /** 获取日期时间展示文本 */
    handleGetHours(){
      const now = (new Date()).getHours();
      const fullYear = new Date().getFullYear();
      const nowDate = this.parseTime(new Date(), '{m}-{d}');
      if ('01-01' === nowDate){
        this.dateText = '元旦了呢，新的一年又开始了，今年是'+fullYear+'年~'
      }else if ('02-14' === nowDate){
        this.dateText = '情人节，'+fullYear+'年找到对象了嘛~'
      } else if ('03-08' === nowDate){
        this.dateText = '今天是妇女节!'
      } else if ('03-12' === nowDate){
        this.dateText = '今天是植树节，要保护环境呀'
      } else if ('04-01' === nowDate){
        this.dateText = '悄悄告诉你一个秘密~ 今天是愚人节，不要被骗了哦~'
      } else if ('05-01' === nowDate){
        this.dateText = '今天是五一劳动节，计划好假期去哪里了吗~'
      } else if ('06-01' === nowDate){
        this.dateText = '儿童节了呢，快活的时光总是短暂，要是永远长不大该多好啊…'
      } else if ('07-07' === nowDate){
        this.dateText = '今天是七夕，♡+♡=♡²~'
      } else if ('09-03' === nowDate){
        this.dateText = '中国人民抗日战争胜利纪念日，铭记历史、缅怀先烈、珍爱和平、开创未来。'
      } else if ('09-10' === nowDate){
        this.dateText = '教师节，在学校要给老师问声好呀~'
      } else if ('10-01' === nowDate){
        this.dateText = '国庆节，新中国已经成立'+fullYear - 1949+'年了呢'
      } else if ('11-05'<= nowDate && '11-12' >= nowDate){
        this.dateText = '双十一是和谁一起过的呢~'
      }
      if (this.dateText !== undefined){
        this.textColor = '#909399';
        this.textSize = '18px';
      }
      if (now > 23 || now <= 5) {
        this.text = '你是夜猫子呀？这么晚还不睡觉，明天起的来嘛';
      } else if (now > 5 && now <= 7) {
        this.text = '早上好！一日之计在于晨，美好的一天就要开始了';
      } else if (now > 7 && now <= 11) {
        this.text = '上午好！工作顺利嘛，不要久坐，多起来走动走动哦！';
      } else if (now > 11 && now <= 14) {
        this.text = '中午了，工作了一个上午，现在是午餐时间！';
      } else if (now > 14 && now <= 17) {
        this.text = '午后很容易犯困呢，今天的运动目标完成了吗？';
      } else if (now > 17 && now <= 19) {
        this.text = '傍晚了！窗外夕阳的景色很美丽呢，最美不过夕阳红~';
      } else if (now > 19 && now <= 21) {
        this.text = '晚上好，今天过得怎么样？';
      } else if (now > 21 && now <= 23) {
        this.text = '已经这么晚了呀，早点休息吧，晚安~';
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 8px 8px 8px 8px;
  background-color: #F2F6FC;
  position: relative;

  .chart-wrapper {
    background: #fff;
    border-radius: 8px;
    padding: 16px 16px 0;
    margin-bottom: 8px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
