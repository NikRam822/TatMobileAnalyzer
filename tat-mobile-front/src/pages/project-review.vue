<template>
  <v-layout>
    <!-- Нужно закрепить app-bar -->
    <v-app-bar>
      <!-- Нужно переделать, пока что обошел с max-width -->
      <v-img src="../assets/Logo.svg" height="40" max-width="64"></v-img>
      <v-app-bar-title class="text-lime-accent-3">Dashboard</v-app-bar-title>
      <v-spacer></v-spacer>
      <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
    </v-app-bar>
    <v-main>
      <h1>{{ this.$route.query.repoName }}</h1>
    </v-main>
  </v-layout>
</template>

<script>

</script>
