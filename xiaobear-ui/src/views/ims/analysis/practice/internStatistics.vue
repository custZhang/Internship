<template>
  <div>
    <el-row class="dashboard-editor-container">
      <el-card shadow="hover" class="border-radius8">
        <internship-plate-group :info-list="infoCardData"/>
      </el-card>
    </el-row>
    <el-row class="dashboard-editor-container" :gutter="32">
      <el-col :xs="24" :sm="24" :lg="14">
        <el-card class="text-center border-radius8">
          <internship-bar-chart :width="width" ref="barData" />
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="10">
        <el-card class="text-center border-radius8">
          <internship-pie-chart :width="width" ref="pieData" />
        </el-card>
      </el-col>
    </el-row>
    <el-row class="dashboard-editor-container">
      <el-card class="border-radius8">
        <el-table v-loading="loading" :data="tableData" border :header-cell-style="{background:'#DCDFE6'}"  >
          <el-table-column label="院系" align="center" prop="deptName" />
          <el-table-column label="实习中人数" align="center" prop="columnNum1" />
          <el-table-column label="已实习人数" align="center" prop="columnNum2" />
          <el-table-column label="未实习人数" align="center" prop="columnNum3" />
        </el-table>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import internshipBarChart from "../components/chart/barChart";
import internshipPieChart from "../components/chart/pieChart";
import internshipPlateGroup from "../components/plateGroup";
export default {
  name: "internStatistics",
  components: {
    internshipPlateGroup,
    internshipPieChart,
    internshipBarChart
  },
  props:{
    className:{
      type:String,
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '360px'
    }
  },
  data(){
    return {
      loading: false,
      //plate数据
      plateData: {},
      //柱形图数据
      barData: {},
      //饼形图数据
      pieData: {},
      //plate数据
      infoCardData: [],
      // 数据列表
      tableData: [],
    }
  },
  methods: {
    getAnalysisValue(m){
      this.infoCardData = m.internshipPlate;
      this.tableData = m.dataTables;
      const legendData = ['实习结束人数', '实习中人数', '未实习人数'];
      let obj = {
        xAxisData: m.deptNameList,
        seriesData2: m.internship,
        seriesData1: m.endOfInternship,
        seriesData3: m.noInternship,
        legendData: legendData,
        seriesName1: '实习结束人数',
        seriesName2: '实习中人数',
        seriesName3: '未实习人数',
      }
      this.barData = obj;
      this.$refs.barData.getBarData(this.barData);
      //重构m.internshipPlate数据，变成pieData
      const seriesData = this.refactorListToPieData(m.internshipPlate);
      //const legendData = ['实习结束人数', '实习中人数', '未实习人数'];
      const name = '实习人数统计';
      let pieObj = {
        seriesData: seriesData,
        legendData: legendData,
        seriesName: name
      }
      this.$refs.pieData.getPieData(pieObj);
    },
    /** 重构 m.internshipPlate数据，变成pieData  */
    refactorListToPieData(list){
      let j = 0, len = list.length;
      const array = [];
      for(; j < len; j++) {
        var object = {
          value: null,
          name: null,
        };
        for(var key in list[j]){
          if (key === 'count'){
            object.value = list[j][key]
          }else {
            object.name = list[j][key]
          }
        }
        array.push(object);
      }
      return array;
    }
  },
}
</script>

<style scoped>
  .dashboard-editor-container {
    padding: 10px;
    background-color: #F2F6FC;
    position: relative;
  }
</style>
