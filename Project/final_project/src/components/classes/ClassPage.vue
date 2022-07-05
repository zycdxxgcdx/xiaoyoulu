<template>
  <div style="padding-top: 1%;">
    <div class="left_main_content">
      <el-button type="primary" style="float: right;" @click="createClasses">创建班级</el-button>
      <div style="clear: both"></div>
      <el-divider></el-divider>
      <!--      已加入的学校-->
      <div style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);padding: 2%">
        <p style="font-size: 14px;font-weight: normal;float: left;">已加入班级</p>
        <div v-if="isInClassLoading" style="clear: both">
          加载中...
        </div>
        <div v-else>
          <div style="float: right;">
            <el-input
                    size="small"
                    clearable
                    v-model="serachInClassAll"
                    :trigger-on-focus="false"
                    placeholder="请输入学校、专业或班级名"
                    @keyup.enter.native="handleInClassSubmit"
                    @input="changeInclassStyle">
              <i class="el-icon-search el-input__icon"
                 id="searchFromInSchool"
                 slot="suffix"
                 @click="handleInClassSubmit">
              </i>
            </el-input>
          </div>
          <div style="clear: both;"></div>

          <div style="margin-left: 3%;" v-if="inClass.length === 0"><p>暂无</p></div>
          <div style="margin: 0;padding: 0">
            <div style="float: left;width: 50%;margin-bottom: 2%;height: 50px"  v-for="item in inClass" @click="toClassInfo(item.classId)" class="inClass">
              <div>
                <el-avatar :src="item.headPosition" @error="errorHandler" style="float: left;margin-left: 1%;"></el-avatar>
              </div>
              <div style="margin-left: 0;height: auto;" v-if="item.schoolName.length+item.major.length+item.grade.length+item.className.length <= 25">
                <p style="padding:0 0 0 0;text-align: left;margin: 2% 1% 0 12%;font-size: 14px">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</p>
              </div>
              <div style="margin-left: 0;height: auto;" v-else="item.schoolName.length+item.major.length+item.grade.length+item.className.length > 25">
                <p style="padding:0 0 0 0;text-align: left;margin: 0.5% 1% 0 12%;font-size: 14px">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</p>
              </div>
            </div>
            <div style="clear: both"></div>
            <el-pagination
                    hide-on-single-page
                    background
                    layout="prev, pager, next"
                    :page-size="inClassSize"
                    @current-change="allInClassPage"
                    :total="inClassTotal">
            </el-pagination>
          </div>
          <div style="clear: both"></div>
        </div>
      </div>

      <!--      全部班级-->
      <div style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);padding: 2%;margin-top: 2%;">
        <p style="font-size: 14px;font-weight: normal;float: left;">全部班级</p>
        <div v-if="isClassLoading" style="clear: both">
          加载中...
        </div>
        <div v-else>
          <div style="float: right;">
            <el-input
                    size="small"
                    clearable
                    v-model="serachClassAll"
                    :trigger-on-focus="false"
                    placeholder="请输入学校、专业或班级名"
                    @keyup.enter.native="handleSubmit"
                    @input="changeStyle">
              <i class="el-icon-search el-input__icon"
                 id="searchFromAllSchool"
                 slot="suffix"
                 @click="handleSubmit()">
              </i>
            </el-input>
          </div>
          <div style="clear: both;"></div>

          <div style="margin: 1% 0 0 0 ;padding: 0">
            <div v-if="classes.length === 0">
              <p>暂无</p>
            </div>
            <div style="float: left;width: 50%;margin-bottom: 2%;height: 50px"  v-for="item in classes" @click="toClassInfo(item.classId)" class="inClass">
              <div>
                <el-avatar :src="item.headPosition" @error="errorHandler" style="float: left;margin-left: 1%;"></el-avatar>
              </div>
              <div style="margin-left: 0;height: auto;" v-if="item.schoolName.length+item.major.length+item.grade.length+item.className.length <= 25">
                <p style="padding:0 0 0 0;text-align: left;margin: 2% 1% 0 12%;font-size: 14px">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</p>
              </div>
              <div style="margin-left: 0;height: auto;" v-else="item.schoolName.length+item.major.length+item.grade.length+item.className.length > 25">
                <p style="padding:0 0 0 0;text-align: left;margin: 0.5% 1% 0 12%;font-size: 14px">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</p>
              </div>
            </div>


            <div style="clear: both"></div>
            <el-pagination
                    hide-on-single-page
                    background
                    layout="prev, pager, next"
                    :page-size="allClassPageSize"
                    @current-change="allClassPage"
                    :total="allClassSize">
            </el-pagination>
          </div>
          <div style="clear: both"></div>
        </div>

      </div>


    </div>
        <router-view/>
  </div>

</template>

