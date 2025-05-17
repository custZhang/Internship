<template>
  <div :id="className" class="chart-wrapper" :style="{height:height,width:width}"></div>
</template>

<script>
import echarts from "echarts";
import resize from "../../../../dashboard/mixins/resize";

const animationDuration = 6000
export default {
  mixins: [resize],
  name: "internshipBarChart",
  props: {
    className: {
      type: String,
      default: 'BarChart'
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
    return {
      chart: null,
      xAxisData: [],
      seriesData1: [],
      seriesData2: [],
      seriesData3: [],
      legendData: [],
      seriesName1: null,
      seriesName2: null,
      seriesName3: null,
    }
  },
  mounted() {
    /*this.$nextTick(() => {
      this.initBarchart();
    })*/
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose();
    this.chart = null
  },
  methods:{
    /** 获取bar数据 */
    getBarData(obj){
      this.xAxisData = obj.xAxisData;
      this.seriesData1 = obj.seriesData1;
      this.seriesData2 = obj.seriesData2;
      this.seriesData3 = obj.seriesData3;
      this.seriesName1 = obj.seriesName1;
      this.seriesName2 = obj.seriesName2;
      this.seriesName3 = obj.seriesName3;
      this.legendData = obj.legendData;
      this.initBarchart();
      console.log(obj)
    },
    /** 柱形图 */
    initBarchart(){
      this.chart = echarts.init(this.$el,this.className);
      let option =
      {
        tooltip: {
          trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        toolbox: {//工具栏,内置有导出图片，数据视图，动态类型切换，数据区域缩放，重置五个工具。
          feature: {//各工具配置项。
            magicType:{//动态类型切换 示例：feature: { magicType: {type: ['line', 'bar', 'stack', 'tiled']}}
              show:true,
              type:['line','bar']
            },
            restore: {//配置项还原。
              show: true
            },
            saveAsImage: {//保存为图片。
              show: true,
              excludeComponents: ['toolbox'],
            },

          }
        },
        color: [
          "#2ec7c9", "#b6a2de", "#5ab1ef", "#ffb980", "#d87a80", "#8d98b3",
          "#e5cf0d", "#97b552", "#95706d", "#dc69aa", "#07a2a4", "#9a7fd1",
          "#588dd5", "#f5994e","#c05050", "#59678c","#c9ab00", "#7eb00a",
        ],
        legend: {
          left: 'center',
          data: this.legendData /*['已实习', '未实习','实习中']*/
        },
        grid: {
          top: 30,
            left: '2%',
            right: '2%',
            bottom: '1%',
            containLabel: true
        },
        xAxis: [{
          type: 'category',
          boundaryGap: true,//设置为true
          data: this.xAxisData,
          axisTick: {
            alignWithLabel: true
          },
          axisLabel: {
            interval: 0,
            rotate: 0,
          }
        }],
          yAxis: [{
        type: 'value',
        realtimeSort: true,
        axisTick: {
          show: false
        }
      }],
        dataZoom: [
          {
            type: 'inside'
          }
        ],
        series: [{
        name: this.seriesName1,
        type: 'bar',
        stack: 'vistors',
        barWidth: '50%',
        barCategoryGap: '40%',
        data: this.seriesData1,
        animationDuration
      }, {
        name: this.seriesName2,
        type: 'bar',
        stack: 'vistors',
        barWidth: '60%',
        data: this.seriesData2,
        animationDuration
      }, {
        name: this.seriesName3,
        type: 'bar',
        stack: 'vistors',
        barWidth: '60%',
        data: this.seriesData3,
        animationDuration
      }]
      }
      this.chart.setOption(option,true);
    }
  }
}
</script>

<style scoped>
  .chart-wrapper{
    background: #fff;
    padding: 16px;
    margin-bottom: 4px;
  }
</style>
