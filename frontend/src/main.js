import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import MainUI from "@/components/MainUI";
import axios from 'axios';

Vue.use(ElementUI);
Vue.use(VueRouter);

const apiUrl = process.env.VUE_APP_API_URL;

axios.defaults.baseURL = apiUrl;

const router = new VueRouter({
  mode: "history",
  routes: [
    {
      path: '/',
      component: MainUI,
      props: true,
    }
  ]
});

Vue.config.productionTip = false

new Vue({
  el: '#app',
  template: '<App/>',
  router,
  render: h => h(App),
}).$mount('#app')
