<template>
  <div style="height: 100%;padding-top: 1%;">
    <div class="left_main_content">
      <div class="titleAndContent">
        <div class="title">
          <h1>{{this.mainContent.title}}</h1>
          <span style="padding-right: 0;">
          <el-avatar :src="mainContent.headPosition" class="username" @click.native="toProfile(mainContent.userId)" @error="errorHandler" size="small" style="float: left;"></el-avatar>
          </span>
          <span style="padding-left: 6px;vertical-align:middle;" class="username" @click="toProfile(mainContent.userId)">{{this.mainContent.username}}</span>
          <span style="padding-left: 15px;vertical-align:middle;">浏览：{{this.mainContent.views}}</span>
          <span style="padding-left: 15px;vertical-align:middle;">学校：{{this.mainContent.school}}</span>
          <span style="padding-left: 15px;vertical-align:middle;">时间：{{this.mainContent.postdate}}</span>
        </div>
        <div class="content">
          {{this.mainContent.content}} <br>
          <el-image v-for="url in submit_images" :key="url" :src="url" lazy></el-image>
        </div>
      </div>
      <div class="comment">
        <div id="comment_post"></div>
        <div class="comment_show">
          <el-button type="primary" style="margin-right: 0;" icon="el-icon-edit" @click="post_comment">发布评论</el-button>
          <div v-if="showcomment">
            <div class="comment_show_posted">
              <p style="margin-top: 10px;text-align: right;" v-if="this.commmentNums">总共有 {{this.commmentNums}} 条评论</p>
              <div class="comment_show_posted_content" v-for="item in this.commentContent" :id="item.commentId">
                <div class="comment_show_posted_content_commenter">
                  <el-avatar :src="item.headPosition" class="username" @click.native="toProfile(item.userId)" @error="errorHandler" style="float: left;" :size="45"></el-avatar>
                  <div>
                    <span style="padding-left: 2%;font-size: 14px;" class="username" @click="toProfile(item.userId)">{{item.username}}</span>
                  </div>
                  <div>
                  <span style="padding-left: 2%;" >
                    <el-button type="success" size="mini" style="padding: 1px" icon="el-icon-chat-round" @click="reply(item.commentId,0,item.userId)">回复</el-button>
                  </span>
                    <span style="padding-left: 1%;font-size: 10px;"> {{item.commentTime}}</span>
                  </div>
                </div>
                <div class="comment_show_posted_content_information">
                  <!--                <p>评论内容</p>-->
                  <!--                返回格式{"commentContent":"img"}-->
                  <dl v-html="item.commentContent" v-lazy="item.commentContent" style="padding-left: 0.5%;">
                    {{item.commentContent}}
                  </dl>
                  <div class="comment_show_posted_content_information_reply">
                    <div class="comment_show_posted_content_information_reply_editor">
                      <el-button type="danger" size="medium" style="padding: 4px;margin-bottom: 1%" v-show="show_reply == item.commentId" @click="exit_reply">取消回复</el-button>
                      <!--                    放置编辑器-->
                      <div :id="'div'+item.commentId"></div>
                      <el-button type="primary" size="small" style="margin-right: 0;float: right;margin-top: 1%;" icon="el-icon-edit" @click="post_reply(item.commentId)"  v-show="show_reply == item.commentId">发布回复</el-button>
                      <!--                    清除浮动-->
                      <div class="clear"></div>
                    </div>
                    <div class="comment_show_posted_content_information_reply_information" v-for="item1 in testinfo" v-if="item.commentId === item1.commentId">
<!--                      回复展示-->
                      <div class="comment_show_posted_content_information_reply_information_replier">
                        <el-avatar :src="item1.headPosition" class="username" @click.native="toProfile(item1.fromuserId)" @error="errorHandler" style="float: left;margin-top:0.6%;" :size="36"></el-avatar>
