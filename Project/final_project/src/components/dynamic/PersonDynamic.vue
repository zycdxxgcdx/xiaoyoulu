<template>
  <div>
    <div class="left_main_content">
      <div class="left_main_content_child">
            <div class="left_main_content_child_one" style="margin-bottom: 1%;height: auto;padding-bottom: 2%">
              <!--头像-->
              <div class="username" @click="toProfile(dynamicContent.userId)" style="margin-left:2%;padding-top: 3%;float: left;">
                <el-avatar :src="dynamicContent.headPosition" @error="errorHandler" :size="45">
                </el-avatar>
              </div>
              <!--用户名-->
              <div style="margin:0 0 0 12%;padding-top:3px;width: 50%;">
                <h4 style="margin-bottom: 0" class="username" @click="toProfile(dynamicContent.userId)" >{{dynamicContent.username}}</h4>
                <span style="font-size: 8px;color:rgba(0,0,0,.5);">{{dynamicContent.dynamic_time}}</span>
              </div>
              <!--内容-->
              <div style="margin:2% 1% 0 12%;font-size: 16px">
                <dl v-html="dynamicContent.content" v-lazy="dynamicContent.content">
                  {{dynamicContent.content}}
                </dl>
                <div style="margin-top: 2%;width: 70%">
                  <el-image v-for="item1 in dynamicContent.submit_images"  :src="item1" alt="加载失败" fit="fit" style="width: 120px; height: 120px;margin-right: 1%;"
                            :preview-src-list="dynamicContent.submit_images"></el-image>
                </div>
              </div>
              <el-divider></el-divider>
              <!--点赞，评论，转发-->
              <div>
                <ul style="text-decoration: none;margin: 0;padding: 0;">
                  <li style="display: inline-block;margin:0 10% 0 14%;width: 7%">
                    <div class="left_main_content_child_one_dpz">
                      <i id="dianzan" class="iconfont icondianzan1" @click="likes(dynamicContent.dynamicId,dynamicContent.islike)" style="font-size:14px;color:rgba(0,0,0,.5);pointer-events: auto"> {{dynamicContent.likesNums}}</i>
                    </div>
                  </li>
                  <el-divider direction="vertical"></el-divider>
                  <li style="display: inline-block;margin:0 10% 0 14%;width: 7%">
                    <div class="left_main_content_child_one_dpz">
                      <i class="el-icon-chat-round" @click="toCommentPage(dynamicContent.dynamicId)" style="font-size:14px">
                          {{dynamicContent.replyNums}}
                      </i>
                    </div>
                  </li>
                  <el-divider direction="vertical"></el-divider>
                  <li style="display: inline-block;margin:0 12%;width: 7%">
                    <!-- 转发前要有个空格-->
                    <div class="left_main_content_child_one_dpz">
                      <i class="el-icon-share" @click="toForwardPage(dynamicContent.dynamicId)" style="font-size:14px">
                        {{dynamicContent.forwardNums}}
                      </i>
                    </div>
                  </li>
                </ul>
              </div>
              <el-divider></el-divider>

                  <!--              评论以及回复内容-->
              <div style="height: auto" v-if="dynamicContent.dynamicId === showCommentController">
                <div style="margin-left:2%;padding-top: 2%;">
                  <!--              编辑器-->
                  <div id="commentEditor" style="padding-left: 10%;width:85%;"></div>
                  <el-button type="primary" style="float: right;margin: 2% 5% 2% 0;" @click="commentDynamic(dynamicId)">评论</el-button>
                  <div style="clear:both"></div>
                  <el-divider></el-divider>
                </div>
                <div v-if="CommentLoading" style="text-align: center;">loading...</div>
                <ul class="infinite-list" v-infinite-scroll="loadComment"  :infinite-scroll-disabled="loadCommentDisabled" infinite-scroll-delay="1000" style="overflow:auto;padding-left: 0;">
                  <li v-for="item1 in commentContent" class="infinite-list-item" style="list-style: none;">
                <div :id="item1.commentId">
                  <div class="username" @click="toProfile(item1.userId)">
                    <el-avatar :src="item1.headPosition" @error="errorHandler" :size="45" style="float: left;margin-left: 10%;margin-top: 1%;"></el-avatar>
                  </div>
                  <div style="margin-left: 20%;">
                    <span style="font-size:14px;" class="username" @click="toProfile(item1.userId)">{{item1.username}}</span>
                    <div style="font-size:16px;">
                      <dl v-html="item1.content" v-lazy="item1.content" style="margin:2% 1% 2% 0" class="replyclass">
                        {{item1.content}}
                      </dl>
                    </div>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);">{{item1.timeDifference}}</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-if="item1.timeDifferenceType === 'day'">天前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item1.timeDifferenceType === 'hour'">小时前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item1.timeDifferenceType === 'minute'">分钟前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item1.timeDifferenceType === 'second'">秒前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);padding-left: 5%;" class="reply" @click="reply(item1.commentId,0)">回复</span>
                  </div>
                  <div :id="'reply'+item1.commentId" style="margin-left: 20%;margin-top: 1%;margin-right: 1%;"></div>
                  <el-button type="primary" style="float: right;margin: 1% 1% 0 0 " size="small" v-show="item1.commentId === replyController" @click="doReply(dynamicId,item1.commentId,0)">回复</el-button>
                  <div style="clear: both;"></div>
