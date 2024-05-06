import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: [],
    RepoSatistic: {},
    filters: [],
    currentRepo: "Error parsing name",
  },
  mutations: {
    changeFilter(state, payload) {
      state.filters = payload;
      delete state.filters.projectId
    },
    addStatistc(state, rep) {
      state.RepoSatistic[rep[0]] = rep[1];
    },
    delStatistic(state, rep) {
      delete state.RepoSatistic[rep]
    },
    refreshRepos(state, repos) {
      state.repositories = repos;
    },
    changeCurrentRepo(state, rep) {
      state.currentRepo = rep;
    },
  },
  plugins: [createPersistedState()],
});
