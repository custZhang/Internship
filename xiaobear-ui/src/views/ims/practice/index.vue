<template>
  <div class="app-container">
    <el-row v-show="false" class="mb20">
      <el-card shadow="never">
        <el-collapse accordion>
          <el-collapse-item>
            <template slot="title" >
              实习须知<i class="header-icon el-icon-info"></i>
            </template>
            <div>与现实生活一致：与现实生活的流程、逻辑保持一致，遵循用户习惯的语言和概念；</div>
            <div>在界面中一致：所有的元素和结构需保持一致，比如：设计样式、图标和文本、元素的位置等。</div>
          </el-collapse-item>
          <el-collapse-item title="反馈 Feedback">
            <div>控制反馈：通过界面样式和交互动效让用户可以清晰的感知自己的操作；</div>
            <div>页面反馈：操作后，通过页面元素的变化清晰地展现当前状态。</div>
          </el-collapse-item>
          <el-collapse-item title="效率 Efficiency">
            <div>简化流程：设计简洁直观的操作流程；</div>
            <div>清晰明确：语言表达清晰且表意明确，让用户快速理解进而作出决策；</div>
            <div>帮助用户识别：界面简单直白，让用户快速识别而非回忆，减少用户记忆负担。</div>
          </el-collapse-item>
          <el-collapse-item title="可控 Controllability">
            <div>用户决策：根据场景可给予用户操作建议或安全提示，但不能代替用户进行决策；</div>
            <div>结果可控：用户可以自由的进行操作，包括撤销、回退和终止当前操作等。</div>
          </el-collapse-item>
        </el-collapse>
      </el-card>
    </el-row>

    <el-row v-show="showStep">
      <el-col class="mb20">
        <el-card class="box-card" shadow="never">
          <i class="el-icon-close pull-right" @click="cancel"></i>
          <h3 style="text-align: center;color: #ff4949">{{this.queryParams.companyName}}实习进度</h3>
          <el-steps :active="active" align-center >
            <el-step title="实习申请" icon="el-icon-edit"></el-step>
            <el-step title="待学院审核" icon="el-icon-s-check"></el-step>
            <el-step title="审核已通过" icon="el-icon-s-check"></el-step>
            <el-step title="实习中" icon="el-icon-loading"></el-step>
            <el-step title="实习结束" icon="el-icon-success" status="success"></el-step>
          </el-steps>
        </el-card>
      </el-col>
    </el-row>

    <h2 style="text-align: center">我的实习申请</h2>
    <!--按钮-->
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
          type="info"
          icon="el-icon-view"
          size="mini"
          :disabled="single"
          @click="handleDetails"
        >详情</el-button>
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
    </el-row>

    <el-table v-loading="loading" :data="practiceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实习单位" align="center" prop="companyName" />
      <el-table-column label="实习岗位" align="center" prop="practicePost" />
      <el-table-column label="学校指导老师" align="center" prop="schoolInstructor" />
      <el-table-column label="实习指导老师" align="center" prop="internInstructor" />
      <el-table-column label="实习指导老师电话" align="center" prop="internPhone" />
      <el-table-column label="审核状态" align="center" prop="status" :formatter="statusFormatter"/>
      <el-table-column label="实习状态" align="center" prop="practiceStatus">
     <template slot-scope="scope">
       <el-tag :type="formatTagType(scope.row.practiceStatus)" size="small">{{selectDictLabel(practiceStatusOption, scope.row.practiceStatus)}}</el-tag>
     </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-d-arrow-right"
            @click="handleStep(scope.row)"
          >进度</el-button>
          <el-button
            size="mini"
            type="text"
            style="color: #67C23A"
            icon="el-icon-finished"
            @click="handleEnd(scope.row)"
            v-if="scope.row.practiceStatus == 2 ? false : true"
          >结束实习</el-button>
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

    <!-- 添加或修改实习信息对话框 -->
    <el-dialog :title="title" :visible.sync="showApplicationForm"  width="800px" append-to-body>
      <el-form :span="4" ref="form" :model="form" :rules="rules" label-width="150px" inline>
        <el-form-item label="实习单位" prop="companyId">
          <el-select v-model="form.companyId" clearable allow-create filterable placeholder="请选择实习单位" style="width: 560px">
            <el-option
              v-for="o in companyInfoList"
              :key="o.companyId"
              :label="o.companyName"
              :value="o.companyId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实习岗位" prop="practicePost">
          <el-input v-model="form.practicePost" placeholder="请输入实习岗位" style="width: 200px" />
        </el-form-item>
        <el-form-item label="家长姓名" prop="parentName">
          <el-input v-model="form.parentName" placeholder="请输入家长姓名" style="width: 200px"/>
        </el-form-item>
        <el-form-item label="家长联系方式" prop="parentPhone">
          <el-input v-model="form.parentPhone" placeholder="请输入家长联系方式" style="width: 200px" />
        </el-form-item>
        <el-form-item label="学校指导老师" prop="schoolInstructor">
          <el-autocomplete
            v-model="form.schoolInstructor"
            :fetch-suggestions="querySearchAsync"
            placeholder="请输入学校指导老师"
            style="width: 200px"
            @select="handleSelect"
          ></el-autocomplete>