<!--                        <div class="clear"></div>-->
                        <div v-show="item1.isat == 1">
                          <span style="padding-left: 1%;font-size: 13px;" class="username" @click="toProfile(item1.fromuserId)">{{item1.fromusername}}</span>
                          <span style="padding-left: 1%;font-size: 13px;color: #5cb87a; ">@</span>
                          <span style="font-size: 13px;" class="username" @click="toProfile(item1.touserId)">{{item1.touusername}}</span>
                        </div>
                        <div v-show="item1.isat == 0">
                          <span style="padding-left: 1%;font-size: 13px;" class="username" @click="toProfile(item1.fromuserId)">{{item1.fromusername}}</span>
                        </div>
                        <div>
                          <span style="padding-left: 1%;" >
                            <el-button type="success" size="mini" style="padding: 1px" icon="el-icon-chat-round" @click="reply(item.commentId,1,item1.fromuserId)">回复</el-button>
                          </span>
                          <span style="padding-left: 1%;font-size: 8px;"> {{item1.replyTime}}</span>
                        </div>
                      </div>
                      <div class="comment_show_posted_content_information_reply_information_replyInformation" style="padding-left: 5%;margin-top: 1%;margin-bottom: 1%;">
<!--                          回复内容-->
                        <dl v-html="item1.replycontent" v-lazy="item1.replycontent">
                          {{item1.replycontent}}
                        </dl>
                      </div>
                    </div>
                    <div v-show="!showNumReply && item.commentId === replyCommentId" style="background-color: #f3f9f6;">
                      <el-pagination
                              class="sysBox"
                              hide-on-single-page
                              background
                              layout="prev, pager, next"
                              :total="item.replyNums"
                              :current-page="replyPages"
                              :page-size="replypagesize"
                              @current-change="replyPage($event,item.commentId)">
                      </el-pagination>
                    </div>
                    <div v-show="item.replyNums>3?true:false">
                      <span style="font-size:12px" v-show="showNumReply || item.commentId !== replyCommentId">共有{{item.replyNums}}条回复，</span>
                      <span class="reply_click" @click="showMoreReply(item.commentId)" v-show="showNumReply || item.commentId !== replyCommentId">点击查看</span>
                    </div>
                  </div>
                </div>
              </div>
                <el-pagination
                        hide-on-single-page
                        layout="prev, pager, next"
                        :total="commmentNums"
                        :page-size="commentpagesize"
                        @current-change="commentPage">
                </el-pagination>
            </div>
          </div>
          <div v-else>
            loading...
          </div>
        </div>
      </div>
      <div>
<!--        接入登录页面-->
        <router-view/>
      </div>
    </div>
  </div>
</template>


