<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="100px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="user.nickName" class="input520"/>
    </el-form-item>
    <el-form-item label="手机号码" prop="phonenumber">
      <el-input v-model="user.phonenumber" maxlength="11" class="input520" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="user.email" maxlength="50" class="input520" />
    </el-form-item>
    <el-form-item label="性别">
      <el-select v-model="user.sex" placeholder="请选择" class="input520">
        <el-option
          v-for="dict in sexOptions"
          :key="dict.dictValue"
          :label="dict.dictLabel"
          :value="dict.dictValue"
        ></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary"  @click="submit">保存</el-button>
      <el-button type="danger"  @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      //用户性别字典
      sexOptions: [],
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          { required: true, message: "手机号码不能为空", trigger: "blur" },
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  created() {
    this.getDicts("sys_user_sex").then(response => {
      this.sexOptions = response.data;
    });
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.user).then(response => {
            this.msgSuccess("修改成功");
          });
        }
      });
    },
    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/index" });
    }
  }
};
</script>
