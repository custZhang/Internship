<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="实习单位" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入实习单位"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="实习期限" prop="theTerm">
        <el-input
          v-model="queryParams.theTerm"
          placeholder="请输入实习期限"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="协议类型" prop="iaType">
        <el-select v-model="queryParams.iaType" placeholder="请选择协议类型" clearable size="small">
          <el-option
            v-for="dict in iaTypeOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="文件名" prop="fileName">
        <el-input
          v-model="queryParams.fileName"
          placeholder="请输入文件名"
          clearable
          size="small"
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

    <el-table v-loading="loading" :data="agreementList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="实习单位" align="center" prop="companyName" />
      <el-table-column label="实习期限" align="center" prop="theTerm" />
      <el-table-column label="协议类型" align="center" prop="iaType" :formatter="iaTypeFormat" />
      <el-table-column label="文件名" align="center" prop="fileName" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建院系" align="center" prop="createDept" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
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

    <!-- 添加或修改实习协议信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="99px" inline>
        <el-form-item label="实习单位" prop="practiceId">
          <el-select v-model="form.practiceId" placeholder="请输入实习单位">
            <el-option
              v-for="o in practiceList"
              :key="o.practiceId"
              :label="o.companyName"
              :value="o.practiceId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实习期限" prop="theTerm" style="margin-left: 24px">
          <el-input v-model="form.theTerm"  placeholder="请输入实习期限" >
            <el-select v-model="theTermOption"
                       class="el-select-theTerm"
                       slot="prepend"
                       placeholder="请选择"
                       filterable
            ><el-option
                v-for="o in theTermSelectOption"
                :key="o.value"
                :label="o.label"
                :value="o.value"
              ></el-option>
            </el-select>
          </el-input>
        </el-form-item>
        <el-form-item label="协议类型" prop="iaType">
          <el-select v-model="form.iaType" placeholder="请选择协议类型">
            <el-option
              v-for="dict in iaTypeOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="协议文件" prop="fileName">
          <el-upload
            ref="file"
            :limit="1"
            accept=".xlsx, .xls, .pdf, .docx, .doc, .jpg, .jpeg, .ppt, .pptx, .png, .bmp, .gif "
            action="#"
            :on-remove="handleRemove"
            :disabled="isUploading"
            :before-remove="beforeRemove"
            :http-request="httpRequest"
            :before-upload="beforeUpload"
            :file-list="fileList" drag>
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" style="color:#ff0000" slot="tip">提示：仅允许上传“xls”,“xlsx”,“pdf“, ”docx“, ”doc”,
              ”.jpg“, ”.jpeg“, ”.ppt“, ”.pptx“, ”.png“格式文件！</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" v-model="form.remark" placeholder="请输入备注" style="width: 320%"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAgreement, getAgreement, delAgreement, addAgreement, updateAgreement, exportAgreement } from "@/api/ims/entitlement/agreement";
import {upload} from "@/api/ims/file/file";
import {personalListPractice} from "@/api/ims/practice/practice";
import {downLoad} from "@/utils/download";

export default {
  name: "Agreement",
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
      //实习期限
      theTermOption: null,
      // 实习协议信息表格数据
      agreementList: [],
      //个人实习单位列表
      practiceList: [],
      //文件列表
      fileList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否禁用上传
      isUploading:false,
      // 协议类型字典
      iaTypeOptions: [],
      //实习期限单位
      theTermSelectOption: [{
        value: '年',
        label: '年'
      }, {
        value: '个月',
        label: '个月'
      }, {
        value: '半年',
        label: '半年'
      }],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        companyName: null,
        theTerm: null,
        iaType: null,
        fileId: null,
        fileName: null,
        createDept: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        practiceId: [
          { required: true, message: "实习单位不能为空", trigger: "blur" }
        ],
        iaType: [
          { required: true, message: "协议类型不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("ia_type").then(response => {
      this.iaTypeOptions = response.data;
    });
  },
  computed: {},
  watch: {

  },
  mounted() {},
  methods: {
    /** 查询实习协议信息列表 */
    getList() {
      this.loading = true;
      listAgreement(this.queryParams).then(response => {
        this.agreementList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询个人实习列表 */
    getPracticeList(){
      personalListPractice(this.queryParams).then(response => {
        this.practiceList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 协议类型字典翻译
    iaTypeFormat(row, column) {
      return this.selectDictLabel(this.iaTypeOptions, row.iaType);
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        iaId: null,
        practiceId: null,
        theTerm: "",
        iaType: null,
        fileId: null,
        fileName: null,
        remark: null,
        delFlag: "0",
        createDept: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 清空文件列表 */
    resetFileList(){
      this.$refs.file.clearFiles();
    },
    /** 文件上传移除文件 */
    handleRemove(file, fileList) {
      this.fileList = fileList;
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    },
    //覆盖默认的上传行为，可以自定义上传的实现
    httpRequest(){

    },
    beforeUpload(file){
      this.fileList.push(file);
      return true;
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
      this.ids = selection.map(item => item.iaId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getPracticeList();
      this.open = true;
      this.theTermOption = null;
      this.title = "添加实习协议信息";
    },
    /**theTerm的期限格式 */
    handleMatch(value){
      for (let i = 0; i < this.theTermSelectOption.length; i++) {
        if (value == this.theTermSelectOption[i].label){
          this.theTermOption = this.theTermSelectOption[i].value;
          break;
        }
      }
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getPracticeList();
      const iaId = row.iaId || this.ids
      getAgreement(iaId).then(response => {
        this.form = response.data;
        const value = response.data.theTerm.match(/[^\d]/g);
        this.handleMatch(value);
        this.open = true;
        this.title = "修改实习协议信息";
      });
      this.theTermOption = null;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.iaId != null) {
            updateAgreement(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            let fileIndex = 0 ;
            this.fileUploadBatch(fileIndex);
          }
        }
      });
    },
    /**文件上传 */
    fileUploadBatch(fileIndex){
      if(fileIndex >= this.fileList.length){
        return ;
      }
      let formData = new FormData;
      formData.append("file", this.fileList[fileIndex])
      upload(formData).then(response => {
        if (response.code === 200) {
          let fileData = response.data;
          this.msgSuccess("上传成功,fileId:"+fileData.fileId);
          this.form.fileId = fileData.fileId;
          this.form.fileName = fileData.fileName;
          this.form.theTerm += this.theTermOption;
          addAgreement(this.form).then(response => {
            this.msgSuccess("新增成功");
            this.open = false;
            this.getList();
          });
        }
      });
      this.resetFileList();
    },
    /** 文件下载 */
    handleDownload(row){
      downLoad("/ims/file/download/" + row.fileId);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const iaIds = row.iaId || this.ids;
      this.$confirm('是否确认删除实习协议信息编号为"' + iaIds + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return delAgreement(iaIds);
        }).then(() => {
          this.getList();
          this.msgSuccess("删除成功");
        })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有实习协议信息数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          return exportAgreement(queryParams);
        }).then(response => {
          this.download(response.msg);
        })
    }
  }
};
</script>

<style>
.el-select-theTerm{
  width: 88px;
}
</style>
