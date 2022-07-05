<template>
  <div style="padding-top: 1%">
    <div class="left_main_content">
      <div style="float: right;">
        <el-input
                style="width: 100%;"
                size="small"
                clearable
                v-model="inputValue"
                :trigger-on-focus="false"
                placeholder="搜索帖子"
                @keyup.enter.native="handleSubmit">
          <i class="el-icon-search el-input__icon"
             id="searchFromAllSchool"
             slot="suffix"
             @click="handleSubmit">
          </i>
        </el-input>
      </div>
      <div style="float: left;padding-top: 0.7%;margin-right: 5%;font-size: 15px;font-weight: bold;color:rgba(0,0,0,.5);">一共有{{total}}条数据</div>
      <div style="clear: both;"></div>
      <el-tabs v-model="activeName">
        <el-tab-pane label="全部" name="first">
          <div v-loading="loading">
            <div class="left_main_content_child" v-if="msgs.length !== 0" v-for="item in msgs">
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
            <div v-if="msgs.length === 0">暂无</div>
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
        </el-tab-pane>
        <!--              <el-tab-pane label="热门" name="second">热门测试</el-tab-pane>-->
      </el-tabs>
    </div>
  </div>
</template>

<script>
  export default {
    name: "SearchPost",
    data() {
      return {
        loading:true,
        inputValue:'',
        flag:false,
        total: null,
        activeIndex: '1',
        msgs:[],
        activeName: 'first',
        activeName1: 'first',
        // circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      };
    },
    methods: {
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
      handleSubmit(){
        //再全部学校中查询时执行的enter操作
        console.log("enter");
        const _this = this;
        if(this.inputValue !== '' && this.check_input(this.inputValue)){
          this.$router.push({
            path: '/searchpost',
            query: {
              input: _this.inputValue
            }
          })
        }else {
          _this.$message.warning("输入不能为空");
        }
      },
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
        if(localStorage.getItem('Authorization')){
          this.$router.push('/post');
        }else{
          this.$router.push('/main/login');
        }
      },
      //分页
      page(currentPage) {
        const _this = this;
        console.log('http://localhost:8181/publicpost/findAll/'+currentPage+'/10')
        axios.get('http://localhost:8181/publicpost/findSearchPost/'+this.$route.query.input+'/'+currentPage+'/10').then(function (resp) {
          if(resp.data.msg === '查询成功'){
            _this.msgs = resp.data.publicPost;
          }
        })
      },
    },
    watch: {
      '$route' (to, from) { //监听路由是否变化
        const _this = this;
        if(to.query.input != from.query.input){
          axios.get('http://localhost:8181/publicpost/findSearchPost/'+to.query.input+'/1/10').then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              _this.msgs = resp.data.publicPost;
              _this.total = resp.data.publicPostNums;
              _this.loading = false;
            }
          })
        }
      }
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/publicpost/findSearchPost/'+this.$route.query.input+'/1/10').then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          _this.msgs = resp.data.publicPost;
          _this.total = resp.data.publicPostNums;
          _this.loading = false;
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
  #searchFromAllSchool:hover{
    cursor: pointer;
  }
</style>
