import { createStore } from "vuex";
import createPersistedState from "vuex-persistedstate";

export default createStore({
  state: {
    repositories: [],
    filters: [],
    currentRepo: "Error parsing name",
    currentStatisitc: "ChurnStatistics",
    RepoSatistic: {},
    projectBranch: {},
    storeParams: {},
    projectBranch: {},
  },
  mutations: {
    setBranch(state, branch) {
      state.projectBranch[state.currentRepo.projectLink] = branch;
    },

    delParam(state) {
      delete state.storeParams[state.currentRepo.projectLink];
    },

    setBranch(state, branch) {
      state.projectBranch[state.currentRepo.projectLink] = branch;
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
      delete state.storeParams[rep];
      delete state.projectDate[rep];
      delete state.projectBranch[rep];
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
    getBranch(state) {
      return state.projectBranch[state.currentRepo.projectLink];
    },
  },
  plugins: [createPersistedState()],
});
