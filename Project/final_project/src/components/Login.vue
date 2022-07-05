<template>
  <div>
    <el-dialog
            :visible.sync="dialogVisible"
            width="30%"
            top="10%"
            @close="close('login_ruleForm','register_ruleForm')">
      <el-tabs v-model="activeName1" type="card" @tab-click="refreshregister('login_ruleForm','register_ruleForm')">
        <el-tab-pane label="登录" name="first">
          <el-form :model="login_ruleForm" status-icon :rules="login_rules" ref="login_ruleForm" label-width="100px">
            <!--                      prop 需要验证的字段名-->
            <el-form-item label="账号" prop="accountName">
              <el-input v-model="login_ruleForm.accountName" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="login_ruleForm.password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="login_submitForm('login_ruleForm')">登录</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" name="second">
          <el-form :model="register_ruleForm" status-icon :rules="register_rules" ref="register_ruleForm" label-width="100px" class="demo-ruleForm">
            <el-form-item label="账号" prop="accountName">
              <el-input v-model="register_ruleForm.accountName" autocomplete="off" placeholder="账号只能为字母,数字,下划线(6-20字符)"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input type="password" v-model="register_ruleForm.password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="check_register_password">
              <el-input type="password" v-model="register_ruleForm.check_register_password" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="register_submitForm('register_ruleForm')">注册</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
  import { mapMutations } from 'vuex';
  export default {
    name: "Login",
    data() {
      //自定义校验 callback 必须被调用
      //登录账号校验
      var checkLoginUsername = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('账号不能为空'));
        }else if (value.length < 6 || value.length >20){
          callback(new Error('账号只能为6-20字符'));
        }else {
          var patrn=/^(\w){6,20}$/;
          if (!patrn.exec(value)){
            callback(new Error('账号只能为字母,数字,下划线(6-20字符)'));
          }
        }
        callback();
      };
      //登录密码校验
      var validateLoginPassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('密码不能为空'));
        }else if (value.length < 6 || value.length >20){
          callback(new Error('密码只能为6-20字符'));
        }
        callback();
      };
      //注册账号校验
      var checkRegisterUsername = (rule, value, callback) => {
        if (value === '') {
          //需要查询数据库空中账号是否重复
          callback(new Error('账号不能为空'));
        }else if (value.length < 6 || value.length >20){
          callback(new Error('账号只能为6-20字符'));
        }else {
          var patrn=/^(\w){6,20}$/;
          if (!patrn.exec(value)){
            callback(new Error('账号只能为字母,数字,下划线(6-20字符)'));
          }
        }
        callback();
      };
      //注册密码校验
      var validateRegisterPassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('密码不能为空'));
        } else if (value.length < 6 || value.length >20){
          callback(new Error('密码只能为6-20字符'));
        } else if (this.register_ruleForm.check_register_password !== '') {
          this.$refs.register_ruleForm.validateField('check_register_password');
        } //当两次密码不一致时，先修改第二次密码，再修改第一次密码执行的验证
        callback();
      };
      //注册密码一致性的校验
      var checkRegisterPassword = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('确认密码不能为空'));
        } else if(value !== this.register_ruleForm.password) {
          callback(new Error('两次密码不一致'));
        } else {
          callback();
        }
      };
      return {
        dialogVisible: true,
        //登录填写的账户名密码表单
        login_ruleForm: {
          accountName:'',
          password: ''
        },
        //注册填写的账户名密码表单
        register_ruleForm: {
          accountName:'',
          password: '',
          check_register_password: '',
        },
        //校验规则
        //trigger:'blur' 失去焦点触发
        //trigger:'change' 数据改变促发焦点触发
        //登录校验
        login_rules: {
          accountName: [
            { validator: checkLoginUsername, trigger: 'blur' }
          ],
          password: [
            { validator: validateLoginPassword, trigger: 'blur' }
          ]
        },
        //注册校验
        register_rules: {
          accountName: [
            { validator: checkRegisterUsername, trigger: ['blur','change']},
          ],
          password: [
            { validator: validateRegisterPassword, trigger: ['blur','change'] }
          ],
          check_register_password: [
            { validator: checkRegisterPassword, trigger: ['blur','change']}
          ]
        },
        activeName1: 'first',
      };
    },
    methods: {
      ...mapMutations(['changeLogin','changeUsername','changeUserId']),
      //切换登录注册清除掉上一次的为空的验证提示
      refreshregister(forName1,forName2){
        if(this.activeName1 === 'first'){
          if(this.register_ruleForm.accountName === ''){
            this.$refs[forName2].clearValidate('accountName');
          }
          if(this.register_ruleForm.password === ''){
            this.$refs[forName2].clearValidate('password');
          }
          if(this.register_ruleForm.check_register_password === ''){
            this.$refs[forName2].clearValidate('check_register_password');
          }
        }else if(this.activeName1 === 'second'){
          if(this.login_ruleForm.accountName === ''){
            this.$refs[forName1].clearValidate('accountName');
          }
          if(this.register_ruleForm.password === ''){
            this.$refs[forName1].clearValidate('password');
          }
        }
      },
      //dialog自带的close回调函数
      //关闭登录注册框时触发
      close(formName1,formName2){
        //resetField 对该表单项进行重置，将其值重置为初始值并移除校验结果
        this.$refs[formName1].resetFields();
        this.$refs[formName2].resetFields();
        //回退到前一页面
        this.$router.go(-1)
      },
      //点击登录按钮执行操作
      login_submitForm(formName) {
        const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            axios.post('http://localhost:8181/account/login',this.login_ruleForm).then(function(resp){
              if(resp.data.msg === '登陆成功'){
                _this.userToken = resp.data.token;
                // console.log(resp.data.userId);
                _this.userId = resp.data.userId.userId;
                // console.log(_this.userId);
                // 将用户token保存到vuex中
                _this.changeLogin({ Authorization: _this.userToken });
                _this.changeUsername({AccountName: _this.login_ruleForm.accountName});
                _this.changeUserId({UserId: _this.userId});
                console.log(localStorage.getItem('UserId'));
                _this.$router.go(-1);
              }else if(resp.data.msg === '账号不存在'){
                _this.$message.error('账号不存在');
              }else if(resp.data.msg === '密码错误'){
                _this.$message.error('密码错误');
              }
              console.log(resp);
              console.log(resp.data.token);
            })
          } else {
            return false;
          }
        });
      },
      //点击注册按钮执行的操作
      register_submitForm(formName) {
        const _this = this;
        this.$refs[formName].validate((valid) => {
          if (valid) {
            axios.get('http://localhost:8181/account/checkAccountName/'+_this.register_ruleForm.accountName).then(function(resp){
              if(resp.data === false){
                _this.$message.error('账号已存在');
              }else{
                axios.post('http://localhost:8181/account/register',_this.register_ruleForm).then(function(resp){
                  console.log(resp);
                  if(resp.data.msg === '注册成功'){
                    _this.$message.success('注册成功');
                    _this.userToken = resp.data.token;
                    _this.userId = resp.data.userId.userId;
                    // 将用户token保存到vuex中
                    _this.changeLogin({ Authorization: _this.userToken });
                    _this.changeUsername({AccountName: _this.register_ruleForm.accountName});
                    _this.changeUserId({UserId: _this.userId});
                    _this.$router.go(-1);
                  }else{
                    _this.$message.error('注册失败');
                  }
                })
              }
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
    }
    // watch: {
    //     '$route'(){
    //         this.dialogVisible = false;
    //     }
    // }
  }
</script>

<style scoped>

</style>
