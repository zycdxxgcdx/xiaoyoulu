<template>
  <div>
    <ul  v-loading="initLoading"
         element-loading-text="拼命加载中"
         element-loading-spinner="el-icon-loading" class="infinite-list" v-infinite-scroll="load"  infinite-scroll-delay="1000" :infinite-scroll-disabled="disabled" style="overflow:auto;padding-left: 2%;margin-top: 1%;">
      <li v-for="item in dynamicContent" class="infinite-list-item" style="list-style: none;">
        <router-link target="_blank"  :to="'/dynamic/'+item.dynamicId" class="users" style="text-decoration:none;outline: none;color: #000;">
        <div class="main_info">
          <div class="left_main_content_child_one" style="margin-bottom: 1%;height: auto;padding-bottom: 2%">
            <!--头像-->
            <router-link target="_blank"  :to="'/u/'+item.userId" class="users" style="text-decoration:none;outline: none;color: #000;width: 50%">
            <div :id="'dynamic'+item.dynamicId" style="margin-left:2%;padding-top: 3%;float: left;">
              <el-avatar :src="item.headPosition" @error="errorHandler" :size="45">
              </el-avatar>
            </div>
            </router-link>
            <!--用户名-->
            <div style="margin:0 0 0 10%;padding-top:1px;width: 15%;">
              <router-link target="_blank"  :to="'/u/'+item.userId" class="users" style="text-decoration:none;outline: none;color: #000;width: 50%">
              <h4 style="margin-bottom: 0" class="username">{{item.username}}</h4>
              </router-link>
              <span style="font-size: 8px;color:rgba(0,0,0,.5);">{{item.dynamic_time}}</span>
            </div>
            <!--内容-->
            <div style="margin:2% 0 0 10%;font-size: 16px">
              <dl v-html="item.content" v-lazy="item.content" class="replyclass" style="margin: 0 2% 0 0">
                {{item.content}}
              </dl>
              <div style="margin-top: 2%;width: 70%">
                <el-image v-for="item1 in item.submit_images"  :src="item1" alt="加载失败" fit="fit" style="width: 120px; height: 120px;margin-right: 1%;"
                          :preview-src-list="item.submit_images"></el-image>
              </div>
            </div>
        </div>
        </div>
        </router-link>
      </li>
    </ul>
    <p v-if="loading" style="text-align: center">加载中...</p>
    <p v-if="noMore" style="text-align: center;">没有更多了...</p>
  </div>
</template>

<script>
  export default {
    name: "test",
    data(){
      return {
        initLoading:true,
        msgs:null,
        username:'',
        circleUrl:'',
        loading:false,
        noMore:false,
        num:2,
        disabled:false,
        dynamicContent:[],
        currentPage:2,
      }
    },
    methods:{
      errorHandler(){
        this.circleUrl = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';
      },
      load(){
        this.loading = true;
        const _this = this;
        setTimeout(() => {
          axios.get('http://localhost:8181/dynamic/findMyDynamic/'+this.$route.params.userId+'/'+this.currentPage+'/10').then(function (resp) {
            for(let item in resp.data.dynamic){
              resp.data.dynamic[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].headPosition;
              if(resp.data.dynamic[item].submit_images!==null){
                resp.data.dynamic[item].submit_images = resp.data.dynamic[item].submit_images.split(",");
                resp.data.dynamic[item].submit_images.pop();  //删除最后一个空的元素
                for(let item1 in resp.data.dynamic[item].submit_images){
                  resp.data.dynamic[item].submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].submit_images[item1];
                }
              }
            }
            _this.dynamicContent = _this.dynamicContent.concat(resp.data.dynamic);
            _this.currentPage++;
            if(resp.data.dynamic.length !== 10){
              _this.noMore = true;
              _this.disabled = true;
            }
          })
          this.loading = false
        }, 2000)
      },
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/dynamic/findMyDynamic/'+this.$route.params.userId+'/1/10').then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          for(let item in resp.data.dynamic){
            resp.data.dynamic[item].headPosition = 'http://localhost:8181/profilephoto/'+resp.data.dynamic[item].headPosition;
            if(resp.data.dynamic[item].submit_images !==null){
              resp.data.dynamic[item].submit_images = resp.data.dynamic[item].submit_images.split(",");
              resp.data.dynamic[item].submit_images.pop();  //删除最后一个空的元素
              for(let item1 in resp.data.dynamic[item].submit_images){
                resp.data.dynamic[item].submit_images[item1] = 'http://localhost:8181/dynamic/'+resp.data.dynamic[item].submit_images[item1];
              }
            }
          }
          _this.dynamicContent = resp.data.dynamic;
          _this.initLoading = false;
        }else{
          _this.$message.error("查询动态信息失败");
        }
      })
    }
  }
</script>

<style scoped>
  .main_info{
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 1%;
    padding:0;
    background-color:  #ffffff;
    border-radius: 3%;
  }
  .username:hover{
    color: #409EFF;
  }
</style>