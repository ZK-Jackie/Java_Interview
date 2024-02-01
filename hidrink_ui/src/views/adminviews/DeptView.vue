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
          :key="deptTable"
          @click="addDeptDialogVisible = true"
        ></el-button>
      </div>
      <!--对话框内部-->
      <el-dialog title="添加部门" :visible.sync="addDeptDialogVisible">
        <el-form :model="addDeptData">
          <el-form-item label="部门名称" label-width="120px">
            <el-input v-model="addDeptData.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="部门介绍" label-width="120px">
            <el-input v-model="addDeptData.intro" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="部门成立时间" label-width="120px">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              v-model="addDeptData.establishTime"
              style="width: 100%"
              value-format="yyyy-MM-ddT00:00:00"
            ></el-date-picker>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addDeptDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="AddDept()">确 定</el-button>
        </div>
      </el-dialog>

      <!--主体表格内容-->
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column label="编号" width="80" type="index"></el-table-column>
        <el-table-column
          prop="name"
          label="部门名称"
          width="150"
        ></el-table-column>
        <el-table-column prop="empNum" label="员工数" width="100">
        </el-table-column>
        <el-table-column prop="intro" label="介绍" width="450">
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
            <el-dialog title="部门信息" :visible.sync="reviseDeptDialogVisible">
              <el-form :model="reshowTableData">
                <el-form-item label="部门名称" label-width="120px">
                  <el-input
                    v-model="reshowTableData.name"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
                <el-form-item label="部门人数" label-width="120px">
                  <el-input
                    v-model="reshowTableData.empNum"
                    autocomplete="off"
                    disabled
                  ></el-input>
                </el-form-item>
                <el-form-item label="部门成立时间" label-width="120px">
                  <el-input
                    v-model="reshowTableData.establishTime"
                    autocomplete="off"
                    disabled
                  ></el-input>
                </el-form-item>
                <el-form-item label="部门简介" label-width="120px">
                  <el-input
                    v-model="reshowTableData.intro"
                    autocomplete="off"
                  ></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="reviseDeptDialogVisible = false"
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
              :visible.sync="delDeptDialogVisible"
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
      tableData: [] /*表格数据，需要一定是一个数组*/,
      /*二级弹窗窗口显示设置：增删改部门*/
      addDeptDialogVisible: false,
      delDeptDialogVisible: false,
      reviseDeptDialogVisible: false,
      /*二级弹窗窗口内部数据：增改部门*/
      addDeptData: {
        name: "",
        intro: "",
      },
      reshowTableData: {},
      deptTable: 0 /*视图强制重载*/,
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
    /*获取部门表格信息*/
    getTableData() {
      var state = this.loginData.isLogin;
      var outside = this;
      var serverResponse;
      if (state == true) {
        let logUrl = this.GLOBAL.networkSrc+"/dept";
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
            console.log("Get Department Response ↓↓");
            console.log(serverResponse.data);
            outside.tableData = serverResponse.data;
            if (serverResponse.code == 1) {
              outside.tableData.forEach((item) => {
                if(item.updateTime != null){
                  item.updateTime = item.updateTime.replace('T',' ');
                }
              });
            } else {
              console.log(serverResponse.message);
            }
          })
          .catch((error) => {
            console.log("出错了，请联系后台管理人员" + error);
          });
      }else{
        outside.$message({
            type: "error",
            message: "请先登录！",
            duration: 1500,
          });
        setTimeout(()=>{
          outside.$router.push('/login');
        },1800)
      }
    },
    /*添加部门*/
    AddDept() {
      if(this.addDeptData.name == "" || this.addDeptData.establishTime == "" || this.addDeptData.establishTime == null){
        this.$message({
          type: "error",
          message: "部门名称和部门成立时间不得为空！",
          duration: 2000,
        });
        return ;
      }
      var serverResponse;
      var addData = this.addDeptData;
      //解决日期问题：
      // addData.establishTime += 'T00:00:00'
      let url = this.GLOBAL.networkSrc+"/addDept";
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      console.log(addData);
      axios
        .put(url, addData, config)
        .then((result) => {
          serverResponse = JSON.parse(result.request.response);
          if (serverResponse.code == 1) {
            this.$message({
              type: "success",
              message: "增加部门成功!",
              duration: 2000,
            });
            /*成功：清空缓存*/
            this.addDeptData.intro = "";
            this.addDeptData.name = "";
          } else {
            this.$message({
              type: "error",
              message: serverResponse.message,
              duration: 2000,
            });
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
      this.addDeptDialogVisible = false;
      setTimeout(() => {
       location.reload();
      }, 2200);
    },
    /*修改部门信息：1.复显；2.提交表单*/
    ShowByRow(index) {
      //1.复显信息单独赋值，添加index信息
      var reshow = this.tableData[index];
      this.reshowTableData.id = reshow.id;
      this.reshowTableData.name = reshow.name;
      this.reshowTableData.empNum = reshow.empNum;
      this.reshowTableData.establishTime = reshow.establishTime;
      this.reshowTableData.intro = reshow.intro;
      this.reshowTableData.index = index;
      this.reviseDeptDialogVisible = true;
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
            this.reshowTableData.name === this.tableData[index].name &&
            this.reshowTableData.intro === this.tableData[index].intro
          )
        )
      ) {
        var serverResponse;
        var updateData = this.reshowTableData;
        let url = this.GLOBAL.networkSrc+"/updateDept";
        var config = {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            Token: Cookies.get("token"),
          },
        };
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
              this.$data.deptTable = Math.random();
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
        this.reviseDeptDialogVisible = false;
      } else {
        this.$message({
          type: "info",
          message: "未修改信息",
          duration: 2000,
        });
        this.reviseDeptDialogVisible = false;
      }
      setTimeout(() => {
       location.reload();
      }, 2200);
    },
    /*移除部门*/
    RemoveByRow(index) {
      this.$confirm("此操作将永久删除该对象, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          var serverResponse;
          var deleteId = this.tableData[index].id;
          let url = this.GLOBAL.networkSrc+"/deleteDept/" + deleteId;
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
                  duration: 1200,
                });
              } else {
                this.$message({
                  type: "error",
                  message: serverResponse.message,
                  duration: 1200,
                });
              }
            })
            .catch((error) => {
              this.$message({
                type: "error",
                message: error.name,
                duration: 1200,
              });
              console.log("出错了，请联系后台管理人员" + error);
            });
          this.reviseDeptDialogVisible = false;
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
      setTimeout(() => {
       location.reload();
      }, 1500);
    },
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
</style>
    