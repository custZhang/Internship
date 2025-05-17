<template>
  <div class="active-plate-main">
    <ul class="active-list">
      <li class="item" v-for="item in infoList" title="实习统计">
        <span class="num" :style="{color:getRandomColor()}"><count-to :start-val="0" :end-val="item.count" :duration="3600" class="card-panel-num"></count-to></span>
        <p class="desc">{{item.title}}</p>
      </li>
      <!--<li class="item" v-for="item in infoList" :key="item.title">
        <p class="num" :style="{color:item.color}"><CountTo :end="item.count">{{item.count}}</CountTo></p>
        <p class="desc">{{item.title}}</p>
      </li>-->
    </ul>
  </div>
</template>

<script>
  import CountTo from 'vue-count-to'
    export default {
      name: "internshipPlateGroup",
      components: {
        CountTo
      },
      props: {
        // 需要展示的数据集合
        infoList: {
          type: Array,
          require: true
        }
      },
      data(){
        return{
          endVal: 12,
          color: [
            "#2ec7c9", "#b6a2de", "#5ab1ef", "#ffb980", "#d87a80", "#8d98b3",
            "#e5cf0d", "#97b552", "#95706d", "#dc69aa", "#07a2a4", "#9a7fd1",
            "#588dd5", "#f5994e","#c05050", "#59678c","#c9ab00", "#7eb00a",
          ],
        }
      },
      methods:{
          /** 获取随机颜色 */
        getRandomColor() {
          const list = this.color;
          const colorIndex = Math.floor(Math.random() * list.length);
          const color = list[colorIndex];
          return color;
        }
      },
    }
</script>

<style lang="scss" scoped>
  .active-plate-main {
    width: 100%;
    height: 100px;
    background: #fff;
    .active-list {
      display: flex;
      list-style: none;
      .item {
        position: relative;
        flex: 1;
        text-align: center;
        .num {
          font-size: 42px;
          font-weight: bold;
          font-family: sans-serif;
        }
        .desc {
          font-size: 16px;
          font-weight: bold;
          color: #909399;
        }
        &::after {
          position: absolute;
          top: 20px;
          right: 0;
          content: "";
          display: block;
          width: 3px;
          height: 48px;
          background: #e7eef0;
        }
        &:nth-last-of-type(1) {
          &::after {
            background: none;
          }
        }
      }
    }
  }
</style>
