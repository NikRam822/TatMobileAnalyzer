<template>
  <v-card class="d-flex flex-column justify-space-between" style="height: 100%">
    <v-container>
      <h3 class="align-self-center">Chose the type of stats:</h3>
      <v-divider></v-divider>
      <v-list>
        <v-list-item v-for="val in statistics">
          <v-btn variant="tonal" @click="toPage(val.link)" width="100%">{{
            val.name
          }}</v-btn>
        </v-list-item>
      </v-list>
      <v-divider></v-divider>
    </v-container>
    <v-container>
      <v-btn width="100%"> Filters </v-btn>
    </v-container>
  </v-card>
</template>
<script>
import axios from "axios";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  data: () => ({
    statistics: [
      { name: "Churn Statistics", link: "ChurnStatistics" },
      { name: "Cocomo Statisitcs", link: "CocomoStatistics" },
    ],
    loader: false,
    path: "",
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
  },
  created() {
    this.updateFilters();
  },
};
</script>
