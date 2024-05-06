<template>
  <v-sheet class="d-flex flex-wrap flex-row ga-4">
    <CardOfProject
      v-for="rep in this.$store.state.repositories"
      :rep="rep"
      @get-repos="getRepos"
    />
    <CardForAdd @get-repos="getRepos" />
  </v-sheet>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {};
  },
  methods: {
    async getRepos() {
      let hostadress = "http://localhost:8080/project/get-projects";
      try {
        const repositories = await axios.get(hostadress);
        this.$store.commit("refreshRepos", repositories.data);
      } catch (error) {
        console.error("Error fetching repositories:", error);
      }
    },
  },
  created() {
    this.getRepos();
  },
};
</script>
