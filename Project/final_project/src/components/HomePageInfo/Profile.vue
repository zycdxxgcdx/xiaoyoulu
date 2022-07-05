<template>
  <div style="margin-top: 1%;">
    <div class="main_content" style="background-color: #ffffff;padding: 1%;">
      <el-page-header @back="goBack" content="个人资料">
      </el-page-header>
      <div class="profile_form" style="margin-top: 4%;">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules" >
          <el-divider></el-divider>
            <el-form-item label="头像" class="head">
              <div style="float: left;margin-left: 5%;">
                <el-avatar shape="square" :size="80" :src="imageUrl" fit="fill"></el-avatar>
              </div>
              <div style="float: left;margin-left: 4%;">
                <el-upload
                        class="avatar-uploader"
                        action="http://localhost:8181/user/profilePhoto"
                        :show-file-list="false"
                        :before-upload="beforeAvatarUpload"
                        :on-success="handleAvatarSuccess"
                        name="profilePhoto">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传jpg/png/jpeg图片，且不超过2M</div>
                </el-upload>
              </div>
            </el-form-item>
          <el-divider></el-divider>
          <el-form-item label="昵称" class="username_input" prop="username">
            <el-input v-model="form.username" style="margin-left: 3%;width: auto" placeholder="请输入你的昵称">
            </el-input>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="form.gender" placeholder="请选择性别" style="margin-left: 5%;width: 150px">
              <el-option label="男" value="male"></el-option>
              <el-option label="女" value="female"></el-option>
            </el-select>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item label="生日" prop="birth">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.birth" style="margin-left: 5%;width: 200px;"></el-date-picker>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item style="margin-left: 4%;">
            <el-button type="primary" @click="onSubmit('form')">立即修改</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: "Profile",
    data(){
      var validateUseranme = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('昵称不能为空'));
        }else if (value.length < 1 || value.length > 10) {
          callback(new Error('昵称只能为1-10字符'));
        }
        callback();
      };
      var validateGender = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('性别不能为空'));
        }
        callback();
      };
      var checkBirth = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('生日不能为空'));
        }
        callback();
      };
      return {
        id:this.$store.state.UserId,
        imageUrl:'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        // token:localStorage.getItem('Authorization'),
        form: {
          username: '',
          gender: '',
          birth: '',
          headPosition:'',
          userId:this.$store.state.UserId,
        },
        rules: {
          username: [
            { validator: validateUseranme, trigger: 'blur' }
          ],
          gender: [
            { validator: validateGender, trigger: 'blur'}
          ],
          birth: [
            { validator: checkBirth, trigger: 'blur' }
          ],
        },
      }
    },
    methods:{
      handleAvatarSuccess(res) {
        // this.squareUrl = URL.createObjectURL(file.raw);
        this.imageUrl= res.url;
        var array = res.url.split("/");
        this.form.headPosition = array[array.length-1];
      },
      beforeAvatarUpload(file){
        let types = ['image/jpeg', 'image/jpg', 'image/png'];
        const isImage = types.includes(file.type);
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isImage) {
          this.$message.error('上传头像图片只能是 JPG、JPEG、PNG 格式!');
          return false;
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
          return false;
        }
        return isImage && isLt2M;
      },
      onSubmit(formName) {
        const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(_this.form.headPosition === null){
              _this.$message.error("头像未上传")
            }else{
              console.log(_this.form)
              axios.post('http://localhost:8181/user/updateUser',_this.form).then(function(resp){
                console.log(resp);
                if(resp.data.msg === '更新用户信息成功'){
                  _this.$message.success('更新成功');
                  _this.$router.push('/u/'+_this.$store.state.UserId);
                }else if(resp.data.msg === '昵称含有屏蔽词'){
                  _this.$message.warning('昵称含有系统屏蔽词，请修改');
                }else {
                  _this.$message.error('更新失败');
                }
              })
            }

          }else{
            console.log("验证失败");
          }
        });
      },
      goBack(){
        this.$router.push('/u/'+this.$store.state.UserId);
      },
    },
    created() {
      const _this =this;
      axios.get('http://localhost:8181/user/getUserProfile/' + this.$store.state.UserId).then(function (resp) {
        console.log(resp)
        if(resp.data.msg === '查找用户信息成功'){
          if(resp.data.userInfo.headPosition !== null){
            _this.imageUrl = 'http://localhost:8181/profilephoto/'+resp.data.userInfo.headPosition;
          }
         _this.form =  resp.data.userInfo;
          console.log(_this.form)
        }
      })
    }
  }
</script>

<style scoped>
  .main_content{
    /*background-color:  #ffffff;*/
    width: 50%;
    position: relative;
    margin-left: 17%;
    padding-top: 0;
    height: 100%;
    float: left;
    /*border: 1px solid red;*/
  }
  .main_profile{
    /*border: 1px solid red;*/
    height: 140px;
    background-color: #ffffff;
  }
  .main_info{
    margin-top: 2%;
    /*border: 1px solid red;*/
    background-color: #ffffff;
  }

  /deep/ .el-tabs__item{
    font-size:16px;
    margin-right: 2%;
  }
  /*修改输入框样式*/
  .username_input>>>.el-input__inner {
    border: 0;
  }
  .el-divider{
    /*margin: 8px 0;*/
    background: 0 0;
    margin-left: 3%;
    border-top: 1px solid #E6EBF5;
    /*border-top: 1px solid red;*/
    /*width: 600px;*/
    width: auto;
  }
  /*设置头像label垂直居中*/
  .head >>> .el-form-item__label{
    margin-top: 2%;
  }
   /deep/ .el-form-item__error{
    margin-left: 5%;
  }
  /deep/ .el-avatar>img{
    width: 100%;
  }
</style>