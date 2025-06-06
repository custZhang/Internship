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
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="success"
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="danger"
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="info"
          icon="el-icon-view"
          size="mini"
          :disabled="single"
          @click="handleDetail"
        >详情
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          plain
          type="warning"
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="weeklyList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="实习单位" align="center" prop="companyName"/>
      <el-table-column label="考核类型" align="center" prop="assessmenType" :formatter="assessmenTypeFormat"/>
      <el-table-column label="标题" align="center" prop="weeklyTitle"/>
      <el-table-column label="开始时间" align="center" prop="beginTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.beginTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="formatTagType(scope.row.status)">{{ selectDictLabel(statusOption, scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交状态" align="center" prop="published" width="100" :formatter="isPublishedFormat">
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
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            style="color: #F56C6C"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
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
  </div>
</template>

<script>
import {listWeekly, getWeekly, delWeekly, addWeekly, updateWeekly, exportWeekly} from "@/api/ims/practice/weekly";
import Editor from '@/components/Editor';
import {getUserProfile} from "../../../../api/system/user";

export default {
  name: "Weekly",
  components: {Editor},
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
      //周次
      week: "第",
      // 总条数
      total: 0,
      // 实习考核信息表格数据
      weeklyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 考核类型 0-实习周记 1-实习总结字典
      assessmenTypeOptions: [],
      //审核
      statusOption: [],
      //提交状态
      isPublishedOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        practiceId: null,
        assessmenType: null,
        weeklyTitle: null,
        weeklyContent: null,
        commentabled: null,
        beginTime: null,
        endTime: null,
        week: null,
        status: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
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
  },
  methods: {
    /** 查询实习考核信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.assessmenType = 1;
      getUserProfile().then(response => {
        if (response.data.roleId == '100') {
          this.queryParams.userId = response.data.userId;
        }
        listWeekly(this.queryParams).then(response => {
          this.weeklyList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      })
    },
    //tag标签获取类型
    formatTagType(status) {
      if (status == 0) return ''
      else if (status == 1 ) return 'success'
      else if (status == 2 ) return 'danger'
    },
    isPublishedFormat(row, column) {
      return this.selectDictLabel(this.isPublishedOptions, row.published);
    },
    // 考核类型 0-实习周记 1-实习总结字典翻译
    assessmenTypeFormat(row, column) {
      return this.selectDictLabel(this.assessmenTypeOptions, row.assessmenType);
    },
    statusFormat(row, column) {
      return this.selectDictLabel(this.statusOption, row.status);
    },
    // 取消按钮
    cancel() {
      this.open = false;
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 提交状态修改
    handleStatusChange(row) {
      //已经提交并且审核通过了
      if (row.status == 1 && row.published == 0) {
        this.$confirm('你的总结已经审核通过，无法进行更改！', "提交", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
        this.getList();
      } else {
        const weeklyId = row.weeklyId || this.ids
        console.log(row.published);
        let text = row.published === "0" ? "不提交" : "提交";
        this.$confirm('确认"' + text + '"你的总结吗?', "提交", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          getWeekly(weeklyId).then(response => {
            response.data.published = row.published;
            updateWeekly(response.data);
          });
        }).then(() => {
          this.msgSuccess(text + "成功");
        }).catch(function () {
          row.published = row.published === "0" ? "1" : "0";
        });
      }
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      //跳转到具体页面信息
      this.$router.push({
        path: '/addWeekly',
        query: {
          assessmenType: 1
        }
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const weeklyId = row.weeklyId || this.ids
      //跳转到具体页面信息
      this.$router.push({
        path: '/addWeekly',
        query: {
          assessmenType: 1,
          weeklyId: weeklyId
        }
      });
    },
    /** 详情按钮 */
    handleDetail(row) {
      this.reset();
      const weeklyId = row.weeklyId || this.ids
      //跳转到具体页面信息
      this.$router.push({
        path: '/addWeekly',
        query: {
          assessmenType: 1,
          weeklyId: weeklyId
        }
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const weeklyIds = row.weeklyId || this.ids;
      this.$confirm('是否确认删除实习考核信息编号为"' + weeklyIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
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
      }).then(function () {
        return exportWeekly(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>
