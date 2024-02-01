<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-main>
      <!--按钮及连带的对话框-->
      <!--按钮样式-->
      <div style="float: right">
        <el-button
          icon="el-icon-plus"
          circle
          title="添加部门"
          :disabled.sync="disableAdd"
          @click="addADDialogVisible = true"
        ></el-button>
      </div>
      <!--对话框内部-->
      <el-dialog title="添加首页广告" :visible.sync="addADDialogVisible">
        <el-form :model="addADData">
          <el-form-item label="广告点击跳转链接" label-width="200px">
            <el-input v-model="addADData.url" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="广告介绍" label-width="200px">
            <el-input v-model="addADData.intro" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addADDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="AddAD()">确 定</el-button>
        </div>
      </el-dialog>

      <!--主体表格内容-->
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column label="编号" width="80" type="index">
        </el-table-column>
        <el-table-column prop="pic" label="广告图片" width="150">
          <template slot-scope="scope">
            <el-avatar
              shape="square"
              :size="100"
              fit="cover"
              :src="scope.row.img"
            ></el-avatar>
            <!-- <img :src="scope.row.img" style="width: 180" /> -->
          </template>
        </el-table-column>
        <el-table-column prop="url" label="跳转链接" width="250">
        </el-table-column>
        <el-table-column prop="intro" label="介绍" width="250">
        </el-table-column>
        <el-table-column label="操作">
          <template v-slot="scope">
            <!--修改按钮-->
            <el-button
              icon="el-icon-edit"
              circle
              title="修改"
              @click="ShowByRow(scope.$index)"
            ></el-button>
            <!--修改界面-->
            <el-dialog title="广告信息" :visible.sync="reviseADDialogVisible">
              <el-form :model="reshowTableData">
                <el-form-item label="广告图片" label-width="120px">
                  <el-upload
                    class="avatar-uploader"
                    action="'this.GLOBAL.networkSrc+'+'/uploadAvatar'"
                    :show-file-list="false"
                    :auto-upload="false"
                    :on-change="AvatarChange"
                  >
                    <el-avatar
                      v-if="reviseADPic"
                      shape="square"
                      :size="200"
                      fit="cover"
                      :src="reviseADPic"
                    ></el-avatar>
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                  </el-upload>
                </el-form-item>
                <el-form-item label="跳转链接" label-width="120px">
                  <el-input
                    v-model="reshowTableData.url"
                    autocomplete="off"
                    disabled
                  ></el-input>
                </el-form-item>
                <el-form-item label="介绍" label-width="120px">
                  <el-input
                    v-model="reshowTableData.intro"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="reviseADDialogVisible = false"
                  >取 消</el-button
                >
                <el-button type="primary" @click="UpdateByRow()"
                  >确 定</el-button
                >
              </div>
            </el-dialog>

            <!--删除按钮-->
            <el-button
              icon="el-icon-delete"
              circle
              title="删除"
              @click="RemoveByRow(scope.$index)"
              :visible.sync="delADDialogVisible"
            ></el-button>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="最后修改时间" width="200">
        </el-table-column>
      </el-table>
    </el-main>
  </div>
</template>
      
      
      
<script>
import axios from "axios";
import Cookies from "js-cookie";
import myMenuBarVue from "../../components/myMenuBar.vue";
axios.defaults.withCredentials = true;

