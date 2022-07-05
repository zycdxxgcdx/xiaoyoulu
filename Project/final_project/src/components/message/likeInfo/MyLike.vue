<template>
  <div style="height: 100%;">
    <div class="top_content">
      收到的赞
    </div>
    <div v-loading="loading"  infinite-scroll-delay="1000" element-loading-background="#f3f9f6"  class="bottom_content" v-infinite-scroll="load" :infinite-scroll-disabled="disabled" style="overflow-y:auto;padding-left: 1%;padding-top: 1%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)">
      <el-scrollbar style="height: 100%;overflow-x: hidden">
        <div style="margin:1% 0 0 1%;" v-for="item in messageLike"  class="info"  @click="toDynamic(item.dynamicId)">
          <el-avatar @click.native="toHomePage(item.userId)" :src="item.headPosition" @error="errorHandler" :size="40" style="float: left;margin-top: 0.5%;"></el-avatar>
          <div style="float: left;margin-left: 1%;width: 70%;margin-top: 1%;">
            <div style="width: 100%">
              <span @click="toHomePage(item.userId)" style="font-size: 14px;margin-right: 2%;font-weight: bold" class="username">{{item.username}}</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px">点赞了我的动态</span>
            </div>
          </div>
          <div style="overflow:hidden;text-overflow: ellipsis;width: 10%;float: right;margin-right: 2%;color:rgba(0,0,0,.5);font-size: 13px">
            <dl v-html="item.content" v-lazy="item.content" class="replyclass">
              {{item.content}}
            </dl>
          </div>
          <div style="clear: both;"></div>
          <el-divider style="margin-left: 6%;"></el-divider>
        </div>
        <div style="text-align: center;margin-bottom: 1%" v-if="loadInfo">加载中...</div>
        <div style="text-align: center;margin-bottom: 1%" v-if="noMore">没有更多了...</div>
        <div v-if="show" style="text-align: center;">
          暂无点赞信息
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
  export default {
    name: "MyLike",
    data(){
      return{
        messageLike:[],
        replyPageSize:6,
        replyPage:2,
        loading:true,
        disabled:false,
        loadInfo:false,
        noMore:false,
        show:false,
        circleUrl:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
      }
    },
    methods:{
      toDynamic(dynamicId){
        let routeUrl = this.$router.resolve({
          path: `/dynamic/${dynamicId}`
        });
        window.open(routeUrl.href, '_blank');
      },
      toHomePage(userId){
        let routeUrl = this.$router.resolve({
          path: `/u/${userId}`
        });
        window.open(routeUrl.href, '_blank');
      },
      load(){
        const _this = this;
        _this.loadInfo =true;
        setTimeout(()=>{
          axios.get('http://localhost:8181/message/findLikeInfo/'+this.$route.params.userId+'/'+this.replyPage+'/'+this.replyPageSize).then(function (resp) {
            console.log("loading:");
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.messageLike){
                resp.data.messageLike[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.messageLike[item].headPosition
              }
              _this.messageLike = _this.messageLike.concat(resp.data.messageLike);
              _this.replyPage++;
              if(resp.data.messageLike.length < 6){
                _this.disabled = true;
                _this.loadInfo = false;
                _this.noMore = true;
              }
            }
          })
        },2000)
      },
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        return true
      },
    },
    created() {
      const _this = this;
      console.log(this.replyPage)
      axios.get('http://localhost:8181/message/findLikeInfo/'+this.$route.params.userId+'/1/'+this.replyPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.messageLike){
            resp.data.messageLike[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.messageLike[item].headPosition
          }
          _this.messageLike = resp.data.messageLike;
          if(resp.data.messageLike.length < 6){
            _this.disabled = true;
          }
          if(resp.data.messageLike.length === 0){
            _this.show = true;
          }
          _this.loading = false;
        }
      })
    }
  }
</script>

<style scoped>
  .top_content{
    /*border: 1px solid red;*/
    /*background-color: #fff;*/
    padding: 1% 1% 1% 2%;
    margin-bottom:2%;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }
  .bottom_content{
    /*border: 1px solid red;*/
    height: 560px;
    /*background-color: #fff;*/
    /*overflow: scroll;*/
  }
  .info:hover{
    cursor: pointer;
  }
  .username:hover{
    color: #409EFF;
  }
  .replyclass{
    margin: 0;
  }
  .replyclass >>> p{
    /*margin-top: 2%;*/
    /*margin-bottom: 1%;*/
    margin: 1% 0 1% 0;
    font-size: 14px;
  }
  .replyclass >>> img{
    vertical-align: sub
  }
</style>