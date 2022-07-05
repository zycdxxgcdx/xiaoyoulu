<template>
  <div style="height: 100%;">
    <div class="top_content">
      班级消息
    </div>
    <div v-loading="loading"  infinite-scroll-delay="1000" element-loading-background="#f3f9f6"  class="bottom_content" v-infinite-scroll="load" :infinite-scroll-disabled="disabled" style="overflow-y:auto;padding-left: 1%;padding-top: 1%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)">
      <el-scrollbar style="height: 100%;overflow-x: hidden">
        <div style="margin:1% 0 0 1%;" v-for="item in messageClassReply"  class="info">
          <el-avatar :src="item.headPosition" @error="errorHandler" :size="40" @click.native="toUserInfo(item.userId)" class="username" style="float: left;"></el-avatar>
          <div style="float: left;margin-left: 1%;width: 70%">
            <div style="width: 100%">
              <span style="font-size: 14px;margin-right: 2%;font-weight: bold" @click="toUserInfo(item.userId)" class="username">{{item.username}}</span>
              <span style="color:rgba(0,0,0,.5);font-size: 14px;margin-right: 2%">申请加入</span>
              <span style="font-size: 13px;font-weight: bold" @click="toClassInfo(item.classId)" class="classhover">{{item.schoolName}} - {{item.major}} - {{item.grade}} - {{item.className}}</span>
            </div>
          </div>
          <div style="overflow:hidden;text-overflow: ellipsis;width: 20%;float: right;margin-right: 2%;color:rgba(0,0,0,.5);font-size: 13px">
            <el-button type="success" round v-if="item.isPass === 0" @click.stop="agree(item.id)">同意</el-button>
            <el-button type="danger" round v-if="item.isPass === 0" @click.stop="reject(item.id)">拒绝</el-button>
            <el-button type="info" disabled v-if="item.isPass === 1">已同意</el-button>
            <el-button type="info" disabled v-if="item.isPass === -1">已拒绝</el-button>
          </div>
          <div style="clear: both;"></div>
          <div style="color:rgba(0,0,0,.5);font-size: 12px;margin:0 0 0 5.5%;">{{item.passTime}}</div>
          <el-divider style="margin-left: 6%;"></el-divider>
        </div>
        <div style="text-align: center;margin-bottom: 1%" v-if="loadInfo">加载中...</div>
        <div style="text-align: center;margin-bottom: 1%" v-if="noMore">没有更多了...</div>
        <div v-if="show" style="text-align: center;">
          暂无班级申请信息
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
  export default {
    name: "ClassInfo",
    data(){
      return{
        messageClassReply:[],
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
      agree(id){
        console.log(id);
        const _this = this;
        axios.put('http://localhost:8181/message/classApplyAgree/'+id).then(function (resp) {
          console.log(resp);
          if(resp.data.msg === '同意成功'){
            axios.get('http://localhost:8181/message/findClassApplyOne/'+id).then(function (resp) {
              if(resp.data.msg === '查询成功'){
                  for(let item in _this.messageClassReply){
                    if(_this.messageClassReply[item].id === id){
                      _this.messageClassReply[item].isPass = resp.data.messageClassApply.isPass;
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
                for(let item in _this.messageClassReply){
                  if(_this.messageClassReply[item].id === id){
                    _this.messageClassReply[item].isPass = resp.data.messageClassApply.isPass;
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
      toUserInfo(userId){
        let routeUrl = this.$router.resolve({
          path: `/u/${userId}`
        });
        window.open(routeUrl.href, '_blank');
      },
      load(){
        const _this = this;
        _this.loadInfo =true;
        setTimeout(()=>{
          axios.get('http://localhost:8181/message/findClassApply/'+this.$route.params.userId+'/'+this.replyPage+'/'+this.replyPageSize).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.messageClassApply){
                resp.data.messageClassApply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.messageClassApply[item].headPosition
              }
              _this.messageClassReply = _this.messageClassReply.concat(resp.data.messageClassApply);
              _this.replyPage++;
              if(resp.data.messageClassApply.length < 6){
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
      axios.get('http://localhost:8181/message/findClassApply/'+this.$route.params.userId+'/1/'+this.replyPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.messageClassApply){
            resp.data.messageClassApply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.messageClassApply[item].headPosition
          }
          _this.messageClassReply = resp.data.messageClassApply;
          _this.loading = false;
          if(resp.data.messageClassApply.length < 6){
            _this.disabled = true;
          }
          if(resp.data.messageClassApply.length === 0){
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
  .username:hover{
    color: #409EFF;
    cursor: pointer;
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