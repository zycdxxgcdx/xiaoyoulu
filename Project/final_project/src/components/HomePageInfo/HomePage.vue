<template>
  <div style="margin-top: 1%;">
    <div class="main_content">
      <div class="main_profile">
        <div style="float: left;margin-top: 3%;margin-left: 5%;">
          <el-avatar :size="90" :src="circleUrl" fit="fill"></el-avatar>
        </div>
        <div style="float: left;margin-top: 0.5%;margin-left: 5%;">
          <h1 style="filter:alpha(Opacity=85);-moz-opacity:1;opacity: 1;">{{this.username}} </h1>
          <el-button type="primary" @click="toProfile" v-if="this.$route.params.userId === this.$store.state.UserId">编辑个人资料</el-button>
          <el-button type="primary" @click="toFollow" :disabled="followDisabled" v-if="this.$route.params.userId !== this.$store.state.UserId && isfollow === false">关注</el-button>
          <el-button type="danger"  @click="toCancelFollow" :disabled="cancelFollowDisabled" v-if="this.$route.params.userId !== this.$store.state.UserId && isfollow === true">取消关注</el-button>
          <el-button type="primary" @click="toPrivateChat" v-if="this.$route.params.userId !== this.$store.state.UserId">私聊</el-button>
        </div>
      </div>
      <div class="main_info">
        <div style="background: #ffffff;">
          <el-tabs v-model="activeName"  @tab-click="handleClick" style="margin-left: 2%;">
            <el-tab-pane label="帖子" name="homeInfo"></el-tab-pane>
            <el-tab-pane label="动态" name="dynamic"></el-tab-pane>
            <el-tab-pane label="学校" name="school"></el-tab-pane>
            <el-tab-pane label="班级" name="class"></el-tab-pane>
          </el-tabs>
        </div>
        <router-view/>
      </div>
    </div>
    <div class="right_main_content">
        <div @click="followList" class="follow" style="float: left;margin-left: 14%;text-align: center;">
          关注
          <div style="font-weight: bold;padding-top: 8%;">
            {{followNums}}
          </div>
        </div>
        <div @click="fansList" class="fans" style="float: right;margin-right: 14%;text-align: center;">
          粉丝
          <div style="font-weight: bold;padding-top: 8%;">
            {{fansNums}}
          </div>
        </div>
    </div>
    <div id="followAndFans"  style="float: left;margin:1% 15% 0 1%;background-color: #ffffff;width: 13%;padding: 1% 1% 0 1%">
      <span style="font-size: 16px;color:rgba(0, 0, 0, 0.5)" v-if="this.$route.params.userId === this.$store.state.UserId &&followFansChange === 0 ">我的关注</span>
      <span style="font-size: 16px;color:rgba(0, 0, 0, 0.5)" v-if="this.$route.params.userId !== this.$store.state.UserId &&followFansChange === 0 ">ta的关注</span>
      <span style="font-size: 16px;color:rgba(0, 0, 0, 0.5)" v-if="this.$route.params.userId === this.$store.state.UserId &&followFansChange === 1">我的粉丝</span>
      <span style="font-size: 16px;color:rgba(0, 0, 0, 0.5)" v-if="this.$route.params.userId !== this.$store.state.UserId &&followFansChange === 1 ">ta的粉丝</span>
      <div style="clear: both;margin-bottom: 5%;"></div>
      <div style="text-align: center" v-if="users.length === 0">暂无</div>
      <div v-else v-for="item in users" style="margin-top: 10%;">
        <router-link target="_blank"  :to="'/u/'+item.userId" class="users" style="color: #000;">
          <el-avatar :src="item.headPosition" fit="fill" style="float: left;"></el-avatar>
          <span style="float: left;margin:5% 0 0 4%;font-size: 14px">{{item.username}}</span>
          <div style="clear: both;"></div>
        </router-link>
      </div>
      <div style="clear: both;margin-bottom: 10%;"></div>
        <el-pagination
                small
                :current-page.sync="currentPage"
                hide-on-single-page
                :page-size="pagesize"
                style="margin-left: 0;"
                layout="prev, pager, next"
                :total="totalNums"
                @current-change="page">
        </el-pagination>
    </div>
  </div>
</template>

