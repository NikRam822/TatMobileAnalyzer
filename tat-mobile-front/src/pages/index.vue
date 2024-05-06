<template>
  <v-row>
    <CardOfProject v-for="rep in this.$store.state.repositories" :rep="rep" @get-repos="getRepos" />
    <CardForAdd @get-repos="getRepos" />
  </v-row>
  <!-- <v-pagination
      v-model="page"
      :length="Math.ceil(this.$store.state.repositories.length / itemPerPage)"
      @input="paginate"
      classs="align-self-end
"
    >
    </v-pagination> -->
</template>
<script>
import axios from "axios";
export default {
  data() {
    return {
      // page: 1,
    };
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
    // paginate(page) {
    //   this.page = page;
    // },
  },
  // computed: {
  //   displayedCards() {
  //     const startIndex = (this.page - 1) * this.itemPerPage;
  //     const endIndex = startIndex + this.itemPerPage;
  //     return this.$store.state.repositories.slice(startIndex, endIndex);
  //   },
  // itemPerPage() {
  //   switch (this.$vuetify.display.name) {
  //     case "xs":
  //       return 2;
  //     case "sm":
  //       return 3;
  //     case "md":
  //       return 8;
  //     case "lg":
  //       return 15;
  //     case "xl":
  //       return 35;
  //     default:
  //       return 15;
  //   }
  // },
  created() {
    this.getRepos();
  },
};
</script>
