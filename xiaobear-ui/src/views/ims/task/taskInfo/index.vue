<template>
  <div class="app-container">
    <el-card class="border-radius8 mb20">
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
        <el-form-item label="流程类型" prop="processType">
          <el-select v-model="queryParams.processType" placeholder="请选择流程类型" clearable size="small">
            <el-option
              v-for="dict in processTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="学年" prop="academicYear">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="queryParams.academicYear"
                          type="year"
                          value-format="yyyy"
                          placeholder="选择学年">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-input
            v-model="queryParams.semester"
            placeholder="请输入学期"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="任务进度" prop="taskProgress">
          <el-select v-model="queryParams.taskProgress" placeholder="请选择任务进度" clearable size="small">
            <el-option
              v-for="dict in taskProgressOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建院系" prop="createDept">
          <el-input
            style="width: 200px"
            v-model="queryParams.createDept"
            placeholder="请输入创建院系"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item style="margin-left: 88px">
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="border-radius8">
      <el-row class="el-row-center">
        <p class="el-row-p"><span class="el-icon-plus mt5 mb5" style="color: #303133" @click="handleAdd">添加</span></p>
      </el-row>
      <el-row  v-for="item in taskInfoList" :key="item.taskId">
        <el-row class="text-center">
          <el-col :span="4">
            <p>院系名称</p>
            <span style="color: #909399">{{item.deptName}}</span>
          </el-col>
          <el-col :span="3">
            <p>任务类型</p>
            <span :style="processTypeColorMethod(item.processType)">{{selectDictLabel(processTypeOptions, item.processType)}}</span>
          </el-col>
          <el-col :span="3">
            <p>学年/学期</p>
            <span style="color: #909399">{{item.academicYear}}/{{selectDictLabel(semesterOptions, item.semester)}}</span>
          </el-col>
          <el-col :span="3">
            <p>截止日期</p>
            <span style="color: #909399">{{ parseTime(item.endTime, '{y}-{m}-{d}') }}</span>
          </el-col>
          <el-col :span="3">
            <p>创建人</p>
            <span style="color: #909399">{{item.createBy}}</span>
          </el-col>
          <el-col :span="4">
            <p>任务进度</p>
            <el-progress :percentage="parseInt(item.remark)" :stroke-width="8" :color="customColorMethod"></el-progress>
          </el-col>
          <el-col :span="4">
            <p>操作</p>
            <span class="iconfont iconfenpeijiaose mr5" v-show="item.processType !== 'P' " style="color: #409EFF" @click="handleDistribution(item)"
            >指导老师分配</span>
            <el-dropdown>
             <span class="el-dropdown-link ml5" style="font-size: 8px;color: #409EFF">更多<i class="el-icon-arrow-down el-icon--right"></i></span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <el-button
                  size="mini"
                  type="text"
                  style="color: #67C23A"
                  icon="el-icon-finished"
                  @click="handleEnd(item)"
                >结束任务</el-button></el-dropdown-item>
                <el-dropdown-item> <el-button
                  type="text"
                  icon="el-icon-edit"
                  size="mini"
                  @click="handleUpdate(item)"
                >修改</el-button></el-dropdown-item>
                <el-dropdown-item> <el-button
                  size="mini"
                  style="color: #F56C6C"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleDelete(item)"
                >删除</el-button></el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
        <el-divider></el-divider>
      </el-row>
      <el-row class="text-center">
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </el-row>
    </el-card>

    <!-- 添加或修改任务信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px"  append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px" inline>
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="form.taskName" placeholder="2020年上学期计算机科学与技术学院实习任务" class="input520" />
        </el-form-item>
        <el-form-item label="院系" prop="deptId">
          <treeselect v-model="form.deptId" :options="deptOptions" :normalizer="normalizer" placeholder="选择院系" class="input520" />
        </el-form-item>
        <el-form-item label="任务类型" prop="processType">
          <el-select v-model="form.processType" placeholder="请选择任务类型">
            <el-option
              v-for="dict in processTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学年" prop="academicYear">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.academicYear"
                          type="year"
                          value-format="yyyy"
                          placeholder="选择学年">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="学期" prop="semester">
          <el-select v-model="form.semester" placeholder="请选择学期">
            <el-option
              v-for="dict in semesterOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期" prop="endTime">
          <el-date-picker clearable size="small" style="width: 200px"
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="选择截止日期">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--指导老师分配-->
    <el-dialog title="指导老师分配" class="icon" :visible.sync="distribution" width="800px" append-to-body>
      <el-form ref="assignForm" :model="assignForm" :rules="rules" label-width="80px" inline>
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="assignForm.taskName" placeholder="请选择指导老师" disabled class="input520" />
        </el-form-item>
        <el-form-item label="选择老师" prop="userId">
          <el-select v-model="assignForm.userId" clearable placeholder="请选择指导老师" class="input520">
            <el-option
              v-for="item in teaList"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择班级" prop="deptId">
          <el-cascader
            filterable
            clearable
            v-model="assignForm.classIds"
            :options="CascaderDeptOptions"
            :props="optionProps"
            class="input520"
            @change="handleChange"
          ></el-cascader>
        </el-form-item>
        <el-form-item label="学生" prop="deptId">
          <el-transfer
            :titles="transferTitles"
            filter-placeholder="请输入姓名"
            v-model="assignForm.studentIds"
            :data="data">
          </el-transfer>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAssignForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTaskInfo, getTaskInfo, delTaskInfo, addTaskInfo, updateTaskInfo, exportTaskInfo } from "@/api/ims/task/taskInfo";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import Treeselect from "@riophae/vue-treeselect";
