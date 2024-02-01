<template>
  <div class="MainBox">
    <myMenuBarVue ref="menuData"> </myMenuBarVue>
    <el-main>
      <div><br /><br /></div>
      <!--跟菜单作稍微分隔-->
      <el-carousel :interval="4000" type="card" height="500px">
        <el-carousel-item v-for="item in imgs" :key="item.url">
            <img :src="item.url" class="HomeImgs" />
        </el-carousel-item>
      </el-carousel>
    </el-main>
  </div>
</template>

<script>
import axios from 'axios';
import Cookies from 'js-cookie';
import myMenuBarVue from '../components/myMenuBar.vue';
/*子组件mounted → 父组件mounted*/
export default {
  components:{
    myMenuBarVue,
  },
  data() {
    return {
      loginData: {},
      imgs: [
        { url: require("../assets/1.png") },
        { url: require("../assets/2.png") },
        { url: require("../assets/3.png") },
        { url: require("../assets/4.png") },
        { url: require("../assets/5.png") },
        { url: require("../assets/6.png") },
      ],
    };
  },
  methods: {
    getLoginInfo(){
      this.loginData = this.$refs["menuData"].userInfo;
    },
    getADs(){ /*网络获取广告图片  暂未开发*/
      var outside = this;
      var serverResponse;
      let logUrl = this.GLOBAL.networkSrc+"/loadAD";
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
          console.log("Get AD Response ↓↓");
          console.log(serverResponse.data);
          if (serverResponse.code == 1) {
            var temp = serverResponse.data;
            var i = 0;
            while(temp[i] != null){
              outside.imgs[i] = temp[i].img;
              i++;
            }
            console.log(outside.imgs);
          } else {
            console.log(serverResponse.message);
          }
        })
        .catch((error) => {
          console.log("出错了，请联系后台管理人员" + error);
        });
      
    }
  },
  beforeMount(){
    // this.getADs();
  },
  mounted(){
    var outside = this;
    outside.getLoginInfo();
    console.log("获取到登录信息↓");
    console.log(JSON.stringify(outside.loginData));
  }
};
</script>

<style>
.MainBox {
  margin: 0 200px;
  /* background-color: gray; //直观看box大小是否符合需求*/   
}
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}
.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}
.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}
</style>