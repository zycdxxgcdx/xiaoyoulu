<template>
  <div style="padding-top: 1%">
    <div class="left_main_content">
      <div class="left_main_content_child">
        <!--文本编辑器 wangEditor-->
        <div style="background-color: #ffffff;">
          <div id="div1" style="padding: 3%;">
          </div>
          <div style="padding-left: 3%;">
            <el-upload
                    action="http://localhost:8181/dynamic/selectJpgUrl"
                    list-type="picture-card"
                    :headers="token"
                    :on-remove="handleRemove"
                    :on-exceed="outUploadLenth"
                    name="images"
                    :file-list="fileList"
                    :limit="maxLoadPhoto"
                    v-show="this.fileList.length > 0"
                    :on-success="handlephotoSuccess">
              <i class="el-icon-plus"></i>
            </el-upload>
          </div>
          <el-button @click="submit" type="primary" style="float: right;margin-right: 3%;margin-bottom: 2%;">发布</el-button>
          <div style="clear: both"></div>
        </div>
<!--        动态内容-->
        <ul class="infinite-list" v-infinite-scroll="load"  :infinite-scroll-disabled="disabled" infinite-scroll-delay="1000" style="overflow:auto;padding-left: 0;">
          <li v-for="item in dynamicContent" class="infinite-list-item" style="list-style: none;">
            <div class="left_main_content_child_one" style="margin-bottom: 1%;height: auto;padding-bottom: 2%">
            <!--头像-->
            <div class="username" :id="'dynamic'+item.dynamicId" style="margin-left:2%;padding-top: 3%;float: left;">
              <router-link target="_blank"  :to="'/u/'+item.userId" style="text-decoration:none;outline: none;color: #000;width: 50%">
              <el-avatar :src="item.headPosition" @error="errorHandler" :size="45">
              </el-avatar>
              </router-link>
            </div>
            <!--用户名-->
            <div style="margin:0 0 0 12%;padding-top:1px;width: 50%;">
              <router-link target="_blank"  :to="'/u/'+item.userId" style="text-decoration:none;outline: none;color: #000;width: 50%">
              <h4 style="margin-bottom: 0" class="username">{{item.username}}</h4>
              </router-link>
              <span style="font-size: 8px;color:rgba(0,0,0,.5);">{{item.dynamic_time}}</span>
            </div>
            <!--内容-->
            <div style="margin:2% 0 0 12%;font-size: 16px">
              <dl v-html="item.content" v-lazy="item.content" class="replyclass" style="margin: 0 2% 0 0">
                {{item.content}}
              </dl>
              <div style="margin-top: 2%;width: 70%">
                <el-image v-for="item1 in item.submit_images"  :src="item1" alt="加载失败" fit="fit" style="width: 120px; height: 120px;margin-right: 1%;"
                          :preview-src-list="item.submit_images"></el-image>
              </div>
            </div>
<!--              转发内容-->
<!--              background-color: #f3f9f6;-->
              <div v-if="item.forwardDynamic !== null" style="margin:2% 2% 2% 12%;padding-bottom: 1%;background-color: #F2F6FC;font-size: 16px;box-shadow: 0 2px 4px rgba(0, 0, 0, .12)">
                <router-link target="_blank" :to="{path:'/dynamic/'+item.forwardDynamic.dynamicId}" style="text-decoration:none;outline: none;color: #000;">
                  <div class="username" style="margin-left:2%;padding-top: 1%;float: left;">
                    <router-link target="_blank"  :to="'/u/'+item.forwardDynamic.userId" style="text-decoration:none;outline: none;color: #000;width: 50%">
                    <el-avatar :src="item.forwardDynamic.headPosition" @error="errorHandler" :size="40">
                    </el-avatar>
                    </router-link>
                  </div>
                  <!--用户名-->
                  <div style="margin:0 0 0 12%;padding-top: 1%;width: 20%;">
                    <router-link target="_blank"  :to="'/u/'+item.forwardDynamic.userId" style="text-decoration:none;outline: none;color: #000;width: 50%">
                    <h4 style="margin: 0;" class="username">{{item.forwardDynamic.username}}</h4>
                    </router-link>
                    <span style="font-size: 8px;color:rgba(0,0,0,.5);">{{item.forwardDynamic.dynamic_time}}</span>
                  </div>
                  <div style="margin:2% 0 0 12%;">
                    <dl v-html="item.forwardDynamic.content" v-lazy="item.forwardDynamic.content" class="replyclass" style="margin: 0 2% 0 0;">
                      {{item.forwardDynamic.content}}
                    </dl>
                    <div style="margin-top: 2%;width: 70%">
                      <el-image v-for="item1 in item.forwardDynamic.submit_images"  :src="item1" alt="加载失败" fit="fit" style="width: 100px; height: 100px;margin-right: 1%;"></el-image>
                    </div>
                  </div>
                </router-link>
              </div>

            <el-divider></el-divider>
            <!--点赞，评论，转发-->
            <div>
              <ul style="text-decoration: none;margin: 0;padding: 0;">
                <li style="display: inline-block;margin:0 10% 0 14%;width: 7%">
                  <div class="left_main_content_child_one_dpz">
