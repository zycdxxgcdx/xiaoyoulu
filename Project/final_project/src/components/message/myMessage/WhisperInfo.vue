<template>
  <div style="height: 100%;">
    <div style="height: 5%;font-size: 15px;text-align: center;box-shadow: 0 2px 0 0 rgba(0, 0, 0, 0.1)" v-if="type == 0">{{username}}</div>
    <div style="height: 5%;font-size: 13px;text-align: center;box-shadow: 0 2px 0 0 rgba(0, 0, 0, 0.1)" v-if="type == 1">{{classes.schoolName}}-{{classes.major}}-{{classes.grade}}-{{classes.className}}</div>
    <div style="height: 60%;width: 100%">
      <el-scrollbar v-infinite-scroll="load"  :infinite-scroll-disabled="disabled" infinite-scroll-delay="1000"   style="height: 100%;" ref="myScrollbar">
        <div v-for="item in messageList">
          <div style="text-align: center;color:rgba(0, 0, 0, 0.5);font-size: 13px" v-show="item.islast === 1">{{item.time}}</div>
          <div style="width: 70%;height: auto;float: right;" v-if="item.fromUser == userId">
            <span style="float: right;margin:2% 5% 0 0;font-size: 10px;color: rgba(0, 0, 0, 0.5)" v-if="type == 1">{{item.username}}</span>
            <div style="clear: both;"></div>
            <el-avatar :src="item.headPosition" @error="errorHandler" :size="35" style="float: right;margin:1% 5% 0 0;position: relative"></el-avatar>
              <div style="background-color: #80b9f2;border-radius: 15px;float: right; margin:1% 2% 2% 0;max-width: 80%;width: auto;padding: 2%">
                <dl v-html="item.content" v-lazy="item.content" style="margin:0" class="replyclass">
                  {{item.content}}
                </dl>
              </div>
            <div style="clear: both;"></div>
          </div>
          <div style="width: 70%;height: auto;" v-else>
            <span style="float: left;margin:2% 0 0 4%;font-size: 10px;color: rgba(0, 0, 0, 0.5)" v-if="type == 1">{{item.username}}</span>
            <div style="clear: both;"></div>
            <el-avatar :src="item.headPosition" @error="errorHandler" :size="35" style="float: left;margin:1% 0 0 4%;"></el-avatar>
            <div style="background-color: #ffffff;width: auto;margin:1% 0 2% 2%;float: left;max-width: 80%;border-radius: 15px;padding: 2%;">
              <dl v-html="item.content" v-lazy="item.content" style="margin:0" class="replyclass">
                {{item.content}}
              </dl>
            </div>
          </div>
          <div style="clear: both;"></div>
        </div>


<!--      <div style="text-align: center;color:rgba(0, 0, 0, 0.5);font-size: 13px">2020年10月23日 16:00</div>-->
<!--      <div style="width: 70%;height: auto">-->
<!--        <el-avatar :src="circleUrl" @error="errorHandler" :size="35" style="float: left;margin:2% 0 0 4%"></el-avatar>-->
<!--        <div style="box-shadow: 2px 2px 12px 0 rgba(0, 0, 0, 0.1);float: left;width: 80%;margin:2% 0 5% 2%;padding: 2%;border-radius: 15px;">内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容</div>-->
<!--        <div style="clear: both;"></div>-->
<!--      </div>-->
      </el-scrollbar>
    </div>
    <div style="height: auto;border-top: 1px solid #E4E7ED;border-left:1px solid #E4E7ED " id="editor"></div>
    <div style="height: 8%;float: right;margin: 0 2% 0 0;">
      <el-button type="primary" @click="sendMessage">发送</el-button>
    </div>
    <div style="clear: both;"></div>
  </div>
</template>

