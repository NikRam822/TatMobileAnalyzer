<template>
  <v-container class="d-flex flex-column justify-space-between" style="height: 100%">
    <v-container class="pb-0">
      <v-progress-linear v-if="filterLoader || loader" size="20" indeterminate rounded></v-progress-linear>
      <v-btn
        v-if="
          !currentRepo ||
          this.$store.getters.getBranch != currentBranch ||
          startDate != this.$store.getters.getDate.startDate ||
          endDate != this.$store.getters.getDate.endDate
        "

        variant="outlined"
        elevation="5"
        width="100%"
        height="100px"
        class="text-none text-h4"
        @click="getStatistic()"
        style="border-bottom: 0px"
      >
        <span class="d-flex flex-column">
          <p>Start Analyze</p>
          <p class="text-body-1">{{ currentBranch }}</p>
          <p class="text-body-1">{{ dateDisplay(this.startDate, this.endDate) }}</p>

        </span>
      </v-btn>
      <v-btn v-else variant="outlined" elevation="5" height="20px" style="border-bottom: 0px" @click="getStatistic()"
        >Restart Analyze
      </v-btn>
      <v-btn
        variant="outlined"
        width="100%"
        height="100px"
        class="text-none text-h4"
        color="rgb(197, 226, 21)"
        elevation="5"
      >
        <span class="d-flex flex-column">
          <p>{{ this.$store.state.currentRepo.projectName }}</p>
          <p class="text-body-1">
            {{ this.$store.getters.getBranch }}
          </p>
          <p class="text-body-1">
            {{ dateDisplay(...Object.values(this.$store.getters.getDate)) }}

          </p>
        </span>
        <v-menu activator="parent">
          <v-list>
            <v-list-item v-for="(item, index) in getRepositoryes" :key="index" :value="index" @click="goToRepo(item)">
              <v-list-item-title>{{ item.projectName }}</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </v-btn>

      <v-menu>
        <template v-slot:activator="{ props }">
          <v-btn
            @click="fetchBranches()"

            v-bind="props"
            variant="outlined"
            width="100%"
            height="40px"
            class="text-none text-h5"
            elevation="5"
            style="border-top: 0px"
          >
            Branches
          </v-btn>
        </template>
        <v-list>
          <v-list-item v-for="(branch, index) in branches" :key="index" :value="branch" @click="currentBranch = branch">
            <v-list-item-title> {{ branch }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

      <v-container>
        <h3 class="align-self-center text-center">Statisitcs</h3>
        <v-divider></v-divider>
        <v-list>
          <v-list-item v-for="val in statistics">
            <v-btn variant="tonal" @click="toPage(val.link)" width="100%">{{ val.name }}</v-btn>
          </v-list-item>
        </v-list>
        <v-divider></v-divider>
      </v-container>
    </v-container>
    <v-progress-circular
      v-if="filterLoader || loader"
      size="100"
      width="20"
      class="align-self-center"
      indeterminate
    ></v-progress-circular>

    <v-container class="pt-0">
      <v-container>
        <v-expansion-panels>
          <v-expansion-panel>
            <v-expansion-panel-title class="text-button"> Date filter </v-expansion-panel-title>
            <v-expansion-panel-text>
              <div>
                Start date
                <v-text-field type="date" v-model="startDate"></v-text-field>
                End date
                <v-text-field type="date" v-model="endDate"></v-text-field>
                <v-btn @click="(startDate = ''), (endDate = '')">Reset</v-btn>
              </div>
            </v-expansion-panel-text>
          </v-expansion-panel>
        </v-expansion-panels>
      </v-container>

      <v-menu :close-on-content-click="false">
        <template v-slot:activator="{ props }">
          <v-btn width="100%" v-bind="props" @click="currentRepo || updateFilters()"> Filters </v-btn>
        </template>
        <v-card>
          <v-list>
            <v-list-item>
              <v-container class="ma-0 pa-0 d-flex justify-space-between">
                <v-btn variant="tonal" width="100%"> New filter </v-btn>
              </v-container>
            </v-list-item>
            <v-divider></v-divider>
            <v-list-item v-for="(files, filter) in this.$store.state.filters" :key="filter">
              <v-container class="ma-0 pa-0 d-flex justify-space-between">
                <v-btn variant="tonal" width="80%">
                  {{ filter }}
                </v-btn>
                <v-btn @click="config = true">
                  <v-icon icon="mdi-cog"></v-icon>
                </v-btn>
                <v-dialog v-model="config" width="1000px" persistent>
                  <v-card prepend-icon="mdi-cog" title="Filter config">
                    <v-form @submit.prevent="addFile(filter)">
                      <v-autocomplete required label="Enter file path" v-model="path" :items="paths"></v-autocomplete>
                      <v-btn type="submit" width="100%"> Add file </v-btn>
                    </v-form>
                    <v-list>
                      <v-list-item v-for="(file, id) in files" key="id">
                        {{ file }}
                        <v-btn variant="text" icon="mdi-trash-can-outline" @click="deleteFile(filter, id)"></v-btn>
                      </v-list-item>
                    </v-list>
                    <v-btn class="ma-1 greenBtn" text="SAVE" @click="config = false"></v-btn>
                  </v-card>
                </v-dialog>
              </v-container>
            </v-list-item>
          </v-list>
          <v-btn @click="updateStatistic(this.$store.state.currentRepo)" variant="tonal" width="100%" class="greenBtn">
            Accept</v-btn
          >
        </v-card>
      </v-menu>
    </v-container>
  </v-container>
</template>
<script>
import axios from "axios";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  data: () => ({
    statistics: [
      { name: "Churn Statistics", link: "ChurnStatistics" },
      { name: "Project cost", link: "CocomoStatistics" },
    ],
    filterLoader: false,
    loader: false,
    path: "",
    config: false,
    branches: [],
    currentBranch: "",
    startDate: "",
    endDate: "",
  }),

  methods: {
    async fetchBranches() {
      try {
        const branches = await this.getBranches();
        this.branches = branches.data;
      } catch (error) {
        console.error("Error: ", error);
      }
    },
    async getBranches() {
      let hostadress = server_path + "/api/project/get-branches";
      try {
        const branches = await axios.post(hostadress, {
          projectId: this.$store.state.currentRepo.projectId,
        });
        return branches;
      } catch (error) {
        console.error("Error: ", error);
      }
    },
    dateFormating(since, until) {
      if (since && until) {
        return `?since=${since}&until=${until}`;
      }
      if (since) {
        return `?since=${since}`;
      }
      if (until) {
        return `?until=${until}`;
      }
    },
    async getStatistic() {
      this.loader = true;
      let hostadress = server_path + "/api/statistic/churn";
      if (this.startDate || this.endDate) {
        hostadress += this.dateFormating(this.startDate, this.endDate);
      }
      try {
        const statistic = await axios.post(hostadress, {
          branch: this.currentBranch,
          projectId: this.$store.state.currentRepo.projectId,
        });
        this.$store.commit("setDate", { startDate: this.startDate, endDate: this.endDate });

        this.$store.commit("addStatistc", [this.$store.state.currentRepo.projectLink, statistic]);
        this.$store.commit("setBranch", this.currentBranch);
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.loader = false;
    },
    toPage(page) {
      this.$store.commit("changePage", page);
    },

    async updateFilters() {
      let hostadress = server_path + "/api/filter/get-filters-for-project";
      try {
        const filters = await axios.post(hostadress, {
          projectId: this.$store.state.currentRepo.projectId,
        });
        this.$store.commit("changeFilter", filters.data);
      } catch (error) {
        console.error("Error: ", error);
      }
    },

    async updateStatistic(repo) {
      this.filterLoader = true;
      let hostadress = server_path + "/api/statistic/churn";
      try {
        const statistic = await axios.post(hostadress, {
          projectId: repo.projectId,
        });
        this.$store.commit("addStatistc", [repo.projectLink, statistic]);
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.filterLoader = false;
    },
    async addFile(filt) {
      let filts = this.$store.state.filters;
      filts[filt].splice(filts[filt].length - 1, 0, this.path);
      let hostadress = server_path + "/api/filter/update-filter";
      try {
        await axios.put(hostadress, {
          test: filts.test,
          generated: filts.generated,
          projectId: this.$store.state.currentRepo.projectId,
        });
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.path = "";
    },
    async deleteFile(filt) {
      let filts = this.$store.state.filters;
      filts[filt].splice(0, 1);
      let hostadress = server_path + "/api/filter/update-filter";
      try {
        await axios.put(hostadress, {
          test: filts.test,
          generated: filts.generated,
          projectId: this.$store.state.currentRepo.projectId,
        });
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.path = "";
    },
    goToRepo(item) {
      this.$store.commit("changeCurrentRepo", item);
      location.reload();
    },
    dateDisplay(startDate, endDate) {
      if (startDate && endDate) {
        return `${startDate} - ${endDate}`;
      }
      if (startDate) {
        return `since ${startDate}`;
      }
      if (endDate) {
        return `until ${endDate}`;
      }
      return "all time";
    },
  },
  computed: {
    dateDisplay() {
      if (this.startDate && this.endDate) {
        return `${this.startDate} - ${this.endDate}`;
      }
      if (this.startDate) {
        return `since ${this.startDate}`;
      }
      if (this.endDate) {
        return `until ${this.endDate}`;
      }
      return "all time";
    },
    currentRepo() {
      return this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink];
    },
    getRepositoryes() {
      const reposNames = [];
      for (let rep of this.$store.state.repositories) {
        reposNames.push(rep);
      }
      return reposNames;
    },
    paths() {
      let allPaths = [];
      for (let path of Object.getOwnPropertyNames(
        this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data.general
      )) {
        path = path.split("/");
        let currentPath = "";
        for (let p of path) {
          currentPath += p;
          allPaths.push(currentPath);
          currentPath += "/";
        }
      }
      return allPaths;
    },
  },
  created() {
    this.currentBranch = this.$store.getters.getBranch || "";
  },
};
</script>
