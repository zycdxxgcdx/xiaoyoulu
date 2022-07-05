<template>
  <div style="height: 100%;">
    <div class="top_content">
      班级消息
    </div>
    <div v-loading="loading"  infinite-scroll-delay="1000" element-loading-background="#f3f9f6"  class="bottom_content" v-infinite-scroll="load" :infinite-scroll-disabled="disabled" style="overflow-y:auto;padding-left: 1%;padding-top: 1%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)">
      <el-scrollbar style="height: 100%;overflow-x: hidden">
        <div style="margin:1% 0 0 1%;" v-for="item in messageClassNotify">
          <el-avatar :src="item.headPosition" @error="errorHandler" :size="40"  class="info" @click.native="toClassInfo(item.classId)" style="float: left;"></el-avatar>
          <div style="float: left;margin-left: 1%;width: 70%">
            <div style="width: 100%">
              <span style="color:rgba(0,0,0,.5);font-size: 14px;margin-right: 1%" v-if="item.exitType === 0">您已退出</span>
              <span style="font-size: 13px;margin-right: 2%;font-weight: bold"  class="classhover" @click="toClassInfo(item.classId)">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px;margin-right: 2%" v-if="item.isPass === 1">同意了您的申请</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px;margin-right: 2%" v-if="item.isPass === -1">拒绝了您的申请</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px;margin-right: 2%" v-if="item.exitType === 1">已经将您移除班级</span>
            </div>
          </div>
          <div style="clear: both;"></div>
          <div style="color:rgba(0,0,0,.5);font-size: 12px;margin:0 0 0 5.5%;">{{item.time}}</div>
          <el-divider style="margin-left: 6%;"></el-divider>
        </div>
        <div style="text-align: center;margin-bottom: 1%" v-if="loadInfo">加载中...</div>
        <div style="text-align: center;margin-bottom: 1%" v-if="noMore">没有更多了...</div>
        <div v-if="show" style="text-align: center;">
          暂无班级通知信息
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ClassNotifyInfo",
    data(){
      return{
        messageClassNotify:[],
        replyPageSize:6,
        replyPage:2,
        loading:false,
        disabled:false,
        loadInfo:false,
        noMore:false,
        show:false,
        circleUrl:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg"
      }
    },
    methods:{
      agree(id){
        console.log(id);
        const _this = this;
        axios.put('http://localhost:8181/message/classApplyAgree/'+id).then(function (resp) {
          console.log(resp);
          if(resp.data.msg === '同意成功'){
            axios.get('http://localhost:8181/message/findClassApplyOne/'+id).then(function (resp) {
              if(resp.data.msg === '查询成功'){
                for(let item in _this.messageClassNotify){
                  if(_this.messageClassNotify[item].id === id){
                    _this.messageClassNotify[item].isPass = resp.data.messageClassApply.isPass;
                    break;
                  }
                }
              }
            })
          }
        })
      },
      reject(id){
        const _this = this;
        axios.put('http://localhost:8181/message/classApplyReject/'+id).then(function (resp) {
          console.log(resp);
          if(resp.data.msg === '拒绝成功'){
            axios.get('http://localhost:8181/message/findClassApplyOne/'+id).then(function (resp) {
              if(resp.data.msg === '查询成功'){
                for(let item in _this.messageClassNotify){
                  if(_this.messageClassNotify[item].id === id){
                    _this.messageClassNotify[item].isPass = resp.data.messageClassApply.isPass;
                    break;
                  }
                }
              }
            })
          }
        })
      },
      toClassInfo(classId){
        let routeUrl = this.$router.resolve({
          path: '/classinfo',
          query:{
            classId:classId
          }
        });
        window.open(routeUrl.href, '_blank');
      },
      load(){
        const _this = this;
        _this.loadInfo =true;
        setTimeout(()=>{
          axios.get('http://localhost:8181/message/findClassNotify/'+this.$route.params.userId+'/'+this.replyPage+'/'+this.replyPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.messageClassNotify){
                resp.data.messageClassNotify[item].headPosition = 'http://localhost:8181/classesphoto/'+resp.data.messageClassNotify[item].headPosition
              }
              _this.messageClassNotify = _this.messageClassNotify.concat(resp.data.messageClassNotify);
              _this.replyPage++;
              if(resp.data.messageClassNotify.length < 6){
                _this.disabled = true;
                _this.loadInfo =false;
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
      console.log(this.$route.params.userId)
      axios.get('http://localhost:8181/message/findClassNotify/'+this.$route.params.userId+'/1/'+this.replyPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.messageClassNotify){
            resp.data.messageClassNotify[item].headPosition = 'http://localhost:8181/classesphoto/'+resp.data.messageClassNotify[item].headPosition
          }
          _this.messageClassNotify = resp.data.messageClassNotify;
          _this.loading = false;
          if(resp.data.messageClassNotify.length < 6){
            _this.disabled = true;
          }
          if(resp.data.messageClassNotify.length === 0){
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
  .classhover:hover{
    color: #409EFF;
    cursor: pointer;
  }
</style>