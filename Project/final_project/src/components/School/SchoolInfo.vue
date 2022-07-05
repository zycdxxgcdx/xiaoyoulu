<template>
  <div style="padding-top: 1%;height: 100%">
    <div class="left_main_content">
      <div  v-infinite-scroll="load"
            v-loading="initLoading"
            element-loading-text="拼命加载中"
            element-loading-background="#f3f9f6"
           element-loading-spinner="el-icon-loading" infinite-scroll-delay="1000" style="overflow-y:auto;padding: 0;margin: 0;" :infinite-scroll-disabled="disabled">
        <transition name="el-zoom-in-center" v-for="item in classInfo">
            <el-card v-show="show" :key="item.classInfo.classId" style="width: 450px;height: 200px;float: left;margin:0 1% 1% 0;" shadow="hover" class="box-card">
              <div slot="header" class="classinfo" style="height: 20%;padding: 0;" @click="toClassInfo(item.classInfo.classId)">
                <div style="float: left;">
                  <el-avatar :src="item.classInfo.headPosition" @error="errorHandler" size="large"></el-avatar>
                </div>
                <div style="float: left;width: 90%">
                  <!--            margin: 1% 0 0 1%;-->
                  <p style="font-size: 14px;margin: 3% 0 0 1%;" v-if="item.classInfo.schoolName.length+item.classInfo.major.length+item.classInfo.grade.length+item.classInfo.className.length <=25">{{item.classInfo.schoolName}} - {{item.classInfo.major}} - {{item.classInfo.grade}} - {{item.classInfo.className}}</p>
                  <p style="font-size: 14px;margin: 1% 0 0 1%;" v-else>{{item.classInfo.schoolName}} - {{item.classInfo.major}} - {{item.classInfo.grade}} - {{item.classInfo.className}}</p>
                </div>
                <div style="clear: both;"></div>
              </div>
              <div style="padding: 1% 0 0 4%;">
                  <div style="float: left;">
                    <p>管理员：</p>
                  </div>
                  <div style="float: left;padding-top: 3.5%;">
                    <router-link class="userInfo" v-for="item1 in item.classAdmin" target="_blank" :to="{path:'/u/'+item1.userId}" style="text-decoration:none;outline: none;color: #000;">
                      <p style="margin: 0;" class="userInfo" >  {{item1.username}}</p>
                    </router-link>
                  </div>
                  <div style="clear: both;"></div>
                <p style="margin: 0 0 3% 0;">班级人数：{{item.classNumbers}}人</p>
              </div>
            </el-card>
        </transition>
        <div v-if="showClass">
          暂无班级
        </div>
      </div>
    </div>
    <router-view/>
  </div>
</template>

<script>
  export default {
    name: "Schoolnfo",
    data() {
      return {
        initLoading:true,
        //每一个班级的信息
        classInfo:[],
        //一次展示几个班级的信息
        classPageSize:6,
        //展示第几页的班级
        classPage:2,
        show:false,
        showClass:false,
        disabled:false,
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", //头像地址
      }
    },
    methods: {
      load(){
        const _this = this;
        setTimeout(()=>{
          axios.get('http://localhost:8181/school/findSchoolInfo/'+this.$route.query.schoolId+'/'+this.classPage+'/'+this.classPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.schoolInfo){
                resp.data.schoolInfo[item].classInfo.headPosition = 'http://localhost:8181/classesphoto/'+resp.data.schoolInfo[item].classInfo.headPosition;
              }
              _this.classInfo = _this.classInfo.concat(resp.data.schoolInfo);
              if(resp.data.schoolInfo.length < 6){
                _this.disabled = true;
              }else{
                _this.classPage++;
              }
            }
          })
        },2000)
      },
      toClassInfo(classId) {
        let routeUrl = this.$router.resolve({
          path: "/classinfo",
          query: {
            classId: classId
          }
        });
        window.open(routeUrl.href, '_blank');
      },
      errorHandler(){
        this.circleUrl = "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg";
      }
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/school/findSchoolInfo/'+this.$route.query.schoolId+'/1/'+this.classPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.schoolInfo){
            resp.data.schoolInfo[item].classInfo.headPosition = 'http://localhost:8181/classesphoto/'+resp.data.schoolInfo[item].classInfo.headPosition;
          }
          _this.classInfo = resp.data.schoolInfo;
          _this.initLoading = false;
          if(resp.data.schoolInfo.length <6){
            _this.disabled = true;
          }
          setTimeout(()=>{
            _this.show = true;
          },1)
          if(resp.data.schoolInfo.length === 0){
            setTimeout(()=>{
              _this.showClass = true;
            },1)
          }
        }
      })
    }
  }
</script>

<style scoped>
  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 70%;
    position: relative;
    margin-left: 18%;
    margin-right: 0;
    float: left;
    height: 100%;
  }
  /deep/ .el-card__body{
    padding: 0;
  }
  /deep/ .el-card__header{
    padding: 4%;
  }
  .classinfo:hover{
    cursor: pointer;
    color: #409EFF;
  }
  .userInfo:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>