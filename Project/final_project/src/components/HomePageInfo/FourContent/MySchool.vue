<template>
  <div>
    <ul v-loading="initLoading"
        element-loading-text="拼命加载中"
        element-loading-spinner="el-icon-loading"   infinite-scroll-delay="1000" class="infinite-list" v-infinite-scroll="load"  :infinite-scroll-disabled="disabled" style="overflow:auto;padding-left: 2%;margin-top: 1%;">
      <li class="infinite-list-item" style="list-style: none;">
        <div @click="toSchoolInfo(item.schoolId)" class="main_info" v-for="item in inSchools" style="width: 30%;float: left;">
          <div style="width: 20%;height: 10%;float: left;">
            <el-avatar :src="item.headPosition" style="margin-left: 1%;"></el-avatar>
          </div>
          <div style="margin-left: 3%;height: auto;" v-if="item.schoolName.length > 10">
            <p style="margin: 0 0 0 5%;font-size: 14px">{{item.schoolName}}</p>
          </div>
          <div class="schoolName" style="float: left;height: 10%;width: 80%" v-else="item.schoolName.length <= 10">
            <p style="margin: 5% 0 0 5%;font-size: 14px">{{item.schoolName}}</p>
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
    name: "MySchool",
    data(){
      return {
        initLoading:true,
        loading:false,
        noMore:false,
        disabled:false,
        userId:this.$route.params.userId,
        allInSchoolPageSize:12,
        currentPage:2,
        inSchools:[],
      }
    },
    methods:{
      toSchoolInfo(schoolId){
        let routeUrl = this.$router.resolve({
          path: "/schoolInfo",
          query: {
            schoolId:schoolId
          }
        });
        window.open(routeUrl.href, '_blank');
      },
      load(){
        const _this = this;
        this.loading = true;
        setTimeout(()=>{
          axios.get('http://localhost:8181/school/findAllInSchool/' + this.$route.params.userId + '/'+this.currentPage+'/' + this.allInSchoolPageSize).then(function (resp) {
            console.log(resp);
            if (resp.data.msg === '查询成功') {
              for (let item in resp.data.schools) {
                resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/' + resp.data.schools[item].headPosition;
              }
              _this.inSchools = _this.inSchools.concat(resp.data.schools);
              _this.currentPage++;
              if(resp.data.schools.length < 12){
                _this.disabled = true;
                _this.loading = false;
                _this.noMore = true;
              }
            }
          })
        },2000)
      },
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/school/findAllInSchool/' + this.$route.params.userId + '/1/' + this.allInSchoolPageSize).then(function (resp) {
        console.log(resp);
        if (resp.data.msg === '查询成功') {
          for (let item in resp.data.schools) {
            resp.data.schools[item].headPosition = 'http://localhost:8181/schoolphoto/' + resp.data.schools[item].headPosition;
          }
          _this.inSchools = resp.data.schools;
          _this.initLoading = false;
          if(resp.data.schools.length < 12){
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
    padding:2% 1% 2% 1%;
    background-color:  #ffffff;
    border-radius: 3%;
  }
  .schoolName:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>