<!--                    v-preventReClick="1000"-->
                    <i :id="'like'+item.dynamicId" class="iconfont icondianzan1" @click="likes(item.dynamicId,item.islike)" style="font-size:14px;color:rgba(0,0,0,.5);pointer-events: auto"> {{item.likesNums}}</i>
                  </div>
                </li>
                <el-divider direction="vertical"></el-divider>
                <li style="display: inline-block;margin:0 10% 0 14%;width: 7%">
                  <div class="left_main_content_child_one_dpz"><i class="el-icon-chat-round" @click="showcomment(item.dynamicId)" style="font-size:14px"> {{item.replyNums}}</i></div>
                </li>
                <el-divider direction="vertical"></el-divider>
                <li style="display: inline-block;margin:0 12%;width: 7%">
                  <!-- 转发前要有个空格-->
                  <div class="left_main_content_child_one_dpz"><i class="el-icon-share" @click="showforward(item.dynamicId)" style="font-size:14px"> {{item.forwardNums}}</i></div>
                </li>
              </ul>
            </div>
            <el-divider></el-divider>

<!--              评论以及回复内容-->
            <div style="height: auto" v-if="item.dynamicId === showCommentController">
                <div style="margin-left:2%;padding-top: 2%;">
    <!--              编辑器-->
                  <div :id="'div'+item.dynamicId" style="padding-left: 10%;width:85%;"></div>
                  <el-button type="primary" style="float: right;margin: 2% 5% 2% 0;" @click="commentDynamic(item.dynamicId)">评论</el-button>
                  <div style="clear:both"></div>
                  <el-divider></el-divider>
                </div>
                <div v-if="CommentLoading" style="text-align: center;">loading...</div>
                <div :id="item1.commentId" v-for="item1 in commentContent">
                  <div @click="toProfile(item.userId)" >
                    <el-avatar :src="item1.headPosition" @error="errorHandler" class="username"  :size="45" style="float: left;margin-left: 10%;margin-top: 1%;"></el-avatar>
                  </div>

                  <div style="margin-left: 20%;">
                      <span style="font-size:14px;" @click="toProfile(item1.userId)" class="username">{{item1.username}}</span>
                      <div style="font-size:16px;">
                        <dl v-html="item1.content" v-lazy="item1.content" class="replyclass" style="margin:2% 1% 2% 0">
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
                  <el-button type="primary" style="float: right;margin: 1% 1% 0 0 " size="small" v-show="item1.commentId === replyController" @click="doReply(item.dynamicId,item1.commentId,0)">回复</el-button>
                  <div style="clear: both;"></div>

<!--                  回复内容-->
                  <div style="margin-left: 20%;margin-bottom: 1%;" v-for="item2 in replyContent" v-if="item2.commentId === item1.commentId">
                    <div @click="toProfile(item1.userId)">
                      <el-avatar :src="item2.headPosition" @error="errorHandler" class="username" :size="40" style="float: left;margin-right: 2%;margin-top: 1%;"></el-avatar>
                    </div>
