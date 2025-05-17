<template>
  <div class="app-container">
    <el-tabs tab-position="left" type="border-card" style="height: 590px;"class="mt5 border-radius8">
      <el-tab-pane label="实习申请">
        <internship-application/>
      </el-tab-pane>
      <el-tab-pane label="实习周记">
        <internship-weekly/>
      </el-tab-pane>
      <el-tab-pane label="实习总结">
       <internship-summary/>
      </el-tab-pane>
      <el-tab-pane label="论文题目">
        <thesis-topic/>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import InternshipWeekly from "./internship/internshipWeekly";
import InternshipSummary from "./internship/internshipSummary";
import InternshipApplication from "./internship/internshipApplication";
import ThesisTopic from "./thesis/thesisTopic";
export default {
  name: "TaskInfo",
  components: {ThesisTopic, InternshipApplication, InternshipSummary, InternshipWeekly},
  data() {
    return {}
  },
  created() {

  },
  methods: {
  }
};
</script>
<style lang="scss" >
  .el-tabs--border-card>.el-tabs__header {
    border-bottom: 1px solid #E4E7ED;
    background-color: white;

    .el-tabs__item.is-active {
      background: #DCEEF6;
    }
  }
</style>