export default {
  components: {
    myMenuBarVue,
  },
  data() {
    return {
      loginData: {
        isLogin: false,
        role: "user",
        username: "null",
      },
      disableAdd: false,
      tableData: [] /*表格数据，需要一定是一个数组*/,
      /*二级弹窗窗口显示设置：增删改部门*/
      addADDialogVisible: false,
      delADDialogVisible: false,
      reviseADDialogVisible: false,
      /*二级弹窗窗口内部数据：增改部门*/
      addADData: {
        pic: "",
        url: "",
        intro: "",
      },
      reshowTableData: {},
      reviseADPic: "",
      adFile: "",
      picTrigger: false,
    };
  },
  methods: {
    /*获取登录信息后，填充表格信息*/
    getLoginInfo(loginData) {
      this.loginData.isLogin = loginData.isLogin;
      this.loginData.username = loginData.username;
      this.loginData.role = loginData.role;
      console.log("ADVue Get Data After Set: ↓↓");
      console.log(this.loginData);
      this.getTableData();
    },
    /*获取部门表格信息*/
    getTableData() {
      var state = this.loginData.isLogin;
      var outside = this;
      var serverResponse;
      if (state == true) {
        let logUrl = this.GLOBAL.networkSrc + "/AD";
        var config = {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            Token: Cookies.get("token"),
          },
        };
        axios
          .get(logUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Product Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              outside.tableData = serverResponse.data;
              outside.tableData.forEach((item) => {
                if (item.updateTime != null) {
                  item.updateTime = item.updateTime.replace("T", " ");
                }
              });
              if (outside.tableData.length >= 6) {
                outside.disableAdd = true;
              }
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
    },
    /*添加部门*/
    AddAD() {
      this.picTrigger = false;
      if (this.addADData.intro == "") {
        this.$message({
          type: "error",
          message: "广告介绍不得为空！",
          duration: 2000,
        });
        return;
      }
      var serverResponse;
      var addData = this.addADData;
      let url = this.GLOBAL.networkSrc + "/addAD";
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      console.log(addData);
      axios
        .post(url, addData, config)
        .then((result) => {
          serverResponse = JSON.parse(result.request.response);
          if (serverResponse.code == 1) {
            this.$message({
              type: "success",
              message: "增加首页广告成功!",
              duration: 2000,
            });
            /*成功：清空缓存*/
            this.addADData.pic = "";
            this.addADData.intro = "";
            this.addADData.url = "";
          } else {
            console.log(serverResponse.message);
          }
        })
        .catch((error) => {
          this.$message({
            type: "error",
            message: error.name,
            duration: 2000,
          });
          console.log("出错了，请联系后台管理人员" + error);
        });
      this.addADDialogVisible = false;
      setTimeout(() => {
        location.reload();
      }, 2200);
    },
    /*修改部门信息：1.复显；2.提交表单*/
    ShowByRow(index) {
      //1.复显信息单独赋值，添加index信息
      var reshow = this.tableData[index];
      this.reshowTableData.id = reshow.id;
      this.reshowTableData.pic = reshow.pic;
      this.reshowTableData.url = reshow.url;
      this.reshowTableData.intro = reshow.intro;
      this.reshowTableData.updateTime = reshow.updateTime;
      this.reviseADDialogVisible = true;
      this.reshowTableData = JSON.parse(JSON.stringify(this.reshowTableData));
      console.log("Page Show ↓↓");
      console.log(this.reshowTableData);
    },
    UpdateByRow() {
      //2.提交表单
      var index = this.reshowTableData.index;
      if (
        !(
          /*比较部门名称和部门简介字段是否相等 */
          (
            !this.picTrigger &&
            this.reshowTableData.url === this.tableData[index].url &&
            this.reshowTableData.intro === this.tableData[index].intro
          )
        )
      ) {
        var serverResponse;
        var updateData = this.reshowTableData;
        let url = this.GLOBAL.networkSrc + "/updateAD";
        var config = {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            Token: Cookies.get("token"),
          },
        };
        if (this.picTrigger) {
          this.AvatarUpload();
        }
        axios
          .put(url, updateData, config)
          .then((result) => {
            serverResponse = JSON.parse(result.request.response);
            if (serverResponse.code == 1) {
              this.$message({
                type: "success",
                message: "修改成功!",
                duration: 2000,
              });
              this.tableData[index] = updateData;
              console.log("更新操作成功");
            } else {
              this.$message({
                type: "error",
                message: "修改失败",
                duration: 2000,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
        this.reviseADDialogVisible = false;
      } else {
        this.$message({
          type: "info",
          message: "未修改信息",
          duration: 2000,
        });
        this.reviseADDialogVisible = false;
      }
      setTimeout(() => {
        location.reload();
      }, 2200);
    },
    /*移除部门*/
    RemoveByRow(index) {
      this.$confirm("此操作将永久删除该条目，是否继续？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          var serverResponse;
          var deleteId = this.tableData[index].id;
          let url = this.GLOBAL.networkSrc + "/deleteAD/" + deleteId;
          var config = {
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Origin": "*",
              Token: Cookies.get("token"),
            },
          };
          axios
            .delete(url, config)
            .then((result) => {
              serverResponse = JSON.parse(result.request.response);
              if (serverResponse.code == 1) {
                this.$message({
                  type: "success",
                  message: "删除成功!",
                  duration: 2000,
                });
              } else {
                this.$message({
                  type: "error",
                  message: serverResponse.message,
                  duration: 2000,
                });
              }
            })
            .catch((error) => {
              this.$message({
                type: "error",
                message: error.name,
                duration: 2000,
              });
              console.log("出错了，请联系后台管理人员" + error);
            });
          this.reviseADDialogVisible = false;
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
      setTimeout(() => {
        location.reload();
      }, 2200);
    },
    AvatarChange(file) {
      this.picTrigger = true;
      const isJPG = file.raw.type === "image/jpeg";
      const isPNG = file.raw.type === "image/png";
      if (!isJPG && !isPNG) {
        this.$message({
          type: "error",
          message: "上传的头像图片仅允许为JPG或PNG格式！",
          duration: 2000,
        });
      } else {
        this.reviseADPic = URL.createObjectURL(file.raw);
        this.adFile = file.raw;
      }
    },
    AvatarUpload() {
      let adId = this.reshowTableData.id;
      let formData = new FormData();
      formData.append("file", this.adFile);

      var serverResponse;
      let url = this.GLOBAL.networkSrc + "/uploadAD/" + adId;
      var config = {
        headers: {
          "Content-Type": "multipart/form-data",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      axios
        .patch(url, formData, config)
        .then((result) => {
          serverResponse = JSON.parse(result.request.response);
          if (serverResponse.code == 1) {
            this.picTrigger = false;
            console.log("图片上传成功" + this.reviseEmpAvatar);
          } else {
            this.$message({
              type: "error",
              message: "图片上传失败",
              duration: 2000,
            });
            console.log("图片上传失败" + serverResponse.message);
          }
        })
        .catch((error) => {
          console.log("图片上传出错了，请联系后台管理人员" + error);
        });
    },
  },
  mounted() {
    this.$notify({
      title: "提示",
      message: "本页尚未开发完毕，暂未实现首页广告与后端联调功能",
      duration: 5000,
    });
  },
};
</script>
      
      
      
<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
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
    