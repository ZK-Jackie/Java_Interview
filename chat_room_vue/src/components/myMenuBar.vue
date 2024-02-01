<template>
  <div class="menu">
    <el-header>
      <el-menu
        class="el-menu-demo"
        mode="horizontal"
        :default-active="selectBar()"
        style="
          box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
          size: auto;
          border-radius: 25px;
        "
      >
        <!-- 主菜单图片及按钮 -->
        <div class="main-logo">
          <img src="../assets/hidrinkLOGO.svg" width="70" align="left" />
        </div>
        <el-menu-item index="0" class="Home" @click="Jump('/')"
          >HiDRINK HOME</el-menu-item
        >

        <!--菜单左侧选项卡------------------------------------>

        <!-- 聊天室入口 -->
        <el-menu-item index="1" v-if="userInfo.id != 0" @click="Jump('/room')"
          >聊天室</el-menu-item
        >

        <!--菜单右侧按钮------------------------------------>
        <!-- 未登录时，登录/注册按钮 -->
        <div
          style="
            align-items: end;
            margin-top: 13px;
            margin-right: 18px;
            float: right;
          "
          v-if="userInfo.id == 0"
        >
          <el-button type="primary" :key="1" round v-on:click="Jump('/login')"
            >登录</el-button
          >
          <el-button type="primary" :key="2" round @click="Jump('/register')"
            >注册</el-button
          >
        </div>

        <!-- 登录后，个人中心及登出按钮 -->
        <div
          style="
            align-items: end;
            margin-top: 13px;
            margin-right: 18px;
            float: right;
          "
          v-if="userInfo.id != 0"
        >
          <el-button round :key="3" :disabled="true" @click="Jump('/user')"
            >{{ userInfo.username }}，您好！</el-button
          >
          <el-button type="primary" :key="4" round @click="UserLogout()">登出</el-button>
        </div>
      </el-menu>
    </el-header>
  </div>
</template>

<script>
import axios from "axios";
import Cookies from "js-cookie";

export default {
  name: "myMenuBarVue",
  data() {
    return {
      token: "",
      userInfo: {
        id: 0,
        username: "user",
      },
    };
  },
  methods: {
    //共同页面跳转
    Jump(place) {
      if (this.$route.path == place) {
        location.reload();
      } else {
        this.$router.push(place);
      }
    },
    UserLogout() {
      var outside = this;
      this.$confirm("是否登出？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          Cookies.remove("token");
          this.$message({
            type: "success",
            message: "已成功登出!",
            duration: 1200,
          });
          setTimeout(function () {
            outside.Jump("/login");
          }, 1500);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消注销操作",
            duration: 1500,
          });
        });
    },
    getUserInfo() {
      return this.userInfo;
    },
    selectBar() {
      switch (this.$route.path) {
        case "/room":
          return "1";
        default:
          return null;
      }
    },
  },
  mounted() {
    //1.获取必要cookie信息
    var outside = this; //Inspired by ChungmingWu in ZhiHu
    var cookieToken = Cookies.get("token");
    //2.初步检查cookieToken是否存在
    if(cookieToken == undefined){
      console.log("用户未登录");
      outside.$emit("transfer", outside.userInfo);
      return;
    }
    //3.向服务器发送token，检查是否登录
    var tokenJson = {
      token: cookieToken,
    };
    var serverResponse;
    console.log("Send to Server:" + cookieToken);
    let checkLoginUrl = this.GLOBAL.networkSrc + "/checkState";
    var config = {
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    };
    axios
      .post(checkLoginUrl, tokenJson, config)
      .then(function (result) {
        serverResponse = JSON.parse(result.request.response);
        console.log("Get Check Login Response:");
        console.log(serverResponse.data);
        if (serverResponse.code == 1) {
          let serverData = serverResponse.data;
          outside.userInfo.id = serverData.userId;
          outside.userInfo.username = serverData.username;
          outside.$emit("transfer", outside.userInfo); //该emit只能置于axios中以便及时更新数值
        } else {
          outside.$emit("transfer", outside.userInfo); //该emit只能置于axios中以便及时更新数值
          console.log("用户未登录，不显示任何按钮");
        }
      })
      .catch((error) => {
        this.$notify({
          title: "提示",
          message: "服务器维护中，暂无法使用系统",
          duration: 0,
        });
        outside.$emit("transfer", outside.userInfo); //只能置于axios中以便及时更新数值
        console.log("出错了，请联系后台管理人员" + error);
      });
    console.log("menu bar emit ↓↓");
    console.log(outside.userInfo);
  },
};
</script>

<style>
.main-logo {
  margin-left: 22px;
}
.menu {
  margin-bottom: 20px;
}
</style>
