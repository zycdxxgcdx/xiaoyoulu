<template>
  <div style="padding-top: 1%;">
    <div class="left_main_content">
      <div style="padding-left: 2%;">
        <p style="font-size: 17px;color:rgba(0,0,0,.8);float: left;">{{classes.schoolName}} - {{classes.major}} - {{classes.grade}} - {{classes.className}}</p>
        <el-button style="float: right;margin-top: 1%;margin-right: 2%;" :disabled="disabled" type="primary" v-show="!isMember" @click="applyClass">申请加入</el-button>
        <el-button style="float: right;margin-top: 1%;margin-right: 2%;" type="danger" v-show="isMember && !isAdministrators" @click="exitClass">退出班级</el-button>
        <el-button style="float: right;margin-top: 1%;margin-right: 2%;" type="primary" v-show="isMember" @click="toGroupChat">发起群聊</el-button>
        <div style="clear: both;"></div>
      </div>
      <el-divider></el-divider>
      <div style="padding-left: 2%;">
        <p style="color:rgba(0,0,0,.5);font-size: 15px">班级人数：{{numbers}}人  </p>
      </div>
      <div>
        <el-table
                class="my_table"
                :header-cell-style="{background:'#eef1f6'}"
                ref="singleTable"
                :data="tableData"

                style="width: 100%;margin-right: 0;">
            <el-table-column
                    type="index"
                    width="50">
            </el-table-column>
            <el-table-column
                    label="成员"
                    width="210">
              <template slot-scope="scope">
                <div style="font-size: 20px;float: left;margin-right: 2%;min-height:1px;display: block" v-if="scope.row.role === 1">
                  <i class="el-icon-s-custom"></i>
                </div>
                <div style="font-size: 20px;float: left;margin-right: 2%;min-height:1px;display: block" v-else>
                  <i class="el-icon-user" style="color:#ffffff;"></i>
                </div>
                <el-avatar shape="square" :size="20" :src="scope.row.users.headPosition" style="float: left;margin-right: 2%;"></el-avatar>
                <span  style="float: left;color: #409EFF;" v-if="userId === scope.row.userId">{{scope.row.users.username}}</span>
                <span  style="float: left;" v-else>{{scope.row.users.username}}</span>
              </template>
            </el-table-column>
            <el-table-column
                    label="性别"
                    width="100">
              <template slot-scope="scope">
                <span v-if="scope.row.users.gender === 'male'">
                    男
                </span>
                <span v-else="scope.row.users.gender === 'female'">
                    女
                </span>
              </template>
            </el-table-column>
            <el-table-column
                    label="生日">
              <template slot-scope="scope">
                <span>{{scope.row.users.birth}}</span>
              </template>
            </el-table-column>
          <el-table-column
                  label="加入时间">
            <template slot-scope="scope">
              <span>{{scope.row.inClassTime}}</span>
            </template>
          </el-table-column>
          <el-table-column width="200">
            <template slot-scope="scope">
              <router-link target="_blank" :to="{path:'/u/'+scope.row.userId}" style="text-decoration:none;outline: none;color: #000;">
              <el-button type="primary" size="small" style="float: left;">查看主页</el-button>
              </router-link>
              <el-button type="danger" size="small"  style="float: left;margin-left: 6%;" v-show="isAdministrators && scope.row.role === 0" @click="delClassMember(scope.row.userId,classId)">删除成员</el-button>

            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <router-view/>
  </div>
</template>

