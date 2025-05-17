<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />

    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />

        <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <xiao-bear-git id="internship-git" class="right-menu-item hover-effect" />
        </el-tooltip>

        <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <xiao-bear-doc id="internship-doc" class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <!--消息-->
        <el-tooltip disabled>
          <internship-message id="internship-message"></internship-message>
        </el-tooltip>

        <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <span class="right-menu-item-name"><span>Hi,</span>{{user.nickName}}</span>
        </div>

        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item icon="el-icon-user">个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="setting = true"
          icon="el-icon-set-up">
            <span >布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout"  icon="el-icon-switch-button">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import internshipGit from "@/components/internship/Git/index";
import internshipDoc from "@/components/internship/Doc/index";
import {getNotice, listNotice} from "@/api/system/notice";
import {selectDictLabel} from "@/utils/internship";
import internshipMessage from "@/components/internship/message/index";
import {getUserProfile} from "../../api/system/user";

export default {
  components: {
    internshipMessage,
    internshipDoc,
    internshipGit,
    Breadcrumb,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search
  },
  data() {
    return {
      //登录用户角色
      user: {},
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    }
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser(){
      getUserProfile().then(response =>{
        this.user = response.data;
      })
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  /*box-shadow: 0 1px 4px rgba(0,21,41,.08);*/

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      margin-bottom: 4px;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 20px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 50px;
          margin-bottom: 24px;
        }

        .right-menu-item-name{
          display: inline-block;
          position: relative;
          height: 100%;
          margin-left: 6px;
          font-size: 14px;
          bottom: 24px;
          color: #606266;
          vertical-align: text-bottom;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
