<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实习单位" prop="companyName">
        <el-select v-model="queryParams.companyName" placeholder="请选择实习单位" clearable  size="small" style="width: 240px">
          <el-option
            v-for="dict in instructorList"
            :key="dict.companyName"
            :label="dict.companyName"
            :value="dict.companyName"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="实习指导老师姓名" prop="internInstructor" label-width="128px">
        <el-select v-model="queryParams.internInstructor" placeholder="请选择实习指导老师姓名" clearable  size="small" style="width: 240px">
          <el-option
            v-for="dict in instructorList"
            :key="dict.internInstructor"
            :label="dict.internInstructor"
            :value="dict.internInstructor"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="实习指导老师电话" prop="internInstructor" label-width="128px">
        <el-input
          v-model="queryParams.internPhone"
          placeholder="请输入实习指导老师电话"
          clearable
          size="small"
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="nickName">
      <el-input
        v-model="queryParams.nickName"
        placeholder="请输入用户名称"
        clearable
        size="small"
        style="width: 240px"
        @keyup.enter.native="handleQuery"
      />
    </el-form-item>
    <el-form-item label="院系名称" prop="deptName">
      <el-input
        v-model="queryParams.deptName"
        placeholder="请输入院系名称"
        clearable
        size="small"
        style="width: 240px"
        @keyup.enter.native="handleQuery"
      />
    </el-form-item>
    <el-form-item>
      <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
      <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
    </el-form-item>
  </el-form>

    <el-row :gutter="10" class="mb8">
    <el-col :span="1.5">
      <el-button
        plain
        type="warning"
        icon="el-icon-download"
        size="mini"
        @click="handleExport"
      >导出</el-button>
    </el-col>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
  </el-row>

    <el-table v-loading="loading" :data="instructorList" @expand-change="expandChange">
      <el-table-column type="expand" width="50" align="center">
        <template slot-scope="props">
          <el-table
            :data="stuPracticeList"
            border
          >
            <el-table-column label="学生所属院系" align="center" prop="professionName" />
            <el-table-column label="班级" align="center" prop="deptName" />
            <el-table-column label="学生姓名" align="center" prop="nickName" />
            <el-table-column label="学生电话" align="center" prop="phonenumber" />
          </el-table>
        </template>
      </el-table-column>
    <el-table-column label="实习单位" align="center" prop="companyName" :show-overflow-tooltip="true" />
    <el-table-column label="实习指导老师姓名" align="center" prop="internInstructor" :show-overflow-tooltip="true" />
    <el-table-column label="实习指导老师电话" align="center" prop="internPhone"  />
    <el-table-column label="实习指导老师邮箱" align="center" prop="internEmail" />
  </el-table>
  </div>
</template>

<script>
  import {exportInternInstructor, internInstructorList, stuList} from "../../../../api/ims/practice/practice";

    export default {
      name: "index",
      data(){
          return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索框
            showSearch: true,
            // 总条数
            total: 0,
            // 实习指导老师列表
            instructorList: [],
            // 院系树选项
            deptOptions: [],
            // 实习单位
            companyName: null,
            //实习学生信息列表
            stuPracticeList: [],
            // 查询参数
            queryParams:{
              pageNum: 1,
              pageSize: 10,
              nickName: null,
              companyName: null,
              internInstructor: null,
              deptName: null,
            },
          }
      },
      created() {
          this.getList();
      },
      methods:{
        /** 实习指导老师列表 */
        getList(){
          this.loading = true;
          internInstructorList(this.queryParams).then(response =>{
            this.instructorList = response.rows;
            this.total = response.total;
            this.loading = false;
          });
        },
        /** 单击每一行获取列表 */
        expandChange(row, expandedRows){
          if(expandedRows.length != 0){
            const companyName = row.companyName;
            stuList({companyName}).then(response => {
              this.stuPracticeList = response.data;
            })
          }
        },
        /** 搜索按钮操作 */
        handleQuery() {
          this.queryParams.page = 1;
          this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
          this.dateRange = [];
          this.resetForm("queryForm");
          this.handleQuery();
        },
        handleExport(){
          const queryParams = this.queryParams;
          this.$confirm('是否确认导出所有实习信息数据项?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function() {
            return exportInternInstructor(queryParams);
          }).then(response => {
            this.download(response.msg);
          })
        },
      }
    }
</script>

<style scoped>

</style>
