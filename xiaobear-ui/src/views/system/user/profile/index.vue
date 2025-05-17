<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div>
            <div class="text-center mt20">
              <userAvatar :user="user" />
              <div class="mt20" style="font-size: 24px">{{ user.nickName }}</div>
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <span class="text-color">
                  <svg-icon icon-class="user" class="mr5" />用户名称
                </span>
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <span class="text-color">
                  <svg-icon icon-class="phone" class="mr5" />手机号码
                </span>
                <div class="pull-right">{{ user.phonenumber }}</div>
              </li>
              <li class="list-group-item">
                <span class="text-color">
                   <svg-icon icon-class="email" class="mr5" />用户邮箱
                </span>
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <span class="text-color">
                  <svg-icon icon-class="tree" class="mr5"/>所属部门
                </span>
                <div class="pull-right" v-if="user.dept">{{ user.dept.deptName }} / {{ postGroup }}</div>
              </li>
              <li class="list-group-item">
                <span class="text-color">
                  <svg-icon icon-class="peoples" class="mr5"/>所属角色
                </span>
                <div class="pull-right">{{ roleGroup }}</div>
              </li>
              <li class="list-group-item">
                <span class="text-color">
                  <svg-icon icon-class="date" class="mr5"/>创建日期
                </span>
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card style="height: 490px">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本信息" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd :user="user" />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";

export default {
  name: "Profile",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
        this.roleGroup = response.roleGroup;
        this.postGroup = response.postGroup;
      });
    }
  }
};
</script>
