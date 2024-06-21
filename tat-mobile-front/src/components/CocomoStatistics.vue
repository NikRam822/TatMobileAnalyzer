<template>
  <div class="d-flex flex-row justify-space-around">
    <v-sheet>
      Your team type:
      <v-select v-model="currentTeam" :items="teamType" variant="solo-inverted" hide-details="true"> </v-select>
    </v-sheet>
    <v-sheet>
      Lines of code:
      <v-text-field variant="solo-inverted" @input="validateInput" v-model="LOC"></v-text-field>
    </v-sheet>
  </div>
  <div class="d-flex flex-row justify-space-around">
    <v-sheet> Person-months </v-sheet>
    <v-sheet> Mouths </v-sheet>
    <v-sheet> Developers </v-sheet>
  </div>
</template>
<script>
export default {
  data() {
    return {
      teamType: ["Organic", "Semidetactch", "Embedded"],
      currentTeam: "Organic",
      LOC: null,
    };
  },
  methods: {
    validateInput(event) {
      const value = event.target.value;
      if (/^\d*$/.test(value)) {
        this.number = value;
      } else {
        event.target.value = this.number;
      }
    },
  },
  computed: {
    calculateLOC() {
      let totalValue = 0;
      for (let val of Object.values(
        this.$store.state.RepoSatistic[this.$store.state.currentRepo.projectLink].data.overall
      )) {
        totalValue += val;
      }
      totalValue = totalValue;
      return totalValue;
    },
  },
  mounted() {
    this.LOC = this.calculateLOC;
  },
};
</script>
