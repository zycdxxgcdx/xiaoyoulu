<template>
  <div style="padding-top: 1%;">
    <div class="left_main_content">
      <el-button type="primary" style="float: right;" @click="createSchool">创建学校</el-button>
      <div style="clear: both"></div>
      <el-divider></el-divider>
<!--      已加入的学校-->
      <div style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);padding: 2%">
        <p style="font-size: 14px;font-weight: normal;float: left;">已加入学校</p>
        <div v-if="isInSchoolLoading" style="clear: both">
          加载中...
        </div>
        <div v-else>
<!--          v-if="inSchools.length !== 0"-->
          <div style="float: right;" >
            <el-autocomplete
                    popper-class="select-option"
                    :popper-append-to-body="flag"
                    ref="inputInSchool"
                    size="small"
                    clearable
                    v-model="serachInSchoolAll"
                    :trigger-on-focus="false"
                    :fetch-suggestions="queryInSchoolSearchAsync"
                    placeholder="请输入学校名"
                    @keyup.enter.native="handleInSchoolSubmit"
                    @select="handleInSchoolSelect"
                    @input="changeInSchoolStyle">
              <i class="el-icon-search el-input__icon"
                 id="searchFromAllInSchool"
                 slot="suffix"
                 @click="handleInSchoolSubmit()">
              </i>
            </el-autocomplete>
          </div>
          <div style="clear: both;"></div>

          <div style="margin: 1% 0 0 0 ;padding: 0">
            <div v-if="inSchools.length === 0">
              <p style="margin-left: 3%;">暂无</p>
            </div>
            <div class="inSchool" @click="toSchoolInfo(item.schoolId)" style="float: left;width: 33%;margin-bottom: 2%;height: 50px" v-for="item in inSchools" v-if="inSchools.length !== 0">
                <div>
                  <el-avatar :src="item.headPosition" @error="errorHandler" style="float: left;margin-left: 1%;"></el-avatar>
                </div>
                <div style="margin-left: 3%;height: auto;" v-if="item.schoolName.length > 14">
                  <p style="padding:0 0 0 4%;text-align: left;margin: 1% 1% 0 12%;font-size: 14px">{{item.schoolName}}</p>
                </div>
                <div style="margin-left: 3%;height: auto;" v-else="item.schoolName.length <= 14">
                  <p style="padding:0 0 0 4%;text-align: left;margin: 3% 1% 0 12%;font-size: 14px">{{item.schoolName}}</p>
                </div>
                <div style="clear: both;"></div>
            </div>
            <div style="clear: both"></div>
            <el-pagination
                    hide-on-single-page
                    background
                    layout="prev, pager, next"
                    :page-size="allInSchoolPageSize"
                    @current-change="allInSchoolPage"
                    :total="allInSchoolSize">
            </el-pagination>
          </div>
          <div style="clear: both"></div>
        </div>

      </div>

