<template>
  <div>
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
        <div class="MainLogo">
          <img src="../assets/hidrinkLOGO.svg" width="70" align="left" />
        </div>
        <el-menu-item index="0" class="Home" @click="Jump('/')"
          >HiDRINK HOME</el-menu-item
        >

        <!--菜单左侧选项卡------------------------------------>

        <!-- admin管理员按钮 -->
        <el-menu-item
          index="1"
          v-if="userInfo.isLogin == true && userInfo.role == 'admin'"
          @click="Jump('/daily')"
          >数据日报</el-menu-item
        >
        <el-menu-item
          index="2"
          v-if="userInfo.isLogin == true && userInfo.role == 'admin'"
          @click="Jump('/log')"
          >所有日志</el-menu-item
        >
        <el-submenu
          index="3"
          v-if="userInfo.isLogin == true && userInfo.role == 'admin'"
        >
          <template slot="title">人员管理</template>
          <el-menu-item index="3-1" @click="Jump('/dept')"
            >部门管理</el-menu-item
          >
          <el-menu-item index="3-2" @click="Jump('/emp')"
            >员工管理</el-menu-item
          >
          <el-menu-item index="3-3" @click="Jump('/product')"
            >商品管理</el-menu-item
          >
          <el-menu-item index="3-4" @click="Jump('/cus')" disabled
            >顾客管理</el-menu-item
          >
        </el-submenu>

        <!-- emp门店员工按钮 -->
        <el-menu-item
          index="1"
          v-if="userInfo.isLogin == true && userInfo.role == 'emp'"
          @click="Jump('/order')"
          >门店订单</el-menu-item
        >
        <el-menu-item
          index="2"
          v-if="userInfo.isLogin == true && userInfo.role == 'emp'"
          @click="Jump('/member')"
          >门店会员</el-menu-item
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
          v-if="userInfo.isLogin === false"
        >
          <el-button type="primary" round v-on:click="Jump('/login')"
            >登录</el-button
          >
          <el-button type="primary" round @click="Jump('/register')"
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
          v-if="userInfo.isLogin === true"
        >
          <el-button round v-on:click="Jump('/user')" disabled
            >{{ userInfo.username }}，您好！</el-button
          >
          <el-button type="primary" round @click="UserLogout()">登出</el-button>
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
        role: -1,
        username: "user",
        isLogin: false,
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
        //管理员地点：
        case "/daily":
          return "1";
        case "/log":
          return "2";
        case "/emp":
          return "3-2";
        case "/dept":
          return "3-1";
        case "/product":
          return "3-3";
        case "/cus":
          return "3-4";
        //员工地点：
        case "/order":
          return "1";
        case "/member":
          return "2";
        default:
          return null;
      }
    },
  },
  mounted() {
    var outside = this; //Inspired by ChungmingWu in ZhiHu
    const roleTable = ["admin", "emp", "cus"];
    var cookie = Cookies.get("token");
    var tokenJson = {
      token: cookie,
    };
    var serverResponse;
    console.log("Send to Server:" + cookie);
    let url = this.GLOBAL.networkSrc + "/checkState";
    var config = {
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "*",
      },
    };
    axios
      .post(url, tokenJson, config)
      .then(function (result) {
        serverResponse = JSON.parse(result.request.response);
        console.log("Get Response ↓↓");
        console.log(serverResponse.data);
        if (serverResponse.code == 1) {
          let serverData = serverResponse.data;
          outside.userInfo.role = roleTable[serverData.role];
          outside.userInfo.username = serverData.username;
          outside.userInfo.isLogin = serverData.isSuccess;
          outside.$emit("transfer", outside.userInfo); //只能置于axios中以便及时更新数值
        } else {
          outside.$emit("transfer", outside.userInfo); //只能置于axios中以便及时更新数值
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
.MainLogo {
  margin-left: 22px;
}
</style>
    