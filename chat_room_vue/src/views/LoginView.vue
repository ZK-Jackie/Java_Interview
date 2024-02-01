<template>
  <div class="MainBox">
    <myMenuBar ref="menuData" @transfer="getLoginInfo"></myMenuBar>
    <!-- 主体 -->
    <el-main style="position: relative">
      <!-- 主体渐变框内部 -->
      <div
          style="
          background: linear-gradient(90deg, white, grey);
          height: 600px;
          align-content: center;
        "
      >
        <!-- 灯泡 -->
        <div
            class="BrightBulb"
            style="position: absolute; margin-top: 200px; margin-left: 0px"
        >
          <img
              :src="bulbPic"
              class="bulb"
              v-on:mouseover="bulbOn()"
              v-on:mouseleave="bulbOff()"
          />
          <p class="word">Make Inspiration Happened.</p>
        </div>
        <!-- 表单块 -->
        <div style="margin-top: 220px; margin-right: 100px; float: right">
          <el-form
              :model="ruleForm"
              status-icon
              ref="ruleForm"
              :rules="rules"
              label-width="100px"
              style="float: right"
          >
            <!-- 单选 -->
            <!-- <div style="margin-left: 20px">
              <a
                style="
                  text-align: right;
                  vertical-align: middle;
                  font-size: 14px;
                  color: #606266;
                  line-height: 40px;
                  padding: 0 12px 0 0;
                  box-sizing: border-box;
                "
                >选择登录角色：</a
              >
              <el-radio v-model="radio" :label="0">管理员</el-radio>
              <el-radio v-model="radio" :label="2" disabled>顾客</el-radio>
            </div> -->

            <!-- 表单 -->
            <el-form-item label="用户名：" prop="username">
              <el-input v-model="ruleForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码：" prop="password">
              <el-input
                  type="password"
                  v-model="ruleForm.password"
                  autocomplete="off"
              ></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="SubmitForm('ruleForm')"
              >登录
              </el-button
              >
              <el-button @click="resetForm('ruleForm')">重置</el-button>
              <!-- <el-button
                @click="Checker()"
                v-if="this.$route.query.strongcheck === false"
                >弱登录校验</el-button
              >
              <el-button
                @click="Checker()"
                v-if="this.$route.query.strongcheck === true"
                >强登录校验</el-button
              > -->
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-main>
  </div>
</template>

<script>
import axios from "axios";
import Cookies from "js-cookie";
import myMenuBar from "../components/myMenuBar.vue";

export default {
  components: {
    myMenuBar,
  },
  data() {
    /*登录界面数据检查，相关规则赋值在mounted函数前面*/
    var checkName = (rule, value, callback) => {
      let nameReg = /^\w{1,10}$/;
      if (!nameReg.test(value)) {
        callback(new Error("账号或用户名长度应在1至10位之间"));
      }
      callback();
    };
    var checkPassword = (rule, value, callback) => {
      var checkPasswordReg = /^\w{3,50}$/;
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

    return {
      loginData: {
       id: 0,
        username: "null",
      },
      bulbPic: require("../assets/off.png"),
      radio: 0,
      strongcheck: true,
      ruleForm: {
        role: 0,
        username: "",
        password: "",
      },
      rules: {
        username: [{validator: checkName, trigger: "blur"}],
        password: [{validator: checkPassword, trigger: "blur"}],
      },
    };
  },
  methods: {
    /*获取登录信息*/
    getLoginInfo(loginData) {
      this.loginData.isLogin = loginData.isLogin;
      this.loginData.username = loginData.username;
      this.loginData.role = loginData.role;
      console.log("LoginVue Get LoginData: ↓↓");
      console.log(this.loginData);
      this.loginData.isLogin = true;
    },
    /*灯泡动画*/
    bulbOn() {
      console.log("Bulb Turned On");
      this.bulbPic = require("../assets/on.png");
    },
    bulbOff() {
      console.log("bulb is off");
      this.bulbPic = require("../assets/off.png");
    },
    SubmitForm(formName) {
      var outside = this;
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$message({
            type: "info",
            message: "登陆中",
            duration: 1200,
          });
          /*后端数据登录接口*/
          var url = outside.GLOBAL.networkSrc + "/login";
          var jsonLoginMsg = JSON.stringify(this.ruleForm);
          var config = {
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Origin": "*",
            },
          };
          console.log(jsonLoginMsg);
          axios
              .post(url, jsonLoginMsg, config)
              .then((result) => {
                console.log(result);
                var jsonBack = result.data;
                console.log(jsonBack);
                if (jsonBack.code == 0) {
                  /* code=0 错误 */
                  this.$message.error(jsonBack.message);
                } else if (jsonBack.code == 1) {
                  /* code=1  正常登录*/
                  this.$message({
                    type: "success",
                    message: "登录成功!",
                    duration: 1200,
                  });
                  Cookies.set("token", jsonBack.data, 60 * 60 * 12);
                  setTimeout(function () {
                    outside.$router.push('/');
                  }, 1000);
                } else {
                  this.$message.error("出错了，请联系后台管理人员");
                }
              })
              .catch((error) => {
                console.log(error);
                this.$message.error("出错了，请联系后台管理人员");
              });
        } else {
          alert("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    Checker() { /*密码校验待优化*/
      this.check = !this.check;
      this.$router.push({
        path: "/login",
        query: {
          strongcheck: this.check,
        },
      });
    },
  },
  created() {
  },
  mounted() {
  },
};
</script>

<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
.word {
  font-family: "JetBrains Mono ExtraBold";
  text-align: left;
}
</style>