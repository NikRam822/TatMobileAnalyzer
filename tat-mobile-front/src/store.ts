import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: {},
    currentRepo: "Error parsing name",
  },
  mutations: {
    addRepos(state, repos) {
      state.repositories[repos[0]] = repos[1];
    },
    delRepos(state, repos) {
      delete state.repositories[repos];
    },
    changeCurrentRepo (state, payload) {
      state.currentRepo = payload
    }
  },
  plugins: [createPersistedState()],
});
