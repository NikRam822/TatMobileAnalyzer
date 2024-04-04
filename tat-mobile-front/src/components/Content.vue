<template>
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
import { mapGetters } from 'vuex';

export default {
  computed: {
    ...mapGetters(['getResult']),
    result() {
      return this.getResult;
    }
  }
}
</script>