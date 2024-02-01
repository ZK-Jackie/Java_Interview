<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-main>
      <!-- 中央表单 -->
      <el-form
        :model="tableData"
        status-icon
        :rules="rules"
        ref="tableData"
        label-width="100px"
        style="width: 80%; height: auto"
      >
        <!--管理员情况下：-->
        <div v-if="loginData.role === 'admin'">
          <el-form-item label="ID" prop="id">
            <el-input v-model="tableData.id" disabled> </el-input>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="tableData.name" disabled> </el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="tableData.username" disabled> </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              type="password"
              v-model="tableData.password"
              autocomplete="off"
              disabled
            ></el-input>
          </el-form-item>
        </div>
        <!--员工情况下：-->
        <div v-if="loginData.role === 'emp'">
          <el-form-item label="头像" prop="url">
            <div class="block">
              <el-upload
                class="avatar-uploader"
                action="'this.GLOBAL.networkSrc'+'/#/upload'"
                :show-file-list="false"
                :auto-upload="false"
                disabled
              >
                <img v-if="tableData.img" :src="tableData.img" class="avatar" />
                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
              </el-upload>
            </div>
          </el-form-item>
          <el-form-item label="ID" prop="id">
            <el-input v-model="tableData.id" disabled> </el-input>
          </el-form-item>
          <el-form-item label="用户名" prop="username">
            <el-input v-model="tableData.username" disabled> </el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input
              type="password"
              v-model="tableData.password"
              autocomplete="off"
              disabled
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="tableData.gender">
              <el-radio :label="0" disabled> 男 </el-radio>
              <el-radio :label="1" disabled> 女 </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="入职时间" prop="entryTime">
            <el-input v-model.number="tableData.entryTime" disabled></el-input>
          </el-form-item>
          <el-form-item label="就职部门" prop="deptId">
            <el-radio-group v-model="tableData.deptId">
              <el-radio
                v-for="deptID in deptIds"
                :key="deptID"
                :label="deptID"
                disabled
              >
                {{ depts[deptID] }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </div>
        <!--无论哪种情况下都存在的：-->
        <el-form-item>
          <el-button type="primary" :disabled="true">保存</el-button>
        </el-form-item>
      </el-form>
    </el-main>
  </div>
</template>
  
  
  
<script>
import axios from "axios";
import Cookies from "js-cookie";
import myMenuBarVue from "../components/myMenuBar.vue";

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
      let checkPasswordReg =
        /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9]{3,50}$/;
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
        isLogin: false,
        role: "user",
        username: "null",
      },
      tableData: {
        username: "",
        password: "",
        img: "",
        age: "",
        gender: "",
        deptId: -1,
      },
      rules: {
        username: [{ required: true, validator: checkName, trigger: "blur" }],
        password: [
          { required: true, validator: checkPassword, trigger: "blur" },
        ],
        age: [{ required: false, validator: checkAge, trigger: "blur" }],
        gender: [{ required: true, trigger: "change" }],
      },
      depts: {}, //部门列表
      deptIds: [],
    };
  },
  methods: {
    async getLoginInfo(loginData) {
      // console.log(loginData);
      // this.loginData.isLogin = await loginData.isLogin;
      // console.log(1);
      // this.loginData.username = await loginData.username;
      // console.log(2);
      // this.loginData.role = await loginData.role;
      // console.log(3);
      this.loginData = loginData;
      console.log("DeptVue Get Data After Set: ↓↓");
      console.log(this.loginData);
      this.getTableData();
    },
    getTableData() {
      var state = this.loginData.isLogin;
      var outside = this;
      var serverResponse;
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      /* 1.获取部门列表信息*/
      if (state == true) {
        let deptUrl = this.GLOBAL.networkSrc+"/deptList";
        axios
          .get(deptUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Department Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code === 1) {
              outside.depts = serverResponse.data;
              outside.deptIds = Object.keys(outside.depts);
              outside.deptIds = outside.deptIds.map(Number);
              console.log(outside.deptIds);
            } else {
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
      } else {
        outside.$message({
          type: "error",
          message: "请先登录！",
          duration: 1500,
        });
        setTimeout(() => {
          outside.$router.push("/login");
        }, 1800);
      }
      /*2.获取个人信息  带上id*/
      if (state == true) {
        let userUrl = this.GLOBAL.networkSrc+"/user" + outside.loginData.role;
        axios
          .post(userUrl, {}, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              outside.tableData = serverResponse.data;
            } else {
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
      } else {
        this.$message({
          type: "error",
          message: serverResponse.message,
          duration: 1500,
        });
        setTimeout(() => {
          this.$router.push("/login");
        }, 1800);
      }
    },
    handleAvatarSuccess(response, file) {
      console.log(response);
      this.url = URL.createObjectURL(file.raw);
    },
    handleFailed(response) {
      console.log(response);
      this.$message.error("上传失败，请联系管理员！");
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === "image/jpeg";
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 2MB!");
      }
      //返回上传正确与否
      return isJPG && isLt2M;
    },
  },
  mounted() {},
};
</script>
  
  
  
  <style>
.MainLogo {
  margin-left: 22px;
}
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
.HomeButton {
  font-family: "Jetbrains Mono bold";
  text-align: left;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
