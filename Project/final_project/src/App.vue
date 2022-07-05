<template>
  <div id="app">
    <div class="navigation">
      <el-backtop target=".el-scrollbar__wrap" :visibility-height="100">
      </el-backtop>
      <!--      滚动条-->
      <el-scrollbar style="height: 100%;">
      <el-container>
        <!--        导航栏-->
        <el-header>
<!--          router.path 表示当前路径-->
              <el-menu router :default-active="$route.path" class="el-menu-demo" mode="horizontal" style="background-color: #ffffff">
                <el-menu-item index="/main" style="margin-left: 12%;">首页</el-menu-item>
<!--                <el-menu-item index="1" style="margin-left: 12%;" @click="toHomePage">首页</el-menu-item>-->
                <el-menu-item index="/school">学校</el-menu-item>
                <el-menu-item index="/class">班级</el-menu-item>
                <el-menu-item style="margin-left:5%">
                  <el-input v-model="input" placeholder="搜索帖子" @keyup.enter.native="edit" style="width: 400px;">
                    <el-button icon="el-icon-search" @click="edit" size="mini" slot="append"></el-button>
                  </el-input>
                </el-menu-item>
                <el-menu-item>
                  <el-button type="primary" size="small" style="height: auto;width: auto;font-size: 15px" @click="login" v-show="this.button"> <i class="el-icon-user" style="color: #f3f9f6"></i>登录</el-button>
                  <el-dropdown v-show="this.profile">
                    <el-avatar :src="circleUrl" @error="errorHandler" fit="fill"></el-avatar>
                    <el-dropdown-menu slot="dropdown" placement="bottom">
                      <el-dropdown-item disabled style="text-align: center">{{username}}</el-dropdown-item>
                      <router-link target="_blank" :to="{path:'/u/'+this.$store.state.UserId}" style="text-decoration:none;outline: none;color: #000;">
                      <el-dropdown-item divided><i class="el-icon-star-off"></i>我的主页</el-dropdown-item>
                    </router-link>
<!--                      <el-dropdown-item><i class="el-icon-user"></i>个人信息</el-dropdown-item>-->
                      <router-link target="_blank" :to="{path:'/message/'+this.$store.state.UserId}" style="text-decoration:none;outline: none;color: #000;">
                        <el-dropdown-item><i class="el-icon-message-solid"></i>我的消息<el-badge  :hidden="test" :value="messages" :max="99" class="item"></el-badge></el-dropdown-item>
                      </router-link>
                      <router-link target="_blank" :to="{path:'/followmessages/'+this.$store.state.UserId}" style="text-decoration:none;outline: none;color: #000;">
                        <el-dropdown-item>
                          <i class="el-icon-s-promotion"></i>关注动态<el-badge :hidden="test" :value="values" :max="99" class="item"></el-badge>
                        </el-dropdown-item>
                      </router-link>
                        <el-dropdown-item @click.native="logout">
                            <i class="iconfont icontuichu1"></i>退出
                        </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
              </el-menu-item>
              </el-menu>
        </el-header>
        <el-main style="padding-top: 0;">
          <!--          登录注册框-->
            <!--            close-on-click-modal 点击非表单内的内容退出表单
                            close-on-press-escape 按Esc退出
                            由于默认都为true，所以这里就不写了-->
        <router-view/>


          <!--          主页统计信息-->
        </el-main>
      </el-container>
      </el-scrollbar>
    </div>
  </div>
