<template>
  <div>
    <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
      <el-form-item label="文件" prop="file" required>
        <el-upload ref="file" :file-list="filefileList" action="#" :http-request="requestUpload"
                   :before-upload="fileBeforeUpload">
          <el-button size="small" type="primary" icon="el-icon-upload">点击上传</el-button>
        </el-upload>
<!--        <el-upload ref="files"
                   multiple
                   accept=".xlsx, .xls, .pdf, .docx, .doc, .jpg, .jpeg, .ppt, .pptx, .png, .bmp, .gif "
                   action="#"
                   :file-list="filefileList"
                   :http-request="requestUpload"
                   :before-upload="fileBeforeUpload"
                   drag>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">
            将文件拖到此处，或
            <em>点击上传</em>
          </div>
          <div class="el-upload__tip" style="color:#ff0000" slot="tip">提示：仅允许导入“xls”,“xlsx”,“pdf“, ”docx“, ”doc”,
            ”.jpg“, ”.jpeg“, ”.ppt“, ”.pptx“, ”.png“, ”.bmp“, ”.gif“格式文件！</div>
        </el-upload>-->
      </el-form-item>

      <el-form-item size="large">
        <el-button type="primary" @click="submitForm">上传</el-button>
      </el-form-item>
    </el-form>
    <el-form ref="elForm2" :model="formData" :rules="rules" size="medium" label-width="100px">
      <el-form-item label="文件ID" prop="fileId">
        <el-input v-model="formData.fileId" placeholder="请输入文件ID" clearable :style="{width: '100%'}" >
        </el-input>
      </el-form-item>
      <el-form-item label="fileId" prop="fileId">
        <span >{{formData.fileId}}</span>
      </el-form-item>
      <el-form-item size="large">
        <el-button type="primary" @click="downloadFile">下载</el-button>

      </el-form-item>
    </el-form>

    <!--下载进度条-->
    <el-dialog title="正在下载，请等待" :visible.sync="fileDown.loadDialogStatus" :close-on-click-modal="false"
               :close-on-press-escape="false" :show-close="false" width="20%">
    <div style="text-align: center;">
    <el-progress type="circle" :percentage="fileDown.percentage"></el-progress>
    </div>
    <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="downClose">取消下载</el-button>
    </div>
    </el-dialog>
  </div>
</template>
<script>
import {upload} from "@/api/ims/file/file";
import {downLoad} from "@/utils/download";
  export default {
    components: {},
    props: [],
    data() {
      return {
        formData: {
          file: null,
          fileId : undefined,
        },
        rules: {
          fileId: [{
            required: true,
            message: '请输入文件ID',
            trigger: 'blur'
          }],

        },
        filefileList: [],
      }
    },
    computed: {},
    watch: {

    },
    created() {},
    mounted() {},
    methods: {
      submitForm() {
          //文件数
          let fileIndex = 0 ;
          this.fileUploadBatch(fileIndex);
      },
      fileUploadBatch(fileIndex){
        if(fileIndex >= this.filefileList.length){
          return ;
        }
        console.log(fileIndex)
        console.log(this.filefileList[fileIndex])
        let formData = new FormData;
        formData.append("file", this.filefileList[fileIndex])
        upload(formData).then(response => {
          if (response.code === 200) {
            let fileData = response.data
            console.log(fileData)
            this.msgSuccess("上传成功,fileId:"+fileData.fileId);
          }
          this.visible = false;
        });
        this.resetForm();
      },
      resetForm() {
        this.$refs.file.clearFiles();
      },
      fileBeforeUpload(file) {
        let isRightSize = file.size / 1024 / 1024 < 2000
        if (!isRightSize) {
          this.$message.error('文件大小超过 200MB')
        }
        //console.log(file)
        this.filefileList.push(file)

        return isRightSize
      },
      // 覆盖默认的上传行为
      requestUpload() {
      },
      downloadFile(){
        //downLoad("/pbc/material/export/1")
        downLoad("/ims/file/download/" + this.formData.fileId);
        console.log(this.formData)
        console.log(this.formData.fileId)
        //viewFile(this.fileId )
        //window.location.href = "/ims/file/download/?fileId=" + this.formData.fileId ;
      },
    }
  }

</script>
<style>
  .el-upload__tip {
    line-height: 1.2;
  }

</style>