<script>
  export default {
    name: "ClassPage",
    data() {
      return {
        isInClassLoading:true,
        isClassLoading:true,
        //是否是已加入班级的模糊查询
        isInClassFuzzySearch:false,
        //是否是全部班级的模糊查询
        isAllClassFuzzySearch:false,
        //所在班级一页6个
        inClassSize:6,
        //所在班级的个数
        inClassTotal:0,
        //全部学校一页15个
        allClassPageSize:10,
        inClass:[],
        //总共有多少个学
        allClassSize:0,

        //所在学校的输入框的输入值
        serachInClassAll:'',
        //查询全部班级中输入框输入值
        serachClassAll:'',
        classes:[],
        searchNothing:[{value:'无'}],
        size:6,
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", //头像地址
      };
    },
    methods: {
      toClassInfo(classId){
        const _this = this;
        if(localStorage.getItem('Authorization')){
          axios.get('http://localhost:8181/account/checkAccountState').then(function (resp) {
            console.log(resp);
            if(resp.data.msg !== '账号登录状态检测成功'){
              localStorage.removeItem('Authorization');
              localStorage.removeItem('AccountName');
              localStorage.removeItem('UserId');
              _this.$message.error("登录信息过期，请重新登录");
              setTimeout(()=>{
                _this.$router.go(0);
              },500)
            }else {
              let routeUrl = _this.$router.resolve({
                path: "/classinfo",
                query: {
                  classId:classId
                }
              });
              window.open(routeUrl.href, '_blank');
            }
          })
        }else {
          this.$router.push('/class/login');
        }
      },
      allInClassPage(currentPage){
        const _this = this;
        if(this.isInClassFuzzySearch) {
          console.log('http://localhost:8181/classes/searchInClass/' + this.$store.state.UserId + '/' + this.serachInClassAll + '/' + currentPage + '/' + this.inClassSize)
          axios.get('http://localhost:8181/classes/searchInClass/' + this.$store.state.UserId + '/' + this.serachInClassAll + '/' + currentPage + '/' + this.inClassSize).then(function (resp) {
            console.log(resp);
            if (resp.data.msg === '查询成功') {
              for (let item in resp.data.classes) {
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/' + resp.data.classes[item].headPosition;
              }
              _this.inClass = resp.data.classes;
              _this.allClassSize = resp.data.allClassNums;
            }
          })
        }else{
          axios.get('http://localhost:8181/classes/findInClasses/'+this.$store.state.UserId+'/'+currentPage+'/'+this.inClassSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.inClassTotal = resp.data.allClassNums;
              _this.inClass = resp.data.classes;
            }
          })
        }
      },
      allClassPage(currentPage){
        const _this = this;
        if(this.isAllClassFuzzySearch){
          axios.get('http://localhost:8181/classes/searchClass/'+this.serachClassAll+'/'+currentPage+'/'+this.allClassPageSize).then(function (resp) {
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.classes = resp.data.classes;
              _this.allClassSize = resp.data.allClassNums;
            }
          })
        }else{
          axios.get('http://localhost:8181/classes/findAllClasses/'+currentPage+'/'+this.allClassPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.classes = resp.data.classes;
            }
          })
        }
      },
      check_input(text){
        //判断发帖内容中是否全为空格
        var flag = false;
        let str = text.replace(/\s+/g,""); //去除空格
        let str2 = str.replace(/&nbsp+/g,"");//再去除&nbsp;
        for(let index in str2){
          console.log(str2[index]);
          if(str2[index] !== ';'){
            flag = true;
            break;
          }
        }
        return flag;
      },
      changeInclassStyle(){
        const _this = this;
        if(this.serachInClassAll === ''){
          axios.get('http://localhost:8181/classes/findInClasses/'+this.$store.state.UserId+'/1/'+this.inClassSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.inClassTotal = resp.data.allClassNums;
              _this.inClass = resp.data.classes;
              _this.isInClassFuzzySearch = false;
            }
          })
        }else if(this.serachInClassAll !== '' && this.check_input(this.serachInClassAll)){
          axios.get('http://localhost:8181/classes/searchInClass/'+this.$store.state.UserId+'/'+this.serachInClassAll+'/1/'+this.inClassSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.inClass = resp.data.classes;
              _this.inClassTotal = resp.data.allClassNums;
              _this.isInClassFuzzySearch = true;
            }
          })
        }
      },
      changeStyle() {
        const _this = this;
        if(this.serachClassAll === ''){
          axios.get('http://localhost:8181/classes/findAllClasses/1/'+this.allClassPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.allClassSize = resp.data.allClassNums;
              _this.classes = resp.data.classes;
              _this.isAllClassFuzzySearch = false;
            }
          })
        }else if(this.serachClassAll !== '' && this.check_input(this.serachClassAll)){
          axios.get('http://localhost:8181/classes/searchClass/'+this.serachClassAll+'/1/'+this.allClassPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.classes = resp.data.classes;
              _this.allClassSize = resp.data.allClassNums;
              _this.isAllClassFuzzySearch = true;
            }
          })
        }
      },
      handleInClassSubmit(){
        const _this = this;
        if(this.serachInClassAll !== '' && this.check_input(this.serachInClassAll)){
          axios.get('http://localhost:8181/classes/searchInClass/'+this.$store.state.UserId+'/'+this.serachInClassAll+'/1/'+this.inClassSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.inClass = resp.data.classes;
              _this.allClassSize = resp.data.allClassNums;
              _this.isInClassFuzzySearch = true;
            }
          })
        }
      },
      handleSubmit(){
        //再全部学校中查询时执行的enter操作
        console.log("enter");
        const _this = this;
        if(this.serachClassAll !== '' && this.check_input(this.serachClassAll)){
          axios.get('http://localhost:8181/classes/searchClass/'+this.serachClassAll+'/1/'+this.allClassPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.classes){
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
              }
              _this.classes = resp.data.classes;
              _this.allClassSize = resp.data.allClassNums;
              _this.isAllClassFuzzySearch = true;
            }
          })
        }
      },
      createClasses(){
        const _this = this;
        if(localStorage.getItem('Authorization')){
          axios.get('http://localhost:8181/account/checkAccountState').then(function (resp) {
            console.log(resp);
            if(resp.data.msg !== '账号登录状态检测成功'){
              localStorage.removeItem('Authorization');
              localStorage.removeItem('AccountName');
              localStorage.removeItem('UserId');
              _this.$message.error("登录信息过期，请重新登录");
              setTimeout(()=>{
                _this.$router.go(0);
              },500)
            }else {
              _this.$router.push('/createClass');
            }
          })
        }else{
          this.$router.push('/class/login');
        }
      },
      errorHandler(){
        this.circleUrl = "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";
      }
    },
    created() {
      const _this = this;
      if(this.$store.state.Authorization) {
        axios.get('http://localhost:8181/classes/findInClasses/' + this.$store.state.UserId + '/1/' + this.inClassSize).then(function (resp) {
          console.log(resp);
          if (resp.data.msg === '查询成功') {
            for (let item in resp.data.classes) {
              resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/' + resp.data.classes[item].headPosition;
            }
            _this.inClassTotal = resp.data.allClassNums;
            _this.inClass = resp.data.classes;
            _this.isInClassLoading = false;
          }
        })
      }else{
        _this.isInClassLoading = false;
      }
      axios.get('http://localhost:8181/classes/findAllClasses/1/'+this.allClassPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.classes){
            resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/'+ resp.data.classes[item].headPosition;
          }
          _this.allClassSize = resp.data.allClassNums;
          _this.classes = resp.data.classes;
          _this.isClassLoading = false;
        }
      })
    }
  }
