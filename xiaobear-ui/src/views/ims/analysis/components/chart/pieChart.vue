<template>
  <div :id="className" class="chart-wrapper" :style="{height:height,width:width}"></div>
</template>

<script>
import echarts from "echarts";
import resize from "../../../../dashboard/mixins/resize";

export default {
  mixins: [resize],
  name: "internshipPieChart",
  props: {
    subtext: 'internship',
    className: {
      type: String,
      default: 'PieChart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '400px'
    }
  },
  data(){
    return{
      chart: null,
      seriesData:[],
      legendData: [],
      seriesName: null,
    }
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  mounted() {
   /* this.$nextTick(() => {
      this.initPieChart();
    })*/
  },
  methods:{
    /** 获取pie数据 */
    getPieData(obj){
      this.seriesName = obj.seriesName;
      this.seriesData = obj.seriesData;
      this.legendData = obj.legendData;
      this.$nextTick(() => {
        this.initPieChart();
       })
      //this.initPieChart();
      console.log(this.legendData)
    },
    /** 饼形图 */
    initPieChart() {
      this.chart = echarts.init(this.$el,this.className);
      let option = {
        title: {
          text: '计算机科学与技术学院实习情况统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        color: [
          "#2ec7c9", "#b6a2de", "#5ab1ef", "#ffb980", "#d87a80", "#8d98b3",
          "#e5cf0d", "#97b552", "#95706d", "#dc69aa", "#07a2a4", "#9a7fd1",
          "#588dd5", "#f5994e","#c05050", "#59678c","#c9ab00", "#7eb00a",
        ],
        legend: {
          left: 'center',
          bottom: '10',
          data: this.legendData /*['已实习', '未实习','实习中']*/
        },
        series: [
          {
            name: this.seriesName,
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '50%'],
            data:this.seriesData/* [
              { value: 320, name: '已实习' },
              { value: 240, name: '未实习' },
              { value: 280, name: '实习中' },
            ]*/,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      }
      this.chart.setOption(option,true)
    },
  }
}
</script>

<style scoped>
.chart-wrapper{
  background: #fff;
  padding: 16px 8px 0;
  margin-bottom: 4px;
}
</style>
