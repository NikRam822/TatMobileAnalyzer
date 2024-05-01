import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    filters: [],
    repositories: [],
    currentRepo: "Error parsing name",
    RepoSatistic: {},
    statsForGraph: {},
  },
  mutations: {
    changeFilter(state, payload) {
      state.filters = payload;
    },
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
  },
  plugins: [createPersistedState()],
});
