<template>
  <div>
    <ul  v-loading="initLoading"
         element-loading-text="拼命加载中"
         element-loading-spinner="el-icon-loading"
         class="infinite-list" v-infinite-scroll="load"   infinite-scroll-delay="1000" :infinite-scroll-disabled="disabled" style="overflow:auto;padding-left: 2%;margin-top: 1%;">
      <li v-for="item in msgs" class="infinite-list-item" style="list-style: none;">
        <div class="main_info">
          <router-link target="_blank"  :to="'/publicpost/'+item.publicpostid" style="text-decoration:none;outline: none;color: #000;">
            <h1 style="font-size: 16px;margin-bottom: 3%;cursor: pointer">{{item.title}}</h1>
          </router-link>
          <!--                  <div class="main_info_msg">-->
          <router-link  target="_blank"  :to="'/u/'+userId" style="text-decoration:none;outline: none;color: #000;">
            <span style="padding-right: 0"><el-avatar :src="circleUrl" size="small" @error="errorHandler" style="float: left"></el-avatar></span>
            <span class="main_info_spanStyleTwo" style="font-size: 12px; font-weight: bold; margin:0 2% 0 1%;color:rgba(0,0,0,.5);">{{username}}</span>
          </router-link>
          <span class="main_info_spanStyleThreeToSix">
                      <label>时间:</label>
                      <span> {{item.postdate}}</span>
                    </span>
          <span class="main_info_spanStyleThreeToSix">
                      <label>学校:</label>
                      <span> 成都信息工程大学</span>
                    </span>
          <span class="main_info_spanStyleThreeToSix">
                        <label>浏览:</label>
                        <span> {{item.views}}</span>
                      </span>
          <!--                  </div>-->
        </div>
      </li>
    </ul>
    <p v-if="loading" style="text-align: center">加载中...</p>
    <p v-if="noMore" style="text-align: center;">没有更多了...</p>
  </div>
</template>

<script>
  export default {
    name: "HomeInfo",
    data(){
      return {
        initLoading:true,
        msgs:null,
        username:'',
        circleUrl:'',
        loading:false,
        noMore:false,
        num:2,
        disabled:false,
        userId:this.$route.params.userId,
      }
    },
    methods:{
      errorHandler(){
        this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
      },
      load(){
        console.log("ssssssssssssssssssssssssss");
        this.loading = true;
        const _this = this;
        console.log('http://localhost:8181/user/userPost/'+this.$route.params.userId+'/'+this.num+'/10');
        setTimeout(() => {
          axios.get('http://localhost:8181/user/userPost/'+this.$route.params.userId+'/'+this.num+'/10').then(function (resp) {
            console.log(resp)
            _this.msgs = _this.msgs.concat(resp.data.userPostInfo);
            _this.num++;
            if(resp.data.userPostInfo.length < 10){
              _this.noMore = true;
              _this.disabled = true;
              _this.loading = false;
            }
          })
        }, 2000)
      },
    },
    created() {
      const _this = this;
      //拿取用户的头像以及用户名
      axios.get('http://localhost:8181/user/userInfo/'+this.$route.params.userId).then(function (resp) {
        console.log(resp);
        _this.username = resp.data.userInfo.username;
        _this.circleUrl = 'http://localhost:8181/profilephoto/'+resp.data.userInfo.headPosition;
      })
      //拿取用户发帖信息
      axios.get('http://localhost:8181/user/userPost/'+this.$route.params.userId+'/1/10').then(function (resp) {
        console.log(resp)
        _this.msgs = resp.data.userPostInfo;
        _this.initLoading = false;
        if(resp.data.userPostInfo.length < 10){
          _this.disabled = true;
          _this.noMore = true;
        }
        console.log("length:"+resp.data.userPostInfo.length);
        console.log(_this.disabled);
      })
    }
  }
</script>

<style scoped>
  .main_info{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 1%;
    padding:2px 0 40px 15px;
    background-color:  #ffffff;
    border-radius: 3%;
  }
  .main_info >span{
    padding-right: 16px;
    font-size: 10px;
    font-weight: bold;
  }
  .main_info_spanStyleTwo:hover{
    color: #409EFF!important;
    cursor: pointer ;
  }
  .main_info>.main_info_spanStyleThreeToSix{
    color:rgba(0,0,0,.5) ;
  }
</style>