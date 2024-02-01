<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-main style="position: relative">
      <div
        style="
          background: linear-gradient(90deg, white, grey);
          height: 600px;
          align-content: center;
        "
      >
        <div
          class="BrightBulb"
          style="position: absolute; margin-top: 200px; margin-left: 0px"
        >
          <img src="../assets/off.png" />
          <p class="word">Join us and Light us!</p>
        </div>

        <div style="margin-top: 50px; margin-right: 100px; float: right">
          <el-form
            :model="ruleForm"
            status-icon
            :rules="rules"
            ref="ruleForm"
            label-width="100px"
            style="float: right"
          >
            <el-form-item label="用户名：" prop="username">
              <el-input v-model="ruleForm.username"> </el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
              <el-input
                type="password"
                v-model="ruleForm.password"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="checkPassword">
              <el-input
                type="password"
                v-model="ruleForm.checkPassword"
                autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item label="年龄" prop="age">
              <el-input v-model.number="ruleForm.age"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="ruleForm.gender">
                <el-radio :label="0"> 男 </el-radio>
                <el-radio :label="1"> 女 </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('ruleForm')"
                >提交</el-button
              >
              <el-button @click="resetForm('ruleForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-main>
  </div>
</template>





<script>
import myMenuBarVue from "../components/myMenuBar.vue";
import axios from "axios";
import Cookies from "js-cookie";

export default {
  components: {
    myMenuBarVue,
  },
  data() {
    var checkName = (rule, value, callback) => {
      let nameReg = /^\w{1,10}$/;
      if (!nameReg.test(value)) {
        callback(new Error("账号或用户名长度应在1至10位之间"));
      }
      callback();
    };
    var checkAge = (rule, value, callback) => {
      if (value == "") {
        callback();
      }
      if (!Number.isInteger(value)) {
        callback(new Error("请输入数字值"));
      } else {
        if (value < 18) {
          callback(new Error("必须年满18岁"));
        } else if (value > 120) {
          callback(new Error("请输入合理的年龄或联系后台管理员"));
        } else {
          callback();
        }
      }
    };
    var checkPassword = (rule, value, callback) => {
      let checkPasswordReg = /^\w{3,50}$/;
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (checkPasswordReg.test(value)) {
          if (value.length >= 6 && value.length <= 18) {
            callback();
          } else {
            callback(new Error("密码长度在6~18之间"));
          }
        } else {
          callback(new Error("密码必须仅同时包含大小写字母和数字"));
        }
      }
    };
    var checkPassword2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入密码"));
      } else if (value !== this.ruleForm.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {    
      loginData: {
        isLogin: false,
        role: "user",
        username: "null",
      },
      ruleForm: {
        role: 1,
        username: "",
        password: "",
        checkPassword: "",
        age: "",
        gender: 0,
        deptId: null,
      },
      deptItem: [],
      rules: {
        username: [{ required: true, validator: checkName, trigger: "blur" }],
        password: [
          { required: true, validator: checkPassword, trigger: "blur" },
        ],
        checkPassword: [
          { required: true, validator: checkPassword2, trigger: "blur" },
        ],
        age: [{ required: false, validator: checkAge, trigger: "blur" }],
        gender: [{ required: true, trigger: "change" }],
      },
    };
  },
  methods: {
    //弹窗
    Choose_customE(){
      document.getElementById("emp").style.display = "block";
    },
    Choose_customC(){
      document.getElementById("emp").style.display = "none";
    },
    getLoginInfo(loginData) {
      this.loginData.isLogin = loginData.isLogin;
      this.loginData.username = loginData.username;
      this.loginData.role = loginData.role;
      console.log("RegisterVue Get Data: ↓↓");
      console.log(this.loginData);
      this.getDeptItem();
    },
    submitForm(formName) {
      var outside = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log("Regster Data Submit ↓↓");
          console.log(this.ruleForm);
          /*后端数据递送接口*/
          let registerUrl = outside.GLOBAL.networkSrc+"/register";
          var config = {
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Origin": "*",
              Token: Cookies.get("token"),
            },
          };
          var serverResponse;
          axios
            .post(registerUrl, this.ruleForm, config)
            .then(function (result) {
              serverResponse = JSON.parse(result.request.response);
              if (serverResponse.code === 1) {
                outside.$message({
                  type: "success",
                  message: "注册成功！",
                  duration: 2000,
                });
                console.log(outside.ruleForm.username + "注册成功！");
                setTimeout(() => {
                  outside.$router.push("/login");
                }, 2000);
              } else {
                outside.$message({
                  type: "error",
                  message: serverResponse.message,
                  duration: 2000,
                });
                console.log(serverResponse.message);
              }
            })
            .catch((error) => {
              outside.$message({
                type: "error",
                message: error,
                duration: 2000,
              });
              console.log("出错了，请联系后台管理人员" + error);
            });
        } else {
          this.$message({
            type: "error",
            message: "请正确填写注册信息",
            duration: 2000,
          });
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>

<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
.MainLogo {
  margin-left: 22px;
}
.Home {
  font-family: "JetBrains Mono bold";
  text-align: left;
}
.word {
  font-family: "JetBrains Mono ExtraBold";
  text-align: left;
}
.HomeImgs {
  height: 500px;
}
</style>