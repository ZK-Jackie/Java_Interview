<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData" @transfer="getLoginInfo"></myMenuBarVue>

    <!--橙色部分表单-->
    <div class="searchCard">
      <el-form :inline="true" :model="pageInfo" class="demo-form-inline">
        <el-form-item label="商品名">
          <el-input v-model="pageInfo.goodsName" placeholder="商品名"></el-input>
        </el-form-item>

        <el-form-item label="价格区间">
          <el-input v-model=" pageInfo.minPrice" placeholder="最低价"></el-input>
        </el-form-item>

        -----
        <el-form-item>
          <el-input v-model=" pageInfo.maxPrice" placeholder="最高价"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="search">查询</el-button>
        </el-form-item>
      </el-form>
      <el-button  class="buyButton" @click="drawer = true" type="primary" style="">
        购物车
      </el-button>
    </div>

    <!--    黄色商品展示部分-->
    <el-row>
      <el-col v-for="(goods, index) in pageInfo.goodsData" :span="6" :key="goods.id" :offset="index > 0 ? 3 : 0">
        <el-card class="GoodsCard">
          <img :src="goods.image" class="image"> <!-- 假设每个商品对象包含图片链接 -->
          <div style="padding: 14px;">
            <span>{{ goods.name }}</span> <!-- 假设每个商品对象包含商品名 -->
            <div class="bottom clearfix">
              <el-button type="primary" @click="addToCart(goods)" icon="el-icon-plus" class="button" circle></el-button>
              <el-button v-show="getQuantities(goods.id)>0" type="primary" @click="removeFromCart(goods)"
                         icon="el-icon-minus" class="button" circle></el-button>
              <div class="counter" v-show="getQuantities(goods.id)>0">{{ getQuantities(goods.id) }}</div>
              <div class="counter2" v-show="totalQuantity>0">{{ totalQuantity }}</div>
            </div>
            <span class="price">￥{{ goods.price }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
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
      >
      </el-pagination>
    </div>

    <!--    购物车-->
    <el-drawer title="我是标题" :visible.sync="drawer" :with-header="false">
      <div v-for="card in cart" :key="card.id">
        <el-card shadow="hover">
          <el-row>
            <el-col style="width: auto">
              <div>
                <img :src="card.urlPicture"
                     style="width:50px; height:50px" alt="商品图片"/>
              </div>
            </el-col>
            <el-col style="width: 200.8px;height: 54px;">
              <div class="GoodsInfo" style="float: left;width: 200.8px;height: 54px;">
                {{ card.name }}
                <div class="PriceInfo">
                  ￥{{ card.price }}
                </div>
              </div>
            </el-col>
            <el-col style="width: auto; float: right;">
              <div style="float: right;">
                <el-button type="primary" @click="addToCart(card)" icon="el-icon-plus" class="button"
                           circle></el-button>
                <span>{{ card.quantities }}</span>
                <el-button type="primary" @click="removeFromCart(card)" icon="el-icon-minus" class="button"
                           circle></el-button>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>
      <!-- 添加最终购物车下单按钮 -->
      <el-button v-show="totalQuantity>0"
          type="primary"
          size="medium"
          plain
          @click="Buy">
        购物车下单
      </el-button>
    </el-drawer>

  </div>

</template>

<script>
import axios from "axios";
import myMenuBarVue from "@/components/myMenuBar.vue";
import Cookies from "js-cookie";

export default {
  computed: {},
  components: {myMenuBarVue},
  data() {
    return {
      loginData: {
        isLogin: false,
        username: "",
        role: -1
      },
      //购物车按钮参数
      drawer: false,
      //购物车商品计数
      totalQuantity: 0,// 商品总数
      //购物车数组
      cart: [],
      //存购物车内商品id
      saveID: [],
      //页面总数据
      pageInfo: {
        totalSize:0,
        pageNow: 1,
        pageSize: 5,
        priceOrder: 0,
        minPrice: 0,
        maxPrice: 0,
        goodsName: '',
        goodsData:[]
        // 初始化商品名
      }
    }
  },
  methods: {
    getLoginInfo(loginData) {
      this.loginData.isLogin = loginData.isLogin;
      this.loginData.username = loginData.username;
      this.loginData.role = loginData.role;
      console.log("GoodsVue Get Data After Set: ↓↓");
      console.log(this.loginData);
      this.getTableData();
    },
    getTableData() {
      var state = this.loginData.isLogin;
      state = true;
      var outside = this;
      var serverResponse;
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      /* 1.获取商品信息*/
      if (state == true) {
        let goodsUrl = this.GLOBAL.networkSrc + "/getGoods?pageNow="+this.pageInfo.pageNow+"&"
        goodsUrl += "pageSize=" + this.pageInfo.pageSize + "&";
        goodsUrl += "totalSize=" + this.pageInfo.totalSize + "&";
        goodsUrl += "goodsName=" + this.pageInfo.goodsName + "&";
        goodsUrl += "priceOrder=" + this.pageInfo.priceOrder + "&";
        goodsUrl += "minPrice=" + this.pageInfo.minPrice + "&";
        goodsUrl += "manPrice=" + this.pageInfo.maxPrice;
        axios
            .get(goodsUrl, config)
            .then(function (result) {
              serverResponse = JSON.parse(result.request.response);
              console.log("Get Total Order Response ↓↓");
              console.log(serverResponse.data);
              if (serverResponse.code === 1) {
                outside.goodsData = serverResponse.data;
                console.log(outside.goodsData);
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
    },
    //查询商品功能
    search() {
      this.goodsName=this.pageInfo.goodsName;
      this.minPrice=this.pageInfo.minPrice;
      this.maxPrice=this.pageInfo.maxPrice;
      console.log(this.pageInfo);
      this.getTableData();
    },
    addToCart(goods) {
      let existingItem = this.cart.find(item => item.id === goods.id);
      if (existingItem) {
        console.log(existingItem);
        existingItem.quantities++;
      } else {
        this.cart.push({
          id: goods.id,
          name: goods.name,
          price: goods.price,
          quantities: 1
        });
      }
      this.updateTotalQuantity();
    },
    removeFromCart(goods) {
      let existingItem = this.cart.find(item => item.id === goods.id);
      if (existingItem) {
        console.log(existingItem);
        existingItem.quantities--;
        if (existingItem.quantities <= 0) {
          this.cart = this.cart.filter(item => item.id !== goods.id);
        }
      }
      this.updateTotalQuantity();
    },
    getQuantities(goodsId) {
      let quantities = 0;
      this.cart.forEach((item) => {
        if (item.id === goodsId) {
          quantities = item.quantities;
        }
      });
      this.updateTotalQuantity()
      return quantities;

    },
    updateTotalQuantity() {
      let total = 0;
      this.cart.forEach(item => {
        total += item.quantities;
      });
      this.totalQuantity = total;
    },
    // 分页
    pageSizeChange(newSize) {
      this.pageInfo.pageSize = newSize;
      this.getTableData();
    },
    pageChange(newPageNow) {
      this.pageInfo.pageNow = newPageNow;
      this.getTableData();
    },
    //购物车下单
    Buy() {
      var serverResponse;
      var submitArr=[];
      var arrIndex = 0;
      this.Cart.forEach((item) => {
        if (item != null) {
          submitArr[arrIndex++] = item.id
        }
      });

      console.log(submitArr);
      alert(submitArr);
      let buyUrl = this.GLOBAL.networkSrc+"/buyGoods/"+this.Cart.toString();
      var config = {
        headers: {
          "Content-Type": "application/json",
          "Access-Control-Allow-Origin": "*",
          Token: Cookies.get("token"),
        },
      };
      console.log(buyUrl);
      axios
          .get(buyUrl, config)
          .then((result) => {
            serverResponse = JSON.parse(result.request.response);
            if (serverResponse.code == 1) {
              this.$message({
                type: "success",
                message: "下单成功!",
                duration: 2000,
              });
              /*成功：清空缓存*/
              this.Cart = [];

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
      setTimeout(() => {
        location.reload();
      }, 2200);
    },
  },
}

</script>


<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; */
}

.GoodsCard {
  margin-top: 5px;
  margin-bottom: 20px;
  width: 80%;
}

.counter {
  top: 10px; /* 距离顶部10像素 */
  right: 10px; /* 距离右侧10像素 */
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #ff0000;
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
}

.counter2 {
  position: absolute;
  top: 10px; /* 距离顶部10像素 */
  right: 10px; /* 距离右侧10像素 */
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #ff0000;
  color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 16px;
}

.price {
  font-family: Arial, sans-serif;
  font-weight: bold;
  font-size: 50px;
  color: red;
}

.searchCard {
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center; /* 如果需要文本居中 */
}
.block {
  text-align: center;
}

.buyButton{
  position: relative;
  bottom: 20px;
  right: 20px;
}

</style>