<script>
  import moment from 'moment'
  export default {
    name: "ClassInfo",
    data() {
      return {
        disabled:false,
        //班级名等信息
        classes:[],
        //班级人数
        numbers:0,
        //是否是班级管理员
        isAdministrators:true,
        //是否是班级成员
        isMember:true,
        userId:this.$store.state.UserId,
        classId:this.$route.query.classId,
        classInfoPageSize:10,
        squareUrl:"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        tableData: [{
          classId:0,
          id:0,
          inCLassTime:'',
          role:1,
          userId:0,
          users:{
            birth:'',
            gender:'male',
            headPosition:'',
            userId:0,
            username:''
          }
        }],
      }
    },
    methods: {
      toGroupChat(){
        const _this = this;
        axios.post('http://localhost:8181/socket/addSocketList/' + this.$store.state.UserId + '/' + this.classId + '/1').then(function (resp) {
          console.log(resp)
          if(resp.data.msg === '更新消息列表成功'){
              let routeUrl = _this.$router.resolve({
                path: '/message/'+_this.$store.state.UserId+'/whisper/'+_this.classId,
                query:{type:1}
              });
              window.open(routeUrl.href, '_blank');
            }
        })
        // this.$router.push('/message/'+this.$store.state.UserId+'/whisper/'+this.classId);
      },
      exitClass(){
        const _this = this;
        this.$confirm('确定要退出班级吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          axios.delete('http://localhost:8181/classes/exitClass/'+_this.userId+'/'+_this.classId).then(function (resp) {
            if(resp.data.msg === '退出成功'){
              _this.$message({
                type: 'success',
                message: '退出成功!'
              });
              _this.$router.push('/class');
            }else{
              _this.$message({
                type: 'error',
                message: '退出失败!'
              });
            }
          })
        }).catch(() => {
          _this.$message({
            type: 'info',
            message: '已取消退出'
          });
        });
      },
      applyClass(){
        const _this = this;
        this.disabled = true;
        axios.post('http://localhost:8181/classes/joinClass/'+this.userId+'/'+this.classId ).then(function (resp) {
          console.log(resp)
          if(resp.data.msg === '申请成功'){
            _this.$message.success("申请成功")
            setTimeout(()=>{
              _this.disabled = false;
            },3000)
          }else if(resp.data.msg === '重复申请'){
            _this.$message.warning("请勿重复申请")
            setTimeout(()=>{
              _this.disabled = false;
            },3000)
          }else{
            _this.$message.error("申请失败")
            setTimeout(()=>{
              _this.disabled = false;
            },3000)
          }
        })
      },
      delClassMember(userId,classId){
        console.log(userId)
        console.log(classId)
        const _this = this;
        this.$confirm('确定要删除改成员吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          axios.delete('http://localhost:8181/classes/delClassMember/'+userId+'/'+classId).then(function (resp) {
            if(resp.data.msg === '删除成功'){
              _this.$message({
                type: 'success',
                message: '删除成功!'
              });
              axios.get('http://localhost:8181/classes/checkMemberIdentity/'+_this.$route.query.classId+'/'+_this.$store.state.UserId).then(function (resp) {
                console.log(resp);
                if(resp.data.msg === '查询成功'){
                  _this.isAdministrators = resp.data.isAdministrators;
                  _this.isMember = resp.data.isMember;
                }
              })
              axios.get('http://localhost:8181/classes/findClassInfo/' +
                  _this.$route.query.classId + '/1/' + _this.classInfoPageSize).then(function (resp) {
                console.log(resp);
                if(resp.data.msg === '查询成功'){
                  _this.numbers = resp.data.memberNumbers;
                  for(let item in resp.data.userInfo){
                    resp.data.userInfo[item].inClassTime = moment(resp.data.userInfo[item].inClassTime).format("YYYY-MM-DD");
                    resp.data.userInfo[item].users.birth =  moment(resp.data.userInfo[item].users.birth).format("YYYY-MM-DD");
                    resp.data.userInfo[item].users.headPosition = 'http://localhost:8181/profilephoto/'+ resp.data.userInfo[item].users.headPosition;
                  }
                  _this.tableData = resp.data.userInfo;
                  console.log(_this.tableData)
                }
              })
            }else{
              _this.$message({
                type: 'error',
                message: '删除失败!'
              });
            }
          })
        }).catch(() => {
          _this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      }
    },
    updated:function(){
    },
    created() {
      const _this = this;
      axios.get('http://localhost:8181/classes/findClassNameByClassId/' + this.$route.query.classId).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          _this.classes = resp.data.classes;
        }
      })
      axios.get('http://localhost:8181/classes/checkMemberIdentity/'+this.$route.query.classId+'/'+this.$store.state.UserId).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          _this.isAdministrators = resp.data.isAdministrators;
          _this.isMember = resp.data.isMember;
        }
      })
      axios.get('http://localhost:8181/classes/findClassInfo/' +
          this.$route.query.classId + '/1/' + this.classInfoPageSize).then(function (resp) {
        console.log(resp);
        if(resp.data.msg === '查询成功'){
          _this.numbers = resp.data.memberNumbers;
          for(let item in resp.data.userInfo){
            resp.data.userInfo[item].inClassTime = moment(resp.data.userInfo[item].inClassTime).format("YYYY-MM-DD");
            resp.data.userInfo[item].users.birth =  moment(resp.data.userInfo[item].users.birth).format("YYYY-MM-DD");
            resp.data.userInfo[item].users.headPosition = 'http://localhost:8181/profilephoto/'+ resp.data.userInfo[item].users.headPosition;
          }
          _this.tableData = resp.data.userInfo;
          console.log("++++++++++++++++++++++")
          console.log(_this.tableData)
          console.log("++++++++++++++++++++++")
        }
      })
    }
  }
</script>

<style scoped>
  .left_main_content{
    background-color:  #ffffff;
    width: 55%;
    position: relative;
    margin-left: 17%;
    margin-top: 2%;
    margin-right: 0;
    float: left;
    height: 100%;
    /*border: 1px solid red;*/
  }
  .el-divider--horizontal{
    margin:0 0;
    padding: 0;
    border-top: 1px dashed #e1e3e5;
    background: 0 0;
  }
  .my_table >>> .el-table__row>td{
    /* 去除表格线 */
    border: none;
  }
</style>