</script>

<style scoped>
  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 60%;
    position: relative;
    margin-left: 20%;
    margin-right: 0;
    float: left;
    height: 100%;
  }
  .left_main_content_child{
    margin-bottom: 15px;
    padding:2px 0 40px 15px;
    background-color:  #ffffff;
    border-radius: 3%;
  }
  /*.left_main_content_child >.left_main_content_child_msg{*/
  /*  margin-bottom: 10px;*/
  /*  !*border: 1px red solid;*!*/
  /*}*/
  .left_main_content_child >span{
    padding-right: 16px;
    font-size: 10px;
    font-weight: bold;
  }
  .left_main_content_child>.left_main_content_child_spanStyleTwo{
    margin-left: 1%;
    color:rgba(0,0,0,.5);
    cursor: pointer ;
  }
  .left_main_content_child>.left_main_content_child_spanStyleTwo:hover{
    color: #409EFF;
  }
  .left_main_content_child>.left_main_content_child_spanStyleThreeToSix{
    color:rgba(0,0,0,.5) ;
  }
  .el-divider--horizontal{
    margin:0 0;
    padding: 0;
    border-top: 1px dashed #e1e3e5;
    background: 0 0;
  }
  /*对回复分页按钮样式的修改*/
  /deep/  .el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color:#f3f9f6;
    color: #409EFF;
  }
  /deep/  .el-pagination.is-background .el-pager li:not(.disabled) {
    background-color:#f3f9f6;
  }
  /deep/  .el-pagination.is-background .btn-next {
    background-color:#f3f9f6;
  }
  /deep/  .el-pagination.is-background .btn-prev {
    background-color:#f3f9f6;
  }
  #searchFromAllSchool:hover{
    cursor: pointer;
  }
  #searchFromInSchool:hover{
    cursor: pointer;
  }
  .inClass:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>
