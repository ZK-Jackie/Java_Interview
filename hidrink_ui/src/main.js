import Vue from 'vue';
import App from './App.vue';
import router from './router';

import qs from 'qs';

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import myAPI from './components/config/myAPI.vue';


Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.$qs = qs;
Vue.prototype.GLOBAL = myAPI;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')

