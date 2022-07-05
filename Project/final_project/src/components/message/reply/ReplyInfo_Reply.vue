<template>
  <div style="height: 100%;">
    <div class="top_content">
      回复我的
    </div>
    <div v-loading="loading"  infinite-scroll-delay="1000" element-loading-background="#f3f9f6"  class="bottom_content" v-infinite-scroll="load" :infinite-scroll-disabled="disabled" style="overflow-y:auto;padding-left: 1%;padding-top: 1%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)">
      <el-scrollbar style="height: 100%;overflow-x: hidden">
        <div style="margin:1% 0 0 1%;" v-for="item in messageReply"  class="info"  @click="toPostOrDynamic(item.dynamicId,item.publicpostid)">
          <el-avatar @click.native="toHomePage(item.userId)" :src="item.headPosition" @error="errorHandler" :size="40" style="float: left;margin-top: 0.5%;"></el-avatar>
          <div style="float: left;margin-left: 1%;width: 70%">
            <div style="width: 100%">
              <span @click="toHomePage(item.userId)" style="font-size: 14px;margin-right: 2%;font-weight: bold" class="username">{{item.username}}</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px" v-if="item.dynamicId === '0'">回复了我的帖子评论</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px" v-if="item.publicpostid === '0'">回复了我的动态评论</span>
            </div>
            <div style="margin-top: 1%;font-size: 15px">
              <dl v-html="item.content" v-lazy="item.content" class="replyclass">
                {{item.content}}
              </dl>
            </div>
          </div>
          <div style="overflow:hidden;text-overflow: ellipsis;width: 10%;float: right;margin-right: 2%;color:rgba(0,0,0,.5);font-size: 13px">
            <dl v-html="item.toContent" v-lazy="item.toContent" class="replyToContentClass">
              {{item.toContent}}
            </dl>
          </div>
          <div style="clear: both;"></div>
          <div style="color:rgba(0,0,0,.5);font-size: 12px;margin:1% 0 0 5.5%;">{{item.time}}</div>
          <el-divider style="margin-left: 6%;"></el-divider>
        </div>
        <div style="text-align: center;margin-bottom: 1%" v-if="loadInfo">加载中...</div>
        <div style="text-align: center;margin-bottom: 1%" v-if="noMore">没有更多了...</div>
        <div v-if="show" style="text-align: center;">
          暂无回复信息
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ReplyInfo_Reply",
    data(){
      return{
        messageReply:[],
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
      toPostOrDynamic(dynamicId,publicpostid){
        const _this = this;
        if(dynamicId === '0'){
          let routeUrl = this.$router.resolve({
            path: `/publicpost/${publicpostid}`
          });
          window.open(routeUrl.href, '_blank');
        }else if(publicpostid === '0'){
          let routeUrl = this.$router.resolve({
            path: `/dynamic/${dynamicId}`
          });
          window.open(routeUrl.href, '_blank');
        }else{
          _this.$message.error("数据有误");
        }
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
          axios.get('http://localhost:8181/message/findReplyInfo/'+this.$route.params.userId+'/'+this.replyPage+'/'+this.replyPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.messageReply){
                resp.data.messageReply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.messageReply[item].headPosition
              }
              _this.messageReply = _this.messageReply.concat(resp.data.messageReply);
              _this.replyPage++;
              if(resp.data.messageReply.length < 6){
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
      axios.get('http://localhost:8181/message/findReplyInfo/'+this.$route.params.userId+'/1/'+this.replyPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.messageReply){
            resp.data.messageReply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.messageReply[item].headPosition
          }
          _this.messageReply = resp.data.messageReply;
          console.log(_this.messageReply)
          _this.loading = false;
          if(resp.data.messageReply.length < 6){
            _this.disabled = true;
          }
          if(resp.data.messageReply.length === 0){
            _this.show = true;
          }
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
    font-size: 15px;
  }
  .replyclass >>> img{
    vertical-align: sub
  }
  .replyToContentClass{
    margin: 0;
  }
  .replyToContentClass >>> p{
    margin: 1% 0 1% 0;
    font-size: 13px;
  }
  .replyToContentClass >>> img{
    vertical-align: sub
  }
</style>