<!--                    <p style="font-size:14px">{{item1.username}}</p>-->
                    <div v-show="item2.isat == 1">
                      <span style="font-size: 13px;" @click="toProfile(item2.fromuserId)" class="username">{{item2.fromusername}}</span>
                      <span style="font-size: 13px;color: #5cb87a; "> @</span>
                      <span style="font-size: 13px;" @click="toProfile(item2.touserId)" class="username">{{item2.touusername}}</span>
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
                    <el-button type="primary" style="float: right;margin: 1% 1% 0 0 " size="small" v-show="item2.dynamicReplyId === replyToReplyController" @click="doReply(item.dynamicId,item1.commentId,1,item2.fromuserId)">回复</el-button>
                    <div style="clear: both;"></div>
                  </div>
                  <div v-show="item1.nums>3?true:false" style="margin-left: 20%;margin-bottom: 1%;">
                    <span style="font-size:12px" v-show="showNumReply || item1.commentId !== replyCommentId">共有{{item1.nums}}条回复，</span>
                    <span class="reply_click" @click="showMoreReply(item.dynamicId,item1.commentId)" v-show="showNumReply || item1.commentId !== replyCommentId">点击查看</span>
                    <div v-show="!showNumReply && item1.commentId === replyCommentId">
                    <el-pagination
                            hide-on-single-page
                            layout="prev, pager, next"
                            :total="item1.nums"
                            :page-size="replypagesize"
                            :current-page="replyPages"
                            @current-change="replyPage($event,item.dynamicId,item1.commentId)">
                    </el-pagination>
                    </div>
                  </div>
<!--                  <div :id="'reply'+item1.commentId" style="margin-left: 20%;margin-top: 1%;margin-right: 1%;"></div>-->
                  <div>
                    <el-divider></el-divider>
                  </div>
                </div>
                <router-link target="_blank" :to="{path:'/dynamic/'+item.dynamicId}" style="text-decoration:none;outline: none;color: #000;">
                  <p class="showMoreReply" v-if="item.replyNums > 6">查看更多></p>
                </router-link>
              </div>
            <div style="height: auto" v-if="item.dynamicId === showForwardController">
              <div style="margin-left:2%;padding-top: 2%;">
                <!--              编辑器-->
                <div :id="'div'+item.dynamicId" style="padding-left: 10%;width:85%;"></div>
                <el-button type="primary" style="float: right;margin: 2% 5% 2% 0;" @click="forwardDynamic(item.dynamicId)">转发</el-button>
                <div style="clear:both"></div>
                <el-divider></el-divider>
              </div>

              <div style="margin-left:12%;padding-top: 2%;" v-for="item in forwardContent">
                <div style="margin-top: 1%;">
                  <router-link target="_blank"  :to="'/u/'+item.userId" style="text-decoration:none;outline: none;color: #000;width: 50%">
                  <el-avatar :src="item.headPosition" @error="errorHandler" class="username"  :size="45" style="float: left;"></el-avatar>
                  </router-link>
                </div>
                <div style="margin:0 0 0 2%;float: left;width: 80%">
                  <router-link target="_blank"  :to="'/u/'+item.userId" style="text-decoration:none;outline: none;color: #000;width: 50%">
                    <span style="font-size: 14px" class="username">{{item.username}}</span>
                  </router-link>
                    <!--                    <span style="font-size:14px;" @click="toProfile(item1.userId)" class="username">{{item1.username}}</span>-->
                    <div style="font-size:16px;margin:0">
                        <dl v-html="item.content" v-lazy="item.content" class="replyclass" style="margin:2% 1% 2% 0">
                          {{item.content}}
                        </dl>
                    </div>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);">{{item.timeDifference}}</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-if="item.timeDifferenceType === 'day'">天前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item.timeDifferenceType === 'hour'">小时前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item.timeDifferenceType === 'minute'">分钟前</span>
                    <span style="font-size: 10px;color:rgba(0,0,0,.5);" v-else-if="item.timeDifferenceType === 'second'">秒前</span>
                </div>
                <div style="clear: both;"></div>
                <el-divider></el-divider>
              </div>
              <router-link target="_blank" :to="{path:'/dynamic/'+item.dynamicId}" style="text-decoration:none;outline: none;color: #000;">
                <p class="showMoreReply" v-if="item.forwardNums > 6">查看更多></p>
              </router-link>
            </div>
            </div>
          </li>
        </ul>
      </div>
      <p v-if="loading" style="text-align: center">加载中...</p>
      <p v-if="noMore" style="text-align: center;">没有更多了...</p>
    </div>
  </div>
</template>