</template>
<script>
  export default {
    data() {
      return {
        test:true,
        show: true,
        //登录按钮
        button:true,
        //个人信息头像
        profile:false,
        //登录状态
        login_status:false,
        total: null, //首页总数据
        input: '', //输入框信息
        messages:1, //我的消息条数
        values:1, //动态条数
        activeName1: 'first', //内容中的默认选中项 “全部”选中
        // circleUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg", //头像地址
        username:'' //用户名
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
      //给每个路由嵌套登录界面
      login(){
        this.$router.push(this.$route.path+'/login')
        // this.$router.push('/main/login')
      },
      edit() {
        const _this = this;
        if(this.input !== '' && this.check_input(this.input)){
          let routeUrl = this.$router.resolve({
            path: "/searchpost",
            query: {
              input: _this.input
            }
          });
          window.open(routeUrl.href, '_blank');
        }else {
          _this.$message.warning("输入不能为空");
        }
      },
      conectWebSocket(){
        if ("WebSocket" in window) {
          this.websocket = new WebSocket(
              "ws://localhost:8181/websocket/" + this.$store.state.UserId+'/'+this.$route.params.Id
          );
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
          if (object.type == 0) {
            // 提示连接成功

          }
          if (object.type == 1) {
            //显示消息
            object.headPosition = 'http://localhost:8181/profilephoto/'+object.headPosition;
            if(object.toUser == _this.$route.params.Id ||(object.toUser == _this.$store.state.UserId && object.fromUser == _this.$route.params.Id) ){
              _this.messageList.push(object);
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
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        // return true
      },
      search() {
        alert(this.state)
      },
      //退出
      logout(){
          const _this = this;
          axios.get('http://localhost:8181/account/logout').then(function (resp) {
              console.log(resp);
              if(resp.data.state === true && resp.data.msg==='退出成功'){
                  localStorage.removeItem('Authorization');
                  localStorage.removeItem('AccountName');
                  localStorage.removeItem('UserId');
                  _this.$router.go(0);
              }else{
                  _this.$message.error('退出失败');
              }
          })
      }
    },
    updated(){

    },
    created() {
      const _this = this;
      // console.log(this.$store.state.Authorization);
      if(this.$store.state.Authorization){
          this.button = false;
          this.profile = true;
        axios.get('http://localhost:8181/account/checkAccountState').then(function (resp) {
          console.log(resp);
          if(resp.data.msg !== '账号登录状态检测成功'){
            localStorage.removeItem('Authorization');
            localStorage.removeItem('AccountName');
            localStorage.removeItem('UserId');
            _this.$message.error("登录信息过期，请重新登录");
            _this.$router.go(0);
          }
        })
        axios.get('http://localhost:8181/user/userInfo/'+this.$store.state.UserId).then(function (resp) {
          console.log(resp);
          _this.username = resp.data.userInfo.username;
          if(resp.data.userInfo.headPosition!==null){
            _this.circleUrl = 'http://localhost:8181/profilephoto/'+resp.data.userInfo.headPosition;
          }
        })
      }
    },
    watch: {
        //监听路由  当用户登录后通过监听路由 来实现登录按钮到头像的变化
        '$route'(){
          const _this = this;
            if(this.$store.state.Authorization){
                this.button = false;
                this.profile = true;
                // this.$router.go(0);
              axios.get('http://localhost:8181/user/userInfo/'+this.$store.state.UserId).then(function (resp) {
                // console.log(resp);
                _this.username = resp.data.userInfo.username;
                if(resp.data.userInfo.headPosition!==null){
                  _this.circleUrl = 'http://localhost:8181/profilephoto/'+resp.data.userInfo.headPosition;
                }
              })
            }
        }
    }
  };
</script>
<style scoped>

  /deep/ .el-container {
    height: 100%;
    background-color: #f3f9f6;
  }

  /* 使用/deep/深度选择器，使样式生效(取消掉横向滚动条)*/
  /deep/ .el-scrollbar__wrap{
    overflow-x: hidden;
  }
  .navigation{
    height: 100%;
    width: 100%;
    position: absolute;
    top:0;
    left: 0;
    background-color: #f3f9f6;
    /*  top,left实现容器的铺满*/
  }

  .navigation>.el-scrollbar__wrap {
    overflow: scroll;
  }
  .navigation>.el-container,.el-header{
    padding: 0;
    margin: 0;
    width: 100%;
  }
  /*去除每个元素的下划线*/
  .el-menu--horizontal > .el-menu-item {
    border-bottom: none;
    margin-right: 10px;
    font-size: 15px;
    color:rgba(0,0,0,.5) !important;
  }
  /*去除选中选中元素下划线*/
  .el-menu--horizontal > .el-menu-item.is-active {
    border-bottom: none;
    background-color: #ffffff !important;
    color: #409EFF !important;
  }
  /*去除鼠标悬停背景颜色的改变*/
  .el-menu--horizontal>.el-menu-item:hover{
    background-color: #ffffff !important;
    color: #409EFF !important;
  }
  /deep/ .el-main{
    height: 100%;
  }
  /deep/ .el-avatar>img{
    width: 100%;
  }
</style>