<script>
  import E from 'wangeditor';
  import emoji from '../../static/emoji.json'
  const editor1 = new E('#comment_post');
  editor1.config.height = 200;
  editor1.config.focus = false;
  editor1.config.uploadImgServer = '/upload-img';  //图片接口地址
  editor1.config.uploadVideoServer = '/api/upload-video';
  editor1.config.showLinkImgAlt = false;
  editor1.config.showLinkImgHref = false;
  editor1.config.showFullScreen = false;  //配置全屏
  editor1.config.showMenuTooltips = true; //隐藏菜单栏提示
  editor1.config.showLinkImg = false;  //隐藏插入网络图片
  editor1.config.showLinkVideo = false;  //隐藏插入网络视频
  editor1.config.zIndex = 1; //降低editor权值,不遮挡其他元素
  editor1.config.menus = [
    'emoticon',
  ];
  editor1.config.emotions = emoji;
  export default {
    name: "PublicPost",
    data() {
      return {
        //记录当前查看回复的评论ID 来控制回复中分页按钮的展示
        replyCommentId: 0,
        showNumReply:true,
//      {"commentId":20,"fromuser":"zycdxxgc","touser":"test","content":"测试","postdate":"2021/04/14 09:06","flag":0}
        //回显回复内容
        testinfo:[],
        //回复时的编辑器
        editor: null,
        //editor使用者
        editorer: null,
        //是否打开回复的编辑器
        //通过判断show_reply与评论的id是否相等，来显示哪一条评论的取消回复以及发布评论按钮
        show_reply:false,
        showcomment:false,
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        //帖子内容
        mainContent:{
          title:'', //标题
          username:'',    //用户名
          views:0, //浏览量
          school:'',//发帖用户所在学校
          postdate:'',//发帖时间
          content:'',//发帖内容
        },
        submit_images:[

        ],//照片路径
        //评论内容
        commentContent:[],
        comment:{
          publicPostId: '',
          accountName: '',
          content: ''
        }, //评论的相关信息
        commmentNums:0, //评论的条数
        nowPage:1, //评论当前页
        replypagesize:6, //每条评论的回复条数
        commentpagesize:10,//每页的评论条数
        replyinfo:{
          commentId: 0,
          fromuser:'',
          touser:0,
          content:'',
          //判断回复中是否加@符号
          isat:0,
          publicpostid:this.$route.params.publicPostId
        },
        //当用户在第二页回复时，记录当前用户的回复的页码，这样就能加载第二页的数据
        replyPages:1
      }
    },
    methods:{
      toProfile(userId){
        const _this = this;
        if(this.$store.state.Authorization){
          axios.get('http://localhost:8181/account/checkAccountState').then(function (resp) {
            console.log(resp);
            if(resp.data.msg !== '账号登录状态检测成功'){
              localStorage.removeItem('Authorization');
              localStorage.removeItem('AccountName');
              localStorage.removeItem('UserId');
              _this.$message.error("登录信息过期，请重新登录");
              _this.$router.go(0);
            }else {
              window.open('/u/'+userId, '_blank');
            }
          })
        }else {
          this.$router.push('/publicpost/'+_this.$route.params.publicPostId+'/login');
        }
      },
      replyPage(val,commentId){
        //val:当前页  commentId:评论ID
        const _this = this;
        //分页自动滚动到头部
        document.getElementById(commentId).scrollIntoView();
        this.replyPages = val;
        axios.get('http://localhost:8181/publicpost/findreply/'+_this.$route.params.publicPostId+'/'+commentId+'/'+_this.nowPage+'/'+val+'/'+_this.commentpagesize+'/'+_this.replypagesize).then(function (resp) {
          // _this.commentContent = resp.data.commentContent;
          for(let item in resp.data.replyContent){
            resp.data.replyContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.replyContent[item].headPosition;
          }
          _this.testinfo = resp.data.replyContent;
        })
      },
      showMoreReply(commentId){
        const _this = this;
        axios.get('http://localhost:8181/publicpost/findreply/'+_this.$route.params.publicPostId+'/'+commentId+'/'+_this.nowPage+'/'+'1'+'/'+_this.commentpagesize+'/'+_this.replypagesize).then(function (resp) {
          // _this.commentContent = resp.data.commentContent;
          for(let item in resp.data.replyContent){
            resp.data.replyContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.replyContent[item].headPosition;
          }
          _this.testinfo = resp.data.replyContent;
          _this.replyCommentId = commentId;
          _this.showNumReply = false;
          _this.replyPages = 1;
        })
      },
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
      post_reply(commentId){
        console.log("commentId:"+commentId);
        //对帖子中的评论进行回复
        const _this = this;
        //参数  帖子ID 用户ID 内容  isat
          //isat === 1 加@
          //通过评论的ID可以找到这条评论的用户ID
        if(this.$store.state.Authorization){
          if(this.editor.txt.html() && this.check_input(this.editor.txt.text())){
            this.replyinfo.commentId = commentId;
            this.replyinfo.fromuser = localStorage.getItem('AccountName');
            this.replyinfo.content = this.editor.txt.html();
            console.log(this.replyinfo);
            axios.post('http://localhost:8181/publicpost/reply',_this.replyinfo).then(function(resp){
              console.log(resp);
              if(resp.data.msg === '回复成功'){
                _this.$message.success('回复成功');
                _this.exit_reply();
                console.log('http://localhost:8181/publicpost/findreply/'+_this.$route.params.publicPostId+'/'+commentId+'/'+_this.nowPage+'/'+_this.replyPages+'/'+_this.commentpagesize+'/'+_this.replypagesize);
                //再拿一次数据 渲染页面 用updated方法自动渲染
                axios.get('http://localhost:8181/publicpost/findreply/'+_this.$route.params.publicPostId+'/'+commentId+'/'+_this.nowPage+'/'+_this.replyPages+'/'+_this.commentpagesize+'/'+_this.replypagesize).then(function (resp) {
                  // _this.commentContent = resp.data.commentContent;
                  for(let item in resp.data.replyContent){
                    resp.data.replyContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.replyContent[item].headPosition;
                  }
                  console.log(resp.data.replyContent);
                  _this.testinfo = resp.data.replyContent;
                })
              }
            })
          }else{
            this.$message.error('内容不能为空');
          }
        }else{
          this.$router.push('/publicpost/'+_this.$route.params.publicPostId+'/login');
        }

      },
      createEditor(commentId){
        //初始化一个editor
        this.editor = new E('#div'+commentId);
        console.log("editor已初始化");
        this.editor.config.height = 100;
        this.editor.config.focus = false;
        this.editor.config.uploadImgServer = '/upload-img';  //图片接口地址
        this.editor.config.uploadVideoServer = '/api/upload-video';
        this.editor.config.showLinkImgAlt = false;
        this.editor.config.showLinkImgHref = false;
        this.editor.config.showFullScreen = false;  //配置全屏
        this.editor.config.showMenuTooltips = true; //隐藏菜单栏提示
        this.editor.config.showLinkImg = false;  //隐藏插入网络图片
        this.editor.config.showLinkVideo = false;  //隐藏插入网络视频
        this.editor.config.zIndex = 1; //降低editor权值,不遮挡其他元素
        this.editor.config.menus = [
          'emoticon',
        ];
        this.editor.config.emotions = emoji;
      },
      exit_reply() {
        //取消回复进行的操作
        this.show_reply = false;
        this.editor.destroy();
        this.editor = null;
      },
      reply(commentId,index,id){
        //回复帖子的操作
        //如果当前没有创建编辑器 则创建一个编辑器
        //如果当前已经创建了编辑器 则看已创建的编辑器的使用者id使用与传进来的id相等，如果不等，则销毁先前的编辑器
        //在为新传来的id创建一个编辑器
        const _this = this;
        if(this.$store.state.Authorization){
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
            }else{
              _this.replyinfo.touser = id;
              if(index === 0){
                _this.replyinfo.isat = 0;// 加@符号
              }else if(index ===1){
                _this.replyinfo.isat = 1;
              }
              if(_this.editor === null){
                _this.createEditor(commentId);
                _this.editor.create();
                _this.editorer = commentId;
                _this.show_reply = commentId;
              }else if(_this.editorer !== commentId){
                _this.editor.destroy();
                _this.editor = null;
                _this.createEditor(commentId);
                _this.editor.create();
                _this.editorer = commentId;
                _this.show_reply = commentId;
              }
              document.getElementById(commentId).scrollIntoView();
              console.log("editor已创建");
              console.log(_this.editor);
            }
          })
        }else{
          this.$router.push(this.$route.path+'/login');
        }
      },
      commentPage(currentPage) {
        //评论分页
        const _this = this;
        axios.get('http://localhost:8181/publicpost/findpublicostComment/'+_this.$route.params.publicPostId+'/'+currentPage+'/'+_this.commentpagesize).then(function (resp) {
          for(let item in resp.data.commentContent){
            resp.data.commentContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.commentContent[item].headPosition
          }
          for(let item in resp.data.replyContent){
            resp.data.replyContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.replyContent[item].headPosition;
          }
          _this.commentContent = resp.data.commentContent;
          _this.commmentNums = resp.data.commentCount;
          _this.testinfo = resp.data.replyContent;
          _this.nowPage = currentPage;
        })
      },
      post_comment(){
        //评论帖子
        const _this = this;
        //参数  帖子ID 用户ID 内容
        if(this.$store.state.Authorization){
          if(editor1.txt.html() && this.check_input(editor1.txt.text())){
            this.comment.publicPostId = this.$route.params.publicPostId;
            this.comment.accountName = localStorage.getItem('AccountName');
            this.comment.content = editor1.txt.html();
            axios.post('http://localhost:8181/publicpost/publicpostComment',{comment:_this.comment}).then(function(resp){
              if(resp.data.msg === '评论成功'){
                _this.$message.success('评论成功');
                editor1.txt.clear();
                //再拿一次数据 渲染页面 用updated方法自动渲染
                axios.get('http://localhost:8181/publicpost/findpublicostComment/'+_this.$route.params.publicPostId+'/'+_this.nowPage+'/'+_this.commentpagesize).then(function (resp) {
                  _this.commentContent = resp.data.commentContent;
                  // _this.testinfo = resp.data.replyContent;
                  _this.commmentNums = resp.data.commentCount;
                })
              }else if(resp.data.msg === '评论失败'){
                _this.$message.error("评论失败");
              }else{
                //token问题
                localStorage.removeItem('Authorization');
                localStorage.removeItem('AccountName');
                localStorage.removeItem('UserId');
                _this.$message.error("登录信息过期，请重新登录");
                setTimeout(()=>{
                  _this.$router.go(0);
                },500)
              }
            })
          }else{
            this.$message.error('内容不能为空');
          }
        }else{
          this.$router.push(this.$route.path+'/login');
        }
      },
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        // return true
      },
    },
    updated:function(){
      //用户评论后 动态更新页面
    },
    mounted() {
      editor1.create();
      // editor2.create();
    },
    created() {
      const _this = this;
      //帖子内容展示
      axios.get('http://localhost:8181/publicpost/findPost/'+this.$route.params.publicPostId).then(function (resp) {
        resp.data.publicpost.headPosition = 'http://localhost:8181/profilephoto/'+resp.data.publicpost.headPosition;
        if(resp.data.publicpost.submit_images!==null){
          var ss = resp.data.publicpost.submit_images.split(",");
          for(var i = 0 ; i<ss.length-1;i++){
            _this.submit_images.push('http://localhost:8181/images/'+ss[i]);
          }
        }
        _this.mainContent = resp.data.publicpost;
        console.log(_this.mainContent);
      })
      //评论显示
      axios.get('http://localhost:8181/publicpost/findpublicostComment/'+this.$route.params.publicPostId+'/1/10').then(function (resp) {
        console.log(resp)
       //评论的数据
        for(let item in resp.data.commentContent){
          resp.data.commentContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.commentContent[item].headPosition
        }
        for(let item in resp.data.replyContent){
          resp.data.replyContent[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.replyContent[item].headPosition;
        }
        _this.commentContent = resp.data.commentContent;
       //评论的总数
        _this.commmentNums = resp.data.commentCount;
        //回复的内容
        _this.testinfo = resp.data.replyContent;
        console.log(_this.testinfo);
        _this.showcomment = true;
      })
    }
  }
