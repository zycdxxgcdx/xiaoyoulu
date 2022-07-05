<template>
  <div style="padding-top: 1%">
    <div class="left_main_content">
            <el-tabs v-model="activeName">
              <el-tab-pane label="全部" name="first">
                <div v-if="flag">
                <div class="left_main_content_child" v-for="item in msgs">
                  <router-link target="_blank"  :to="'/publicpost/'+item.publicpostid" style="text-decoration:none;outline: none;color: #000;">
                    <h1 style="font-size: 16px;margin-bottom: 3%;cursor: pointer">{{item.title}}</h1>
                  </router-link>
                  <!--                  <div class="left_main_content_child_msg">-->
                  <span style="padding-right: 0"><el-avatar :src="'http://localhost:8181/profilephoto/'+item.headPosition" @click.native="toHomePage(item.userId)" size="small" @error="errorHandler" style="float: left;cursor:pointer"></el-avatar></span>
                  <span class="left_main_content_child_spanStyleTwo" @click="toHomePage(item.userId)">{{item.username}}</span>
                  <span class="left_main_content_child_spanStyleThreeToSix">
                      <label>时间:</label>
                      <span> {{item.postdate}}</span>
                    </span>
                  <span class="left_main_content_child_spanStyleThreeToSix">
                      <label>学校:</label>
                      <span> {{item.school}}</span>
                    </span>
                  <span class="left_main_content_child_spanStyleThreeToSix">
                        <label>浏览:</label>
                        <span> {{item.views}}</span>
                      </span>
                  <!--                  </div>-->
                </div>
                <div>
                  <el-pagination
                          hide-on-single-page
                          background
                          layout="prev, pager, next"
                          :total="total"
                          style="color: #ffffff"
                          :page-size="10"
                          @current-change="page">
                  </el-pagination>
                </div>
                </div>
                <div v-else class="loading">
                  load...
                </div>
              </el-tab-pane>
<!--              <el-tab-pane label="热门" name="second">热门测试</el-tab-pane>-->
            </el-tabs>
    </div>
          <!--          主页统计信息-->
    <router-view/>
    <div class="right_main_content">
            <el-button type="primary" icon="el-icon-edit" size="medium" style="width: 100%;" @click="post">发布帖子</el-button>
            <div class="right_main_content_child_one">
              <i class="el-icon-s-data" style="font-size: 14px"><b> 统计信息</b></i>
              <p>用户数:{{statisticalInfo.userNum}}人</p>
              <p>帖子数:{{statisticalInfo.postNumber}}个</p>
              <p>回复数:{{statisticalInfo.replyNumber}}条</p>
              <p>班级数:{{statisticalInfo.classNumber}}个</p>
              <p>学校数:{{statisticalInfo.schoolNumber}}个</p>
            </div>
          </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        flag:false,
        total: null,
        activeIndex: '1',
        msgs:null,
        activeName: 'first',
        activeName1: 'first',
        // circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        statisticalInfo:[],
      };
    },
    methods: {
      toHomePage(userId){
        let routeUrl = this.$router.resolve({
          path: `/u/${userId}`
        });
        window.open(routeUrl.href, '_blank');
      },
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        // return true
      },
      post(){
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
              _this.$router.push('/post');
            }
          })
        }else{
          this.$router.push('/main/login');
        }
      },
      //分页
      page(currentPage) {
        const _this = this;
        console.log('http://localhost:8181/publicpost/findAll/'+currentPage+'/10')
        axios.get('http://localhost:8181/publicpost/findAll/'+currentPage+'/10').then(function (resp) {
          _this.msgs = resp.data;
        })
      },
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/publicpost/findAll/1/10').then(function (resp) {
        console.log(resp.data);
        _this.msgs = resp.data;
        _this.flag = true;
      })
      axios.get('http://localhost:8181/publicpost/findAllPages').then(function (resp) {
        console.log(resp.data);
        _this.total = resp.data;
      })
      axios.get('http://localhost:8181/publicpost/getStatisticalInfo').then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          _this.statisticalInfo = resp.data.allNum;
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
    margin-left: 12%;
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
  .right_main_content{
    margin-right: 11%;
    position: relative;
    width: 20%;
    float: right;
  }
  .right_main_content_child_one{
    /*border: 50px red solid;*/
    margin-top: 6%;
    padding: 3% 0 1% 3%;
    background-color: #ffffff;
  }
  .right_main_content_child_one>p{
    padding-left: 3%;
  }
</style>
