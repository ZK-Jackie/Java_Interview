<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-main>
      <!--按钮及连带的对话框-->
      <!--员工的条件筛选查询-->
      <div style="float: left">
        <el-form :inline="true">
          <el-form-item label="员工姓名" style="float: left">
            <el-input
              v-model="pageInfo.name"
              placeholder="输入员工姓名查询"
            ></el-input>
          </el-form-item>

          <el-form-item label="入职日期">
            <el-col :span="11">
              <el-form-item>
                <el-date-picker
                  type="date"
                  placeholder="选择日期"
                  v-model="pageInfo.startDate"
                  style="width: 100%"
                  value-format="yyyy-MM-dd"
                ></el-date-picker>
              </el-form-item>
            </el-col>
            <div style="padding: 0; margin-right: 10px; float: left">——</div>
            <el-col :span="11">
              <el-date-picker
                type="date"
                placeholder="选择日期"
                v-model="pageInfo.endDate"
                style="width: 100%"
                value-format="yyyy-MM-dd"
              ></el-date-picker>
            </el-col>
          </el-form-item>
          <!-- <el-form-item label="性别"
            style="float: left;"
          >
            <el-select v-model="pageInfo.gender" placeholder="性别筛选"
            style="width: 30%; float: left"
            >
              <el-option label="男" value="0"></el-option>
              <el-option label="女" value="1"></el-option>
            </el-select>
          </el-form-item> -->
          <el-form-item>
            <el-button type="primary" @click="Search">查询</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div style="float: right">
        <el-button
          icon="el-icon-plus"
          circle
          title="添加员工"
          @click="addEmpDialogVisible = true"
        ></el-button>
      </div>

      <!--添加员工对话框内部-->
      <el-dialog
        title="添加员工"
        :visible.sync="addEmpDialogVisible"
        top="60px"
      >
        <el-form :model="addEmpData">
          <el-form-item
            label="员工登录用户名"
            label-width="120px"
            :required="true"
          >
            <el-input
              v-model="addEmpData.username"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="员工登录密码"
            label-width="120px"
            :required="true"
          >
            <el-input
              v-model="addEmpData.password"
              autocomplete="off"
            ></el-input>
          </el-form-item>
          <el-form-item
            label="员工就职部门"
            label-width="120px"
            :required="true"
          >
            <el-radio-group v-model="addEmpData.deptId">
              <el-radio
                v-for="deptID in this.deptIds"
                :key="deptID"
                :label="deptID"
              >
                {{ depts[deptID] }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="入职日期" label-width="120px" :required="true">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              v-model="addEmpData.entryTime"
              style="width: 100%"
              value-format="yyyy-MM-dd"
            ></el-date-picker>
          </el-form-item>
          <el-form-item label="性别" label-width="120px" :required="true">
            <el-radio-group v-model="addEmpData.gender">
              <el-radio :label="0"> 男 </el-radio>
              <el-radio :label="1"> 女 </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addEmpDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="AddEmp()">确 定</el-button>
        </div>
      </el-dialog>

      <!--主体表格内容-->
      <el-table :data="pageInfo.tableData" stripe style="width: 100%">
        <el-table-column width="80" type="selection" :selectable="Selection"></el-table-column>
        <el-table-column label="序号" width="80" type="index">
          <template slot-scope="row">
            <span>{{
              row.$index + 1 + (pageInfo.pageNow - 1) * pageInfo.pageSize
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="username"
          label="员工姓名（用户名）"
          width="150"
        ></el-table-column>
        <el-table-column prop="password" label="密码" width="150">
        </el-table-column>
        <el-table-column prop="img" label="头像" width="150">
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
        <el-table-column prop="deptId" label="就职部门" width="120">
          <template slot-scope="scope">
            <div>
              {{ depts[scope.row.deptId] }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="entryTime" label="入职日期" width="150">
        </el-table-column>
        <el-table-column prop="gender" label="性别" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.gender == 0"> 男 </span>
            <span v-else> 女 </span>
          </template>
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
            <el-dialog
              title="员工信息"
              :visible.sync="reviseEmpDialogVisible"
              top="20px"
            >
              <el-form :model="reshowTableData">
                <el-form-item label="头像" label-width="150px">
                  <div class="block">
                    <el-upload
                      class="avatar-uploader"
                      action="'this.GLOBAL.networkSrc'+'/uploadAvatar'"
                      :show-file-list="false"
                      :auto-upload="false"
                      :on-change="AvatarChange"
                    >
                      <!-- <img
                        v-if="reviseEmpAvatar"
                        :src="reviseEmpAvatar"
                        class="avatar"
                      /> -->
                      <el-avatar
                        v-if="reviseEmpAvatar"
                        shape="square"
                        :size="100"
                        fit="cover"
                        :src="reviseEmpAvatar"
                      ></el-avatar>
                      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                  </div>
                </el-form-item>

                <el-form-item label="员工姓名（用户名）" label-width="150px">
                  <el-input
                    v-model="reshowTableData.username"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
                <el-form-item label="员工登录密码" label-width="150px">
                  <el-input
                    v-model="reshowTableData.password"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
                <el-form-item label="员工就职部门" label-width="150px">
                  <el-radio-group v-model="reshowTableData.deptId">
                    <el-radio
                      v-for="deptID in deptIds"
                      :key="deptID"
                      :label="deptID"
                    >
                      {{ depts[deptID] }}
                    </el-radio>
                  </el-radio-group>
                </el-form-item>
                <el-form-item label="员工入职日期" label-width="150px">
                  <el-date-picker
                    type="date"
                    placeholder="选择日期"
                    v-model="reshowTableData.entryTime"
                    style="width: 100%"
                    value-format="yyyy-MM-dd"
                  ></el-date-picker>
                </el-form-item>
                <el-form-item label="性别" label-width="150px">
                  <el-radio-group v-model="reshowTableData.gender">
                    <el-radio :label="0"> 男 </el-radio>
                    <el-radio :label="1"> 女 </el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="reviseEmpDialogVisible = false"
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
              :visible.sync="delEmpDialogVisible"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>

      <!--分页条-->
      <div class="block">
        <el-pagination
          @size-change="pageSizeChange"
          @current-change="pageChange"
          :current-page="pageInfo.pageNow"
          :page-sizes="[5, 10, 20]"
          :page-size="pageInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pageInfo.totalSize"
          :hide-on-single-page="true"
        >
        </el-pagination>
      </div>
    </el-main>
  </div>
</template>
      
      
      
<script>
import axios from "axios";
import Cookies from "js-cookie";
import myMenuBarVue from "../../components/myMenuBar.vue";
axios.defaults.withCredentials = true;
const qs = require("qs");

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
      /*二级弹窗窗口显示设置：增删改员工*/
      addEmpDialogVisible: false,
      delEmpDialogVisible: false,
      reviseEmpDialogVisible: false,
      /*二级弹窗窗口内部数据：增改员工*/
      addEmpData: {
        name: "",
        username: "",
        password: "",
        entryTime: "",
        gender: "",
      },
      reshowTableData: {}, //修改数据复显
      pageInfo: {
        /*当前页面总数据*/
        totalSize: 0,
        pageSize: 5,
        pageNow: 1,
        tableData: [] /*表格数据，需要一定是一个数组*/,
        name: "",
        startDate: "",
        endDate: "",
      },
      reviseEmpAvatar: "" /*临时储存修改后的头像的地址*/,
      avatarFile: "" /*临时储存头像文件*/,
      isAvatarTrigger: false /*记录用户是否修改了头像*/,
      depts: {}, //部门列表
      deptIds: [],
    };
  },
  methods: {
    /*获取登录信息后，填充表格信息*/
    getLoginInfo(loginData) {
      this.loginData.isLogin = loginData.isLogin;
      this.loginData.username = loginData.username;
      this.loginData.role = loginData.role;
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
      /* 1.获取部门列表信息，用于deptId和部门名称一一对应*/
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
              outside.$message({
                type: "error",
                message: serverResponse.message,
                duration: 1500,
              });
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            outside.$message({
              type: "error",
              message: error,
              duration: 1500,
            });
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

      /* 2.获取员工页面信息 */
      if (state == true) {
        this.pageInfo.tableData = [];
        let empUrl = this.GLOBAL.networkSrc+"/emp?" + qs.stringify(this.pageInfo);
        axios
          .get(empUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Employee Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              outside.pageInfo = serverResponse.data;
              outside.pageInfo.tableData.forEach((item) => {
                if (item.entryTime != null) {
                  item.entryTime = item.entryTime.substr(0, 10);
                }
              });
            } else {
              outside.$message({
                type: "error",
                message: serverResponse.message,
                duration: 2000,
              });
              console.log(serverResponse.message);
              return;
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
    /*添加员工*/
    AddEmp() {
      var serverResponse;
      var addData = this.addEmpData;
      //解决日期问题：
      addData.entryTime += 'T00:00:00';
      let url = this.GLOBAL.networkSrc+"/addEmp";
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      console.log("Add Emp ↓↓");
      console.log(addData);
      axios
        .put(url, addData, config)
        .then((result) => {
          serverResponse = JSON.parse(result.request.response);
          if (serverResponse.code == 1) {
            this.$message({
              type: "success",
              message: "增加员工成功!",
              duration: 2000,
            });
            /*成功：清空缓存*/
            this.addEmpData.name = "";
            this.addEmpData.username = "";
            this.addEmpData.password = "";
            this.addEmpData.entryTime = "";
            this.addEmpData.gender = "";
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
      this.addEmpDialogVisible = false;
      setTimeout(() => {
        location.reload();
      }, 2200);
    },

    /*修改部门信息：1.复显；2.提交表单*/
    ShowByRow(index) {
      //1.复显信息单独赋值，添加index信息
      var reshow = this.pageInfo.tableData[index];
      this.reshowTableData.id = reshow.id;
      this.reshowTableData.img = reshow.img;
      this.reshowTableData.username = reshow.username;
      this.reshowTableData.password = reshow.password;
      this.reshowTableData.entryTime = reshow.entryTime;
      this.reshowTableData.gender = reshow.gender;
      this.reshowTableData.deptId = reshow.deptId;
      this.reshowTableData.index = index;
      this.reshowTableData = JSON.parse(JSON.stringify(this.reshowTableData));
      console.log("Page Show ↓↓");
      console.log(this.reshowTableData);
      this.reviseEmpDialogVisible = true;
    },
    UpdateByRow() {
      //2.提交表单
      var index = this.reshowTableData.index;
      if (
        /*比较部门名称和部门简介字段是否相等 */
        !(
          !this.isAvatarTrigger &&
          this.reshowTableData.username ===
            this.pageInfo.tableData[index].username &&
          this.reshowTableData.password ===
            this.pageInfo.tableData[index].password &&
          this.reshowTableData.entryTime ==
            this.pageInfo.tableData[index].entryTime &&
          this.reshowTableData.gender ===
            this.pageInfo.tableData[index].gender &&
          this.reshowTableData.deptId === this.pageInfo.tableData[index].deptId
        )
      ) {
        var serverResponse;
        var updateData = this.reshowTableData;
        //处理日期转换问题：
        if(updateData.entryTime != null){
          updateData.entryTime += 'T00:00:00';
        }
        if (this.isAvatarTrigger) { //检验是否发生头像改变，若有 一同上传
          this.AvatarUpload();
        }
        let url = this.GLOBAL.networkSrc+"/updateEmp";
        var config = {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            Token: Cookies.get("token"),
          },
        };
        console.log(updateData);
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
              this.pageInfo.tableData[index] = updateData;
              console.log("信息更新操作成功");
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
            if (
              error + "" !=
              "AxiosError: Request failed with status code 400"
            ) {
              this.$message({
                type: "error",
                message: "修改失败",
                duration: 2000,
              });
              console.log("信息更新出错了，请联系后台管理人员" + error);
            } else {
              this.$message({
                type: "success",
                message: "修改成功!",
                duration: 2000,
              });
              this.pageInfo.tableData[index] = updateData;
              console.log("信息更新操作成功");
            }
          });
      } else {
        this.$message({
          type: "info",
          message: "未修改信息",
          duration: 2000,
        });
      }
      this.reviseEmpDialogVisible = false;
      this.reviseEmpAvatar = "";
      this.isAvatarTrigger = false;
      setTimeout(() => {
        location.reload();
      }, 2200);
    },
    /*移除某一员工*/
    RemoveByRow(index) {
      this.$confirm("此操作将永久删除该对象, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          var serverResponse;
          var deleteId = this.pageInfo.tableData[index].id;
          let url = this.GLOBAL.networkSrc+"/deleteEmp/" + deleteId;
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
          this.reviseDeptDialogVisible = false;
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
            duration: 1200,
          });
        });
      this.getTableData();
      setTimeout(() => {
        location.reload();
      }, 2200);
    },
    async Search() {
      /*必须等页码完成初始化、日期比较后才能重载页面内容*/
      await function () {
        this.pageInfo.pageNow = 1;
        return 1;
      };
      this.pageInfo.pageNow = 1;
      await this.getTableData();
    },
    AvatarChange(file) {
      this.isAvatarTrigger = true;
      const isJPG = file.raw.type === "image/jpeg";
      const isPNG = file.raw.type === "image/png";
      if (!isJPG && !isPNG) {
        this.$message({
          type: "error",
          message: "上传的头像图片仅允许为JPG或PNG格式！",
          duration: 2000,
        });
      } else {
        this.reviseEmpAvatar = URL.createObjectURL(file.raw);
        this.avatarFile = file.raw;
      }
    },
    pageSizeChange(newSize) {
      this.pageInfo.pageSize = newSize;
      this.getTableData();
    },
    pageChange(newPageNow) {
      this.pageInfo.pageNow = newPageNow;
      this.getTableData();
    },
    AvatarUpload() {
      let userId = this.reshowTableData.id;
      let formData = new FormData();
      formData.append("file", this.avatarFile);

      var serverResponse;
      let url = this.GLOBAL.networkSrc+"/upload/" + userId;
      var config = {
        headers: {
          "Content-Type": "multipart/form-data",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      axios
        .post(url, formData, config)
        .then((result) => {
          serverResponse = JSON.parse(result.request.response);
          if (serverResponse.code == 1) {
            this.reviseEmpAvatar = serverResponse.data;
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
    Selection(){
      return false;
    }
  },
  mounted() {},
};
</script>
      
      
      
<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
.el-main {
  overflow: 0;
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
    