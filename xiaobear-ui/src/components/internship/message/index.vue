<template>
  <el-badge :value="messageNum" :hidden="isZero" style="padding: 0 0 34px 4px;margin-top: 2px!important">
    <el-col >
      <el-popover
        placement="bottom"
        width="520"
        trigger="click">
        <i slot="reference" class="el-icon-message"  style="font-size: 30px;color: #5a5e66;"/>
        <el-tabs :stretch="true" v-model="activeNames" @tab-click="handleTabClick">
          <el-tab-pane label="消息" name="1">
            <el-row v-show="messageList.length == 0" class="text-center">
              <img :src="noContent.img" width="60%" height="60%" >
            </el-row>
            <el-row align="center" v-for="o in messageList" :key="o.childId">
              <el-card shadow="hover" class="navbar-card" style="margin: auto;padding: 0 3px;">
                <el-badge is-dot class="mr5" v-show="o.childTaskStatus === '01'"></el-badge>
                <span class="view-text" @click="handleJumpMyTodo(o.businessName)">
                  <span v-show="o.stuNickName !== null">{{o.stuNickName +'的'+ o.businessName}}</span>
                   <span v-show="o.stuNickName == null">{{o.nickName +'的'+ o.businessName}}</span>
                  <el-tag class="ml20" size="mini">{{selectDictLabel(childTaskStatusOptions,o.childTaskStatus)}}</el-tag>
                </span>
                <span class="pull-right">2020.12.12</span>
              </el-card>
            </el-row>
            <el-row v-show="messageList.length == 0 ? false : true" class="el-row-center">
              <p class="el-row-p" @click="clearUnread"><span class="el-icon-brush mt5 mb5" style="color: #303133">清除未读</span></p>
            </el-row>
          </el-tab-pane>
          <!--通知-->
          <el-tab-pane label="通知" name="2">
            <el-row v-show="noticeList.length == 0" class="text-center">
              <img :src="noContent.noticeImg" width="60%" height="60%" >
            </el-row>
            <el-row v-for="item in noticeList" :key="item.noticeId" v-loading="loading">
              <el-card shadow="hover" class="navbar-card" style="margin: auto;padding: 0 3px;">
                <el-badge is-dot class="mr5" v-if="item.isRead == '0'"></el-badge>
                <span class="view-text" @click="handleNotificationClick(item.noticeId)">{{item.noticeTitle}}</span>
                <span class="pull-right">{{ parseTime(item.updateTime, '{y}-{m}-{d}') }}</span>
              </el-card>
            </el-row>
            <el-row v-show="noticeList.length == 0 ? false : true" class="el-row-center">
              <p class="el-row-p" @click="clearUnread"><span class="el-icon-brush mt5 mb5" style="color: #303133">清除未读</span></p>
            </el-row>
          </el-tab-pane>
          <!--公告-->
          <el-tab-pane label="公告" name="3">
            <el-row v-show="noticeList.length == 0" class="text-center">
              <img :src="noContent.noticeImg" width="60%" height="60%" >
            </el-row>
            <el-row v-for="o in noticeList" :key="o.noticeId" v-loading="loading">
              <el-card shadow="hover" class="navbar-card" style="margin: auto;padding: 0 3px;">
                <el-badge is-dot class="mr5" v-if="o.isRead == '0'"></el-badge>
                <span class="view-text" @click="handleNotificationClick(o.noticeId)"> {{o.noticeTitle}}</span>
                <span class="pull-right">{{ parseTime(o.updateTime, '{y}-{m}-{d}') }}</span>
              </el-card>
            </el-row>
            <el-row  v-show="noticeList.length == 0 ? false : true" class="el-row-center">
              <p class="el-row-p" @click="clearUnread"><span class="el-icon-brush mt5 mb5" style="color: #303133">清除未读</span></p>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </el-popover>
    </el-col>
  </el-badge>
</template>

