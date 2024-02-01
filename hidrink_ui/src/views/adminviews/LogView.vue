<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-main>
      <el-table :data="pageInfo.tableData" stripe style="width: 100%">
        <el-table-column label="编号" width="80" type="index">
          <template slot-scope="row">
            <span>{{
              row.$index + 1 + (pageInfo.pageNow - 1) * pageInfo.pageSize
            }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="operateUser"
          label="用户ID"
          width="80"
        ></el-table-column>
        <el-table-column prop="operateRole" label="用户角色" width="80">
          <div slot-scope="scope">
            <span v-if="scope.row.operateRole == 0"> 管理员 </span>
            <span v-else-if="scope.row.operateRole == 1"> 员工 </span>
            <span v-else> 顾客 </span>
          </div>
        </el-table-column>
        <el-table-column prop="methodName" label="调用方法" width="150">
        </el-table-column>
        <el-table-column prop="methodParams" label="提交参数" width="400">
        </el-table-column>
        <el-table-column prop="operateTime" label="请求时间" width="160">
        </el-table-column>
        <el-table-column prop="costTime" label="操作用时(ms)" width="110">
        </el-table-column>
      </el-table>
      <div class="block" style="margin-top: 5px;">
        <el-pagination
          @size-change="pageSizeChange"
          @current-change="pageChange"
          :current-page="pageInfo.pageNow"
          :page-sizes="[10, 20, 50, 200]"
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
import qs from "qs";
import Cookies from "js-cookie";
import myMenuBarVue from "../../components/myMenuBar.vue";

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
      pageInfo: {
        /*当前页面总数据*/
        totalSize: 0,
        pageSize: 10,
        pageNow: 1,
        tableData: [] /*表格数据，需要一定是一个数组*/,
      },
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
      if (state == true) {
        this.pageInfo.tableData = [];
        let logUrl = this.GLOBAL.networkSrc+"/log?" + qs.stringify(this.pageInfo);
        axios
          .get(logUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Log Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code == 1) {
              outside.pageInfo = serverResponse.data;
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
    pageSizeChange(newSize) {
      /*页面大小变化，更新data值并且重新加载*/
      this.pageInfo.pageSize = newSize;
      this.getTableData();
    },
    pageChange(newPageNow) {
      /*页码变化，更新data值并且重新加载*/
      this.pageInfo.pageNow = newPageNow;
      this.getTableData();
    },
  },
  mounted() {},
};
</script>
    
    
    

<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
</style>
  