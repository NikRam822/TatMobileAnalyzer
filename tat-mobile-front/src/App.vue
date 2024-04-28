<template>
  <v-app>
    <v-layout>
      <v-app-bar>
        <v-img src="./assets/Logo.svg" height="40" max-width="64"></v-img>
        <v-app-bar-title style="color: rgb(197, 226, 21)">Dashboard</v-app-bar-title>
        <v-spacer></v-spacer>
        <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
      </v-app-bar>
      <v-main class="d-flex align-center justify-center">
        <v-sheet height="94%" width="100dvw" class="pa-4 ma-5" rounded="xl" elevation="4">
          <router-view @get-repos="getRepos" />
        </v-sheet>
      </v-main>
    </v-layout>
  </v-app>
</template>

<script>
import axios from 'axios'
export default {
  methods: {
    async getRepos() {
      let hostadress = "http://localhost:8080/project/get-projects"
      try {
        const repositories = await axios.get(hostadress);
        this.$store.commit('refreshRepos', repositories.data)
      } catch (error) {
        console.error('Error fetching repositories:', error);
      }
    }
  },
  created() {
    this.getRepos()
  }
}
</script>
