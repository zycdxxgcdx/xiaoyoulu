<template>
  <div style="padding-top: 1%;">
    <div class="main_content" style="background-color: #ffffff;padding: 1%;">
      <el-page-header @back="goBack" content="学校创建">
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
                      action="http://localhost:8181/school/profilePhoto"
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
          <el-form-item label="地区">
            <el-col :span="5" style="margin-left: 4%;">
              <el-form-item prop="province">
                <el-select v-model="form.province" placeholder="请选择省份" @click.native="loadProvince()"  style="margin-left: 4%;">
                  <el-option :label="item" :value="item" v-for="item in provinces"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col class="line" :span="1" style="margin-left: 2%;" v-if="showCity">-</el-col>

            <el-col :span="5" v-if="showCity">
              <el-form-item prop="city">
                <el-select v-model="form.city" placeholder="请选择市" @click.native="loadCity(form.province)">
                  <el-option :label="item" :value="item" v-for="item in citys"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-divider></el-divider>

          <el-form-item label="名称" prop="schoolName" ref="school">
            <el-col :span="6" style="margin-left:5%;" v-if="!flag">
              <el-select v-model="form.schoolName" placeholder="请选择学校" @click.native="loadSchool(form.city)">
                <el-option :label="item" :value="item" v-for="item in schools"></el-option>
              </el-select>
            </el-col>
            <el-col :span="10" style="margin-left:5%;font-size: 10px" v-if="!flag">
              <span @click="noSchool" class="noshool"> 无所在学校,点击这里手动输入</span>
            </el-col>
            <el-col :span="15" style="margin-left:1%;" v-if="flag">
                <el-input v-model="form.schoolName"  class="username_input"  style="margin-left: 3%;" placeholder="请输入学校名称">
                </el-input>
            </el-col>
          </el-form-item>
          <el-divider></el-divider>
          <el-form-item style="margin-left: 4%;">
            <el-button type="primary" @click="onSubmit('form')">立即创建</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  // import cityJson from '../static/city.json'
  import schoolJson from '../../static/school.json'
  export default {
    name: "CreateSchool",
    data(){
      var validateName = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('名称不能为空'));
        }else if (value.length < 1 || value.length > 26) {
          callback(new Error('学校名称只能为1-26字符'));
        }
        callback();
      };
      var validatepProvince= (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('省份不能为空'));
        }
        callback();
      };
      var validateCity= (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('市不能为空'));
        }
        callback();
      };
      return {
        showCity:true,
        flag:false,
        provinces:[],
        citys:[],
        schools:[],
        id:this.$store.state.UserId,
        imageUrl:"https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        // token:localStorage.getItem('Authorization'),
        form: {
          schoolName: '',
          province:'',
          city:'',
          headPosition:'',
        },
        rules: {
          schoolName: [
            { validator: validateName, trigger: 'blur' }
          ],
          province: [
            { validator: validatepProvince, trigger: 'blur' }
          ],
          city: [
            { validator: validateCity, trigger: 'blur' }
          ],
        },
      }
    },
    methods:{
      noSchool(){
        this.flag = true;
        this.form.schoolName = '';
      },
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
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 5MB!');
        }
        return isImage && isLt2M;
      },
      onSubmit(formName) {
        const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            if(_this.imageUrl === 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'){
              _this.$message.error("头像未上传")
            }else{
              console.log(_this.form);
              axios.post('http://localhost:8181/school/createSchool',_this.form).then(function(resp){
                console.log(resp);
                if(resp.data.msg === '创建成功'){
                  _this.$message.success('创建学校成功');
                  _this.$router.push('/school');
                }else if(resp.data.msg === '已创建'){
                  _this.$message.warning('学校已被创建');
                }else if(resp.data.msg === '学校名含有屏蔽词'){
                  _this.$message.warning('学校名含有系统屏蔽词，请修改');
                }else{
                  _this.$message.error('创建学校失败');
                }
              })
            }

          }else{
            console.log("验证失败");
          }
        });
      },
      goBack(){
        this.$router.push('/school');
      },
      //加载省份
      loadProvince(){
        //移除名称的校验结果
        this.$refs.form.clearValidate('schoolName');
        //回显多项选择框
        this.flag = false;

        var in_provinces = [];
        for(let item in schoolJson.schools){
          in_provinces.push(schoolJson.schools[item].province_name);
        }
        this.provinces = in_provinces;
        this.form.city = '';
        this.form.schoolName = '';
      },
      //根据所选的省份加载城市
      loadCity(province){
        this.$refs.form.clearValidate('schoolName');
        this.flag = false;

        console.log(province);
        var in_citys = [];
          for(let item in schoolJson.schools){
            if(schoolJson.schools[item].province_name === province){
              for(let item1 in schoolJson.schools[item].cities){
                in_citys.push(schoolJson.schools[item].cities[item1].city_name);
              }
              this.citys = in_citys;
              break;
            }
          }
          this.form.schoolName = '';
      },
      loadSchool(city){
        var in_school = [];
        for(let item in schoolJson.schools){
          if(schoolJson.schools[item].province_name === this.form.province){
            for(let item1 in schoolJson.schools[item].cities){
              if(schoolJson.schools[item].cities[item1].city_name === city){
               for(let item2 in schoolJson.schools[item].cities[item1].universities){
                 in_school.push(schoolJson.schools[item].cities[item1].universities[item2]);
               }
              }
            }
            this.schools = in_school;
            break;
          }
        }
      }

    },
    created() {

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
  .noshool{
    font-size:12px;
    color:#409EFF;
    cursor: pointer;
  }
</style>