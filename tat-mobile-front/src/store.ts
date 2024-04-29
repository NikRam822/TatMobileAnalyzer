import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: [],
    currentRepo: "Error parsing name",
    RepoSatistic: {},
  },
  mutations: {
    addStatistc(state, rep) {
      state.RepoSatistic[rep[0]] = rep[1];
    },
    deleteStatistic(state){
      state.RepoSatistic = {}
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