<script>
import {getNotice, listNotice} from "@/api/system/notice";
import {selectDictLabel} from "@/utils/internship";
import {listUserNotice, updateUserNotice} from "../../../api/system/userNotice";
import {getUserProfile} from "../../../api/system/user";
import {listNoticeIsRead} from "../../../api/system/notice";
import {listChildPractice, listPracticeApplication} from "../../../api/ims/task/child";
import img from "../../../assets/image/noMessage.png"
import noticeImg from "../../../assets/image/notice.png"
import {listChildTopic} from "../../../api/ims/task/childTopic";
export default {
  name: "internshipMessage",
  data(){
    return {
      noContent:{img,noticeImg},
      // 遮罩层
      loading: true,
      activeNames:'1',
      messageNum: '',
      isZero: true,
      //通知公告列表
      noticeList: [],
      //实习周记、总结列表
      taskChildList: [],
      //实习申请列表
      practiceList: [],
      childTaskStatusOptions: [],
      messageList: [],
      //是否查看通知
      isClick: true,
      //公告类型
      noticeType: null,
      //用户ID
      userId: null,
      //定时器
      timer: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeType: null
      },
    }
  },
  mounted() {
    //定时执行
    // if (this.timer){
    //   clearInterval(this.timer)
    // }else {
    //   this.timer = setInterval(() => {
    //     setTimeout(this.getList)
    //   }, 1000*5)
    // }
  },
  destroyed() {
    clearInterval(this.timer)
  },
  created() {
    this.getList();
    this.getDicts("sys_notice_type").then(response => {
      this.typeOptions = response.data;
    });
    this.getDicts("stage").then(response => {
      this.childTaskStatusOptions = response.data;
    });
  },
  methods: {
    /** 查询用户与公告关联信息列表 */
    getList() {
      const isRead = '0';
      getUserProfile().then(response => {
        this.userId = response.data.userId;
        const userId = this.userId;
        listUserNotice({isRead, userId}).then(response => {
          this.messageNum = response.total;
          this.getToDoList(userId);
        });
      });
    },
    getToDoList(userId){
      const nextUser = userId;
      const childTaskProcess = '01';
      let total = 0;
      listChildPractice({childTaskProcess,nextUser}).then(response => {
        this.taskChildList = response.rows;
        total = response.total;
      });
      listPracticeApplication({childTaskProcess,nextUser}).then(response => {
        response.total += total;
        this.practiceList = response.rows.concat(this.taskChildList);
        this.messageNum += response.total;
      });
      listChildTopic({childTaskProcess,nextUser}).then(response => {
        this.messageList = response.rows.concat(this.practiceList);
        response.total += total;
        this.messageNum += response.total;
        this.isZero = this.messageNum == 0;
      });
    },
    /**标签页切换 */
    handleTabClick(){
      if (this.activeNames == 2){
        this.noticeType = '1';
      }
      if (this.activeNames == 3){
        this.noticeType = '2';
      }
      const noticeType = this.noticeType;
      /** 查询通知公告列表 */
      this.loading = true;
      this.getNoticeList(noticeType);
    },
    /** 通知与公告列表 */
    getNoticeList(noticeType){
      const userId = this.userId;
      listNoticeIsRead({noticeType,userId}).then(response => {
        this.noticeList = response.rows;
        this.loading = false;
      });
    },
    /** 消息详细内容 */
    handleNotificationClick(noticeId){
      this.isClick = false;
      getNotice(noticeId).then(response => {
        this.form = response.data;
        this.$notify.warning({
          title: selectDictLabel(this.typeOptions, this.form.noticeType),
          dangerouslyUseHTMLString: true,
          message: this.form.noticeContent,
        });
        const userId = this.userId;
        updateUserNotice({noticeId,userId}).then(response => {
          this.getList();
          this.handleTabClick();
        });
      });
    },
    /** 待办点击处理 */
    handleJumpMyTodo(businessName){
      console.log("打分")
      //跳转到具体页面信息
      this.$router.push({
        path: '/oa/upcoming',
      });
    },
    /** 清除未读 */
    clearUnread(){
      if(this.messageNum == '0'){
        this.$message('没有可清除的消息');
      }else {
        this.$confirm('确定消除所有消息吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          if (this.activeNames == 2){
            this.noticeType = '1';
          }
          if (this.activeNames == 3){
            this.noticeType = '2';
          }
          const noticeType = this.noticeType;
          const userId = this.userId;
          const isRead = '0';
          /** 查询通知公告列表 */
          listUserNotice({userId, isRead, noticeType}).then(response => {
            for(let i = 0; i < response.total; i++){
              const noticeId = response.rows[i].noticeId;
              updateUserNotice({noticeId,userId}).then(response => {
                this.getList();
                this.getNoticeList(noticeType);
                this.$message({
                  message: '清除消息成功',
                  type: 'success'
                });
              });
            }
          });
        })
      }
    }
  }
}
</script>

<style scoped>
.el-row-center{
  text-align: center;
}
.el-row-p{
  border-style: dashed;
  color: #DCDFE6;
  width: 100%;
  height: 30px
}
.el-row-center p:hover{
  color: #409EFF;
}
</style>