</script>

<style scoped>
  .clear {
    clear: both;
  }
  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 65%;
    position: relative;
    margin-left: 15%;
    /*float: left;*/
    height: 100%;
    /*overflow: scroll;*/
    /*border: 1px solid red;*/
    /*background-color:  #ffffff;*/
  }
  .titleAndContent{
    background-color:  #ffffff;
    padding: 20px;
  }
  .title{
    /*height: 15%;*/
    /*border: 1px solid red;*/
    position: relative;
  }
  .title > span{
    font-size:13px;
    /*font-weight: bold;*/
    padding-right: 16px;
    color:rgba(0,0,0,.5) ;
  }
  .username:hover{
    color: #409EFF;
    cursor: pointer;
   }
  .content{
    margin-top: 4%;
    /*border: 1px solid red;*/
  }
  .comment{
    margin-top: 4%;
    /*border: 1px solid red;*/
    background-color:  #ffffff;
    padding: 20px;
  }
  .comment_show{
    margin-top: 2%;
  }
  .comment_show_posted{
    margin-top: 2%;
  }
  .comment_show_posted_content{
    /*border-right: 1px solid red;*/
    margin-top: 2%;
    padding: 15px;
    background-color: #f3f9f6;
  }
  .comment_show_posted_content_information{
    padding-left: 6.4%;
  }
  .comment_show_posted_content_information >>> img{
    /*让表情对齐文本的下标*/
    vertical-align: sub
  }
  .reply_click{
    font-size:12px;
    color:#409EFF;
    cursor: pointer;
  }
  /*对回复分页按钮样式的修改*/
  /deep/  .el-pagination.is-background .el-pager li:not(.disabled).active {
    background-color:#f3f9f6;
    color: #409EFF;
  }
  /deep/  .el-pagination.is-background .el-pager li:not(.disabled) {
    background-color:#f3f9f6;
  }
  /deep/  .el-pagination.is-background .btn-next {
    background-color:#f3f9f6;
  }
  /deep/  .el-pagination.is-background .btn-prev {
    background-color:#f3f9f6;
  }
</style>
