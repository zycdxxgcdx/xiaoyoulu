<template>
  <div style="padding-top: 1%;">
    <div class="main_content" style="background-color: #ffffff;padding: 1%;">
      <el-page-header @back="goBack" content="班级创建">
      </el-page-header>
      <div class="profile_form">
        <el-form ref="form" :model="form" label-width="80px" :rules="rules" >
          <el-divider></el-divider>
          <el-form-item label="头像" class="head">
            <div style="float: left;margin-left: 5%;">
              <el-avatar shape="square" :size="80" :src="imageUrl" fit="fill"></el-avatar>
            </div>
            <div style="float: left;margin-left: 4%;">
              <el-upload
                      class="avatar-uploader"
                      action="http://localhost:8181/classes/profilePhoto"
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

          <el-form-item label="学校" prop="schoolName">
            <el-col :span="6" style="margin-left:5%;" v-if="!flag">
              <el-select v-model="form.schoolName" placeholder="请选择学校" @click.native="loadSchool(form.province,form.city)">
                <el-option :label="item" :value="item" v-for="item in schools"></el-option>
              </el-select>
            </el-col>
          </el-form-item>
          <el-divider></el-divider>

          <el-form-item label="专业">
            <el-col :span="5" style="margin-left: 4%;">
              <el-form-item prop="subject">
                <el-select v-model="form.subject" placeholder="请选择学科" @click.native="loadSubject()"  style="margin-left: 4%;">
                  <el-option :label="item" :value="item" v-for="item in subjects"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col class="line" :span="1" style="margin-left: 2%;" v-if="showCity">-</el-col>
            <el-col :span="5">
              <el-form-item prop="types">
                <el-select v-model="form.types" placeholder="请选择类别" @click.native="loadType(form.subject)"  style="margin-left: 4%;">
                  <el-option :label="item" :value="item" v-for="item in types"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col class="line" :span="1" style="margin-left: 2%;" v-if="showCity">-</el-col>
            <el-col :span="5" v-if="showCity">
              <el-form-item prop="major">
                <el-select v-model="form.major" placeholder="请选择专业" @click.native="loadMajor(form.types)">
                  <el-option :label="item" :value="item" v-for="item in majors"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-divider></el-divider>

          <el-form-item label="班级">
            <el-col :span="5" style="margin-left: 4%;">
              <el-form-item prop="grade">
                <el-select v-model="form.grade" placeholder="请选择年级" @click.native="loadGrade()"  style="margin-left: 4%;">
                  <el-option :label="item" :value="item" v-for="item in grades"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col class="line" :span="1" style="margin-left: 2%;" v-if="showCity">-</el-col>
            <el-col :span="5" v-if="showCity">
              <el-form-item prop="className">
                <el-select v-model="form.className" placeholder="请选择班级" @click.native="loadClassName()">
                  <el-option :label="item" :value="item" v-for="item in classes"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-form-item>
          <el-divider style="padding-bottom: 0;" id="lastDivide"></el-divider>

          <el-form-item style="margin-left: 4%;margin-bottom: 0;">
            <el-button type="primary" @click="onSubmit('form')">立即创建</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  import grade from '../../static/grade.json'
  import schoolJson from '../../static/school.json'
  import major from '../../static/major.json'
  import classes from '../../static/classes.json'

  export default {
    name: "CreateClass",
    data() {
      var validateName = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('学校不能为空'));
        }
        callback();
      };
      var validatepProvince = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('省份不能为空'));
        }
        callback();
      };
      var validateCity = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('市不能为空'));
        }
        callback();
      };
      var validateSubject = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('学科不能为空'));
        }
        callback();
      };
      var validateTypes = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('类别不能为空'));
        }
        callback();
      };
      var validateMajor = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('专业不能为空'));
        }
        callback();
      };
      var validateGrade = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('年级不能为空'));
        }
        callback();
      };
      var validateGlassName = (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('班级不能为空'));
        }
        callback();
      };
      return {
        //学科
        subjects:[],
        //学科类型
        types:[],
        //专业
        majors:[],
        //年级
        grades:[],
        //班级
        classes:[],

        showCity: true,
        flag: false,
        provinces: [],
        citys: [],
        schools: [],
        id: this.$store.state.UserId,
        imageUrl: "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png",
        // token:localStorage.getItem('Authorization'),
        form: {
          schoolName: '',
          province: '',
          city: '',
          subject:'',
          types:'',
          major:'',
          grade:'',
          className:'',
          headPosition: '',
          userId:this.$store.state.UserId
        },
        rules: {
          schoolName: [
            {validator: validateName, trigger: 'blur'}
          ],
          province: [
            {validator: validatepProvince, trigger: 'blur'}
          ],
          city: [
            {validator: validateCity, trigger: 'blur'}
          ],
          subject: [
            {validator: validateSubject, trigger: 'blur'}
          ],
          types: [
            {validator: validateTypes, trigger: 'blur'}
          ],
          major: [
            {validator: validateMajor, trigger: 'blur'}
          ],
          grade: [
            {validator: validateGrade, trigger: 'blur'}
          ],
          className: [
            {validator: validateGlassName, trigger: 'blur'}
          ],
        },
      }
    },
    methods: {
      handleAvatarSuccess(res) {
        // this.squareUrl = URL.createObjectURL(file.raw);
        this.imageUrl = res.url;
        var array = res.url.split("/");
        this.form.headPosition = array[array.length - 1];
      },
      beforeAvatarUpload(file) {
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
            if (_this.imageUrl === 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png') {
              _this.$message.error("头像未上传")
            } else {
              console.log(_this.form);
              axios.post('http://localhost:8181/classes/createClass', _this.form).then(function (resp) {
                console.log(resp);
                if (resp.data.msg === '创建成功') {
                  _this.$message.success('创建班级成功');
                  _this.$router.push('/class');
                }else if(resp.data.msg === '已存在'){
                  _this.$message.warning('班级已存在');
                }else{
                  _this.$message.error('创建失败');
                }
              })
            }

          } else {
            console.log("验证失败");
          }
        });
      },
      goBack() {
        this.$router.push('/class');
      },
      //加载省份
      loadProvince() {
        var in_provinces = [];
        for (let item in schoolJson.schools) {
          in_provinces.push(schoolJson.schools[item].province_name);
        }
        this.provinces = in_provinces;
        this.form.city = '';
        this.form.schoolName = '';
      },
      //根据所选的省份加载城市
      loadCity(province) {
        console.log(province);
        var in_citys = [];
        for (let item in schoolJson.schools) {
          if (schoolJson.schools[item].province_name === province) {
            for (let item1 in schoolJson.schools[item].cities) {
              in_citys.push(schoolJson.schools[item].cities[item1].city_name);
            }
            this.citys = in_citys;
            break;
          }
        }
        this.form.schoolName = '';
      },
      loadSchool(province,city) {
        console.log(province);
        console.log(city);
        const _this = this;
        axios.get('http://localhost:8181/classes/getSchools/'+province+'/'+city).then(function (resp) {
            console.log(resp);
            if(resp.data.msg === '查询成功'){
              _this.schools= resp.data.schools
            }
        })
      },
      loadSubject(){
        var in_subject = [];
        for(let item in major){
          in_subject.push(major[item].SubjectCategories);
        }
        this.subjects = in_subject;
      },
      loadType(subject){
        var in_type = [];
        for(let item in major){
          if(major[item].SubjectCategories === subject){
            for(let item1 in major[item].category){
              in_type.push(major[item].category[item1].type);
            }
            break;
          }
        }
        this.types = in_type;
      },
      loadMajor(type){
        var in_major = [];
        for(let item in major){
          if(major[item].SubjectCategories === this.form.subject){
            for(let item1 in major[item].category){
              if(major[item].category[item1].type === type){
                for(let item2 in major[item].category[item1].subject){
                  in_major.push(major[item].category[item1].subject[item2]);
                }
                break;
              }
            }
          }
        }
        this.majors = in_major;
      },
      loadGrade(){
        this.grades = grade.grades;
      },
      loadClassName(){
        this.classes = classes.classes;
      }

    },
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
  #lastDivide{
    margin-top: 0;
    margin-bottom: 0;
  }
</style>