import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    stats: [],
    repositories: [],
    currentRepo: "Error parsing name",
    RepoSatistic: {},
  },
  mutations: {
    pushStats(state, stat) {
      state.stats = stat;
    },
    addStatistc(state, rep) {
      state.RepoSatistic[rep[0]] = rep[1];
    },
    refreshRepos(state, repos) {
      state.repositories = repos;
    },
    changeCurrentRepo(state, payload) {
      state.currentRepo = payload;
    },
  },
  plugins: [createPersistedState()],
});
