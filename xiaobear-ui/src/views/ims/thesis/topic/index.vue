<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="导师" prop="teacherId">
        <el-select v-model="queryParams.teacherId" filterable clearable size="small" placeholder="请选择导师">
          <el-option
            v-for="o in teaList"
            :key="o.userId"
            :label="o.nickName"
            :value="o.userId"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课题" prop="topicTitle">
        <el-input
          v-model="queryParams.topicTitle"
          placeholder="请输入课题"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课题状态" prop="topicStatus">
        <el-select v-model="queryParams.topicStatus" placeholder="请选择课题状态" clearable size="small">
          <el-option
            v-for="dict in topicStatusOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="学科门类" prop="subjectCategory">
        <el-select
          v-model="queryParams.subjectCategory"
          clearable
          size="small"
          placeholder="请选择学科门类">
          <el-option
            v-for="dict in subjectCategoryOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="课题类型" prop="subjectType">
        <el-select v-model="queryParams.subjectType" placeholder="请选择课题类型" clearable size="small">
          <el-option
            v-for="dict in subjectTypeOptions"
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
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
	  <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="topicList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="学生" align="center" prop="stuNickName" width="90px" />
      <el-table-column label="导师" align="center" prop="nickName" width="90px" />
      <el-table-column label="课题" align="center" prop="topicTitle" width="300px" />
      <el-table-column label="学科门类" align="center" prop="subjectCategory" width="90px" :formatter="subjectCategoryFormat"/>
      <el-table-column label="课题类型" align="center" prop="subjectType" width="90px" :formatter="subjectTypeFormat"/>
      <el-table-column label="题目来源" align="center" prop="topicSource"  width="110px" :formatter="topicSourceFormat"/>
      <el-table-column label="课题状态" align="center" prop="topicStatus" >
        <template slot-scope="scope">
          <el-tag size="small">{{selectDictLabel(topicStatusOptions, scope.row.topicStatus)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleDetail(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            style="color: #F56C6C"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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
    <!-- 添加或修改课题信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="99px" inline>
        <el-form-item label="导师" prop="teacherId">
          <el-select v-model="form.teacherId" filterable placeholder="请选择导师">
            <el-option
              v-for="o in teaList"
              :key="o.userId"
              :label="o.nickName"
              :value="o.userId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属学院" prop="deptId">
          <el-select v-model="form.deptId" filterable placeholder="请选择学院">
            <el-option
              v-for="o in deptList"
              :key="o.deptId"
              :label="o.deptName"
              :value="o.deptId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="题目来源" prop="topicSource">
          <el-select v-model="form.topicSource" placeholder="请输入题目来源">
            <el-option
              v-for="dict in topicSourceOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课题类型" prop="subjectType">
          <el-select v-model="form.subjectType" placeholder="请选择课题类型">
            <el-option
              v-for="dict in subjectTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="课题" prop="topicTitle">
          <el-input v-model="form.topicTitle" class="input520" placeholder="请输入课题" />
        </el-form-item>
        <el-form-item label="课题描述" prop="topicDesc">
          <el-input type="textarea" class="input520" v-model="form.topicDesc" placeholder="请输入课题描述" />
        </el-form-item>
        <el-form-item label="学科门类" prop="subjectCategory">
          <el-select v-model="form.subjectCategory" class="input520" placeholder="请输入学科门类">
            <el-option
              v-for="dict in subjectCategoryOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog :title="title" :visible.sync="viewOpen"  width="1000px" append-to-body>
      <el-card class="box-card" shadow="never" :data="viewData">
        <el-row>
          <el-col class="text-center">
            <el-tag type="success" style="font-size: 18px">{{viewData.topicTitle}}</el-tag>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="18">
            <span class="text-color">学生姓名：</span>
            <span>{{viewData.stuNickName}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">指导老师：</span>
            <span >{{viewData.nickName}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">学科门类：</span>
            <span>{{selectDictLabel(this.subjectCategoryOptions, viewData.subjectCategory)}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">课题类型：</span>
            <span>{{selectDictLabel(this.subjectTypeOptions, viewData.subjectType)}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">题目来源：</span>
            <span>{{selectDictLabel(this.topicSourceOptions, viewData.topicSource)}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col>
            <span class="text-color">课题描述：</span>
            <span>{{viewData.topicDesc}}</span>
          </el-col>
        </el-row>
      </el-card>
    </el-dialog>

  </div>
</template>

<script>
import { listTopic, getTopic, delTopic, addTopic, updateTopic, exportTopic } from "@/api/ims/thesis/topic";
import { getUserProfile, listUser} from "@/api/system/user";
import {listDept, listPerDeniedDept} from "../../../../api/system/dept";
export default {
  name: "Topic",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 课题信息表格数据
      topicList: [],
      //教师列表
      teaList: [],
      //学院列表
      deptList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //是否是明细
      viewOpen: false,
      //是否提交
      published: undefined,
      // 题目来源
      topicSourceOptions: [],
      // 课题状态
      topicStatusOptions: [],
      // 学科门类
      subjectCategoryOptions: [],
      // 课题类型
      subjectTypeOptions: [],
      //志愿
      volunteerOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        topicId: null,
        teacherId: null,
        topicTitle: null,
        topicSource: null,
        topicType: null,
        topicDesc: null,
        topicStatus: null,
        subjectCategory: null,
        subjectType: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      viewData: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getTeaList();
    this.getLoginUser();
    this.getDicts("topic_source").then(response => {
      this.topicSourceOptions = response.data;
    });
    this.getDicts("topic_status").then(response => {
      this.topicStatusOptions = response.data;
    });
    this.getDicts("subject_category").then(response => {
      this.subjectCategoryOptions = response.data;
    });
    this.getDicts("subject_type").then(response => {
      this.subjectTypeOptions = response.data;
    });
    this.getDicts("volunteer").then(response => {
      this.volunteerOptions = response.data;
    });
  },
  methods: {
    /** 查询课题信息列表 */
    getList() {
      this.loading = true;
      listTopic(this.queryParams).then(response => {
        this.topicList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取登录用户信息 */
    getLoginUser(){
      getUserProfile().then(response =>{
        this.form.userId = response.row.userId
      })
    },
    //获取教师列表
    getTeaList(){
      let user = {
        roleId: 2,
      }
      listUser(user).then(res => {
        this.teaList = res.rows;
      })
    },
    /** 获取学院列表 */
    getDeptList(){
      const deptType = '1';
      listPerDeniedDept({deptType}).then(response => {
        this.deptList = response.data;
      });
    },
    // 题目来源
    topicSourceFormat(row, column) {
      return this.selectDictLabel(this.topicSourceOptions, row.topicSource);
    },
    // 课题状态
    topicStatusFormat(row, column) {
      return this.selectDictLabel(this.topicStatusOptions, row.topicStatus);
    },
    // 学科门类
    subjectCategoryFormat(row, column) {
      return this.selectDictLabel(this.subjectCategoryOptions, row.subjectCategory);
    },
    // 课题类型
    subjectTypeFormat(row, column) {
      return this.selectDictLabel(this.subjectTypeOptions, row.subjectType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        topicId: null,
        userId: null,
        teacherId: null,
        topicTitle: null,
        topicSource: null,
        topicType: null,
        topicDesc: null,
        topicStatus: "0",
        subjectCategory: null,
        subjectType: null,
        delFlag: null,
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
      this.ids = selection.map(item => item.topicId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getDeptList();
      this.open = true;
      this.title = "添加课题信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getDeptList();
      const topicId = row.topicId || this.ids
      getTopic(topicId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改课题信息";
      });
    },
    /** 明细按钮 */
    handleDetail(row){
      this.reset();
      this.viewData = row;
      this.viewOpen = true;
      this.title = "论题详情";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.topicId != null) {
            updateTopic(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTopic(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const topicIds = row.topicId || this.ids;
      if ("0" === row.topicStatus){
        this.$confirm('是否确认删除课题信息编号为"' + topicIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTopic(topicIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
      }else {
        this.msgError("你的论文题目已提交，不允许删除！")
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有课题信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTopic(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
