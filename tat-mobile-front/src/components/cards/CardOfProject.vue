<template>
  <v-col xs="12" sm="6" md="4" lg="3">
    <v-card
      :key="rep.projectName"
      @click="navigateToProjectReview()"
      :subtitle="rep.projectLink"
      elevation="4"
      rounded="xl"
      height="165"
      class="d-flex flex-column"
      style="min-width: 200px"
      :style="
        this.$store.state.RepoSatistic[rep.projectLink]
          ? {
              background: 'rgb(69,77,85)',
              background:
                'linear-gradient(0deg, rgba(69,77,85,0) 0%, rgba(134,153,52,0.1) 60%, rgba(197,226,21,0.2) 100%)',
            }
          : {}
      "
    >
      <template v-slot:title>
        <v-btn
          variant="text"
          @click.stop="updateFavor(rep.favorite, rep.projectId)"
          flat
          class="text-amber-accent-3"
          :icon="rep.favorite ? 'mdi-star' : 'mdi-star-outline'"
        ></v-btn>
        {{ rep.projectName }}
      </template>
      <template v-slot:append>
        <v-btn variant="text" @click.stop="getStatistic()" icon="mdi-autorenew"></v-btn>
      </template>
      <v-container v-show="loader">
        <v-progress-linear color="rgb(92, 99, 106)" height="6" indeterminate rounded></v-progress-linear>
        <p>Analyzing reposytory</p>
      </v-container>
      <v-spacer></v-spacer>
      <v-card-actions class="ps-4">
        <p>Date of last update</p>
        <v-spacer></v-spacer>
        <v-btn variant="text" @click.stop="deleteProject" flat icon="mdi-trash-can-outline" class="text-grey"></v-btn>
      </v-card-actions>
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
    async getStatistic() {
      this.loader = true;
      let hostadress = server_path + "/api/statistic/churn";
      try {
        const statistic = await axios.post(hostadress, {
          branch: "",
          projectId: this.rep.projectId,
        });
        this.$store.commit("setBranch", "");
        this.$store.commit("addStatistc", [this.rep.projectLink, statistic]);
      } catch (error) {
        console.error("Error " + error.message);
      }
      this.loader = false;
    },
    async navigateToProjectReview() {
      this.$store.commit("changeCurrentRepo", this.rep);
      this.$router.push("/project-review");
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
