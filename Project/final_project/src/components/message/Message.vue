<template>
  <div style="padding-top: 1%">
    <div class="left_main_content">
      <el-col :span="15" style="width: 100%;">
<!--                        router
                :default-active="$route.path"-->
        <el-menu
                router
                :default-active="$route.path"
                class="el-menu-vertical-demo"
                style="padding-bottom: 10%;height: 100%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);background-color:#f3f9f6;">
          <h3 style="text-align: center"> <i class="el-icon-message-solid" style="color: black;"></i>消息中心</h3>
          <el-submenu index="1">
              <template slot="title" class="test">
                <i class="el-icon-message-solid" style="margin-top: 1%;"></i>
                <span>回复我的</span>
              </template>
              <el-menu-item :index="'/message/'+this.$store.state.UserId+'/reply/publicpost'">帖子信息</el-menu-item>
              <el-menu-item :index="'/message/'+this.$store.state.UserId+'/reply/dynamic'">动态信息</el-menu-item>
              <el-menu-item :index="'/message/'+this.$store.state.UserId+'/reply/replyinfo'">回复信息</el-menu-item>
          </el-submenu>
          <el-menu-item :index="'/message/'+this.$store.state.UserId+'/likeinfo'" style="vertical-align:top">
            <i class="el-icon-message-solid"  style="margin-top: 1%;"></i>
            <span slot="title">收到的赞</span>
          </el-menu-item>
          <el-submenu index="3">
            <template slot="title" class="test">
              <i class="el-icon-message-solid" style="margin-top: 1%;"></i>
              <span>班级信息</span>
            </template>
            <el-menu-item :index="'/message/'+this.$store.state.UserId+'/classinfo/apply'">班级申请</el-menu-item>
            <el-menu-item :index="'/message/'+this.$store.state.UserId+'/classinfo/notify'">班级通知</el-menu-item>
          </el-submenu>
          <el-menu-item :index="'/message/'+this.$store.state.UserId+'/whisper'">
              <i class="el-icon-message-solid"  style="margin-top: 1%;"></i>
            <span slot="title">我的消息</span>
          </el-menu-item>
        </el-menu>
      </el-col>
    </div>
    <!--          主页统计信息-->
    <div class="right_main_content">
      <router-view/>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Message",
    data(){
      return {
      }
    },
    methods:{
    }
  }
</script>

<style scoped>
  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 10%;
    position: relative;
    margin-left: 12%;
    margin-right: 0;
    float: left;
    height: 100%;
    /*border: 1px solid red;*/
  }
  .right_main_content{
    position: relative;
    width: 60%;
    float: left;
    margin-left: 2%;
    /*height: 100%;*/
  }
   .el-menu-item:hover{
    background-color: #f3f9f6;
    color: #409EFF!important;
  }
  .el-menu-item.is-active{
    background-color: #f3f9f6;
    color: #409EFF;
  }
  .el-submenu /deep/ .el-submenu__title:hover {
    background-color: #f3f9f6;
    color: #409EFF;
  }
  .el-submenu .el-menu-item{
    min-width: 145px!important;
    background-color: #f3f9f6;
  }
  .el-submenu .el-menu-item:hover{
    background-color: #f3f9f6;
    color: #409EFF;
  }

</style>