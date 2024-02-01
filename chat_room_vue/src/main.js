import Vue from 'vue';
import elTableInfiniteScroll from 'el-table-infinite-scroll';
import App from './App.vue';
import router from './router';

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import 'element-ui/lib/theme-chalk/base.css';

import Contextmenu from "vue-contextmenujs";


import myAPI from './components/config/myAPI.vue';


Vue.config.productionTip = false
//ElementUI
Vue.use(ElementUI);
//无限滚动
Vue.use(elTableInfiniteScroll);
//全局变量--后端网络接口
Vue.prototype.GLOBAL = myAPI;
//自定义右键菜单
Vue.use(Contextmenu);

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')