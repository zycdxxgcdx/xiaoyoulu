<template>
  <div>
    <ul v-loading="initLoading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"   infinite-scroll-delay="1000" class="infinite-list" v-infinite-scroll="load"  :infinite-scroll-disabled="disabled" style="overflow:auto;padding-left: 2%;margin-top: 1%;">
      <li class="infinite-list-item" style="list-style: none;">
        <div  @click="toClassInfo(item.classId)" class="main_info" v-for="item in inClass" style="width: 45%;float: left;">
            <div style="width: 15%;height: 10%;float: left;">
              <el-avatar :src="item.headPosition" style="margin-left: 1%;"></el-avatar>
            </div>
            <div class="schoolName"  style="float: left;width: 80%; height: 10%;" v-if="item.schoolName.length+item.major.length+item.grade.length+item.className.length <=17">
              <p style="word-break: break-all;margin: 3% 0 0 0;font-size: 14px">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</p>
            </div>
            <div class="schoolName" style="float: left;height: 10%;width: 80%" v-else="item.schoolName.length+item.major.length+item.grade.length+item.className.length >17">
              <p style="word-break: break-all;margin: 0;font-size: 14px">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</p>
            </div>
            <div style="clear: both;"></div>
        </div>
      </li>
    </ul>
    <p v-if="loading" style="text-align: center">加载中...</p>
    <p v-if="noMore" style="text-align: center;">没有更多了...</p>
  </div>
</template>

<script>
  export default {
    name: "MyClass",
    data(){
      return {
        initLoading:true,
        loading:false,
        noMore:false,
        disabled:false,
        userId:this.$route.params.userId,
        allInClassPageSize:10,
        currentPage:2,
        inClass:[],
      }
    },
    methods:{
      toClassInfo(classId){
        let routeUrl = this.$router.resolve({
          path: "/classinfo",
          query: {
            classId:classId
          }
        });
        window.open(routeUrl.href, '_blank');
      },
      load(){
        const _this = this;
        this.loading = true;
        setTimeout(()=>{
          axios.get('http://localhost:8181/classes/findInClasses/' + this.$route.params.userId  + '/'+this.currentPage+'/' + this.allInClassPageSize).then(function (resp) {
            console.log(resp);
            if (resp.data.msg === '查询成功') {
              for (let item in resp.data.classes) {
                resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/' + resp.data.classes[item].headPosition;
              }
              _this.inClass =  _this.inClass.concat(resp.data.classes);
              _this.currentPage++;
              if(resp.data.classes.length < 10){
                _this.disabled = true;
                _this.noMore = true;
                _this.loading = false;
              }
            }
          })
        },2000)
      },
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/classes/findInClasses/' + this.$route.params.userId  + '/1/' + this.allInClassPageSize).then(function (resp) {
        console.log(resp);
        if (resp.data.msg === '查询成功') {
          for (let item in resp.data.classes) {
            resp.data.classes[item].headPosition = 'http://localhost:8181/classesphoto/' + resp.data.classes[item].headPosition;
          }
          _this.inClass = resp.data.classes;
          _this.initLoading = false;
          if(resp.data.classes.length < 10){
            _this.disabled = true;
            _this.noMore = true;
          }
        }
      })
    }
  }
</script>

<style scoped>
  .main_info{
    box-shadow: 0 2px 4px rgba(0, 0, 0, .12);
    margin-bottom: 1%;
    margin-right: 1%;
    padding:2% 2% 2% 1%;
    background-color:  #ffffff;
    border-radius: 3%;
  }
  .schoolName:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>