<script>
  import E from 'wangeditor';
  import emoji from '../../static/emoji.json'
  //发动态的编辑器
  const editor1 = new E('#div1');
  export default {
    data() {
      return {
        showNumReply:true,
        //记录当前查看回复的评论ID 来控制回复中分页按钮的展示
        replyCommentId: 0,
        //当用户在第二页回复时，记录当前用户的回复的页码，这样就能加载第二页的数据
        replyPages:1,
        // replyNums:120,
        replypagesize:6,
        loading:false,
        noMore:false,
        disabled:false,
        comment_num:2,
        //拿取评论时的loading
        CommentLoading:true,
        //评论的内容
        commentContent:[],
        //回复的内容
        replyContent:[],
        //回复的当前页
        nowReplyPage:1,
        //转发内容
        forwardContent:[],
        showCommentController:null,
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
          submitList:'',
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
        //评论时的编辑器
        editor: null,
        //回复时的编辑器
        replyEditor:null,
        //回复编辑器的使用者
        replyEditorer:null,
        //控制回复按钮的show
        replyController:null,
        //控制回复评论中的回复的按钮显示
        replyToReplyController:null,
        //flag==0代表对评论的回复   flag==1代表对回复的回复
        replyFlag:-1,
        dialogImageUrl1:[ 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
          'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg', 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg'],
        dynamicContent:null,
        dialogImageUrl: '',
        dialogVisible: false,
        activeIndex: '1',
        num: 1,
        input: '',
        messages:1,
        values:1,
        flag:0,
        circleUrl: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
        form:{
          desc:''
        }
      }
    },
    methods: {
      toProfile(userId){
        window.open('/u/'+userId, '_blank');
      },
      replyPage(val,dynamicId,commentId) {
        //评论分页
        const _this = this;
        this.replyEditorer = null;
        this.replyController = null;
        this.replyToReplyController = null;
        if(this.replyEditor!==null){
          this.replyEditor.destroy();
          this.replyEditor = null;
        }
        document.getElementById(commentId).scrollIntoView();
        this.replyPages = val;
        axios.get('http://localhost:8181/dynamic/findDynamicReply/'+dynamicId+'/'+commentId+'/'+this.replyPages+'/'+this.replypagesize).then(function (resp) {
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
      load(){
        this.loading = true;
        const _this = this;
        setTimeout(() => {
          axios.get('http://localhost:8181/dynamic/findDynamic/'+this.$store.state.UserId+'/'+this.comment_num+'/10').then(function (resp) {
            console.log(resp)
            for(let item in resp.data.dynamic){
              resp.data.dynamic[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].headPosition;
              if(resp.data.dynamic[item].submit_images!==null){
                resp.data.dynamic[item].submit_images = resp.data.dynamic[item].submit_images.split(",");
                resp.data.dynamic[item].submit_images.pop();  //删除最后一个空的元素
                for(let item1 in resp.data.dynamic[item].submit_images){
                  resp.data.dynamic[item].submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].submit_images[item1];
                }
              }
              if(resp.data.dynamic[item].forwardDynamic !== null){
                resp.data.dynamic[item].forwardDynamic.headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].forwardDynamic.headPosition;
                if(resp.data.dynamic[item].forwardDynamic.submit_images !== null){
                  resp.data.dynamic[item].forwardDynamic.submit_images = resp.data.dynamic[item].forwardDynamic.submit_images.split(",");
                  resp.data.dynamic[item].forwardDynamic.submit_images.pop();  //删除最后一个空的元素
                  for(let item1 in resp.data.dynamic[item].forwardDynamic.submit_images){
                    resp.data.dynamic[item].forwardDynamic.submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].forwardDynamic.submit_images[item1];
                  }
                }
              }
            }
            _this.dynamicContent = _this.dynamicContent.concat(resp.data.dynamic);
            _this.comment_num++;
            if(resp.data.dynamic.length < 10){
              _this.noMore = true;
              _this.disabled = true;
            }
          })
          this.loading = false
        }, 2000)
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

        this.replyEditor= null;
        this.replyEditor = new E(ele);
        console.log(this.replyEditor);
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
      createEditor(dynamicId){
        //初始化一个editor
        var ele = document.getElementById('div'+dynamicId);
        this.editor = new E('#div'+dynamicId);
        this.editor.config.height = 80;
        this.editor.config.focus = false;
        this.editor.config.showLinkImgAlt = false;
        this.editor.config.uploadImgHeaders = {
          token: localStorage.getItem('Authorization') // 设置请求头
        }
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
      showcomment(dynamicId){
        document.getElementById('dynamic'+dynamicId).scrollIntoView();
        this.showForwardController = null;
        const _this = this;
        //编辑器的控制
        if(this.showCommentController === null){
          //点击一个评论按钮 加载评论
          this.showCommentController = dynamicId;
          if(this.editor === null){
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });

          }else{
            this.editor.destroy();
            this.editor = null;
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
          }
        }else if(this.showCommentController !== dynamicId){
          //如果在点击了前一个评论按钮但没有关闭 又点击了宁外一个评论按钮
          this.showCommentController = dynamicId;
          if(this.editor === null){
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
            // this.replyEditor = null;
          }else{
            this.editor.destroy();
            this.editor = null;
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
            // this.replyEditor = null;
          }
        }else{
          //再次点击同一个评论按钮 收回评论界面
          this.showCommentController = null;
          this.replyController = null;
          this.replyToReplyController = null;
          this.showNumReply = true;
          // this.replyEditor = null;
          // if(this.editor === null){
          //   this.$nextTick(function () {
          //     this.createEditor(dynamicId);
          //     this.editor.create();
          //   });
          // }else{
          //   this.editor.destroy();
          //   this.editor = null;
          //   this.$nextTick(function () {
          //     this.createEditor(dynamicId);
          //     this.editor.create();
          //   });
          // }
        }
        // console.log('http://localhost:8181/dynamic/findDynamicReply/'+dynamicId+'/1/10');
        axios.get('http://localhost:8181/dynamic/findDynamicComment/'+dynamicId).then(function (resp) {
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
          console.log(resp);
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
      showforward(dynamicId){
        this.replyToReplyController = null;
        this.replyController = null;
        this.showCommentController = null;
        if(this.showForwardController === null){
          //点击一个评论按钮 加载评论
          this.showForwardController = dynamicId;
          if(this.editor === null){
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
          }else{
            this.editor.destroy();
            this.editor = null;
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
          }
        }else if(this.showForwardController !== dynamicId){
          //如果在点击了前一个评论按钮但没有关闭 又点击了宁外一个评论按钮
          this.showForwardController = dynamicId;
          if(this.editor === null){
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
            // this.replyEditor = null;
          }else{
            this.editor.destroy();
            this.editor = null;
            this.$nextTick(function () {
              this.createEditor(dynamicId);
              this.editor.create();
            });
            // this.replyEditor = null;
          }
        }else{
          //再次点击同一个评论按钮 收回评论界面
          this.showForwardController = null;
          this.showNumReply = true;
        }
        const _this = this;
        axios.get('http://localhost:8181/dynamic/findDynamicForward/'+dynamicId+'/1/6').then(function (resp) {
          console.log(resp);
          if(resp.data.msg ==='查询转发成功'){
            for(let item in resp.data.forwardInfo){
              resp.data.forwardInfo[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.forwardInfo[item].headPosition;
            }
            _this.forwardContent = resp.data.forwardInfo;
          }
        })
      },
      commentDynamic(dynamicId){
        const _this = this;
        // document.getElementById(dynamicId).scrollIntoView();
        if(this.$store.state.Authorization) {
          // console.log("========================");
          if(this.editor.txt.html() && this.check_input(this.editor.txt.text())) {
            // console.log(dynamicId);
            // console.log(this.editor.txt.html());
            this.submitCommentInfo.dynamicId = dynamicId;
            this.submitCommentInfo.content = this.editor.txt.html();
            // console.log(this.submitCommentInfo);
            axios.post('http://localhost:8181/dynamic/postDynamicComment',this.submitCommentInfo).then(function (resp) {
              // console.log(resp);
              if(resp.data.msg === '评论发送成功'){
                _this.$message.success('评论成功');
                _this.editor.txt.clear();
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
          if(this.editor.txt.html() && this.check_input(this.editor.txt.text())) {
            console.log(dynamicId);
            console.log(this.editor.txt.html());
            this.submitForwardInfo.forwardDynamicId = dynamicId;
            this.submitForwardInfo.content = this.editor.txt.html();
            // console.log(this.submitCommentInfo);
            axios.post('http://localhost:8181/dynamic/postDynamicForward',this.submitForwardInfo).then(function (resp) {
              console.log(resp);
              if(resp.data.msg ==='转发成功'){
                _this.editor.txt.clear();
                _this.$message.success("转发成功");
                axios.get('http://localhost:8181/dynamic/findDynamicForward/'+dynamicId+'/1/6').then(function (resp) {
                  console.log(resp);
                  if(resp.data.msg ==='查询转发成功'){
                    for(let item in resp.data.forwardInfo){
                      resp.data.forwardInfo[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.forwardInfo[item].headPosition;
                    }
                    _this.forwardContent = resp.data.forwardInfo;
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
      submit(){
        const _this = this;
        this.submitInfo.content = editor1.txt.html();
        for(let item in this.fileList){
          var name_jpg = this.fileList[item].url.split('/');
          this.submitInfo.submitList = this.submitInfo.submitList + name_jpg[name_jpg.length-1]+',';
        }
        if(this.$store.state.Authorization) {
          if(editor1.txt.html() && this.check_input(editor1.txt.text())) {
            axios.post('http://localhost:8181/dynamic/postDynamic',this.submitInfo).then(function (resp) {
              // console.log(resp);
              if(resp.data.msg === '发送动态成功'){
                editor1.txt.clear();
                _this.fileList = [];
                _this.$message.success("发布动态成功");
                axios.get('http://localhost:8181/dynamic/findDynamic/'+_this.$store.state.UserId+'/1/10').then(function (resp) {
                  if(resp.data.msg === '查询成功'){
                    for(let item in resp.data.dynamic){
                      resp.data.dynamic[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].headPosition;
                      if(resp.data.dynamic[item].submit_images !== null){
                        resp.data.dynamic[item].submit_images = resp.data.dynamic[item].submit_images.split(",");
                        resp.data.dynamic[item].submit_images.pop();  //删除最后一个空的元素
                        for(let item1 in resp.data.dynamic[item].submit_images){
                          resp.data.dynamic[item].submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].submit_images[item1];
                        }
                      }
                      if(resp.data.dynamic[item].forwardDynamic !== null){
                        resp.data.dynamic[item].forwardDynamic.headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].forwardDynamic.headPosition;
                        if(resp.data.dynamic[item].forwardDynamic.submit_images !== null){
                          resp.data.dynamic[item].forwardDynamic.submit_images = resp.data.dynamic[item].forwardDynamic.submit_images.split(",");
                          resp.data.dynamic[item].forwardDynamic.submit_images.pop();  //删除最后一个空的元素
                          for(let item1 in resp.data.dynamic[item].forwardDynamic.submit_images){
                            resp.data.dynamic[item].forwardDynamic.submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].forwardDynamic.submit_images[item1];
                          }
                        }
                      }
                    }
                    // console.log(resp.data.dynamic);
                    _this.dynamicContent = resp.data.dynamic;
                  }else{
                    _this.$message.error("查询动态信息失败");
                  }
                })
              }else if(resp.data.msg === '动态内容含有屏蔽词'){
                _this.$message.warning("动态内容含有系统屏蔽词，请修改");
              }else {
                _this.$message.error("动态发布失败");
              }
            })
          }else{
            this.$message.error('内容不能为空');
          }
        }else{
          this.$router.push(this.$route.path+'/login');
        }

      },
      handleRemove(file) {
        //用户删除上传的图片后出发的操作 使图片数组元素-1
        this.fileList.splice(this.fileList.findIndex(item=>item.uid==file.uid),1);
      },
      likes(dynamicId,islike){
        let dianzan = document.getElementById('like'+dynamicId);
        console.log("++++++++++++++++++++++++++++")
        console.log(dianzan.style.color)
        console.log(dianzan.style.color === 'rgb(64, 158, 255)')
        console.log(dianzan.style.color === 'rgba(0, 0, 0, 0.5)')
        console.log("++++++++++++++++++++++++++++")
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
                    for(let item in _this.dynamicContent){
                      if(_this.dynamicContent[item].dynamicId === dynamicId){
                        _this.dynamicContent[item].likesNums = resp.data;
                      }
                    }
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
      const _this = this;
      editor1.config.height = 120;
      editor1.config.focus = false;
      editor1.config.uploadImgServer = 'http://localhost:8181/dynamic/selectJpgUrl'; //图片接口地址
      editor1.config.uploadImgHeaders = {
        token: localStorage.getItem('Authorization') // 设置请求头
      }
      editor1.config.uploadFileName = 'images';
      editor1.config.uploadImgMaxLength = 9;
      editor1.config.showLinkImgAlt = false;
      editor1.config.showLinkImgHref = false;
      editor1.config.showFullScreen = false;  //配置全屏
      editor1.config.showMenuTooltips = true; //隐藏菜单栏提示
      editor1.config.showLinkImg = false;  //隐藏插入网络图片
      editor1.config.showLinkVideo = false;  //隐藏插入网络视频
      editor1.config.menuTooltipPosition = false;
      editor1.config.zIndex = 0;
      editor1.config.emotions = emoji;
      editor1.config.menus = [
        'emoticon',
        'image',
      ];
      editor1.config.uploadImgHooks = {
        before: function (xhr, editor, files) {
          // 图片上传之前触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件
          if(_this.fileList.length === 9) {
            return {
              prevent: true,
              msg: '图片最多上传9张'
            };
          }
        },
        success: function (xhr, editor, result) {
          alert("成功");
        },
        fail: function (xhr, editor, result) {

          alert("失败");
        },
        error: function (xhr, editor) {
        },
        customInsert: function (insertImg, result, editor) {
          var url = result.url;
          _this.fileList.push(result);

        }
      }
      editor1.create();
    },
    // updated:function(){
    //   //用户评论后 动态更新页面
    // },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/dynamic/findDynamic/'+this.$store.state.UserId+'/1/10').then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.dynamic){
            resp.data.dynamic[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].headPosition;
            if(resp.data.dynamic[item].submit_images !== null){
              resp.data.dynamic[item].submit_images = resp.data.dynamic[item].submit_images.split(",");
              resp.data.dynamic[item].submit_images.pop();  //删除最后一个空的元素
              for(let item1 in resp.data.dynamic[item].submit_images){
                resp.data.dynamic[item].submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].submit_images[item1];
              }
            }
            if(resp.data.dynamic[item].forwardDynamic !== null){
              resp.data.dynamic[item].forwardDynamic.headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].forwardDynamic.headPosition;
              if(resp.data.dynamic[item].forwardDynamic.submit_images !== null){
                resp.data.dynamic[item].forwardDynamic.submit_images = resp.data.dynamic[item].forwardDynamic.submit_images.split(",");
                resp.data.dynamic[item].forwardDynamic.submit_images.pop();  //删除最后一个空的元素
                for(let item1 in resp.data.dynamic[item].forwardDynamic.submit_images){
                  resp.data.dynamic[item].forwardDynamic.submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].forwardDynamic.submit_images[item1];
                }
              }
            }
          }
          // console.log(resp.data.dynamic);
          _this.dynamicContent = resp.data.dynamic;
        }else{
          _this.$message.error("查询动态信息失败");
        }
      })
    }
  }
</script>

<style scoped>
  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 45%;
    position: relative;
    margin-left: 20%;
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
  .right_main_content{
    margin-right: 16%;
    position: relative;
    width: 18%;
    float: right;
    /*border: 1px red solid;*/
  }
  .right_main_content_child_one{
    background-color: #ffffff;
    /*margin-top: 6%;*/
    /*padding: 3% 0 1% 3%;*/
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

  /*调整照片墙的大小*/
  /deep/.el-upload--picture-card {
    width: 150px;
    height: 100px;
    line-height: 100px;
  }
  /deep/ .el-upload{
    width: 100px;
    height: 100px;
    line-height: 100px;
  }
  /deep/ .el-upload-list--picture-card .el-upload-list__item{
    width: 100px;
    height: 100px;
    line-height: 100px;
  }
  .showMoreReply{
    text-align: center;
    font-size: 14px;
    color: #406599;
  }
  .showMoreReply:hover{
    filter: alpha(Opacity=60);
    opacity: 0.6;
  }
  .reply:hover{
    cursor: pointer;
    filter: alpha(Opacity=80);
    opacity: 0.8;
  }
  .replyclass >>> p{
    margin-top: 1%;
    margin-bottom: 1%;
    font-size: 15px;
  }
  .replyclass >>> img{
    vertical-align: sub
  }
  .reply_click{
    font-size:12px;
    color:#409EFF;
    cursor: pointer;
  }
  .username:hover{
    cursor: pointer;
    color: #409EFF;
  }
</style>
