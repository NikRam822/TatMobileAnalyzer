<template>
  <v-progress-circular v-if="loader" indeterminate size="50" width="10" color="grey-darken-3"></v-progress-circular>
  <v-container>
    <h2>Churn</h2>
    <v-list v-for="val, item in result.churn">
      <v-list-item> {{ item }}: {{ Math.round(val) }}%</v-list-item>
    </v-list>
  </v-container>
  <v-container>
    <h2>Overall</h2>
    <v-list v-for="val, item in result.overall">
      <v-list-item> {{ item }}: {{ val }}</v-list-item>
    </v-list>
  </v-container>
</template>
<script>
export default {
  props: {
    result: [],
    loader: false
  }
}
</script>