<template>
  <v-row>
    <CardOfProject v-for="rep in repositories" :rep="rep" @get-repos="getRepos" />
    <CardForAdd @get-repos="getRepos" />
  </v-row>
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {};
  },
  props: ["searchRepo"],
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
  computed: {
    repositories() {
      let newRpos = [];
      for (let repo of this.$store.state.repositories) {
        if (repo.projectLink.toLowerCase().includes(this.searchRepo.toLowerCase())) {
          newRpos.push(repo);
        }
      }
      return newRpos;
    },
  },
  created() {
    this.getRepos();
  },
};
</script>
