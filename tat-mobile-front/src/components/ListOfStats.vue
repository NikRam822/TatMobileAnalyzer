<template>
  <v-container class="d-flex flex-column justify-space-between" style="height: 100%">
    <v-container>
      <v-container>
        <v-btn variant="default" width="100%" height="70px" class="text-none text-h4" color="rgb(197, 226, 21)">
          {{ this.$store.state.currentRepo.projectName }}
          <v-menu activator="parent">
            <v-list>
              <v-list-item v-for="(item, index) in getRepositoryes" :key="index" :value="index" @click="goToRepo(item)">
                <v-list-item-title>{{ item.projectName }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </v-btn>
      </v-container>
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
      v-if="loader"
      size="100"
      width="20"
      class="align-self-center"
      indeterminate
    ></v-progress-circular>
    <v-container>
      <v-menu :close-on-content-click="false">
        <template v-slot:activator="{ props }">
          <v-btn width="100%" v-bind="props"> Filters </v-btn>
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
                      <v-autocomplete
                        required
                        label="Enter file path"
                        v-model="path"
                        :items="
                          Object.getOwnPropertyNames(
                            this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data.general
                          )
                        "
                      ></v-autocomplete>
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
    loader: false,
    path: "",
    config: false,
  }),

  methods: {
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
      this.loader = true;
      let hostadress = server_path + "/api/statistic/churn";
      try {
        const statistic = await axios.post(hostadress, {
          projectId: repo.projectId,
        });
        this.$store.commit("addStatistc", [repo.projectLink, statistic]);
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.loader = false;
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
  },
  created() {
    this.updateFilters();
  },
  computed: {
    getRepositoryes() {
      const reposNames = [];
      for (let rep of this.$store.state.repositories) {
        if (
          rep.projectName != this.$store.state.currentRepo.projectName &&
          this.$store.state.RepoSatistic[rep.projectLink]
        ) {
          reposNames.push(rep);
        }
      }
      return reposNames;
    },
  },
};
</script>