<!--                      <el-divider/>-->
                  <!--                  回复内容-->
                  <div style="margin-left: 20%;margin-bottom: 1%;" v-for="item2 in replyContent" v-if="item2.commentId === item1.commentId">
                    <div class="username" @click="toProfile(item2.fromuserId)">
                      <el-avatar :src="item2.headPosition" @error="errorHandler" :size="40" style="float: left;margin-right: 2%;margin-top: 1%;"></el-avatar>
                    </div>
                    <!--                    <p style="font-size:14px">{{item1.username}}</p>-->
                    <div v-show="item2.isat == 1">
                      <span style="font-size: 13px;" class="username" @click="toProfile(item2.fromuserId)">{{item2.fromusername}}</span>
                      <span style="font-size: 13px;color: #5cb87a; "> @</span>
                      <span style="font-size: 13px;" class="username" @click="toProfile(item2.touserId)">{{item2.touusername}}</span>
                    </div>
                    <div v-show="item2.isat == 0">
                      <span style="font-size: 13px;" class="username" @click="toProfile(item2.fromuserId)">{{item2.fromusername}}</span>
                    </div>
                    <div style="font-size:16px;margin-left: 9%;">
                      <dl class="replyclass" v-html="item2.content" v-lazy="item2.content" style="margin: 0;padding: 0;">
                        {{item2.content}}
                      </dl>
                    </div>
                    <div style="margin-left: 9.2%;margin-bottom: 1%;">
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);">{{item2.timeDifference}}</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-if="item2.timeDifferenceType === 'day'">天前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item2.timeDifferenceType === 'hour'">小时前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item2.timeDifferenceType === 'minute'">分钟前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item2.timeDifferenceType === 'second'">秒前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);padding-left: 5%;" class="reply" @click="reply(item2.dynamicReplyId,1)">回复</span>
                    </div>
                    <div :id="'replyToReply'+item2.dynamicReplyId" style="margin-left: 10%;margin-top: 1%;margin-right: 1%;"></div>
                    <el-button type="primary" style="float: right;margin: 1% 1% 0 0 " size="small" v-show="item2.dynamicReplyId === replyToReplyController" @click="doReply(dynamicId,item1.commentId,1,item2.fromuserId)">回复</el-button>
                    <div style="clear: both;"></div>
                  </div>
                  <div v-show="item1.nums>3?true:false" style="margin-left: 20%;margin-bottom: 1%;">
                    <span style="font-size:12px" v-show="showNumReply || item1.commentId !== replyCommentId">共有{{item1.nums}}条回复，</span>
                    <span class="reply_click" @click="showMoreReply(dynamicId,item1.commentId)" v-show="showNumReply || item1.commentId !== replyCommentId">点击查看</span>
                    <div v-show="!showNumReply && item1.commentId === replyCommentId">
                      <el-pagination
                              hide-on-single-page
                              layout="prev, pager, next"
                              :total="item1.nums"
                              :page-size="replypagesize"
                              :current-page="replyPages"
                              @current-change="replyPage($event,dynamicId,item1.commentId)">
                      </el-pagination>
                    </div>
                  </div>
                  <!--                  <div :id="'reply'+item1.commentId" style="margin-left: 20%;margin-top: 1%;margin-right: 1%;"></div>-->
                  <div>
                    <el-divider></el-divider>
                  </div>
                </div>

                  </li>
                </ul>
              </div>
