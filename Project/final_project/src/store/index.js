import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    //存储token
    Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization') : '',
    AccountName: localStorage.getItem('AccountName') ? localStorage.getItem('AccountName') : '',
    UserId: localStorage.getItem('UserId') ? localStorage.getItem('UserId') : ''
  },
  mutations: {
    // 修改token，并将token存入localStorage
    changeLogin (state, user) {
      state.Authorization = user.Authorization;
      localStorage.setItem('Authorization', user.Authorization);
    },
    changeUsername (state, user) {
      state.AccountName = user.AccountName;
      localStorage.setItem('AccountName', user.AccountName);
    },
    changeUserId (state,user) {
      state.UserId = user.UserId;
      localStorage.setItem('UserId',user.UserId);
    }
  },
  actions: {
  },
  modules: {
  }
})
