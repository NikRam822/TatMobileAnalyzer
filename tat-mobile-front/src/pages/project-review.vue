<template>
  <v-layout>
    <v-app-bar>
      <v-img src="../assets/Logo.svg" height="40" max-width="64"></v-img>
      <v-app-bar-title class="text-lime-accent-3">{{ repos.slice(repos.lastIndexOf('/') + 1) }}</v-app-bar-title>
      <v-spacer></v-spacer>
      <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
    </v-app-bar>
    <v-main>
      <v-sheet class="pa-4 ma-4" height="89dvh" rounded="xl" elevation="4">
        <Content />
      </v-sheet>
    </v-main>
  </v-layout>
</template>

<script>
export default {
  data: () => ({
  }),
  created() {
    this.repos = this.$route.query.repoName;
  }
}
</script>
