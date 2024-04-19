import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: {},
  },
  mutations: {
    addRepos(state, repos) {
      state.repositories[repos[0]] = repos[1];
    },
    delRepos(state, repos) {
      delete state.repositories[repos];
    },
  },
  plugins: [createPersistedState()],
});