<script>
  import E from 'wangeditor';
  import emoji from '../../../static/emoji.json'
  export default {
    name: "WhisperInfo",
    data(){
      return {
        Id:0,  //用户ID 为了用户切换到例如回复我的时候不抛出异常
        disabled:true,
        classes:{schoolName:'11',major:'11',grade:'11',className:'11'},
        type:0,
        msgPageSize:10,
        msgPage:2,
        username: "111", // 昵称
        websocket: null, // WebSocket对象
        aisle: "", // 对方频道号
        messageList: [], // 消息列表
        messageValue: "" ,// 消息内容
        userId:this.$store.state.UserId,
        circleUrl:"https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      }
    },
    methods:{
      load(){
        const _this = this;
        if(this.$route.query.type == 0){
          setTimeout(()=>{
            axios.get('http://localhost:8181/socket/findSocketMsg/'+this.$store.state.UserId+'/'+this.$route.params.Id+'/'+this.msgPage+'/'+this.msgPageSize).then(function (resp) {
              if(resp.data.msg === '查询消息记录成功'){
                console.log("load：")
                console.log(resp);
                for(let item in resp.data.socketMsg){
                  resp.data.socketMsg[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.socketMsg[item].headPosition;
                }
                for(let item in resp.data.socketMsg.reverse()){
                  _this.messageList.unshift(resp.data.socketMsg[item])
                  console.log(resp.data.socketMsg[item].time);
                }
                _this.msgPage++;
                if(resp.data.socketMsg.length < 10){
                  _this.disabled = true;
                }
              }
            })
          },2000)
        }else if(this.$route.query.type == 1){
          setTimeout(()=>{
            axios.get('http://localhost:8181/socket/findSocketGroupMsg/'+this.Id+'/'+this.msgPage+'/'+this.msgPageSize).then(function (resp) {
              if(resp.data.msg === '查询消息记录成功'){
                for(let item in resp.data.socketMsg){
                  resp.data.socketMsg[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.socketMsg[item].headPosition;
                }
                for(let item in resp.data.socketMsg.reverse()){
                  _this.messageList.unshift(resp.data.socketMsg[item])
                }
                _this.msgPage++;
                if(resp.data.socketMsg.length < 10){
                  _this.disabled = true;
                }
              }
            })
          },2000)
        }else{
          _this.disabled = true;
          _this.websocket.close();
        }
      },
      conectWebSocket(){
        if ("WebSocket" in window) {
          this.websocket = new WebSocket(
              "ws://localhost:8181/websocket/" + this.$store.state.UserId+'/'+this.$route.params.Id
          );
          console.log("websocket已连接")
        } else {
          alert("不支持建立socket连接");
        }
        //连接发生错误的回调方法
        this.websocket.onerror = function() {

        };
        //连接成功建立的回调方法
        this.websocket.onopen = function(event) {

        };
        //接收到消息的回调方法
        const _this = this;
        this.websocket.onmessage = function(event) {
          //将json字符串转换为json对象
          var object = eval("(" + event.data + ")");
          console.log(object)
          if (object.type == 0) {
            // 提示连接成功
            // _this.showInfo(object.people, object.aisle)
          }
          if (object.type == 1) {
            //显示消息
            object.headPosition = 'http://localhost:8181/profilephoto/'+object.headPosition;
            if(object.toUser == _this.$route.params.Id ||(object.toUser == _this.$store.state.UserId && object.fromUser == _this.$route.params.Id) ){
              console.log("0")
              _this.messageList.push(object);
            }else if(object.infotype == 0){
              console.log("1")
              _this.showPrivateInfo(object.username)
            }else if(object.infotype == 1){
              console.log("2")
              _this.showGroupInfo(object.schoolName,object.major,object.grade,object.className)
            }
          }
        };
        //连接关闭的回调方法
        this.websocket.onclose = function() {
        };
        //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
        window.onbeforeunload = function() {
          this.websocket.close();
        };
        //窗口刷新
        window.onbeforeunload = function(event){
          this.websocket.close();
        };
      },
      // 发送消息
      sendMessage(){
        var socketMsg = { content: this.editor.txt.html(), toUser: this.$route.params.Id ,type:this.$route.query.type};
        this.websocket.send(JSON.stringify(socketMsg));
        this.editor.txt.clear();
      },
      showPrivateInfo: function(username) {
        const h = this.$createElement;
        this.$notify.info({
          customClass:'notify-success',
          // title: "当前在线人数：" + people,
          title: '消息',
          message: "用户"+username+"给您发来一条新消息",
        });
      },
      showGroupInfo: function(schoolName,major,grade,className) {
        const h = this.$createElement;
        this.$notify.info({
          customClass:'notify-success',
          // title: "当前在线人数：" + people,
          title: '消息',
          message: "群"+schoolName+major+grade+className+"给您发来一条新消息",
          duration: 3000
        });
      },
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        return true
      },
      getSocketInfo(){
        this.type = this.$route.query.type;
        const _this = this;
        this.websocket.close();
        this.conectWebSocket();
        this.msgPage = 2;
        this.disabled = false;
        if(this.$route.query.type == 0){
          axios.get('http://localhost:8181/socket/findSocketMsg/'+this.$store.state.UserId+'/'+this.$route.params.Id+'/1/'+this.msgPageSize).then(function (resp) {

            if(resp.data.msg === '查询消息记录成功'){
              for(let item in resp.data.socketMsg){
                resp.data.socketMsg[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.socketMsg[item].headPosition;
              }
              _this.messageList = resp.data.socketMsg;
              _this.username = resp.data.toUsername;
            }
          })
        }else if(this.$route.query.type == 1){
          axios.get('http://localhost:8181/socket/findSocketGroupMsg/'+this.$route.params.Id+'/1/'+this.msgPageSize).then(function (resp) {

            if(resp.data.msg === '查询消息记录成功'){
              for(let item in resp.data.socketMsg){
                resp.data.socketMsg[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.socketMsg[item].headPosition;
              }
              _this.messageList = resp.data.socketMsg;
              _this.classes = resp.data.toUsername;
            }
          })
        }else{
          this.websocket.close();
        }
      }
    },
    watch:{
      messageList:function () {
        this.$forceUpdate();
        const _this = this;
        this.$nextTick(function () {
          this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
        })
      },
      //监听路由的变化 并执行对应的方法
      '$route':'getSocketInfo'
    },
    updated:function(){

    },
    mounted() {
      this.editor = new E('#editor');
      this.editor.config.height = 110;
      this.editor.config.focus = true;
      this.editor.config.uploadImgServer = 'http://localhost:8181/post/selectJpgUrl';  //图片接口地址
      this.editor.config.uploadImgHeaders = {
        token: localStorage.getItem('Authorization') // 设置请求头
      }
      this.editor.config.uploadFileName = 'images';
      // 限制一次最多上传 5 张图片
      this.editor.config.uploadImgMaxLength = 5;
      // 将 timeout 时间改为 3s
      this.editor.config.uploadImgTimeout = 50000;
      this.editor.config.showLinkImgAlt = false;
      this.editor.config.showLinkImgHref = false;
      this.editor.config.showFullScreen = false;  //配置全屏
      this.editor.config.showMenuTooltips = true; //隐藏菜单栏提示
      this.editor.config.showLinkImg = false;  //隐藏插入网络图片
      this.editor.config.zIndex = 1; //降低editor权值,不遮挡其他元素
      this.editor.config.emotions = emoji;
      this.editor.config.menus = [
        'emoticon',
      ]
      this.editor.create();
      this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
    },
    created() {
      this.conectWebSocket();
      const _this = this;
      this.Id = this.$route.params.Id;
      this.type = this.$route.query.type;
      if(this.$route.query.type == 0){
        axios.get('http://localhost:8181/socket/findSocketMsg/'+this.$store.state.UserId+'/'+this.$route.params.Id+'/1/'+this.msgPageSize).then(function (resp) {
          if(resp.data.msg === '查询消息记录成功'){
            for(let item in resp.data.socketMsg){
              resp.data.socketMsg[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.socketMsg[item].headPosition;
            }
            _this.messageList = resp.data.socketMsg;
            _this.username = resp.data.toUsername;
            if(resp.data.socketMsg.length === 10){
              _this.disabled = false;
            }
          }
        })
      }else if(this.$route.query.type == 1){
        axios.get('http://localhost:8181/socket/findSocketGroupMsg/'+this.$route.params.Id+'/1/'+this.msgPageSize).then(function (resp) {
          if(resp.data.msg === '查询消息记录成功'){
            for(let item in resp.data.socketMsg){
              resp.data.socketMsg[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.socketMsg[item].headPosition;
            }
            _this.messageList = resp.data.socketMsg;
            _this.classes = resp.data.toUsername;
            if(resp.data.socketMsg.length === 10){
              _this.disabled = false;
            }
          }
        })
      }else{
        this.websocket.close();
      }

    }
  }
</script>

<style scoped>
  /deep/ .el-scrollbar__wrap{
    overflow-x: hidden;
  }
  /deep/ .w-e-toolbar{
    border: 0!important;
    background-color: #f3f9f6!important;
  }
  /deep/ .w-e-text-container{
    border: 0!important;
    background-color: #f3f9f6;
  }
  /deep/ .w-e-panel-container{
    margin: 0 0 0 120px!important;
    overflow: hidden;
  }
  /deep/ .w-e-panel-tab-content{
    height: 180px!important;
  }
  .replyclass >>> p{
    margin:0;
    font-size: 15px;
    float: right;
    word-break: break-all;
  }
  .replyclass >>> img{
    vertical-align: sub
  }
  /deep/ .el-notification{
    width: 240px!important;
    padding:14px 1px 14px 1px!important;
  }
  /deep/ .el-notification.right{
    width: 240px!important;
    padding:14px 1px 14px 1px!important;
  }
</style>
<style>
  .notify-success{
    width: 240px!important;
    padding:14px 14px 14px 14px!important;
    right: 0px!important;
  }
</style>