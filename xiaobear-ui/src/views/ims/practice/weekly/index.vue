<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实习单位" prop="practiceId">
        <el-input
          v-model="queryParams.practiceId"
          placeholder="请输入实习单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标题" prop="weeklyTitle">
        <el-input
          v-model="queryParams.weeklyTitle"
          placeholder="请输入标题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="周次" prop="week">
        <el-select v-model="queryParams.week" placeholder="请选择周次" clearable size="small">
          <el-option
            v-for="dict in weekTimesOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择审核状态" clearable size="small">
          <el-option
            v-for="dict in statusOption"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
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
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="info"
          icon="el-icon-view"
          size="mini"
          :disabled="singleDetail"
          @click="handleDetail"
        >详情</el-button>
      </el-col>
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

    <el-table v-loading="loading" :data="weeklyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学号" align="center" prop="userName" v-if="teaShow"/>
      <el-table-column label="姓名" align="center" prop="nickName" v-if="teaShow"/>
      <el-table-column label="实习单位" align="center" prop="companyName" />
      <el-table-column label="考核类型" align="center" prop="assessmenType" :formatter="assessmenTypeFormat" />
      <el-table-column label="标题" align="center" prop="weeklyTitle" />
      <el-table-column label="开始时间" align="center" prop="beginTime" width="128">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="128">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="周次" align="center" prop="week" :formatter="weekTimesFormat"/>
      <el-table-column label="审核状态" align="center" prop="status" width="100" >
        <template slot-scope="scope">
          <el-tag :type="formatTagType(scope.row.status)">{{selectDictLabel(statusOption, scope.row.status)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交状态" align="center" prop="published" width="100"  :formatter="isPublishedFormat">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.published"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-if="scope.row.status == 1 ? false :true"
          >修改</el-button>
          <el-button
            size="mini"
            style="color: #F56C6C"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-if="scope.row.status == 1 ? false :true"
            v-hasPermi="['practice:weekly:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <el-dialog
      title="周记详情"
      :visible.sync="weekDetail"
      width="888px"
    >
      <span ref="weeklyInfo" :model="weeklyInfo">
        <el-row>
          <el-col prop="weeklyTitle">
            <h2 class="text-center">{{weeklyInfo.weeklyTitle}}({{selectDictLabel(weekTimesOptions, weeklyInfo.week)}})</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8" class="font-bolder">姓名：{{weeklyInfo.nickName}}</el-col>
          <el-col :span="8" class="font-bolder">学号：{{weeklyInfo.userName}}</el-col>
          <el-col :span="8" class="font-bolder">班级：{{weeklyInfo.deptName}}</el-col>
        </el-row>
        <el-row>
          <el-col class="p-line-height">
            <p v-html="weeklyInfo.weeklyContent"></p>
          </el-col>
        </el-row>
        <el-row>
           <el-col v-if="weeklyInfo.commentabled !== null && weeklyInfo.commentabled !== undefined && weeklyInfo.commentabled.length !== 0">
            <h4>辅导员的评价</h4>
            <span>{{weeklyInfo.commentabled}}</span>
          </el-col>
        </el-row>
         <el-row>
          <el-col v-if="isBlankValue">
            <el-input type="textarea" v-model="commentabled" placeholder="请输入评价内容"/>
          </el-col>
         </el-row>
          <el-row class="text-center mt10">
            <el-button type="primary" v-show="isBlankValue" size="medium" @click="detailSubmitForm">提交</el-button>
            <el-button @click="cancel">取 消</el-button>
          </el-row>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { listWeekly, getWeekly, delWeekly, addWeekly, updateWeekly, exportWeekly } from "@/api/ims/practice/weekly";
import Editor from '@/components/Editor';
import {getUserProfile} from "@/api/system/user";

export default {
  name: "Weekly",
  components: { Editor },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //标签类型
      tagType: '',
      // 非单个禁用
      single: true,
      singleDetail: true,
      // 非多个禁用
      multiple: true,
      //列表展示
      teaShow: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 实习考核信息表格数据
      weeklyList: [],
      // 弹出层标题
      title: "",
      commentabled:"",
      //评论是否有值
      isBlankValue: true,
      // 是否显示弹出层
      weekDetail: false,
      // 考核类型 0-实习周记 1-实习总结字典
      assessmenTypeOptions: [],
      //审核
      statusOption:[],
      //是否提交
      isPublishedOptions:[],
      //周次
      weekTimesOptions:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        weeklyTitle: null,
        beginTime: null,
        endTime: null,
        week: null
      },
      // 表单参数
      form: {},
      // 表单参数
      weeklyInfo: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("assessmen_type").then(response => {
      this.assessmenTypeOptions = response.data;
    });
    //审核状态 0-审核中  1-审核通过
    this.getDicts("status").then(response => {
      this.statusOption = response.data;
    });
    //提交状态 0-未提交  1-已提交
    this.getDicts("is_published").then(response => {
      this.isPublishedOptions = response.data;
    });
    //实习周次
    this.getDicts("week_times").then(response => {
      this.weekTimesOptions = response.data;
    });
  },
  methods: {
    /** 查询实习考核信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.assessmenType = 0;
      getUserProfile().then(response => {
        if (response.data.roleId == '100'){
          this.queryParams.userId = response.data.userId;
        }
        listWeekly(this.queryParams).then(response => {
          this.weeklyList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      });
    },
    //tag标签获取类型
    formatTagType(status) {
      if (status == 0) return ''
      else if (status == 1 ) return 'success'
      else if (status == 2 ) return 'danger'
    },
    // 考核类型 0-实习周记 1-实习总结字典翻译
    assessmenTypeFormat(row, column) {
      return this.selectDictLabel(this.assessmenTypeOptions, row.assessmenType);
    },
    statusFormat(row, column){
      return this.selectDictLabel(this.statusOption, row.status);
    },
    isPublishedFormat(row, column){
      return this.selectDictLabel(this.isPublishedOptions, row.published);
    },
    weekTimesFormat(row, column){
      return this.selectDictLabel(this.weekTimesOptions, row.week);
    },
    // 取消按钮
    cancel() {
      this.weekDetail = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        weeklyId: null,
        userId: null,
        practiceId: null,
        assessmenType: null,
        weeklyTitle: null,
        weeklyContent: null,
        commentabled: null,
        beginTime: null,
        endTime: null,
        week: null,
        delFlag: null,
        status: "0",
        createDept: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.weeklyId)
      this.single = selection.length!== 1
      this.multiple = !selection.length
      this.singleDetail = selection.length!==1

    },
    // 提交状态修改
    handleStatusChange(row) {
      //已经提交并且审核通过了
      if (row.status == 1 && row.published == 0){
        this.$confirm(this.selectDictLabel(this.weekTimesOptions, row.week)+'的周记已经审核通过，无法进行更改！', "提交", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        this.getList();
      }else {
        const weeklyId = row.weeklyId || this.ids
        console.log(row.published);
        let text = row.published === "0" ? "不提交" : "提交";
        this.$confirm('确认"' + text + '"你的周记吗?', "提交", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          getWeekly(weeklyId).then(response => {
            response.data.published = row.published;
            response.data.status = '0';
            updateWeekly(response.data);
          });
        }).then(() => {
          this.getList();
          this.msgSuccess(text + "成功");
        }).catch(function() {
          row.published = row.published === "0" ? "1" : "0";
        });
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      //跳转到具体页面信息
        this.$router.push({
          path: '/addWeekly',
          query:{
            assessmenType: 0
          }
        });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const weeklyId = row.weeklyId || this.ids
      getWeekly(weeklyId).then(response => {
        if ("1" === response.data.status){
          this.msgSuccess(this.selectDictLabel(this.weekTimesOptions, response.data.week)+'的周记已经审核通过，无法进行更改！')
        }else {
          //跳转到具体页面信息
          this.$router.push({
            path: '/addWeekly',
            query:{
              assessmenType: 0,
              weeklyId: weeklyId
            }
          });
        }
      });
    },
    /** 详情按钮 */
    handleDetail(row){
      this.reset();
      const weeklyId = row.weeklyId || this.ids
      getWeekly(weeklyId).then(response => {
        this.weeklyInfo = response.data;
        this.weekDetail = true;
        getUserProfile().then(response =>{
         if (100 == response.data.roleId){
           this.isBlankValue = false;
         }else if (this.weeklyInfo.commentabled !== null && this.weeklyInfo.commentabled !== undefined && this.weeklyInfo.commentabled.length !==0){
           this.isBlankValue = false;
         }
        })
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.weeklyId != null) {
            updateWeekly(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWeekly(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 详情提交按钮 */
    detailSubmitForm() {
      if (this.weeklyInfo.weeklyId != null) {
        this.weeklyInfo.commentabled = this.commentabled;
        updateWeekly(this.weeklyInfo).then(response => {
          this.msgSuccess("评价成功");
        });
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const weeklyIds = row.weeklyId || this.ids;
      this.$confirm('是否确认删除实习考核信息编号为"' + weeklyIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delWeekly(weeklyIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有实习考核信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportWeekly(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