<!--      全部学校-->
      <div style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);padding: 2%;margin-top: 2%;">
        <p style="font-size: 14px;font-weight: normal;float: left;">全部学校</p>
        <div v-if="isSchoolLoading" style="clear: both">
          加载中...
        </div>
        <div v-else>
          <div style="float: right;">
            <el-autocomplete
                    popper-class="select-option"
                    :popper-append-to-body="flag"
                    ref="inputSchool"
                    size="small"
                    clearable
                    v-model="serachSchoolAll"
                    :trigger-on-focus="false"
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入学校名"
                    @keyup.enter.native="handleSubmit"
                    @select="handleSelect"
                    @input="changeStyle">
              <i class="el-icon-search el-input__icon"
                 id="searchFromAllSchool"
                 slot="suffix"
                 @click="handleSubmit()">
              </i>
            </el-autocomplete>
          </div>
          <div style="clear: both;"></div>

          <div style="margin: 1% 0 0 0 ;padding: 0">
            <div v-if="schools.length === 0">
              <p>暂无</p>
            </div>
            <div class="inSchool" @click="toSchoolInfo(item.schoolId)" style="float: left;width: 33%;margin-bottom: 2%;height: 50px" v-for="item in schools" v-else>
              <div>
                <el-avatar :src="item.headPosition" @error="errorHandler" style="float: left;margin-left: 1%;"></el-avatar>
              </div>
              <div style="margin-left: 3%;height: auto;" v-if="item.schoolName.length > 14">
                <p style="padding:0 0 0 4%;text-align: left;margin: 1% 1% 0 12%;font-size: 14px">{{item.schoolName}}</p>
              </div>
              <div style="margin-left: 3%;height: auto;" v-else="item.schoolName.length <= 14">
                <p style="padding:0 0 0 4%;text-align: left;margin: 3% 1% 0 12%;font-size: 14px">{{item.schoolName}}</p>
              </div>
            </div>

            <div style="clear: both"></div>
            <el-pagination
                    hide-on-single-page
                    background
                    layout="prev, pager, next"
                    :page-size="allSchoolPageSize"
                    @current-change="allSchoolPage"
                    :total="allSchoolSize">
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
    name: "SchoolPage",
    data() {
      return {
        isInSchoolLoading:true,
        isSchoolLoading:true,
        //是否是已加入学校的模糊查询
        isInSchoolFuzzySearch:false,
        //是否是全部学校的模糊查询
        isAllSchoolFuzzySearch:false,
        flag:false,
        //所在学校一页6个
        allInSchoolPageSize:6,
        //全部的所在学校
        allInSchoolSize:0,
        //全部学校一页15个
        allSchoolPageSize:15,
        inSchools:[],
        //总共有多少个学校
        allSchoolSize:0,

        //所加入的学校输入框的输入值
        serachInSchoolAll:'',
        //查询全部学校中输入框输入值
        serachSchoolAll:'',
        schools:[],
        searchNothing:[{value:'无'}],
        size:6,
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", //头像地址
      };
    },
    methods: {
      toSchoolInfo(schoolId){
        let routeUrl = this.$router.resolve({
          path: "/schoolInfo",
          query: {
            schoolId:schoolId
          }
        });
        window.open(routeUrl.href, '_blank');
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
      allInSchoolPage(currentPage){
        const _this = this;
        if(this.isInSchoolFuzzySearch){
          console.log('http://localhost:8181/school/searchInSchool/'+this.$store.state.UserId+'/'+this.serachInSchoolAll+'/'+currentPage+'/'+this.allInSchoolPageSize)
          axios.get('http://localhost:8181/school/searchInSchool/'+this.$store.state.UserId+'/'+this.serachInSchoolAll+'/'+currentPage+'/'+this.allInSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.inSchools = resp.data.schools;
            }
          })
        }else{
          console.log('http://localhost:8181/school/findAllInSchool/'+this.$store.state.UserId+'/'+currentPage+'/'+this.allInSchoolPageSize)
          axios.get('http://localhost:8181/school/findAllInSchool/'+this.$store.state.UserId+'/'+currentPage+'/'+this.allInSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.inSchools = resp.data.schools;
            }
          })
        }

      },
      allSchoolPage(currentPage){
        const _this = this;
        if(this.isAllSchoolFuzzySearch){
          console.log('http://localhost:8181/school/searchSchool/'+this.serachSchoolAll+'/'+currentPage+'/'+this.allSchoolPageSize);
          axios.get('http://localhost:8181/school/searchSchool/'+this.serachSchoolAll+'/'+currentPage+'/'+this.allSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.schools = resp.data.schools;
            }
          })
        }else{
          axios.get('http://localhost:8181/school/findAllSchool/'+currentPage+'/'+this.allSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.schools = resp.data.schools;
            }
          })
        }

      },

      changeInSchoolStyle(){
        const _this = this;
        if(this.serachInSchoolAll === '') {
          axios.get('http://localhost:8181/school/findAllInSchool/' + this.$store.state.UserId + '/1/' + this.allInSchoolPageSize).then(function (resp) {
            console.log(resp);
            if (resp.data.msg === '查询成功') {
              for (let item in resp.data.schools) {
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/' + resp.data.schools[item].headPosition;
              }
              _this.allInSchoolSize = resp.data.allSchoolsNums;
              _this.inSchools = resp.data.schools;
              _this.isInSchoolFuzzySearch = false;
            }
          })
        }
      },
      changeStyle() {
        const _this = this;
        if(this.serachSchoolAll === ''){
          axios.get('http://localhost:8181/school/findAllSchool/1/'+this.allSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.allSchoolSize = resp.data.allSchoolsNums;
              _this.schools = resp.data.schools;
              _this.isAllSchoolFuzzySearch = false;
            }
          })
        }
      },

      handleInSchoolSubmit(){
        const _this = this;
        if(this.serachInSchoolAll !== '' && this.check_input(this.serachInSchoolAll)){
          axios.get('http://localhost:8181/school/searchInSchool/'+this.$store.state.UserId+'/'+this.serachInSchoolAll+'/1/'+this.allInSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.inSchools = resp.data.schools;
              _this.allInSchoolSize = resp.data.allSchoolsNums;
              _this.isInSchoolFuzzySearch = true;
            }
          })
          this.changeInSchoolStyle("none", ".el-autocomplete-suggestion"); //用户enter或者点击搜索后停掉建议框
        }
      },
      handleSubmit(){
        //再全部学校中查询时执行的enter操作
        console.log("enter");
        const _this = this;
        if(this.serachSchoolAll !== '' && this.check_input(this.serachSchoolAll)){
          axios.get('http://localhost:8181/school/searchSchool/'+this.serachSchoolAll+'/1/'+this.allSchoolPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schools){
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
              }
              _this.schools = resp.data.schools;
              _this.allSchoolSize = resp.data.allSchoolsNums;
              _this.isAllSchoolFuzzySearch = true;
            }
          })
          this.changeStyle("none", ".el-autocomplete-suggestion"); //用户enter或者点击搜索后停掉建议框
        }
      },
      queryInSchoolSearchAsync(queryString,callback){
        const _this = this;
        if(queryString !== '' && this.check_input(queryString)){
          axios.get('http://localhost:8181/school/searchSchoolForName/'+this.$store.state.UserId+'/'+queryString).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              if(resp.data.schools.length === 0){
                callback(_this.searchNothing);
              }else{
                callback(resp.data.schools);
              }
            }
          })
        }else{
          callback(_this.searchNothing);
        }
      },
      querySearchAsync(queryString,callback){
        const _this = this;
        if(queryString !== '' && this.check_input(queryString)){
          axios.get('http://localhost:8181/school/searchSchoolForName/'+queryString).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              if(resp.data.schools.length === 0){
                callback(_this.searchNothing);
              }else{
                callback(resp.data.schools);
              }
            }
          })
        }else{
          callback(_this.searchNothing);
        }
      },
      handleInSchoolSelect(item){
        this.$refs['inputInSchool'].focus();
        this.serachInSchoolAll = item.value;
      },
      handleSelect(item) {
        this.$refs['inputSchool'].focus();
        this.serachSchoolAll = item.value;
      },
      createSchool(){
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
              _this.$router.push('/createSchool');
            }
          })
        }else{
          this.$router.push('/school/login');
        }
      },
      errorHandler(){
        this.circleUrl = "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";
      }
    },
    created() {
      const _this = this;
      if(this.$store.state.Authorization) {
        axios.get('http://localhost:8181/school/findAllInSchool/' + this.$store.state.UserId + '/1/' + this.allInSchoolPageSize).then(function (resp) {
          console.log(resp);
          if (resp.data.msg === '查询成功') {
            for (let item in resp.data.schools) {
              resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/' + resp.data.schools[item].headPosition;
            }
            _this.allInSchoolSize = resp.data.allSchoolsNums;
            _this.inSchools = resp.data.schools;
            _this.isInSchoolLoading = false;
          }
        })
      }else{
        this.isInSchoolLoading = false;
      }
      axios.get('http://localhost:8181/school/findAllSchool/1/'+this.allSchoolPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.schools){
            resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/'+ resp.data.schools[item].headPosition;
          }
          _this.allSchoolSize = resp.data.allSchoolsNums;
          _this.schools = resp.data.schools;
          _this.isSchoolLoading = false;
        }
      })
    }
  }
</script>

<style scoped>

  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 55%;
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
  #searchFromAllInSchool:hover{
    cursor:pointer;
  }
  /deep/ .select-option {
    padding-bottom: 5%!important;
  }
  .inSchool:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>

