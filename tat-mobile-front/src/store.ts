import Vuex from 'vuex';

export default new Vuex.Store({
  state: {
    result: []
  },
  mutations: {
    setResult(state, result) {
      state.result = result;
    }
  },
  actions: {
    updateResult({ commit }, result) {
      commit('setResult', result);
    }
  },
  getters: {
    getResult: state => state.result
  }
});