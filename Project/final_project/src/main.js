import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/icon/dianzan/iconfont.css'
import './assets/icon/logout/iconfont.css'
import VueLazyload from 'vue-lazyload'
import moment from 'moment'
import 'element-ui/lib/theme-chalk/base.css'
import infiniteScroll from 'vue-infinite-scroll'

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VueLazyload)

Vue.directive('preventReClick', {
  inserted (el, binding) {
    el.addEventListener('click', () => {
      if (el.style.pointerEvents === 'auto') {
        el.style.pointerEvents = 'none'
        setTimeout(() => {
          el.style.pointerEvents = 'auto'
        },binding.value || 1000)
      }
    })
  }
})
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
