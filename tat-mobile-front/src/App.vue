<template>
  <v-app style="height: 100vh">
    <v-layout>
      <v-app-bar>
        <v-btn variant="plain" to="/" title="Go dashboard">
          <v-icon size="x-large">
            <v-img src="./assets/Logo.svg"></v-img>
          </v-icon>
        </v-btn>
        <v-app-bar-title style="color: rgb(197, 226, 21)">{{ currentPage }}</v-app-bar-title>
        <v-spacer></v-spacer>
        <v-text-field
          hide-details="true"
          density="compact"
          prepend-inner-icon="mdi-magnify"
          variant="outlined"
          class="mr-8"
          label="Search reposytory"
          v-model="searchRepo"
        ></v-text-field>
        <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
      </v-app-bar>
      <v-navigation-drawer v-if="currentPage != 'Dashboard'" width="400">
        <ListOfStats />
      </v-navigation-drawer>
      <v-main style="height: 100%">
        <v-sheet
          style="height: 96%; overflow-y: auto"
          class="pa-4 ma-5 page"
          rounded="xl"
          elevation="4"
          id="scroll-target"
        >
          <router-view @get-repos="getRepos" :searchRepo="searchRepo" />
        </v-sheet>
      </v-main>
    </v-layout>
  </v-app>
</template>

<script>
export default {
  data() {
    return {
      searchRepo: "",
    };
  },
  methods: {},
  computed: {
    currentPage() {
      switch (this.$route.name) {
        case "/project-review":
          return this.$store.state.currentRepo.projectName;
        default:
          return "Dashboard";
      }
    },
  },
};
</script>
<style scoped>
.page::-webkit-scrollbar {
  display: none;
}

.page {
  -ms-overflow-style: none;
  scrollbar-width: none;
}
</style>
