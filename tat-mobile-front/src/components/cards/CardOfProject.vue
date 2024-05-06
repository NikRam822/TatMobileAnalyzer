<template>
  <v-col xs="12" sm="6" md="4" lg="3" xl="2">
    <v-card
      :key="rep.projectName"
      @click="navigateToProjectReview()"
      :title="rep.projectName"
      :subtitle="rep.projectLink"
      rounded="xl"
      height="165"
      border="md"
      class="d-flex flex-column"
      style="max-width: 500px; min-width: 200px"
    >
      <template v-slot:append>
        <v-btn
          @click.stop="updateFavor(rep.favorite, rep.projectId)"
          flat
          :icon="['mdi-star-outline', 'mdi-star'][+rep.favorite]"
        ></v-btn>
      </template>
      <v-container v-show="loader">
        <v-progress-linear color="rgb(92, 99, 106)" height="6" indeterminate rounded></v-progress-linear>
        <p>Analyzing reposytory</p>
      </v-container>
      <v-btn @click.stop="deleteProject" flat icon="mdi-trash-can-outline" class="align-self-end ma-4"></v-btn>
    </v-card>
  </v-col>
</template>
<script>
import axios from "axios";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  data() {
    return {
      loader: false,
    };
  },
  props: ["rep"],
  methods: {
    async navigateToProjectReview() {
      if (!this.$store.state.RepoSatistic[this.rep.projectLink]) {
        this.loader = true;
        let hostadress = server_path + "/api/statistic/patch";
        try {
          const statistic = await axios.post(hostadress, {
            projectId: this.rep.projectId,
            projectLink: this.rep.projectLink,
            projectName: this.rep.projectName,
          });
          this.$store.commit("addStatistc", [this.rep.projectLink, statistic]);
        } catch (error) {
          console.error("Error " + error.message);
        }
        this.loader = false;
      } else {
        this.$store.commit("changeCurrentRepo", this.rep);
        this.$router.push("/project-review");
      }
    },
    async deleteProject() {
      let hostadress = server_path + "/api/project/delete-project";
      try {
        const statistic = await axios.delete(hostadress, {
          data: {
            projectId: this.rep.projectId,
          },
        });
        this.$store.commit("delStatistic", this.rep.projectLink);
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.$emit("get-repos");
    },
    async updateFavor(fav, id) {
      if (fav) {
        let hostadress = server_path + "/api/project/favorite/delete";
        try {
          await axios.post(hostadress, {
            projectId: id,
          });
        } catch (error) {
          console.error("Error " + error.message);
        }
      } else {
        let hostadress = server_path + "/api/project/favorite/add";
        try {
          await axios.post(hostadress, {
            projectId: id,
          });
        } catch (error) {
          console.error("Error " + error.message);
        }
      }
      this.$emit("get-repos");
    },
  },
};
</script>
