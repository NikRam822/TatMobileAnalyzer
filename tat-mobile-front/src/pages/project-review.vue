<template>
  <v-layout>
    <!-- Нужно закрепить app-bar -->
    <v-app-bar>
      <!-- Нужно переделать, пока что обошел с max-width -->
      <v-img src="../assets/Logo.svg" height="40" max-width="64"></v-img>
      <v-app-bar-title class="text-lime-accent-3">{{ repos.slice(repos.lastIndexOf('/') + 1) }}</v-app-bar-title>
      <v-spacer></v-spacer>
      <v-icon icon="mdi-account" size="40px" class="mr-10"></v-icon>
    </v-app-bar>
    <v-main>
      <v-sheet class="pa-6 ma-4" rounded="xl" elevation="4">
        <v-table density="compact">
          <thead>
            <tr>
              <th> Name </th>
              <th> Overall </th>
              <th> Churn </th>
              <th> Total commits </th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="churn, name in this.$store.state.repositories[repos].churn">
              <td> {{ name }} </td>
              <td> {{ this.$store.state.repositories[repos].overall[name] }}</td>
              <td> {{ Math.round(churn) }} </td>
              <td> {{ Math.round(this.$store.state.repositories[repos].overall[name] * ((100 - churn) / 100)) }} </td>
            </tr>
          </tbody>
        </v-table>
      </v-sheet>
    </v-main>
  </v-layout>
</template>

<script>
export default {
  data: () => ({
    repos: [],
    statistic: []
  }),
  created() {
    this.repos = this.$route.query.repoName;
  }
}
</script>