<!--          <el-input v-model="form.schoolInstructor" placeholder="请输入学校指导老师" style="width: 200px" />-->
        </el-form-item>
        <el-form-item label="实习指导老师" prop="internInstructor">
          <el-input v-model="form.internInstructor" placeholder="请输入实习指导老师" style="width: 200px" />
        </el-form-item>
        <el-form-item label="实习指导老师电话" prop="internPhone">
          <el-input v-model="form.internPhone" placeholder="请输入实习指导老师电话" style="width: 200px" />
        </el-form-item>
        <el-form-item label="实习指导老师email" prop="internEmail">
          <el-input v-model="form.internEmail" placeholder="请输入实习指导老师email" style="width: 200px" />
        </el-form-item>
        <el-form-item label="实习开始时间" prop="beginTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.beginTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择实习开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实习结束时间" prop="endTime">
          <el-date-picker clearable size="small" style="width: 200px"
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="选择实习结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="实习类型" prop="practiceType">
          <el-select v-model="form.practiceType" placeholder="请选择实习类型" clearable mini>
            <el-option
              v-for="dict in practiceTypeOption"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <br>
        <el-form-item label="实习描述" prop="practiceDesc">
          <el-input type="textarea" v-model="form.practiceDesc" placeholder="请输入实习描述" style="width: 560px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--详情对话框-->
    <el-dialog :title="title" :visible.sync="open"  width="1000px" append-to-body>
      <el-card class="box-card" shadow="never" :data="practiceInfo">
        <el-row>
          <el-col :span="6">
            <el-tag type="success">{{selectDictLabel(practiceTypeOption, practiceInfo.practiceType)}}</el-tag>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">实习时间：</span>
            <span>{{practiceInfo.beginTime}}至{{practiceInfo.endTime}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习单位：</span>
            <span >{{practiceInfo.companyName}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">实习岗位：</span>
            <span >{{practiceInfo.practicePost}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">实习指导老师：</span>
            <span>{{practiceInfo.internInstructor}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">实习老师联系方式：</span>
            <span>{{practiceInfo.internPhone}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">实习老师Email：</span>
            <span>{{practiceInfo.internEmail}}</span>
          </el-col>
        </el-row>
        <el-row class="mt20">
          <el-col :span="10">
            <span class="text-color">学校指导老师：</span>
            <span>{{practiceInfo.schoolInstructor}}</span>
          </el-col>
          <el-col :span="8">
            <span class="text-color">家长姓名：</span>
            <span>{{practiceInfo.parentName}}</span>
          </el-col>
          <el-col :span="6">
            <span class="text-color">家长联系方式：</span>
            <span>{{practiceInfo.parentPhone}}</span>
          </el-col>
        </el-row>
      </el-card>
    </el-dialog>
  </div>
</template>

<script>
import {
  listPractice,
  getPractice,
  delPractice,
  addPractice,
  updatePractice,
  exportPractice,
  personalListPractice
} from "@/api/ims/practice/practice";
import {companyListInfo, getInfo} from "../../../api/ims/enterprise/info";
import {getUserProfile, listUser} from "../../../api/system/user";

export default {
  name: "Practice",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      //标签样式
      tagType: '',
      status: null,
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      //显示步骤条
      showStep: false,
      //显示申请表
      showApplicationForm: false,
      // 总条数
      total: 0,
      restaurants: [],
      //步骤条
      active: 0,
      // 实习信息表格数据
      practiceList: [],
      // 公司列表
      companyInfoList: [],
      //字典管理
      statusOption:[],
      reviewStatusOption: [],
      practiceStatusOption:[],
      practiceTypeOption:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        companyId: null,
        companyName: null,
        schoolInstructor: null,
        internInstructor: null,
        internPhone: null,
        internEmail: null,
        beginTime: null,
        endTime: null,
        practiceDesc: null,
        status: null,
        practiceStatus: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      practiceInfo: {},
      // 表单校验
      rules: {
        userId: [
          { required: false, message: "姓名不能为空", trigger: "blur" }
        ],
        companyName: [
          { required: true, message: "实习单位不能为空", trigger: "blur" }
        ],
        schoolInstructor: [
          { required: true, message: "学校指导老师不能为空", trigger: "blur" }
        ],
        internInstructor: [
          { required: true, message: "实习指导老师不能为空", trigger: "blur" }
        ],
        internPhone: [
          {
            required:true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        parentName:[
          { required: true, message: "家长姓名不能为空", trigger: "blur" }
        ],
        practicePost:[
          { required: true, message: "实习岗位不能为空", trigger: "blur" }
        ],
        practiceType:[
          { required: true, message: "实习类型不能为空", trigger: "blur" }
        ],
        parentPhone: [
          {
            required:true,
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ],
        beginTime: [
          { required: true, message: "实习开始时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getCollageTeaList();
    //审核状态 0-审核中  1-审核通过
    this.getDicts("status").then(response => {
      this.statusOption = response.data;
    });
    //审批状态 0-退回 1-通过
    this.getDicts("review_status").then(response => {
      this.reviewStatusOption = response.data;
    });
    //实习状态 0-未实习 1-实习中 2-实习结束
    this.getDicts("practice_status").then(response => {
      this.practiceStatusOption = response.data;
    });
    //实习类型 0-统一实习 1-自主实习
    this.getDicts("practice_type").then(response => {
      this.practiceTypeOption = response.data;
    });
  },
  methods: {
    /** 查询实习信息列表 */
    getList() {
      this.loading = true;
      personalListPractice(this.queryParams).then(response => {
        this.practiceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 招聘公司列表 */
    getCompanyList() {
      companyListInfo(this.queryParams).then(response => {
        this.companyInfoList = response.rows;
      });
    },
    /**远程调用教师列表 */
    querySearchAsync(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createStateFilter(queryString)) : restaurants;
      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 3000 * Math.random());
    },
    createStateFilter(queryString) {
      return (state) => {
        return (state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    handleSelect(item) {
      console.log(item);
    },
    /** 查询学院老师列表 */
    getCollageTeaList(){
      getUserProfile().then(response => {
        this.user = response.data;
        const deptId = this.user.deptId;
        console.log("部门ID为：",deptId)
        const roleId = '2';
        listUser({deptId,roleId}).then(response => {
          response.rows.forEach((item, index) =>{
            this.restaurants.push({
              value: item.nickName
            })
          })
        });
      });
    },
    //tag标签获取类型
    formatTagType(status) {
      if (status == '0') return 'info'
      else if (status == '1' ) return 'warning'
      else if (status == '2' ) return 'success'
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.showStep = false;
      this.showApplicationForm = false;
    },
    // 表单重置
    reset() {
      this.form = {
        practiceId: null,
        userId: null,
        companyId: null,
        schoolInstructor: null,
        internInstructor: null,
        parentName: null,
        parentPhone: null,
        internPhone: null,
        internEmail: null,
        beginTime: null,
        endTime: null,
        practiceDesc: null,
        status: "0",
        delFlag: "0",
        practiceStatus: "0",
        createDept: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    //字典翻译
    /** 审核状态 */
    statusFormatter(row, column){
      return this.selectDictLabel(this.statusOption, row.status);
    },
    /** 实习状态 */
    practiceStatusFormatter(row, column){
      return this.selectDictLabel(this.practiceStatusOption, row.practiceStatus);
    },
    /** 审批进度 */
    handleStep(row){
      this.active = 1;
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.form = response.data;
        this.showStep = true;
        if (this.form.status == 1){
          this.active = 4;
        }
        if (this.form.status == 1 && this.form.practiceStatus == 2){
          this.active = 5;
        }
        this.queryParams.companyName = this.form.companyName;
      });
    },
    /** 结束实习按钮 */
    handleEnd(row){
      this.reset();
      console.log(row.practiceStatus)
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.form = response.data;
        if (this.form.practiceStatus != 2 && this.form.status == 1){
          this.$confirm('确定结束该习单位吗', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() =>{
            this.form.practiceStatus = 2;
            updatePractice(this.form).then(response => {
              this.msgSuccess("恭喜你，实习结束");
              this.getList();
            });
          })
        }
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.practiceId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 详情按钮 */
    handleDetails(row){
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.practiceInfo = response.data;
        this.open = true;
        this.title = '实习详情';
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      /** 判断是否存在正在实习的单位 */
      let practiceStatus = '1';
      let userId;
      getUserProfile().then(response => {
        userId = response.data.userId;
      })
      personalListPractice({practiceStatus}).then(response => {
        this.practiceList = response.rows;
        if (this.practiceList.length > 0){
          this.$confirm('存在正在实习的单位，若确定新增，待你提交后，将结束你原实习单位', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() =>{
            this.getCompanyList();
            this.title = '实习申请表';
            this.showApplicationForm = true;
            this.getList();
          })
        }else {
          this.getCompanyList();
          this.showApplicationForm = true;
          this.showStep = false;
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const practiceId = row.practiceId || this.ids
      getPractice(practiceId).then(response => {
        this.form = response.data;
        /** 审核通过后不可以修改 */
        if (this.form.status == 1){
          this.msgInfo("你的实习申请已审核，不能进行修改！");
        }else {
          this.getCompanyList();
          this.showApplicationForm = true;
        }
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.practiceId != null) {
            /** 审核通过后不可以修改 */
            if (this.form.status == 1){
              this.msgInfo("你的实习申请已审核，不能进行修改！");
            } else {
              updatePractice(this.form).then(response => {
                this.msgSuccess("修改成功");
                this.showApplicationForm = false;
                this.getList();
              });
            }
          } else {
            if (!this.handleIsNumber(this.form.companyId)){
              this.form.companyName = this.form.companyId;
              this.form.companyId = null
            }
            addPractice(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.getList();
              this.showApplicationForm = false;
              this.active = 1;
            });
          }
        }
      });
    },
    //判断是否是数字
    handleIsNumber(obj){
      const numReg = /^[0-9]*$/;
      const numRe = new RegExp(numReg);
      return numRe.test(obj);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const practiceIds = row.practiceId || this.ids;
      let practiceId;
      /** 审核通过的不能进行删除 */
      if (practiceIds.length > 1) {
        for (let i = 0; i < practiceIds.length - 1; i++) {
          practiceId = practiceIds[i];
          getPractice(practiceId).then(response => {
            this.form = response.data;
            if (this.form.status == 1) {
              this.$confirm('你的实习申请已经审核，不能删除', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "danger"
              })
            } else {
              this.$confirm('是否确认删除实习信息编号为"' + practiceIds + '"的数据项?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(function () {
                return delPractice(practiceIds);
              }).then(() => {
                this.getList();
                this.msgSuccess("删除成功");
              })
            }
          });
        }
      }else {
        getPractice(practiceIds).then(response => {
          this.form = response.data;
          if (this.form.status == 1) {
            this.$confirm('你的实习申请已经审核，不能删除', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            })
          } else {
            this.$confirm('是否确认删除实习信息编号为"' + practiceIds + '"的数据项?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function () {
              return delPractice(practiceIds);
            }).then(() => {
              this.getList();
              this.msgSuccess("删除成功");
            })
          }
        });
      }
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有实习信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportPractice(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>
