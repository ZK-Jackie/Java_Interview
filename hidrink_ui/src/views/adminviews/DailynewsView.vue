<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-main>
      <div style="margin: 50px auto">
        <el-row :gutter="20">
          <!-- 第一个数据框 -->
          <el-col :span="6">
            <div>
              <!-- group-separator：每3位数之间的间隔符号设定
                     precision：数据显示精度
                -->
              <el-statistic
                group-separator=","
                :precision="0"
                :value="tableData.totalCustomer"
                title="当前注册顾客总数"
              ></el-statistic>
            </div>
          </el-col>

          <!-- 第二个数据框 -->
          <el-col :span="6">
            <div>
              <el-statistic title="注册顾客男女比">
                <template slot="formatter"
                  >{{ tableData.totalMaleCustomer }}/{{ tableData.totalFemaleCustomer }}
                </template>
              </el-statistic>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <el-statistic
                group-separator=","
                :precision="0"
                decimal-separator="."
                :value="tableData.todayRegister"
                title="今日新注册用户"
              >
                <template slot="prefix">
                  <i class="el-icon-s-flag" style="color: red"></i>
                </template>
                <template slot="suffix">
                  <i class="el-icon-s-flag" style="color: blue"></i>
                </template>
              </el-statistic>
            </div>
          </el-col>
          <el-col :span="6">
            <div>
              <el-statistic
                :value="tableData.todaySales"
                title="今日销量"
                group-separator=","
                :precision="0"
              ></el-statistic>
            </div>
          </el-col>
        </el-row>
      </div>

      <el-divider></el-divider>

      <div>
        <el-progress type="circle" :percentage="tableData.todaySales/tableData.dailySaleAim*100" :width="120"></el-progress>
        <p>今日营销目标完成度</p>
      </div>

      <el-divider></el-divider>

      <div style="width: 40%; height: auto; margin: 0 auto">
        <a>暂无数据</a><el-progress :percentage="tableData.todayPic1Clicked/tableData.dailyClickAim*100" status="exception" :stroke-width="8"></el-progress>
        <br />
        <a>暂无数据</a><el-progress :percentage="tableData.todayPic2Clicked/tableData.dailyClickAim*100" status="exception" :stroke-width="8"></el-progress>
        <br />
        <a>暂无数据</a><el-progress :percentage="tableData.todayPic3Clicked/tableData.dailyClickAim*100" status="exception" :stroke-width="8"></el-progress>
        <br />
        <a>暂无数据</a><el-progress :percentage="todayPic4Clicked/tableData.dailyClickAim*100" status="exception" :stroke-width="8"></el-progress>
        <p>首页展示点击量</p>
      </div>

      <el-divider></el-divider>
    </el-main>
  </div>
</template>
    
    
    
<script>
import Cookies from 'js-cookie';
import axios from 'axios';
import myMenuBarVue from "../../components/myMenuBar.vue";

export default {
  components:{
    myMenuBarVue,
  },
  data() {
    return {
      loginData: {
        isLogin: false,
        role: "user",
        username: "null",
      },
      tableData:{

      },
      like: true,
      value1: 4154,
      value2: 1314,
      value3: 1221,
      maleNum: 13,
      femaleNum: 14,
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
    getTableData(){
      var state = this.loginData.isLogin;
      var outside = this;
      var serverResponse;
      if (state == true) {
        let logUrl = this.GLOBAL.networkSrc+"/daily";
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
            if (serverResponse.code == 1) {
              outside.tableData = serverResponse.data;
              console.log(outside.tableData);
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
        },1800);
      }
    },
  },
  mounted() {
    
  },
};
</script>
    
    
    

<style>
.like {
  cursor: pointer;
  font-size: 25px;
  display: inline-block;
}
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
</style>
  