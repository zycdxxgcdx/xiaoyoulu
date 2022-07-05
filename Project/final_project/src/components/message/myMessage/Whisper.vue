<template>
  <div style="height: 100%;">
    <div class="top_content">
      我的消息
    </div>
    <div class="bottom_content" style="box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);">
      <div class="left_content">
        <div style="height: 5%;padding:0 0 0 10%;font-size: 13px;box-shadow: 2px 2px 0 0 rgba(0, 0, 0, 0.1);">近期消息</div>
        <div style="height: 95%;box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);">
          <el-scrollbar style="height: 100%;">
          <div v-infinite-scroll="load"  :infinite-scroll-disabled="disabled" infinite-scroll-delay="1000"  v-for="(item,index) in SocketList" @click="toInfo(item.touserId,item.type)" class="test" style="padding: 5% 0 0 2%;height: 60px;box-shadow: 2px 2px 0 0 rgba(0, 0, 0, 0.1);">
            <i class="el-icon-close" style="margin:7% 0 0 0;float: left;display: none" @click="delUser(item.touserId,index)"></i>
            <el-avatar :src="item.headPosition" @error="errorHandler" :size="45" style="float: left;margin-left: 4%;"></el-avatar>
            <p style="margin:3% 0 0 4%;float: left;width: 45%" v-if="item.type === 0">{{item.username}}</p>
            <p style="margin:0 0 0 4%;float: left;width: 58%;font-size:10px;" v-if="item.type === 1">{{item.schoolName}}-{{item.major}}-{{item.grade}}-{{item.className}}</p>
          </div>
            <div v-if="show" style="text-align: center;">
              暂无近期信息
            </div>
          </el-scrollbar>
        </div>
      </div>
      <div class="right_content">
        <router-view/>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Whisper",
    data(){
      return{
        circleUrl:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        //一次性拿列表中7人
        userPageSize:7,
        userPage:2,
        SocketList:[],
        disabled:false,
        show:false,
      }
    },
    methods:{
      load(){
        const _this = this;
        setTimeout(()=>{
          axios.get('http://localhost:8181/socket/findSocketList/'+this.$store.state.UserId+'/'+this.userPage+'/'+this.userPageSize).then(function (resp) {
            if(resp.data.msg === '查询成功'){
              for(let item in resp.data.SocketList){
                if(resp.data.SocketList[item].type === 0){
                  resp.data.SocketList[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.SocketList[item].headPosition
                }else{
                  resp.data.SocketList[item].headPosition = 'http://localhost:8181/classesphoto/'+resp.data.SocketList[item].headPosition
                }
              }
              _this.SocketList = _this.SocketList.concat(resp.data.SocketList);
              _this.userPage++;
              if(resp.data.SocketList.length < 7){
                _this.disabled = true;
              }
            }
          })
        },2000)
      },
      delUser(id,index){
        const _this = this;
        axios.put('http://localhost:8181/socket/delListUser/'+this.$store.state.UserId+'/'+id).then(function (resp) {
            if(resp.data.msg === '删除用户列表成功'){
              var index = _this.SocketList.findIndex(item =>{
                if(item.touserId==id){
                  return true
                }
              })
              _this.SocketList.splice(index,1)
            }
        })
      },
      toInfo(id,type){
        this.$router.push(
            {path:'/message/'+this.$store.state.UserId+'/whisper/'+id,query:{type:type}});
      },
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        return true
      },
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/socket/findSocketList/'+this.$store.state.UserId+'/1/'+this.userPageSize).then(function (resp) {
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.SocketList){
            if(resp.data.SocketList[item].type === 0){
              resp.data.SocketList[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.SocketList[item].headPosition
            }else{
              resp.data.SocketList[item].headPosition = 'http://localhost:8181/classesphoto/'+resp.data.SocketList[item].headPosition
            }
          }
          _this.SocketList = resp.data.SocketList;
          if(resp.data.SocketList.length < 7){
            _this.disabled = true;
          }
          if(resp.data.SocketList.length === 0){
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
    padding: 0;
    /*background-color: #fff;*/
    /*overflow: scroll;*/
  }
  .left_content{
    /*border: 1px solid red;*/
    width: 28%;
    height: 100%;
    margin: 0;
    padding: 0;
    float: left;
  }
  .right_content{
    /*border: 1px solid red;*/
    width: 72%;
    margin: 0;
    padding: 0;
    height: 100%;
    float: right;
  }
  /deep/ .el-scrollbar__wrap{
    margin-right: 0;
  }
  .test:hover{
    cursor: pointer;
    color: #409EFF;
    background: #EBEEF5;
  }
  .test:hover .el-icon-close{
    display: block!important;
  }
</style>