<script>
  export default {
    name: "HomePage",
    data(){
      return{
        // 0 follow 1 fan
        followFansChange:0,
        currentPage:1,
        followDisabled:false,
        cancelFollowDisabled:false,
        isfollow:false,
        pagesize:10,
        username:'',
        //存放关注以及粉丝的数组
        users:[],
        followNums:0,
        totalNums:0,
        fansNums:0,
        activeName: 'homeInfo',
        circleUrl:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      }
    },
    methods:{
      followList(){
        const _this = this;
        this.followFansChange = 0;
        this.totalNums = this.followNums;
        this.currentPage = 1;
        axios.get('http://localhost:8181/user/findFollows/'+this.$route.params.userId+'/1/'+this.pagesize).then(function (resp) {
          if(resp.data.msg === '查询成功'){
            for(let item in resp.data.followUsers){
              resp.data.followUsers[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.followUsers[item].headPosition;
            }
            _this.users = resp.data.followUsers;
          }
        })
      },
      fansList(){
        console.log(this.currentPage)
        const _this = this;
        this.followFansChange = 1;
        this.totalNums = this.fansNums;
        this.currentPage = 1;
        axios.get('http://localhost:8181/user/findFans/'+this.$route.params.userId+'/1/'+this.pagesize).then(function (resp) {
          if(resp.data.msg === '查询成功'){
            for(let item in resp.data.fanUsers){
              resp.data.fanUsers[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.fanUsers[item].headPosition;
            }
            _this.users = resp.data.fanUsers;
          }
        })
      },
      page(currentPage) {
        const _this = this;
        document.getElementById('followAndFans').scrollIntoView();
        console.log("page:"+currentPage)
        if(this.followFansChange === 0){
          axios.get('http://localhost:8181/user/findFollows/'+this.$route.params.userId+'/'+currentPage+'/'+this.pagesize).then(function (resp) {
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.followUsers){
                resp.data.followUsers[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.followUsers[item].headPosition;
              }
              _this.users = resp.data.followUsers;
            }
          })
        }else if(this.followFansChange === 1){
          axios.get('http://localhost:8181/user/findFans/'+this.$route.params.userId+'/'+currentPage+'/'+this.pagesize).then(function (resp) {
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.fanUsers){
                resp.data.fanUsers[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.fanUsers[item].headPosition;
              }
              _this.users = resp.data.fanUsers;
            }
          })
        }else{
         _this.users = [];
        }
      },
      toPrivateChat(){
        const _this = this;
        axios.post('http://localhost:8181/socket/addSocketList/' + this.$store.state.UserId + '/' + this.$route.params.userId + '/0').then(function (resp) {
          console.log(resp)
          if(resp.data.msg === '更新消息列表成功'){
            let routeUrl = _this.$router.resolve({
              path: '/message/'+_this.$store.state.UserId+'/whisper/'+_this.$route.params.userId,
              query:{type:0}
            });
            window.open(routeUrl.href, '_blank');
          }
        })
      },
      toProfile(){
        this.$router.push('/u/'+this.$store.state.UserId+'/profile')
      },
      toFollow(){
        const _this = this;
        axios.post('http://localhost:8181/user/follow/'+this.$store.state.UserId+'/'+this.$route.params.userId).then(function (resp) {
            if(resp.data.msg === '关注成功'){
              _this.isfollow = true;
              _this.$message.success("关注成功")
              _this.cancelFollowDisabled = true;
            }
            setTimeout(()=>{
              _this.cancelFollowDisabled = false;
            },2000)
        })
      },
      toCancelFollow(){
        const _this = this;
        axios.delete('http://localhost:8181/user/cancelFollow/'+this.$store.state.UserId+'/'+this.$route.params.userId).then(function (resp) {
          if(resp.data.msg === '取消关注成功'){
            _this.isfollow = false;
            _this.$message.success("取消关注成功")
            _this.followDisabled = true;
          }
          setTimeout(()=>{
            _this.followDisabled = false;
          },2000)
        })
      },
      handleClick(tab) {
        console.log(tab.label);
        if(tab.name === 'homeInfo'){
          this.$router.push('/u/'+this.$route.params.userId);
        }else if(tab.name ===  'dynamic'){
          this.$router.push('/u/'+this.$route.params.userId+'/dynamic');
        }else if(tab.name ===  'school'){
          this.$router.push('/u/'+this.$route.params.userId+'/school');
        }else if(tab.name ===  'class'){
          this.$router.push('/u/'+this.$route.params.userId+'/class');
        }
      }
    },
    created() {
      const _this = this;
      this.$router.push('/u/'+this.$route.params.userId);
      axios.get('http://localhost:8181/user/userInfo/'+this.$route.params.userId).then(function (resp) {
        _this.username = resp.data.userInfo.username;
        if(resp.data.userInfo.headPosition!==null){
          _this.circleUrl = 'http://localhost:8181/profilephoto/'+resp.data.userInfo.headPosition;
        }
      })
      if(this.$store.state.UserId !== this.$route.params.userId) {
        axios.get('http://localhost:8181/user/isFollow/' + this.$store.state.UserId + '/' + this.$route.params.userId).then(function (resp) {
          console.log(resp);
          if (resp.data.msg === '已关注') {
            _this.isfollow = true;
          }
        })
      }
      axios.get('http://localhost:8181/user/findFollowAndFansNums/'+this.$route.params.userId).then(function (resp) {
        if(resp.data.msg === '查询成功'){
          _this.followNums = resp.data.followNums;
          _this.fansNums = resp.data.fansNums;
          _this.totalNums = resp.data.followNums;
        }
      })
      axios.get('http://localhost:8181/user/findFollows/'+this.$route.params.userId+'/1/'+this.pagesize).then(function (resp) {
        console.log(resp)
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.followUsers){
            resp.data.followUsers[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.followUsers[item].headPosition;
          }
          _this.users = resp.data.followUsers;
        }
      })
    }
  }
</script>

<style scoped>
  .main_content{
    /*background-color:  #ffffff;*/
    width: 50%;
    position: relative;
    margin-left: 17%;
    padding-top: 0;
    height: 100%;
    float: left;
    /*border: 1px solid red;*/
  }
  .main_profile{
    /*border: 1px solid red;*/
    height: 140px;
    background-color: #ffffff;
  }
  .main_info{
    margin-top: 2%;
    /*border: 1px solid red;*/
    background-color: #ffffff;
  }

  /deep/ .el-tabs__item{
    font-size:16px;
  }
  .right_main_content{
    padding: 1%;
    margin-left: 1%;
    position: relative;
    width: 13%;
    float: left;
    background-color: #ffffff;
  }
  .follow:hover{
    cursor: pointer;
    color: #409EFF;
  }
  .fans:hover{
    cursor: pointer;
    color: #409EFF;
  }
  /deep/ .el-pagination .btn-next .el-icon {
    display: none;
  }
  /deep/ .el-pagination .btn-prev .el-icon{
    display: none;
  }
  /deep/ .btn-prev{
    display: none;
  }
  /deep/ .btn-next{
    display: none;
  }
  .users:hover{
    cursor: pointer;
    color: #409EFF!important;
  }
  /deep/ .el-avatar>img{
    width: 100%;
  }
</style>