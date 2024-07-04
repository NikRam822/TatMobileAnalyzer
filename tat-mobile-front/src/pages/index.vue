<template>
  <v-row>
    <CardOfProject v-for="rep in repositories" :rep="rep" @get-repos="getRepos" />
    <CardForAdd @get-repos="getRepos" />
  </v-row>
</template>
<script>
import axios from "axios";
let server_path = import.meta.env.VITE_BACKEND_URL;
export default {
  props: ["searchRepo"],
  data() {
    return {};
  },
  methods: {
    async getRepos() {
      let hostadress = server_path + "/api/project/get-projects";
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
        const projectLink = repo.projectLink?.toLowerCase() || "";
        const searchTerm = this.searchRepo.toLowerCase();
        if (projectLink.includes(searchTerm)) {
          newRpos.push(repo);
        }
      }
      newRpos.sort(function (a, b) {
        return b.favorite - a.favorite;
      });
      return newRpos;
    },
  },
  created() {
    this.getRepos();
  },
};
</script>