<!--              转发内容-->
              <div style="height: auto" v-if="dynamicContent.dynamicId === showForwardController">
                <div style="margin-left:2%;padding-top: 2%;">
                  <!--              编辑器-->
                  <div id="forwardEditor" style="padding-left: 10%;width:85%;"></div>
                  <el-button type="primary" style="float: right;margin: 2% 5% 2% 0;" @click="forwardDynamic(dynamicContent.dynamicId)">转发</el-button>
                  <div style="clear:both"></div>
                  <el-divider></el-divider>
                </div>
                <div v-if="ForwardLoading" style="text-align: center;">loading...</div>
                <ul class="infinite-list"  v-infinite-scroll="loadComment"  :infinite-scroll-disabled="loadCommentDisabled" infinite-scroll-delay="1000" style="overflow:auto;padding-left: 0;">
                  <li v-for="item1 in forwardContent" class="infinite-list-item" style="list-style: none;">
                    <div class="username" @click="toProfile(item1.userId)">
                      <el-avatar :src="item1.headPosition" @error="errorHandler" :size="45" style="float: left;margin-left: 10%;margin-top: 1%;"></el-avatar>
                    </div>
                    <div style="margin-left: 20%;">
                      <span style="font-size:14px;" class="username" @click="toProfile(item1.userId)">{{item1.username}}</span>
                      <div style="font-size:16px;">
                        <dl v-html="item1.content" v-lazy="item1.content" style="margin:2% 1% 2% 0" class="replyclass">
                          {{item1.content}}
                        </dl>
                      </div>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);">{{item1.timeDifference}}</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-if="item1.timeDifferenceType === 'day'">天前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item1.timeDifferenceType === 'hour'">小时前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item1.timeDifferenceType === 'minute'">分钟前</span>
                      <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item1.timeDifferenceType === 'second'">秒前</span>
                    </div>
                    <div style="clear: both;"></div>
                    <div>
                      <el-divider></el-divider>
                    </div>
                  </li>
                </ul>
              </div>
              <p v-if="loading" style="text-align: center">加载中...</p>
              <p v-if="noMore" style="text-align: center;">没有更多了...</p>
            </div>
      </div>
    </div>
  </div>
</template>