import {listDept} from "@/api/system/dept";
import {getUserProfile, listUser} from "@/api/system/user";
import {addTaskDept} from "@/api/ims/task/taskDept";
export default {
  name: "TaskInfo",
  components: { Treeselect },
  data() {
    return {
      /** 字段映射 */
      optionProps: {
        value: 'deptId',
        label: 'deptName',
        children: 'children'
      },
      //进度条
      percentage: 0,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //用户姓名数组
      userNickNameList: [],
      //用户id数组
      userIds: [],
      //班级列表
      classIds: [],
      //老师列表
      teaList: [],
      //选中学生列表
      studentIds: [],
      //穿梭框标题
      transferTitles:['选择学生','已选学生'],
      //数据
      data: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      //用户总条数
      userTotal: 0,
      // 任务信息表格数据
      taskInfoList: [],
      // 院系树选项
      deptOptions: [],
      //级联选择器数据
      CascaderDeptOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //指导老师分配弹出层
      distribution: false,
      // 流程类型0-实习任务1-论文任务字典
      processTypeOptions: [],
      // 任务进度01-进行中02-已完成字典
      taskProgressOptions: [],
      // 删除状态字典
      delFlagOptions: [],
      // 审核状态字典
      statusOptions: [],
      //学期
      semesterOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        taskName: null,
        processType: null,
        academicYear: null,
        semester: null,
        endTime: null,
        taskProgress: null,
        status: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      //指导老师分配表单参数
      assignForm: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("process_type").then(response => {
      this.processTypeOptions = response.data;
    });
    this.getDicts("task_progress").then(response => {
      this.taskProgressOptions = response.data;
    });
    this.getDicts("del_flag").then(response => {
      this.delFlagOptions = response.data;
    });
    this.getDicts("status").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("semester").then(response => {
      this.semesterOptions = response.data;
    });

  },
  methods: {
    /** 查询任务信息列表 */
    getList() {
      this.loading = true;
      listTaskInfo(this.queryParams).then(response => {
        this.taskInfoList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询学院老师列表 */
    getCollageTeaList(){
      getUserProfile().then(response => {
        this.user = response.data;
        const deptId = this.user.deptId;
        console.log("部门ID为：",deptId)
        const roleId = '2';
        listUser({deptId,roleId}).then(response => {
          this.teaList = response.rows;
        });
      });
    },
    /** 级联选择器班级 */
    getCollageClass(){
       getUserProfile().then(response => {
         this.user = response.data;
         const deptId = this.user.deptId;
         listDept({deptId}).then(response => {
           this.CascaderDeptOptions = this.formatData(this.handleTree(response.data, "deptId"));
         });
       });
    },
    /** 查询班级所有学生姓名,并放入穿梭框中  */
     getCollageUser(deptId){
       listUser({deptId}).then(response => {
         this.userTotal = response.total;
         const data = [];
         if (this.userTotal > 0) {
           for (let i = 0; i < this.userTotal; i++) {
             this.userIds = response.rows[i].userId;
             this.userNickNameList = response.rows[i].nickName;
             data.push({
               label: response.rows[i].nickName,
               key: response.rows[i].userId,
             });
           }
           this.data = data;
         }
       });
    },
    /** 格式化数据，递归将空的children置为undefined */
    formatData(data) {
      for (let i = 0; i < data.length; i++) {
        if (data[i].children.length < 1) {
          data[i].children = undefined;
        } else {
          this.formatData(data[i].children);
        }
      }
      return data;
    },
    /** Cascader选中的值发生变化时 */
    handleChange(){
      this.resetTransfer();
      if (null != this.assignForm.classIds && this.assignForm.classIds.length > 1){
        const deptId = this.assignForm.classIds[this.assignForm.classIds.length - 1];
        this.getCollageUser(deptId);
      }
    },
  /** 进度条颜色方法 */
    customColorMethod(percentage) {
      if (percentage < 30) {
        return '#409eff';
      } else if (percentage < 70) {
        return '#e6a23c';
      } else {
        return '#67c23a';
      }
    },
    /** 任务类型不同颜色方法 */
    processTypeColorMethod(processType){
      if (processType == 'I'){
        return 'color: #409EFF;font-weight: bolder;'
      }else {
        return 'color: #F56C6C;font-weight: bolder;'
      }
    },
    /** 转换院系数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.deptId,
        label: node.deptName,
        children: node.children
      };
    },
    // 流程类型 0-实习任务 1-论文任务 字典翻译
    processTypeFormat(row, column) {
      return this.selectDictLabel(this.processTypeOptions, row.processType);
    },
    // 任务进度 01-进行中 02-已完成 字典翻译
    taskProgressFormat(row, column) {
      return this.selectDictLabel(this.taskProgressOptions, row.taskProgress);
    },
    // 删除状态字典翻译
    delFlagFormat(row, column) {
      return this.selectDictLabel(this.delFlagOptions, row.delFlag);
    },
    // 审核状态字典翻译
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOptions, row.status);
    },
    // 学期状态字典翻译
    semesterFormat(row, column) {
      return this.selectDictLabel(this.semesterOptions, row.semester);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.distribution = false;
      this.reset();
      this.resetAssignForm();
      this.resetTransfer();
    },
    //指导老师分配表单
    resetAssignForm(){
      this.assignForm = {
        taskId: null,
        taskName: null,
        classIds: [],
        studentIds: [],
        userId: null
      }
    },
    //穿梭框数据重置
    resetTransfer(){
      this.data = [];
    },
    // 表单重置
    reset() {
      this.form = {
        taskId: null,
        taskName: null,
        processType: null,
        academicYear: null,
        semester: null,
        endTime: null,
        taskProgress: null,
        delFlag: null,
        status: null,
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
      this.ids = selection.map(item => item.taskId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加任务";
      const deptType = "1";
      listDept({deptType}).then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const taskId = row.taskId || this.ids
      getTaskInfo(taskId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改任务";
      });
    },
    /** 任务结束 */
    handleEnd(row){
      this.reset();
      const taskId = row.taskId || this.ids
      getTaskInfo(taskId).then(response => {
        this.form = response.data;
        this.form.taskProgress = '02';
        updateTaskInfo(this.form).then(response => {
          this.msgSuccess("实习任务已结束");
          this.getList();
        });
      });
    },
    /**指导老师分配按钮 */
    handleDistribution(row){
      this.resetAssignForm();
      this.resetTransfer();
      this.distribution = true;
      const taskId = row.taskId || this.ids
      getTaskInfo(taskId).then(response => {
        this.assignForm.taskName = response.data.taskName;
        this.assignForm.taskId = response.data.taskId;
      });
      this.getCollageTeaList();
      this.getCollageClass();
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.taskId != null) {
            updateTaskInfo(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTaskInfo(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 指导老师分配提交按钮 */
    submitAssignForm(){
      this.$refs["assignForm"].validate(valid => {
        if (valid) {
          console.log(this.assignForm)
          addTaskDept(this.assignForm).then(response => {
            this.msgSuccess("分配成功");
            this.distribution = false;
            this.getList();
          });
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const taskIds = row.taskId || this.ids;
      this.$confirm('是否确认删除任务信息编号为"' + taskIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delTaskInfo(taskIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有任务信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportTaskInfo(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>

<style scoped>
.el-row-center{
  text-align: center;
}
.el-row-p{
  border-style: dashed;
  color: #DCDFE6;
  width: 100%;
}
.el-row-center p:hover{
  color: #409EFF;
}
</style>
