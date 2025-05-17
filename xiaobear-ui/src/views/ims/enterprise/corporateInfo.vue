<template>
  <div class="app-container">
    <el-row >
      <el-row>
        <el-page-header @back="goBack" class="mb20"/>
      </el-row>
      <el-col>
        <span style="font-size: 42px;">{{recInfo.postName}}</span><span style="color: #ff4949;font-size: 24px;float: right">{{recInfo.postWage}}</span>
        <h4>{{recInfo.companyName}}</h4>
      </el-col>
      <el-row>
        <el-col :span="4">
          <span>{{recInfo.location}}</span>
        </el-col>
        <el-col :span="5">
          <span>{{selectDictLabel(educationOption, recInfo.education)}}</span>
        </el-col>
        <el-col :span="5">
          <span>{{selectDictLabel(workingYearsOption, recInfo.postExperience)}}</span>
        </el-col>
        <el-col :span="4">
          <span>招{{recInfo.postNumber}}人</span>
        </el-col>
        <el-col :span="6">
          <span class="pull-right">招聘详情：{{recInfo.recruitmentWebsite}}</span><br>
        </el-col>
      </el-row>
    </el-row>
    <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
    <el-row>
      <div class="corporate-border"><span >工作职责</span></div>
      <p><span v-html="recInfo.responsibilities"></span>1.&nbsp;熟悉亚马逊等电商平台对视频、图片的要求和规则，负责产品视频拍摄和后期制作；</p><p>2.&nbsp;熟悉视频拍摄制作流程、视频编辑、可独立完成产品小视频后期制作（视频策划、剪辑、合成、包装、音乐等）；</p><p>3. 负责联系和协助图片视频外包委派的工作；</p><p>4.&nbsp;协助完成亚马逊产品的主图和附图的拍摄和后期制作，包括产品图片方案设计、前期拍照、素材寻找、产品图片优化等；</p><p>5.&nbsp;协助业务部门处理工作中遇到的产品图片问题；</p><p>6. 负责摄影器材、摄影辅助资产的保管和日常维护；</p><p>7. 完成公司领导安排的其他工作。</p><p><br></p>
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <div class="corporate-border"><span>任职要求</span></div>
      <p><span v-html="recInfo.requirements"></span><p>1. 统招大专及以上学历，艺术、美术、视觉传达等设计类相关专业；</p><p>2. 1-3年视频拍摄、剪辑经验，熟练使用后期软件达芬奇、PR&nbsp;、&nbsp;AE&nbsp;&nbsp;、剪映等软件；</p><p>3. 剪辑基本功扎实，节奏感强，有脑洞，有创造性，对色彩、构图、镜头语言有较清晰认识，独特的创意；</p></p>
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <div class="corporate-border"><span >联系方式</span></div>
      <p style="margin-top: 32px"><i class="el-icon-location-information" style="color: #ed5565"></i>    {{recInfo.companyLocation}}</p>
      <span><i class="el-icon-phone" style="color: #1890ff"></i>&nbsp;&nbsp;&nbsp;&nbsp;{{recInfo.principalPhone}}</span>
    </el-row>
    <el-divider></el-divider>
    <el-row>
      <div class="corporate-border"><span>公司介绍</span></div>
      <p><span v-html="recInfo.companyProfile"></span>&nbsp;&nbsp;&nbsp;&nbsp;湖南和美顺电子商务有限公司是一家新兴跨境电商公司,公司充分利用全球移动互联网技术的快速发展，并依托Amazon、ebay、wish等国际电商平台进行产品销售和市场推广。目前公司主要市场是欧美地区，热销产品覆盖家居、户外运动、电子产品、宠物用品、时尚等品类。我们有丰富的跨境电商资源及经营经验，并和众多优秀的生产厂商建立了良好的合作关系。我们以一流的质量和服务争取全球各地用户的信赖和支持。<br><br>&nbsp;&nbsp;&nbsp;&nbsp;目前公司正处于发展阶段，我们需要一批具有高素质的人才作为创业团队的新力军。同时作为一家成长中的小企业，我们重视并珍惜每一位员工，我们希望每位员工都能在公司业务中发挥重要作用并与公司携手共同成长。<br><br>&nbsp;&nbsp;&nbsp;&nbsp;如果您有志从事跨境电商，并且阳光自信，专业能力NICE，具有超强的学习能力，喜欢挑战自我，我们期待您的加入。</p>
    </el-row>
  </div>
</template>

<script>
import {getRecPost } from "@/api/ims/enterprise/post";
import Link from "@/layout/components/Sidebar/Link";
import {listRec} from "@/api/ims/enterprise/post";

export default {
  name: "Post",
  components: {Link},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 弹出层标题
      title: "",
      recInfo:"",
      workingYearsOption:[],
      educationOption:[],
    };
  },
  mounted() {
    window.that = this;
    this.getRouter();
  },
  created() {
    this.getRecList();
    this.getRouter();
    this.getDicts("working_years").then(response => {
      this.workingYearsOption = response.data;
    });
    this.getDicts("education").then(response => {
      this.educationOption = response.data;
    });
  },
  methods: {
    /** 接收招聘信息页面传递的值postId */
    getRouter(){
      const postId = this.$route.query.id;
      getRecPost(postId).then(response =>{
        this.recInfo = response.data;
        console.log(response)
      });
    },
    /** 查询企业招聘岗位信息列表 */
    getRecList() {
      listRec().then(response => {
        this.recList = response.rows;
        this.total = response.total;
        console.log(response.rows);
      });
    },
    goBack(){
      this.$router.go(-1);
    },
  }
};
</script>
