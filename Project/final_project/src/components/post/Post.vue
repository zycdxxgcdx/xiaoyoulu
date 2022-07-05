<template>
  <div style="padding-top: 1%">
    <div class="left_main_content">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="标题" prop="title">
            <el-input v-model="ruleForm.title" ></el-input>
          </el-form-item>
        <el-form-item label="学校" prop="school">
          <el-select v-model="ruleForm.school" placeholder="请选择所在学校">
            <el-option v-if="schools.length === 0" label="无" value="无">无</el-option>
            <el-option v-else :label="item.schoolName" :value="item.schoolName" :key="item.schoolName" v-for="item in schools">{{item.schoolName}}</el-option>
          </el-select>
        </el-form-item>
          <el-form-item label="内容">
            <div id="div1"></div>
<!--            <el-input type="textarea" v-model="ruleForm.desc"></el-input>-->
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">发布</el-button>
          </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  import E from 'wangeditor';
  export default {
    data() {
      return {
        schools:[],
        flag: false,  //检测用户是否上传图片
        editor: null,
        upload_images: '',//上传到服务器的照片  不是最终提交照片
        ruleForm: {
          title: '',
          school: '',
          content:'',
          submit_images:'',  //最终提交照片 通过和上传到服务器的照片相比较 比较出用户删除的照片
                            // 以删除服务器中未提交的照片   最终存入数据库的照片
          delete_images:''  //需要在服务器中删除的照片  是用户先上传到了服务器并回显到了页面上的照片
                            //但是后面用户又删除掉了的照片
        },
        rules: {
          title:[
            {required:true,message: '标题不能为空' ,trigger: 'blur'},
            {max: 80, message: '长度应小于80个字符', trigger: 'blur' }
          ],
          school:[
            {required:true,message: '请选择所在学校', trigger: 'change'}
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        const _this = this;
        console.log(this.ruleForm.school);
        this.$refs[formName].validate((valid) => {
          //用户可以只上传图片  没有文字
          if (valid) {
            if(_this.editor.txt.text()|| _this.flag){
              _this.ruleForm.content = _this.editor.txt.text();   //获取用户输入的文本内容
              var imgReg = /<img.*?(?:>|\/>)/gi;
              var srcReg = /src=[\'\"]?([^\'\"]*)[\'\"]?/i;
              //获取所有的img元素
              var imgArr = _this.editor.txt.html().match(imgReg);
              let imgSrc = [];
              //获取每一个img标签中的src元素
              if (imgArr) {
                for (let i = 0; i < imgArr.length; i++) {
                  var src = imgArr[i].match(srcReg)[1];    //取数组中的第一个元素 就是src中的内容
                  imgSrc.push(src)
                }
              }
              //获取每一个src元素中的图片名 赋值到imgs_name数组中
              let imgsName = [];
              for(let j=0;j <imgSrc.length;j++){
                var array = imgSrc[j].split("/");
                _this.ruleForm.submit_images+= array[array.length-1]+',';  //获取每一个src中的图片名 在组装成一个字符串 以逗号相隔
              }
              // _this.ruleForm.submit_images = imgsName;
              console.log("提交的图片的的字符串： "+_this.ruleForm.submit_images);
              console.log("上传的图片的字符串： "+_this.upload_images);
              var array_submit = _this.ruleForm.submit_images.split(',');
              var array_upload = _this.upload_images.split(',');
              for(let k=0;k<array_upload.length;k++){
                let flag = false;
                for(let h=0;h<array_submit.length;h++){
                  if(array_upload[k] === array_submit[h]){
                    flag = true;
                    continue;
                  }
                }
                if(flag === false){
                  _this.ruleForm.delete_images += array_upload[k]+',';
                }
              }
              console.log("需要删除的字符串： "+_this.ruleForm.delete_images);
              console.log(localStorage.getItem('AccountName'));
              //  传的参数   标题  学校名称  内容  提交照片名   需删除照片名
              axios.post('http://localhost:8181/post/publicPost',{ruleForm:_this.ruleForm,accountName:localStorage.getItem('AccountName')}).then(function(resp){
                console.log(resp);
                if(resp.data.msg === '发帖成功'){
                  _this.$message.success('发帖成功');
                  _this.$router.push('/main');
                }else if(resp.data.msg === '标题含有屏蔽词'){
                  _this.$message.warning('标题中含有系统屏蔽词，请修改');
                }else if(resp.data.msg === '内容含有屏蔽词'){
                  _this.$message.warning('内容中含有系统屏蔽词，请修改');
                }else {
                  _this.$message.error('发帖失败');
                }
              })
            }
            else{
              _this.$message.warning('内容不能为空');
            }
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    },
    mounted() {
      const _this = this;
        this.editor = new E('#div1');
        this.editor.config.height = 300;
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
        this.editor.config.menus = [
          'image',
      ]
      this.editor.config.uploadImgHooks = {
        before: function (xhr, editor, files) {
          // 图片上传之前触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，files 是选择的图片文件

          // 如果返回的结果是 {prevent: true, msg: 'xxxx'} 则表示用户放弃上传
          // return {
          //     prevent: true,
          //     msg: '放弃上传'
          // }
          // alert("前奏");
        },
        success: function (xhr, editor, result) {
          // 图片上传并返回结果，图片插入成功之后触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
          // var url = result.data.url;
          // alert(JSON.stringify(url));
          // editor.txt.append(url);
          alert("成功");
        },
        fail: function (xhr, editor, result) {
          // 图片上传并返回结果，但图片插入错误时触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象，result 是服务器端返回的结果
          alert("失败");
        },
        error: function (xhr, editor) {
          // 图片上传出错时触发
          // xhr 是 XMLHttpRequst 对象，editor 是编辑器对象
          // alert("错误");
        },
        // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
        // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
        customInsert: function (insertImg, result, editor) {
          // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
          // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果
          // 举例：假如上传图片成功后，服务器端返回的是 {url:'....'} 这种格式，即可这样插入图片：
          var url = result.url;
          // console.log(url);
          _this.flag = true;  //已插入图片
          // console.log(_this.flag);
          var array = url.split("/");   //提取出图片名  xxx.jpg
          // console.log(array[array.length-1]);
          // _this.upload_images.push(array[array.length-1]);
          _this.upload_images = _this.upload_images + array[array.length-1] + ',';
          //var jsonStrings = encodeURIComponent(url);
          insertImg(url);
          // result 必须是一个 JSON 格式字符串！！！否则报错
        }
      }
      this.editor.create();
    },
    created() {
      // 获取用户已加入的学校的数据
      // 如果用户未加入学校，则this.ruleForm.school='无'
      const _this = this;
      axios.get('http://localhost:8181/post/getMySchool/'+this.$store.state.UserId).then(function (resp) {
        console.log(resp.data);
        if(resp.data.msg === '查询成功'){
          _this.schools = resp.data.schoolNames;
        }
      })
    }
  }
</script>

<style scoped>
  .left_main_content{
    /*background-color:  #ffffff;*/
    width: 55%;
    position: relative;
    padding-top: 2%;
    padding-right: 2%;
    margin-left: 15%;
    /*margin-right: 10%;*/
    float: left;
    height: 100%;
    /*border: 1px solid red;*/
    background: #ffffff;
  }
  .left_main_content_child >span{
    padding-right: 16px;
    font-size: 10px;
    font-weight: bold;
  }
  .left_main_content_child>.left_main_content_child_spanStyleTwo{
    margin-left: 1%;
    color:rgba(0,0,0,.5);
    cursor: pointer ;
  }
  .left_main_content_child>.left_main_content_child_spanStyleTwo:hover{
    color: #409EFF;
  }
  .left_main_content_child>.left_main_content_child_spanStyleThreeToSix{
    color:rgba(0,0,0,.5) ;
  }

</style>
