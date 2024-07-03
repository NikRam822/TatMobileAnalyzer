import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: [],
    RepoSatistic: {},
    filters: [],
    currentRepo: "Error parsing name",
    currentStatisitc: "ChurnStatistics",
    storeParams: {},
  },
  mutations: {
    delParam(state) {
      delete state.storeParams[state.currentRepo.projectLink];
    },

    setParams(state, param) {
      state.storeParams[state.currentRepo.projectLink] = param;
    },

    changePage(state, payload) {
      state.currentStatisitc = payload;
    },

    changeFilter(state, payload) {
      state.filters = payload;
      delete state.filters.projectId;
    },

    addStatistc(state, rep) {
      state.RepoSatistic[rep[0]] = rep[1];
    },

    delStatistic(state, rep) {
      delete state.RepoSatistic[rep];
    },

    refreshRepos(state, repos) {
      state.repositories = repos;
    },

    changeCurrentRepo(state, rep) {
      state.currentRepo = rep;
    },
  },
  getters: {
    getParams(state) {
      return state.storeParams[state.currentRepo.projectLink];
    },
  },
  plugins: [createPersistedState()],
});