<script>
  import E from 'wangeditor'
  import emoji from '../../static/emoji.json'
  export default {
    name: "PersonDynamic",
    data() {
      return {
        commentEditor:null,
        forwardEditor:null,
        // 0 评论无限加载  1 转发无线加载
        infiniteScroll:0,
        //load拿取评论时 拿取当前评论的第几页
        comment_num:2,
        //load拿取评论时 拿取当前回复的第几页
        reply_num:2,
        forward_num:2,
        dynamicId:0,
        //控制回复按钮的show
        replyController:null,
        showNumReply:true,
        //记录当前查看回复的评论ID 来控制回复中分页按钮的展示
        replyCommentId: 0,
        //当用户在第二页回复时，记录当前用户的回复的页码，这样就能加载第二页的数据
        replyPages:1,
        //评论的内容
        commentContent:[],
        //转发的内容
        forwardContent:[],
        CommentLoading:true,
        ForwardLoading:true,
        // replyNums:120,
        replypagesize:6,
        loading:true,
        noMore:false,
        loadCommentDisabled:false,
        loadForwardDisabled:true,
        //拿取回复时的loading
        ReplyLoading:true,
        //回复的内容
        replyContent:[],
        //回复的当前页
        nowReplyPage:1,
        dynamicContent:[{headPosition:'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          username:'加载中',dynamic_time:'加载中',content:'加载中',submit_images:[]}],
        showCommentController:this.$route.params.dynamicId,
        showForwardController:null,
        maxLoadPhoto:9,
        token:{token:this.$store.state.Authorization},
        //用户上传的图片地址暂存的位置
        fileList: [],
        submitInfo:{
          content:'',
          submitList:'',
          userId:this.$store.state.UserId
        },
        submitCommentInfo:{
          userId:this.$store.state.UserId,
          dynamicId:0,
          content:'',
        },
        submitForwardInfo:{
          userId:this.$store.state.UserId,
          forwardDynamicId:0,
          content:'',
        },
        //回复内容提交到后台的数据格式
        replyCommentInfo:{
          commentId:0,
          isat:0,
          content:'',
          fromuserId:this.$store.state.UserId,
          touserId:'',
        },
        submitReplyInfo:{
          userId:this.$store.state.UserId,
          dynamicId:0,
          content:'',
        },
        //评论时的编辑器
        editor: null,
        //回复时的编辑器
        replyEditor:null,
        //回复编辑器的使用者
        replyEditorer:null,
        //控制回复评论中的回复的按钮显示
        replyToReplyController:null,
        //flag==0代表对评论的回复   flag==1代表对回复的回复
        replyFlag:-1,

        dialogImageUrl1:[ 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'],
        dynamireplyPagescContent:[{headPosition:'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          username:'加载中',dynamic_time:'加载中',content:'加载中',submit_images:[]}],
        dialogImageUrl: '',
        dialogVisible: false,
        num: 1,
        flag:0,
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
      }
    },
    methods: {
      toCommentPage(dynamicId){
        const _this = this;
        this.infiniteScroll = 0
        this.loadCommentDisabled = false;
        if(this.showCommentController === null){
          this.forwardEditor.destroy();
          this.forwardEditor = null;
          this.commentEditor = new E('#commentEditor');
          // this.createEditor(editor1);
          this.createEditor(this.commentEditor);
          this.$nextTick(function () {
            setTimeout(()=>{
              this.commentEditor.create();
            },100)
          });
          this.showCommentController = dynamicId;
          this.showForwardController = null;
          _this.noMore = false;
          // _this.loadCommentDisabled = false;
          _this.loading = true;
          _this.comment_num = 2;
          _this.reply_num = 2;
          //拿取动态中的前6条评论
          axios.get('http://localhost:8181/dynamic/findDynamicComment/'+dynamicId).then(function (resp) {
            if(resp.data.msg === '查询回复成功'){
              _this.CommentLoading = false;
              for(let item in resp.data.comment){
                resp.data.comment[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.comment[item].headPosition;
              }
              _this.commentContent = resp.data.comment;
            }else if(resp.data.msg === '查询回复异常'){
              _this.$message.warning("查询回复发送异常");
            }
          })
          //拿取前6条评论，每一条评论的前3条回复
          axios.get('http://localhost:8181/dynamic/findDynamicReply/'+dynamicId).then(function (resp) {
            if(resp.data.msg === '查询回复成功'){
              for(let item in resp.data.reply){
                resp.data.reply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.reply[item].headPosition;
              }
              _this.replyContent = resp.data.reply;
              _this.CommentLoading = false;
            }else{
              _this.$message.warning("查询回复发送异常");
            }
          })
        }
      },
      toForwardPage(dynamicId){
        const _this =this;
        this.infiniteScroll = 1;
        this.loadCommentDisabled = false;
        if(this.showForwardController === null){
          //建立转发编辑器 销毁评论编辑器
          this.commentEditor.destroy();
          this.commentEditor = null;
          this.forwardEditor = new E('#forwardEditor');
          // this.createEditor(editor1);
          this.createEditor(this.forwardEditor);
          this.$nextTick(function () {
            setTimeout(()=>{
              this.forwardEditor.create();
            },100)
          });
          //关闭评论界面回复的编辑器
          this.replyController = null;
          this.replyToReplyController = null;
          //控制转发界面的打开
          this.showForwardController = dynamicId;
          //控制评论界面的关闭
          this.showCommentController = null;
          //控制转发界面的无限加载
          this.forward_num = 2;
          this.noMore = false;
          // this.loadForwardDisabled = false;
          this.loading = true;
          axios.get('http://localhost:8181/dynamic/findDynamicForward/'+dynamicId+'/1/6').then(function (resp) {
            console.log(resp);
            if(resp.data.msg ==='查询转发成功'){
              for(let item in resp.data.forwardInfo){
                resp.data.forwardInfo[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.forwardInfo[item].headPosition;
              }
              _this.forwardContent = resp.data.forwardInfo;
              _this.ForwardLoading = false;
            }
          })
        }
      },
      toProfile(userId){
        window.open('/u/'+userId, '_blank');
      },
      doReply(dynamicId,commentId,isAt,touserId){
        const _this = this;
        if(this.$store.state.Authorization) {
          if (this.replyEditor.txt.html() && this.check_input(this.replyEditor.txt.text())) {
            if(isAt === 1){
              this.replyCommentInfo.touserId = touserId;
            }
            this.replyCommentInfo.commentId = commentId;
            this.replyCommentInfo.isat = isAt;
            this.replyCommentInfo.content = this.replyEditor.txt.html();
            axios.post('http://localhost:8181/dynamic/postDynamicCommentReply/',this.replyCommentInfo).then(function (resp) {
              // console.log(resp);
              if(resp.data.msg === '回复发送成功'){
                _this.$message.success("回复成功");
                _this.replyEditor.destroy();
                _this.replyEditor = null;
                _this.replyEditorer = null;
                _this.replyController = null;
                _this.replyToReplyController = null;
                _this.showMoreReply(dynamicId,commentId);
              }else{
                _this.$message.error("回复发送异常");
              }
            })
          }else{
            this.$message.error('内容不能为空');
          }
        }else{
          this.$router.push(this.$route.path+'/login');
        }
      },
      commentDynamic(dynamicId){
        // console.log(dynamicId);
        const _this = this;
        // document.getElementById(dynamicId).scrollIntoView();
        if(this.$store.state.Authorization) {
          // console.log("========================");
          if(this.commentEditor.txt.html() && this.check_input(this.commentEditor.txt.text())) {
            // console.log(dynamicId);
            // console.log(editor1.txt.html());
            this.submitCommentInfo.dynamicId = dynamicId;
            this.submitCommentInfo.content = this.commentEditor.txt.html();
            // console.log(this.submitCommentInfo);
            axios.post('http://localhost:8181/dynamic/postDynamicComment',this.submitCommentInfo).then(function (resp) {
              // console.log(resp);
              if(resp.data.msg === '评论发送成功'){
                _this.$message.success('评论成功');
                editor1.txt.clear();
                _this.noMore = true;
                _this.disabled = true;
                _this.reply_num = 2;
                axios.get('http://localhost:8181/dynamic/findDynamicComment/'+dynamicId).then(function (resp) {
                  // console.log(resp);
                  if(resp.data.msg === '查询回复成功'){
                    _this.CommentLoading = false;
                    for(let item in resp.data.comment){
                      resp.data.comment[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.comment[item].headPosition;
                    }
                    _this.commentContent = resp.data.comment;
                    // console.log(resp.data.comment);
                  }else if(resp.data.msg === '查询回复异常'){
                    _this.$message.warning("查询回复发送异常");
                  }
                })
              }else if(resp.data.msg === '查询回复异常'){
                _this.$message.warning("查询回复发送异常");
              }
            })
          }else{
            this.$message.error('内容不能为空');
          }
        }else{
          this.$router.push(this.$route.path+'/login');
        }
      },
      forwardDynamic(dynamicId){
        const _this = this;
        // document.getElementById(dynamicId).scrollIntoView();
        if(this.$store.state.Authorization) {
          // console.log("========================");
          if(this.forwardEditor.txt.html() && this.check_input(this.forwardEditor.txt.text())) {
            this.submitForwardInfo.forwardDynamicId = dynamicId;
            this.submitForwardInfo.content = this.forwardEditor.txt.html();
            console.log(this.submitCommentInfo);
            axios.post('http://localhost:8181/dynamic/postDynamicForward',this.submitForwardInfo).then(function (resp) {
              console.log(resp);
              if(resp.data.msg ==='转发成功'){
                _this.forwardEditor.txt.clear();
                // _this.showForwardController = null;
                _this.$message.success("转发成功");
                axios.get('http://localhost:8181/dynamic/findDynamicForward/'+dynamicId+'/1/6').then(function (resp) {
                  console.log(resp);
                  if(resp.data.msg ==='查询转发成功'){
                    for(let item in resp.data.forwardInfo){
                      resp.data.forwardInfo[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.forwardInfo[item].headPosition;
                    }
                    _this.forwardContent = resp.data.forwardInfo;
                    _this.forward_num = 2;
                    _this.noMore = false;
                    _this.disabled = false;
                    _this.loading = true;
                  }
                })
              }
            })
          }else{
            this.$message.error('内容不能为空');
          }
        }else{
          this.$router.push(this.$route.path+'/login');
        }
      },
      replyPage(val,dynamicId,commentId) {
        //评论分页
        const _this = this;
        document.getElementById(commentId).scrollIntoView();
        this.replyPages = val;
        axios.get('http://localhost:8181/dynamic/findDynamicReply/'+dynamicId+'/'+commentId+'/'+this.replyPages+'/'+this.replypagesize).then(function (resp) {
          // _this.commentContent = resp.data.commentContent;
          for(let item in resp.data.reply){
            resp.data.reply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.reply[item].headPosition;
          }
          _this.replyContent = resp.data.reply;
          // _this.testinfo = resp.data.replyContent;
          _this.replyCommentId = commentId;
          _this.showNumReply = false;
        })
      },
      showMoreReply(dynamicid,commentId){
        const _this = this;
        axios.get('http://localhost:8181/dynamic/findDynamicReply/'+dynamicid+'/'+commentId+'/'+'1'+'/'+'6').then(function (resp) {
          // _this.commentContent = resp.data.commentContent;
          for(let item in resp.data.reply){
            resp.data.reply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.reply[item].headPosition;
          }
          console.log(resp.data.reply);
          for(let item1 in resp.data.reply){
            for(let item in _this.commentContent){
              if(_this.commentContent[item].commentId === resp.data.reply[item1].commentId){
                _this.commentContent[item].nums = resp.data.reply[item1].nums;
              }
            }
          }
          _this.replyContent = resp.data.reply;
          _this.replyCommentId = commentId;
          _this.showNumReply = false;
          _this.replyPages = 1;
        })
      },
      loadComment(){
        const _this = this;
        console.log("+++++++++++++++++++++++++---------------+++++++++++++++++++")
        setTimeout(() => {
          if(this.infiniteScroll === 0){
            axios.get('http://localhost:8181/dynamic/findDynamicComment/' + this.dynamicId + '/' + this.comment_num + '/6').then(function (resp) {
              // // console.log(resp);
              if (resp.data.msg === '查询回复成功') {
                _this.CommentLoading = false;
                for (let item in resp.data.comment) {
                  resp.data.comment[item].headPosition = 'http://localhost:8181/profilephoto/' + resp.data.comment[item].headPosition;
                }
                _this.commentContent = _this.commentContent.concat(resp.data.comment);
                _this.comment_num++;
                axios.get('http://localhost:8181/dynamic/findDynamicMoreReply/' + _this.dynamicId + '/' + _this.reply_num + '/6').then(function (resp) {
                  // console.log(resp);
                  if (resp.data.msg === '查询回复成功') {
                    for (let item in resp.data.reply) {
                      resp.data.reply[item].headPosition = 'http://localhost:8181/profilephoto/' + resp.data.reply[item].headPosition;
                    }
                    _this.replyContent = _this.replyContent.concat(resp.data.reply);
                    _this.reply_num++;
                  } else if (resp.data.msg === '查询回复异常') {
                    _this.$message.warning("查询回复发送异常");
                  }
                })
                if (resp.data.comment.length !== 6) {
                  _this.noMore = true;
                  _this.loadCommentDisabled = true;
                  _this.loading = false;
                }
              } else if (resp.data.msg === '查询回复异常') {
                _this.$message.warning("查询回复发送异常");
              }
            })
          }else if(this.infiniteScroll === 1){
            axios.get('http://localhost:8181/dynamic/findDynamicForward/'+this.dynamicId+'/'+this.forward_num+'/6').then(function (resp) {
              console.log("++++++++++++++++++++++");
              console.log(resp);
              console.log("++++++++++++++++++++++");
              if(resp.data.msg ==='查询转发成功'){
                for(let item in resp.data.forwardInfo){
                  resp.data.forwardInfo[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.forwardInfo[item].headPosition;
                }
                _this.forwardContent = _this.forwardContent.concat(resp.data.forwardInfo);
                _this.forward_num++;
                if(resp.data.forwardInfo.length < 6){
                  _this.noMore = true;
                  _this.loadCommentDisabled = true;
                  _this.loading = false;
                  console.log(_this.forwardContent)
                }
              }
            })
          }
        },2000)
      },
      handlephotoSuccess(resp){
        this.fileList.push(resp);
      },
      outUploadLenth(){
        this.$message.error("图片最多上传9张");
      },
      errorHandler() {
        this.circleUrl = "https://cube.elemecdn.com/e/fd/0fc7d20532fdaf769a25683617711png.png"
        return true
      },
      check_input(text){
        //判断发帖内容中是否全为空格
        var flag = false;
        let str = text.replace(/\s+/g,""); //去除空格
        let str2 = str.replace(/&nbsp+/g,"");//再去除&nbsp;
        for(let index in str2){
          // console.log(str2[index]);
          if(str2[index] !== ';'){
            flag = true;
            break;
          }
        }
        return flag;
      },
      createEditor(editor){
        editor.config.height = 80;
        editor.config.focus = false;
        editor.config.showLinkImgAlt = false;
        editor.config.showLinkImgHref = false;
        editor.config.showFullScreen = false;  //配置全屏
        editor.config.showMenuTooltips = true; //隐藏菜单栏提示
        editor.config.showLinkImg = false;  //隐藏插入网络图片
        editor.config.showLinkVideo = false;  //隐藏插入网络视频
        editor.config.zIndex = 1; //降低editor权值,不遮挡其他元素
        editor.config.menus = [
          'emoticon',
        ];
        editor.config.emotions = emoji;
      },
      showcomment(dynamicId){
        const _this = this;
        //编辑器的控制
        if(this.showCommentController === null){
          this.showCommentController = dynamicId;
          if(this.editor === null){
            this.createEditor(dynamicId);
            this.editor.create();
          }else{
            this.editor.destroy();
            this.editor = null;
            this.createEditor(dynamicId);
            this.editor.create();
          }
        }else if(this.showCommentController !== dynamicId){
          this.showCommentController = dynamicId;
          if(this.editor === null){
            this.createEditor(dynamicId);
            this.editor.create();
          }else{
            this.editor.destroy();
            this.editor = null;
            this.createEditor(dynamicId);
            this.editor.create();
          }
        }else{
          this.showCommentController = null;
          this.showNumReply = true;
          if(this.editor === null){
            this.createEditor(dynamicId);
            this.editor.create();
          }else{
            this.editor.destroy();
            this.editor = null;
            this.createEditor(dynamicId);
            this.editor.create();
          }
        }
        axios.get('http://localhost:8181/dynamic/findDynamicComment/'+dynamicId).then(function (resp) {
          // console.log(resp);
          if(resp.data.msg === '查询回复成功'){
            for(let item in resp.data.comment){
              resp.data.comment[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.comment[item].headPosition;
            }
            _this.commentContent = resp.data.comment;
          }else if(resp.data.msg === '查询回复异常'){
            _this.$message.warning("查询回复发送异常");
          }
        })
        axios.get('http://localhost:8181/dynamic/findDynamicReply/'+dynamicId).then(function (resp) {
          // console.log(resp.data.reply);
          if(resp.data.msg === '查询回复成功'){
            for(let item in resp.data.reply){
              resp.data.reply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.reply[item].headPosition;
            }
            _this.replyContent = resp.data.reply;
            _this.CommentLoading = false;
          }else{
            _this.$message.warning("查询回复发送异常");
          }
        })
      },
      createReplyEditor(Id,flag){
        //初始化一个editor
        //flag==0代表对评论的回复   flag==1代表对回复的回复
        var ele = null;
        if(flag === 0){
          ele = document.getElementById('reply'+Id);
          this.replyController = Id;
        }else if(flag === 1){
          ele = document.getElementById('replyToReply'+Id);
          this.replyToReplyController = Id;
        }
        this.replyEditor = new E(ele);
        // this.replyEditor = new E('#test');
        this.replyEditor.config.height = 80;
        this.replyEditor.config.focus = false;
        this.replyEditor.config.uploadImgHeaders = {
          token: localStorage.getItem('Authorization') // 设置请求头
        }
        this.replyEditor.config.showLinkImgAlt = false;
        this.replyEditor.config.showLinkImgHref = false;
        this.replyEditor.config.showFullScreen = false;  //配置全屏
        this.replyEditor.config.showMenuTooltips = true; //隐藏菜单栏提示
        this.replyEditor.config.showLinkImg = false;  //隐藏插入网络图片
        this.replyEditor.config.showLinkVideo = false;  //隐藏插入网络视频
        this.replyEditor.config.zIndex = 1; //降低editor权值,不遮挡其他元素
        this.replyEditor.config.menus = [
          'emoticon',
        ];
        this.replyEditor.config.emotions = emoji;
      },
      reply(Id,flag){
        //flag==0代表对评论的回复   flag==1代表对回复的回复
        if(this.replyEditorer == null){
          //第一次点击回复时 即没有创建回复编辑器的时候
          this.createReplyEditor(Id,flag);
          this.replyEditor.create();
          this.replyEditorer = Id;
          // this.replyController = Id;
          this.replyFlag = flag;
        }else if(this.replyEditorer !== Id || (this.replyEditorer === Id && this.replyFlag !== flag)){
          if(flag === 0){
            this.replyToReplyController = null;
          }else {
            this.replyController = null;
          }
          //点击的回复未创建编辑器  但编辑器再其它地方以及创建
          this.replyEditor.destroy();
          this.replyEditor = null;
          this.createReplyEditor(Id,flag);
          this.replyEditor.create();
          this.replyEditorer = Id;
          // this.replyController = Id;
          this.replyFlag = flag;
        }else{
          //当用户点击了回复按钮后显示了编辑框 用户再次点击同一个回复按钮时的操作
          if(flag === this.replyFlag){
            if(flag === 0){
              this.replyController = null;
            }else {
              this.replyToReplyController = null;
            }
            this.replyEditor.destroy();
            this.replyEditor = null;
            this.replyEditorer = null;
          }else{
            this.replyEditor.destroy();
            this.replyEditor = null;
            this.createReplyEditor(Id,flag);
            this.replyEditor.create();
            this.replyEditorer = Id;
            // this.replyController = Id;
            this.replyFlag = flag;
          }
        }

      },
      likes(dynamicId,islike){
        let dianzan = document.getElementById('dianzan');
        const _this = this;
        if(islike === 1){
          this.$message.warning("已点过赞");
        }else{
          if(dianzan.style.color === 'rgba(0, 0, 0, 0.5)'){
            //点赞
            console.log("点赞")
            axios.post('http://localhost:8181/dynamiclikes/likes/'+this.$store.state.UserId+'/'+dynamicId).then(function (resp) {
              if(resp.data.msg === '点赞成功'){
                _this.$message.success("点赞成功");
                axios.get('http://localhost:8181/dynamiclikes/getLikes/'+dynamicId).then(function (resp) {
                  _this.dynamicContent.likesNums = resp.data;
                })
              }
            })
            dianzan.style.color = '#409EFF';
          }else if(dianzan.style.color === 'rgb(64, 158, 255)'){
            //取消点赞
            axios.post('http://localhost:8181/dynamiclikes/dislikes/'+this.$store.state.UserId+'/'+dynamicId).then(function (resp) {
              if(resp.data.msg === '取消点赞成功'){
                axios.get('http://localhost:8181/dynamiclikes/getLikes/'+dynamicId).then(function (resp) {
                  for(let item in _this.dynamicContent){
                    if(_this.dynamicContent[item].dynamicId === dynamicId){
                      _this.dynamicContent[item].likesNums = resp.data;
                    }
                  }
                })
              }
            })
            dianzan.style.color = 'rgba(0,0,0,.5)';
          }
        }
      },
    },
    mounted() {
      //评论
      this.commentEditor = new E('#commentEditor');
      // this.createEditor(editor1);
      this.createEditor(this.commentEditor);
      this.$nextTick(function () {
        setTimeout(()=>{
          this.commentEditor.create();
        },100)
      });
    },
    updated:function(){
      //用户评论后 动态更新页面
    },
    created() {
      this.dynamicId = this.$route.params.dynamicId;
      const _this = this;
      //查找单个动态中的内容
      axios.get('http://localhost:8181/dynamic/findDynamic/'+this.$route.params.dynamicId).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询动态成功'){
          resp.data.dynamic.headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic.headPosition;
          if(resp.data.dynamic.submit_images!==null){
            resp.data.dynamic.submit_images = resp.data.dynamic.submit_images.split(",");
            resp.data.dynamic.submit_images.pop();  //删除最后一个空的元素
            for(let item in resp.data.dynamic.submit_images){
              resp.data.dynamic.submit_images[item] = 'http://localhost:8181/dynamic/'+resp.data.dynamic.submit_images[item];
            }
          }
          _this.dynamicContent = resp.data.dynamic;
        }else{
          _this.$message.error("查询动态信息失败");
        }
      })
      //拿取动态中的前6条评论
      axios.get('http://localhost:8181/dynamic/findDynamicComment/'+this.$route.params.dynamicId).then(function (resp) {
        if(resp.data.msg === '查询回复成功'){
          _this.CommentLoading = false;
          for(let item in resp.data.comment){
            resp.data.comment[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.comment[item].headPosition;
          }
          _this.commentContent = resp.data.comment;
          if(resp.data.comment.length < 6){
            _this.loadCommentDisabled = false;
          }
        }else if(resp.data.msg === '查询回复异常'){
          _this.$message.warning("查询回复发送异常");
        }
      })
      //拿取前6条评论，每一条评论的前3条回复
      axios.get('http://localhost:8181/dynamic/findDynamicReply/'+this.$route.params.dynamicId).then(function (resp) {
          if(resp.data.msg === '查询回复成功'){
            for(let item in resp.data.reply){
              resp.data.reply[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.reply[item].headPosition;
            }
            _this.replyContent = resp.data.reply;
            _this.CommentLoading = false;
          }else{
            _this.$message.warning("查询回复发送异常");
          }
        })
    }
  }
</script>

<style scoped>
  .left_main_content{
    padding-top: 1%;
    /*background-color:  #ffffff;*/
    width: 45%;
    position: relative;
    margin-left: 25%;
    /*position: center;*/
    margin-right: 0;
    float: left;
  }
  .left_main_content_child{
    /*border: 1px red solid;*/
    /*background-color:#ffffff ;*/
    margin-bottom: 15px;
    border-radius: 3%;

  }
  .left_main_content_child_one{
    margin-top: 1%;
    height: 200px;
    background-color: #ffffff;
  }
  .iconfont{
    /*color: #409EFF;*/
  }
  .iconfont:hover{
    color: #409EFF;
  }
  /*虚线  */
  .el-divider--horizontal{
    margin:0 0;
    padding: 0;
    border-top: 1px dashed #e1e3e5;
    background: 0 0;
  }
  .left_main_content_child_one_dpz{
    font-size: 14px;
    cursor: pointer;
    color:rgba(0,0,0,.5);
  }
  .left_main_content_child_one_dpz:hover{
    color: #409EFF;
  }
  .reply_click{
    font-size:12px;
    color:#409EFF;
    cursor: pointer;
  }
  .replyclass >>> p{
    margin-top: 1%;
    margin-bottom: 1%;
    font-size: 15px;
  }
  .replyclass >>> img{
    vertical-align: sub
  }
  .reply:hover{
    cursor: pointer;
    filter: alpha(Opacity=80);
    opacity: 0.8;
  }
  .username:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>