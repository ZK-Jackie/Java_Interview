<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"> </myMenuBarVue>

    <el-row :gutter="6" style="margin-top: 60px; margin-bottom: 10px">
      <el-col
        :span="8"
        v-for="(card, index) in tableData.cardData"
        :key="card.id"
        :offset="2.5"
        style="margin-bottom: 50px"
      >
        <el-card :body-style="{ padding: '10px' }">
          <!--卡片总div-->
          <div style="padding: 14px">
            <!--左侧订单号div-->
            <div class="order_id">{{ card.id }}</div>
            <!--中央商品名+数量大div-->
            <div v-for="(name, index) in card.goodsNames" :key="index">
              <span class="product_name">{{ name }}</span>
              <span class="product_num">{{ card.purchaseVolume[index] }}</span>
            </div>
            <!--下方时间+右下角三个按钮div-->
            <div class="bottom clearfix">
              <time class="time">订单创建时间：{{ card.createTime }}</time>
              <p class="name">下单用户：{{ card.cusName }}</p>
              <div class="button">
                <!--提交按钮-->
                <el-button
                  type="success"
                  icon="el-icon-check"
                  circle
                  @click="finishOrder(index, card.id)"
                ></el-button>
                <!--悬浮提示按钮-->
                <el-popover
                  placement="bottom-start"
                  title="订单备注"
                  width="150"
                  trigger="hover"
                  :ref="`popover-${card.id}-${card.note}`"
                  v-show="card.note != null"
                >
                  <p>{{ card.note }}</p>
                  <el-button
                    icon="el-icon-info"
                    circle
                    slot="reference"
                  ></el-button>
                </el-popover>
                <!--删除悬浮提示按钮-->
                <el-popover
                  placement="bottom"
                  width="150"
                  trigger="click"
                  :ref="`popover-${card.id}`"
                >
                  <p>取消该订单？</p>
                  <div style="text-align: right; margin: 0">
                    <!-- <el-button size="mini" type="text" @click="cancelDelOrder()"
                      >取消</el-button
                    > -->
                    <el-button
                      type="primary"
                      size="mini"
                      @click="delOrder(index, card.id)"
                      >确定</el-button
                    >
                  </div>
                  <el-button
                    type="danger"
                    icon="el-icon-delete"
                    slot="reference"
                    circle
                  ></el-button>
                </el-popover>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
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
      //登录信息
      loginData: {
        isLogin: false,
        role: "user",
        username: "null",
      },
      deleteHoverTip: false,
      tableData: {
        totalSize: 5,
        cardData: [
          
        ],
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
      /* 1.获取订单信息*/
      if (state == true) {
        let orderUrl = this.GLOBAL.networkSrc + "/orderList";
        axios
          .get(orderUrl, config)
          .then(function (result) {
            /*2.接受到后端响应*/
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Total Order Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code === 1) {
              /*3.1处理正常信息*/
              outside.cardData = serverResponse.data;
              console.log(outside.cardData);
            } else {
              /*3.2处理异常信息*/
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
    },
    delOrder(index, id) {
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
      console.log(index);
      console.log(id);
      if (state == true) {
        let delUrl = this.GLOBAL.networkSrc + "/withdrawOrder/"+id;
        axios
          .delete(delUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Total Order Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code === 1) {
              console.log("Delete" + id + "Successful!");
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
      // alert(index);
      location.reload();
    },
    finishOrder(index, id) {
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
			console.log(index);
      console.log(id);
      if (state == true) {
        let finishUrl = this.GLOBAL.networkSrc + "/finishOrder/"+id;
        axios
          .patch(finishUrl, config)
          .then(function (result) {
            serverResponse = JSON.parse(result.request.response);
            console.log("Get Total Order Response ↓↓");
            console.log(serverResponse.data);
            if (serverResponse.code === 1) {
              console.log("Finish " + id + " Successful!");
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
      // alert(index);
      location.reload();
		},
  },
};
</script>

<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}
.order_id {
  float: left;
  font-size: xx-large;
  font-weight: bold;
}
.product_name {
  font-size: x-large;
  font-weight: bold;
}
.product_num {
  color: darkred;
  font-size: x-large;
  font-weight: bold;
  float: right;
}
.order_name {
  font-size: x-large;
  font-weight: bold;
}
</style>