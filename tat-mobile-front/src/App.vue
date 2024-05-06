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
        <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
      </v-app-bar>
      <v-navigation-drawer v-if="currentPage == '/project-review'" width="400">
        <ListOfStats />
      </v-navigation-drawer>
      <v-main style="height: 100%">
        <v-sheet style="height: 96%" class="pa-4 ma-5" rounded="xl" elevation="4">
          <router-view @get-repos="getRepos" />
        </v-sheet>
      </v-main>
    </v-layout>
  </v-app>
</template>

<script>
import axios from "axios";
// import Logo from "@/assets/logo.svg";
export default {
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
