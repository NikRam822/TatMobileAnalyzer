import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: [],
    currentStatistic: "",
    currentRepo: "Error parsing name",
    RepoSatistic: {},
    statsForGraph: {},
  },
  mutations: {
    changestatsForGraph(state, payload) {
      state.statsForGraph = payload;
    },
    addStatistc(state, rep) {
      state.RepoSatistic[rep[0]] = rep[1];
    },
    deleteStatistic(state) {
      state.RepoSatistic = {};
    },
    refreshRepos(state, repos) {
      state.repositories = repos;
    },
    changeCurrentRepo(state, payload) {
      state.currentRepo = payload;
    },
    changeCurrentStatistic(state, payload) {
      state.currentStatistic = payload;
    },
  },
  plugins: [createPersistedState()],
});
