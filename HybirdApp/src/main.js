// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import App from './App'
import router from './router'

Vue.config.productionTip = false

// 注入ZWeb 插件
// import VueZWeb from './plugins/vue-zweb.js'
// Vue.use(VueZWeb);

// 注入ZWeb 插件
import VueZWeb from 'vue-zweb';
Vue.use(VueZWeb);

/* eslint-disable no-new */
let vm = new Vue({
  el: '#app',
  router,
  template: '<App/>',
  components: { App }
})
